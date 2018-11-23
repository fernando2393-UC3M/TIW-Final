package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name="LoginServlet"/*, urlPatterns={"/login"}*/)
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Login loginInstance = new Login();
		loginInstance.openConnection();
		ResultSet result = loginInstance.CheckUser(request.getParameter("loginEmail"), request.getParameter("loginPassword"));

		if (result != null) { //User match
			try {
				HttpSession session = request.getSession();
				String userId = result.getString("USER_ID");
				
				session.setAttribute("user", userId);
				session.setMaxInactiveInterval(30*60); // 30 mins
				
				Cookie user = new Cookie("id", userId);
				user.setMaxAge(30*60);
				
				response.addCookie(user);
				response.sendRedirect("registrado.jsp");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else { // No user match
			response.sendRedirect("index.jsp");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}