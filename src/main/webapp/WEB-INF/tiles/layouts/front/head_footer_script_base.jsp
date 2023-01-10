<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMddHH" var="version" />
<!--begin::Fonts-->
<!-- <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" /> -->
<!--end::Fonts-->

<!-- 20210805 jjw 퍼블리싱 css 및 js 추가 -->
<link rel="stylesheet" type="text/css" href="/static/css/guide/common.css?v=${version}">
<!-- 20221111 jjw 고도화 퍼블리싱 css 추가 -->
<link rel="stylesheet" type="text/css" href="/static/css/guide/style-v2.css?ver=${version}">
<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/popup.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/slick.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mmenu.all.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mCustomScrollbar.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/selectric.css">

<link rel="shortcut icon" href="/static/images/guide/favicon_new.ico">
<link rel="apple-touch-icon" sizes="64x64" href="/static/images/guide/favicon_new64.png">
<link rel="icon" type="image/png" sizes="16x16" href="/static/images/guide/favicon_new.ico">
<link rel="icon" type="image/png" sizes="32x32" href="/static/images/guide/favicon_new64.png">  <!-- 32x32 적용필요-->
<link rel="icon" type="image/png" sizes="128x128" href="/static/images/guide/favicon_new128.png">

<spring:eval expression="@environment.getProperty('spring.profiles.active')" var="ACTIVE" />
<spring:eval expression="@environment.getProperty('kko.login.client.id.javascript')" var="kko_id_javascript" />
<spring:eval expression="@environment.getProperty('kko.login.redirect.url')" var="kko_redirectUrl" />
<script type="text/javascript">
	var active = "${ACTIVE}";
	var kko_redirect_url = "${kko_redirectUrl}";
	var kko_id = "${kko_id_javascript}";
</script>
