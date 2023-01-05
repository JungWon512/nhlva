<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!-- admin_head [s] -->
<div class="admin_head">
	<a href="javascript:pageMove('/office/main');">NH 가축시장</a>
	<sec:authorize access="!isAnonymous()"> 
		<button type="button" class="btn_logout" onclick="javascript:pageMove('/office/user/login');">로그아웃</button>
	</sec:authorize>
</div>
<!-- admin_head [e] -->
