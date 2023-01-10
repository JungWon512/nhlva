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
		<col width="16%">
		<col width="16%">
		<col width="16%">
		<col width="16%">
		<col width="16%">
		<col width="16%">
	</colgroup>
	<tbody>
		<tr class="st">
			<td class="bg-red val">
				<p class="fz40">${johapData.CLNTNM}</p>
				<p class="fz60 auctionNum"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">최저금액</p>
				<p class="fz60 lowsSbidLmtAm"> - </p>
			</td>
			<td class="complate">
				<p class="txt-green fz40">낙찰금액</p>
				<p class="fz60 tdBiddAmt"> - </p>
			</td>
			<td class="complate">
				<p class="txt-green fz40">낙찰번호</p>
				<p class="fz60 tdBiddNum"> - </p>
			</td>
			<td class="count-td" colspan='2'>
				<p class="txt-count">경매 대기중</p>
			</td>
		</tr>
		<tr>
			<td colspan='4' rowspan='6' style="background: transparent;">
			</td>
		</tr>
		<tr>
			<td>
				<p class="txt-green fz40">지역</p>
				<p class="fz60 loc"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">출하주</p>
				<p class="fz60 ftsnm"> - </p>
			</td>
		</tr>
		<tr>
			<td>
				<p class="txt-green fz40">성별</p>
				<p class="fz60 sex"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">어미</p>
				<p class="fz60 mcowDsc"> - </p>
			</td>
		</tr>
		<tr>
			<td>
				<p class="txt-green fz40">중량</p>
				<p class="fz60 cowSogWt"> - </p>
			</td>
			<td>
				<p class="txt-green fz40">계대</p>
				<p class="fz60 sraIndvPasgQcn"> - </p>
			</td>
		</tr>
		<tr>
			<td colspan='2'>
				<p class="txt-green fz60 move-txt rmkCntn" style="width:80%"> - </p>
			</td>
		</tr>
		<tr>
			<td colspan='2'>
				<p class="fz40 footerDate" style="font-size: 25px;font-weight: 200;letter-spacing: -1px;"> 
					${johapData.CLNTNM}
					<span class="txt-orange date" style="padding: 0 10px 0 10px;">00월 00일</span>
					<span class="txt-orange time">00:00</span>
				</p>
			</td>
		</tr>
	</tbody>
</table>