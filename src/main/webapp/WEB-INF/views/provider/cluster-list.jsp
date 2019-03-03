<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<html>	
	

	
	  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

<div id="clusterList-div" class="clusterList-div">
	<h3>My Clusters</h3>
	<!-- Beginning of table -->
	<c:forEach var="cluster" items="${currentAccount.clusters}"
		varStatus="count">
		<c:url var="removeLink" value="/provider/delete">
			<c:param name="clusterUrl" value="${cluster.clusterUrl}" />
		</c:url>



		<div class="card text-center  col-sm-10 col-md-10 col-lg-12">
			<div class="card-header ">
				<div class="card-header-blue">
					<h3>${cluster.clusterUrl}</h3>
					<a class=" btn active btn-danger mx-1 my-1" href="${removeLink}"
						onclick="if(!(confirm('Are you sure you want to delete cluster')))return false"
						role="button">Delete Cluster</a>
					<!-- show metrics graph hide everything else -->

					<!-- TODO: GET CLUSTER IP SUBSTRING 255.255.255.255 NOT HTTPS://255.255.255.255 -->
					<a class=" btn active btn-dark mx-1 my-1"
						href="javascript:show(4,'${cluster.clusterUrl}')" role="button">Show
						More Metric</a>
				</div>
			</div>
			<div class="card-body">
				<div class=" form-group">

					<div class="row">
						
						<c:set var="clusterUrl" value="${cluster.clusterUrl}" />
						<c:set var="ipAddress"
							value="${fn:substringAfter(clusterUrl,'https://')}" />
						<iframe
							src="http://104.198.3.94:3000/d/g1Q_TZjmk/gauges?refresh=10s&orgId=1&var-datasource=prometheus&var-instance=${ipAddress}:443&kiosk&theme=light"
							width="1150" height="300"></iframe>



					</div>
				</div>
			</div>
		</div>

	</c:forEach>
	</div>





</html>