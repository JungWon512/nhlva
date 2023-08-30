<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<link rel="stylesheet" type="text/css" href="/static/css/guide/style-dashboard.css">
<div class="winpop board-main">
	<div class="inner">
		<div class="winpop-head">
			<button type="button" class="winpop_close ta-R"><span class="sr-only">윈도우 팝업 닫기</span></button>
			<h2 class="winpop_tit">낙찰가 현황 I ${empty sbidInfo ? inputParam.searchPlaceNm : sbidInfo.NA_BZPLCLOC_NM }</h2>
		</div>
		<div class="sec-board">
			<p class="annotation">(<span class="cntNaBzplc">${cntNaBzplc}</span>개 조합, 전월대비 <span>증감</span>)</p>
			<div class="sort-tit-area">
				<span class="sort1">${searchMonTxt} 기준</span>
				<div><strong class="sort2">${empty sbidInfo ? inputParam.aucObjDscNm : sbidInfo.AUC_OBJ_DSC_NM}</strong><span class="sort3">${empty sbidInfo.MONTH_OLD_C_NM ? inputParam.monthOldCNm : sbidInfo.MONTH_OLD_C_NM}</span></div>
			</div>
			<div class="white-box chart_area">
				<div class="table-simple m-0">
					<table>
						<colgroup>
							<col>
							<col width="27%">
							<col width="27%">
							<col width="27%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col" class="ta-C">구분</th>
								
								<th scope="col" class="before2Month ta-C">최고</th>
								
								<th scope="col" class="beforeMonth ta-C">최저</th>
								
								<th scope="col" class="searchMonth ta-C">평균</th>
							</tr>
						</thead>
						<tbody class="sogCowInfoList">
							<c:choose>
								<c:when test="${empty sbidList}">
									<c:choose>
										<c:when test="${inputParam.aucObjDsc eq '1' }">										
											<tr class="sogCow">
												<th>합계</th>
												<td class="ta-C">
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C l-spacing">													
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C">	
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
											</tr>
											<tr class="sogCow">
												<th>송아지(수)</th>
												<td class="ta-C">
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C l-spacing">													
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C">	
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
											</tr>
											<tr class="sogCow">
												<th>송아지(암)</th>
												<td class="ta-C">
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C l-spacing">													
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C">	
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr class="sogCow">
												<th>${empty sbidInfo ? inputParam.aucObjDscNm : sbidInfo.AUC_OBJ_DSC_NM}</th>
												<td class="ta-C">
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C l-spacing">													
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
												<td class="ta-C">	
													<span class="won l-spacing d-block tar">0 원</span>											
													<span class="fc-zero max_sbid_chg l-spacing d-block tar">0 원</span>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>										
									<c:forEach items="${sbidList}" var="sbidVo">										
										<tr class="sogCow">
											<th>${sbidVo.AUC_OBJ_DSC_NM } ${sbidVo.INDV_SEX_C }</th>
											<td class="ta-C">
												<c:set var="MAX_SBID_UPR" value="${sbidVo.MAX_SBID_UPR}" />
												<c:set var="MAX_SBID_CHG" value="${sbidVo.MAX_SBID_CHG}" />
												<span class="won l-spacing d-block tar"><fmt:formatNumber value="${MAX_SBID_UPR}" type="number" /> 원</span>											
												<c:choose>
													<c:when test="${MAX_SBID_CHG > 0}">
														<span class="fc-red max_sbid_chg l-spacing d-block tar">+ <fmt:formatNumber value="${MAX_SBID_CHG}" type="number" /> 원</span>	
													</c:when>
													<c:when test="${MAX_SBID_CHG < 0}">
														<span class="fc-blue max_sbid_chg l-spacing d-block tar">- <fmt:formatNumber value="${fn:replace(MAX_SBID_CHG, '-', '')}" type="number" /> 원</span>
													</c:when>
													<c:otherwise>
														<span class="fc-zero max_sbid_chg l-spacing d-block tar"><fmt:formatNumber value="${MAX_SBID_CHG}" type="number" /> 원</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td class="ta-C l-spacing">
												<c:set var="MIN_SBID_UPR" value="${sbidVo.MIN_SBID_UPR}" />
												<c:set var="MIN_SBID_CHG" value="${sbidVo.MIN_SBID_CHG}" />
												<span class="won l-spacing d-block tar"><fmt:formatNumber value="${MIN_SBID_UPR}" type="number" /> 원</span>								
												<c:choose>
													<c:when test="${MIN_SBID_CHG > 0}">
														<span class="fc-red max_sbid_chg l-spacing d-block tar">+ <fmt:formatNumber value="${MIN_SBID_CHG}" type="number" /> 원</span>	
													</c:when>
													<c:when test="${MIN_SBID_CHG < 0}">
														<span class="fc-blue max_sbid_chg l-spacing d-block tar">- <fmt:formatNumber value="${fn:replace(MIN_SBID_CHG, '-', '')}" type="number" /> 원</span>
													</c:when>
													<c:otherwise>
														<span class="fc-zero max_sbid_chg l-spacing d-block tar"><fmt:formatNumber value="${MIN_SBID_CHG}" type="number" /> 원</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td class="ta-C">
												<c:set var="AVG_SBID_UPR" value="${sbidVo.AVG_SBID_UPR}" />
												<c:set var="AVG_SBID_CHG" value="${sbidVo.AVG_SBID_CHG}" />
												<span class="won l-spacing d-block tar"><fmt:formatNumber value="${AVG_SBID_UPR}" type="number" /> 원</span>									
												<c:choose>
													<c:when test="${AVG_SBID_CHG > 0}">
														<span class="fc-red max_sbid_chg l-spacing d-block tar">+ <fmt:formatNumber value="${AVG_SBID_CHG}" type="number" /> 원</span>	
													</c:when>
													<c:when test="${AVG_SBID_CHG < 0}">
														<span class="fc-blue max_sbid_chg l-spacing d-block tar">- <fmt:formatNumber value="${fn:replace(AVG_SBID_CHG, '-', '')}" type="number" /> 원</span>
													</c:when>
													<c:otherwise>
														<span class="fc-zero max_sbid_chg l-spacing d-block tar"><fmt:formatNumber value="${AVG_SBID_CHG}" type="number" /> 원</span>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				
				<div style="margin-top: 20px;">
					<canvas id="myChartSample4" class="bar_chart"></canvas>
				</div>
				<div class="gap-area mt50">
					<div class="gap-box">
						<c:set var="EXPRI_SBID_SUM_AMT" value="${empty sbidInfo ? 0 : sbidInfo.EXPRI_SBID_SUM_AMT}" />
						<em>낙찰예정가</em>
						<strong><fmt:formatNumber value="${EXPRI_SBID_SUM_AMT}" type="number" />원</strong>
					</div>
					<div>
						<c:set var="expri_gap" value="${AVG_SBID_UPR - EXPRI_SBID_SUM_AMT}" />
						<c:choose>
							<c:when test="${expri_gap >= 0 }"><span class="gap down expri_gap">+ <fmt:formatNumber value="${expri_gap}" type="number" /></span></c:when>
							<c:when test="${expri_gap < 0 }"><span class="gap up expri_gap"><fmt:formatNumber value="${expri_gap}" type="number" /></span></c:when>
						</c:choose>
					</div>
					<div class="gap-box">
						<em>낙찰평균가</em>
						<strong><fmt:formatNumber value="${AVG_SBID_UPR}" type="number" />원</strong>
					</div>
				</div>
			</div>
		</div>
		<div class="sec-board">
			<h2 class="sec-tit">월별 낙찰가 현황</h2>
			<div class="white-box chart_area">
				<canvas id="myChartSample2" class="bar_chart"></canvas>
			</div>
		</div>
		<div class="sec-board">
			<h2 class="sec-tit">월별 출장우 현황</h2>
			<div class="white-box chart_area">
				<canvas id="myChartSample3" class="bar_chart"></canvas>
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
var barSbidData = [];	//평균 낙찰가
var barExpriData = [];	//평균 예상가
const ctx2 = document.getElementById('myChartSample2');

