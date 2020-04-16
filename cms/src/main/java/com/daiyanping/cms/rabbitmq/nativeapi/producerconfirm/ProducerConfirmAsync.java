package com.daiyanping.cms.rabbitmq.nativeapi.producerconfirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *类说明：异步确认，什么时候确认由rabbitmq自己决定
 *
 * ----------------------------------------------------
 *  Sent Message: [error]:'Hello World_1_1587047108054'
 * ----------------------------------------------------
 *  Sent Message: [warning]:'Hello World_2_1587047108067'
 * ----------------------------------------------------
 *  Sent Message: [error]:'Hello World_3_1587047108068'
 * ----------------------------------------------------
 *  Sent Message: [warning]:'Hello World_4_1587047108069'
 * ----------------------------------------------------
 *  Sent Message: [error]:'Hello World_5_1587047108070'
 * ----------------------------------------------------
 *  Sent Message: [warning]:'Hello World_6_1587047108071'
 * ----------------------------------------------------
 *  Sent Message: [error]:'Hello World_7_1587047108072'
 * ----------------------------------------------------
 *  Sent Message: [warning]:'Hello World_8_1587047108073'
 * ----------------------------------------------------
 *  Sent Message: [error]:'Hello World_9_1587047108074'
 * ----------------------------------------------------
 *  Sent Message: [warning]:'Hello World_10_1587047108075'
 * RabbitMq路由失败:  warning.Hello World_2_1587047108067
 * 消费者确认成功结果，deliveryTag：2 确认模式 multiple：false
 * RabbitMq路由失败:  warning.Hello World_4_1587047108069
 * 消费者确认成功结果，deliveryTag：4 确认模式 multiple：false
 * 消费者确认成功结果，deliveryTag：3 确认模式 multiple：true
 * RabbitMq路由失败:  warning.Hello World_6_1587047108071
 * 消费者确认成功结果，deliveryTag：6 确认模式 multiple：false
 * RabbitMq路由失败:  warning.Hello World_8_1587047108073
 * 消费者确认成功结果，deliveryTag：8 确认模式 multiple：false
 * RabbitMq路由失败:  warning.Hello World_10_1587047108075
 * 消费者确认成功结果，deliveryTag：10 确认模式 multiple：false
 * 消费者确认成功结果，deliveryTag：9 确认模式 multiple：true
 *
 *
 * 从上面的打印结果可以看出，结合channel.waitForConfirms() 也就一条条确认
 * 可以看出，消息只要成功发送到交换器，就会发送ack通知
 *
 *
 * 如果消息发送的时候随便指定一个不存在的交换器，发送者通知是否有效呢?
 * 答案是在这种情况下发送者通知是无效的，但是会抛channel关闭的异常出来，
 * 如果有给channel.addShutdownListener 添加监听器则会回调
 */
public class ProducerConfirmAsync {

    public final static String EXCHANGE_NAME = "producer_async_confirm";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();

        // 设置MabbitMQ所在主机ip或者主机名
        factory.setHost("192.168.111.128");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("test1234");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();
        // 指定转发
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 启用发送者确认模式
        channel.confirmSelect();

        // 异步确认
        channel.addConfirmListener(new ConfirmListener() {
            /**
             *
             * @param deliveryTag 消息标示
             * @param multiple true表示 批量确认，false 表示一条一条确认
             * @throws IOException
             */
            public void handleAck(long deliveryTag, boolean multiple)
                    throws IOException {

                System.out.println("消费者确认成功结果，deliveryTag：" + deliveryTag + " 确认模式 multiple：" + multiple);
            }

            public void handleNack(long deliveryTag, boolean multiple)
                    throws IOException {
                System.out.println("消费者确认失败结果，deliveryTag：" + deliveryTag + " 确认模式 multiple：" + multiple);
            }
        });

        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties,
                                     byte[] body)
                    throws IOException {
                String message = new String(body);
                System.out.println("RabbitMq路由失败:  "+routingKey+"."+message);
            }
        });

        String[] severities={"error","warning"};
        for(int i=0;i<10;i++){
            String severity = severities[i%2];
            // 发送的消息
            String message = "Hello World_"+(i+1)+("_"+System.currentTimeMillis());
            channel.basicPublish(EXCHANGE_NAME, severity, true,
                    MessageProperties.PERSISTENT_BASIC, message.getBytes());
            System.out.println("----------------------------------------------------");
            System.out.println(" Sent Message: [" + severity +"]:'"+ message + "'");
        }

        // 关闭频道和连接
        //channel.close();
        //connection.close();
    }


}
