<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!-- <link href="/static/css/guide/board.css" rel="stylesheet" type="text/css" /> -->
<link href="/static/css/guide/common.css" rel="stylesheet" type="text/css" />
<!-- <link rel="stylesheet" type="text/css" href="/static/css/guide/jquery-ui.css"> -->
<link rel="stylesheet" type="text/css" href="/static/css/guide/popup.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/slick.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mmenu.all.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mCustomScrollbar.css">
<link rel="stylesheet" type="text/css" href="/static/css/guide/selectric.css">

<link rel="shortcut icon" href="/static/images/guide/favicon_admin.ico">
<link rel="apple-touch-icon" sizes="64x64" href="/static/images/guide/favicon_admin32.png">
<link rel="icon" type="image/png" sizes="16x16" href="/static/images/guide/favicon_admin.ico">
<link rel="icon" type="image/png" sizes="32x32" href="/static/images/guide/favicon_admin32.png">  <!-- 32x32 적용필요-->

<spring:eval expression="@environment.getProperty('spring.profiles.active')" var="ACTIVE" />
<spring:eval expression="@environment.getProperty('kko.login.client.id.javascript')" var="kko_id_javascript" />
<spring:eval expression="@environment.getProperty('kko.login.client.id.api')" var="kko_id_api" />
<spring:eval expression="@environment.getProperty('kko.login.redirect.url')" var="kko_redirectUrl" />
<script type="text/javascript">
	var active = "${ACTIVE}";
	var kko_redirect_url = "${kko_redirectUrl}";
	var kko_id = "${kko_id_javascript}";
	var kko_id = "${kko_id_api}";
</script>