<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="/static/js/socket.io/socket.io.js"></script>
<style type="text/css">
	.contents {
		padding: 5px 0 !important;
	}
</style>

<div class="auction_list">
	<input type="hidden" id="token" value="${watchToken }"/>
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="aucDsc" value="${johapData.AUC_DSC}" />
	<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
	
	<input type="hidden" id="aucDate" value="${dateVo.AUC_DT}" />

	<!-- //auction_seeBox e -->
	<div class="list_table auction_see tblAuction">
		<div class="list_head" style="${johapData.AUC_DSC eq '2'?'border-radius: 0px;':'' }">
			<dl>
				<dd class="date">경매일자</dd>
				<dd class="num"><span class="w_view_in">경매</span>번호</dd>
				<dd class="name">출하주</dd>
				<dd class="pd_ea">개체</dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
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
				<c:if test="${ watchList.size() <= 0 }">
					<li>
						<dl>
							<dd>경매관전 자료가 없습니다.</dd>
						</dl>
					</li>
				</c:if>
				<c:forEach items="${ watchList }" var="vo" varStatus="st">
					<li class="${st.index == 0?'act':'' }">
						<input type='hidden' name="mcowDsc" class="mcowDsc" value="${ vo.MCOW_DSC_NAME }"/>
						<input type='hidden' name="matime" class="matime" value="${ vo.MATIME }"/>
						<dl>
							<dd class="date aucDt">${ vo.AUC_DT_STR }</dd>
							<dd class="num aucPrgSq">${ vo.AUC_PRG_SQ }</dd>
							<dd class="name ftsnm">${ vo.FTSNM }</dd>
							<dd class="pd_ea sraIndvAmnno">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
							<dd class="pd_sex indvSexC">${ vo.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg cowSogWt textNumber">
								<fmt:formatNumber value="${(empty vo.COW_SOG_WT or vo.COW_SOG_WT <= 0 ) ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]}" type="number" />
							</dd>
							<dd class="pd_kpn kpnNo">${ vo.KPN_NO_STR }</dd>
							<dd class="pd_num1 sraIndvPasgQcn">${ vo.SRA_INDV_PASG_QCN }</dd>
							<dd class="pd_pay1 lowsSbidLmtAm textNumber">
								<c:choose>
									<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_pay2 sraSbidAm textNumber">
								<c:choose>
									<c:when test="${vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_state selSts">${vo.SEL_STS_DSC_NAME }</dd>
							<dd class="pd_etc rmkCntn">${ fn:replace(vo.RMK_CNTN,'|',',') }</dd>
						</dl>
					</li>
				</c:forEach>				
			</ul>
		</div>
	</div>
	<!-- //auction_see e -->
</div>
<!-- //auction_list e -->