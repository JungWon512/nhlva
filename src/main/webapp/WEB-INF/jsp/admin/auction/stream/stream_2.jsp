<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="https://webrtc.github.io/adapter/adapter-latest.js"></script>
<script src="/static/js/socket.io/socket.io.js"></script>

<input type="hidden" id="token" value="${token }"/>
<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="aucDsc" value="${johapData.AUC_DSC}" />
<input type="hidden" id="kkoSvcId" value="${johapData.KKO_SVC_ID}" />
<input type="hidden" id="kkoSvcKey" value="${johapData.KKO_SVC_KEY}" />
<input type="hidden" id="kkoSvcCnt" value="${johapData.KKO_SVC_CNT}" />
<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
<input type="hidden" id="listAucNum" value="${count.LIST_AUCNUM}" />
<table class="youtube-table tblAuctionSt">
	<colgroup>
		<col width="17.5%">
		<col width="17.5%">
		<col width="17.5%">
		<col width="17.5%">
		<col width="15%">
		<col width="15%">
	</colgroup>
	<tbody>
		<tr>
			<td class="seller">
				<p class="fz40">${johapData.CLNTNM}</p>
				<p class="fz96 auctionNum"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">최저금액</p>
				<p class="fz64 lowsSbidLmtAm"> - </p>
			</td>
			<td class="complate">
				<p class="txt-green fz40">낙찰금액</p>
				<p class="fz64 tdBiddAmt"> - </p>
			</td>
			<td class="complate">
				<p class="txt-green fz40">낙찰번호</p>
				<p class="fz64 tdBiddNum">-</p>
			</td>
			<td class="auctionTxt" colspan="2">
				<!-- <p class="txt-white fz96">유찰입니다</p> -->
				<!-- <p class="txt-green fz96">경매 대기중</p> -->
				<p class="txt-green fz96">경매 대기중</p>
			</td>
		</tr>
		<tr>
			<td colspan="4" rowspan="4" class="td_video">
				<!--<div class="bg_yotube">
					<iframe width="100%" height="100%" src="https://www.youtube.com/embed/kTHYB632Uws?autoplay=1" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
				</div>-->
				<div class="">
				</div>
			</td>
			<td>
				<p class="txt-green fz40">지역</p>
				<p class="fz64 loc"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">출하주</p>
				<p class="fz64 ftsnm"> - </p>
			</td>
		</tr>
		<tr>
			<td>
				<p class="txt-green fz40">성별</p>
				<p class="fz64 sex"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">어미</p>
				<p class="fz64 mcowDsc"> - </p>
			</td>
		</tr>
		<tr>
			<td>
				<p class="txt-green fz40">중량(Kg)</p>
				<p class="fz64 cowSogWt"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">계대</p>
				<p class="fz64 sraIndvPasgQcn"> - </p>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<p class="fz64 move-txt rmkCntn" style="width:100%"> - </p>
			</td>
		</tr>
	</tbody>
</table>