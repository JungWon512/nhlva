<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<div class="auction_choice">
	<h3>실시간경매</h3>
	<dl>
		<!--<dt><span>${fn:substring(johapData.CLNTNM,0,8) }</span></dt>-->
		<dt class="tit"><span>${johapData.CLNTNM}</span></dt>
		<dd>${aucDate[0].AUC_MONTH }월 ${aucDate[0].AUC_DAY }일(${aucDate[0].AUC_WEEK_DAY }) ${fn:replace(aucDate[0].AUC_OBJ_DSC_NAME,'일괄','번식우,송아지,비육우') } 경매</dd>
		<c:if test="${! empty bizList.get(0).QCN_MSG }">
			<dd>${bizList.get(0).QCN_MSG}</dd>
		</c:if>
		<dd class="link"><a href="javascript:pageMove('/home');"><span class="ico_arrowblue">지역선택</span></a></dd>
	</dl>
	<ul class="auction_btn">
		<c:if test="${!empty entryList}">
			<li><h1 style="font-weight: bold;font-size: 24px;text-align: center;">참가번호 : ${entryList[0].LVST_AUC_PTC_MN_NO} 번</h1></li>
		</c:if>
		<li>
			<sec:authorize access="hasRole('ROLE_BIDDER')">
				<a href="javascript:goAuctionApp();" class="auction_join">경매 응찰</a>
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_BIDDER')">
				<a href="javascript:goLoginPage();" class="auction_join">경매 응찰</a>
			</sec:authorize>
		</li>
		<li>
			<div class="btn2">	
				<a href="javascript:location.href='/sales'+location.search;" class="auction_see">출장우</a>
				<a href="javascript:goWatchApp();" class="auction_see">경매 관전</a>
			</div>
		</li>
		<c:if test="${johapData.KIOSK_YN eq '1'}">
		<sec:authorize access="hasAnyRole('ROLE_BIDDER', 'ROLE_FARM')">
			<li>
				<a class="accordion kiosk_btn">
					<span class="kioskTxt">키오스크 인증번호 확인</span>
					<img src="/static/images/guide/ico_move.png" class="kiosk_toggle_btn" />
					<div class="kiosk_num_area" style="display:none;padding-bottom:15px;">
						<div class="authNoArea" style="display:none;">
							<span id="authNo">${authNoYmd.AUTH_NO}</span>
							<p class="auth_text_line">
								<span id="authTxt" class="inline_block">인증번호 유효시간</span>
								<span id="countdown" class="inline_block"></span>
							</p>
						</div>
						<sec:authorize access="!isAnonymous()">
							<button class="btn-re-auth auth_num_issue" style="display:${empty authNoYmd.AUTH_YMD or (!empty authNoYmd.AUTH_YMD and authNoYmd.AUTH_YMD < today) ? 'block' : 'none'}">발급하기</button>
						</sec:authorize>
						<sec:authorize access="isAnonymous()">
							<button class="btn-re-auth login_confirm">발급하기</button>
						</sec:authorize>
					</div>
				</a>
				<form name="frm_info">
					<input type="hidden" name="place" value="${naBzPlcNo}" />
			   		<input type="hidden" name="auth_no" value="${authNoYmd.AUTH_NO}" />
			   		<input type="hidden" name="auth_ymd" value="${authNoYmd.AUTH_YMD}" />
				</form>
			</li>
		</sec:authorize>
		</c:if>
	</ul>
	
	<div class="login_area ">
		<div class="login_bottom">
			<p class="login_info">${johapData.CLNTNM} 이용문의</p>
			<p class="cs_phone">${johapData.TEL_NO }</p>
			<p class="cs_clock">월-금 9:00~18:00<br>(점심 12:00~13:00)</p>
		</div>
	</div>
</div>
<!-- //auction_choice e -->