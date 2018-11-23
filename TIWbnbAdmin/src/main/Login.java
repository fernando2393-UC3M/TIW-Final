package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login {

	Connection con;
	Statement st;

	public void openConnection () {
		try {
			// Load Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiwbnb", "root", "admin");
			System.out.println("Sucessful connection");
		} catch (Exception e) {
			System.out.println("Error when connecting to the database ");
		}
	}
	
public ResultSet CheckAdmin(String input_mail, String input_password) {
		
		ResultSet rs = null;
		
		try {
			// Create statement
			st =con.createStatement();

			//Once the statement is created, we need to get the user input for both user email and password

			// Execute statement
			// Here we obtain the full User table 
			rs = st.executeQuery("SELECT * FROM ADMIN WHERE ADMIN_EMAIL = '"+input_mail+"' AND ADMIN_PASSWORD = '"+input_password+"'");
			
			if (rs.next() == false){ // Empty rs check
				rs = null;
			}
	

		} catch (SQLException e) {
			System.out.println("Error when opening table ");
		}
		
		return rs;		
	}

public void closeConnection() {
	try {
		con.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
