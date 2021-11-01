<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->

<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
<input type="hidden" id="userId" value="${userId}" />
<table class="bill-table tblBoard">
	<colgroup>
		<col width="20%">
		<col width="20%">
		<col width="20%">
		<col width="20%">
		<col width="20%">
	</colgroup>
	<tbody>
		<tr class="title">
			<td class="bg-blue"><p class="fz180 auctionNum">000</p></td>
			<td colspan="2" class="complate" style="display:none;">
				<p class="txt-green fz80">낙찰금액</p>
				<p class="fz150 bidPrice">-</p>
			</td>
			<td colspan="2" class="complate" style="display:none;">
				<p class="txt-green fz80">낙찰번호</p>
				<p class="fz150 bidUser">-</p>
			</td>
			<td colspan="4" class="count-td">
				<p class="txt-count boardTitle">경매대기중</p>
			</td>
		</tr>
		<tr class="val">
			<td>
				<p class="txt-green fz60">출하주</p>
				<p class="fz100 ftsnm">-</p>
			</td>
			<td>
				<p class="txt-green fz60">성별</p>
				<p class="fz100 sex">-</p>
			</td>
			<td>
				<p class="txt-green fz60">중량(Kg)</p>
				<p class="fz100 cowSogWt">-</p>
			</td>
			<td>
				<p class="txt-green fz60">어미</p>
				<p class="fz100 mcowDsc">-</p>
			</td>
			<td>
				<p class="txt-green fz60">KPN</p>
				<p class="fz100 kpnNo">-</p>
			</td>
		</tr>
		<tr class="val">
			<td>
				<p class="txt-green fz60">계대</p>
				<p class="fz100 sraIndvPasgQcn">-</p>
			</td>
			<td>
				<p class="txt-green fz60">산차</p>
				<p class="fz100 matime">-</p>
			</td>
			<td>
				<p class="txt-green fz60">최저가</p>
				<p class="fz100 lowsSbidLmtAm">-</p>
			</td>
			<td colspan="2">
				<p class="txt-green fz60">비고</p>
				<p class="fz100 move-txt rmkCntn">-</p>
				<!-- width 값에 따라 속도 조정 -->
			</td>
		</tr>
	</tbody>
</table>