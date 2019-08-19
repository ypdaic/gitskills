package com.daiyanping.cms.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import static org.apache.commons.codec.Charsets.UTF_8;

/**
 * @ClassName MyMessageListener
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-05-27
 * @Version 0.1
 */
//@Component
public class MyMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        String s = new String(body, UTF_8);
        System.out.println(s);
    }
}
