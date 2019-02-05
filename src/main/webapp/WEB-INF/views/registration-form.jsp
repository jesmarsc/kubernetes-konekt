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
 
