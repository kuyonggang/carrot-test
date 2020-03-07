package com.younger.test.jms.base.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class JmsProducer {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            /**
             * producer默认是异步发送消息。在没有开启事务的情况下，producer发送持久化消息是同步的，但默认情况下producer发送的信息是持久化消息。
             * 所以producer会同步发送持久化消息，此时producer会等待broker对这条消息的处理情况的反馈，所以发送是阻塞的，直到收到确认。同步发送肯定是有流量控制的。
             * 这样会影响producer的发送性能，可以设置producer发送持久消息为异步方式。异步发送不会等待broker的确认，但是如果broker内存限制被超过了，将不会被通知,
             * 所以就需要考虑流量控制了，需要设置回执窗口大小ProducerWindowSize
             * ProducerWindowSize含义：producer每发送一个消息，统计一下发送的字节数，当字节数达到ProducerWindowSize值时，需要等待broker的确认，才能继续发送。
             */
            activeMQConnectionFactory.setUseAsyncSend(true);
            activeMQConnectionFactory.setProducerWindowSize(200000);

            connection = activeMQConnectionFactory.createConnection();
            connection.start();

            /**
             * 如果设置Boolean.TRUE 代表事务性的会话 此时第二个参数值被忽略(发送方和接收方)。acknowledgment mode被jms服务器设置为SESSION_TRANSACTED
             * 如果设置Boolean.FALSE 代表非事务性的会话 测试发现第二个参数值在消息发送方不起作用；在消息接收方可以起作用
             */
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("first-queue");
            MessageProducer producer = session.createProducer(destination);
            for (int i=0;i<1;i++){
                TextMessage textMessage = session.createTextMessage("你好，ActiveMQ" + i);
                textMessage.setStringProperty("name","张三");
                /**
                 * 默认情况下producer发送的信息是持久化消息,可以通过下面的代码设置消息为非持久化的
                 * 非持久化模式下，默认就是异步发送过程
                 */
//                textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
//                或者设置：producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                producer.send(textMessage);
            }

            //事务性会话需要手动commit才可以将消息发出
            session.commit();
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
