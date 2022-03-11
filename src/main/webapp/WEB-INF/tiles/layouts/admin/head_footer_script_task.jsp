<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!-- <script type="text/javascript" src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js" crossorigin="anonymous"></script> -->
<!-- <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" crossorigin="anonymous"></script> -->
<!-- <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" /> -->

<link rel="stylesheet" type="text/css" href="/static/css/guide/common.css?version=20211108" />
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
<script type="text/javascript">
	var active = "${ACTIVE}";
</script>