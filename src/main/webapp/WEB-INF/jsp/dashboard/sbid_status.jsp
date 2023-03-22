<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMM" var="nowMonth" />
<input type="hidden" name="searchPlaceNm" 		id="searchPlaceNm" 			value="">

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}" />
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}" />
	<input type="hidden" name="searchMonth" 		id="searchMonth" 			value="${inputParam.searchMonth}" />
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="" />
	<input type="hidden" name="aucObjDsc" 		id="aucObjDsc" 			value="${inputParam.aucObjDsc }" />
	<input type="hidden" name="monthOldC" 		id="monthOldC" 			value="" />
	<input type="hidden" name="nowMonth" 		id="nowMonth" 			value="${nowMonth}" />
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축시장 낙찰가 시세</h2>
		</div>
		<div class="dark-box calendar-box sty-round">
			<div class="c-months">
				<div class="has-btn">
					<button class="btn-prev btn-chg"><span class="sr-only">이전</span></button>
					<span class="month_txt_title">${searchMonTxt }</span>
<%-- 					<button class="btn-next btn-chg" ${nowMonth eq inputParam.searchMonth ? 'disabled' : ''}><span class="sr-only">다음</span></button> --%>
					<button class="btn-next btn-chg" <c:if test="${nowMonth eq inputParam.searchMonth}">style="display:none;"</c:if>><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="sort-area">
			<div class="btnBox auc_obj_dsc">
				<button id="1" class="${empty aucObjDsc or aucObjDsc eq '1' ? 'on' : '' }">송아지</button>
				<button id="2" class="${aucObjDsc eq '2' ? 'on' : '' }">비육우</button>
				<button id="3" class="${aucObjDsc eq '3' ? 'on' : '' }">번식우</button>
			</div>
			<hr class="hr-line"/>
			<div class="btnBox sty-blue birth_weight_dsc">
				<button id="" class="${empty inputParam.monthOldC ? 'on' : ''}">전체</button>
				<button id="01" class="${inputParam.monthOldC eq '01' ? 'on' : ''}">4~5개월</button>
				<button id="02" class="${inputParam.monthOldC eq '02' ? 'on' : ''}">6~7개월</button>
				<button id="03" class="${inputParam.monthOldC eq '03' ? 'on' : ''}">8개월 이상</button>
			</div>
		</div>
		<div class="white-box">
			<div class="pc-top">
				<div class="cow-board-top">
					<strong class="tot_sbid_cnt"><fmt:formatNumber value="${sbidInfo.TOT_SBID_CNT}" type="number" /> 두</strong>
					<c:choose>
						<c:when test="${sbidInfo.TOT_SBID_CHG > 0 }">
							<c:set var="tot_sbid_chg_txt">
								▲ <fmt:formatNumber value="${sbidInfo.TOT_SBID_CHG}" type="number" /> 두
							</c:set>
							<span class="tot_sbid_chg_txt fc-red">${tot_sbid_chg_txt }</span>
						</c:when>
						<c:when test="${sbidInfo.TOT_SBID_CHG eq 0 }">
							<c:set var="tot_sbid_chg_txt">
								- <fmt:formatNumber value="${sbidInfo.TOT_SBID_CHG}" type="number" /> 두
							</c:set>
							<span class="tot_sbid_chg_txt fc-blue">${tot_sbid_chg_txt }</span>
						</c:when>
						<c:otherwise>
							<c:set var="tot_sbid_chg_txt">
								▼ <fmt:formatNumber value="${fn:replace(sbidInfo.TOT_SBID_CHG, '-', '')}" type="number" /> 두
							</c:set>
							<span class="tot_sbid_chg_txt fc-blue">${tot_sbid_chg_txt }</span>
						</c:otherwise>
					</c:choose>
					<br>(<span class="month_old_c_nm">${empty sbidInfo.MONTH_OLD_C_NM ? '전체' : sbidInfo.MONTH_OLD_C_NM}</span>)
				</div>
			
				<ul class="board-number col-3">
					<li>
						<dl>
							<dt>최고</dt>
							<dd class="won max_sbid_upr"><fmt:formatNumber value="${sbidInfo.MAX_SBID_UPR}" type="number" /> 원</dd>
							<c:choose>
								<c:when test="${sbidInfo.MAX_SBID_CHG > 0}">
									<dd><span class="fc-red max_sbid_chg">+ <fmt:formatNumber value="${sbidInfo.MAX_SBID_CHG}" type="number" /> 원</span></dd>	
								</c:when>
								<c:when test="${sbidInfo.MAX_SBID_CHG < 0}">
									<dd><span class="fc-blue max_sbid_chg">- <fmt:formatNumber value="${fn:replace(sbidInfo.MAX_SBID_CHG, '-', '')}" type="number" /> 원</span></dd>	
								</c:when>
								<c:otherwise>
									<dd><span class="fc-zero max_sbid_chg"><fmt:formatNumber value="${sbidInfo.MAX_SBID_CHG}" type="number" /> 원</span></dd>
								</c:otherwise>
							</c:choose>
						</dl>
					</li>
					<li>
						<dl>
							<dt>평균</dt>
							<dd class="won avg_sbid_upr"><fmt:formatNumber value="${sbidInfo.AVG_SBID_UPR}" type="number" /> 원</dd>
							<c:choose>
								<c:when test="${sbidInfo.AVG_SBID_CHG > 0}">
									<dd><span class="fc-red avg_sbid_chg">+ <fmt:formatNumber value="${sbidInfo.AVG_SBID_CHG}" type="number" /> 원</span></dd>	
								</c:when>
								<c:when test="${sbidInfo.AVG_SBID_CHG < 0}">
									<dd><span class="fc-blue avg_sbid_chg">- <fmt:formatNumber value="${fn:replace(sbidInfo.AVG_SBID_CHG, '-', '')}" type="number" /> 원</span></dd>	
								</c:when>
								<c:otherwise>
									<dd><span class="fc-zero avg_sbid_chg"><fmt:formatNumber value="${sbidInfo.AVG_SBID_CHG}" type="number" /> 원</span></dd>
								</c:otherwise>
							</c:choose>
						</dl>
					</li>
					<li>
						<dl>
							<dt>최저</dt>
							<dd class="won min_sbid_upr"><fmt:formatNumber value="${sbidInfo.MIN_SBID_UPR}" type="number" /> 원</dd>
							<c:choose>
								<c:when test="${sbidInfo.MIN_SBID_CHG > 0}">
									<dd><span class="fc-red min_sbid_chg">+ <fmt:formatNumber value="${sbidInfo.MIN_SBID_CHG}" type="number" /> 원</span></dd>	
								</c:when>
								<c:when test="${sbidInfo.MIN_SBID_CHG < 0}">
									<dd><span class="fc-blue min_sbid_chg">- <fmt:formatNumber value="${fn:replace(sbidInfo.MIN_SBID_CHG, '-', '')}" type="number" /> 원</span></dd>	
								</c:when>
								<c:otherwise>
									<dd><span class="fc-zero min_sbid_chg"><fmt:formatNumber value="${sbidInfo.MIN_SBID_CHG}" type="number" /> 원</span></dd>
								</c:otherwise>
							</c:choose>
						</dl>
					</li>
				</ul>
			</div>
			<div id="chart_area_4" class="chart_area" style="margin-top: 20px;">
				<canvas id="myChartSample4" class="bar_chart"></canvas>
			</div>
			<div class="gap-area mt50">
				<div class="gap-box">
					<em>낙찰예정가</em>
					<strong class="expri_sbid_sum_amt"><fmt:formatNumber value="${sbidInfo.EXPRI_SBID_SUM_AMT}" type="number" />원</strong>
				</div>
				<div>
					<c:set var="expri_gap" value="${sbidInfo.AVG_SBID_UPR - sbidInfo.EXPRI_SBID_SUM_AMT}" />
					<c:choose>
						<c:when test="${expri_gap > 0 }"><span class="gap up expri_gap">+ <fmt:formatNumber value="${expri_gap}" type="number" /></span></c:when>
						<c:when test="${expri_gap <= 0 }"><span class="gap down expri_gap"><fmt:formatNumber value="${expri_gap}" type="number" /></span></c:when>
					</c:choose>
				</div>
				<div class="gap-box">
					<em>낙찰평균가</em>
					<strong class="avg_sbid_upr"><fmt:formatNumber value="${sbidInfo.AVG_SBID_UPR}" type="number" />원</strong>
				</div>
			</div>
			<hr class="hr-line" />
			<h2 class="sec-tit">지역별 평균 낙찰가</h2>
			<ul class="board-number average" id="area_sbid_list">
				<c:forEach items="${areaSbidList }" var="areaVo" varStatus="a">
				<li class="sbidPop" id="${areaVo.NA_BZPLCLOC}">
					<dl class="">		<!-- ${empty inputParam.searchPlace and a.count eq '1' ? 'on' : (areaVo.NA_BZPLCLOC eq inputParam.searchPlace ? 'on' : '') } -->
						<dt class="locnm">${areaVo.NA_BZPLCLOC_NM}</dt>
						<dd class="won"><fmt:formatNumber value="${areaVo.AVG_SBID_UPR}" type="number" />원</dd>
						<dd><fmt:formatNumber value="${areaVo.TOT_SBID_CNT}" type="number" />두</dd>
					</dl>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit chart-tit">월별 평균 낙찰가</h2>
		<div class="white-box chart_area" id="chart_area_2">
			<canvas id="myChartSample2" class="bar_chart"></canvas>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit chart-tit">월별 출장두수</h2>
		<div class="white-box chart_area" id="chart_area_3">
			<canvas id="myChartSample3" class="bar_chart"></canvas>
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
	}
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
	}
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
				borderWidth: 0,
				yAxisID: 'y-left'
			},
			{
				label: '이전(만원)',
				data: prevData,
				backgroundColor: '#ffb1c1',
				borderWidth: 0,
				yAxisID: 'y-left'
			}
		]
	},
	options: {
		scales: {
			yAxes : [{
				ticks : {
					beginAtZero: true
				}
			}]
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
	}
});
</script>
