<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<input type="hidden" id="loginNo" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" id="johpCd" value="${johapData.NA_BZPLC}"/>
<input type="hidden" id="searchDate" value="${inputParam.date}"/>

<form name="frm_select" action="" method="post">
	<input type="hidden" name="place" value="${param.place}" />
	<input type="hidden" name="naBzplc" value="" />
	<input type="hidden" name="aucDt" value="" />
	<input type="hidden" name="aucObjDsc" value="" />
	<input type="hidden" name="sraIndvAmnno" value="" />
	<input type="hidden" name="aucPrgSq" value="" />
	<input type="hidden" name="ledSqno" value="" />
	<input type="hidden" name="bidPopYn" value="${inputParam.bidPopYn}" />
</form>
<div class="winpop winpop-list">
	<button type="button" class="winpop_close"><span class="sr-only">윈도우 팝업 닫기</span></button>
	<div class="tab_list">
		<ul class="${johapData.AUC_DSC eq '1'?'tab_2':'tab_3' }">
			<c:choose>
				<c:when test="${johapData.AUC_DSC eq '1' }">
					<li><a href="javascript:;" class="${(empty inputParam.tabAct or inputParam.tabAct eq 'sold')?'act':'' } sold" data-tab-id='sold'>낙찰내역</a></li>
					<li><a href="javascript:;" class="auc ${(inputParam.tabAct eq 'auc')?'act':'' }" data-tab-id='auc'>출장우조회</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="javascript:;" class="${(empty inputParam.tabAct or inputParam.tabAct eq 'bid')?'act':'' } bid" data-tab-id='bid'>응찰내역</a></li>
					<li><a href="javascript:;" class="${(inputParam.tabAct eq 'sold')?'act':'' } sold" data-tab-id='sold'>낙찰내역</a></li>
					<li><a href="javascript:;" class="auc ${(inputParam.tabAct eq 'auc')?'act':'' }" data-tab-id='auc'>출장우조회</a></li>
				</c:otherwise>				
			</c:choose>			
			
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area auction_list auc" style="display:none;">
	</div>
	
	
	<div class="tab_area auction_list sold" style="display:none;">
	</div>
	
	<div class="tab_area auction_list bid" style="display:none;">
	</div>
</div>