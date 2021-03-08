package persistent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducePersistent {
    public static final String ACTIVEMQ_URL="tcp://localhost:61616";
    public static final String QUEUE_NAME="queue01";
    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        //生产者
        MessageProducer producer = session.createProducer(queue);
        //设置是否持久化
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("msg----" + i);

            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("end");

    }
}
