<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
<link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet"/>

  <head>  
  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
   <link href="https://fonts.googleapis.com/css?family=Maven+Pro:bold|700" rel="stylesheet">
    
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link href="/css/light-bootstrap-dashboard.css?v=2.0.1" rel="stylesheet" />
  
</head>
	<!-- 
	 href"#" means clicking the text does not redirect the page to a new page.
		For now they are just stubs later they can be removed of used to redirect the user to other places 
		on the website.
	-->

<body>

	<div class="wrapper">
		<div class="sidebar" data-color="blue"
			>
			<!--
        Tip 1: You can change the color of the sidebar using: data-color="purple | blue | green | orange | red"

        Tip 2: you can also add an image using data-image tag
    -->
			<div class="sidebar-wrapper ">
				<div class="logo m-2">
					<a>Hello, ${currentAccount.firstName}</a>
				</div>
				<ul class="nav">
					<li id="userWorkloadNavLink"><a
						class="nav-link " href="javascript:show(1,'')"
						class="button"> <i class="nc-icon nc-layers-3"></i> My Workload
					</a></li>
					<li id="userUploadNavLink"><a class="nav-link"
						href="javascript:show(2,'')" class="button"> <i
							class="nc-icon nc-cloud-upload-94"></i> Upload Workload
					</a></li>
					<li id="userYamlBuilderFormNavLink">
					<sec:authorize access="hasRole('USER')">
					<a class="nav-link"
						href="/user/build-yaml" class="button"> <i
							class="nc-icon nc-paper-2"></i> Yaml Build Form
					</a>
					</sec:authorize>
					</li>
				</ul>
			</div>
		</div>
		<div class="main-panel" >
			<!-- Navbar -->
			<nav class="navbar navbar-expand-lg ">
				<div class=" container  ">
					<a class="navbar-brand" href="${pageContext.request.contextPath}/">Kubernetes
						Konekt </a>
					<div class="collapse navbar-collapse" id="navigation"></div>

					<button class=" navbar-toggler navbar-toggler-right " type="button"
						data-toggle="collapse" aria-controls="navigation-index"
						aria-expanded="false" aria-label="Toggle navigation">
						<span class="navbar-toggler-bar burger-lines"></span> <span
							class="navbar-toggler-bar burger-lines"></span> <span
							class="navbar-toggler-bar burger-lines"></span>
					</button>


					<button class="navbar-toggler" type="button" data-toggle="collapse"
						data-target="#TopNavBarContent">
						<span class="navbar-toggler-icon"></span>
					</button>

					<div class="collapse navbar-collapse" id="TopNavBarContent">

						<ul class="navbar-nav ml-auto">

							<sec:authorize access="hasRole('USER')">
								<li class="nav-link"><a class="tocxref btn"
									style="color: black;"
									href="${pageContext.request.contextPath}/user"> User
										Dashboard </a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('PROVIDER')">
								<li class="nav-btn"><a class="btn btn-warning"
									style="color: black;"
									href="${pageContext.request.contextPath}/provider">
										Provider Dashboard </a></li>
							</sec:authorize>

							<li class="nav-item"><form:form
									action="${pageContext.request.contextPath}/logout"
									method="POST">
									<input type="submit" value="Logout" class="btn btn-danger" />
								</form:form></li>


						</ul>
					</div>

				</div>
			</nav>

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
			<div id="user-workload-div"
				class=" user-workload-div d-none container mx-1  col-sm-10 col-md-10 col-lg-12  ">
				<div
					class="userWorkload-div container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
					<table class="table table-hover table-bordered table-striped">
						<thead class="thead-transparent">
							<tr>
								<th align="center"><h5>Id</h5></th>
								<th><h5>Name</h5></th>
								<th><h5>Kind</h5></th>
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
								<c:url var="statusLink" value="/user/get-status">
									<c:param name="containerId" value="${container.id}" />
								</c:url>
								<tr>
									<td>${container.id}</td>
									<td>${container.containerName}</td>
									<td>${container.kind}</td>
									<td>${container.clusterUrl}</td>
									<td><span class="badge badge-pill badge-primary">${container.status}</span></td>
									<td><a class="btn btn-danger"
										href="${removeLink}"
										onclick="if(!(confirm('Are you sure you want to delete container')))return false"
										role="button">Delete Container</a> 
										<a class="btn btn-primary"
										href="${statusLink}"
										role="button">Get Status</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>

			<div id="user-upload-workload"
				class="user-upload-workload d-none container  col-sm-10 col-md-10 col-lg-12   ">
				<div
					class="userUploadForm-div border-box container  mx-0 my-4 col-sm-10 col-md-10 col-lg-6">
					<!-- Form to upload deployment yaml file to cluster -->
					<div class="form-group row ">
						<div class="col-lg-1"></div>
						 <h3>Browse For Yaml File: </h3>
						</div>
					<form:form class="custom-file " method="POST" action="/user/upload"
						enctype="multipart/form-data"
						modelAttribute="uploadContainerClusterForm">
						
			
						<!-- choose deployment file -->
						
						<div class="form-group row">
							<input type="file" class="custom-file-input" id="customFile"
								name="containerFile">
						</div>
						<!-- display selected file  -->

						<div class="form-group row ">
							<label class="custom-file-label" for="customFile">  </label>
						</div>
						
						<!-- Select cluster 
						<div
							class="form-group row mx-1 col-sm-10 col-md-10 col-lg-12">
							<label><font face="Voltaire" color="#000"> Select
									An Available Cluster: </font></label>
							<form:select class="form-control row" path="clusterUrl">
								<form:option value=""></form:option>
								<form:options items="${availableClusters}" />
							</form:select>
							<form:errors path="clusterUrl" cssClass="error" />
						</div>

						<div
							class="form-group row mx-0 my-2 col-sm-10 col-md-10 col-lg-12 ">
							<small> <font face="Voltaire" color="#000">NOTE:
									If cluster is not specified it will be chosen for you.</font></small>
						</div>
 -->
						<!-- submit -->
						<div
							class="border-box row mx-0  col-sm-10 col-md-10 col-lg-12">
							<input class="btn btn-primary text-center" type="submit"
								value="Submit" />
						</div>

					</form:form>
				</div>


			<div id="user-yaml-builder"
				class=" user-yaml-builder container mx-1 col-sm-10 col-md-10 col-lg-12 hidden ">

			</div>

			<div id="cluster-metrics-div1"
				class=" cluster-metrics-div container mx-1 col-sm-10 col-md-10 col-lg-12  ">

			</div>

		</div>

	</div>





	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
			</div>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>	
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
	<!--   Core JS Files   -->
