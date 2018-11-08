<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>User Home</title>
</head>
<body>
<h1>
	Welcome to User page!  
</h1>

<a href="<c:url value = '/j_spring_security_logout'/> " > Logout </a>

</body>
</html>
