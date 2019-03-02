<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
	
	<div id="clusterUpload-div" class = "clusterUpload-div">
	
		<div class="mx-4 my-4">
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