package com.younger.test.jms.base.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsTopicConsumer1 {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();

            //创建事务性的会话 Boolean.TRUE
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //通过topic创建消费者来实现消息的订阅
            Destination destination = session.createTopic("first-topic");
            MessageConsumer consumer = session.createConsumer(destination);

            for(int i=0;i<10;i++){
                TextMessage message = (TextMessage) consumer.receive();
                System.out.println(message.getText());
            }

            //事务性会话需要手动commit才可以将消息从队列中清除
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
