package com.daiyanping.cms;

import com.daiyanping.cms.batchSubmission.AppConfig;
import com.daiyanping.cms.batchSubmission.BathSubmissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
public class BathSubmitTests {

	@Autowired
	private BathSubmissionService bathSubmissionService;

	private CountDownLatch countDownLatch = new CountDownLatch(1000);

	@Test
	public void test() {
		for (int i = 0; i < 1000; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long count = countDownLatch.getCount();
					try {
						countDownLatch.countDown();
						countDownLatch.await();
						System.out.println("线程开始执行：" + count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Long id = count;
					int i1 = id.intValue();
					Integer id2 = i1;
					Map<String, Object> user = bathSubmissionService.getUser(id2);
				}
			});

			thread.start();


		}

		try {
			Thread.sleep(1000 * 60 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
