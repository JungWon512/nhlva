<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMddHH" var="version" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>${johapData.CLNTNM } - ${subheaderTitle }</title>
	<meta property="og:title" content="${johapData.CLNTNM } - ${subheaderTitle }">
	<meta property="og:description" content="${johapData.CLNTNM } - ${subheaderTitle }" />
	<meta name="description" content="${johapData.CLNTNM } - ${subheaderTitle }" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<tiles:insertAttribute name="commons_css_block"/>
	<tiles:insertAttribute name="commons_js_block"/>
	<tiles:insertAttribute name="head_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>?v=${version}" type="text/javascript"></script>
</head>
<!-- body [s] -->
<c:set var="requestPath" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<body style="background-color: #fff;">
	<!-- wrap [s] -->
	<div id="wrap">
		<!-- admin_area [s] -->
		<section class="youtube-view ${requestPath eq '/office/auction/stream_2'? 'youtube-v2' :'' }">
			<tiles:insertAttribute name="content"/>	
			
			<tiles:insertAttribute name="body_footer_script"/>			
		</section>
		<!-- admin_area [e] -->
	</div>
	<!-- wrap [s] -->
</body>
<!-- body [e] -->
</html>