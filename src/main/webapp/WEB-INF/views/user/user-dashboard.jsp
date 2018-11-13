<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
    <head>
    	<meta charset="utf-8">
    	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    	<!-- Bootstrap CSS -->
    	<link rel="stylesheet" 
    		href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"/>

        <title>User Dashboard</title>
        
    </head>
	<!-- 
	 href"#" means clicking the text does not redirect the page to a new page.
		For now they are just stubs later they can be removed of used to redirect the user to other places 
		on the website.
	-->
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

					<li class="nav-item"><a class="nav-link" href="#">
							Messages </a></li>

					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navDropdown"
						data-toggle="dropdown"> Alerts </a> <!-- later these alerts will be read from database for now theres a dummy dropdown menu -->
						<div class="dropdown-menu">
							<a class="dropdown-item" href="#"> Cluster 1 Started </a> <a
								class="dropdown-item" href="#"> Cluster 2 Started </a> <a
								class="dropdown-item" href="#"> Cluster 1 Stopped </a>
						</div></li>

					<li class="nav-item"><a class="nav-link" href="#">Profile</a>
					</li>

					<li class="nav-item">
					<form:form action="${pageContext.request.contextPath}/logout" method="POST">
						<input type="submit" value="Logout" class="btn btn-primary"/>
					</form:form>
					</li>

				</ul>
			</div>
		</nav>
	</div>

	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-10">
		<table class="table table-hover table-bordered">
			<caption>Container/Cluster Status</caption>
			<thead>
				<tr>
					<th>Container</th>
					<th>Cluster IP</th>
					<th>Status</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">Container 1</th>
					<td>196.12.186.145</td>
					<td>Running</td>
					<td>
					<button type="button" class="btn btn-primary m-1">Start</button>
					<button type="button" class="btn btn-primary m-1">Stop</button>
					</td>
				</tr>
				<tr>
					<th scope="row">Container 2</th>
					<td>193.123.146.155</td>
					<td>Running</td>
					<td>
					<button  type="button" class="btn btn-primary m-1">Start</button>
					<button type="button" class="btn btn-primary m-1">Stop</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>


	<!--  probably will want to wrap the next three containers into with <form:form>...</form:form> -->

	<!-- input field browse/Upload button for 
			user to upload new containers/ browse for containers. -->

	<div class="custom-file  mx-4 my-4 col-sm-10 col-md-10 col-lg-10">
		<input type="file" class="custom-file-input" id="customFile">
		<label class="custom-file-label" for="customFile">Upload New
			Container</label>
	</div>


	<form:form
		class=" border-box container  mx-1 my-4 col-sm-10 col-md-10 col-lg-10"
		action="uploadContainerToClusterConfirmation" modelAttribute="uploadContainerClusterForm">


		<!-- Uploaded containers -->


		<div class="form-group row">
			<label> Select An Uploaded Containers: </label>
			<form:select class="form-control" path="containerName">
				<form:options items="${currentAccount.containers}" />
			</form:select>
			<form:errors path="containerName" cssClass="error" />
		</div>

			<!-- table Available cluster  -->

		<div class="form-group row">
			<label> Select An Available Cluster: </label>
			<form:select class="form-control" path="clusterIp">
				<form:options items="${availableClusters}" />
			</form:select>
			<form:errors path="clusterIp" cssClass="error" />
		</div>

		<div class="form-group mx-4 my-4">
			<input class="btn btn-primary text-center" type="submit"
				value="Submit" />
		</div>
	</form:form>

</body>
</html>
