<html>
<head>
<link rel="icon" href="favicon.ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<title>Usage Report</title>
</head>
<body>
	<h1>Usage Report</h1>
	<div>
		Item searched for:
		<div>${group}</div>
		<div>${artifact}</div>
<div> in ${directoryName}</div>
	</div>
	<div>
		<table class="table table-bordered table-striped">
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
				<td><#if usage.parentGroupId??>${usage.parentGroupId}<#else>&nbsp;</#if></td>
				<td><#if usage.parentArtifactId??>${usage.parentArtifactId}<#else>&nbsp;</#if></td>
				<td>${usage.versionUsed}</td>
				<td><#if usage.classifier??>${usage.classifier}<#else>&nbsp;</#if></td>
				<td><#if usage.scope??>${usage.scope}<#else>&nbsp;</#if></td>
			</tr>
</#list>
		</table>
	</div>
</body>
</html>