<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<h1 class="test">OAUTH</h1>

<br/><br/><br/>

<br/><br/><br/>
1111
<br/><br/><br/>
${json.refresh_token}
<br/><br/><br/>
${json.access_token}



<div class="login_area sms_authentication">
	<form name="frm" id="frm" method="post">
		<input type="hidden" id="kakao_refresh_token" value="${json.refresh_token}" />
		<input type="hidden" id="kakao_access_token" value="${json.access_token}" />
		<div class="login_top" id="login_info">
			<h3></h3>
			<dl>
				<dd><input type="text" id="userName" name="userName" placeholder="이름" maxlength="20" required autofocus/></dd>
				
				<dd><input type="text" id="password" name="birthDate" placeholder="생년월일(6자리) 또는 사업자번호" maxlength="10" pattern="\d*" required inputmode="numeric"/></dd>
				<dd><input type="hidden" id="password" name="telNo" placeholder="전화번호 또는 휴대전화번호" maxlength="11" pattern="\d*" required inputmode="numeric" /></dd>
			</dl>
			<a href="javascript:;" class="btn_login">인증</a>
		</div>
	</form>
</div>
<!-- //login_area e -->

<script type="text/javascript" src="/static/js/common/core.min.js"></script>
<script type="text/javascript" src="/static/js/common/sha512.min.js"></script>

