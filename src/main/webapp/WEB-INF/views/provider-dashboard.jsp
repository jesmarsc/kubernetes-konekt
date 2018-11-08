<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Begin HTML Document -->
<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link href="/bootstrap.min.css" rel="stylesheet" />

<!-- Title at the Tab of the Browser -->
<title>Provider Dashboard</title>

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

					<li class="nav-item"><a class="nav-link" href="#"> Home </a></li>

					<li class="nav-item"><a class="nav-link" href="#">
							Messages </a></li>

					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navDropdown"
						data-toggle="dropdown"> Alerts </a> <!-- later these alerts will be read from database for now there's a dummy drop down menu -->
						<div class="dropdown-menu">
							<a class="dropdown-item" href="#"> Frank Smith wants to buy
								Bad Cluster </a> <a class="dropdown-item" href="#"> Jesmar paid
								you $123.12 </a> <a class="dropdown-item" href="#"> Your account
								balance is $3456.02 </a>
						</div></li>

					<li class="nav-item"><a class="nav-link" href="#">Profile</a>
					</li>

					<li class="nav-item"><a class="nav-link"
						href="<c:url value = '/j_spring_security_logout'/> ">Logout </a></li>

				</ul>
			</div>
		</nav>
	</div>

	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-10">

		<h3>My Clusters</h3>
		<!-- Beginning of table -->
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>Cluster Name</th>
					<th>Cluster IP</th>
					<th>Options</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Best cluster</td>
					<td>125.114.112.1</td>
					<td>
						<button type="button" class="btn btn-secondary btn-sm">Option
							1</button>
						<button type="button" class="btn btn-secondary btn-sm">Option
							2</button>
					</td>
				</tr>
				<tr>
					<td>Bad cluster</td>
					<td>128.111.22.31</td>
					<td>
						<button type="button" class="btn btn-secondary btn-sm">Option
							1</button>
						<button type="button" class="btn btn-secondary btn-sm">Option
							2</button>
					</td>
				</tr>
				<tr>
					<td>Okay cluster</td>
					<td>123.456.78.9</td>
					<td>
						<button type="button" class="btn btn-secondary btn-sm">Option
							1</button>
						<button type="button" class="btn btn-secondary btn-sm">Option
							2</button>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- End of Table -->

		<!-- New cluster upload -->
		<!-- Will need to decide on validation on how to figure out if IP address is valid
More than likely we can simplify it by sending email or uploading a file top the cluster -->
		<h3>Upload New Cluster</h3>
		<form action="#">
			<!-- Action will be to send to confirmation page and validate -->
			<div class="form-group">
				<label for="cluster">IP address:</label> <input type="text"
					class="form-control" id="cluster">
			</div>
		</form>


		<!-- Submit Button -->
		<a href="#"><button type="button" class="btn btn-info btn-lg">Submit</button></a>
	</div>

</body>
</html>
