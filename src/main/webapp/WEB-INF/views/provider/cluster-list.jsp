<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>	
	<style>
		.modal{display:none;position:fixed;z-index:1;padding-top:100px;left:0;top:0;width:100%;height:100%;overflow:auto;background-color:#000;background-color:rgba(0,0,0,.4)}.modal-content{background-color:#fefefe;margin:auto;padding:20px;border:1px solid #888;width:80%}.close{color:#aaa;float:right;font-size:28px;font-weight:700}.close:focus,.close:hover{color:#000;text-decoration:none;cursor:pointer}
		
	</style>
	<div id="clusterList-div">
		
		<h3>My Clusters</h3>
		<!-- Beginning of table -->
		<table class="table table-bordered">
			<thead class="thead-light">
				<tr>
					<th><h5>Cluster URL</h5></th>
					<th><h5>Options</h5></th>
					<th><h5>Metrics</h5></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="cluster" items="${currentAccount.clusters}" varStatus="count">
					<c:url var="removeLink" value="/provider/delete">
						<c:param name="clusterUrl" value="${cluster.clusterUrl}" />
					</c:url>
					
					<tr>
						<td>${cluster.clusterUrl}</td>
						<td>
							<a class="btn btn-outline-primary" href="${removeLink}" onclick="if(!(confirm('Are you sure you want to delete cluster')))return false" role="button">Delete Cluster</a>
							<a class="btn btn-light" href="#" role="button" data-modal="myModalA">Show More Metrics</a>
							<!-- <button class="myBtn" data-modal="myModalA">Open Modal A</button> -->
							<div id="myModalA" class="modal">
								<div class="modal-content">
									<span class="close">&times;</span>
										<p> Cluster Metrics Placeholder</p>
									</div>
							</div>
						</td>
						 <td>
							<div id="chart_div_${count.index}" style="width:200px; height: 150px;"></div>
							
						</td> 
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  	<script type="text/javascript">
	  	google.charts.load('current', {'packages':['gauge']});
	    google.charts.setOnLoadCallback(function()
	    		{
	    	   var chart = "chart_div_";
	    	   for (var i=0; i<8; i++){
	    		   var ID = String(chart) + String(i);
	    		   drawChart(ID);
	    	   }
	    	});
	    
	    var btns = document.getElementsByClassName('btn btn-light'),
	    // These variables will hold the currently open modal and close button
	    modal, closeBtn;

		// For each button
		for(var i=0; i<btns.length; i++) {
		    // On click
		    btns[i].addEventListener('click', function(){
		      // Get the modal ID
		      var modalId = this.getAttribute('data-modal');
		      // Retrieve the corresponding modal
		      modal = document.getElementById(modalId);
		      // Retrieve the close button
		      closeBtn = modal.querySelector('.close');
		      // Show the modal
		      modal.style.display = "block";
		    }, false);
		}

		window.addEventListener('click', function(event) {
		    // If we clicked on the backdrop or the close button
		    if (event.target == modal || event.target == closeBtn) {
		        // Hide the modal
		        modal.style.display = "none";
		    }
		}, false);
	    
	    function drawChart(ID) {

	        var data = google.visualization.arrayToDataTable([
	          ['Label', 'Value'],
	          ['Memory', Math.round(50 * Math.random())],
	          ['CPU', Math.round(89 * Math.random())],
	          ['Network', Math.round(10 * Math.random())]
	        ]);

	        var options = {
	          width: 400, height: 120,
	          redFrom: 90, redTo: 100,
	          yellowFrom:75, yellowTo: 90,
	          minorTicks: 5
	        };
	       
	        var chart = new google.visualization.Gauge(document.getElementById(ID));

	        chart.draw(data, options);

	        setInterval(function() {
	          data.setValue(0, 1, 20 + Math.round(10 * Math.random()));
	          chart.draw(data, options);
	        }, 5000);
	        setInterval(function() {
	          data.setValue(1, 1, 5 + Math.round(5 * Math.random()));
	          chart.draw(data, options);
	        }, 5000);
	        setInterval(function() {
	          data.setValue(2, 1, 30 + Math.round(20 * Math.random()));
	          chart.draw(data, options);
	        }, 5000);
	      }  
	    
	    
  </script>
</html>