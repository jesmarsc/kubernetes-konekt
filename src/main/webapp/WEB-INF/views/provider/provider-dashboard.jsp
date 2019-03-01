<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- Begin HTML Document -->
<!DOCTYPE html>
<html class="no-js" lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- <meta http-equiv="Refresh" content="120"> --><!-- used as a backup, will refresh entire page ever 120sec -->
    
    <!-- Title at the Tab of the Browser -->
	<title>Provider Dashboard</title>
   
    <link href="https://fonts.googleapis.com/css?family=Maven+Pro:bold|700" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/3.0.0/css/ionicons.css" rel="stylesheet">
	<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
	<link rel="stylesheet" href="/css/bootstrap.min.css" />
    <link href="/css/light-bootstrap-dashboard.css?v=2.0.1" rel="stylesheet" />
     <style>
     
     body {
        font-family: 'Maven+Pro';
        font-size: 20px;
      }
     	.card-header-blue {
    background-color:#003d99;
    color:#FFFFFF;
    border-bottom:3px solid #BBB;
    box-shadow: 0 8px 17px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    font-family: 'Roboto', sans-serif;
    padding:0px;
    margin-top:0px;
    overflow:hidden;
    -webkit-transition: box-shadow 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          transition: box-shadow 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
		
     </style>   
</head>

<!-- Begin Body -->
<body>

	    <div class="wrapper">
        <div class="sidebar"  data-color="blue" data-image="images/sidebar-6.jpg">
            <!--
        Tip 1: You can change the color of the sidebar using: data-color="purple | blue | green | orange | red"

        Tip 2: you can also add an image using data-image tag
    -->
            <div class="sidebar-wrapper ">
                <div class="logo m-2">
                    <a>Hello, ${currentAccount.firstName}</a>
                </div>
                <ul class="nav">
                    <li id="clusterListNavLink" >
                        <a   class="nav-link clusterListNavLink " href="javascript:show(1,'')" class="button">
                            <i class="nc-icon nc-atom"></i>
                          Cluster List
                        </a>
 
                    </li>
                    <li id="clusterWorkloadNavLink">
                        <a class="nav-link" href="javascript:show(2,'')" class="button">
                            <i class="nc-icon nc-app"></i>
                          Cluster Workload
                        </a>
 
                    </li>
                    <li id="clusterUploadFormNavLink">
                        <a class="nav-link" href="javascript:show(3,'')" class="button">
                            <i class="nc-icon nc-paper-2"></i>
                          Cluster Upload Form
                        </a>
                    </li>
                    
                </ul>
            </div>
        </div>
        <!-- the next line can be add to give blue background to main-panel -->
        <!-- style="background:transparent url('images/sidebar-6.jpg') no-repeat center center /cover" -->
       <div class="main-panel"  >
            <!-- Navbar -->
            <nav class="navbar navbar-expand-lg ">
                <div class=" container  ">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">Kubernetes Konekt   </a>
                    <div class="collapse navbar-collapse" id="navigation">
                        
                    </div>
                    
                    <button  class=" navbar-toggler navbar-toggler-right " type="button" data-toggle="collapse" aria-controls="navigation-index" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-bar burger-lines"></span>
                        <span class="navbar-toggler-bar burger-lines"></span>
                        <span class="navbar-toggler-bar burger-lines"></span>
                    </button>
                    
			
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#TopNavBarContent">
				<span class="navbar-toggler-icon"></span>
			</button>
			
			<div class="collapse navbar-collapse" id="TopNavBarContent">
				
				<ul class="navbar-nav ml-auto">

					<sec:authorize access="hasRole('USER')">
					<li class="nav-link"><a class="tocxref btn" style="color:black;"
						href="${pageContext.request.contextPath}/user"> User Dashboard </a></li>
					</sec:authorize>
					<sec:authorize access="hasRole('PROVIDER')">
						<li class="nav-btn"><a class="btn btn-warning" style="color:black;"
						href="${pageContext.request.contextPath}/provider"> Provider Dashboard </a></li>
					</sec:authorize>
					
					<li class="nav-item">
						<form:form action="${pageContext.request.contextPath}/logout" method="POST">
							<input type="submit" value="Logout" class="btn btn-danger"/>
						</form:form> 
					</li>
				

				</ul>
			</div>
			
                </div>
            </nav>
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

				<div id="cluster-list-div1"
					class=" cluster-list-div container mx-1 my-4 col-sm-10 col-md-10 col-lg-12  ">
					<%@ include file="cluster-list.jsp"%>
				</div>

				<div id="cluster-workload-div1"
					class=" cluster-workload-div container mx-1 my-4 col-sm-10 col-md-10 col-lg-12 hidden  ">
					<%@ include file="cluster-workload.jsp"%>
				</div>

				<div id="cluster-upload-form-div1"
					class=" cluster-upload-form-div container mx-1 my-4 col-sm-10 col-md-10 col-lg-12 hidden ">
					<%@ include file="upload-cluster.jsp"%>
				</div>

				<div id="cluster-metrics-div1"
					class=" cluster-metrics-div container mx-1 my-4 col-sm-10 col-md-10 col-lg-12  ">

				</div>





			</div>

	<script src="http://code.jquery.com/jquery-3.1.1.js"></script>
	<script type="text/javascript">
		
  	var count = 0;
	var clusterListClone;
	var clusterWorkLoadClone;
	var clusterUploadFormClone;
	
	function clone(){
		clusterListClone =  $(".clusterList-div").clone();
		clusterWorkLoadClone = $(".clusterWork-div").clone();
		clusterUploadFormClone = $(".clusterUpload-div").clone();


	}
	$(window).on('load',function(){
		clone();
		show(1,'');
		show(1,'');
		
		});

	function remove(){
		$( "div" ).remove( ".clusterList-div" );
		$( "div" ).remove( ".clusterWork-div" );
		$( "div" ).remove( ".clusterUpload-div" );
		$( "div" ).remove( ".clusterMetric-div" );
	}
	
	
	function show(showId, ipInstance){
		ipInstance = '35.247.41.79:9090';
		remove();
		setActiveEmpty();
		if(showId == 1){
			document.getElementById("clusterListNavLink").className="active";
			$(".cluster-list-div").append(clusterListClone);
			createGraphs();
			
		}else if(showId == 2){
			document.getElementById("clusterWorkloadNavLink").className="active";
			$(".cluster-workload-div").append(clusterWorkLoadClone);
		}else if(showId == 3){
			document.getElementById("clusterUploadFormNavLink").className="active";
			$(".cluster-upload-form-div").append(clusterUploadFormClone);
		}else if(showId == 4){
			document.getElementById("clusterListNavLink").className="active";
			$(".cluster-metrics-div").append('<div class="clusterMetric-div"><iframe src="http://104.198.3.94:3000/d/GjdqjUrmz/global-metrics?refresh=10s&orgId=1&var-instance='+ipInstance+'&theme=light&kiosk" width="1150" height="300" frameborder="0"></iframe></div>');
		}
		
	}
	
	function setActiveEmpty(){
		document.getElementById("clusterListNavLink").className="";
		document.getElementById("clusterWorkloadNavLink").className="";
		document.getElementById("clusterUploadFormNavLink").className="";
		
	}

	</script>

	

	<div id="sample" class="container mx-1"></div>
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="/js/scripts.js"></script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>	
	</div>
	<!--   Core JS Files   -->
<script src="js/core/jquery.3.2.1.min.js" type="text/javascript"></script>
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

</html>

