<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Kubernetes Konekt</title>
	
	
    

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" >
	<link rel="stylesheet" href="fonts/font-awesome-4.3.0/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Maven+Pro" />
	<link href='http://fonts.googleapis.com/css?family=Montserrat:400,700|Source+Sans+Pro:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
	   <link href="https://fonts.googleapis.com/css?family=Maven+Pro:bold|700" rel="stylesheet">
    
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link href="/css/light-bootstrap-dashboard.css?v=2.0.1" rel="stylesheet" />
	<style>
	*{
		font-family: "Maven Pro";  
		font-style: normal; 
		font-variant: normal;
		
	}
	body {
		background-color: #006CFF;
		
	}
	h1 { font-family: "Maven Pro"; font-size: 40px; font-style: normal; font-variant: normal; font-weight: 700; line-height: 26.4px; } 
	h3 { font-family: "Maven Pro"; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 700; line-height: 15.4px; }
	p { font-family: "Maven Pro"; font-size: 14px; font-style: normal; font-variant: normal; font-weight: 400; line-height: 20px; } 
	blockquote { font-family: "Maven Pro"; font-size: 21px; font-style: normal; font-variant: normal; font-weight: 400; line-height: 30px; }
	pre { font-family: "Maven Pro"; font-size: 13px; font-style: normal; font-variant: normal; font-weight: 400; line-height: 18.5714px; }
	.navbar-inner {
    background:transparent;
}
	
	</style>
</head>
<body>

		
	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-inner navbar-expand-lg ">
				<div class=" container  ">
				
					<a class="navbar-brand" style="color:white !important;" href="${pageContext.request.contextPath}/"><font size="6">Kubernetes
						Konekt </font></a>
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
								<li class="nav-link"><a class="tocxref btn btn-round"
									style=" background-color: #ffffff !important; font-size: 24px;"
									href="/user"> User
										Dashboard </a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('PROVIDER')">
								<li class="nav-btn"><a class="btn btn-warning btn-round"
									style=" background-color: #ffffff !important; font-size: 24px;"
									href="/provider">
										Provider Dashboard </a></li>
							</sec:authorize>
							
							<sec:authorize access="hasAnyRole('PROVIDER','USER')">
							<li class="nav-item"><form:form
									action="${pageContext.request.contextPath}/logout"
									method="POST">
									<input type="submit" value="Logout" class="btn btn-danger btn-round"style=" background-color: #ffffff !important; font-size: 24px;" />
								</form:form></li>
							</sec:authorize>
							<sec:authorize access="hasRole('ANONYMOUS')">
							<li class="nav-item"><form:form
									action="/login"
									method="POST">
									<input type="submit" value="Login" class="btn btn-primary btn-round" style=" background-color: #ffffff !important; font-size: 24px;"/>
								</form:form></li>
							</sec:authorize>

						</ul>
					</div>

				</div>
			</nav>
	</div>


	<div class="row">
		<div class="col-lg-6 ">
		<div class="container ">
			
			<div class="text-block text-center " style="color: #fff;">
				

					<h1>No Cluster? No problem.</h1>

				<h2>Upload your containers and that's it!</h2>
				
				<h1>No Job? No problem.</h1>

				<h2>Host Kubernetes clusters and start earning!</h2>

				<br>
				<button type="button" class="btn btn-primary btn-round" style=" background-color: #ffffff !important;" onclick="location.href='/register'" type="submit" >Sign Up Now</button>
			
			<br>
			<br>
			</div>
	
		</div>
		</div>
		<div class="col-lg-6">
			<img src="images/home-page-image.png"  style="width:80%;" class="img-fluid" alt="Responsive image">
		</div>
	</div>
	<div class="fixed-bottom">
		<img src="images/clouds.png" alt="">
	</div>
	
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script> 
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
