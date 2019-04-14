package com.daiyanping.cms.batchSubmission;

import com.daiyanping.cms.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class BathSubmissionService {

	//创建一个定时任务线程池
	ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	private LinkedBlockingQueue<Request> linkedBlockingQueue = new LinkedBlockingQueue<Request>();

	@Autowired
	private RemoteService remoteService;

	public Map<String, Object> getUser(Integer userId) {
		Request request = new Request();
		request.setUserId(userId);
		CompletableFuture<Map<String, Object>> mapCompletableFuture = new CompletableFuture<>();
		request.setCompletableFuture(mapCompletableFuture);
		linkedBlockingQueue.offer(request);
		Map<String, Object> result = null;
		try {
			//阻塞在这里,只有调用CompletableFuture的complete方法才会返回结果
			result = mapCompletableFuture.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Data
	private class Request {
		private Integer userId;
		private CompletableFuture<Map<String, Object>> completableFuture;

	}

	//被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Serclet的inti()方法。被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
	@PostConstruct
	public void init() {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (linkedBlockingQueue.size() == 0) {
					return;
				}

				ArrayList<Integer> ids = new ArrayList<>();
				ArrayList<BathSubmissionService.Request> requests = new ArrayList<BathSubmissionService.Request>();
				linkedBlockingQueue.forEach(Request->{
					BathSubmissionService.Request request = linkedBlockingQueue.poll();
					requests.add(request);
					ids.add(request.userId);
				});

				List<Map<String, Object>> users = remoteService.getUsers(ids);
				System.out.println("批量查询的结果：" + users.size());
				HashMap<String, Object> map1 = new HashMap<>();
				users.forEach(map ->{
					Integer id = (Integer) map.get("id");
					map1.put(id.toString(), map);
				});

				requests.forEach(request -> {
					Integer userId = request.getUserId();
					CompletableFuture<Map<String, Object>> completableFuture = request.getCompletableFuture();
					Object result = map1.get(userId.toString());
					completableFuture.complete((Map<String, Object>) result);
				});


			}
		});

		//创建一个定时任务，第一次执行在10ms后，后面就按10ms的周期执行，如果某次执行超过10ms则往后推迟
		//执行周期是不变的
		scheduledExecutorService.scheduleAtFixedRate(thread, 10, 10 , TimeUnit.MILLISECONDS);
	}

	@PreDestroy
	public void destory() {
		scheduledExecutorService.shutdown();
	}

}
