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

	<h1>Welcome to Kubernetes Konekt!</h1>
	
	<div class="container-fluid col-md-12">
		<input type="button" onclick="location.href='register'" value="Register" class="btn">
		<input type="button" onclick="location.href='login'" value="Login" class="btn">
		<sec:authorize access="hasRole('USER')">
			<input type="button" onclick="location.href='user'"
				value="User Dashboard" class="btn">
		</sec:authorize>
		<sec:authorize access="hasRole('PROVIDER')">
			<input type="button" onclick="location.href='provider'"
				value="Provider Dashboard" class="btn">
		</sec:authorize>

		<input type="button" onclick="location.href='user/list'" value="User List" class="btn">
		<form:form action="${pageContext.request.contextPath}/logout" method="POST">
			<input type="submit" value="Logout" class="btn btn-primary"/>
		</form:form>
	</div>
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
