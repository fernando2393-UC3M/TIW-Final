package main;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

	private static final String MESSAGES_API_URL = "http://localhost:10006/";
	private static final String ADMIN_API_URL = "http://localhost:10005/admin";
	private static final String HOME_API_URL = "http://localhost:10002/homes";
	private static final String USER_API_URL = "http://localhost:10001/users";

	String path = "http://localhost:8080/TIWbnbAdmin/";
		
	ServletContext context;
	
	HttpSession session;
	
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
			//Get adminId to query for admin
			Integer adminId = (Integer) session.getAttribute("admin"); 
			
			
			List<MessagesAdmin> messageList = null;

			// Obtain messages to admin
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(MESSAGES_API_URL).path("admin/admin").path(adminId.toString());
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			// Set admin messages as Read
			WebTarget webResource3 = client.target(MESSAGES_API_URL).path("admin/admin/setRead/").path(adminId.toString());
			Invocation.Builder invocationBuilder3 = webResource3.request(MediaType.APPLICATION_JSON);
			invocationBuilder3.get();
			
			if(response.getStatus() == 200){
				MessagesAdmin[] temp = response.readEntity(MessagesAdmin[].class);
				messageList = Arrays.asList(temp);
				
				if(messageList.size() > 0){
					session.setAttribute("AdminMessages", messageList);					
				}				
			}
			
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

			List <User> result;
			
			if(response.getStatus() == 200) {
				User[] temp = response.readEntity(User[].class);
				result = Arrays.asList(temp);
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
				
				res.sendRedirect("manage_users");				
			}
			else { // Error in deletion
				
				res.sendRedirect("manage_users");
			}			
		}
		
		// --------------------- DELETE PLACE CASE -------------------------------------------
		
		else if (requestURL.toString().equals(path+"delete_place")) {

			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(HOME_API_URL).path(req.getParameter("homeId"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.delete();
			
			if(response.getStatus() == 200) {
				
				res.sendRedirect("resultados");		
			}
			else { // Error in deletion
				res.sendRedirect("resultados");	
			}								
		}
		
		// ------------------------- MODIFY USER CASE -------------------------------
		
		else if (requestURL.toString().equals(path+"modify")) {
			
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USER_API_URL).path(req.getParameter("inputId"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			Response responseUsr = invocationBuilder.get();
			
			User resultUsr = responseUsr.readEntity(User.class);	
			
			User result = new User();
			// Password confirmed
			if(!req.getParameter("inputName").isEmpty()){
				result.setUserName(req.getParameter("inputName"));
			}
			else {
				result.setUserName(resultUsr.getUserName());
			}
			
			if(!req.getParameter("inputEmail").isEmpty()){
				result.setUserEmail(req.getParameter("inputEmail"));
			}
			else {
				result.setUserEmail(resultUsr.getUserEmail());
			}
			
			if(!req.getParameter("inputSurname").isEmpty()){
				result.setUserSurname(req.getParameter("inputSurname"));
			}
			else {
				result.setUserSurname(resultUsr.getUserSurname());
			}
			
			if(!req.getParameter("inputPassword").isEmpty()){
				result.setUserPassword(req.getParameter("inputPassword"));
			}
			else {
				result.setUserPassword(resultUsr.getUserPassword());
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = new Date(1970, 01, 01);
			try {
				
				if(!req.getParameter("inputBirthdate").isEmpty()){
					parsed = format.parse(req.getParameter("inputBirthdate"));
					java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
					result.setUserBirthdate(sqlDate);
				}
				else {
					result.setUserBirthdate(resultUsr.getUserBirthdate());
				}
			} catch (ParseException e) {
			}		
			
			invocationBuilder.put(Entity.entity(result, MediaType.APPLICATION_JSON));
				
			res.sendRedirect("manage_users");
			
		} 
		
		// ------------------------- MODIFY PLACE CASE -------------------------------
		
		else if (requestURL.toString().equals(path+"modify_place")) {
			
			
			// Look for home now
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(HOME_API_URL).path(req.getParameter("inputId"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			Response responseHome = invocationBuilder.get();
			
			Home resultHome = responseHome.readEntity(Home.class);
			
			Home result = new Home();
			
			if(!req.getParameter("inputName").isEmpty()) {
				result.setHomeName(req.getParameter("inputName"));
			}
			else {
				result.setHomeName(resultHome.getHomeName());
			}
			
			// First look for user to re-assign email
			
			Client clientUsr = ClientBuilder.newClient();
			WebTarget webResourceUsr = clientUsr.target("http://localhost:10001").queryParam("email", req.getParameter("inputEmail"));
			Invocation.Builder invocationBuilderUsr = webResourceUsr.request(MediaType.APPLICATION_JSON);
			
			Response responseUsr = invocationBuilderUsr.get();
			
			User resultUsr = responseUsr.readEntity(User.class); //New user to whom this house will be assigned
			
			if(resultUsr == null) { // Error getting the user
				
				res.sendRedirect("resultados");					
			}
			
			else {
			
			result.setUser(resultUsr);
			
			if(!req.getParameter("inputCity").isEmpty()) {
				result.setHomeCity(req.getParameter("inputCity"));
			}
			else {
				result.setHomeCity(resultHome.getHomeCity());
			}
			
			if(!req.getParameter("inputDescriptionFull").isEmpty()) {
				result.setHomeDescriptionFull(req.getParameter("inputDescriptionFull"));
			}
			else {
				result.setHomeDescriptionFull(resultHome.getHomeDescriptionFull());
			}
			
			if(!req.getParameter("inputDescriptionShort").isEmpty()) {
				result.setHomeDescriptionShort(req.getParameter("inputDescriptionShort"));
			}
			else {
				result.setHomeDescriptionShort(resultHome.getHomeDescriptionShort());
			}
			
			if(!req.getParameter("inputType").isEmpty()) {
				result.setHomeType(req.getParameter("inputType"));
			}
			else {
				result.setHomeType(resultHome.getHomeType());
			}
			
			if(!req.getParameter("inputGuests").isEmpty()) {
				result.setHomeGuests(Integer.parseInt(req.getParameter("inputGuests")));
			}
			else {
				result.setHomeGuests(resultHome.getHomeGuests());
			}
			
			if(!req.getParameter("inputPriceNight").isEmpty()) {
				result.setHomePriceNight(new BigDecimal (req.getParameter("inputPriceNight")));
			}
			else {
				result.setHomePriceNight(resultHome.getHomePriceNight());
			}
			
			if(!req.getParameter("inputPhotos").isEmpty()) {
				String pic = "images/"+req.getParameter("inputPhotos");
				result.setHomePhotos(pic);
			}
			else {
				result.setHomePhotos("images/place-1.jpg");
			}
			
			if(!req.getParameter("inputDateInit").isEmpty()) {

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date parsed = new Date(1970, 01, 01);
				try {
					parsed = format.parse(req.getParameter("inputDateInit"));
					java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
					result.setHomeAvDateInit(sqlDate);

				} catch (ParseException e) {
				}				
			}
			else {
				result.setHomeAvDateInit(resultHome.getHomeAvDateInit());
			}
			
			if(!req.getParameter("inputDateFin").isEmpty()) {

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date parsed = new Date(1970, 01, 01);
				try {
					parsed = format.parse(req.getParameter("inputDateFin"));
					java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
					result.setHomeAvDateFin(sqlDate);

				} catch (ParseException e) {
				}				
			}
			else {
				result.setHomeAvDateFin(resultHome.getHomeAvDateFin());
			}
			
			Response response = invocationBuilder.put(Entity.entity(result, MediaType.APPLICATION_JSON));
			
			if(response.getStatus() == 200) {
				res.sendRedirect("resultados");				
			}
			else { // Error in update
				res.sendRedirect("resultados");	
			}
			
			}
					
		} 
		else {
			dispatcher.forward(req, res);
		}
	}
}