<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->

<section class="billboard-view">
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
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
				<td colspan="4" class="complate">
					<p class="txt-green fz80">낙찰번호</p>
					<p class="fz150 bidUser">-</p>
				</td>
			</tr>
		</tbody>
	</table>
</section>