<script src="js/core/popper.min.js" type="text/javascript"></script>
<script src="js/core/bootstrap.min.js" type="text/javascript"></script>
<!--  Plugin for Switches, full documentation here: http://www.jque.re/plugins/version3/bootstrap.switch/ -->
<script src="js/plugins/bootstrap-switch.js"></script>

<!--  Chartist Plugin  -->
<script src="js/plugins/chartist.min.js"></script>
<!--  Notifications Plugin    -->
<script src="js/plugins/bootstrap-notify.js"></script>
<!-- Control Center for Light Bootstrap Dashboard: scripts for the example pages etc -->
<script src="js/light-bootstrap-dashboard.js?v=2.0.1" type="text/javascript"></script>
	
	
</body>
	<script type="text/javascript">
		
  	var count = 0;
  	var url = new URL(window.location.href );
  	var showThisId = url.searchParams.get("value");
  	$(document).ready(function(){
		if(showThisId != null ){
			remove();
			show(showThisId,'');
		}else{
			show(1,'');
		}
	});
	
	function remove(){
		document.getElementById("user-workload-div").className="d-none";
		document.getElementById("user-upload-workload").className="d-none";
	}
	
	
	function show(showId, ipInstance){
		remove();
		setActiveEmpty();
		if(showId == 1){
			document.getElementById("userWorkloadNavLink").className="active";
			document.getElementById("user-workload-div").className="user-workload-div container mx-1  col-sm-10 col-md-10 col-lg-12 "
			window.history.pushState("","","/user?value=1");
		}else if(showId == 2){
			document.getElementById("userUploadNavLink").className="active";
			document.getElementById("user-upload-workload").className="user-upload-workload container  col-sm-10 col-md-10 col-lg-12";
			window.history.pushState("","","/user?value=2");
		}
		
	}
	
	function setActiveEmpty(){
		document.getElementById("userWorkloadNavLink").className="";
		document.getElementById("userUploadNavLink").className="";		
	}

	</script>
	<script type="application/javascript">
$('input[type="file"]').change(function(e){
 var fileName = e.target.files[0].name;
$('.custom-file-label').html(fileName);
});
</script>
</html>
