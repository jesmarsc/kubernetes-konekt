<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>	
	<style>
		.modal{display:none;position:fixed;z-index:1;padding-top:100px;left:0;top:0;width:100%;height:100%;overflow:auto;background-color:#000;background-color:rgba(0,0,0,.4)}.modal-content{background-color:#fefefe;margin:auto;padding:20px;border:1px solid #888;width:80%}.close{color:#aaa;float:right;font-size:28px;font-weight:700}.close:focus,.close:hover{color:#000;text-decoration:none;cursor:pointer}
		font-family: 'Maven Pro', sans-serif;
		
	</style>
	  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script type="text/javascript">
	var totalCluster = 0;
	Chart.pluginService
			.register({
				beforeDraw : function(chart) {
					if (chart.config.options.elements.center) {
						//Get ctx from string
						var ctx = chart.chart.ctx;

						//Get options from the center object in options
						var centerConfig = chart.config.options.elements.center;
						var fontStyle = centerConfig.fontStyle || 'Arial';
						var txt = centerConfig.text;
						var color = centerConfig.color || '#000';
						var sidePadding = centerConfig.sidePadding || 20;
						var sidePaddingCalculated = (sidePadding / 100)
								* (chart.innerRadius * 2)
						//Start with a base font of 30px
						ctx.font = "30px " + fontStyle;

						//Get the width of the string and also the width of the element minus 10 to give it 5px side padding
						var stringWidth = ctx.measureText(txt).width;
						var elementWidth = (chart.innerRadius * 2)
								- sidePaddingCalculated;

						// Find out how much the font can grow in width.
						var widthRatio = elementWidth / stringWidth;
						var newFontSize = Math.floor(30 * widthRatio);
						var elementHeight = (chart.innerRadius * 2);

						// Pick a new font size so it will not be larger than the height of label.
						var fontSizeToUse = Math
								.min(newFontSize, elementHeight);

						//Set font settings to draw it correctly.
						ctx.textAlign = 'center';
						ctx.textBaseline = 'middle';
						var centerX = ((chart.chartArea.left + chart.chartArea.right) / 2);
						var centerY = ((chart.chartArea.top + chart.chartArea.bottom) / 2);
						ctx.font = fontSizeToUse + "px " + fontStyle;
						ctx.fillStyle = color;

						//Draw text in center
						ctx.fillText(txt, centerX, centerY);
					}
				}
			});
	function createChart(elementID, ChartName, ChartValue, TextUnits,
			graphColor) {
		var myChart = document.getElementById(elementID).getContext('2d');

		var theChart = new Chart(myChart, {
			type : 'doughnut', // bar, horizontalBar, pie, line, doughnut, radar, polarArea
			data : {
				labels : [ ChartName, "" ],
				datasets : [ {
					label : [ ChartName ],
					data : [ ChartValue, 100 - ChartValue ],
					backgroundColor : [ graphColor, '#F0F0F0',

					],
					borderWidth : 1,
					borderColor : '#777'
				} ]
			},
			options : {
				elements : {
					center : {
						text : ChartValue + TextUnits,
						color : graphColor, // Default is #000000
						fontStyle : 'Arial', // Default is Arial
						sidePadding : 20
					// Defualt is 20 (as a percentage)
					}
				},
				title : {
					display : true,
					text : ChartName,
					fontSize : 25
				},
				legend : {
					display : false,
					position : 'right',
					labels : {
						fontColor : '#000'
					}
				},
				layout : {
					padding : {
						left : 50,
						right : 0,
						bottom : 0,
						top : 0
					}
				},
				tooltips : {
					enabled : false
				}
			}
		});
		return theChart;
	}
	// TODO: REMOVE ALL LINES OF instanceIp = '35.247.41.79:9090' WHEN TEST AGAINS REAL INSTANCES
	var cpuResponse;
	var memoryResponse;
	var networkUploadResponse;
	var networkDownloadResponse;

	var HttpClient = function() {
		this.get = function(aUrl, aCallback) {
			var anHttpRequest = new XMLHttpRequest();
			var response;
			anHttpRequest.onreadystatechange = function() {

				if (anHttpRequest.readyState == 4
						&& anHttpRequest.status == 200) {
					response = anHttpRequest.responseText;
					aCallback(response);
				}
			}
			anHttpRequest.open("GET", aUrl, true);
			anHttpRequest.send(null);
		}
	}

	function makeHttpRequestForCpu(instanceIp) {
		instanceIp = '35.247.41.79:9090';
		var theurl = 'http://35.247.41.79:9090/api/v1/query?query=1-avg(irate(node_cpu_seconds_total{instance="'
				+ instanceIp + '",mode="idle"}[2m]))';
		var client = new HttpClient();
		client.get(theurl, function(response) {
			cpuResponse = JSON.parse(response)
		});
		var cpuUsage = 0;

		if (cpuResponse != null) {
			cpuUsage = (cpuResponse.data.result[0].value)[1] * 100;
		}
		return cpuUsage
	}

	function makeHttpRequestForMemory(instanceIp) {

		instanceIp = '35.247.41.79:9090';
		//{instance="URL"}

		var theurl = 'http://35.247.41.79:9090/api/v1/query?query=1-sum(node_memory_MemFree_bytes{instance="'
				+ instanceIp
				+ '"}%2Bnode_memory_Cached_bytes{instance="'
				+ instanceIp
				+ '"}%2Bnode_memory_Buffers_bytes{instance="'
				+ instanceIp
				+ '"})/sum(node_memory_MemTotal_bytes{instance="'
				+ instanceIp + '"})';

		var client = new HttpClient();
		client.get(theurl, function(response) {
			memoryResponse = JSON.parse(response);
		});
		var memoryUsage = 0;
		if (memoryResponse != null) {
			memoryUsage = (memoryResponse.data.result[0].value)[1] * 100;
		}

		return memoryUsage;
	}

	function makeHttpRequestForNetworkUpload(instanceIp) {
		instanceIp = '35.247.41.79:9090';
		var params = '{instance="' + instanceIp + '",device="eth0"}';
		var theurl = 'http://35.247.41.79:9090/api/v1/query?query='
				+ 'sum(irate(node_network_transmit_bytes_total' + params
				+ '[2m]))';
		var client = new HttpClient();
		client.get(theurl, function(response) {
			networkUploadResponse = JSON.parse(response);
			console.log("upload")
			console.log(response)
		});

		var networkUsage = 0;
		if (networkUploadResponse != null) {
			networkUsage = Number((networkUploadResponse.data.result[0].value)[1]);
		}
		return networkUsage;
	}

	function makeHttpRequestForNetworkDownload(instanceIp) {
		instanceIp = '35.247.41.79:9090';
		var params = '{instance="' + instanceIp + '",device="eth0"}';
		var theurl = 'http://35.247.41.79:9090/api/v1/query?query='
				+ 'sum(irate(node_network_receive_bytes_total' + params
				+ '[2m]))';
		var client = new HttpClient();
		client.get(theurl, function(response) {
			networkDownloadResponse = JSON.parse(response);
			console.log("download")
			console.log(response);
		});

		var networkUsage = 0;
		if (networkDownloadResponse != null) {
			networkUsage = Number((networkDownloadResponse.data.result[0].value)[1]);
		}
		console.log('download network');
		console.log(networkUsage);
		return networkUsage;
	}

	function getCpu(instanceIp) {
		return Math.round(makeHttpRequestForCpu(instanceIp));

	}
	function getMemory(instanceIp) {
		return Math.round(makeHttpRequestForMemory(instanceIp));

	}
	function getNetwork(instanceIp) {
		return Math.round((makeHttpRequestForNetworkUpload(instanceIp) + makeHttpRequestForNetworkDownload(instanceIp)) / 1000);

	}
</script>
<div id="clusterList-div">

	<h3>My Clusters</h3>
	<!-- Beginning of table -->
	<c:forEach var="cluster" items="${currentAccount.clusters}"
		varStatus="count">
		<c:url var="removeLink" value="/provider/delete">
			<c:param name="clusterUrl" value="${cluster.clusterUrl}" />
		</c:url>
		<script>
					totalCluster += 1;
		</script>
		<div class="jumbotron rounded form-group ">
			<div class="row form-group">
				<div class="col-lg-6 d-flex flex-wrap align-content-center border  border-top-0 border-bottom-0">
					<div class="text-secondary">
						<h1>Cluster Url</h1>
					</div>

				</div>
				<div class="col-lg-6 d-flex flex-wrap align-content-center border  border-top-0 border-bottom-0">
					<div class="text-secondary">
						<h1>Options</h1>
					</div>

				</div>
			</div>
			<div class="row form-group">
				<div class="col-lg-6 align-content-center border border-top-0 border-bottom-0 ">
					<h3>${cluster.clusterUrl}</h3>
				</div>
				<div class="col-lg-6 d-flex flex-wrap align-content-center border border-top-0 border-bottom-0">
					<div class="mt-2 mr-1">
					<a class=" btn btn-danger" href="${removeLink}"
						onclick="if(!(confirm('Are you sure you want to delete cluster')))return false"
						role="button">Delete Cluster</a> 
					</div>
					<div class="mt-2 mr-1">
					<a class="btn btn-dark" href="#"
						role="button" data-modal="myModalA">Show More Metrics</a>
					</div>
				</div>
			</div>
			<div class="container form-group">
				<div class="row">
					<div class="col-md-4">
						<canvas id="myChart1_${count.index}" width="200" height="200"></canvas>
						<object>
							<param id="ip_instance_${count.index}"
								value="${cluster.prometheusIp}">
						</object>
						</div>
					<div class="col-md-4">
						<canvas id="myChart2_${count.index}" width="200" height="200"
							></canvas>
						
					</div>
					<div class="col-md-4">
						<canvas id="myChart3_${count.index}" width="200" height="200"
							></canvas>
					</div>
				</div>
			</div>
		</div>
	</c:forEach>
</div>

<!-- TESTING GRAPHS -->

<!-- END OF TESTING GRAPHS -->

<script>

$(window).load(function() {
	for(var i = 0; i  <totalCluster; i++){
		var ipInstanceID = "ip_instance_" + String(i)
		drawMyGraphs(String(i),ipInstanceID)
	}	
});

function drawMyGraphs(chartID,ipInstanceID){
	var ipInstance = document.getElementById(ipInstanceID).value;
	var chart1 = createChart("myChart1_"+chartID, "CPU", 0, ' %','#00d2f9')
	var chart2 = createChart("myChart2_"+chartID, "MEMORY", 0, ' %','#1E90FF')
	var chart3 = createChart("myChart3_"+chartID, "NETWORK", 0,' KB/s','#0001ff')
	
	setInterval(function(){
		var networkValue = getNetwork(ipInstance);
		var percentOfGraph = networkValue/10;
		chart3.data.datasets[0].data[0] = percentOfGraph;
		chart3.data.datasets[0].data[1]= 100 - percentOfGraph;
		chart3.options.elements.center.text = networkValue +' KB/s';
		chart3.update();
	},2000);
    setInterval(function(){
    	var cpuValue = getCpu(ipInstance);
    	chart1.data.datasets[0].data[0] = cpuValue;
    	chart1.data.datasets[0].data[1]= 100 - cpuValue;
    	chart1.options.elements.center.text = cpuValue +' %';
    	chart1.update();
    }, 2000);
    setInterval(function(){
    	var memoryValue = getMemory(ipInstance);
    	chart2.data.datasets[0].data[0] = memoryValue;
    	chart2.data.datasets[0].data[1]= 100 - memoryValue;
    	chart2.options.elements.center.text = memoryValue +' %';
    	chart2.update();
    }, 2000);	
}

</script>

</html>