package main;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Admin;
import model.User;

/**
 * Servlet implementation class SendMessage
 */
@WebServlet("/SendMessage")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/* Attributes */
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue adminQueue;
	
	@PersistenceContext(unitName="TIWbnbAdmin")
	private EntityManager em;
	
    public SendMessageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("receiver");
		String content = request.getParameter("message");
		
		/* Query user using email from DB */
		Query query = em.createQuery("SELECT u "
			      + " FROM User u "
			      + " WHERE u.userEmail = :p");
		@SuppressWarnings("rawtypes")
		List results = query.setParameter("p", email).getResultList();
		if (results.isEmpty()){
			// TODO: Error handling for invalid email
		}
		// Email is unique so we can get first result		
		User user = (User) results.get(0);
		
		 // Look for the admin by id
		Admin admin = em.find(Admin.class, 1);
		
		// Send message to queue
		try {
			SendMessages.sendMessage(admin, user, content, cf, adminQueue);
		} catch (JMSException e) {
			// TODO: Error handling for sending message
		}
		
		// Redirect back to messages
		response.sendRedirect("mensajes");
	}

}
