<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
  <head>  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
 	<link href="css/theme.css" rel="stylesheet">
    
</head>
	<!-- 
	 href"#" means clicking the text does not redirect the page to a new page.
		For now they are just stubs later they can be removed of used to redirect the user to other places 
		on the website.
	-->

<body>
<div class="view " style="background-image: url('images/background_image_goodfellas.png'); background-repeat: no-repeat; background-size: cover; background-position: initial;">

	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-expand-lg navbar-dark transparent">
			<a class="navbar-brand" href="#">Kubernetes Konekt</a>

			<button class="navbar-toggler dropdown-toggle" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    			<span class="navbar-toggler-icon"></span>
  			</button>


			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ml-auto">

					<li class="nav-item" ><a class="nav-link" href="/" >Home</a></li>
					<sec:authorize access="hasRole('USER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user">User Dashboard</a></li>

					</sec:authorize>
					<sec:authorize access="hasRole('PROVIDER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/provider"> Provider Dashboard</a></li>
					</sec:authorize>
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

					<li class="nav-item"><a class="nav-link" href="#">Profile </a>
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


<c:choose>
    <c:when test="${not empty uploadContainerToClusterFailStatus}">
    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
        <div class="alert alert-danger" role="alert">
		<strong>${uploadContainerToClusterFailStatus}</strong> ${uploadContainerToClusterFailMessage}
		</div>
		</div>
    </c:when>
    <c:when test="${not empty uploadContainerToClusterSuccessStatus}">
    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
      	<div class="alert alert-success" role="alert">
		<strong>${uploadContainerToClusterSuccessStatus}</strong> ${uploadContainerToClusterSuccessMessage}
		</div>
		</div>
    </c:when>
        <c:when test="${not empty uploadContainerFailStatus}">
    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
      	<div class="alert alert-danger" role="alert">
		<strong>${uploadContainerFailStatus}</strong> ${uploadContainerFailMessage}
		</div>
		</div>
    </c:when>

        <c:when test="${not empty uploadContainerSuccessStatus}">
    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
      	<div class="alert alert-success" role="alert">
		<strong>${uploadContainerSuccessStatus}</strong> ${uploadContainerSuccessMessage}
		</div>
		</div>
    </c:when>
  
        <c:when test="${not empty deleteContainerToClusterStatus}">
    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
      	<div class="alert alert-danger" role="alert">
		<strong>${deleteContainerToClusterStatus}</strong> ${deleteContainerToClusterMessage}
		</div>
		</div>
    </c:when>
   
    <c:otherwise>

    </c:otherwise>
</c:choose>


	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
		<table class="table table-hover table-bordered table-striped">
			<thead class="thead-transparent">
				<tr>
					<th align="center"><h5>Id</h5></th>
					<th><h5>Container</h5></th>
					<th><h5>Cluster URL</h5></th>
					<th><h5>Status</h5></th>
					<th><h5>Action</h5></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="container" items="${currentAccount.containers}">
					<c:url var="removeLink" value="/user/delete">
						<c:param name="containerId" value="${container.id}" />
					</c:url>
					
					<tr>
						<td>${container.id}</td>
						<td>${container.containerName}</td>
						<td>${container.clusterUrl}</td>
						<td ><span class="badge badge-pill badge-primary">${container.status}</span></td>
						<td>
							<a class="btn btn-outline-primary" href="${removeLink}" onclick="if(!(confirm('Are you sure you want to delete container')))return false" role="button">Delete Container</a>
							<a class="btn btn-light" href="#" role="button">Another Option</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- input field browse/Upload button for 
			user to upload new containers/ browse for containers. -->

	<div class="border-box container  mx-0 my-4 col-sm-10 col-md-10 col-lg-6">
		<!-- Form to upload deployment yaml file to cluster -->
		<form:form class="custom-file " method="POST"
			action="/user/upload" enctype="multipart/form-data" modelAttribute="uploadContainerClusterForm">
			<!-- choose deployment file -->
			<div class="form-group row mb-2">
				<input type="file" class="custom-file-input" id="customFile"
					name="containerFile">
			</div>
			<!-- display selected file  -->
			<div class="form-group row  mb-2">
				 <label class="custom-file-label"
					for="customFile"> <font face="Voltaire" color="#fff">Upload New Container</font></label>
			</div>

			<!-- Select cluster  -->
			<div class="form-group row mx-1 my-2 col-sm-10 col-md-10 col-lg-12">
				<label><font face="Voltaire" color="#fff"> Select An Available Cluster: </font></label>
				<form:select class="form-control row" path="clusterUrl">
					<form:option value=""></form:option>
					<form:options items="${availableClusters}" />
				</form:select>
				<form:errors path="clusterUrl" cssClass="error" />
			</div>
			
			<div class="form-group row mx-0 my-2 col-sm-10 col-md-10 col-lg-12 ">
				<small> <font face="Voltaire" color="#fff">NOTE: If cluster is not specified it
						will be chosen for you.</font></small>
			</div>

			<!-- submit -->
			<div class="border-box row mx-0 my-2 col-sm-10 col-md-10 col-lg-12">
				<input class="btn btn-light text-center" type="submit"
					value="Submit" />
			</div>
			
		</form:form>
	</div>

	<sec:authorize access="hasRole('USER')">
		<input type="button" onclick="location.href='user/build-yaml'"
			value="Build A Yaml File" class="btn btn-light text-center mx-4 my-4">
	</sec:authorize>
</div>
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
</body>
</html>
