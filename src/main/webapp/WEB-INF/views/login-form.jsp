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
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>

<body class="text-center">

	<h1>Login</h1>
	<div class="container-fluid col-md-4 col-md-offset-4">
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
					name="username" placeholder="Email" class="form-control">
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
</body>
</html>
