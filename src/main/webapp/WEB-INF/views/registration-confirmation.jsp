<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Home</title>
	<!-- Bootstrap CSS -->
    	<link rel="stylesheet" 
    		href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>
	
</head>
<body>
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-10">

		<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
			<a class="navbar-brand" href="#">Kubernetes Konekt</a>

			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent">
				<span class="navbar-toggler-icon"></span>
			</button>


			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ml-auto">

					<li class="nav-item"><a class="nav-link" href="/"> Home </a></li>
				</ul>
			</div>
		</nav>
	</div>
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-10">
		<h1>Account Created!</h1>
		<br>
		<br> Name: ${userRegistration.firstName}
			${userRegistration.lastName} <br>
		<br> Email: ${userRegistration.email} <br>
		<br> Role: ${userRegistration.role} <br>
		<br>
	</div>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
		></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
		></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
		></script>
</body>
</html>
