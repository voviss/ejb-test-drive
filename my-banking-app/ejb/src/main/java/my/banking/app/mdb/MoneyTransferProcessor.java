package my.banking.app.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/moneyTransferNotificationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })

public class MoneyTransferProcessor implements MessageListener {

	private static final Logger log = Logger.getLogger(MoneyTransferProcessor.class.getName());
	
	public void onMessage(Message msg) {
        try {
            if (msg instanceof TextMessage) {
                log.info("Sending mail to a client regarding the money transfer:\nContent: " + ((TextMessage) msg).getText());
            } else {
            	log.warning("Message of wrong type: " + msg.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
	}

}