<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<div class="kt-container">
	<form name="frm_info" id="frm_info">
		<input type="hidden" name="place" value="${naBzPlcNo}" />
		<input type="hidden" name="auth_no" value="${authNoYmd.AUTH_NO}" />
		<input type="hidden" name="auth_ymd" value="${authNoYmd.AUTH_YMD}" />
		<div class="my_info">
			<dl class="profile">
				<dt class="photo">
					<img src="/static/images/guide/v2/ic_my.svg" alt="기본프로필" />
				</dt>
				<dd class="name">
					<sec:authorize access="!isAnonymous()">
						<sec:authorize access="hasRole('ROLE_BIDDER')">
							<sec:authentication property="principal.sraMwmnnm"/>
						</sec:authorize>
						<sec:authorize access="hasRole('ROLE_FARM')">
							<sec:authentication property="principal.ftsnm"/>
						</sec:authorize>
					</sec:authorize>
				</dd>
				<dd class="mpno" style="min-height:26px;"><sec:authentication property="principal.cusMpno"/></dd>
				<dd class="intg_no">(회원번호 : <sec:authentication property="principal.mbIntgNo"/>)</dd>
				<!-- TODO : 농가를 아예 통합회원에서 제거하게 되면 수정 or 제거해야 할 부분-->
			</dl>
			<a href="javascript:logoutProc();" role="button" class="btn-logout">로그아웃</a>
			<ul class="menu_1">
				<li>
					<span>나의 경매내역</span>
					<ul class="menu_2">
						<sec:authorize access="!isAnonymous()">
							<sec:authorize access="hasRole('ROLE_BIDDER')">
								<li><a href="javascript:pageMove('/my/buy');">구매내역</a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('ROLE_FARM')">
								<li><a href="javascript:pageMove('/my/entry');">출하내역</a></li>
							</sec:authorize>
						</sec:authorize>
					</ul>
				</li>
				<sec:authorize access="!isAnonymous()">
					<sec:authorize access="hasRole('ROLE_BIDDER')">
						<c:if test="${secAplyPossible eq 'Y'}">
						<li><a href="javascript:secApplyProc();">이용해지 신청</a></li>
						</c:if>
						<c:if test="${secAplyPossible eq 'N'}">
						<li><a href="javascript:secWithdrawProc();">해지신청 철회</a></li>
						</c:if>
					</sec:authorize>
					<c:if test="${authNoYmd.KIOSK_YN eq '1'}">
						<li><a href="javascript:;" class="kiosk_btn off">키오스크 인증번호 확인</a></li>
						<li class="kiosk_num_area" style="display:none;">
							<div class="authNoArea" style="display:none;">
								<span id="authNo">${authNoYmd.AUTH_NO}</span>
								<span id="authTxt" class="inline_block">인증번호 유효시간</span>
								<span id="countdown" class="inline_block"></span>
							</div>
							<button class="btn-re-auth" style="display:${empty authNoYmd.AUTH_YMD or (!empty authNoYmd.AUTH_YMD and authNoYmd.AUTH_YMD < today) ? 'block' : 'none'}">발급하기</button>
						</li>
					</c:if>
				</sec:authorize>
			</ul>
		</div>
	</form>
</div>
