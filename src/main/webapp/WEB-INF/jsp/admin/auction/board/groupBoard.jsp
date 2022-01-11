<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->

<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
<input type="hidden" id="userId" value="${userId}" />

<section class="billboard-info" style="display:block;">
	<table class="bill-table">
		<colgroup>
			<col width="25%">
			<col width="25%">
			<col width="25%">
			<col width="25%">
		</colgroup>
		<tbody>
			<tr>
				<td colspan="4" class="h-190">
					<span class="txt-name fz100">${johapData.CLNTNM}</span>
					<span class="txt-orange fz100 time">00:00</span>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="bg-gray">
					<p class="fz100 infoTxt"><span class="txt-green">경매 대기 중</span></p>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<span class="txt-green fz80">총 출장우 </span>
					<span class="fz100 txt-bold">${cowTotCnt.CNT }</span>
				</td>
			</tr>
			<tr>
				<td>
					<p class="txt-green fz60">암송아지</p>
					<p class="fz100 txt-bold">${cowTotCnt.CNT_CALF_SEX_W }</p>
				</td>
				<td>
					<p class="txt-green fz60">수송아지</p>
					<p class="fz100 txt-bold">${cowTotCnt.CNT_CALF_SEX_M }</p>
				</td>				
				<td>
					<p class="txt-green fz60">비육우</p>
					<p class="fz100 txt-bold">${cowTotCnt.CNT_NO_COW }</p>
				</td>
				<td>
					<p class="txt-green fz60">번식우</p>
					<p class="fz100 txt-bold">${cowTotCnt.CNT_COW }</p>
				</td>
			</tr>
		</tbody>
	</table>
</section>

<section class="billboard-noBid" style="display:none;">
	<h1 class="h-190">
		<span class="txt-orange fz100">미응찰 출장우</span>
	</h1>
	<div class="list-body">
		<div class="mCustomScrollBox">
			<ul class="cow-number-list">
<!-- 				<li><div class="bg bg-gray"><span class="fz120 txt-bold">9</span></div></li> -->
<!-- 				<li><div class="bg"></div></li> -->
			</ul>
		</div>
	</div>
</section>

<section class="billboard-view" style="display:none;">
	<div class="auction_list_full tblAuction">
		<div class="list_table_full">
			<div class="list_head">
				<dl>
					<dd class="pd_name">${johapData.CLNTNM}</dd>
					<dd class="pd_txt">일괄경매 <span class="space">|</span> 출장우 : ${cowTotCnt.CNT }두</dd>
				</dl>
				<dl>
					<dd class="num">번호</dd>
					<dd class="name">출하주</dd>
					<dd class="pd_ea">개체번호</dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량(kg)</dd>
					<dd class="pd_kpn">KPN</dd>
					<dd class="pd_pay1">최저가</dd>
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_state">결과</dd>
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
								<dd class="num">${ vo.AUC_PRG_SQ }</dd>
								<dd class="name">${ vo.FTSNM }</dd>
								<dd class="pd_ea">${ vo.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${ vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg"> ${(vo.COW_SOG_WT eq '' || vo.COW_SOG_WT == null || vo.COW_SOG_WT <= 0 ) ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]} </dd>
								<dd class="pd_kpn">${ vo.KPN_NO_STR }</dd>
								<dd class="pd_pay1">${(vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0 ) ? '-' : vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${(vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0 ) ? '-' : vo.SRA_SBID_UPR <= 0 ? '0' : fn:split(vo.SRA_SBID_UPR,'.')[0]}</dd>
								<dd class="pd_state">${ vo.SEL_STS_DSC_NAME }</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- //auction_result e -->
	</div>
	<!-- //auction_list e -->
</section>