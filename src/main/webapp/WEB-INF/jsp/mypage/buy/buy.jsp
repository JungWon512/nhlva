<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<form name="frm" action="" method="post">
	<input type="hidden" id="loginNo" 		name="loginNo" 		value="${inputParam.loginNo}"/>
	<input type="hidden" id="place" 		name="place"			value="${johapData.NA_BZPLCNO}"/>
	<input type="hidden" id="naBzPlcNo" 	name="naBzPlcNo"	value="${johapData.NA_BZPLCNO}"/>
	<input type="hidden" id="johpCd" 		name="johpCd"		value="${johapData.NA_BZPLC}"/>
	<input type="hidden" id="stateFlag"   name="stateFlag"		value="${inputParam.stateFlag}"/>
</form>

<div class="auction_list">
	<h3>나의 경매내역</h3>
	<div class="tab_list item-4">
		<ul class="buy_${tabList[0].VISIB_YN eq '1' ? '4' : '3'}">
			<li><a href="javascript:;" class="${param.tabId eq 'buy' or empty param.tabId ? 'act':'' }" id="buy" data-tab-id="buy">구매내역</a></li>
			<li><a href="javascript:;" id="bid" data-tab-id="bid">응찰내역</a></li>
			<li><a href="javascript:;" id="state" data-tab-id="state">정산서</a></li>
			<!-- 현재 정보제공 관리 항목에서 my현황만 관리하고 있으므로 하나만 체크 -->
			<c:if test="${tabList[0].VISIB_YN eq '1'}">
				<li><a href="javascript:;" id="myMenu" data-tab-id="myMenu">My 현황</a></li>
			</c:if>
		</ul>
	</div>
	
	<div class="tab_area auction_list buy_list buy" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/buy/buy_tab_0.jsp" %>
	</div>
	
	<div class="tab_area bid" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/buy/buy_tab_1.jsp" %>
	</div>
	
	<div class="tab_area state" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/buy/buy_tab_2.jsp" %>
	</div>
	
	<div class="tab_area myMenu" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/buy/buy_tab_3.jsp" %>
	</div>
</div>
