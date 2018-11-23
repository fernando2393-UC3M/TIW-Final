package main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Admin;
import model.MessagesAdmin;
import model.User;

public class ReadMessages {
	
	/* Obtains all messages for an admin
	 * Gets all from JMS queue plus those stores in DB
	 */
	@SuppressWarnings("null")
	public static List<MessagesAdmin> getMessages(int adminId, EntityManager em, ConnectionFactory cf, Queue adminQueue) throws JMSException{
		List<MessagesAdmin> ret = new ArrayList<MessagesAdmin>();
		
		/* Obtain messages from Queue */
		List<Message> queueResults = getMessagesFromQueue(cf, adminQueue, adminId);
		
		/* Convert Messages into Entity Instances */
		for(Message msg: queueResults){
			// Use the message
			TextMessage aux = (TextMessage) msg;
			int sender = Integer.parseInt(aux.getStringProperty("sentBy"));
			
			Admin aux1 = em.find(Admin.class, adminId);
			User aux2 = em.find(User.class, sender);
			ret.add( convertFormat(aux1, aux2, aux) );			
		}
		
		/* Query messages from DB */
		Query query = em.createQuery(
			      "SELECT a "
			      + " FROM MessagesAdmin a " +
			      " WHERE a.messageId = :p");
		@SuppressWarnings("rawtypes")
		List results = query.setParameter("p", adminId).getResultList();
		
		/* Insert JMS Messages into DB if there are any */
		if(ret.size() > 0){
			insertMessages(ret, em);
		}
		
		/* Add messages from DB */
		if(results != null){
			for(Object obj: results){
				ret.add((MessagesAdmin) obj);				
			}
		}
		
		return ret;
	}
	
	/* Checks the JMS Queue for messages */
	public static List<Message> getMessagesFromQueue(ConnectionFactory cf, Queue adminQueue, int adminId){
		Connection _connection;
		Session _session;
		
		List<Message> ret = new ArrayList<Message>();
		Message message = null;
		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			String selector = "sentTo = '" + adminId + "' AND admin = 'toAdmin'";
			MessageConsumer consumer = _session.createConsumer(adminQueue, selector);
			
			_connection.start();
			/* Loop and receive all messages available 
			 * Used this approach as messages will only be received when loading the page
			 */
			message = consumer.receive(500);
			while(message != null){
				ret.add(message);
				message = consumer.receive(500);
				
			}		
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/* Converts a JMS Message into an entity instance */
	public static MessagesAdmin convertFormat(Admin admin, User user, TextMessage msg) throws JMSException{	
		
		MessagesAdmin ret = new MessagesAdmin();
		
		ret.setAdmin(admin);
		ret.setUser(user);
		ret.setMessageContent(msg.getText());
		ret.setMessageDate(new Date(msg.getJMSTimestamp()));
		ret.setMessageFromAdmin(false);
		ret.setMessageRead(false);
		  
		return ret;
	}
	
	/* Sets all unread messages to read in DB
	 * Assumes a transaction commit afterwards
	 */
	public static void setRead(int adminId, EntityManager em){		
		Admin admin = em.find(Admin.class, adminId);
		
		Query query = em.createQuery(
			      "UPDATE MessagesAdmin m "
			      + " SET m.messageRead = :b " +
			      " WHERE m.admin = :p"
			      + " AND m.messageFromAdmin = :f ");
		query.setParameter("b", true).setParameter("p", admin).setParameter("f", false).executeUpdate();
	}
	
	/* Inserts messages into DB
	 * Assumes a transaction commit afterwards
	 */
	public static void insertMessages(List<MessagesAdmin> messages, EntityManager em){
		for(MessagesAdmin obj: messages){
			em.persist(obj);
		}
	}
	
	

}
