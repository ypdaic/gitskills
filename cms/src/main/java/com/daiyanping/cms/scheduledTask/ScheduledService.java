package com.daiyanping.cms.scheduledTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ScheduledService
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
@Service
@EnableScheduling
public class ScheduledService {

    @Autowired
    private RedisTemplate redisTemplate;

    public static  final String SESSION_OVERVIEW = "session_overview:%s";

    /**
     * 每天凌晨执行
     */
    @Scheduled(cron = "0 54 16 * * ?")
    public void initSessionOverview() {
        System.out.println(Thread.currentThread().getName());
        Set keys = redisTemplate.keys(ScheduledService.SESSION_OVERVIEW.replace("%s", "*"));

        if (CollectionUtils.isNotEmpty(keys)) {

            redisTemplate.delete(keys);
        }
    }

    @EventListener(classes = SessionOverviewEvent.class)
    public void updateAndSaveSessionOverview(SessionOverviewEvent sessionOverviewEvent) {
        SessionOverview sessionOverview = sessionOverviewEvent.getSessionOverview();
        BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(String.format(SESSION_OVERVIEW, sessionOverview.getSkillGroups()));
        SessionOverview sessionOverview1 = (SessionOverview) boundValueOperations.get();
        if (sessionOverview1 == null) {
            boundValueOperations.set(sessionOverview);
        } else {
            sessionOverview1.setLeaveMessageCount(sessionOverview1.getLeaveMessageCount() + sessionOverview.getLeaveMessageCount());
            sessionOverview1.setAllocatedSeatsCount(sessionOverview1.getAllocatedSeatsCount() + sessionOverview.getAllocatedSeatsCount());
            sessionOverview1.setLeaveSessionCount(sessionOverview1.getLeaveSessionCount() + sessionOverview1.getLeaveSessionCount());
            sessionOverview1.setQuitQueueCount(sessionOverview1.getQuitQueueCount() + sessionOverview.getQuitQueueCount());
            sessionOverview1.setSessionTotal(sessionOverview1.getLeaveSessionCount() + sessionOverview1.getAllocatedSeatsCount() + sessionOverview1.getQuitQueueCount());
            boundValueOperations.set(sessionOverview1);
        }
    }

    public JSONObject getSessonOverview() {
        Set keys = redisTemplate.keys(SESSION_OVERVIEW.replace("%s", "*"));
        JSONObject overView = new JSONObject();
        JSONObject sessionTotalStatistical = new JSONObject();
        Integer sessionTotalForAll = 0;
        Integer quitQueueCountForAll = 0;
        Integer leaveSessionCountForAll = 0;
        Integer allocatedSeatsCountForAll = 0;
        Integer leaveMessageCountAll = 0;

        Set<Integer> longs = new HashSet<Integer>();
        HashMap<Integer, JSONObject> integerJSONObjectHashMap = new HashMap<>();

        //技能组
        ArrayList<SessionOverview> sessionOverviews = new ArrayList<SessionOverview>();
        if (CollectionUtils.isNotEmpty(keys)) {

            for (Object key : keys) {

                SessionOverview sessionOverview = (SessionOverview) redisTemplate.boundValueOps(key.toString()).get();
                sessionTotalForAll += sessionOverview.getSessionTotal();
                quitQueueCountForAll += sessionOverview.getQuitQueueCount();
                leaveSessionCountForAll += sessionOverview.getLeaveSessionCount();
                allocatedSeatsCountForAll += sessionOverview.getAllocatedSeatsCount();
                leaveMessageCountAll += sessionOverview.getLeaveSessionCount();
                if (longs.contains(sessionOverview.getChannel())) {
                    JSONObject jsonObject = integerJSONObjectHashMap.get(sessionOverview.getChannel());
                    Integer sessionTotalForChannel = jsonObject.getInteger("sessionTotalForChannel") + sessionOverview.getSessionTotal();
                    Integer quitQueueCountForChannel = jsonObject.getInteger("quitQueueCountForChannel") + sessionOverview.getQuitQueueCount();
                    Integer LeaveSessionCountForChannel = jsonObject.getInteger("LeaveSessionCountForChannel") + sessionOverview.getLeaveSessionCount();
                    Integer allocatedSeatsCountForChannel = jsonObject.getInteger("allocatedSeatsCountForChannel") + sessionOverview.getAllocatedSeatsCount();
                    Integer leaveMessageCountForChannel = jsonObject.getInteger("leaveMessageCountForChannel") + sessionOverview.getLeaveMessageCount();
                    jsonObject.put("sessionTotalForChannel", sessionTotalForChannel);
                    jsonObject.put("quitQueueCountForChannel", quitQueueCountForChannel);
                    jsonObject.put("LeaveSessionCountForChannel", LeaveSessionCountForChannel);
                    jsonObject.put("allocatedSeatsCountForChannel", allocatedSeatsCountForChannel);
                    jsonObject.put("leaveMessageCountForChannel", leaveMessageCountForChannel);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sessionTotalForChannel", sessionOverview.getSessionTotal());
                    jsonObject.put("quitQueueCountForChannel", sessionOverview.getQuitQueueCount());
                    jsonObject.put("LeaveSessionCountForChannel", sessionOverview.getLeaveSessionCount());
                    jsonObject.put("allocatedSeatsCountForChannel", sessionOverview.getAllocatedSeatsCount());
                    jsonObject.put("leaveMessageCountForChannel", sessionOverview.getLeaveMessageCount());
                    integerJSONObjectHashMap.put(sessionOverview.getChannel(), jsonObject);
                    longs.add(sessionOverview.getChannel());
                }

                sessionOverviews.add(sessionOverview);
            }
        }

        sessionTotalStatistical.put("sessionTotalForAll", sessionTotalForAll);
        sessionTotalStatistical.put("quitQueueCountForAll", quitQueueCountForAll);
        sessionTotalStatistical.put("leaveSessionCountForAll", leaveSessionCountForAll);
        sessionTotalStatistical.put("allocatedSeatsCountForAll", allocatedSeatsCountForAll);
        sessionTotalStatistical.put("leaveMessageCountAll", leaveMessageCountAll);
        overView.put("overallStatisticsToday", sessionTotalStatistical);
        overView.put("channelStatisticsToday", integerJSONObjectHashMap);
        overView.put("skillGroupsStatisticsToday", sessionOverviews);

        return overView;
    }

}
