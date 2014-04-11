<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
	<head>
		<title>JAPI-Checker Online</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script src="<c:url value="/static/resources/js/jquery-2.1.0.js"/>"></script>
		<script src="<c:url value="/static/resources/js/search.js"/>"></script>
		<link href="<c:url value="/static/resources/font/stylesheet.css"/>" rel="stylesheet" type="text/css" />	
		<link href="<c:url value="/static/resources/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/resources/css/bootstrap-responsive.min.css"/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/resources/css/styles.css"/>" rel="stylesheet" type="text/css" />
		<link href="<c:url value="/static/resources/css/media-queries.css"/>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/static/resources/fancybox/jquery.fancybox-1.3.4.css"/>" media="screen" />

		<meta name="viewport" content="width=device-width" />
 
		<link rel="shortcut icon" href="<c:url value="/static/resources/favicon.ico"/>" type="image/x-icon">

		<link href='http://fonts.googleapis.com/css?family=Exo:400,800' rel='stylesheet' type='text/css'>

	</head>

	<body>
		<!-- TOP MENU NAVIGATION -->
		<div class="navbar navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<a class="brand pull-left" href="#"> JAPI-Checker Online! </a> <a
						class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a>
					<!-- a class="brand pull-left" href="#"> FlexApp </a> <a
						class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a-->
	
					<!-- div class="nav-collapse collapse">
						<ul id="nav-list" class="nav pull-right">
							<li><a href="#home">Home</a></li>
							<li><a href="#about">About</a></li>
							<li><a href="#updates">Updates</a></li>
							<li><a href="#screenshots">Screenshots</a></li>
							<li><a href="#contact">Contact</a></li>
						</ul>
					</div-->
					<form method="GET" class="navbar-form navbar-left" role="search">
						<div class="form-group">
							<img src="<c:url value="/static/resources/img/loading.gif" />" width="16" height="16"  id="searchprogress"/>
							<input type="text" id="searchbox" name="searchbox"  class="form-control" placeholder="Search"/>
						</div>
						<br/>					 
						<input type="checkbox" id="exactmatch" name="exactmatch" value="true"/> Exact match
					</form>
				<script>
				$("#searchprogress").hide();
				$("#searchbox").data('oldVal', $("#searchbox").val());
				$("#searchbox").bind("propertychange keyup input paste", function() {
					if ($("#searchbox").data('oldVal') != $("#searchbox").val()) {
						// Updated stored value
					   	$("#searchbox").data('oldVal', $("#searchbox").val());
						// start the spin box
					   	$("#searchprogress").show();
						// run the search						
						doSearch();
					}
				});
				</script>
				</div>
			</div>
		</div>
	
	
		<div class="container content container-fluid" id="search">
			<div class="row-fluid">				

				<div id="searchresult">
				</div>
			</div>
		</div>
		<div class="footer container container-fluid">
			<div id="copyright">
				JAPI-Checker (C) 2014 William Bernardet
			</div>
	
			<!-- CREDIT - PLEASE LEAVE THIS LINK! -->
			<div id="credits">
				<a href="http://github.differential.io/flexapp">Theme</a> by <a href="http://carp.io">Carp</a>.
			</div>
		</div>
	
		<script src="<c:url value="/static/resources/js/bootstrap.min.js" />"></script>
		<script src="<c:url value="/static/resources/js/bootstrap-collapse.js" />"></script>
		<script src="<c:url value="/static/resources/js/bootstrap-scrollspy.js" />"></script>
		<script src="<c:url value="/static/resources/fancybox/jquery.mousewheel-3.0.4.pack.js" />"></script>
		<script src="<c:url value="/static/resources/fancybox/jquery.fancybox-1.3.4.pack.js" />"></script>
		<script src="<c:url value="/static/resources/js/init.js" />"></script>

	</body>
</html>
