<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cluster Upload Confirmation</title>
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
		<div class="jumbotron">
			<h1 class="display-3">Upload Successful</h1>
			<p class="lead">You have successfully uploaded cluster with IP
				address: ${clusterIp}</p>
		</div>
	</div>
</body>
</html>