<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


	<div class="container" id="diff">
		<div class="row">
			<div class="col-md-12">
				<h1>Reference <c:out value="${groupId}:${artifactId}-${version}.${extension}"/></h1>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2">
				Select a version to compare to:
				<div class="list-group" id="versionlist">
					<c:forEach items="${artifacts}" var="artifact">     
   						<a href="<c:out value="diff/${groupId}/${artifactId}/${version}/${extension}/${artifact.version}"/>" class="list-group-item versionlink"><c:out value="${artifact.version}"/></a>
					</c:forEach>				
				</div>
			</div>
			<div class="col-md-10">
				<table width="100%" class="table" id="diffresult">
					<tr>
						<td>Severity</td>
						<td>Source</td>
						<td>Message</td>
					</tr>
				</table>				
			</div>
		</div>
	</div>

		<!-- div class="container content container-fluid" id="search">
			<div class="row-fluid">				
				<h2>Reference <c:out value="${groupId}:${artifactId}-${version}.${extension}"/></h2>
				<div id="choices" class="span2">
					<form:form>
						<c:forEach items="${artifacts}" var="artifact">     
   							<input type="radio" name="versionchoice" value="<c:out value="${artifact.version}"/>" /><c:out value="${artifact.version}"/><br/>
						</c:forEach>				
					</form:form>
					<img src="<c:url value="/static/resources/img/loading.gif" />" width="16" height="16"  id="diffprogress"/><br/>
   					<script>
   						$("#diffprogress").hide();
   						$("input[name=versionchoice]:radio").change(function() {
  							var imgRoot = '<c:url value="/static/resources/img"/>';
  							var groupId = '<c:out value="${groupId}"/>';
  							var artifactId = '<c:out value="${artifactId}"/>';
  							var version = '<c:out value="${version}"/>';
  							var extension = '<c:out value="${extension}"/>';
  							var againstVersion = $('input[name=versionchoice]:checked').val();
  							updateDiff(imgRoot, groupId, artifactId, version, extension, againstVersion);
  						});
   					</script>
				</div>
				<div id="diff" class="span9">
					<table width="100%" id="result">
						<tr>
							<td>Severity</td>
							<td>Source</td>
							<td>Message</td>
						</tr>
					</table>				
				</div>
			</div>
		</div-->
