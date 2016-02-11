<html>
<head>
<link rel="icon" href="favicon.ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<title>Usage Report</title>
</head>
<body>
	<h1>Usage Report</h1>
	<div>
		<h3>Item searched for:</h3>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">Group:</div>
			<div class="col-md-3 col-md-offset-2">${group}</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">Artifact:</div>
			<div class="col-md-3 col-md-offset-2">${artifact}</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-md-offset-2">Directory searched:</div>
			<div class="col-md-3 col-md-offset-2">${directoryName}</div>
		</div>
	</div>
	<div>
		<h3>Search Results</h3>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-striped table-bordered">
					<tr>
						<th>Group</th>
						<th>Artifact</th>
						<th>Version</th>
						<th>Parent Group</th>
						<th>Parent Artifact</th>
						<th>Version Used</th>
						<th>Classifier</th>
						<th>Scope</th>
					</tr>
					<#list usages as usage>
					<tr>
						<td>${usage.groupId}</td>
						<td>${usage.artifactId}</td>
						<td>${usage.version}</td>
						<td><#if
							usage.parentGroupId??>${usage.parentGroupId}<#else>&nbsp;</#if></td>
						<td><#if
							usage.parentArtifactId??>${usage.parentArtifactId}<#else>&nbsp;</#if></td>
						<td>${usage.versionUsed}</td>
						<td><#if
							usage.classifier??>${usage.classifier}<#else>&nbsp;</#if></td>
						<td><#if usage.scope??>${usage.scope}<#else>&nbsp;</#if></td>
					</tr>
					</#list>
				</table>
			</div>
		</div>
	</div>
</body>
</html>