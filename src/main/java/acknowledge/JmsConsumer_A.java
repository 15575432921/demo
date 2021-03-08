package acknowledge;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_A {
    public static final String ACTIVEMQ_URL="tcp://localhost:61616";
    public static final String QUEUE_NAME="queue01";
    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("1");
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //事务 >签收
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        //消费者
        MessageConsumer consumer = session.createConsumer(queue);
//        while(true){
//            TextMessage receive = (TextMessage)consumer.receive();
//            if(receive!=null){
//                System.out.println(receive.getText());
//            }
//            else
//                break;
//        }
//

        //通过监听的方式来消费消息
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if(null!=message&&message instanceof TextMessage)
                {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println(textMessage.getText());
                        //手动签收
                        textMessage.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
