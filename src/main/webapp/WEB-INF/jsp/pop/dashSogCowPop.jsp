<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="winpop board-main page-board-01">
	<div class="inner">
		<div class="winpop-head">
			<button type="button" class="winpop_close ta-R"><span class="sr-only">윈도우 팝업 닫기</span></button>
			<h2 class="winpop_tit">출장우 현황 I ${inputParam.searchPlaceNm}</h2>
		</div>
		<div class="sec-board">
			<div class="sort-tit-area">
				<span class="sort1">${searchMonTxt} 기준</span>
				<div><strong class="sort2">${inputParam.aucObjDscNm}</strong><span class="sort3">${inputParam.monthOldCNm}</span></div>
			</div>
			<div class="white-box">
				<div class="cow-board-top v2">
					<strong class="tot_cow_cnt"><fmt:formatNumber value="${sogCowInfo.TOT_SOG_CNT}" type="number" /> 두</strong>
					<c:choose>
						<c:when test="${sogCowInfo.ASC_SOG_CNT > 0 }">
							<span class="tot_sCow_chg_txt fc-red">
								▲ <fmt:formatNumber value="${sogCowInfo.ASC_SOG_CNT}" type="number" /> 두
							</span>
						</c:when>
						<c:when test="${sogCowInfo.ASC_SOG_CNT < 0 }">
							<span class="tot_sCow_chg_txt fc-blue">
								▼ <fmt:formatNumber value="${sogCowInfo.ASC_SOG_CNT}" type="number" /> 두
							</span>
						</c:when>
						<c:otherwise>
							<span class="tot_sCow_chg_txt fc-blue">
								- <fmt:formatNumber value="${sogCowInfo.ASC_SOG_CNT}" type="number" /> 두
							</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="sec-board">
			<h2 class="sec-tit">월별 출장우 현황</h2>
			<div class="white-box">
				<canvas id="myCharSample1"></canvas>
			</div>
		</div>
		<div class="bottom-btn">
			<button type="button" class="winpop_close btn-close-winpop">닫기</button>
		</div>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
var labelData = [];
var preCntData = [];	// 전년도 출장우
var thisCntData = [];	// 올해 출장우

const ctx1 = document.getElementById('myCharSample1');

<c:forEach items="${monSogCowList}" var="vo">
	var month = "${vo.MONTH}";
	var preCowCnt = "${vo.PRE_COW_CNT}";
	var thisCowCnt = "${vo.THIS_COW_CNT}";
	
	labelData.push(month ?? '없음');
	preCntData.push(preCowCnt);
	thisCntData.push(thisCowCnt);
</c:forEach>

new Chart(ctx1, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '전년도 출장우',
				data: preCntData,
				borderColor: '#37a2eb',
				background:'',
				type: 'line',
				order: 0
			},
			{
				label: '출장우',
				data: thisCntData,
				borderColor: '#ffb1c1',
				backgroundColor: '#ffb1c1',
				type: 'bar',
				borderWidth: 0,
				order: 1
			}
		]
	},
	options: {
		responsive: true,
		plugins: {
			legend: {
				position: 'top',
			},
		},
		scales: {
			yAxes: [
				{
					id: 'y-left',
					position: 'left',
					display: true,
					ticks: {
						beginAtZero: true,
						callback: function(value, index) {
							return parseInt(value, 10).toLocaleString();
						}
					}
				}
			]
			, xAxes : [{
				ticks : {
					fontSize : 9
				}
			}]
		},
		tooltips: {
			enabled: true,
			callbacks: {
				label: function(tooltipItem, data) {
					const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
					return parseInt(tooltipValue, 10).toLocaleString();
				}
			}
		},
		legend: {
			labels:{
				fontSize : 10
			}
		}
	}
});
</script>
