<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<!-- Page Content -->
<style>
	@font-face {
		font-family: 'LAB디지털';
		src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/LAB디지털.woff') format('woff');
		font-weight: normal;
		font-style: normal;
	}
	.billboard-view .countVer{color: #fff;border: 10px solid #000;border-bottom: 0px;background-color: #1b1b1b;}
	.billboard-view .countVer p{font-weight: normal;text-align: center;line-height: normal;}
	.billboard-view .countVer td{padding: 2% 2.5%;border: none;border-bottom: 10px solid #000;box-sizing: border-box;}
	.billboard-view .countVer td + td{border-left: 10px solid #000;}
	.billboard-view .countVer .brandTit{height: 10%;}
	.billboard-view .countVer .brandTit p{text-align: left;font-size: 100px;letter-spacing: -5px;}
	.billboard-view .countVer .secTxt{height: 34%;background-color: #3b3b3b;}
	.billboard-view .countVer .secTxt p{font-size: 150px;letter-spacing: -7.5px;}
	.billboard-view .countVer .countTxt{height: 56%;padding:  0px;}
	.billboard-view .countVer .countTxt p{font-size: 500px;color: #ffd500;font-family: 'LAB디지털';letter-spacing: 50px;}
</style>

<section class="billboard-view">
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
				<td colspan="2" class="brandTit"><p>${johapData.CLNTNM}</p></td>
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