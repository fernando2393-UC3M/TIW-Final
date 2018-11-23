package messages;

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
	
	/* Obtains all messages for a user from another user
	 * Gets all from JMS queue plus those stores in DB
	 */
	public static List<model.Message> getMessages(int userId, EntityManager em, ConnectionFactory cf, Queue queue) throws JMSException{
		List<model.Message> ret = new ArrayList<model.Message>();
		
		/* Obtain messages from Queue */
		List<Message> queueResults = getMessagesFromQueue(cf, queue, userId);

		User userReceiver = em.find(User.class, userId);
		
		/* Convert Messages into Entity Instances */
		for(Message msg: queueResults){
			// Use the message
			TextMessage aux = (TextMessage) msg;
			int sender = Integer.parseInt(aux.getStringProperty("sentBy"));
			
			User userSender = em.find(User.class, sender);
			ret.add( convertFormat(userSender, userReceiver, aux) );			
		}
		
		/* Query messages from DB */
		Query query = em.createQuery(
			      "SELECT a "
			      + " FROM Message a " +
			      " WHERE a.user2 = :p");
		@SuppressWarnings("rawtypes")
		List results = query.setParameter("p", userReceiver).getResultList();
		
		/* Insert JMS Messages into DB if there are any */
		if(ret.size() > 0){
			insertMessages(ret, em);
		}
		
		/* Add messages from DB */
		if(results != null){
			for(Object obj: results){
				ret.add((model.Message) obj);				
			}
		}
		
		return ret;
	}
	
	/* Obtains all messages for a user from an admin
	 * Gets all from JMS queue plus those stores in DB
	 */
	public static List<MessagesAdmin> getMessagesAdmin(int userId, EntityManager em, ConnectionFactory cf, Queue queue) throws JMSException{
		List<MessagesAdmin> ret = new ArrayList<MessagesAdmin>();
		
		/* Obtain messages from Queue */
		List<Message> queueResults = getMessagesFromQueue(cf, queue, userId);

		User userReceive = em.find(User.class, userId);
		
		/* Convert Messages into Entity Instances */
		for(Message msg: queueResults){
			// Use the message
			TextMessage aux = (TextMessage) msg;
			int sender = Integer.parseInt(aux.getStringProperty("sentBy"));
			
			Admin aux1 = em.find(Admin.class, sender);
			ret.add( convertFormatAdmin(aux1, userReceive, aux) );			
		}
		
		/* Query messages from DB */
		Query query = em.createQuery(
			      "SELECT a "
			      + " FROM MessagesAdmin a " +
			      " WHERE a.user = :p");
		@SuppressWarnings("rawtypes")
		List results = query.setParameter("p", userReceive).getResultList();
		
		/* Insert JMS Messages into DB if there are any */
		if(ret.size() > 0){
			insertMessagesAdmin(ret, em);
		}
		
		/* Add messages from DB */
		if(results != null){
			for(Object obj: results){
				ret.add((MessagesAdmin) obj);				
			}
		}
		
		return ret;
	}

	/* Checks the JMS Queue for user messages */
	public static List<Message> getMessagesFromQueue(ConnectionFactory cf, Queue queue, int userId){
		Connection _connection;
		Session _session;
		
		List<Message> ret = new ArrayList<Message>();
		Message message = null;
		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			String selector = "sentTo = '" + userId + "' AND admin = 'no'";
			MessageConsumer consumer = _session.createConsumer(queue, selector);
			
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
	
	/* Checks the JMS Queue for admin messages */
	public static List<Message> getAdminMessagesFromQueue(ConnectionFactory cf, Queue queue, int userId){
		Connection _connection;
		Session _session;
		
		List<Message> ret = new ArrayList<Message>();
		Message message = null;
		try {
			_connection = cf.createConnection();
			_session= _connection.createSession(false, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

			String selector = "sentTo = '" + userId + "' AND admin = 'fromAdmin'";
			MessageConsumer consumer = _session.createConsumer(queue, selector);
			
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
	
	/* Converts a JMS Message into an entity instance Message */
	public static model.Message convertFormat(User sender, User receiver, TextMessage msg) throws JMSException{	
		
		model.Message ret = new model.Message();
		
		ret.setUser1(sender);
		ret.setUser2(receiver);
		ret.setMessageContent(msg.getText());
		ret.setMessageDate(new Date(msg.getJMSTimestamp()));
		ret.setMessageRead(false);
		  
		return ret;
	}
	
	/* Converts a JMS Message into an entity instance MessagesAdmin */
	public static MessagesAdmin convertFormatAdmin(Admin admin, User user, TextMessage msg) throws JMSException{	
		
		MessagesAdmin ret = new MessagesAdmin();
		
		ret.setAdmin(admin);
		ret.setUser(user);
		ret.setMessageContent(msg.getText());
		ret.setMessageDate(new Date(msg.getJMSTimestamp()));
		ret.setMessageFromAdmin(true);
		ret.setMessageRead(false);
		  
		return ret;
	}
	
	/* Sets all unread messages to read in DB
	 * Assumes a transaction commit afterwards
	 */
	public static void setRead(int userId, EntityManager em){		
		User user = em.find(User.class, userId);
		
		Query query = em.createQuery(
			      "UPDATE Message m "
			      + " SET m.messageRead = :b " +
			      " WHERE m.user2 = :p");
		query.setParameter("b", true).setParameter("p", user).executeUpdate();
	}
	
	/* Sets all unread admin messages to read in DB
	 * Assumes a transaction commit afterwards
	 */
	public static void setReadAdmin(int userId, EntityManager em){		
		User user = em.find(User.class, userId);
		
		Query query = em.createQuery(
			      "UPDATE MessagesAdmin m "
			      + " SET m.messageRead = :b " +
			      " WHERE m.user = :p"
			      + " AND m.messageFromAdmin = :f ");
		query.setParameter("b", true).setParameter("p", user).setParameter("f", true).executeUpdate();
	}
	
	/* Inserts messages into DB
	 * Assumes a transaction commit afterwards
	 */
	public static void insertMessages(List<model.Message> messages, EntityManager em){
		for(model.Message obj: messages){
			em.persist(obj);
		}
	}
	
	/* Inserts admin messages into DB 
	 * Assumes a transaction commit afterwards
	 */
	public static void insertMessagesAdmin(List<MessagesAdmin> messages, EntityManager em){
		for(MessagesAdmin obj: messages){
			em.persist(obj);
		}
	}
	
	

}
