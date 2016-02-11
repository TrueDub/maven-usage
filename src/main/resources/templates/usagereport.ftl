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
		$('#usageTable').DataTable();
	});
</script>
<title>Usage Report</title>
</head>
<body>
	<h1>Usage Report</h1>
	<div>
		<h3>Item searched for:</h3>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">
				<strong>Group:</strong>
			</div>
			<div class="col-md-3 col-md-offset-2">${group}</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">
				<strong>Artifact:</strong>
			</div>
			<div class="col-md-3 col-md-offset-2">${artifact}</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">
				<strong>Directory searched:</strong>
			</div>
			<div class="col-md-3 col-md-offset-2">${directoryName}</div>
		</div>
	</div>
	<div>
		<h3>Search Results</h3>
		<div class="row">
			<div class="col-md-12">
				<table id="usageTable" class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>Group</th>
							<th>Artifact</th>
							<th>Version</th>
							<th>Packaging</th>
							<th>Parent Group</th>
							<th>Parent Artifact</th>
							<th>Version Used</th>
							<th>Inherited</th>
							<th>Scope</th>
						</tr>
					</thead>
					<tbody>
						<#list usages as usage>
						<tr>
							<td>${usage.groupId}</td>
							<td>${usage.artifactId}</td>
							<td>${usage.version}</td>
							<td>${usage.packaging}</td>
							<td><#if
								usage.parentGroupId??>${usage.parentGroupId}<#else>&nbsp;</#if></td>
							<td><#if
								usage.parentArtifactId??>${usage.parentArtifactId}<#else>&nbsp;</#if></td>
							<td>${usage.versionUsed}</td>
							<td>${usage.versionInherited?string('Yes', 'No')}</td>
							<td><#if usage.scope??>${usage.scope}<#else>&nbsp;</#if></td>
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>