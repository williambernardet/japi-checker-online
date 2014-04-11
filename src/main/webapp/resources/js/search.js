function doSearch() {
	var search = $('#searchbox').val();
	var exactmatch = $('#exactmatch').prop("checked");
	
	$.ajax({
		type : "GET",
		url : "search",
		data : {"query": search, "exactmatch": exactmatch.toString()},
		success : function(data) {
			$("#searchprogress").hide();
			$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");
			$.each(data, function(i, group) {
	            var content = '<li>' + i;
	            content += '<ul>';
				$.each(group.artifactInfos, function(i, artifactInfos) {
					content += '<li><a href="diff/' + artifactInfos.groupId +
						'/'+ artifactInfos.artifactId +
						'/'+ artifactInfos.version +
						'/'+ artifactInfos.fextension + '/">';
					content += artifactInfos.artifactId;
					if (artifactInfos.classifier != null) {
						content += "-" +artifactInfos.classifier;
					}
					content += "-" + artifactInfos.version;
					content += "." + artifactInfos.fextension;
					content += '</a></li>';
				});
	            content += '</ul>';
	            content += '</li>';
	            $(content).appendTo("#searchresult");
	          });			
		},
		error : function(e) {
			$("#searchprogress").hide();
			alert('Error: ' + e);
			$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");
		}
	});
}


function updateDiff(imgroot, groupId, artifactId, version, extension, againstVersion) {
	console.log("updateDiff: " + groupId + "/" + artifactId + "/" + version + "/" + extension + "/" + againstVersion);
	$("#diffprogress").show();
	$.ajax({
		type : "GET",
		headers: { "Accept": "application/json" },
		url : againstVersion,
		success : function(data) {
			console.log(data);
			$("#diffprogress").hide();
			$('#result tr:gt(0)').remove();
			$.each(data, function(i, report) {
				var content = '<tr>';
				content += '<td><img src="' + imgroot + '/' + report.severity.toLowerCase() + '.png" width=\"32\" heigh=\"32\"/></td>';
				content += '<td>' + (report.source==null?'':report.source) + '</td>';
				content += '<td>' + report.message + '</td>';
	            content += '</tr>';
	            $('#result tr:last').after(content);
	          });			
		},
		error : function(e) {
			$("#diffprogress").hide();
			alert('Error: ' + e);
			$(".row-start").replaceWith("<tr class=\"row-start\"></tr>");
		}
	});
}
