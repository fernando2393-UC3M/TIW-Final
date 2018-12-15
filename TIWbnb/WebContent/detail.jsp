<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
	<%@ page contentType="text/html; charset=UTF-8" %>
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

	<!-- Modernizr JS -->
	<script src="js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
	<![endif]-->
	</head>
<body>
		<%@ page import="java.util.List" %>
		<%@ page import="java.util.Date" %>
		<%@ page import="java.util.Iterator" %>
		<%@ page import="java.sql.DriverManager" %>
		<%@ page import="java.sql.Connection" %>
		<%@ page import="java.sql.Statement" %>
		<%@ page import="java.sql.ResultSet" %>
		<%@ page import="java.sql.SQLException" %>
		<%@ page import="javax.ws.rs.client.*" %>
		<%@ page import="javax.ws.rs.core.MediaType" %>
		<%@ page import="javax.ws.rs.core.Response" %>
		<%@ page import="javax.ws.rs.core.GenericType" %>
		<%@ page import="model.User" %>
		<%@ page import="model.Home" %>
		
		<%! final String HOMES_API_URL = "http://localhost:10002/homes/"; 
			Home home;
		%>

		<% 
		int id = Integer.parseInt(request.getParameter("id"));

		Client client = ClientBuilder.newClient();
		WebTarget webResource = client.target(HOMES_API_URL).path("" + id);
		Invocation.Builder invocationBuilder = webResource.request(MediaType.APPLICATION_JSON);
		
		Response resp = invocationBuilder.get();
		
		home = resp.readEntity(new GenericType<Home>(){}); %>
		
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
							<li class="active"><a href="index">Home</a></li>
							<li ><a href="viajes">Viajes</a></li>
							<li ><a href="casa">Ofrece Alojamiento</a></li>
							<li ><a href="renting">Mis Alojamientos</a></li>
							<li ><a href="mensajes">Mensajes</a></li>                                
							<li><a href="registrado">Perfil</a></li>
							<li><a href="logout">Cerrar Sesión</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</header>

		<!-- end:header-top -->

		<div class="fh5co-hero">
			<div class="fh5co-overlay"></div>
			<div class="fh5co-cover" data-stellar-background-ratio="0.5" style="background-image: url(images/cover_bg_5.jpg);">
				<div class="desc">
					<div class="container">
						<div class="row">
							<div class="col-sm-7 col-md-7">
								<!-- <a href="index" id="main-logo">Travel</a> -->
								<div class="tabulation animate-box">

								  <!-- Nav tabs -->
								   <ul class="nav nav-tabs" role="tablist">
								      <li role="presentation" class="active">
								    	   <a href="#alojamientos" aria-controls="alojamientos" role="tab" data-toggle="tab">Modifica tu Alojamiento</a>
								      </li>
								   </ul>

								   <!-- Tab panes -->
									<div class="tab-content">
									 <div role="tabpanel" class="tab-pane active" id="hotels">
									 <form class="form-signin" METHOD="POST" ACTION="modifyHome?id=<%= home.getHomeId() %>">
									 	<div class="row">
													<div class="col-xxs-12 col-xs-12 mt">



														<div class="input-field">
															<label for="name">Nombre:</label> <input type="text"
																class="form-control" id="name" name="houseName" value="<%= home.getHomeName() %>" />
														</div>
													</div>
													<div class="col-xxs-12 col-xs-12 mt alternate">
														<div class="input-field">
															<label for="desc">Ciudad:</label> <input
																type="text" class="form-control" id="desc" name="houseCity"
																value="<%= home.getHomeCity() %>" />
														</div>
													</div>
													<div class="col-xxs-12 col-xs-12 mt alternate">
														<div class="input-field">
															<label for="desc">Descripción:</label> <input
																type="text" class="form-control" id="desc" name="houseDesc"
																value="<%= home.getHomeDescriptionFull() %>" />
														</div>
													</div>
													<div class="col-xxs-12 col-xs-12 mt alternate">
														<div class="input-field">
															<label for="subdesc">Resumen:</label> <input
																type="text" class="form-control" id="subdesc" name="houseSubDesc" value="<%= home.getHomeDescriptionShort() %>"/>
														</div>
													</div>
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
														<label for="type">Tipo de Alojamiento:</label>
														<font color="black">
															<select id="type" name="houseType">
																<% if(home.getHomeType().equals("Apartamento")) { %>
																	<option value="Apartamento" selected="selected">Apartamento</option>
																	<option value ="Privado">Habitación Privada</option>
																<% } else { %>
																	<option value="Apartamento">Apartamento</option>
																	<option value="Privado" selected="selected">Habitación Privada</option>
																<% } %>
															</select>
															</font>
														</div>
													</div>
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
															<label for="guests">Número de Huéspedes:</label> <input
																type="number" class="form-control" id="guests" name="guests" value="<%=home.getHomeGuests()%>"/>
														</div>
													</div>
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
															<label for="picture">Foto:</label> <input
																type="file" name="photo" class="form-control" id="picture" value="<%=home.getHomePhotos() %>" accept="image/*"/>
														</div>
													</div>
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
															<label for="price">Precio por Noche:</label> <input
																type="number" class="form-control" id="price" name="inputPriceNight" value="<%= home.getHomePriceNight() %>"/>
														</div>
													</div>
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
															<label for="date0">Fecha Inicial:</label> <input
																type="date" name="iDate" class="form-control" id="date0" value="<%= home.getHomeAvDateInit() %>"/>
														</div>
													</div>
													<!-- <form class="form-signin" METHOD="POST" ACTION="casa"> -->
													<div class="col-xxs-12 col-xs-6 mt alternate">
														<div class="input-field">
															<label for="date1">Fecha Final:</label> <input
																type="date" name="fDate" class="form-control" id="date1" value="<%= home.getHomeAvDateFin()%>"/>
														</div>
													</div>

											<div class="col-xs-12">
												<input type="submit" name="submitHouse" class="btn btn-primary btn-block" value="Guardar Alojamiento">
											</div>
										<!-- </form> -->
										</form>
                  </div>
				                    </div>
								 </div>


								</div>
							</div>
						</div>
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
	function savedInfo() {
		alert("Alojamiento registrado con éxito.");
	}
	</script>


	</body>
</html>
