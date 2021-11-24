<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->

<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
<input type="hidden" id="userId" value="${userId}" />

<div id="wrap">
	<div class="auction_list_full tblAuction">
		<div class="list_table_full">
			<div class="list_head">
				<dl>
					<dd class="pd_name">${johapData.CLNTNM}</dd>
					<dd class="pd_txt">일괄경매 <span class="space">|</span> 출장우 : ${cowTotCnt.CNT }두</dd>
				</dl>
				<dl>
					<dd class="date">경매일자</dd>
					<dd class="num">경매번호</dd>
					<dd class="name">출하주</dd>
					<dd class="pd_ea">개체</dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량(kg)</dd>
					<dd class="pd_kpn">KPN</dd>
					<dd class="pd_num1">계대</dd>
					<dd class="pd_pay1">최저가</dd>
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_state">경매상태</dd>
					<dd class="pd_etc">비고</dd>
				</dl>
			</div>
			<div class="list_body">
				<ul class="mCustomScrollBox">
	               	<c:if test="${ list.size() <= 0 }">
						<li>
							<dl>
								<dd>경매관전 자료가 없습니다.</dd>
							</dl>
						</li>
	               	</c:if>
	               	<c:forEach items="${ list }" var="vo" varStatus="st">
						<li>
							<dl>
								<dd class="date">${ vo.AUC_DT_STR }</dd>
								<dd class="num">${ vo.AUC_PRG_SQ }</dd>
								<dd class="name">${ vo.SRA_PDMNM }</dd>
								<dd class="pd_ea">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${ vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg"> ${(vo.COW_SOG_WT eq '' || vo.COW_SOG_WT == null || vo.COW_SOG_WT <= 0 ) ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]} </dd>
								<dd class="pd_kpn">${ vo.KPN_NO_STR }</dd>
								<dd class="pd_num1">${ vo.SRA_INDV_PASG_QCN }</dd>
								<dd class="pd_pay1">${(vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0 ) ? '-' : vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${(vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0 ) ? '-' : vo.SRA_SBID_UPR <= 0 ? '0' : fn:split(vo.SRA_SBID_UPR,'.')[0]}</dd>
								<dd class="pd_state">${ vo.SEL_STS_DSC_NAME }</dd>
								<dd class="pd_etc">${ fn:replace(vo.RMK_CNTN,'|',',') }</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- //auction_result e -->
	</div>
	<!-- //auction_list e -->
</div>