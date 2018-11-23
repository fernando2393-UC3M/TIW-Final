<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
	<%@ page contentType="text/html; charset=UTF-8" %>
	<%@ page import="java.util.List" %>
	<%@ page import="java.util.Date" %>
	<%@ page import="model.User" %>
	<%@ page import="java.sql.DriverManager" %>
	<%@ page import="java.sql.Connection" %>
	<%@ page import="java.sql.Statement" %>
	<%@ page import="java.sql.ResultSet" %>
	<%@ page import="java.sql.SQLException" %>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>TIWbnb</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Free HTML5 Template by FREEHTML5.CO" />
	<meta name="keywords" content="free html5, free template, free bootstrap, html5, css3, mobile first, responsive" />
	<meta name="author" content="FREEHTML5.CO" />

  <!--
	//////////////////////////////////////////////////////

	FREE HTML5 TEMPLATE
	DESIGNED & DEVELOPED by FREEHTML5.CO

	Website: 		http://freehtml5.co/
	Email: 			info@freehtml5.co
	Twitter: 		http://twitter.com/fh5co
	Facebook: 		https://www.facebook.com/fh5co

	//////////////////////////////////////////////////////
	 -->

  	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content=""/>
	<meta property="og:image" content=""/>
	<meta property="og:url" content=""/>
	<meta property="og:site_name" content=""/>
	<meta property="og:description" content=""/>
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />

	<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
	<link rel="shortcut icon" href="favicon.ico">

	<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700,300' rel='stylesheet' type='text/css'>

	<!-- Animate.css -->
	<link rel="stylesheet" href="css/animate.css">
	<!-- Icomoon Icon Fonts-->
	<link rel="stylesheet" href="css/icomoon.css">
	<!-- Bootstrap  -->
	<link rel="stylesheet" href="css/bootstrap.css">
	<!-- Superfish -->
	<link rel="stylesheet" href="css/superfish.css">
	<!-- Magnific Popup -->
	<link rel="stylesheet" href="css/magnific-popup.css">
	<!-- Date Picker -->
	<link rel="stylesheet" href="css/bootstrap-datepicker.min.css">
	<!-- CS Select -->
	<link rel="stylesheet" href="css/cs-select.css">
	<link rel="stylesheet" href="css/cs-skin-border.css">

	<link rel="stylesheet" href="css/style.css">

	<link rel="stylesheet" href="css/messaging.css">
	<link rel="stylesheet" href="css/renting.css">

	<!-- Modernizr JS -->
	<script src="js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
	<![endif]-->
	</head>
