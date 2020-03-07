package com.younger.test.jms.base.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer1 {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();

            //创建事务性的会话 Boolean.TRUE
            Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
            Destination destination = session.createQueue("first-queue");
            MessageConsumer consumer = session.createConsumer(destination);

            for(int i=0;i<8;i++){
                TextMessage message = (TextMessage) consumer.receive();
                System.out.println(message.getText());
                //前5条已经接受的消息将会被确认
                if(i == 5){
                    message.acknowledge();
                }
            }
            //事务性会话需要手动commit才可以将消息从队列中清除
//            session.commit();
//            session.close();
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
