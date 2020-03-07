package com.younger.test.jms.base.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class JmsTopicProducer {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            //创建事务性的会话 Boolean.TRUE
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //创建topic实现消息的发布
            Destination destination = session.createTopic("first-topic");
            MessageProducer producer = session.createProducer(destination);
            for (int i=0;i<10;i++){
                TextMessage textMessage = session.createTextMessage("你好，ActiveMQ" + i);
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
