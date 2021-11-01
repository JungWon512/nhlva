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
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <!--begin::Fonts -->
    <!--begin::Global Config(global config for global JS scripts)-->
    <script>var KTAppSettings = { "breakpoints": { "sm": 576, "md": 768, "lg": 992, "xl": 1200, "xxl": 1400 }, "colors": { "theme": { "base": { "white": "#ffffff", "primary": "#3E97FF", "secondary": "#E5EAEE", "success": "#08D1AD", "info": "#844AFF", "warning": "#F5CE01", "danger": "#FF3D60", "light": "#E4E6EF", "dark": "#181C32" }, "light": { "white": "#ffffff", "primary": "#DEEDFF", "secondary": "#EBEDF3", "success": "#D6FBF4", "info": "#6125E1", "warning": "#FFF4DE", "danger": "#FFE2E5", "light": "#F3F6F9", "dark": "#D6D6E0" }, "inverse": { "white": "#ffffff", "primary": "#ffffff", "secondary": "#3F4254", "success": "#ffffff", "info": "#ffffff", "warning": "#ffffff", "danger": "#ffffff", "light": "#464E5F", "dark": "#ffffff" } }, "gray": { "gray-100": "#F3F6F9", "gray-200": "#EBEDF3", "gray-300": "#E4E6EF", "gray-400": "#D1D3E0", "gray-500": "#B5B5C3", "gray-600": "#7E8299", "gray-700": "#5E6278", "gray-800": "#3F4254", "gray-900": "#181C32" } }, "font-family": "Poppins" };</script>
    <!--end::Global Config-->
    <tiles:insertAttribute name="head_footer_script"/>
</head>
<body>
	<tiles:insertAttribute name="wrapper_header"/>
	<div id="wrap">
		<section class="monitoring-view">
			<tiles:insertAttribute name="content"/>
		</section>
	</div>
	<!--begin::Main-->
<%-- 	<tiles:insertAttribute name="wrapper_header"/> --%>
	
	
	<tiles:insertAttribute name="body_footer_script"/>
    <script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
</body>
<footer class="text-center text-lg-start bg-light text-muted w-100" style="position: fixed; bottom:0;">
    <figure class="text-center">Copyright all right reserved. <cite title="Source Title">가축시장.kr</cite></figure>
</footer>
</html>