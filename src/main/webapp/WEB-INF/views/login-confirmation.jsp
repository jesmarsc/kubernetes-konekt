<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<title>Dashboard</title>
</head>
<body>
	Welcome back ${user.userName}!<br>
	Account type: ${user.accType}<br>
	
	<form:form modelAttribute="user">
		<form:select path="accType">
			<form:options items="${user.accTypes}"/>
		</form:select>
		<input type="submit"/>
	</form:form>
</body>
</html>
