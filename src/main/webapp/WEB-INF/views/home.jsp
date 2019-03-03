<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Kubernetes Konekt</title>
	
	<link rel="icon" type="image/png" href="favicons/favicon-16x16.png" sizes="16x16">

    

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" >
	<link rel="stylesheet" href="fonts/font-awesome-4.3.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/all.css">
	<link href='http://fonts.googleapis.com/css?family=Montserrat:400,700|Source+Sans+Pro:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
	   <link href="https://fonts.googleapis.com/css?family=Maven+Pro:bold|700" rel="stylesheet">
    
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link href="/css/light-bootstrap-dashboard.css?v=2.0.1" rel="stylesheet" />
</head>
<body>

		
	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-expand-lg ">
				<div class=" container  ">
					<a class="navbar-brand" href="${pageContext.request.contextPath}/">Kubernetes
						Konekt </a>
					<div class="collapse navbar-collapse" id="navigation"></div>

					<button class=" navbar-toggler navbar-toggler-right " type="button"
						data-toggle="collapse" aria-controls="navigation-index"
						aria-expanded="false" aria-label="Toggle navigation">
						<span class="navbar-toggler-bar burger-lines"></span> <span
							class="navbar-toggler-bar burger-lines"></span> <span
							class="navbar-toggler-bar burger-lines"></span>
					</button>


					<button class="navbar-toggler" type="button" data-toggle="collapse"
						data-target="#TopNavBarContent">
						<span class="navbar-toggler-icon"></span>
					</button>

					<div class="collapse navbar-collapse" id="TopNavBarContent">

						<ul class="navbar-nav ml-auto">

							<sec:authorize access="hasRole('USER')">
								<li class="nav-link"><a class="tocxref btn"
									style="color: black;"
									href="${pageContext.request.contextPath}/user"> User
										Dashboard </a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('PROVIDER')">
								<li class="nav-btn"><a class="btn btn-warning"
									style="color: black;"
									href="${pageContext.request.contextPath}/provider">
										Provider Dashboard </a></li>
							</sec:authorize>
							
							<sec:authorize access="hasAnyRole('PROVIDER','USER')">
							<li class="nav-item"><form:form
									action="${pageContext.request.contextPath}/logout"
									method="POST">
									<input type="submit" value="Logout" class="btn btn-danger" />
								</form:form></li>
							</sec:authorize>
							<sec:authorize access="hasRole('ANONYMOUS')">
							<li class="nav-item"><form:form
									action="/login"
									method="POST">
									<input type="submit" value="Login" class="btn btn-primary" />
								</form:form></li>
							</sec:authorize>

						</ul>
					</div>

				</div>
			</nav>
	</div>

	
		<section class="visual">
		<div class="container">
			<div class="text-block">
				<div class="heading-holder">
					<h1>Welcome to Kubernetes Konekt!</h1>
					
				</div>
				<p class="tagline">Connecting containers to clusters!</p>
				<span class="info">Get connected now</span>
			</div>
		</div>

	</section>

	
		
		<p> </p>
		
		<section class="area">
		<div class="container">
			<div class="row">
				<div class="col-md-5">
					<ul class="visual-list">
						<li>
							<div class="img-holder">
								<img src="images/online-payment.svg" width="110" alt="">
							</div>
							<div class="text-holder">
								<h3>Easy Payment</h3>
								<p>With most major credit cards accepted and PayPal being set up, easy payment is only minutes away! *Soon to Accept Venmo, etc. </p>
							</div>
						</li>
						<li>
							<div class="img-holder">
								<img class="pull-left" src="images/integration.svg" width="90" alt="">
							</div>
							<div class="text-holder">
								<h3>Seamless Cluster Integration</h3>
								<p>Kubernetes Konekt allows for multiple connections with multiple purchases. No more need to connect with different clients to support large apps! </p>
							</div>
						</li>
						<li>
							<div class="img-holder">
								<img src="images/user.svg" height="84" alt="">
							</div>
							<div class="text-holder">
								<h3>Command-less Interface</h3>
								<p>No need for users to upload their image through multiple command-line arguments. Easily upload a user container and let us do the rest! </p>
							</div>
						</li>
					</ul>
				</div>
				<div class="col-md-7">
					<div class="slide-holder">
						<div class="img-slide scroll-trigger"><img src="https://outcrawl.com/static/76acbf5eebdbc17b190ddb2d02988e64/b3b80/cover.jpg" height="365" width="650" alt=""></div>
					</div>
				</div>
			</div>
		</div>
	</section>
		
	<!-- </div> -->
	
	
	</br></br>

	<div class="container mt-3">
		<!-- Left-aligned --> 
		<div class="media border p-3">
			<div class="media-left">
				<img src="images/img_avatar1.png" class="mr-3 mt-3 rounded-circle" style="width:60px;">
			</div>
			<div class="media-body" align="left">
				<h4 class="media-heading">Frank Lagomarsino <small><i>August 19, 2018</i></small></h4>
				<p>Kubernetes Konekt really helped me with seamlessly connecting me to a kubernetes cluster. I was able to launch my
				application with ease and paid within 5 minutes!</p>
			</div>
		</div>
		
		<!-- Right-aligned -->
		<div class="media border p-3">
			<div class="media-body" align="left">
		  		<h4 class="media-heading">John Dough <small><i>November 30, 2018</i></small></h4>
		    	<p>I cannot believe how easy it was to upload my container and launch my application! Usually it takes me more than half
		    	an hour trying to run my endless list of commands, but all I had to do was click three buttons.</p>
		  	</div>
		  	<div class="media-right">
		    	<img src="images/img_avatar1.png" class="mr-3 mt-3 rounded-circle" style="width:60px;">
		  	</div>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script> 
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
