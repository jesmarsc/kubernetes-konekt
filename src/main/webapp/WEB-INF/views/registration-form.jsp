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
	<link href="/bootstrap.min.css" rel="stylesheet"/>
	
	<title>Registration</title>
</head>

<body class="text-center">

	<h1>Registration Form</h1>

	<h1>
		<font color="red">${message} </font>
		<!-- use to let user know they made an error. -->
	</h1>
	
	<div class="container-fluid col-sm-12 col-md-6">
		<form:form action="accountConfirmation" modelAttribute="userRegistration">
			<div class="form-group row">
				<label>First Name: </label>
				<form:input class="form-control" path="firstName" />
				<form:errors path="firstName" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Last Name: </label>
				<form:input class="form-control" path="lastName" />
				<form:errors path="lastName" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> User Role:  </label>
				<form:select class="form-control" path="role">
					<form:options items="${userRegistration.accountRoles}" />
				</form:select>
				<form:errors path="role" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Email: </label>
				<form:input  class="form-control" path="email" />
				<form:errors path="email" cssClass="error" />
			</div>

			<div class="form-group row">
				<label> Confirm Email:</label>
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

</body>
</html>
