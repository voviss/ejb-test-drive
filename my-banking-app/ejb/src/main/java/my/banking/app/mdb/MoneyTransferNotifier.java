package my.banking.app.mdb;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@ApplicationScoped
public class MoneyTransferNotifier {
	
	private static final Logger log = Logger.getLogger(MoneyTransferNotifier.class.getName());
	
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/moneyTransferNotificationQueue")
    private Queue queue;
    
    public void sendNotification(String content){
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();
            TextMessage message = session.createTextMessage();
            message.setText(content);
            messageProducer.send(message);
        } catch (JMSException e) {
        	log.severe(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
