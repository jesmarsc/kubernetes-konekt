<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Begin HTML Document -->
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Refresh" content="100"> <!-- used as a backup, will refresh entire page ever 100sec -->
    
    <!-- Title at the Tab of the Browser -->
	<title>Provider Dashboard</title>
   
    <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link href="css/theme.css" rel="stylesheet">
		
</head>

<!-- Begin Body -->
<body>

	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">

		<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">Kubernetes Konekt</a>
  
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent">
				<span class="navbar-toggler-icon"></span>
			</button>


			<div class="collapse navbar-collapse" id="navbarSupportedContent">
			
				
				<ul class="navbar-nav ml-auto">

					<sec:authorize access="hasRole('USER')">
						<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/user"> User Dashboard </a></li>
					</sec:authorize>
					<sec:authorize access="hasRole('PROVIDER')">
						<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/provider"> Provider Dashboard </a></li>
					</sec:authorize>
					
					<li class="nav-item">
						<a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
					</li>
				

				</ul>
			</div>
		</nav>
	</div>

	<!-- Begin Code for Forms -->
	<c:choose>
	    <c:when test="${not empty uploadClusterFailStatus}">
	    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
	        <div class="alert alert-danger" role="alert">
			<strong>${uploadClusterFailStatus}</strong> ${uploadClusterFailMessage}
			</div>
			</div>
	    </c:when>
	    <c:when test="${not empty uploadClusterSuccessStatus}">
	    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
	      	<div class="alert alert-success" role="alert">
			<strong>${uploadClusterSuccessStatus}</strong> ${uploadClusterSuccessMessage}
			</div>
			</div>
	    </c:when>
	        <c:when test="${not empty deleteClusterSuccessStatus}">
	    	<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
	      	<div class="alert alert-danger" role="alert">
			<strong>${deleteClusterSuccessStatus}</strong> ${deleteClusterSuccessMessage}
			</div>
			</div>
	    </c:when>
		<c:when test="${not empty deleteContainerToClusterStatus}">
			<div class="container  mx-1 my-4 col-sm-10 col-md-10 col-lg-12">
				<div class="alert alert-danger" role="alert">
					<strong>${deleteContainerToClusterStatus}</strong>
					${deleteContainerToClusterMessage}
				</div>
			</div>
		</c:when>
		<c:otherwise>
	
	    </c:otherwise>
	</c:choose>
	
	<div class="row-offcanvas row-offcanvas-left">
	  	<div id="sidebar" class="sidebar-offcanvas">
	      <div class="col-md-12">
	        <ul class="nav nav-pills nav-stacked">
	          <li class="active"><a href="javascript:unhide('cluster1-div', 'cluster2-div', 'cluster3-div', 'welcome')" class="button">Cluster List</a></li>
	          <li><a href="javascript:unhide('cluster2-div', 'cluster1-div', 'cluster3-div', 'welcome')" class="button">Cluster Workload</a></li>
	          <li><a href="javascript:unhide('cluster3-div', 'cluster1-div', 'cluster2-div', 'welcome')" class="button">Cluster Upload</a></li>
	        </ul>
	      </div>
  		</div>
	
	<script src="http://code.jquery.com/jquery-3.1.1.js"></script>
	
	<script type="text/javascript">
		var count = 0;
		 
		function unhide(divID, otherDivId, otherDivId2,welcome) {
		    var item = document.getElementById(divID);
		    if (item) {
		            item.className=(item.className=='hidden')?'unhidden container mx-1 my-4 col-sm-10 col-md-10 col-lg-12':'hidden';
		        }
		        document.getElementById(otherDivId).className = 'hidden';
		        document.getElementById(otherDivId2).className = 'hidden';
		        document.getElementById(welcome).className = 'hidden';
		}
    	function doRefresh(){
        	$("#cluster1-div").load("cluster-list.jsp");
        	$("#cluster2-div").load("cluster-workload.jsp");
        	$("#cluster3-div").load("upload-cluster.jsp");
        	$("#sample").html(count);
        	count++;
    	}
    	$(function() {
        	setInterval(doRefresh, 5000);
    	}); 
	</script>
	<script src="jquery.AshAlom.gaugeMeter-2.0.0.min.js"></script> 
	<div id="main">
	
	<div id="welcome" class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12" >
		<h3>Welcome to the Provider Dashboard!</h3>
		
    	
	</div>
	
	<div id="cluster1-div" class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12 hidden" >
		<%@ include file="cluster-list.jsp" %>
	</div>
	
	<div id="cluster2-div" class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12 hidden" >
		<%@ include file="cluster-workload.jsp" %>
	</div>
	
	<div id="cluster3-div" class="container mx-1 my-4 col-sm-10 col-md-10 col-lg-12 hidden" >
		<%@ include file="upload-cluster.jsp" %>
	</div> 
	
	<div id="sample" class="container mx-1"></div>
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="js/scripts.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>	


	</div>
	</div>
	
</body>

</html>
