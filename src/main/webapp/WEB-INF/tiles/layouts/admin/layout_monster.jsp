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
    <tiles:insertAttribute name="head_footer_script"/>
</head>
<body>
	<div id="wrap">
		<section class="monitoring-view">
			<tiles:insertAttribute name="wrapper_header"/>
			<tiles:insertAttribute name="content"/>
		</section>
	</div>
	
	<tiles:insertAttribute name="body_footer_script"/>
	<script src="<tiles:getAsString name="page_footer_script"/>" type="text/javascript"></script>
</body>
</html>