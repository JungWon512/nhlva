<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="/static/css/guide/style-dashboard.css">

<!-- 현재날짜 처리 -->
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMM" var="nowMonth" />
<input type="hidden" name="searchPlaceNm" 		id="searchPlaceNm" 			value="">

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="">
	<input type="hidden" name="searchMonth" 	id="searchMonth" 			value="${inputParam.searchMonth}">
	<input type="hidden" name="aucObjDsc" 		id="aucObjDsc" 			value="${inputParam.aucObjDsc }" />
	<input type="hidden" name="monthOldC" 		id="monthOldC" 			value="" />
	<input type="hidden" name="indvSexC" 		id="indvSexC" 			value="1" />
	<input type="hidden" name="nowMonth" 		id="nowMonth" 			value="${nowMonth}" />
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축시장 출장우<p class="annotation">(<span class="cntNaBzplc"><fmt:formatNumber value="${empty sogCowInfo ? 0 : sogCowInfo.CNT_NABZPLC}" type="number" /></span>개 조합)</p></h2>			
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
			<div style="margin: 20px auto; max-width:500px;" id="chart_area1" class="chart_area">
				<canvas id="myPieSample1"></canvas>
			</div>
			<div class="table-simple m-0">
				<table>
					<colgroup>
						<col>
						<col width="26%">
						<col width="26%">
						<col width="26%">
					</colgroup>
					<thead>
						<tr>
							<th scope="col" class="ta-C">구분</th>
							<c:set var="before2Month" value="${inputParam.before2Month}"/>
							<th scope="col" class="before2Month ta-C">${fn:substring(before2Month,4,6)}월</th>
							<c:set var="beforeMonth" value="${inputParam.beforeMonth}"/>
							<th scope="col" class="beforeMonth ta-C">${fn:substring(beforeMonth,4,6)}월</th>
							<c:set var="searchMonth" value="${inputParam.searchMonth}"/>
							<th scope="col" class="searchMonth ta-C on">${fn:substring(searchMonth,4,6)}월</th>
						</tr>
					</thead>
					
					<tbody class="sogCowInfoList">
						<c:choose>
							<c:when test="${empty sogCowInfoList }">
								<tr class="sogCow"><th>송아지</th><td class="ta-C">0</td><td class="ta-C">0</td><td class="ta-C on">0</td></tr>
								<tr class="sogCow"><th>송아지(암)</th><td class="ta-C">0</td><td class="ta-C">0</td><td class="ta-C on">0</td></tr>
								<tr class="sogCow"><th>송아지(수)</th><td class="ta-C">0</td><td class="ta-C">0</td><td class="ta-C on">0</td></tr>
								<tr class="sogCow"><th>비육우</th><td class="ta-C">0</td><td class="ta-C">0</td><td class="ta-C on">0</td></tr>
								<tr class="sogCow"><th>번식우</th><td class="ta-C">0</td><td class="ta-C">0</td><td class="ta-C on">0</td></tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${sogCowInfoList}" var="cowInfo">
									<tr class="sogCow">
										<th>${cowInfo.AUC_OBJ_DSC_NAME}</th>
										<td class="ta-C"><fmt:formatNumber value="${cowInfo.CNT_1}" type="number" /></td>
										<td class="ta-C"><fmt:formatNumber value="${cowInfo.CNT_2}" type="number" /></td>
										<td class="ta-C on"><fmt:formatNumber value="${cowInfo.CNT_3}" type="number" /></td>
									</tr>
								</c:forEach>
							</c:otherwise>							
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<hr class="hr-line" />
	<div class="sec-board">
		<h2 class="sec-tit">월별 출장우 현황</h2>
		<div class="sort-area">
			<div class="btnBox auc_obj_dsc">
				<button id="11" class="on">암송아지</button>
				<button id="12">수송아지</button>
				<button id="2">비육우</button>
				<button id="3">번식우</button>
			</div>
			<hr class="hr-line"/>
			<div class="btnBox sty-blue birth_weight_dsc">
				<button id="" class="on">전체</button>
				<button id="01">4~5개월</button>
				<button id="02">6~7개월</button>
				<button id="03">8개월 이상</button>
			</div>
		</div>
		<div class="white-box" style="display:none;">
			<ul class="board-number average" id="area_scow_list">
				<c:forEach items="${areaSbidList}" var="areaVo" varStatus="a">
				<li class="sCowPop" id="${areaVo.NA_BZPLCLOC}">
					<dl>
						<dt class="locnm">${areaVo.NA_BZPLCLOC_NM}</dt>
						<dd class="won"><fmt:formatNumber value="${areaVo.AVG_EXPRI_UPR}" type="number" />원</dd>
						<dd><fmt:formatNumber value="${areaVo.TOT_SOG_CNT}" type="number" />두</dd>
					</dl>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class="sec-board">
<!-- 		<h2 class="sec-tit">월별 출장우 현황</h2> -->
		<div id="chart_area2" class="white-box chart_area">
			<canvas id="myCharSample1" class="bar_chart"></canvas>
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



const ctx2 = document.getElementById('myPieSample1');
const sogCowInfoList = ("${sogCowInfoList}" == null || "${sogCowInfoList}" == undefined) ? [] : "${sogCowInfoList}";
if( sogCowInfoList.length <= 0 
		|| (sogCowInfoList.length > 0 && "${sogCowInfoList[0].CNT_3}" == 0 && "${sogCowInfoList[1].CNT_3}" == 0 && "${sogCowInfoList[2].CNT_3}" == 0) ){
	
	new Chart(ctx2, {
		type: 'pie',
		data: {
			labels: ['등록된 출장두수가 없습니다'],
			datasets: [
				{
					data: [100],
					borderColor: ['#ffcd56'],
					backgroundColor: ['#ffcd56'],
					borderWidth: 0,
				}
			]
		},
		options: {
			responsive: true,
			maintainAspectRatio: true,
			plugins: {
				legend: {
					position: 'right'
				},
			}
		},
	});
	
}
else{
	const auc1Data = ${sogCowInfoList[0].CNT_3};
	const auc2Data = ${sogCowInfoList[1].CNT_3};
	const auc3Data = ${sogCowInfoList[2].CNT_3};

	new Chart(ctx2, {
			type: 'pie',
		data: {
			labels: ['송아지', '비육우', '번식우'],
			datasets: [
				{
					data: [auc1Data,auc2Data,auc3Data],
					borderColor: ['#ffcd56','#ff9f40','#ff4069'],
					backgroundColor: ['#ffcd56','#ff9f40','#ff4069'],
					borderWidth: 0,
				}
			]
		},
		options: {
			responsive: true,
			maintainAspectRatio: true,
			plugins: {
				legend: {
					position: 'right'
				},
			}
		},
	});
}
	
	
</script>