<c:forEach items="${monSbidPriceList}" var="vo">
	var aucDt = "${vo.AUC_DT}";
	var aucDtTxt = parseInt(aucDt.substring(aucDt, 4));
	var avgSbidUpr = "${vo.AVG_SBID_UPR}";
	var avgExpriUpr = "${vo.AVG_EXPRI_UPR}";
	
	labelData.push(aucDtTxt ?? '없음');
	barSbidData.push(Math.round(avgSbidUpr / 10000) ?? 0);
	barExpriData.push(Math.round(avgExpriUpr / 10000) ?? 0);
</c:forEach>

new Chart(ctx2, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '평균예상가(만원)',
				data: barExpriData,
				borderColor: '#37a2eb',
				type: 'line',
				order: 0,
				yAxisID: 'y-left'
			},
			{
				label: '평균낙찰가(만원)',
				data: barSbidData,
				borderColor: '#a5dfdf',
				backgroundColor: '#a5dfdf',
				type: 'bar',
				borderWidth: 0,
				order: 1,
				yAxisID: 'y-left'
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
		legend: {
			labels:{
				fontSize : 10
			}
		}
		, scales: {
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
		}
		, tooltips: {
			enabled: true,
			callbacks: {
				label: function(tooltipItem, data) {
					const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
					return parseInt(tooltipValue, 10).toLocaleString();
				}
			}
		}
	},
});

