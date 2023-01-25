<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<div class="login_area sms_authentication">
	<form name="frm" id="frm" method="post">
		<input type="hidden" id="kakao_access_token" name="kakao_access_token" value="${kkoAccessToken}" />
		<input type="hidden" id="birthyear" name="birthyear" value="${birthyear}" />
		<input type="hidden" id="birthday" name="birthday" value="${birthday}" />
		<input type="hidden" id="name" name="name" value="${name}" />
		<input type="hidden" id="state" name="state" value="${inputParam.state}" />
		<input type="hidden" id="place" name="place" value="${place}" />
	</form>
</div>
<!-- //login_area e -->

<script type="text/javascript" src="/static/js/common/core.min.js"></script>
<script type="text/javascript" src="/static/js/common/sha512.min.js"></script>

