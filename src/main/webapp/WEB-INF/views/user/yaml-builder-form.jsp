<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html class="no-js" lang="en">

<style>
.error {
	color: red
}
</style>

<head>  
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YAML Builder</title>
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

<body class="text-center">
	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

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
	<h1>Yaml Builder Form</h1>

	<h1>
		<font color="red">${message} </font>
		<!-- use to let user know they made an error. -->
	</h1>

	<div class="container-fluid col-sm-12 col-md-6">
		<form:form action="YamlBuildConfirmation"
			modelAttribute="YamlBuilderForm">


			<div class="form-group row">
				<label> Deployment Name: </label>
				<form:input class="form-control" path="deploymentName" />
				<form:errors path="deploymentName" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Image: </label>
				<form:input class="form-control" path="image" />
				<form:errors path="image" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> replicas: </label>
				<form:input class="form-control" path="replicas" />
				<form:errors path="replicas" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Container Port: </label>
				<form:input class="form-control" path="containerPort" />
				<form:errors path="containerPort" cssClass="error" />
			</div>
			<div class=" form-group row">
				<h3>Metadata: Labels</h3>
			</div>

			<div class="form-group">
				<label class="col-md-4 control-label" for="key">Key</label>
				<div class="col-md-5">
					<form:input id="key" name="key" type="text" placeholder=""
						class="form-control input-md" path="key" />
					<form:errors path="key" cssClass="error" />
				</div>
			</div>
			<br>
			<br>
			<!-- Text input-->
			<div class="form-group">
				<label class="col-md-4 control-label" for="value">Value</label>
				<div class="col-md-5">
					<form:input id="value" name="value" type="text" placeholder=""
						class="form-control input-md" path="value" />
					<form:errors path="value" cssClass="error" />
				</div>
			</div>
			<br>
			<br>
			<div class="form-group row">
				<input class="btn btn-primary text-center" type="submit"
					value="Submit" />
			</div>
			
		</form:form>
	</div>

	<!-- <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script> -->
		
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
		
</body>
</html>
