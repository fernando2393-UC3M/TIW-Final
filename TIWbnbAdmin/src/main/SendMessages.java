package main;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import model.Admin;
import model.User;

public class SendMessages {
	
	/* Send a message to an user from an admin
	 * Receives Admin, User
	 * Needs Connection Factory and Queue
	 */
	public static void sendMessage(Admin admin, User user, String content, ConnectionFactory cf, Queue adminQueue) throws JMSException{
		Connection _connection = null;
		Session _session;
		

		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			MessageProducer producer = _session.createProducer(adminQueue);
			

			Message message = _session.createTextMessage(content);
			message.setStringProperty("sentTo", ""+user.getUserId());
			message.setStringProperty("sentBy", ""+admin.getAdminId());
			message.setStringProperty("admin", "fromAdmin");
			
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
}
