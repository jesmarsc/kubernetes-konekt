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
		
	</div>
	<div id="gauge_div" style="width:300px; height: 200px;"></div>
	<input type="button" value="Go Faster" onclick="changeTemp(1)" />
  <input type="button" value="Slow down" onclick="changeTemp(-1)" />
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  	<script type="text/javascript">
	    google.charts.load('current', {'packages':['gauge']});
	    google.charts.setOnLoadCallback(drawGauge);
	
	    var gaugeOptions = {min: 0, max: 100, yellowFrom: 70, yellowTo: 85,
	      redFrom: 85, redTo: 100, minorTicks: 5};
	    var gauge;
	
	    function drawGauge() {
	      gaugeData = new google.visualization.DataTable();
	      gaugeData.addColumn('number', 'CPU');
	      gaugeData.addColumn('number', 'RAM');
	      gaugeData.addColumn('number', 'Networking')
	      gaugeData.addRows(3);
	      gaugeData.setCell(0, 0, 100);
	      gaugeData.setCell(0, 1, 80);
		  gaugeData.setCell(0,2,70);
	      gauge = new google.visualization.Gauge(document.getElementById('gauge_div'));
	      gauge.draw(gaugeData, gaugeOptions);
	    }
	
	    function changeTemp(dir) {
	      gaugeData.setValue(0, 0, gaugeData.getValue(0, 0) + dir * 25);
	      gaugeData.setValue(0, 1, gaugeData.getValue(0, 1) + dir * 20);
	      gaugeData.setValue(0, 2, gaugeData.getValue(0, 2) + dir * 40);
	      gauge.draw(gaugeData, gaugeOptions);
	    }
  </script>
</html>