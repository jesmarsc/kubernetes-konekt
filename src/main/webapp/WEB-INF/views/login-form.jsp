<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<!DOCTYPE html>
<html lang="en">

<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="/css/login_caro.css" rel="stylesheet" >
<link href="fonts/font-awesome-4.3.0/css/font-awesome.min.css" rel="stylesheet">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<section class="login-block">
    <div class="container">
	<div class="row">
		<div class="col-md-4 login-sec">
		    <h2 class="text-center">Login</h2>
  
  		<form:form
			action="${pageContext.request.contextPath}/login-confirmation"
			method="POST">

			<c:if test="${param.error != null}">
				<font color="red">You entered an invalid email or password.</font>
				<br>
				<br>
			</c:if>

			<c:if test="${param.logout != null}">
				<font color="blue">You have been logged out.</font>
				<br>
				<br>
			</c:if>

			<!-- User name -->
			<div style="margin-bottom: 25px" class="input-group">
				<span class="input-group-addon"><i
					class="glyphicon glyphicon-user"></i></span> <input type="text"
					name="username" placeholder="Username" class="form-control">
			</div>

			<!-- Password -->
			<div style="margin-bottom: 25px" class="input-group">
				<span class="input-group-addon"><i
					class="glyphicon glyphicon-lock"></i></span> <input type="password"
					name="password" placeholder="Password" class="form-control">
			</div>

			<input class="btn btn-primary float-right" type="submit" value="Login" />

		</form:form>
		
		<input class="btn btn-primary" onclick="location.href='/register'" type="submit" value="Register" />
		
  		</div>

	<!-- Insert Image here -->
	<div class="col-md-8 banner-sec">
    	<img class="d-block img-fluid" src="https://static.pexels.com/photos/33972/pexels-photo.jpg">
	</div>
	</div>
</div>
</section>
 