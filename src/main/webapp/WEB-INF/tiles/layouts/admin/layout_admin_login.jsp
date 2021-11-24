<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>TITLE</title>
	<meta name="description" content="Description">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<tiles:insertAttribute name="commons_css_block"/>
	<tiles:insertAttribute name="commons_js_block"/>
	<tiles:insertAttribute name="head_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
</head>
<body>
	<tiles:insertAttribute name="content"/>	
	
	<tiles:insertAttribute name="body_footer_script"/>
</body>
</html>