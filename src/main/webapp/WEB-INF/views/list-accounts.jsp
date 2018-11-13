<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>

<head>
	<title>List User Accounts</title>
	
	<!-- reference our style sheet -->

	<!-- Bootstrap CSS -->
	<link rel="stylesheet"
		href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>

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

	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-10">

		<div id="wrapper">
			<div id="header">
				<h2>KCL - Kubernetes Kontakt List</h2>
			</div>
		</div>

		<div id="container">

			<div id="content">

				<!--  add our html table here -->

				<table>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
					</tr>

					<!-- loop over and print our customers -->
					<c:forEach var="tempUser" items="${accounts}">

						<tr>
							<td>${tempUser.firstName}</td>
							<td>${tempUser.lastName}</td>
							<td>${tempUser.email}</td>
						</tr>

					</c:forEach>

				</table>

			</div>

		</div>
	</div>

</body>

</html>
