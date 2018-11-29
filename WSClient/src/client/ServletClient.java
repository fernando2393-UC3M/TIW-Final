package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import domains.User;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



/**
 * Servlet implementation class ServletClient
 */
@WebServlet("/ServletClient")
public class ServletClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletClient() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		//***** 1. Create an Instance of a Client *****/		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		//***** 2A. Create a WebTarget, using the URI of the targeted web resource *****/
		WebTarget webtarget = client.target("http://localhost:8081");
		
		
		//***** 2B. Using the WebTarget define the path to the specific resource *****/
		WebTarget webtargetPath = webtarget.path("users");
		WebTarget webtargetPath2 = webtarget.path("users").path("Frodo");
		WebTarget webtargetPath3 = webtarget.path("users").path("Frodo").path("Bolson");
		
			// If you need to add a queryparam you'd add it between 
			// the last .path(..) and before .request() like this
			// .queryParam("greeting", "Hi World!")

		
		//***** 3. Build an HTTP Request Invocation *****/		
		Invocation.Builder invocationBuilder = webtargetPath.request(MediaType.APPLICATION_JSON);
		Invocation.Builder invocationBuilder2 = webtargetPath2.request(MediaType.APPLICATION_JSON);
		Invocation.Builder invocationBuilder3 = webtargetPath3.request(MediaType.APPLICATION_JSON);
			
			//  If you need to add something to the header do it here
			//	invocationBuilder.header("some-header", "true");
		
		//***** 4. Invoking HTTP Request *****/	
		Response httpResponse = invocationBuilder.get();
		Response httpResponse2 = invocationBuilder2.get();
		Response httpResponse3 = invocationBuilder3.get();
		
		//***** 5. From the response you can get the HTTP status and the data obtained *****/
		int status1 = httpResponse.getStatus();
		int status2 = httpResponse2.getStatus();
		int status3 = httpResponse3.getStatus();
		
		User[] list1 =
				httpResponse.readEntity(User[].class);
		User emp = httpResponse2.readEntity(User.class);
		User[] list3 =
				httpResponse3.readEntity(User[].class);
		
		// Consuming path /users
		out.println("<h1>path(GET) /users </h1>");		
		out.println("<h2> Status:" + status1 +" </h2>");
		for(int i=0; i<list1.length; i++)
			out.println("<h2> " + list1[i].getName() + " " + list1[i].getSurname()+" </h2>");
		
	
		// Consuming path /users/{name} 
		out.println("<h1>path(GET) /users/{name} </h1>");		
		out.println("<h2> Status:" + status2 +" </h2>");
		out.println("<h2> " + emp.getName() + " " + emp.getSurname()+" </h2>");

		// Consuming path /users/{name}/{surname} 
		out.println("<h1>path(GET) /users/{name}/{surname} </h1>");		
		out.println("<h2> Status:" + status3 +" </h2>");
		for(int i=0; i<list3.length; i++)
			out.println("<h2> " + list3[i].getName() + " " + list3[i].getSurname()+" </h2>");
		
		User postUser = new User();
		postUser.setName("testpostNAME");
		postUser.setSurname("testpostSURNAME");
		
		// COMPLETE WITH THE CALL OF THE SERVICE TO THE url /user to create the user postUser
		// DISPLAY THE RESULTS
		out.println("<h1>path(POST) /user </h1>");
		//out.println("<h2> Status:" + ... complete with the status of the response				
		//out.println("<h2>" + ... complete with the info of the user			
				
		
		// Consuming(PUT) path /user		
		// COMPLETE WITH THE CALL OF THE SERVICE TO THE url /user to update an user
		// DISPLAY THE RESULTS
		out.println("<h1>path(PUT) /user </h1>");		
		//out.println("<h2> Status:" + ... complete with the status of the response				
		//out.println("<h2>" + ... complete with the info of the user			
		
			
		// Consuming(DELETE) path /user	
		// COMPLETE WITH THE CALL OF THE SERVICE TO THE url /user to delete an user
		// DISPLAY THE RESULTS

		out.println("<h1>path(DELETE) /user </h1>");		
		//out.println("<h2> Status:" + ... complete with the status of the response		
		
	 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
}
