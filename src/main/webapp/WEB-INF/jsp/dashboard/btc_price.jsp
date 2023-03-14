<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<style>
	.nonData{background-color: #eee;
    height: 200px;
    border-radius: 10px;
    text-align: center;
    vertical-align: center;
    padding-top: 87px;}
</style>
<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
	<input type="hidden" name="searchSbidDt" 		id="searchSbidDt" 			value="${btcAucDate.SBID_DT}">
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<h2 class="sec-tit">가축시장 도축장시세 <span style="font-weight:400; font-size:14px;">(농협 4대 공판장 기준)</span></h2>
		<c:choose>
			<c:when test="${empty btcAucDate }">
		<div class="nonData">
			<p>최근 도축 데이터가 없습니다.</p>
		</div>	
			</c:when>
			<c:otherwise>
		<div class="btnBox btnSex">
			<button class="on" id="1">암</button>
			<button id="2">수</button>
		</div>
		<div class="dark-box calendar-box">
			<div class="c-years">
				<div class="basicYm">${basicYm}</div>
			</div>
			<div class="c-days bd-left">
				<div class="has-btn">
					<button class="btn-prev btn-date-chg" id="${btcAucDate.BEFORE_DT}" ${empty btcAucDate.BEFORE_DT? 'disabled' : '' }><span class="sr-only">이전</span></button>
					<span class="basicDay">${basicDay }</span>
					<button class="btn-next btn-date-chg" id="${btcAucDate.AFTER_DT}" ${empty btcAucDate.AFTER_DT? 'disabled' : '' }><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="sort-grade">
			<select name="searchBtcAucGrd" id="searchBtcAucGrd">
				<c:forEach items="${btcAucGrd }" var="grade">
					<option value="${grade.SRA_GRD_DSC }">${grade.SRA_GRD_DSC }</option>
				</c:forEach>
			</select>	
		</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<c:if test="${!empty btcAucDate }">
	<div class="sec-board">
		<div class="white-box">
			<div class="market-price col-1">
				<dl class="pan">
					<dt>평균가격</dt>
					<dd class="fw-bold avg_sra_sbid_upr"><fmt:formatNumber value="${btcAucAvg.AVG_SRA_SBID_UPR}" type="number" /> 원</dd>
					<c:choose>
						<c:when test="${btcAucAvg.AVG_CHG < 0}">
							<dd class="per fc-blue avg_chg">▼ ${fn:replace(btcAucAvg.AVG_CHG, '-', '') } %</dd>
						</c:when>
						<c:when test="${btcAucAvg.AVG_CHG eq 0}">
							<dd class="per fc-zero avg_chg">- ${btcAucAvg.AVG_CHG} %</dd>
						</c:when>
						<c:otherwise>
							<dd class="per fc-red avg_chg">▲ ${btcAucAvg.AVG_CHG} %</dd>
						</c:otherwise>
					</c:choose>
					
				</dl>
			</div>
			<div class="market-price col-2">
				<dl class="pan">
					<dt>최고가격</dt>
					<dd class="fw-bold max_sra_sbid_upr"><fmt:formatNumber value="${btcAucAvg.MAX_SRA_SBID_UPR}" type="number" /> 원</dd>
					<c:choose>
						<c:when test="${btcAucAvg.MAX_CHG < 0}">
							<dd class="per fc-blue max_chg">▼ ${fn:replace(btcAucAvg.MAX_CHG, '-', '') } %</dd>
						</c:when>
						<c:when test="${btcAucAvg.MAX_CHG eq 0}">
							<dd class="per fc-zero max_chg">- ${btcAucAvg.MAX_CHG} %</dd>
						</c:when>
						<c:otherwise>
							<dd class="per fc-red max_chg">▲ ${btcAucAvg.MAX_CHG} %</dd>
						</c:otherwise>
					</c:choose>
				</dl>
				<dl class="pan">
					<dt>최저가격</dt>
					<dd class="fw-bold min_sra_sbid_upr"><fmt:formatNumber value="${btcAucAvg.MIN_SRA_SBID_UPR}" type="number" /> 원</dd>
					<c:choose>
						<c:when test="${btcAucAvg.MIN_CHG < 0}">
							<dd class="per fc-blue min_chg">▼ ${fn:replace(btcAucAvg.MIN_CHG, '-', '') } %</dd>
						</c:when>
						<c:when test="${btcAucAvg.MIN_CHG eq 0}">
							<dd class="per fc-zero min_chg">- ${btcAucAvg.MIN_CHG} %</dd>
						</c:when>
						<c:otherwise>
							<dd class="per fc-red min_chg">▲ ${btcAucAvg.MIN_CHG } %</dd>
						</c:otherwise>
					</c:choose>
				</dl>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="market-price col-1">
				<dl class="pan">
					<dt>거래두수</dt>
					<dd class="fw-bold brc_hdcn"><fmt:formatNumber value="${btcAucAvg.BRC_HDCN}" type="number" /> 두</dd>
					<c:choose>
						<c:when test="${btcAucAvg.BRC_CHG < 0}">
							<dd class="fc-blue brc_chg">▼ ${fn:replace(btcAucAvg.BRC_CHG, '-', '') } %</dd>
						</c:when>
						<c:when test="${btcAucAvg.BRC_CHG eq 0}">
							<dd class="per fc-zero brc_chg">- ${btcAucAvg.BRC_CHG} %</dd>
						</c:when>
						<c:otherwise>
							<dd class="fc-red brc_chg">▲ ${btcAucAvg.BRC_CHG } %</dd>
						</c:otherwise>
					</c:choose>
				</dl>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="chart_area" style="margin-bottom: 30px;">
				<canvas id="myCharSample5" class="bar_chart"></canvas>
			</div>
			<div class="table-simple m-0">
				<table>
					<colgroup>
						<col width="20%">
						<col width="50%">
						<col width="30%">
					</colgroup>
					<thead>
						<tr>
							<th scope="col">지역</th>
							<th scope="col" class="ta-C">kg당 거래가</th>
							<th scope="col" class="ta-C">낙찰두수</th>
						</tr>
					</thead>
					<tbody class="no-bd" id="tbodyAreaList">
						<c:forEach items="${areaList }" var="areaVo">
							<tr>
								<td>${areaVo.CLNTNM }</td>
								<td class="ta-C"><fmt:formatNumber value="${areaVo.AVG_SBID_PRC }" type="number" /></td>
								<td class="ta-C"><fmt:formatNumber value="${areaVo.SUM_HDCN }" type="number" /></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot class="bd-top">
						<tr >
							<td>평균</td>
							<td class="ta-C foot_avg_sbid_prc"><fmt:formatNumber value="${areaSum.AVG_SBID_PRC }" type="number" /></td>
							<td class="ta-C foot_sum_hdcn"><fmt:formatNumber value="${areaSum.SUM_HDCN }" type="number" /></td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
	
	</c:if>
	
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
	var labelData = [];
	var barData = [];
	const ctx = document.getElementById('myCharSample5');
	<c:forEach items="${areaList}" var="vo">
		labelData.push("${vo.CLNTNM}" ?? '없음');
		barData.push("${vo.AVG_SBID_PRC}" ?? 0);
	</c:forEach>
	
	var config = {
		type: 'bar',
		data: {
			labels: labelData,
			datasets: [
				{
					label: '거래가',
					data: barData,
					borderColor: '#a5dfdf',
					backgroundColor: '#a5dfdf',
					yAxisID: 'y-left'
				}
			]
		},
		options: {
			interaction: {
				intersect: false,
				mode: 'index'
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
	};
	
	barChart = new Chart(ctx, config);
</script>