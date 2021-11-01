<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<c:set var="URL" value="${pageContext.request.requestURL}" />
<!--begin::Head-->
<head>
    <!-- Google Tag Manager -->
    <!-- End Google Tag Manager -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>NH 가축시장</title>
    <meta property="og:title" content="NH 가축시장">
    <meta property="og:description" content="한우, 송아지 스마트 경매, 실시간 화상경매" />
    <meta name="description" content="한우, 송아지 스마트 경매, 실시간 화상경매" />
    
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="HandheldFriendly" content="true" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <!--end::Layout Themes-->
    <tiles:insertAttribute name="head_footer_script"/>
</head>
<!--end::Head-->
<!--begin::Body-->

<body>
	<div id="wrap">
		<section class="header">
			<tiles:insertAttribute name="wrapper_header"/>
		</section>
		<!-- //header e -->
		<section class="contents">
			<tiles:insertAttribute name="content"/>
		</section>
		<!-- //content e -->
		<section class="footer">
			<tiles:insertAttribute name="wrapper_footer"/>
		</section>
		<!-- //footer e -->
	</div>	
	<!-- //wrap e -->
	<tiles:insertAttribute name="body_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
	<input type="hidden" value="" id="windowWidth">
	<input type="hidden" value="" id="deviceMode"/>
</body>
<!--end::Body-->
</html>









