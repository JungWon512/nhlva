<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<div class="auction_choice">
	<h3>실시간경매</h3>
	<dl>
		<!--<dt><span>${fn:substring(johapData.CLNTNM,0,8) }</span></dt>-->
		<dt class="tit"><span>${johapData.CLNTNM}</span></dt>
		<dd>${aucDate[0].AUC_MONTH }월 ${aucDate[0].AUC_DAY }일(${aucDate[0].AUC_WEEK_DAY }) ${aucDate[0].AUC_OBJ_DSC_NAME } 경매</dd>
		<dd class="link"><a href="javascript:pageMove('/home');"><span class="ico_arrowblue">지역선택</span></a></dd>
	</dl>
	<ul class="auction_btn">
		<c:if test="${!empty entryList}">
			<li>
				<h1 style="font-weight: bold;font-size: 24px;text-align: center;">참가번호 : ${entryList[0].LVST_AUC_PTC_MN_NO} 번</h1></li>
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
			<a href="javascript:goWatchApp();" class="auction_see">경매 관전</a>
		</li>
	</ul>
<!-- 	<div class="app_down"> -->
<!-- 		<a href="javascript:;"><span class="ico_aos">AOS 앱설치</span></a> -->
<!-- 		<a href="javascript:;"><span class="ico_ios">iOS 앱설치</span></a> -->
<!-- 	</div> -->
	<div class="login_area ">
		<div class="login_bottom">
			<p class="login_info">${johapData.CLNTNM} 이용문의</p>
			<p class="cs_phone">${johapData.TEL_NO }</p>
			<p class="cs_clock">월-토 9:00~18:00<br>(점심 12:00~13:00)</p>
		</div>
	</div>
</div>
<!-- //auction_choice e -->