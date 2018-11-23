package main;

import java.io.IOException;

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


	public ResultSet CheckUser(String input_mail, String input_password) {
		
		ResultSet rs = null;
		
		try {
			// Create statement
			st =con.createStatement();

			//Once the statement is created, we need to get the user input for both user email and password

			// Execute statement
			// Here we obtain the full User table
			String query = "SELECT * FROM USER WHERE USER_EMAIL = '"+input_mail+"' AND USER_PASSWORD = '"+input_password+"'";
			rs = st.executeQuery(query);
			
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