var preCntData = [];	// 전년도 출장우
var thisCntData = [];	// 올해 출장우

<c:forEach items="${monSogCowList}" var="vo">
	var month = "${vo.MONTH}";
	var preCowCnt = "${vo.PRE_COW_CNT}";
	var thisCowCnt = "${vo.THIS_COW_CNT}";
	
	labelData.push(month ?? '없음');
	preCntData.push(preCowCnt);
	thisCntData.push(thisCowCnt);
</c:forEach>
const ctx3 = document.getElementById('myChartSample3');
new Chart(ctx3, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '전년도 출장우',
				data: preCntData,
				borderColor: '#37a2eb',
				type: 'line',
				order: 0,
				yAxisID: 'y-left'
			},
			{
				label: '출장우',
				data: thisCntData,
				borderColor: '#ffb1c1',
				backgroundColor: '#ffb1c1',
				type: 'bar',
				borderWidth: 0,
				order: 1,
				yAxisID: 'y-left'
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
		legend: {
			labels:{
				fontSize : 10
			}
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
		},
		tooltips: {
			enabled: true,
			callbacks: {
				label: function(tooltipItem, data) {
					const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
					return parseInt(tooltipValue, 10).toLocaleString();
				}
			}
		}
	},
});
const ctx4 = document.getElementById('myChartSample4');
var currData = [];
var prevData = [];

currData.push("${sbidInfo.MAX_SBID_UPR}" == undefined ? 0 : Math.round("${sbidInfo.MAX_SBID_UPR}" / 10000));
currData.push("${sbidInfo.AVG_SBID_UPR}" == undefined ? 0 : Math.round("${sbidInfo.AVG_SBID_UPR}" / 10000));
currData.push("${sbidInfo.MIN_SBID_UPR}" == undefined ? 0 : Math.round("${sbidInfo.MIN_SBID_UPR}" / 10000));

prevData.push("${sbidInfo.MAX_SBID_UPR_B}" == undefined ? 0 : Math.round("${sbidInfo.MAX_SBID_UPR_B}" / 10000));
prevData.push("${sbidInfo.AVG_SBID_UPR_B}" == undefined ? 0 : Math.round("${sbidInfo.AVG_SBID_UPR_B}" / 10000));
prevData.push("${sbidInfo.MIN_SBID_UPR_B}" == undefined ? 0 : Math.round("${sbidInfo.MIN_SBID_UPR_B}" / 10000));

new Chart(ctx4, {
	type: 'bar',
	data: {
		labels: ['최고', '평균', '최저'],
		datasets: [
			{
				label: '현재(만원)',
				data: currData,
				backgroundColor: '#9ad0f5',
				borderWidth: 0
			},
			{
				label: '이전(만원)',
				data: prevData,
				backgroundColor: '#ffb1c1',
				borderWidth: 0
			}
		]
	},
	options: {
		scales: {
			yAxes : [{
				ticks : {
					beginAtZero: true
					, callback: function(value, index) {
						return parseInt(value, 10).toLocaleString();
					}
				}
			}]
		},
		legend: {
			labels:{
				fontSize : 10
			}
		}
		, tooltips: {
			enabled: true,
			callbacks: {
				label: function(tooltipItem, data) {
					const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
					return parseInt(tooltipValue, 10).toLocaleString();
				}
			}
		}
	}
});
</script>