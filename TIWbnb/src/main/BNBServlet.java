package main;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Admin;
import model.Home;
import model.Message;
import model.MessagesAdmin;
import model.User;

/**
 * Servlet implementation class BDServlet
 */
@WebServlet(urlPatterns = {"/index", "/admin", "/resultados",
				"/renting", "/delete", "/registrado",
				"/mensajes", "/login", "/register",
				"/alojamiento", "/casa", "/queryhome", "/viajes",
				"/logout", "/SendMessage", "/SendMessageAdmin",
				"/booking", "/booking_confirmation", "/deleteHome", "/detail", "/modifyHome"})
public class BNBServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String USERS_API_URL = "http://localhost:10001/users";
	private static final String HOMES_API_URL = "http://localhost:10002/homes";
	private static final String MESSAGES_API_URL = "http://localhost:10006";
	
	String path = "http://localhost:8080/TIWbnb/";
	
	ServletContext context;
	
	HttpSession session;
	
	 
////////////////////////////////////////////////////////////////////////////////////////
	public void init() {

		// It reads servlet's context

		context = getServletContext();
	}


////////////////////////////////////////////////////////////////////////////////////////
	public void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher ReqDispatcher;

		String requestURL = req.getRequestURL().toString();

		if(requestURL.equals(path+"alojamiento")){
			ReqDispatcher =req.getRequestDispatcher("alojamiento.jsp");
		}
		else if(requestURL.equals(path+"casa")){
			ReqDispatcher =req.getRequestDispatcher("casa.jsp");
		}
		else if(requestURL.equals(path+"detail")){
			ReqDispatcher =req.getRequestDispatcher("detail.jsp");
		}
		else if(requestURL.toString().equals(path+"deleteHome")) {
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(HOMES_API_URL).path(req.getParameter("id"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			Response response = invocationBuilder.delete();			
			
			if(response.getStatus() == 200){
				req.setAttribute("deleted", 1);
			} else {
				req.setAttribute("deleted", 2);
			}			
			
			ReqDispatcher =req.getRequestDispatcher("renting.jsp");

		}
		
		else if(requestURL.equals(path+"mensajes")){
			
			//------------------------READ MESSAGES------------------------
			//Get userId to query for user
			Integer userId = (Integer) session.getAttribute("user"); 
			

			List<Message> msgList = null;
			List<MessagesAdmin> adminList = null;

			// Get user messages
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(MESSAGES_API_URL).path("user").path(userId.toString());
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			if(response.getStatus() == 200){
				Message[] temp = response.readEntity(Message[].class);
				msgList = Arrays.asList(temp);
				
				if(msgList.size() > 0){
					session.setAttribute("UserMessages", msgList);					
				}				
			}
			
			// Set messages as Read
			WebTarget webResource1 = client.target(MESSAGES_API_URL).path("setRead").path(userId.toString());
			Invocation.Builder invocationBuilder1 = webResource1.request(MediaType.APPLICATION_JSON);
			invocationBuilder1.get();

			// Get admin messages
			WebTarget webResource2 = client.target(MESSAGES_API_URL).path("admin/user").path(userId.toString());
			Invocation.Builder invocationBuilder2 = webResource2.request(MediaType.APPLICATION_JSON);
			Response response2 = invocationBuilder2.get();
			
			if(response2.getStatus() == 200){
				MessagesAdmin[] temp = response2.readEntity(MessagesAdmin[].class);
				adminList = Arrays.asList(temp);
				
				if(adminList.size() > 0){
					session.setAttribute("AdminMessages", adminList);					
				}				
			}
			
			// Set admin messages as Read
			WebTarget webResource3 = client.target(MESSAGES_API_URL).path("admin/user/setRead/").path(userId.toString());
			Invocation.Builder invocationBuilder3 = webResource3.request(MediaType.APPLICATION_JSON);
			invocationBuilder3.get();
			
			/*
			if(bookingList.size() > 0)
				session.setAttribute("bookingList", bookingList); 
			*/
			
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");				
			
				
			//------------------------END READ MESSAGES------------------------
			
		}
		
		else if(requestURL.equals(path+"registrado")){
			int id = (int) session.getAttribute("user");
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USERS_API_URL).path("" + id);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			Response response = invocationBuilder.get();
			
			int status = response.getStatus();
			
			User user = response.readEntity(User.class);
			
			if(status == 200){
				req.setAttribute("Name", user.getUserName());
				req.setAttribute("Surname", user.getUserSurname());		
				req.setAttribute("Birthdate", user.getUserBirthdate());
				req.setAttribute("Password", user.getUserPassword());
				
				ReqDispatcher = req.getRequestDispatcher("registrado.jsp");
			} else {
				ReqDispatcher = req.getRequestDispatcher("index.jsp");
			}
		}
		else if(requestURL.equals(path+"renting")){
			ReqDispatcher =req.getRequestDispatcher("renting.jsp");					
		}
		else if(requestURL.equals(path+"resultados")){
			ReqDispatcher =req.getRequestDispatcher("resultados.jsp");				
		}
		else if(requestURL.equals(path+"viajes")){
			ReqDispatcher =req.getRequestDispatcher("viajes.jsp");
		}
		else if(requestURL.equals(path+"logout")){
			doPost(req, res); // Special case -------------------------
			return;
		} 
		else {
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		ReqDispatcher.forward(req, res);
	}

	////////////////////////////////////////////////////////////////////////////////////////  	
	public void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");

		// Here we get the current URL requested by the user

		String requestURL = req.getRequestURL().toString();
		
		//------------------------PROFILE LOGIN------------------------
		
		if(requestURL.toString().equals(path+"login")){
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USERS_API_URL).path(req.getParameter("loginEmail")).path(req.getParameter("loginPassword"));
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			User result = response.readEntity(User.class);
			
			if(response.getStatus() == 200) {
				req.setAttribute("Name", result.getUserName());
				req.setAttribute("Surname", result.getUserSurname());
				req.setAttribute("Birthdate", result.getUserBirthdate());
				req.setAttribute("Password", result.getUserPassword());
				
				session = req.getSession();
				session.setAttribute("user", result.getUserId());
				session.setMaxInactiveInterval(30*60); // 30 mins
				
				Cookie user = new Cookie("id", Integer.toString(result.getUserId()));
				user.setMaxAge(30*60);
				
				res.addCookie(user);
				
				dispatcher = req.getRequestDispatcher("registrado.jsp");
				dispatcher.forward(req, res);				
			}
			else { // No user match
				dispatcher = req.getRequestDispatcher("index.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}
		}
		
		//------------------------USER REGISTRATION------------------------
		
		else if(requestURL.toString().equals(path+"register")) {
			
			dispatcher = req.getRequestDispatcher("index.jsp");
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USERS_API_URL);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			User newUser = new User();
			
			@SuppressWarnings("deprecation")
			Date aux = new Date(1970, 01, 01);
			
			// User id automatically generated by MySQL
			newUser.setUserEmail(req.getParameter("registerEmail"));
			newUser.setUserName(req.getParameter("registerName"));
			newUser.setUserSurname(req.getParameter("registerSurname"));
			newUser.setUserPassword(req.getParameter("registerPassword"));
			
			java.sql.Date sqlDate = new java.sql.Date(aux.getTime());
			newUser.setUserBirthdate(sqlDate);
									
			Response response = invocationBuilder.post(Entity.entity(newUser, MediaType.APPLICATION_JSON));			
			
			if(response.getStatus() == 200){
				req.setAttribute("Registered", 1);
			} else {
				req.setAttribute("Registered", 2);
			}

			dispatcher.forward(req, res);			

		}

		//------------------------INFORMATION UPDATE------------------------

		else if(requestURL.toString().equals(path+"registrado")) {
			
			
			dispatcher = req.getRequestDispatcher("registrado.jsp");
			
			int id = (int) session.getAttribute("user");
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USERS_API_URL).path("" + id);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			
			
			if(req.getParameter("password").equals(req.getParameter("password1"))) {
				User result = new User();
				// Password confirmed
				result.setUserName(req.getParameter("name"));
				result.setUserSurname(req.getParameter("surname"));
				result.setUserPassword(req.getParameter("password"));
				
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date parsed = new Date(1970, 01, 01);
				try {
					parsed = format.parse(req.getParameter("birthdate"));
				} catch (ParseException e) {
				}
				java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
				result.setUserBirthdate(sqlDate);
				
				Response response = invocationBuilder.put(Entity.entity(result, MediaType.APPLICATION_JSON));
				
				if(response.getStatus() == 200) {
					req.setAttribute("Name", result.getUserName());
					req.setAttribute("Surname", result.getUserSurname());
					
					String myDate = format.format(parsed);
							
					req.setAttribute("Birthdate", myDate);
					req.setAttribute("Password", result.getUserPassword());
					
					req.setAttribute("Updated", 1);
				} else {
					req.setAttribute("Updated", 2);
				}
				
			} else {

				Response response = invocationBuilder.get();

				User user = response.readEntity(User.class);

				req.setAttribute("Name", user.getUserName());
				req.setAttribute("Surname", user.getUserSurname());				
				req.setAttribute("Birthdate", user.getUserBirthdate());
				req.setAttribute("Password", user.getUserPassword());

				req.setAttribute("Updated", 2);
			}


			dispatcher.forward(req, res);			
			
		}
		
		//------------------------PROFILE DELETION------------------------

		else if(requestURL.toString().equals(path+"delete")) {
			
			int id = (int) session.getAttribute("user");
			String id_str = Integer.toString(id);
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(USERS_API_URL).path(id_str);
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.delete();
			
			if(response.getStatus() == 200) {
				dispatcher = req.getRequestDispatcher("index.jsp");
				session.removeAttribute("user"); // Remove user from session
			}
			else { // No user match
				dispatcher = req.getRequestDispatcher("registrado.jsp");
			}

			dispatcher.forward(req, res);			

		}
		//////////////////// MODIFY HOUSE
		else if(requestURL.toString().equals(path+"modifyHome")){
			
			dispatcher = req.getRequestDispatcher("renting.jsp");
			
				Client client = ClientBuilder.newClient();
				WebTarget webResource = client.target(HOMES_API_URL).path("" + req.getParameter("id"));
				Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
				Response response = invocationBuilder.get();
				
				Home home = response.readEntity(Home.class);
				
				if(response.getStatus() == 200) {
					
					if(!req.getParameter("houseName").isEmpty()) {
						home.setHomeName(req.getParameter("houseName"));
					}
				
					if(!req.getParameter("houseCity").isEmpty()) {
						home.setHomeCity(req.getParameter("houseCity"));
					}
					
					if(!req.getParameter("houseDesc").isEmpty()) {
						home.setHomeDescriptionFull(req.getParameter("houseDesc"));
					}
					
					if(!req.getParameter("houseSubDesc").isEmpty()) {
						home.setHomeDescriptionShort(req.getParameter("houseSubDesc"));
					}
					
					if(!req.getParameter("houseType").isEmpty()) {
						home.setHomeType(req.getParameter("houseType"));
					}
					
					if(!req.getParameter("guests").isEmpty()) {
						home.setHomeGuests(Integer.parseInt(req.getParameter("guests")));
					}
					
					if(!req.getParameter("inputPriceNight").isEmpty()) {
						home.setHomePriceNight(new BigDecimal (req.getParameter("inputPriceNight")));
					}
					
					if(!req.getParameter("photo").isEmpty()) {
						String pic = "images/"+req.getParameter("photo");
						home.setHomePhotos(pic);
					}
					
					if(!req.getParameter("iDate").isEmpty()) {

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date parsed = new Date(1970, 01, 01);
						try {
							parsed = format.parse(req.getParameter("iDate"));
							home.setHomeAvDateInit(parsed);

						} catch (ParseException e) {
						}				
					}
					
					if(!req.getParameter("fDate").isEmpty()) {

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date parsed = new Date(1970, 01, 01);
						try {
							parsed = format.parse(req.getParameter("fDate"));
							home.setHomeAvDateFin(parsed);

						} catch (ParseException e) {
						}				
					}
										
					response = invocationBuilder.put(Entity.entity(home, MediaType.APPLICATION_JSON));			
					
					if(response.getStatus() == 200) {
						req.setAttribute("Registered", 1);
					} else {
						req.setAttribute("Registered", 2);
					}
					dispatcher = req.getRequestDispatcher("renting.jsp");

				}

			dispatcher.forward(req, res);		
			
		}
		
		//------------------------HOUSE CREATION------------------------

		else if(requestURL.toString().equals(path+"casa")){
			
			dispatcher = req.getRequestDispatcher("casa.jsp");
			
			if(req.getParameter("houseName") != null && req.getParameter("houseCity") != null && req.getParameter("houseDesc") != null && req.getParameter("houseSubDesc") != null && 
					req.getParameter("houseType") != null && req.getParameter("guests") != null && req.getParameter("photo") != null && req.getParameter("inputPriceNight") != null &&
					 req.getParameter("iDate") != null && req.getParameter("fDate") != null){
			
				Client client = ClientBuilder.newClient();
				WebTarget webResource = client.target(HOMES_API_URL).path("users/" + (int)session.getAttribute("user"));
				Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
								
				int guests = Integer.parseInt(req.getParameter("guests"));
				String aux = "";
				
				aux = req.getParameter("inputPriceNight");
				
				BigDecimal inputPriceNight = new BigDecimal(aux.replaceAll(",",""));
				
				Home home = new Home();
				
				String iDate = req.getParameter("iDate");
				String fDate = req.getParameter("fDate");

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date parsedIDate = new Date(1970, 01, 01);
				java.util.Date parsedFDate = new Date(1970, 01, 01);
				try {
					parsedIDate = format.parse(iDate);
					parsedFDate = format.parse(fDate);
				} catch (ParseException e) {
				}
								
				// House id automatically generated by MySQL
				home.setHomeName(req.getParameter("houseName"));
				home.setHomeCity(req.getParameter("houseCity"));
				home.setHomeDescriptionFull(req.getParameter("houseDesc"));
				home.setHomeDescriptionShort(req.getParameter("houseSubDesc"));
				home.setHomeType(req.getParameter("houseType"));
				home.setHomeGuests(guests);
				home.setHomePhotos(req.getParameter("photo"));
				home.setHomePriceNight(inputPriceNight);
				home.setHomeAvDateInit(parsedIDate);
				home.setHomeAvDateFin(parsedFDate);
										
				Response response = invocationBuilder.post(Entity.entity(home, MediaType.APPLICATION_JSON));			
				
				if(response.getStatus() == 200){
					req.setAttribute("Registered", 1);
				} else {
					req.setAttribute("Registered", 2);
				}
			}

			dispatcher.forward(req, res);		
			
		}
			//-----------------------QUERY HOME-------------------------------
		else if(requestURL.toString().equals(path+"queryhome")) {
			String city = req.getParameter("homeCiudad");
			String strInit = req.getParameter("homeIda");
			String strEnd = req.getParameter("homeVuelta");
			String price = req.getParameter("homePrecio");
			String type = req.getParameter("homeTipo");
			int numAdults = Integer.parseInt(req.getParameter("homeAdultos"));
			int numKids = Integer.parseInt(req.getParameter("homeNinios"));
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date dateInit = null;;
			Date dateEnd = null;
			try {
				dateInit = formatter.parse(strInit);
				dateEnd = formatter.parse(strEnd);
				
			} catch (ParseException e) {

				e.printStackTrace();
			}
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target(HOMES_API_URL).queryParam("homeCity", city)
															   .queryParam("homeInit", dateInit)
															   .queryParam("homeEnd", dateEnd)
															   .queryParam("homePrice", price)
															   .queryParam("homeType", type)
															   .queryParam("homeAdults", numAdults)
															   .queryParam("homeKids", numKids);
			
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response response = invocationBuilder.get();
			
			List <Home> result = (List<Home>) response.readEntity(new GenericType<List<Home>>(){});
			
			for(int i = 0; i < result.size(); i++){
				System.out.println(result.get(i).getHomeCity());
				System.out.println(result.get(i).getHomeDescriptionFull());
				System.out.println(result.get(i).getHomeDescriptionShort());
				System.out.println(result.get(i).getHomeGuests());
				System.out.println(result.get(i).getHomeId());
				System.out.println(result.get(i).getHomeName());
				System.out.println(result.get(i).getHomePhotos());
				System.out.println(result.get(i).getHomeType());
			}
			
			req.setAttribute("resultHomes", result);
			if(response.getStatus() == 200) {
				dispatcher = req.getRequestDispatcher("resultados.jsp");
				dispatcher.forward(req, res);				
			}
			else { // Error in deletion
				dispatcher = req.getRequestDispatcher("resultados.jsp");
				// Forward to requested URL by user
				dispatcher.forward(req, res);
			}			
			
			
		}
		//-----------------------LOGOUT-------------------------------
		
		else if(requestURL.toString().equals(path+"logout")) {
			
			req.removeAttribute("Name");
			req.removeAttribute("Surname");
			req.removeAttribute("Birthdate");
			req.removeAttribute("Password");
			
			session = req.getSession(false);
			
			if(session != null) {
				session.removeAttribute("user");
				session.invalidate();
			}
			
			dispatcher = req.getRequestDispatcher("index.jsp");
			dispatcher.forward(req, res);
			
		}
		
		//-----------------------SEND MESSAGE-------------------------------		
		
		
		else if(requestURL.toString().equals(path+"SendMessage")){

			Integer userId = (Integer) session.getAttribute("user"); 
			
			String email = req.getParameter("receiver");
			String content = req.getParameter("message");
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:10001/").path("users").path(userId.toString());
			Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
			Response resp = invocationBuilder.get();		

			WebTarget webResource2 = client.target("http://localhost:10001/").path("mail").path(email);
			Invocation.Builder invocationBuilder2 = webResource2.request(MediaType.APPLICATION_JSON);
			Response resp2 = invocationBuilder2.get();
			
		    User sender = resp.readEntity(User.class);
		    User receiver = resp2.readEntity(User.class);
			
			Message aux = new Message();
			aux.setMessageContent(content);
			Date date = new Date();
			aux.setMessageDate( new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			aux.setMessageRead((byte) 0);
			aux.setUser1(sender);
			aux.setUser2(receiver);
			

			WebTarget webResource3 = client.target("http://localhost:10006/sendMessage");
			Invocation.Builder invocationBuilder3 = webResource3.request(MediaType.APPLICATION_JSON);
			Response resp3 = invocationBuilder3.post(Entity.entity(aux, MediaType.APPLICATION_JSON));
			
			// Redirect back to messages
			res.sendRedirect("mensajes");
			
		}
		
		//------------------------MESSAGE ADMIN------------------------
				
		else if(requestURL.equals(path+"SendMessageAdmin")){

			Integer userId = (Integer) session.getAttribute("user"); 
			String content = req.getParameter("message");
			
			Client client = ClientBuilder.newClient();
			WebTarget webResource = client.target("http://localhost:10001/").path("users").path(userId.toString());
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
			aux.setMessageFromAdmin((byte) 0);
			aux.setMessageRead((byte) 0);
			aux.setUser(usr);
			aux.setAdmin(adm);
			

			WebTarget webResource3 = client.target("http://localhost:10006/sendAdminMessage");
			Invocation.Builder invocationBuilder3 = webResource3.request(MediaType.APPLICATION_JSON);
			Response resp3 = invocationBuilder3.post(Entity.entity(aux, MediaType.APPLICATION_JSON));
			
			// Redirect back to messages
			res.sendRedirect("mensajes");
		}

		//------------------------BOOKING------------------------
		
		else if(requestURL.toString().equals(path+"booking")) {
			/*
			<form class="form-sendmsg" METHOD="POST" ACTION="booking">
         	<input type="hidden" id="userid" name="userid"  value=<%= home.getUser().getUserId() %> />
         	<input type="hidden" id="homeid" name="homeid"  value=<%= home.getHomeId() %> />
			<label for="date-start">Date Start:</label>
			<input type="text" name="homeIda" class="form-control" id="date-start" placeholder="mm/dd/yyyy"/>
			<label for="date-end">Date End:</label>
			<input type="text" name="homeVuelta"  class="form-control" id="date-end" placeholder="mm/dd/yyyy"/>
			<label for="message">Card Number:</label>
			<textarea id="cardnum" class="text" rows="1" cols="25" name="cardnum" >1234567890123456</textarea>
			<label for="message">Exp. Code:</label>
			<textarea id="expcode" class="text" rows="1" cols="25" name="expcode" >MMyyyy</textarea>
			<label for="message">CV2:</label>
			<textarea id="cv2" class="text" rows="1" cols="25" name="cv2" >123</textarea>
			<button type="submit" formmethod="post">Book</button>
			 */
			
			// Obtain sender
			/*int id = (int) session.getAttribute("user");
			// Obtain host
			Home aux = em.find(Home.class, Integer.parseInt(req.getParameter("home_id")));

			@SuppressWarnings("deprecation")
			Date bookingDateIn = new Date(2000, 01, 01);
			@SuppressWarnings("deprecation")
			Date bookingDateOut = new Date(2000, 01, 02);
			try {
				bookingDateIn = new Date((new SimpleDateFormat("yyyy-MM-dd")).parse(req.getParameter("date_in")).getTime());
				bookingDateOut = new Date((new SimpleDateFormat("yyyy-MM-dd")).parse(req.getParameter("date_out")).getTime());
			} catch (ParseException e2) {
				// Error parsing date
			}
			
			// Add Booking to Database from POST request
			Booking obj = new Booking();
			obj.setUser(em.find(User.class, id));
			obj.setHome(aux);
			obj.setBookingDateIn(bookingDateIn);
			obj.setBookingDateOut(bookingDateOut);
			obj.setBookingCardNum(Integer.parseInt(req.getParameter("card")));
			obj.setBookingExpCode(req.getParameter("exp"));
			obj.setBookingCv2(Integer.parseInt(req.getParameter("cv2")));
			obj.setBookingConfirmed(false);
			
			try {
				ut.begin();
				em.persist(obj);
				ut.commit();
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e1) {
				// JPA Error
			}			
			*/
			
			//Get Booking id into message
			
			/*String content = ""+obj.getBookingId();

			
			Connection _connection = null;
			
			try {
				_connection = cf.createConnection();
				// Creates transaction, needs to be committed when booking request happens
				transaction = _connection.createSession(true, javax.jms.TopicSession.AUTO_ACKNOWLEDGE);

				// Send Message
				SendMessages.sendTransaction(id, aux.getUser().getUserId(), content, queue, transaction);
				
			} catch (JMSException e) {
				// Error JMS
			} finally {
	            if (_connection != null) {
	            	try {
						_connection.close();
					} catch (JMSException e) {
						// Error JMS close
					}
	            }
	        }

			// TODO: Redirect back to casa*/
			
			res.sendRedirect("mensajes");
			
		}
		
		//------------------------BOOKING CONFIRMATION------------------------
		
		else if(requestURL.toString().equals(path+"booking_confirmation")) {
			
			//Commit the JMS transaction (queue is always same object)
			/*
			try {
				transaction.commit();
			} catch (JMSException e1) {
				// Error JMS commit transaction
			}

			// Get Booking Request
			int bookingId = (int) session.getAttribute("bookingId");
			Booking object = em.find(Booking.class, bookingId);


			// Get response from home owner
			boolean accept = (boolean) session.getAttribute("bookingId");
			
			// Get users involved in booking
			User sender = object.getHome().getUser();
			User receiver = object.getUser();
			String body = "Your booking request with id "+object.getBookingId()+" has been ";
			
			try {
				ut.begin();
				
				// Accept Booking Request
				if(accept){
					object.setBookingConfirmed(true);
					em.persist(object);
					body += "accepted.";
				}
				// Reject (delete) Booking Request
				else{
					em.remove(object);
					body += "rejected.";
				}				

				ut.commit();	
				*/
			/*
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Error JPA
			}
			
			// Send a response message
			try {
				SendMessages.sendMessage(sender.getUserId(), receiver.getUserId(), body, cf, queue);
			} catch (JMSException e) {
				// Error JMS
			}
			*/
			res.sendRedirect("mensajes");
			
		} 
	}
}
