<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	
	<div id="clusterList-div">
		
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
		
	</div>
</html>