<body>
		<div id="fh5co-wrapper">
		<div id="fh5co-page">

		<header id="fh5co-header-section" class="sticky-banner">
			<div class="container">
				<div class="nav-header">
					<a href="#" class="js-fh5co-nav-toggle fh5co-nav-toggle dark"><i></i></a>
					<h1 id="fh5co-logo"><a href="registrado.jsp"><i class="icon-airplane"></i>TIWbnb</a></h1>
					<!-- START #fh5co-menu-wrap -->
					<nav id="fh5co-menu-wrap" role="navigation">
						<ul class="sf-menu" id="fh5co-primary-menu">
							<li class="active"><a href="admin.jsp">Home</a></li>
							<li ><a href="manage_users.jsp">Administrar Usuarios</a></li>
							<li ><a href="resultados.jsp">Administrar Alojamientos</a></li>
							<li ><a href="mensajes">Mensajes</a></li>
							<li><a href="logout" id="Login">Cerrar sesión</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</header>

		<!-- end:header-top -->

			<form ACTION="modify" METHOD="POST" class="form-signin">
				<input type="number" id="inputId" name="inputId" class="form-control" placeholder="Id del usuario">
				<input type="text" id="inputName" name="inputName" class="form-control" placeholder="Nombre" required>
				<input type="text" id="inputSurname" name="inputSurname" class="form-control" placeholder="Apellidos" required>
				<input type="email" id="inputEmail" name="inputEmail" class="form-control" placeholder="Dirección de correo electrónico">
				<input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="Contraseña" required>
				<input type="date" id="inputBirthdate" name="inputBirthdate" class="form-control" placeholder="Fecha de Nacimiento" required>
				<button class="btn btn-lg btn-primary btn-block" type="submit" id="IniciaSesion">Actualizar información</button>
			</form>
			<form ACTION="delete" METHOD="POST" class="form-signin">
				<input type="number" id="inputId" name="inputId" class="form-control" placeholder="Id del usuario">
				<button class="btn btn-lg btn-primary btn-block" type="submit" id="IniciaSesion">Eliminar usuario</button>
			</form>

			<div>
		
		
		<table style="width:100%">
			<tr>
				<th>User ID</th>
				<th>Name</th>
				<th>Surname</th>
				<th>Email</th>
				<th>Password</th>
				<th>Birth date</th>
			</tr>
		
		<%
		
		Connection con = null;
		Statement st = null;
		
		// Open connection
		
		try {
			// Load Driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Connect to the database
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiwbnb", "root", "admin");
			System.out.println("Sucessful connection");
		} catch (Exception e) {
			System.out.println("Error when connecting to the database ");
		}
		
		ResultSet rs = null;
		
		try {
			// Create statement
			st =con.createStatement();

			//Once the statement is created, we need to get the user input for both user email and password

			// Execute statement
			// Here we obtain the full User table
			String query = "SELECT * FROM USER";
			rs = st.executeQuery(query);
			
		} catch (SQLException e) {
			System.out.println("Error when opening table ");
		}
		
		// List <User> userList = (List <User>) request.getAttribute("Users");
		
		while(rs.next()){
			
			out.println("<tr>");
			out.println("<td>"+rs.getInt("USER_ID")+"</td><td>"+rs.getString("USER_NAME")+"</td><td>"+rs.getString("USER_SURNAME")+
					"</td><td>"+rs.getString("USER_EMAIL")+"</td><td>"+rs.getString("USER_PASSWORD")+"</td><td>"+rs.getString("USER_BIRTHDATE")+"</td>");
			out.println("</tr>");
		}	
		%>
		</table>
		</div>

		<!-- Update Modal -->
		<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">

		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		        <h1 class="h3 mb-3 font-weight-normal" id="update_modal_user_title">Here should be the pk to update</h1>
		      </div>

		      <div class="modal-body">

		          <form class="form-signin">
								<input type="text" id="update_modal_user_user" class="form-control" placeholder="Username" required autofocus>
								<input type="text" id="update_modal_user_name" class="form-control" placeholder="Name" required autofocus>
								<input type="text" id="update_modal_user_surname" class="form-control" placeholder="Surname(s)" required autofocus>
					      <input type="email" id="update_modal_user_email" class="form-control" placeholder="Email address" required autofocus>
					      <input type="password" id="update_modal_user_password" class="form-control" placeholder="Password" required>
								<button class="btn btn-lg btn-primary btn-block" type="button" id="Update">Update</button>
		    			</form>

		      </div>
		    </div>
		  </div>
		</div>




		<footer>
			<div id="footer">
				<div class="container">
					<div class="row row-bottom-padded-md">
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>About TIWbnb</h3>
							<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla porttitor enim et libero pharetra, Nam ipsum augue, eleifend ut dui eu, egestas malesuada velit. </p>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>Lorem ipsum </h3>
							<ul>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
							</ul>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>Lorem ipsum </h3>
							<ul>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
							</ul>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>Lorem ipsum </h3>
							<ul>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
							</ul>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>Lorem ipsum </h3>
							<ul>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
							</ul>
						</div>
						<div class="col-md-2 col-sm-2 col-xs-12 fh5co-footer-link">
							<h3>Lorem ipsum </h3>
							<ul>
								<li><a href="#">Xxxxx xxxx</a></li>
								<li><a href="#">Xxxxx xxxx</a></li>
							</ul>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-md-offset-3 text-center">
							<p class="fh5co-social-icons">
								<a href="#"><i class="icon-twitter2"></i></a>
								<a href="#"><i class="icon-facebook2"></i></a>
								<a href="#"><i class="icon-instagram"></i></a>
								<a href="#"><i class="icon-dribbble2"></i></a>
								<a href="#"><i class="icon-youtube"></i></a>
							</p>
						</div>
					</div>
				</div>
			</div>
		</footer>



	</div>
	<!-- END fh5co-page -->

	</div>
	<!-- END fh5co-wrapper -->


	<!-- jQuery -->

	<script src="js/jquery.min.js"></script>
	<!-- jQuery Easing -->
	<script src="js/jquery.easing.1.3.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Waypoints -->
	<script src="js/jquery.waypoints.min.js"></script>
	<script src="js/sticky.js"></script>

	<!-- Stellar -->
	<script src="js/jquery.stellar.min.js"></script>
	<!-- Superfish -->
	<script src="js/hoverIntent.js"></script>
	<script src="js/superfish.js"></script>
	<!-- Magnific Popup -->
	<script src="js/jquery.magnific-popup.min.js"></script>
	<script src="js/magnific-popup-options.js"></script>
	<!-- Date Picker -->
	<script src="js/bootstrap-datepicker.min.js"></script>
	<!-- CS Select -->
	<script src="js/classie.js"></script>
	<script src="js/selectFx.js"></script>

	<!-- Main JS -->
	<script src="js/main.js"></script>

	<script>

		 $(document).ready(function() {
	  //  $('#modify').click(function() {
				$(document).on('click', '.modify', function() {

	  		var key = $(this).siblings('.user_user').text();
				//This values should be the result of a query with the key into the db instead of a copy of the html
				var k_name = $(this).parent().siblings('.user_name').children().children().children().text();
				var k_surname = $(this).parent().siblings('.user_surname').children().children().children().text();
				var k_email = $(this).parent().siblings('.user_email').children().children().children().text();
				var k_password = $(this).parent().siblings('.user_password').children().children().children().text();

				$("#updateModal").modal("show");
//																<a class="info" class="modify">Modify Information</a>

				$("#update_modal_user_title").text(key);
				$("#update_modal_user_user").attr("placeholder", key);
				$("#update_modal_user_name").attr("placeholder", k_name);
				$("#update_modal_user_surname").attr("placeholder", k_surname);
				$("#update_modal_user_email").attr("placeholder", k_email);
				$("#update_modal_user_password").attr("placeholder", k_password);
		 });

		 $('#Update').click(function() {
			 //SEND UPDATES TO DB
			 alert("Updated");
		 });

	 	});



	</script>


	<script>

	function deleteUser() {
		alert("Actualmente este usuario tiene casas en alquiler");
	}


	</script>


	</body>
</html>
