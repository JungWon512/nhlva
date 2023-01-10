<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title><sec:authentication property="principal.naBzplNm"/> - ${subheaderTitle}</title>
	<meta property="og:title" content="<sec:authentication property="principal.naBzplNm"/> - ${subheaderTitle}">
	<meta property="og:description" content="<sec:authentication property="principal.naBzplNm"/> - ${subheaderTitle}" />
	<meta name="description" content="<sec:authentication property="principal.naBzplNm"/> - ${subheaderTitle}" />
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no, maximum-scale=1.0, user-scalable=0">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<tiles:insertAttribute name="head_footer_script"/>
</head>
<body>
	<!-- wrap [s] -->
	<div id="wrap">
		<!-- admin_area [s] -->
		<section class="admin_area">
			<tiles:insertAttribute name="wrapper_header"/>
			<tiles:insertAttribute name="content"/>
		</section>
		<!-- admin_area [e] -->
		<button id="btn_top_move"><span class="sr-only">TOP</span></button>
	</div>
	<!-- wrap [e] -->
	<tiles:insertAttribute name="body_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
</body>
</html>