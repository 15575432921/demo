package persistent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopicPersistent {
    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "topic-persistent";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println(1);
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.setClientID("z3");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remake");
        //消费者
        connection.start();
        //通过监听的方式来消费消息
        Message message = topicSubscriber.receive();
        while (null != message && message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                System.out.println(textMessage.getText());
                message = topicSubscriber.receive(1000L);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        session.close();
        connection.close();
    }
}

