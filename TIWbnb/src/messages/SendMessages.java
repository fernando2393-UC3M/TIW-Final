package messages;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

public class SendMessages {
	
	/* Send a message from an admin to an user
	 * Receives Sender and Receiver Users
	 * Needs Connection Factory and Queue
	 */
	public static void sendMessage(int sender, int receiver, String content, ConnectionFactory cf, Queue queue) throws JMSException{
		Connection _connection = null;
		Session _session;

		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			MessageProducer producer = _session.createProducer(queue);

			Message message = _session.createTextMessage(content);
			message.setStringProperty("sentTo", ""+receiver);
			message.setStringProperty("sentBy", ""+sender);
			message.setStringProperty("admin", "no");
			
			producer.send(message);	
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (_connection != null) {
            	_connection.close();
            }
        }
	}	
	
	/* Send a message to an admin
	 * Receives Admin, User
	 * Needs Connection Factory and Queue
	 */
	public static void sendMessageAdmin(int admin, int user, String content, ConnectionFactory cf, Queue queue) throws JMSException{
		Connection _connection = null;
		Session _session;

		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			MessageProducer producer = _session.createProducer(queue);

			Message message = _session.createTextMessage(content);
			message.setStringProperty("sentTo", ""+admin);
			message.setStringProperty("sentBy", ""+user);
			message.setStringProperty("admin", "toAdmin");
			
			producer.send(message);	
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (_connection != null) {
            	_connection.close();
            }
        }
	}

	/* Send a message with a booking request to another user
	 * Receives Sender and Receiver Users
	 * Needs Queue and QueueSession
	 */
	public static void sendTransaction(int sender, int receiver, String content, Queue queue, Session session) throws JMSException{

		MessageProducer producer = session.createProducer(queue);

		Message message = session.createTextMessage(content);
		message.setStringProperty("sentTo", ""+receiver);
		message.setStringProperty("sentBy", ""+sender);
		message.setStringProperty("admin", "booking");
		
		producer.send(message);
	}	
}
