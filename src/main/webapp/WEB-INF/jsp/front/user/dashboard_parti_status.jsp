<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="searchMonth" 	id="searchMonth" 		value="${inputParam.searchMonth}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
	<input type="hidden" name="sbid_per" id="sbid_per" value="${bidderInfo.SBID_PER}"/>
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="dark-box calendar-box sty-round">
			<div class="c-months">
				<div class="has-btn">
					<button class="btn-prev btn-chg"><span class="sr-only">이전</span></button>
						<span class="month_txt_title">${searchMonTxt }</span>
					<button class="btn-next btn-chg"><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="white-box border-dot">
			<div class="gap-area">
				<div class="gap-box">
					<em>총 출장두수</em>
					<strong class="tot_sog_cnt">${empty bidderInfo ? 0 : bidderInfo.TOT_SOG_CNT} 두</strong>
				</div>
				<div>
					<span class="gap ico-red up bid_minus_cnt">+ ${bidderInfo.TOT_SOG_CNT - bidderInfo.TOT_SBID_CNT}</span>
					<!-- <span class="gap ico-red down">- 999</span> -->
				</div>
				<div class="gap-box">
					<em>낙찰두수</em>
					<strong class="tot_sbid_cnt">${empty bidderInfo ? 0 : bidderInfo.TOT_SBID_CNT} 두</strong>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="doughnut">
				<div class="graph">
					<canvas id="myDoughnutSample1"></canvas>
					<span class="total douhnut_txt">전체<br>${empty bidderInfo ? 0 : bidderInfo.TOT_SOG_CNT}두</span>
				</div>
				<div class="txt">
					낙찰율
					<strong class="fc-red sbid_per_red">${empty bidderInfo ? 0 : bidderInfo.SBID_PER} %</strong>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit">가축시장 낙찰율 현황 <span class="s-txt d-block mon_txt_block">${searchMonTxt} 기준</span></h2>
		<div class="table-simple">
			<table>
				<colgroup>
					<col width="25%">
					<col width="26%">
					<col width="35%">
					<col>
				</colgroup>
				<thead>
					<tr>
						<th scope="col">지역</th>
						<th scope="col">낙찰두수</th>
						<th scope="col">유찰두수</th>
						<th scope="col">낙찰율</th>
					</tr>
				</thead>
				<tbody id="bidder_list">
					<c:forEach items="${bidderPerList}" var="perVo">
						<tr>
							<td>${perVo.NA_BZPLCLOC_NM}</td>
							<td>${perVo.TOT_SBID_CNT}</td>
							<td>${perVo.MINUS_SOG_BID}</td>
							<td>${perVo.SBID_PER}%</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
const ctx3 = document.getElementById('myDoughnutSample1');
var sbid_per = document.getElementById('sbid_per').value;
var non_bid_per = 100 - sbid_per;

new Chart(ctx3, {
	type: 'doughnut',
	data: {
		datasets: [{
			data: [non_bid_per, sbid_per],
			backgroundColor: ['#cac3fa', '#8b7feb'],
			borderWidth: 0
		}]
	},
});
</script>
