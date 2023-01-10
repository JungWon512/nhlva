<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
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
	<script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
</head>
<!-- body [s] -->
<body style="background-color: #fff;">
	<!-- wrap [s] -->
	<div id="wrap">
		<!-- admin_area [s] -->
		<section class="admin_area" style="max-width: 1330px;">
			<tiles:insertAttribute name="wrapper_header"/>
			
			<tiles:insertAttribute name="content"/>	
			
			<tiles:insertAttribute name="body_footer_script"/>
			
			<section class="admin_footer">Copyright(c) 가축시장.kr All right reserved.</section>
		</section>
		<!-- admin_area [e] -->
	</div>
	<!-- wrap [s] -->
</body>
<!-- body [e] -->
</html>