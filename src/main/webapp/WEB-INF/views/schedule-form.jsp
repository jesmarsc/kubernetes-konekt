<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html class="no-js" lang="en">

<style>
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
	
	<h1>Scheduling Form</h1>




<div class="container-fluid col-sm-12 col-md-6">
		<form:form action="scheduleConfirmation" modelAttribute="scheduleForm">
			<div class="form-group row">
				<label> Seconds: </label>
				<form:input class="form-control" path="second" />
				<form:errors path="second" cssClass="error" />
			</div>
						<div class="form-group row">
				<label> Minutes: </label>
				<form:input class="form-control" path="minute" />
				<form:errors path="minute" cssClass="error" />
			</div>
			
			<div class="form-group row">
				<label> Hour: </label>
				<form:input class="form-control" path="hour" />
				<form:errors path="hour" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Date: </label>
				<form:input class="form-control" path="date_" />
				<form:errors path="date_" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Month: </label>
				<form:input class="form-control" path="month" />
				<form:errors path="month" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Day Of Week: </label>
				<form:input class="form-control" path="dayofWeek" />
				<form:errors path="dayofWeek" cssClass="error" />
			</div>
			<div class="form-group row">
				<input class="btn btn-primary text-center" type="submit" value="Submit" />
			</div>
		</form:form>
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