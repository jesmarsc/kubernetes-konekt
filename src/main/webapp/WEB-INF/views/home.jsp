<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	<!-- Bootstrap CSS -->
	<link href="css/bootstrap.min.css" rel="stylesheet"/>
	
	<title>Home</title>
</head>
<body class="text-center">
	
	<div class="jumbotron text-center">
		<h1>Welcome to Kubernetes Konekt!</h1><br>
		<img src="images/kubernetes.png" style="height:60px;">
		
		<p> </p>
		<div class="container-fluid col-md-12">
			<sec:authorize access="isAnonymous()">
				<input type="button" onclick="location.href='register'" value="Register" class="btn btn-primary">
				<input type="button" onclick="location.href='login'" value="Login" class="btn btn-primary">
			</sec:authorize>
			<sec:authorize access="hasRole('USER')">
				<input type="button" onclick="location.href='user'"
					value="User Dashboard" class="btn btn-primary">
			</sec:authorize>
			<sec:authorize access="hasRole('PROVIDER')">
				<input type="button" onclick="location.href='provider'"
					value="Provider Dashboard" class="btn btn-primary">
			</sec:authorize>
	
			<input type="button" onclick="location.href='user/list'" value="User List" class="btn btn-primary">
			
			<sec:authorize access="isAuthenticated()">
				<form:form action="${pageContext.request.contextPath}/logout" method="POST">
					<input type="submit" value="Logout" class="btn btn-primary"/>
				</form:form>
			</sec:authorize>
		</div>
	</div>
	
	<!-- Start Div Class for Descriptions -->
	
	<div class="container">
		<div class="row">
			<div class="col-lg-4 col-sm-6 portfolio-item">
          		<div class="card h-45">
          			<h4 class="card-header" align="center">Seamless Cluster Integration</h4>
          			<div class="card-body">
              			<p class="card-text">Kubernetes Konekt allows for multiple connections with multiple purchases. No more need to connect with different clients to support large apps!</p>
            		</div>
          		</div>
        	</div>
        	<div class="col-lg-4 col-sm-6 portfolio-item">
          		<div class="card h-45">
          			<h4 class="card-header" align="center">Command-less Interface</h4>
          			<div class="card-body">
              			<p class="card-text">No need for users to upload their image through multiple command-line arguments. Easily upload a user container and let us do the rest!</p>
            		</div>
          		</div>
        	</div>
        	<div class="col-lg-4 col-sm-6 portfolio-item">
          		<div class="card h-45">
          			<h4 class="card-header" align="center">Easy Payment</h4>
          			<div class="card-body">
              			<p class="card-text">With most major credit cards accepted and PayPal being set up, easy payment is only minutes away! *Soon to Accept Venmo, etc.</p>
            		</div>
          		</div>
        	</div>
		</div>
	</div>
	
	<!-- Testimonials -->
	
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
