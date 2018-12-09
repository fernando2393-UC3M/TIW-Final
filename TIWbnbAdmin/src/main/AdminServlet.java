package main;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Admin;
import model.Home;
import model.MessagesAdmin;
import model.User;

@WebServlet(urlPatterns = {
		"/admin", "/resultados", "/homes", 
		"/manage_users", "/mensajes", "/modify_place", "/modify",
		"/index", "/delete", "/delete_place", "/login", "/logout"
		})
public class AdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = 6176032171079275384L;
	
	private static final String ADMIN_API_URL = "http://localhost:10005/admin";
	private static final String HOME_API_URL = "http://localhost:10002/homes";
	private static final String USER_API_URL = "http://localhost:10001/users";

	/*
	@PersistenceContext(unitName="TIWbnbAdmin")
	protected EntityManager em;
	
	@Resource
	private UserTransaction ut;
	*/
	String path = "http://localhost:8080/TIWbnbAdmin/";
		
	ServletContext context;
	
	HttpSession session;

	/* Attributes */
	/*
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue adminQueue;	
	*/
	
	public void init() {

		// It reads servlet's context

		context = getServletContext();
	}
	

	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher ReqDispatcher;

		String requestURL = req.getRequestURL().toString();

		if(requestURL.toString().equals(path+"admin")){
			ReqDispatcher =req.getRequestDispatcher("admin.jsp");
		}
	
		//------------------------READ MESSAGES------------------------
		
		else if(requestURL.equals(path+"mensajes")){		
			//Get adminId from session (need parameter name to access)
			/*int adminId = (Integer) session.getAttribute("admin"); 
			
			List<MessagesAdmin> messageList = null;
			try {
				ut.begin();
				//List<MessagesAdmin> messageList;
				messageList = ReadMessages.getMessages(adminId, em, cf, adminQueue);
				ReadMessages.setRead(adminId, em);
				ut.commit();
				
				// Save messages in user session
				if(messageList.size() > 0)
					req.setAttribute("AdminMessages", messageList); 
				
			} catch (JMSException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Treat JMS/JPA Exception
			}*/
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");
			
			
		}
	
		//------------------------END READ MESSAGES------------------------
		
		else if(requestURL.equals(path+"resultados")){
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(HOME_API_URL);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			List <Home> result = (List<Home>) response.readEntity(new GenericType<List<Home>>(){});
			
			if(response.getStatus() == 200) {
				
				req.setAttribute("homes", result);
								
			}		
			
			ReqDispatcher =req.getRequestDispatcher("resultados.jsp");
		}
		else if(requestURL.equals(path+"modify_place")) {
			ReqDispatcher =req.getRequestDispatcher("resultados.jsp");
		}
		else if(requestURL.equals(path+"modify")) {
			ReqDispatcher =req.getRequestDispatcher("manage_users.jsp");
		}
		
		else if(requestURL.equals(path+"manage_users")){
			
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USER_API_URL);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			List <User> result = (List<User>) response.readEntity(new GenericType<List<User>>(){});
			
			if(response.getStatus() == 200) {
				
				req.setAttribute("users", result);
								
			}
			
			ReqDispatcher =req.getRequestDispatcher("manage_users.jsp");
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		
		// --------------------------------- LOGOUT CASE -----------------------
		
		else if (requestURL.toString().equals(path+"logout")) {
					
			session = req.getSession(false);
					
			if(session != null) {
				session.removeAttribute("admin");
				session.invalidate();
			}
					
			ReqDispatcher = req.getRequestDispatcher("index.jsp");
			ReqDispatcher.forward(req, res);
		}
		
		// ----------------------------------------------------------------------
		
		else {
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		ReqDispatcher.forward(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user
		String requestURL = req.getRequestURL().toString();

		// ------------------------ LOGIN CASE ------------------------------------------

		if(requestURL.toString().equals(path+"login")){
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(ADMIN_API_URL).path(req.getParameter("loginEmail")).path(req.getParameter("loginPassword"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			Admin result = response.readEntity(Admin.class);
			
			if(response.getStatus() == 200) {
							
				session = req.getSession();
				session.setAttribute("admin", result.getAdminId());
				session.setMaxInactiveInterval(30*60); // 30 mins
				
				Cookie admin = new Cookie("id", Integer.toString(result.getAdminId()));
				admin.setMaxAge(30*60);
				
				res.addCookie(admin);
				
				dispatcher = req.getRequestDispatcher("admin.jsp");
				dispatcher.forward(req, res);				
			}
			else { // No admin match
				dispatcher = req.getRequestDispatcher("index.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}
		}
		
		// --------------------- DELETE USER CASE -------------------------------------------
		
		else if (requestURL.toString().equals(path+"delete")) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USER_API_URL).path(req.getParameter("inputId"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.delete();
			
			if(response.getStatus() == 200) {
				
				dispatcher = req.getRequestDispatcher("manage_users.jsp");
				dispatcher.forward(req, res);				
			}
			else { // Error in deletion
				dispatcher = req.getRequestDispatcher("manage_users.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}			
		}
		
		// --------------------- DELETE PLACE CASE -------------------------------------------
		
		else if (requestURL.toString().equals(path+"delete_place")) {

			dispatcher = req.getRequestDispatcher("resultados.jsp");
			/*	
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			String aux = req.getParameter("homeId");
			int id = Integer.parseInt(aux);
					
			DeletePlace.delete(em, id);
					
			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/	
			dispatcher.forward(req, res);
					
		}
		
		// ------------------------- MODIFY USER CASE -------------------------------
		
		else if (requestURL.toString().equals(path+"modify")) {
			
			String aux = req.getParameter("inputId");
			int id = Integer.parseInt(aux);
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USER_API_URL).path(req.getParameter("inputId"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			User result = new User();
			// Password confirmed
			result.setUserName(req.getParameter("inputName"));
			result.setUserSurname(req.getParameter("inputSurname"));
			result.setUserPassword(req.getParameter("inputPassword"));
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = new Date(1970, 01, 01);
			try {
				parsed = format.parse(req.getParameter("inputBirthdate"));
			} catch (ParseException e) {
			}

			result.setUserBirthdate(parsed);
			
			Response response = invocationBuilder.put(Entity.entity(result, MediaType.APPLICATION_JSON));
			
			if(response.getStatus() == 200) {
				
				dispatcher = req.getRequestDispatcher("manage_users.jsp");
				dispatcher.forward(req, res);				
			}
			else { // Error in update
				dispatcher = req.getRequestDispatcher("manage_users.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}			
			
		} 
		
		// ------------------------- MODIFY PLACE CASE -------------------------------
		
		else if (requestURL.toString().equals(path+"modify_place")) {
					
			//ModifyPlace modify = new ModifyPlace();
			dispatcher = req.getRequestDispatcher("resultados.jsp");
			/*	
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			String aux = "";
			int id = Integer.parseInt(req.getParameter("inputId"));
			
			aux = req.getParameter("inputGuests");
			int inputGuests = Integer.parseInt(aux);
			
			aux = req.getParameter("inputPriceNight");
			
			BigDecimal inputPriceNight = new BigDecimal(aux.replaceAll(",",""));
					
			modify.updatePlaceData(
					id, 
					req.getParameter("inputAvDateFin"), 
					req.getParameter("inputAvDateInit"), 
					req.getParameter("inputCity"),
					req.getParameter("inputDescriptionFull"),
					req.getParameter("inputDescriptionShort"),
					inputGuests,
					req.getParameter("inputName"),
					req.getParameter("inputPhotos"),
					inputPriceNight,
					req.getParameter("inputType"),
					em);
					
			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
					
			dispatcher.forward(req, res);
					
		} 
		else {
			dispatcher.forward(req, res);
		}
	}
}