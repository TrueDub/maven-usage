<html>
<head>
<link rel="icon" href="favicon.ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<script src='js/jquery-1.12.0.min.js'></script>
<script src='js/jquery.dataTables.min.js'></script>
<script src='js/dataTables.bootstrap.min.js'></script>
<script>
	$(document).ready(function() {
		$('#usageTable').DataTable({
			"paging" : false
		});
	});
</script>
<title>Usage Report</title>
</head>
<body>
	<h1>Usage Report</h1>
	<div>
		<h3>Item searched for:</h3>
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<table class="table table-bordered">
					<tr>
						<td><strong>Group:</strong></td>
						<td>${runData.group}</td>
					</tr>
					<tr>
						<td><strong>Artifact:</strong></td>
						<td>${runData.artifact}</td>
					</tr>
					<tr>
						<td><strong>Directory searched:</strong></td>
						<td>${runData.searchDirectory}</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div>
		<h3>Execution statistics:</h3>
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<table class="table table-bordered">
					<tr>
						<td><strong>Time taken:</strong></td>
						<td>${runData.formattedElapsedTime}</td>
					</tr>
					<tr>
						<td><strong>POMs read:</strong></td>
						<td>${runData.pomsRead}</td>
					</tr>
					<tr>
						<td><strong>POMs reported on:</strong></td>
						<td>${runData.pomsProcessed}</td>
					</tr>
					<tr>
						<td><strong>POMs with error:</strong></td>
						<td>${runData.pomsReadError}</td>
					</tr>
					<tr>
						<td><strong>Occurrences found:</strong></td>
						<td>${runData.occurrencesFound}</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div>
		<h3>Version statistics:</h3>
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>Version</th>
							<th>Usage Count</th>
						</tr>
					</thead>
					<tbody>
						<#list runData.versionCounts?keys as version>
						<tr>
							<td>${version}</td>
							<td>${runData.versionCounts[version]}</td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div>
		<h3>Search Results:</h3>
		<div class="row">
			<div class="col-md-12">
				<table id="usageTable" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Group</th>
							<th>Artifact</th>
							<th>Version</th>
							<th>Version Used</th>
							<th>Scope</th>
						</tr>
					</thead>
					<tbody>
						<#list runData.usages as usage>
						<tr>
							<td>${usage.groupId}</td>
							<td>${usage.artifactId}</td>
							<td>${usage.version}</td>
							<td>${usage.versionUsed}</td>
							<td><#if usage.scope??>${usage.scope}<#else>&nbsp;</#if></td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div>
		<h3>POMs not processed due to error:</h3>
		<div class="row">
			<div class="col-md-12">
				<ul>
					<#list runData.pomsInError as pom>
					<li>${pom}</li> </#list>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>