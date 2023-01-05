<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<input type="hidden" id="loginNo" 		name="loginNo" 			value="${inputParam.loginNo}"/>
<input type="hidden" id="stateFlag" 	name="stateFlag"			value="${inputParam.stateFlag}"/>
<input type="hidden" id="naBzPlcNo"	name="naBzPlcNo"	 	value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" id="johpCd" 		name="johpCd"			value="${johapData.NA_BZPLC}"/>
<div class="auction_list">
	<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
	<h3>나의 출장우</h3>
	<div class="tab_list">
		<ul class="entry_${tabList[0].VISIB_YN eq '1' ? '3' : '2'}">
			<li><a href="javascript:;" id="entry" class="act" data-tab-id="entry">출장우</a></li>
			<li><a href="javascript:;" id="state" data-tab-id="state">정산서</a></li>
			<!-- 현재 정보제공 관리 항목에서 my현황만 관리하고 있으므로 하나만 체크 -->
			<c:if test="${tabList[0].VISIB_YN eq '1'}">
				<li><a href="javascript:;" id="myMenu" data-tab-id="myMenu">My 현황</a></li>
			</c:if>
		</ul>
	</div>
	
	<div class="tab_area entry" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/entry/entry_tab_0.jsp" %>
	</div>
	
	<div class="tab_area state" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/entry/entry_tab_1.jsp" %>
	</div>
	
	<div class="tab_area myMenu" style="display:none;">
		<%@ include file="/WEB-INF/jsp/mypage/entry/entry_tab_2.jsp" %>
	</div>
</div>
<!-- //auction_list e -->
<script src="/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
var doughChart;
var barChart;
</script>
