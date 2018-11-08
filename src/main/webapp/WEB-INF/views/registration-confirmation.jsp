<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Account Created!  
</h1>
<br><br>

Name: ${userRegistration.firstName}  ${userRegistration.lastName}

<br><br>
Email: ${userRegistration.email}

<br><br>
Role: ${userRegistration.role}

<input type="button" onclick="location.href='/'" value="Home">
</body>
</html>
