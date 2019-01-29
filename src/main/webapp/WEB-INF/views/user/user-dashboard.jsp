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
    <meta name="description" content="Tequila is a free, open source Bootstrap 4 theme" />
    <meta name="generator" content="Themestr.app">
    <link rel="icon" href="http://themes.guide/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="http://themes.guide/favicon.ico" type="image/x-icon" />
    <meta property="og:image" name="twitter:image" content="http://bootstrap.themes.guide/assets/ss_tequila.png">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@ThemesGuide">
    <meta name="twitter:creator" content="@ThemesGuide">
    <meta name="twitter:title" content="Open-source Bootstrap 4 Themes">
    <meta name="twitter:description" content="Download Tequila - free, open source Bootstrap 4 theme by Themes.guide">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
    
 <link href="css/theme.css" rel="stylesheet">
    <link href="css/template.css" rel="stylesheet">
    


</head>
	<!-- 
	 href"#" means clicking the text does not redirect the page to a new page.
		For now they are just stubs later they can be removed of used to redirect the user to other places 
		on the website.
	-->

<body>
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
			<a class="navbar-brand" href="#"><h4>Kubernetes Konekt</h4></a>

			  <button class="navbar-toggler dropdown-toggle" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>


			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav ml-auto">

					<li class="nav-item"><a class="nav-link" href="/"><h6>Home</h6> </a></li>
					<sec:authorize access="hasRole('USER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/user"><h6> User Dashboard </h6></a></li>

					</sec:authorize>
					<sec:authorize access="hasRole('PROVIDER')">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/provider"> <h6>Provider Dashboard </h6></a></li>
					</sec:authorize>
					<li class="nav-item"><a class="nav-link" href="#">
							<h6>Messages </h6></a></li>

					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navDropdown"
						data-toggle="dropdown"><h6> Alerts </h6></a> <!-- later these alerts will be read from database for now theres a dummy dropdown menu -->
						<div class="dropdown-menu">
							<a class="dropdown-item" href="#"> Cluster 1 Started </a> <a
								class="dropdown-item" href="#"> Cluster 2 Started </a> <a
								class="dropdown-item" href="#"> Cluster 1 Stopped </a>
						</div></li>

					<li class="nav-item"><a class="nav-link" href="#"><h6> Profile</h6> </a>
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
		<table class="table table-hover table-bordered">
			<thead>
				<tr>
					<th align="center"><h5>Id</h5></th>
					<th><h5>Container</h5></th>
					<th><h5>Cluster URL</h5></th>
					<th><h5><span class="badge badge-pill badge-light">Status</span></h5></th>
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
							<a class="btn btn-primary" href="${removeLink}" onclick="if(!(confirm('Are you sure you want to delete container')))return false" role="button">Delete Container</a>
							<a class="btn btn-primary" href="#" role="button">another option</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- input field browse/Upload button for 
			user to upload new containers/ browse for containers. -->

	<div class="  border-box container  mx-1 my-4 col-sm-10 col-md-10 col-lg-6">
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
					for="customFile">Upload New Container</label>
			</div>
			<!-- submit -->
			<div class="form-group row mx-4 my-4">
				<input class="btn btn-primary text-center" type="submit"
					value="Submit" />
			</div>
			
		</form:form>
	</div>

	<sec:authorize access="hasRole('USER')">
		<input type="button" onclick="location.href='user/build-yaml'"
			value="Build A Yaml File" class="btn btn-primary mx-4 my-4">
	</sec:authorize>

<!-- 	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script type="application/javascript">
		$('input[type="file"]').change(function(e){
			var fileName = e.target.files[0].name;
			$('.custom-file-label').html(fileName);
		});
	</script> -->
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
</body>
</html>
