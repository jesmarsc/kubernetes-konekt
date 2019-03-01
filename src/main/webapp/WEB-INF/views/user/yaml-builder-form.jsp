<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html class="no-js" lang="en">
<link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet"/>

<style>
.error {
	color: red
}
</style>

<head>  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YAML Builder</title>
    <link href="https://fonts.googleapis.com/css?family=Maven+Pro:bold|700" rel="stylesheet">
    
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link href="/css/light-bootstrap-dashboard.css?v=2.0.1" rel="stylesheet" />
	<style>
     body {
        font-family: 'Maven+Pro', serif;
        font-size: 20px;
      }
     </style> 
</head>

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
						class="nav-link " href="/user?value=1"
						class="button"> <i class="nc-icon nc-layers-3"></i> My Workload
					</a></li>
					<li id="userUploadNavLink"><a class="nav-link"
						href="/user?value=2" class="button"> <i
							class="nc-icon nc-cloud-upload-94"></i> Upload Workload
					</a></li>
					<li id="userYamlBuilderFormNavLink" class="active">
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
		<div class="main-panel">
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
<div class="container-fluid col-xs-12 col-sm-6">
		
		<h1> Yaml Builder Form </h1>

		<h1>
			<font color="red">${message} </font>
			<!-- use to let user know they made an error. -->
		</h1>
		
		<table class="table table-hover table-bordered">
		
		<form:form action="YamlBuildConfirmation"
			modelAttribute="YamlBuilderForm">

			<div class="form-group">
				<label> Deployment Name: </label>
					<form:input class="form-control" path="deploymentName" />
					<form:errors path="deploymentName" cssClass="error" />
			</div>
			<div class="form-group">
				<label> Image: </label>
				<form:input class="form-control" path="image" />
				<form:errors path="image" cssClass="error" />
			</div>
			<div class="form-group">
				<label> replicas: </label>
				<form:input class="form-control" path="replicas" />
				<form:errors path="replicas" cssClass="error" />
			</div>
			<div class="form-group">
				<label> Container Port: </label>
				<form:input class="form-control" path="containerPort" />
				<form:errors path="containerPort" cssClass="error" />
			</div>
			<div class=" form-group">
				<h3>Metadata: Labels</h3>
			</div>

			<div class="form-group">
				<label class="col-md-4 control-label" for="key">Key</label>
				<div class="col-md-6">
					<form:input id="key" name="key" type="text" placeholder=""
						class="form-control input-md" path="key" />
					<form:errors path="key" cssClass="error" />
				</div>
			</div>
			<br>
			<!-- Text input-->
			<div class="form-group">
				<label class="col-md-4 control-label" for="value">Value</label>
				<div class="col-md-6">
					<form:input id="value" name="value" type="text" placeholder=""
						class="form-control input-md" path="value" />
					<form:errors path="value" cssClass="error" />
				</div>
			</div>
			<br>
			<div class="form-group">
				<input class="btn btn-primary text-center" type="submit"
					value="Submit" />
			</div>
			
		</form:form>
		
		</table>
	</div>


	</div>
	</div>
	<!-- Begin Body -->
	
	

	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	
	
		
</body>
</html>
