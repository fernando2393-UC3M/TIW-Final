
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
		<%@ page contentType="text/html; charset=UTF-8" %>
	<%@ page import="java.util.List" %>
	<%@ page import="java.util.Date" %>
	<%@ page import="java.util.Iterator" %>
	<%@ page import="model.Home" %>
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
					<h1 id="fh5co-logo"><a href="index"><i class="icon-airplane"></i>TIWbnb</a></h1>
					<!-- START #fh5co-menu-wrap -->
					<nav id="fh5co-menu-wrap" role="navigation">
						<ul class="sf-menu" id="fh5co-primary-menu">
						<%
						
						if(session.getAttribute("user") != null) {
							
							out.println("<li class=\"active\"><a href=\"index\">Home</a></li><li ><a href=\"viajes\">Viajes</a></li><li ><a href=\"casa\">Ofrece Alojamiento</a></li><li ><a href=\"renting\">Mis Alojamientos</a></li><li ><a href=\"mensajes\">Mensajes</a></li><li><a href=\"registrado\">Perfil</a></li><li><a href=\"logout\">Cerrar Sesión</a></li>");
							
						}
						
						%>
						
						<%
						
						if(session.getAttribute("user") == null) {
							
							out.println("<li class=\"active\"><a href=\"index\">Home</a></li>");       
							
						}
						
						%>
						
						</ul>
					</nav>
				</div>
			</div>
		</header>

		<!-- end:header-top -->

		<div id="fh5co-tours" class="fh5co-section-gray">
			<div class="container">
				<div class="row">
					<div class="col-md-8 col-md-offset-2 text-center heading-section animate-box">
						<h3>Resultados</h3>
					</div>
				</div>
				<div class="row row-bottom-padded-md">
		<%
		
		List <Home> resultHomes = (List <Home>) request.getAttribute("resultHomes");

		
		if(resultHomes != null && resultHomes.size()>0) {
							Iterator i = resultHomes.iterator();
							
							
							while(i.hasNext()){
								Home curr = (Home)i.next();
								
								out.println("<div class=\"col-sm-6 col-md-6\">");
								out.println("<div class=\"tabulation animate-box\">");
								
								out.println("<ul class=\"nav nav-tabs\" role=\"tablist\">");
							    out.println("<li role=\"presentation\" class=\"active\">");
							    out.println("<a href=\"#alojamientos\" aria-controls=\"alojamientos\" role=\"tab\" data-toggle=\"tab\">Alojamiento</a>");
							    out.println("</li>");
							   	out.println("</ul>");
							   	
							   	out.println("<div class=\"tab-content\">");
							   	out.println("<div role=\"tabpanel\" class=\"tab-pane active\" id=\"hotels\">");
							   	out.println("<div class=\"row\">");
							   	out.println("<div class=\"col-xxs-12 col-xs-12 mt\">");
							   	out.println("<div class=\"input-field\">");
							   	out.println("<label>" + curr.getHomeName() + "</label>");
							   	out.println("<a class=\"info\" href=\"detail?id=" + curr.getHomeId() +"\"></a>");
							   	out.println("<br>");
							   	out.println("<a href=\"alojamiento?id=" + curr.getHomeId() + "\"><img alt=\"" + curr.getHomeName() + "\" src=\"" + curr.getHomePhotos() + "\" max></a>");
							   	out.println("</div>");
							   	out.println("<br>");
							   	out.println("<div class=\"col-xxs-12 col-xs-12 mt\">");
							   	out.println("<label>Alquilada:</label><font class=\"rented\" color=\"green\">Alquilada</font>");
							   	out.println("</div>");
							   	out.println("<div class=\"col-xxs-12 col-xs-12 mt\">");
								out.println("<label>Descripción:</label>");
								out.println("<p><font color=\"black\">" + curr.getHomeDescriptionFull() + "</font>");
								out.println("</p>");											
								out.println("</div>");
								out.println("</div>");                                 
								
								out.println("<div class=\"col-xs-12\">");
								//out.println("<a href=\"deleteHome?id=" + curr.getHomeId() + "\"> <input type=\"button\" class=\"btn btn-primary btn-block\" value=\"Eliminar Alojamiento\"></a>");
								
								out.println("</div>");
								out.println("</div>");
	   							out.println("</div>");
								out.println("</div>");
								out.println("</div>");
								out.println("</div>");
								
							   	
							}
			}
			else{
				out.println("<div class=\"col-sm-6 col-md-6\">");
				out.println("<div class=\"tabulation animate-box\">");
				out.println("<label>" + "No se encuentran resultados según los criterios de búsqueda" + "</label>");
				out.println("</div>");
				out.println("</div>");

			}

		%>
					
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
		
    </script>
        
	</body>
</html>

