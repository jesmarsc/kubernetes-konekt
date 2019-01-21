<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<head>
<title>Login</title>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<!--  <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="icon" type="image/png" href="favicons/favicon-16x16.png" sizes="16x16">-->
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="fonts/font-awesome-4.3.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/all.css">
	<link href='http://fonts.googleapis.com/css?family=Montserrat:400,700|Source+Sans+Pro:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
</head>

<body>
<div class="image-widescreen margin-top-image">
    <img src="images/5940.jpg" alt=""/>
  </div>
  <div class="container">
	<h1 class="text-center">Login</h1>
	
	
	<div class="container-fluid col-md-4 col-md-offset-4 text-center">
		<form:form
			action="${pageContext.request.contextPath}/login-confirmation"
			method="POST">

			<c:if test="${param.error != null}">
				<font color="red">You entered an invalid email or password.</font>
				<br>
				<br>
			</c:if>

			<c:if test="${param.logout != null}">
				<font color="blue">You have been logged out.</font>
				<br>
				<br>
			</c:if>

			<!-- User name -->
			<div style="margin-bottom: 25px" class="input-group">
				<span class="input-group-addon"><i
					class="glyphicon glyphicon-user"></i></span> <input type="text"
					name="username" placeholder="username" class="form-control">
			</div>

			<!-- Password -->
			<div style="margin-bottom: 25px" class="input-group">
				<span class="input-group-addon"><i
					class="glyphicon glyphicon-lock"></i></span> <input type="password"
					name="password" placeholder="Password" class="form-control">
			</div>

			<div class="form-group row">
				<input class="btn btn-primary" type="submit" value="Login" />
			</div>

		</form:form>

		<input type="button" onclick="location.href='register'"
			value="Register" class="btn">

	</div>
	</div>
	
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	
</body>
</html>
