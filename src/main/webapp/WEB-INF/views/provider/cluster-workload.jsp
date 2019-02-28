<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	
	<div class="clusterWork-div" id="clusterWork-div">

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
		
	</div>
</html>