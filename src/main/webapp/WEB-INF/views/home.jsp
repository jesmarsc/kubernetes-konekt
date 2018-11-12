<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
		<input type="button" onclick="location.href='user'" value="User Login" class="btn">
		<input type="button" onclick="location.href='provider'" value="Provider Login" class="btn">
		<input type="button" onclick="location.href='user/list'" value="User List" class="btn">
		<form:form action="${pageContext.request.contextPath}/logout" method="POST">
			<input type="submit" value="Logout" class="btn btn-primary"/>
		</form:form>
	</div>
</body>
</html>
