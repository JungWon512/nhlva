<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!--begin::Fonts-->
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" />
<!--end::Fonts-->
<!--begin::Page Vendors Styles(used by this page)-->
<link href="/static/assets/plugins/custom/fullcalendar/fullcalendar.bundle.css?v=2.1.1" rel="stylesheet" type="text/css" />
<!--end::Page Vendors Styles-->
<!--begin::Global Theme Styles(used by all pages)-->
<link href="/static/assets/plugins/global/plugins.bundle.css?v=2.1.1" rel="stylesheet" type="text/css" />
<!--<link href="/static/assets/css/style.bundle.css?v=2.1.1" rel="stylesheet" type="text/css" />-->
<!--end::Global Theme Styles-->
<!--begin::Layout Themes(used by all pages)-->

<spring:eval expression="@environment.getProperty('spring.profiles.active')" var="ACTIVE" />
<spring:eval expression="@environment.getProperty('kko.login.client.id.javascript')" var="kko_id_javascript" />
<spring:eval expression="@environment.getProperty('kko.login.client.id.api')" var="kko_id_api" />
<spring:eval expression="@environment.getProperty('kko.login.redirect.url')" var="kko_redirectUrl" />
<script type="text/javascript">
	var active = "${ACTIVE}";
	var kko_redirect_url = "${kko_redirectUrl}";
	var kko_id = "${kko_id_javascript}";
	var kko_api_id = "${kko_id_api}";
</script>
