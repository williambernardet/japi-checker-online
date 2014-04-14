<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>JAPI-Checker Online</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="<c:url value="/static/resources/js/jquery-2.1.0.js"/>"></script>
<script src="<c:url value="/static/resources/js/search.js"/>"></script>
<link href="<c:url value="/static/resources/font/stylesheet.css"/>"
	rel="stylesheet" type="text/css" />
<link href="<c:url value="/static/resources/css/bootstrap.min.css"/>"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value="/static/resources/css/bootstrap-theme.min.css"/>"
	rel="stylesheet" type="text/css" />
<link
	href="<c:url value="/static/resources/css/japi-checker-style.css"/>"
	rel="stylesheet" type="text/css" />
<link href="<c:url value="/static/resources/css/media-queries.css"/>"
	rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/static/resources/fancybox/jquery.fancybox-1.3.4.css"/>"
	media="screen" />

<meta name="viewport" content="width=device-width" />

<link rel="shortcut icon"
	href="<c:url value="/static/resources/favicon.ico"/>"
	type="image/x-icon">

<link href='http://fonts.googleapis.com/css?family=Exo:400,800'
	rel='stylesheet' type='text/css'>

</head>

<body>
	<div id="wrap">
		<!-- TOP MENU NAVIGATION -->
		<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target=".navbar-collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#" id="indexlink">JAPI-Checker Online!</a>
				</div>
				<div class="navbar-collapse collapse">
					<div class="col-sm-3 col-md-3 pull-right">
						<div class="navbar-form">
							<form class="form-search form-inline" role="form" id="searchform">
								<div class="input-group">
									<input type="text" class="form-control search-query"
										placeholder="Search" id="searchbox" name="searchbox" />
								</div>
							</form>
						</div>
					</div>
				</div>
				<!--/.navbar-collapse -->
			</div>
		</div>
		<div class="jumbotron" id="intro">
			<div class="container">
				<h1>JAPI-Checker Online!</h1>
				<h2>The Java library API backward compatibility made easy</h2>
				<div id="mainblabla" class="col-md-4">
					<p></p>
					<p>Have you ever wonder what is the difference between two Java
						libraries? What is the impact of upgrading to newer version?</p>
					<p>With JAPI-Checker Online you can quickly answer those
						questions by easily compare any revision of a Java library
						available in Maven Central repository.</p>
				</div>
				<div id="#mainvideo" class="col-md-8">
					<!-- iframe width="640" height="360"
						src="http://www.youtube.com/embed/zINOgfnAbFw?feature=player_detailpage"
						frameborder="0" allowfullscreen></iframe-->
					<img src="<c:url value="/static/resources/img/video_mockup.png"/>" width="640" height="360" />
				</div>
			</div>
		</div>
		<div class="container" id="search" style="display:none;">
			<div class="row">
				<h1>Search result...</h1>
				<div id="searchresult"></div>
			</div>
		</div>
		<div class="container" id="diff" style="display:none;">
			<div class="row">
				<div id="diffresult"></div>
			</div>
		</div>
		<div id="push"></div>
	</div>
	<div id="footer">
		<div id="copyright">JAPI-Checker (C) 2014 William Bernardet</div>
	</div>

	<script src="<c:url value="/static/resources/js/bootstrap.min.js" />"></script>
	<script
		src="<c:url value="/static/resources/js/bootstrap-collapse.js" />"></script>
	<script
		src="<c:url value="/static/resources/js/bootstrap-scrollspy.js" />"></script>
	<script
		src="<c:url value="/static/resources/fancybox/jquery.mousewheel-3.0.4.pack.js" />"></script>
	<!--  script
		src="<c:url value="/static/resources/fancybox/jquery.fancybox-1.3.4.pack.js" />"></script-->
	<script src="<c:url value="/static/resources/js/init.js" />"></script>
</body>
</html>
