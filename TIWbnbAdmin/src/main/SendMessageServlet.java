package main;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;

import model.Admin;
import model.MessagesAdmin;
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
		
		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target("http://localhost:10001/").path("mail").path(email);
		Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
		Response resp = invocationBuilder.get();		

		WebTarget webResource2 = client.target("http://localhost:10005/admin").path("1");
		Invocation.Builder invocationBuilder2 = webResource2.request(MediaType.APPLICATION_JSON);
		Response resp2 = invocationBuilder2.get();
		
	    User usr = resp.readEntity(User.class);
	    Admin adm = resp2.readEntity(Admin.class);
		
		MessagesAdmin aux = new MessagesAdmin();
		aux.setMessageContent(content);
		Date date = new Date();
		aux.setMessageDate( new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		aux.setMessageFromAdmin((byte) 1);
		aux.setMessageRead((byte) 0);
		aux.setUser(usr);
		aux.setAdmin(adm);
		

		WebTarget webResource3 = client.target("http://localhost:10006/sendAdminMessage");
		Invocation.Builder invocationBuilder3 = webResource3.request(MediaType.APPLICATION_JSON);
		Response resp3 = invocationBuilder3.post(Entity.entity(aux, MediaType.APPLICATION_JSON));
		
		// Redirect back to messages
		response.sendRedirect("mensajes");
	}

}
