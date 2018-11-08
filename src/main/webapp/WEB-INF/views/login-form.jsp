<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<title>Login</title>
	
	<style>
		.error {color:red}
	</style>
</head>
<body>
	<form:form action="dashboard" modelAttribute="user">
	
		Username *: <form:input type="text" path="userName"/>
		<form:errors path="userName" cssClass="error"/><br><br>
		
		Password *: <form:input type="password" path="passWord"/>
		<form:errors path="passWord" cssClass="error"/><br><br>
		
		Account:
		<form:select path="accType">
			<form:options items="${user.accTypes}"/>
		</form:select><br><br>
		
		<input type="submit" value="Login"/>
	</form:form>
</body>
</html>
