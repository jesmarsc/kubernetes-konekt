<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html class="no-js" lang="en">

<style>
.error {
	color: red
}
</style>

<head>  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YAML Builder</title>
    <meta name="description" />
    <meta name="generator">
    <link rel="icon" href="http://themes.guide/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="http://themes.guide/favicon.ico" type="image/x-icon" />
<<<<<<< HEAD
    <meta property="og:image" name="twitter:image" >
    <meta name="twitter:card" >
    <meta name="twitter:site" >
    <meta name="twitter:creator" >
    <meta name="twitter:title" >
    <meta name="twitter:description" >
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
    <link href="../css/theme.css" rel="stylesheet">
    
    

=======
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
    <link href="../css/theme.css" rel="stylesheet">
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9

</head>

<body>
<<<<<<< HEAD
=======

	<!-- Navbar -->
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
			<a class="navbar-brand" href="#">Kubernetes Konekt</a>

<<<<<<< HEAD
	<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent">
				<span class="navbar-toggler-icon"></span>
			</button>
		
=======
			<button class="navbar-toggler dropdown-toggle" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    			<span class="navbar-toggler-icon"></span>
  			</button>
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9


			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ml-auto">

					<li class="nav-item" ><a class="nav-link" href="/" >Home</a></li>
					<sec:authorize access="hasRole('USER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user">User Dashboard</a></li>

					</sec:authorize>
					<sec:authorize access="hasRole('PROVIDER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/provider"> Provider Dashboard</a></li>
					</sec:authorize>
					<li class="nav-item"><a class="nav-link" href="#">
							Messages </a></li>

					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navDropdown"
						data-toggle="dropdown"> Alerts </a> <!-- later these alerts will be read from database for now theres a dummy dropdown menu -->
						<div class="dropdown-menu">
							<a class="dropdown-item" href="#"> Cluster 1 Started </a> <a
								class="dropdown-item" href="#"> Cluster 2 Started </a> <a
								class="dropdown-item" href="#"> Cluster 1 Stopped </a>
						</div></li>

					<li class="nav-item"><a class="nav-link" href="#">Profile </a>
					</li>

					<li class="nav-item">
					<form:form action="${pageContext.request.contextPath}/logout" method="POST">
						<input type="submit" value="Logout" class="btn btn-primary"/>
					</form:form>
					</li>

				</ul>
			</div>
		</nav>
	</div>
<<<<<<< HEAD
	<h1 class="text-center">Yaml Builder Form</h1>

	<h1>
		<font color="red">${message} </font>
		<!-- use to let user know they made an error. -->
	</h1>
=======
	
	<!-- Begin Body -->
	
	<div class="container-fluid col-xs-12 col-sm-6">
		
		<h1> Yaml Builder Form </h1>
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9

		<h1>
			<font color="red">${message} </font>
			<!-- use to let user know they made an error. -->
		</h1>
		
		<table class="table table-hover table-bordered">
		
		<form:form action="YamlBuildConfirmation"
			modelAttribute="YamlBuilderForm">

			<div class="form-group">
				<label> Deployment Name: </label>
					<form:input class="form-control" path="deploymentName" />
					<form:errors path="deploymentName" cssClass="error" />
			</div>
			<div class="form-group">
				<label> Image: </label>
				<form:input class="form-control" path="image" />
				<form:errors path="image" cssClass="error" />
			</div>
			<div class="form-group">
				<label> replicas: </label>
				<form:input class="form-control" path="replicas" />
				<form:errors path="replicas" cssClass="error" />
			</div>
			<div class="form-group">
				<label> Container Port: </label>
				<form:input class="form-control" path="containerPort" />
				<form:errors path="containerPort" cssClass="error" />
			</div>
			<div class=" form-group">
				<h3>Metadata: Labels</h3>
			</div>

<<<<<<< HEAD
			<div class="form-group text-center">
				<label class="control-label text-center" for="key">Key</label>
				<div class="col-md-5">
=======
			<div class="form-group">
				<label class="col-md-4 control-label" for="key">Key</label>
				<div class="col-md-6">
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9
					<form:input id="key" name="key" type="text" placeholder=""
						class="form-control input-md" path="key" />
					<form:errors path="key" cssClass="error" />
				</div>
			</div>
			<br>
			<!-- Text input-->
			<div class="form-group">
<<<<<<< HEAD
				<label class="col-md-4 control-label text-center" for="value">Value</label>
				<div class="col-md-5">
=======
				<label class="col-md-4 control-label" for="value">Value</label>
				<div class="col-md-6">
>>>>>>> f0e95625ad7c33a869c54b953a1b1001334825c9
					<form:input id="value" name="value" type="text" placeholder=""
						class="form-control input-md " path="value" />
					<form:errors path="value" cssClass="error" />
				</div>
			</div>
			<br>
			<div class="form-group">
				<input class="btn btn-primary text-center" type="submit"
					value="Submit" />
			</div>
			
		</form:form>
		
		</table>
	</div>
		
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
		
</body>
</html>
