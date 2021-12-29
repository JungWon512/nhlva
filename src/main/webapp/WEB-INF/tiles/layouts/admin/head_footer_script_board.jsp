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

<spring:eval expression="@environment.getProperty('spring.profiles.active')" var="ACTIVE" />
<script type="text/javascript">
	var active = "${ACTIVE}";
</script>