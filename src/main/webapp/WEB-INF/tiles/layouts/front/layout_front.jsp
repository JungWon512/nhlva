<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<c:set var="URL" value="${pageContext.request.requestURL}" />
<c:set var="requestPath" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMddHHmmss" var="version" />
<!--begin::Head-->
<head>
    <!-- Google Tag Manager -->
    <!-- End Google Tag Manager -->
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <title>스마트 가축시장</title>
    <meta property="og:title" content="스마트 가축시장">
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
		<c:if test="${requestPath ne '/watchApp'}">
			<section class="header">
				<tiles:insertAttribute name="wrapper_header"/>
			</section>
		</c:if>
		<!-- //header e -->
		<section class="contents">
			<tiles:insertAttribute name="content"/>
		</section>
		<!-- //content e -->
		<section class="footer">
			<tiles:insertAttribute name="wrapper_footer"/>
		</section>
		<!-- //footer e -->
		<button id="btn_top_move"><span class="sr-only">TOP</span></button>
	</div>	
	<!-- //wrap e -->
	<tiles:insertAttribute name="body_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>?v=${version}" type="text/javascript"></script>
	<input type="hidden" value="" id="windowWidth">
	<input type="hidden" value="" id="deviceMode"/>
</body>
<!--end::Body-->
</html>









