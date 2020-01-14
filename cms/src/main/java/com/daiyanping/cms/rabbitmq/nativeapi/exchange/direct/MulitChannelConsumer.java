package com.daiyanping.cms.rabbitmq.nativeapi.exchange.direct;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：一个连接多个信道，一个队列多个消费者
 */
public class MulitChannelConsumer {

    public final static String EXCHANGE_NAME = "direct_logs";

    @AllArgsConstructor
    private static class ConsumerWorker implements Runnable {

        final Connection connection;

        final String queueName;

        /**
         * 一个线程一个信道
         */
        @Override
        public void run() {
            try {
                /*创建一个信道，意味着每个线程单独一个信道*/
                final Channel channel = connection.createChannel();
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME,
                        "direct");
                String factQueueName = queueName;
                String consumerName = "";
                if (null == factQueueName) {

                    // 声明一个随机队列
                    factQueueName = channel.queueDeclare().getQueue();
                    //消费者名字，打印输出用
                    consumerName = Thread.currentThread().getName() +"-single";
                } else {
                    // 声明一个队列
                    channel.queueDeclare(factQueueName, false, false, false, null);
                    //消费者名字，打印输出用
                    consumerName = Thread.currentThread().getName() +"-all";
                }

                //所有日志严重性级别
                String[] severities={"error","info","warn"};
                for (String severity : severities) {
                    //关注所有级别的日志（多重绑定）
                    channel.queueBind(factQueueName,
                            DirectProducer.EXCHANGE_NAME, severity);
                }
                System.out.println("["+consumerName+"] Waiting for messages:");

                final String consumerName2 = consumerName;
                // 创建队列消费者
                final Consumer consumerA = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties
                                                       properties,
                                               byte[] body)
                            throws IOException {
                        String message =
                                new String(body, "UTF-8");
                        System.out.println(consumerName2 +
                                " Received "  + envelope.getRoutingKey()
                                + ":'" + message + "'");
                    }
                };
                channel.basicConsume(factQueueName, true, consumerA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] argv) throws IOException,
                InterruptedException, TimeoutException {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("192.168.111.128");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("root");
            connectionFactory.setPassword("test1234");

            // 打开连接和创建频道，与发送端一样
            Connection connection = connectionFactory.newConnection();
            //一个连接多个信道
            for(int i=0;i<2;i++){
                /*将连接作为参数，传递给每个线程*/
                Thread worker =new Thread(new ConsumerWorker(connection, null));
                worker.start();
            }

            String queueName = "focusAll";

            //3个线程，线程之间共享队列，一个队列多个消费者，消息在多个消费者轮训
            for(int i=0;i<3;i++){
                /*将连接作为参数，传递给每个线程*/
                Thread worker =new Thread(new ConsumerWorker(connection, queueName));
                worker.start();
            }
        }
    }
}
