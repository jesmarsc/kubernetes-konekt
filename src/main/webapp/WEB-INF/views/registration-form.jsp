<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="en">

<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="/css/registration.css" rel="stylesheet" type="text/css">
<link href="fonts/font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<%-- <style>
.error {
	color: red
}
</style>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	
	<!-- Bootstrap CSS -->
	<link rel="stylesheet"
		href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>
	
	<title>Registration</title>
</head>

<body class="text-center">
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

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
	<h1>Registration Form</h1>

	<h1>
		<font color="red">${message} </font>
		<!-- use to let user know they made an error. -->
	</h1>
	
	<div class="container-fluid col-sm-12 col-md-6">
		<form:form action="accountConfirmation" modelAttribute="registrationForm">
			<div class="form-group row">
				<label> Username: </label>
				<form:input class="form-control" path="userName" />
				<form:errors path="userName" cssClass="error" />
			</div>
			
			<div class="form-group row">
				<label> First Name: </label>
				<form:input class="form-control" path="firstName" />
				<form:errors path="firstName" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Last Name: </label>
				<form:input class="form-control" path="lastName" />
				<form:errors path="lastName" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Role:  </label>
				<form:select class="form-control" path="role">
					<form:options items="${registrationForm.accountRoles}" />
				</form:select>
				<form:errors path="role" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Email: </label>
				<form:input  class="form-control" path="email" />
				<form:errors path="email" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Confirm Email: </label>
				<form:input class="form-control" path="confirmEmail" />
				<form:errors path="confirmEmail" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Password: </label>
				<form:password class="form-control" path="password" />
				<form:errors path="password" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Confirm Password: </label>
				<form:password class="form-control" path="confirmPassword" />
				<form:errors path="confirmPassword" cssClass="error" />
			</div>

			<div class="form-group row">
				<input class="btn btn-primary text-center" type="submit" value="Submit" />
			</div>
		</form:form>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html> --%>

<section class="register-block" style='background-image:url("/images/7135.jpg")'>
    <div class="container">
	<div class="row">
		<div class="col-md-4 register-sec">
		    <h2 class="text-center">Register</h2>
		    			
		
  		<form:form
			action="accountConfirmation" modelAttribute="registrationForm">
			
			<div class="form-group row">
				<form:input class="form-control" path="userName" placeholder = "Username"/>
				<form:errors path="userName" cssClass="error" />
			</div>
			
 			<div class="form-group row">
				<form:input class="form-control" path="firstName"  placeholder = "First Name"/>
				<form:errors path="firstName" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:input class="form-control" path="lastName" placeholder = "Last Name" />
				<form:errors path="lastName" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:select class="form-control" path="role">
					<form:options items="${registrationForm.accountRoles}" />
				</form:select>
				<form:errors path="role" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:input  class="form-control" path="email" placeholder = "Email Address"/>
				<form:errors path="email" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:input class="form-control" path="confirmEmail" placeholder = "Confirm Email Address" />
				<form:errors path="confirmEmail" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:password class="form-control" path="password" placeholder = "Password" />
				<form:errors path="password" cssClass="error" />
			</div>

			<div class="form-group row">
				<form:password class="form-control" path="confirmPassword" placeholder = "Confirm Password"/>
				<form:errors path="confirmPassword" cssClass="error" />
			</div>
			<div class="form-group row">
				<input class="btn btn-primary float-right" type="submit" value="Register" />
			</div>
		</form:form>
				
  		</div>

	<div class="col-md-8 banner-sec">
    	<img class="d-block img-fluid" src="https://i.pinimg.com/originals/39/0b/9b/390b9b3ea5f1df1d315ba7928beb8949.jpg">
	</div>
	</div>
</div>
</section>
 
