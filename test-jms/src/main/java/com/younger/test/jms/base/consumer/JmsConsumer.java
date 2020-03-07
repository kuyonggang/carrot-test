package com.younger.test.jms.base.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 在一个JMS客户端，可以使用本地事务来组合消息的发送和接收。JMS Session 接口提供了commit和rollback方法。
 * JMS Provider会缓存每个生产者当前生产的所有消息，直到commit或者rollback，commit操作将会导致事务中所有的消息被持久存储；rollback意味着JMS Provider将会清除此事务下所有的消息记录。
 * 在事务未提交之前，消息是不会被持久化存储的，也不会被消费者消费
 * 事务提交意味着生产的所有消息都被发送。消费的所有消息都被确认；
 * 事务回滚意味着生产的所有消息被销毁，消费的所有消息被恢复，也就是下次仍然能够接收到发送端的消息，除非消息已经过期了
 */
public class JmsConsumer {

    private static final String brokerURL = "tcp://192.168.26.118:61616";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();

            //创建事务性的会话 Boolean.TRUE
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //创建非事务性的会话 Boolean.FALSE
            /**
             * 当客户端成功从recive方法返回以后，或者[MessageListener.onMessage] 方法成功返回以后，会话会自动确认该消息
             * 不需要调用 session.commit()
             */
//            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            /**
             * 客户端通过调用消息的textMessage.acknowledge();确认消息。
             * 在这种模式中，如果一个消息消费者消费一共是10个消息，那么消费了5个消息，然后在第5个消息通过textMessage.acknowledge()，那么之前的所有消息都会被消确认
             */
//            Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
            /**
             * 延迟确认消息
             * 允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效。
             */
//            Session session = connection.createSession(Boolean.FALSE, Session.DUPS_OK_ACKNOWLEDGE);
//            Session session = connection.createSession(Boolean.FALSE, Session.SESSION_TRANSACTED);
            Destination destination = session.createQueue("first-queue");
            MessageConsumer consumer = session.createConsumer(destination);

//            consumer.setMessageListener(new MessageListener() {
//                public void onMessage(Message message) {
//                    try {
//                        System.out.println(((TextMessage)message).getText());
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            for(int i=0;i<1;i++){
                TextMessage message = (TextMessage) consumer.receive();
                System.out.println(message.getText() + " === " + message.getStringProperty("name"));
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
