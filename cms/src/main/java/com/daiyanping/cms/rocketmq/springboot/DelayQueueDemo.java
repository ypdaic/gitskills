package com.daiyanping.cms.rocketmq.springboot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueDemo {
    public static void main(String[] args) {
        DelayQueue<DelayTask> queue = new DelayQueue<DelayTask>();
        //TODO 放数据
        queue.add(new DelayTask("order1", new Date()));//第一个元素，时间放当前时间（放进去就可以出来）。
        //第2个元素，时间放当前时间+5秒（放进去5秒就可以出来）。
        queue.add(new DelayTask("order2-5s", new Date(System.currentTimeMillis()+5000)));
        //第3个元素，时间放当前时间+10秒（放进去10秒就可以出来）。
        queue.add(new DelayTask("order3-10s", new Date(System.currentTimeMillis()+10000)));

        System.out.println("queue put done");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        //TODO 无限循环。取数据
        while(!queue.isEmpty()) {
            try {
                DelayTask task = queue.take();
                System.out.println(task.name + ":" +df.format(new Date(System.currentTimeMillis())) );

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //存放到DelayDeque的元素必须实现Delayed接口
    static class DelayTask implements Delayed {
        private String name;
        private Date taskTime;

        public DelayTask(String name, Date  taskTime) {
            this.name = name;
            this.taskTime = taskTime;
        }
        //TODO 延迟任务是否到时
        public long getDelay(TimeUnit unit) {
            return unit.convert(taskTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
        //TODO 排序
        public int compareTo(Delayed o) {
            DelayTask delayTask = (DelayTask) o;
            long diff = taskTime.getTime() - delayTask.getTaskTime().getTime();
            if (diff > 0) {
                return 1;
            } else if (diff == 0) {
                return 0;
            } else {
                return -1;
            }
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(Date taskTime) {
            this.taskTime = taskTime;
        }
    }
}
