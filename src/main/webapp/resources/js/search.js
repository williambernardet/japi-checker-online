/**
 * Set the console if not defined.
 */
try {
	console.log
} catch(e) {
	if (e) {
		console.log = function() {}
	}
};

function doIndex() {
	// restore default state...
	$('#intro').show();
	$('#diff').hide();
	$('#search').hide();
	$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");	
}

function getClassNameForSeverity(severity) {
	if (severity.toLowerCase() == "error") {
		return "danger";
	}
	if (severity.toLowerCase() == "warning") {
		return "warning";
	}
	return "info";
}

function setSearchBoxActive(active) {
	if (active) {
		$('#searchform').removeClass('form-search');
		$('#searchform').addClass('form-search-active');
	} else {
		$('#searchform').removeClass('form-search-active');
		$('#searchform').addClass('form-search');
	}
}

function enableVersionLinks() {
	$('.versionlink').on('click', function (e) {
        e.preventDefault();
        $('.versionlink').removeClass('active');
        $(this).addClass('active loading');
    	$.ajax({
    		type : "GET",
    		headers: { "Accept": "application/json" },
    		url : $(this).attr('href'),
    		success : function(data) {
    			console.log(data);
    			$('.versionlink.loading').removeClass('loading');
    			$('#diffresult tr:gt(0)').remove();
    			$.each(data, function(i, report) {
    				var content = '<tr class=\"' + getClassNameForSeverity(report.severity) + '\">';
    				content += '<td><img src="static/resources/img/' + report.severity.toLowerCase() + '.png" width=\"32\" heigh=\"32\"/></td>';
    				content += '<td>' + (report.source==null?'':report.source) + '</td>';
    				content += '<td>' + report.message + '</td>';
    	            content += '</tr>';
    	            $('#diffresult tr:last').after(content);
    	          });			
    		},
    		error : function(e) {
    			$('.versionlink.loading').removeClass('loading');
    			console.log('Error: ' + e);
    		}
    	});    	
    });
}

var currentSearch = null;
function doSearch() {
	var search = $('#searchbox').val();
	var exactmatch = false; //$('#exactmatch').prop("checked");

	// restore default state...
	$('#search').show();
	$('#diff').hide();
	$('#intro').hide();
	$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");
	
	setSearchBoxActive(true);
	currentSearch = $.ajax({
		type : "GET",
		url : "search",
		data : {"query": search, "exactmatch": exactmatch.toString()},
		beforeSend : function()    {           
            if(currentSearch != null) {
            	currentSearch.abort();
            }
			setSearchBoxActive(true);
        },
		success : function(data) {
			setSearchBoxActive(false);
			$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");
            var content = '<table class=\"table\">';
            content += '<thead>';
            content += '<tr>';
            content += '<th>GroupId</th>';
            content += '<th>ArtifactId</th>';
            content += '<th>Packaging</th>';
            content += '<th>Version</th>';
            content += '</tr>';
            content += '</thead>';
            content += '<tbody>';
			$.each(data, function(i, group) {
				$.each(group.artifactInfos, function(i, artifactInfos) {
		            content += '<tr>';
		            if (i == 0) {
			            content += '<td rowspan=\"' + group.artifactInfos.length + '\">' + artifactInfos.groupId + '</td>';
		            }
		            content += '<td>' + artifactInfos.artifactId + '</td>';
		            content += '<td>' + artifactInfos.fextension + '</td>';
					content += '<td><a href="diff/' + artifactInfos.groupId +
						'/'+ artifactInfos.artifactId +
						'/'+ artifactInfos.version +
						'/'+ artifactInfos.fextension + '/" class=\"searchlink\">';
					content += artifactInfos.version;
					content += '</a></td>';
		            content += '</tr>';
				});

	          });			
            content += '</tbody></table>';
            $(content).appendTo("#searchresult");
            
            $('.searchlink').on('click', function (e) {
            	console.log("loading " + $(this).attr('href'));
                e.preventDefault();
            	$('#search').hide();
            	$('#intro').hide();
            	$('#diff').show();
            	$("#diff").load($(this).attr('href'), function() {
	    			enableVersionLinks();	            		
            	});
            });		},
		error : function(e) {
			setSearchBoxActive(false);
			console.log('Error: ' + e);
			//$("#searchresult").replaceWith("<div id=\"searchresult\"></div>");
		}
	});
}
