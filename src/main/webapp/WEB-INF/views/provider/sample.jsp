<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	
	<div id="cluster-div">
		
		<h3>My Clusters</h3>
		<!-- Beginning of table -->
		<table class="table table-bordered">
			<thead class="thead-light">
				<tr>
					<th><h5>Cluster URL</h5></th>
					<th><h5>Options</h5></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="cluster" items="${currentAccount.clusters}">
					<c:url var="removeLink" value="/provider/delete">
						<c:param name="clusterUrl" value="${cluster.clusterUrl}" />
					</c:url>
					
					<tr>
						<td>${cluster.clusterUrl}</td>
						<td>
							<a class="btn btn-outline-primary" href="${removeLink}" onclick="if(!(confirm('Are you sure you want to delete cluster')))return false" role="button">Delete Cluster</a>
							<a class="btn btn-light" href="#" role="button">Another Option</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- End of Table -->

		<h3>Running On Your Clusters</h3>
		<!-- Beginning of table -->
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>Cluster URL</th>
					<th>Name</th>
					<th>Kind</th>
					<th>Option(s)</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="container" items="${runningContainers}">
					<c:url var="removeLink" value="/provider/delete-container">
						<c:param name="containerId" value="${container.id}" />
					</c:url>
					<tr>
						<td>${container.clusterUrl}</td>
						<td>${container.containerName}</td>
						<td>${container.kind}</td>
						<td>
							<a class="btn btn-primary" href="${removeLink}" onclick="if(!(confirm('Are you sure you want to delete container')))return false" role="button">Delete Container</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- End of Table -->
		<br/>
		<div class-"mx-4 my-4">
			<h3>Upload New Cluster URL</h3>
		</div>
		
		<!-- New cluster upload -->
		<form:form action="/provider/upload" modelAttribute="newClusterForm">
			<!-- Action will be to send to confirmation page and validate -->
			<div class="form-group row mx-1 my-4 col-sm-10 col-md-10 col-lg-6">
				<label> Cluster URL (i.e. https://122.198.122.166): </label>
				<form:input class="form-control" path="clusterUrl" />
				<form:errors path="clusterUrl" cssClass="error" />
			</div>
			<div class="form-group row mx-1 my-4 col-sm-10 col-md-10 col-lg-6">
				<label> Cluster Username (must have admin privileges): </label>
				<form:input class="form-control" path="clusterUsername" />
				<form:errors path="clusterUsername" cssClass="error" />
			</div>
			<div class="form-group row mx-1 my-4 col-sm-10 col-md-10 col-lg-6">
				<label> Cluster Password: </label>
				<form:password class="form-control" path="clusterPassword" />
				<form:errors path="clusterPassword" cssClass="error" />
			</div>
			<div class="form-group row mx-4 my-4">
				<input class="btn btn-primary text-center" type="submit"
					value="Submit" />
			</div>
			
			<br/><br/>
		</form:form>

</div>
</html>