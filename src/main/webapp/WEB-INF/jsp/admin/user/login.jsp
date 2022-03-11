<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script type="text/javascript" src="/static/js/common/commons.js"></script>
<script type="text/javascript" src="/static/js/common/rsa/jsbn.js"></script>
<script type="text/javascript" src="/static/js/common/rsa/prng4.js"></script>
<script type="text/javascript" src="/static/js/common/rsa/rng.js"></script>
<script type="text/javascript" src="/static/js/common/rsa/rsa.js"></script>

<input type="hidden" id="RSAKey"      value="${RSAKey }"/>
<input type="hidden" id="RSAModulus"  value="${RSAModulus }"/>
<input type="hidden" id="RSAExponent" value="${RSAExponent }"/>
<div class="login_section">
	<div class="login_box">
		<h1>로그인<span>스마트가축시장</span></h1>
		<form class="form-signin" method="post">
			<fieldset>
				<input type="text" id="usrid" name="usrid" class="inp inp_id" placeholder="아이디" required autofocus />
				<input type="password" id="pw" name="pw" class="inp inp_pw" placeholder="패스워드" required />
				<input type="checkbox" id="save_id">
				<label for="save_id">아이디 저장</label>
				<button type="button" class="btn_login action-submit">로그인</button>
				<button type="button" class="btn_login btnApkDownload" style="display:none;">App 다운로드</button>
			</fieldset>			
		</form>
	</div>
</div>