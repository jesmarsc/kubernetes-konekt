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
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" />
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<title>Yaml Builder</title>
</head>

<body class="text-center">
	<div class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

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
				<label> Application Name: </label>
				<form:input class="form-control" path="applicationName" />
				<form:errors path="applicationName" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Version: </label>
				<form:input class="form-control" path="version" />
				<form:errors path="version" cssClass="error" />
			</div>
			<div class="form-group row">
				<label> Container Port: </label>
				<form:input class="form-control" path="containerPort" />
				<form:errors path="containerPort" cssClass="error" />
			</div>




			<div class=" form-group row">
				<h3>Metadata: Labels</h3>
			</div>


			<div class="col-xs-12 form-group row">
				<div class="col-md-12">

					<div id="field">
						<div id="field0">
							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="key">Key</label>
								<div class="col-md-5">
									<form:input id="key" name="key" type="text" placeholder=""
										class="form-control input-md" path="key" />

								</div>
							</div>
							<br> <br>
							<!-- Text input-->
							<div class="form-group">
								<label class="col-md-4 control-label" for="value">Value</label>
								<div class="col-md-5">
									<form:input id="value" name="value" type="text" placeholder=""
										class="form-control input-md" path="value" />

								</div>
							</div>


						</div>
					</div>
					<!-- Button -->
					<div class="form-group">
						<div class="col-md-4">
							<button id="add-more" name="add-more" class="btn btn-primary">Add
								Another Label</button>
						</div>
						<br> <br> <br> <br> <br> <br> <br>
						<br>

						<div class="form-group row">
							<input class="btn btn-primary text-center" type="submit"
								value="Submit" />
						</div>
					</div>


				</div>
			</div>

			<script type="text/javascript">
		$(document)
				.ready(
						function() {
							//@naresh action dynamic childs
							var next = 0;
							$("#add-more")
									.click(
											function(e) {
												e.preventDefault();
												var addto = "#field" + next;
												var addRemove = "#field"
														+ (next);
												next = next + 1;
												var newIn = ' <div id="field'+ next 
												+'" name="field'+ next 
												+'"><!-- Text input--><div class="form-group"> <label class="col-md-4 control-label" for="key">Key</label> <div class="col-md-5"> <form:input id="key" name="key" path="key" type="text" placeholder="" class="form-control input-md"/> </div></div><br><br> <!-- Text input--><div class="form-group"> <label class="col-md-4 control-label" for="value">Value</label> <div class="col-md-5"> <form:input id="value" name="value" type="text" path="value" placeholder="" class="form-control input-md"/> </div></div>';
												var newInput = $(newIn);
												var removeBtn = '<button id="remove'
														+ (next - 1)
														+ '" class="btn btn-danger remove-me" >Remove</button></div></div><div id="field">';
												var removeButton = $(removeBtn);
												$(addto).after(newInput);
												$(addRemove)
														.after(removeButton);
												$("#field" + next).attr(
														'data-source',
														$(addto).attr(
																'data-source'));
												$("#count").val(next);

												$('.remove-me')
														.click(
																function(e) {
																	e
																			.preventDefault();
																	var fieldNum = this.id
																			.charAt(this.id.length - 1);
																	var fieldID = "#field"
																			+ fieldNum;
																	$(this)
																			.remove();
																	$(fieldID)
																			.remove();
																});
											});

						});
	</script>


			<br></br>

		</form:form>
	</div>

	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
