<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<!--begin::Head-->
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>가축시장</title>
	<link rel="SHORTCUT ICON" href="/static/images/guide/favicon_new.ic" />
	<meta name="description" content="Updates and statistics" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="HandheldFriendly" content="true" />
	<meta name="apple-mobile-web-app-capable" content="yes">
<!-- 	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" /> -->
	<link rel="stylesheet" type="text/css" href="/static/css/guide/common.css">
<!-- 	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery-ui.css"> -->
	<link rel="stylesheet" type="text/css" href="/static/css/guide/popup.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/slick.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mmenu.all.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/jquery.mCustomScrollbar.css">
	<link rel="stylesheet" type="text/css" href="/static/css/guide/selectric.css">
	<script type="text/javascript" src="/static/js/guide/jquery-1.12.4.js" style="user-select: auto;"></script>
	<link rel="apple-touch-icon" sizes="64x64" href="/static/images/guide/favicon_new64.png">
	<link rel="icon" type="image/png" sizes="16x16" href="/static/images/guide/favicon_new.ico">
	<link rel="icon" type="image/png" sizes="32x32" href="/static/images/guide/favicon_new64.png">
	<link rel="icon" type="image/png" sizes="128x128" href="/static/images/guide/favicon_new128.png">
	<script src="/static/js/common/utils.js"></script>
</head>
<!--end::Head-->
<!--begin::Body-->
<body>
	<div id="wrap">
		<div class="error">
			<div class="inner">
				<fmt:formatNumber type="number" value="${requestScope['javax.servlet.error.status_code']}" var="errCode"/>
				<div class="ico-error">
					<img src="/static/images/guide/ico_404.svg" alt="">
				</div>
				<h2 style="letter-spacing: -8px;">
					<c:forEach var="i" begin="0" end="${fn:length(errCode) - 1}" step="1" varStatus="s">
						<c:choose>
							<c:when test="${s.index % 2 eq '1'}">
								<span>${fn:substring(errCode, s.index, s.index + 1)}</span>
							</c:when>
							<c:otherwise>
								${fn:substring(errCode, s.index, s.index + 1)}
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</h2>
				<p class="txt1">페이지를 찾을 수 없습니다.</p>
				<p class="txt2">입력하신 인터넷 주소(URL) 또는 <br>경로를 다시 한 번 확인해주시기 바랍니다.</p>
				<c:choose>
			<c:when test="${fn:indexOf(path, '/office') > -1}">
				<a href="javascript:pageMove('/office/main');">메인으로</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:pageMove('/home');">가축시장 홈</a>
			</c:otherwise>
			</c:choose>
			</div>
		</div>
	</div>
	<!-- //wrap e -->
</body>
<!--end::Body-->
</html>