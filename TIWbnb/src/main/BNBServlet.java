package main;

import java.io.IOException;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import messages.ReadMessages;

import messages.SendMessages;
import model.Admin;
import model.Booking;

import model.Home;
import model.MessagesAdmin;
import model.User;
import javax.jms.JMSException;

/**
 * Servlet implementation class BDServlet
 */
@WebServlet(urlPatterns = {"/index", "/admin", 
				"/resultados", "/renting", "/delete",
				"/registrado", "/mensajes", "/login", "/register",
				"/alojamiento", "/casa", "/viajes", "/logout",
				"/SendMessage", "/SendMessageAdmin", "/booking", "/booking_confirmation"})
public class BNBServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName="TIWbnb")
	protected EntityManager em;
	
	@Resource
	private UserTransaction ut;
	
	@Resource(mappedName="tiwconnectionfactory")
	ConnectionFactory cf;

	@Resource(mappedName="tiwqueue")
	Queue queue;
	Session transaction;
	
	String path = "http://localhost:8080/TIWbnb/";
	
	ServletContext context;
	
	HttpSession session;
	
	public void persist(Object entity) {
		try {
			ut.begin();
			em.persist(entity);
			ut.commit();
		} catch (SecurityException | IllegalStateException | NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
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

//		if(requestURL.toString().equals(path+"admin")){
//			ReqDispatcher =req.getRequestDispatcher("admin.jsp");
//		}
//		else 
		if(requestURL.equals(path+"alojamiento")){
			ReqDispatcher =req.getRequestDispatcher("alojamiento.jsp");
		}
		else if(requestURL.equals(path+"casa")){
			ReqDispatcher =req.getRequestDispatcher("casa.jsp");
		}
		else if(requestURL.equals(path+"mensajes")){
			
			//------------------------READ MESSAGES------------------------
					
			// Get userId from session (need parameter name to access)
			int userId = (Integer) session.getAttribute("user"); 
			
			try {
				ut.begin();
				List<model.Message> messageList;
				messageList = ReadMessages.getMessages(userId, em, cf, queue);
				ReadMessages.setRead(userId, em);
				ut.commit();

				ut.begin();
				List<MessagesAdmin> messageAdminList;
				messageAdminList = ReadMessages.getMessagesAdmin(userId, em, cf, queue);
				ReadMessages.setRead(userId, em);
				ut.commit();
				
				/* Query messages from DB */
				Query query = em.createQuery(
					      "SELECT b "
					      + " FROM Booking b "
					      + " JOIN b.home h "
					      + " JOIN h.user u " +
					      " WHERE u.userId = :p "
					      + " AND b.bookingConfirmed = :c");
					      
				@SuppressWarnings({ "unchecked" })
				List<Booking> bookingList = query.setParameter("p", userId).setParameter("c", false).getResultList();
				
				// Save messages in user session
				if(messageList.size() > 0)
					session.setAttribute("UserMessages", messageList); 
				
				// Save admin messages in user session
				if(messageAdminList.size() > 0)
					session.setAttribute("AdminMessages", messageAdminList); 
				
				if(bookingList.size() > 0)
					session.setAttribute("bookingList", bookingList); 
				
			} catch (JMSException | NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Treat JMS/JPA Exception
			}
			ReqDispatcher =req.getRequestDispatcher("mensajes.jsp");				
		
			//------------------------END READ MESSAGES------------------------
			
		}
		else if(requestURL.equals(path+"registrado")){
			int id = (int) session.getAttribute("user");
						
			User user = em.find(User.class, id); // Select the user after commit

			req.setAttribute("Name", user.getUserName());
			req.setAttribute("Surname", user.getUserSurname());			
			req.setAttribute("Birthdate", (new SimpleDateFormat("yyyy-MM-dd")).format(user.getUserBirthdate()));
			req.setAttribute("Password", user.getUserPassword());
			
			ReqDispatcher =req.getRequestDispatcher("registrado.jsp");	
		}
		else if(requestURL.equals(path+"delete")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");		
		}
		else if(requestURL.equals(path+"login")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
		}
		else if(requestURL.equals(path+"register")){
			ReqDispatcher =req.getRequestDispatcher("index.jsp");
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
			Login loginInstance = new Login();
			loginInstance.openConnection();
			ResultSet result = loginInstance.CheckUser(req.getParameter("loginEmail"), req.getParameter("loginPassword"));

			if (result != null) { //User match
				dispatcher = req.getRequestDispatcher("registrado.jsp");
				try {
					req.setAttribute("Name", result.getString("USER_NAME"));
					req.setAttribute("Surname", result.getString("USER_SURNAME"));
					req.setAttribute("Birthdate", result.getString("USER_BIRTHDATE"));
					req.setAttribute("Password", result.getString("USER_PASSWORD"));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Save user in servlet session
				
				session = req.getSession();

				try {
					session.setAttribute("user", result.getInt("USER_ID"));
					session.setMaxInactiveInterval(30*60); // 30 mins
					
					Cookie user = new Cookie("id", Integer.toString(result.getInt("USER_ID")));
					user.setMaxAge(30*60);
					
					res.addCookie(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				// Forward to requested URL by user
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
			
			String queryS = "SELECT s FROM User s WHERE s.userEmail = '"+req.getParameter("registerEmail")+"'";
			Query query = em.createQuery(queryS);
			@SuppressWarnings("unchecked")
			List <User> userList = query.getResultList();
			
			if(userList.isEmpty()){
				
				User user = new User();
								
				@SuppressWarnings("deprecation")
				Date aux = new Date(1970, 01, 01);
				
				// User id automatically generated by MySQL
				user.setUserEmail(req.getParameter("registerEmail"));
				user.setUserName(req.getParameter("registerName"));
				user.setUserSurname(req.getParameter("registerSurname"));
				user.setUserPassword(req.getParameter("registerPassword"));
				user.setUserBirthdate(aux);
				
				persist(user);
				
				req.setAttribute("Registered", 1);
			}
			
			else {
				req.setAttribute("Registered", 2);
			}
			

			dispatcher.forward(req, res);			

		}

		//------------------------INFORMATION UPDATE------------------------

		else if(requestURL.toString().equals(path+"registrado")) {
			
			
			dispatcher = req.getRequestDispatcher("registrado.jsp");
			
			int id = (int) session.getAttribute("user");
			
			
			Registrado registradoInstance = new Registrado();
			
			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			boolean updated = registradoInstance.updateUserData(id, req.getParameter("name"), req.getParameter("surname"), 
					req.getParameter("birthdate"), req.getParameter("password"), req.getParameter("password1"), em);			

			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			User user = em.find(User.class, id); // Select the user after commit

			req.setAttribute("Name", user.getUserName());
			req.setAttribute("Surname", user.getUserSurname());			
			req.setAttribute("Birthdate", (new SimpleDateFormat("yyyy-MM-dd")).format(user.getUserBirthdate()));
			req.setAttribute("Password", user.getUserPassword());
			
			if(updated) {
				req.setAttribute("Updated", 1);
			}
			else {
				req.setAttribute("Updated", 2);
			}

			dispatcher.forward(req, res);			
			
		}
		
		//------------------------PROFILE DELETION------------------------

		else if(requestURL.toString().equals(path+"delete")) {
			
			dispatcher = req.getRequestDispatcher("index.jsp");

			int id = (int) session.getAttribute("user");

			DeleteUser delete = new DeleteUser();

			try {
				ut.begin();
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			delete.deletion(id, em); // Deletion method

			try {
				ut.commit();
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			session.removeAttribute("user"); // Remove user from session

			dispatcher.forward(req, res);			

		}
		
		//------------------------HOUSE CREATION------------------------

		else if(requestURL.toString().equals(path+"casa")){
			
			dispatcher = req.getRequestDispatcher("casa.jsp");
			
			if(req.getParameter("houseName") != null && req.getParameter("houseCity") != null && req.getParameter("houseDesc") != null && req.getParameter("houseSubDesc") != null && 
					req.getParameter("houseType") != null && req.getParameter("guests") != null && req.getParameter("photo") != null && req.getParameter("inputPriceNight") != null &&
					 req.getParameter("iDate") != null && req.getParameter("fDate") != null){
				
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
				
				String photography = req.getParameter("photo");
				
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

				persist(home);
				
				
//				houseInstance.RegisterHouse(em, req.getParameter("houseName"), req.getParameter("houseCity"), req.getParameter("houseDesc"), req.getParameter("houseSubDesc")
//						, req.getParameter("houseType"), guests, req.getParameter("photo"), inputPriceNight, req.getParameter("iDate"), req.getParameter("fDate"));
				
			}
			
			dispatcher.forward(req, res);	
			
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
			
			/* Get content from POST message */
			String email = req.getParameter("receiver");
			String content = req.getParameter("message");
			
			// Get userId from session
			int id = (int) session.getAttribute("user");
			
			
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
			User userReceiver = (User) results.get(0);
						
			// Send message to queue
			try {
				SendMessages.sendMessage(id, userReceiver.getUserId(), content, cf, queue);
			} catch (JMSException e) {
				// TODO: Error handling for sending message
			}

			res.sendRedirect("mensajes");
			
		}
		
		//------------------------MESSAGE ADMIN------------------------
				
		else if(requestURL.equals(path+"SendMessageAdmin")){
			
			/* Get content from POST message */
			String content = req.getParameter("message");
			
			// Get userId from session
			int id = (int) session.getAttribute("user");
			
			// Look for the admin by id
			Admin admin = em.find(Admin.class, 1);
			
			// Send message to queue
			try {
				SendMessages.sendMessageAdmin(admin.getAdminId(), id, content, cf, queue);
			} catch (JMSException e) {
				// TODO: Error handling for sending message
			}

			res.sendRedirect("mensajes");
			
		}

		//------------------------BOOKING------------------------
		
		else if(requestURL.toString().equals(path+"booking")) {
			// Obtain sender
			int id = (int) session.getAttribute("user");
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
			
			
			/* Get Booking id into message */
			String content = ""+obj.getBookingId();

			
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

			// TODO: Redirect back to casa
			res.sendRedirect("mensajes");
			
		}
		
		//------------------------BOOKING CONFIRMATION------------------------
		
		else if(requestURL.toString().equals(path+"booking_confirmation")) {
			
			/* Commit the JMS transaction (queue is always same object) */
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
			} catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
				// Error JPA
			}
			
			// Send a response message
			try {
				SendMessages.sendMessage(sender.getUserId(), receiver.getUserId(), body, cf, queue);
			} catch (JMSException e) {
				// Error JMS
			}

			res.sendRedirect("mensajes");
			
		}
		
	}
}