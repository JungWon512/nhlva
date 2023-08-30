<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="/static/css/guide/style-dashboard.css">

<!-- 현재날짜 처리 -->
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMM" var="nowMonth" />

<form name="frm" action="" method="post">
	<input type="hidden" name="searchMonth" 	id="searchMonth" 		value="${inputParam.searchMonth}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
	<input type="hidden" name="nowMonth" 		id="nowMonth" 			value="${nowMonth}" />
	<input type="hidden" name="sbid_per" id="sbid_per" value="${bidderInfo.SBID_PER}"/>
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축시장 낙찰현황<p class="annotation">(<span class="cntNaBzplc"><fmt:formatNumber value="${empty bidderInfo ? 0 : bidderInfo.CNT_NABZPLC}" type="number" /></span>개 조합)</p></h2>
		</div>
		<div class="dark-box calendar-box sty-round">
			<div class="c-months">
				<div class="has-btn">
					<button class="btn-prev btn-chg"><span class="sr-only">이전</span></button>
						<span class="month_txt_title">${searchMonTxt }</span>
<!-- 					<button class="btn-next btn-chg"><span class="sr-only">다음</span></button> -->
					<button class="btn-next btn-chg" <c:if test="${nowMonth eq inputParam.searchMonth}">style="display:none;"</c:if>><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="white-box border-dot">
			<div class="gap-area">
				<div class="gap-box">
					<em>총 출장두수</em>
					<strong class="tot_sog_cnt"><fmt:formatNumber value="${empty bidderInfo ? 0 : bidderInfo.TOT_SOG_CNT}" type="number" /> 두</strong>
				</div>
				<div>
					<span class="gap ico-red down bid_minus_cnt">- <fmt:formatNumber value="${bidderInfo.TOT_SOG_CNT - bidderInfo.TOT_SBID_CNT}" type="number" /></span>
				</div>
				<div class="gap-box">
					<em>낙찰두수</em>
					<strong class="tot_sbid_cnt"><fmt:formatNumber value="${empty bidderInfo ? 0 : bidderInfo.TOT_SBID_CNT}" type="number" /> 두</strong>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="doughnut">
				<div class="graph">
					<div class="chart_area">
						<canvas id="myDoughnutSample1"></canvas>
					</div>
					<span class="total douhnut_txt">전체<br><fmt:formatNumber value="${empty bidderInfo ? 0 : bidderInfo.TOT_SOG_CNT}" type="number" />두</span>
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
					<col width="15%">					
					<col width="25%">
					<col width="25%">
					<col width="20%">
					<col width="15%">
				</colgroup>
				<thead>
					<tr>
						<th scope="col">지역</th>
						<th scope="col">구분</th>
						<th scope="col">낙찰두수</th>
						<th scope="col">유찰두수</th>
						<th scope="col">낙찰율</th>
					</tr>
				</thead>
				<tbody id="bidder_list" class="bdr">
	               	<c:if test="${ bidderPerList.size() <= 0 }">
						<tr><td colspan='5' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>
	               	</c:if>					
					<c:forEach items="${bidderPerList}" var="perVo">
						<tr class="${perVo.NUM_NABZPLC eq '1'?'total':''}">
							<c:if test="${perVo.NUM_NABZPLC eq '1'}">
								<td rowspan="${perVo.CNT_NABZPLC}">${perVo.NA_BZPLCLOC_NM}</td>
							</c:if>
							<td>${perVo.GUBUN}</td>
							<td><fmt:formatNumber value="${perVo.TOT_SBID_CNT}" type="number" /></td>
							<td><fmt:formatNumber value="${perVo.MINUS_SOG_BID}" type="number" /></td>
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
	options: {
		tooltips: {
			callbacks: {
				label : function(tooltipItem, data){
					var dataset = data.datasets[tooltipItem.datasetIndex];
					var currentValue = dataset.data[tooltipItem.index];
					return Math.floor(((currentValue / 100) * 100)) + "%";
				}
			}
		}
	}
});
</script>
