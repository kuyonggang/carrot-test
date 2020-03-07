package com.younger.test.jms.base.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 非持久订阅：如果订阅者中断一段时间后再重启，期间发布的消息将丢失
 * 持久订阅：如果订阅者中断一段时间后再重启，期间发布的消息依然可以接收到
 *
 * 当所有的消息必须接收的时候，则需要用到持久订阅。反之，则用非持久订阅
 */
public class JmsTopicPersistentConsumer {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try{
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = activeMQConnectionFactory.createConnection();
            connection.setClientID("DUBBO-ORDER");//设置持久订阅
            connection.start();

            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("first-topic");
            MessageConsumer consumer = session.createDurableSubscriber(topic,"DUBBO-ORDER");

            for (int i=0;i<10;i++){
                TextMessage textMessage = (TextMessage) consumer.receive();
                System.out.println("subscribe msg:" + textMessage.getText());
            }

            session.commit();
            session.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
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
