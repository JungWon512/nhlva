<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->
<section class="billboard-view bidInfo">
	<input type="hidden" id="naBzPlc" value="${johapData.NA_BZPLC}" />
	<input type="hidden" id="webPort" value="${johapData.WEB_PORT}" />
	<input type="hidden" id="userId" value="${userId}" />
	<table class="bill-table countVer tblBoard">
		<colgroup>
			<col width="50%">
			<col width="50%">
		</colgroup>
		<tbody>
			<tr>
				<td colspan="2" class="brandTit" style="padding: 0 0 0 2%;"><p>${johapData.CLNTNM}</p></td> 
			</tr>
			<tr>
				<td class="secTxt"><p>경매번호</p></td>
				<td class="secTxt"><p>낙찰자</p></td>
			</tr>
			<tr>
				<td class="countTxt auctionNum"><p>-</p></td>
				<td class="countTxt bidUser"><p>-</p></td>
			</tr>
		</tbody>
	</table>
</section>