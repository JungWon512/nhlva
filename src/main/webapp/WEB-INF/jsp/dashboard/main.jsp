<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="loginYn" 						id="loginYn" 						value="${inputParam.loginYn}">
	<input type="hidden" name="searchRaDate" 			id="searchRaDate" 				value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 				id="searchFlag" 					value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 				id="searchPlace" 					value="${inputParam.searchPlace}">
	<input type="hidden" name="searchAucObjDsc" 		id="searchAucObjDsc" 			value="${inputParam.searchAucObjDsc}">
	<input type="hidden" name="searchMonthOldC" 		id="searchMonthOldC" 			value="${inputParam.searchMonthOldC}">
	<input type="hidden" name="placeNm" 					id="placeNm" 						value="${inputParam.placeNm}">
</form>
	
<div class="chk_step1 dash-tit">
	<div class="main-tab">
		<ul>
			<li>
				<a href="javascript:pageMove('/home')">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
			</li>
			<li class="on">
				<a href="javascript:pageMove('/dashboard/main')">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
			</li>
		</ul>
	</div>
</div>
<div class="board-main page-board-main">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">최근 가축시장 시세</h2>
			<a href="javascript:;" id="dashboard_filter" class="btn-more"
				style="width: 30px;height: 30px;background: url(/static/images/guide/v2/ico-filter.svg) no-repeat 50% 50%;background-size: 23px auto;"></a>
		</div>
		<div class="period-area">
			${title}
			<strong>전국</strong>
		</div>
		<div class="btnBox aucObjBtn">
			<button id="1" class="on">송아지</button>
			<button id="2" class="<c:if test="${inputParam.searchAucObjDsc eq '2'}">on</c:if>">비육우</button>
			<button id="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">on</c:if>>번식우</button>
		</div>
		<ul class="list-market">
			<c:choose>
				<c:when test="${!empty cowPriceList}">
					<c:forEach items="${cowPriceList}" var="cowPriceVo" varStatus="c">
						<li class="row_${c.count}">
							<dl class="item">
								<dt>${cowPriceVo.MONTH_OLD_C_NM}</dt>		
								<dd>
									<c:choose>
										<c:when test="${cowPriceVo.ACS_SBID_AM > 0 }">
									<div class="price"><fmt:formatNumber value="${cowPriceVo.THIS_AVG_SBID_AM}" type="number" /> 원 <span class="per fc-red">▲ ${cowPriceVo.ACS_SBID_AM} %</span></div>
										</c:when>
										<c:otherwise>
									<div class="price"><fmt:formatNumber value="${cowPriceVo.THIS_AVG_SBID_AM}" type="number" /> 원 <span class="per fc-blue">▼ ${cowPriceVo.ACS_SBID_AM} %</span></div>
										</c:otherwise>
									</c:choose>
									
									<c:choose>
										<c:when test="${cowPriceVo.ACS_SBID_CNT > 0}">
									<div class="num"><fmt:formatNumber value="${cowPriceVo.THIS_SUM_SBID_CNT}" type="number" /> 두 <span class="per fc-red">▲ ${cowPriceVo.ACS_SBID_CNT} %</span></div>
										</c:when>
										<c:otherwise>
									<div class="num"><fmt:formatNumber value="${cowPriceVo.THIS_SUM_SBID_CNT}" type="number" /> 두 <span class="per fc-blue">▼ ${cowPriceVo.ACS_SBID_CNT} %</span></div>
										</c:otherwise>
									</c:choose>
								</dd>		
							</dl>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li colspan="4" style="text-align: center;">데이터가 없습니다.</li>
				</c:otherwise>			
			</c:choose>
		</ul>
	</div>
	<div class="sec-board">
		<div class="btnBox avgSbidBtn">
			<button id="" class="on">전체</button>
			<button id="01" class="<c:if test="${inputParam.searchMonthOldC eq '01'}">on</c:if>">4~5개월</button>
			<button id="02" class="<c:if test="${inputParam.searchMonthOldC eq '02'}">on</c:if>">6~7개월</button>
			<button id="03" class="<c:if test="${inputParam.searchMonthOldC eq '03'}">on</c:if>">8개월 이상</button>
		</div>
		<h2 class="sec-tit">지역별 평균 낙찰가</h2>
		<div class="white-box">
			<canvas id="myChart1"></canvas>
		</div>
	</div>
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">금주의 TOP 3</h2>
			<a href="javascript:;" id="btn-top10" class="btn-top3">Top 10</a>
		</div>
		<ol class="list-top10">
			<c:choose>
				<c:when test="${ recentDateTopList.size() > 0 }">
					<c:forEach items="${ recentDateTopList }" var="vo" varStatus="i">
					<c:if test="${i.index < 3}">
						<li>
							<dl class="union">
								<dt><img src="/static/images/guide/v2/sample_logo.jpg" alt=""></dt>
								<dd class="name">${ vo.CLNTNM }</dd>
								<fmt:formatNumber value="${empty vo.AMT ? 0 : vo.AMT}" type="number" var="AMT"/>
								<c:choose>
									<c:when test="${vo.AMT < 0}">
										<dd class="change fc-blue">${AMT} 원</dd>
									</c:when>
									<c:otherwise>
										<dd class="change fc-red">+${AMT} 원</dd>
									</c:otherwise>								
								</c:choose>
								
								<fmt:formatNumber value="${vo.SBID_AMT}" type="number" var="SBID_AMT"/>
								<dd class="price fc-red">${empty SBID_AMT ? '0' : SBID_AMT} 원</dd>
								<c:choose>
									<c:when test="${ vo.AUC_OBJ_DSC eq '1'}">
										<dd class="info">${ vo.INDV_SEX_C_NM }${ vo.AUC_OBJ_DSC_NM }<i class="dash"></i>${ vo.MONTH_C }개월<i class="dash"></i>${ vo.RG_DSC_NAME }</dd>
									</c:when>
									<c:otherwise>
										<dd class="info">${ vo.AUC_OBJ_DSC_NM }<i class="dash"></i>${ vo.MONTH_C } 개월<i class="dash"></i>${ vo.RG_DSC_NAME }</dd>										
									</c:otherwise>
								</c:choose>
							</dl>
						</li>
						</c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li style="text-align:center;">금주의 TOP 3이 없습니다.</li>
				</c:otherwise>
			</c:choose>
		</ol>
	</div>
	<hr class="hr-line"/>
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축 시장의 더 많은 정보들 ...</h2>
			<div class="login-btn-area">
				<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
					<a href="javascript:pageMove('/dashboard/user/logoutProc');" class="btn-logout"><span class="sr-only">로그아웃</span></a>
				</sec:authorize>
				<sec:authorize access="!hasAnyRole('ROLE_ADMIN')">
					<a href="javascript:pageMove('/dashboard/user/login');" class="btn-login"><span class="sr-only">로그인</span></a>
				</sec:authorize>
			</div>
		</div>
		<div class="list-link">
			<div>
				<a id="/dashboard/cow_status" href="javascript:;"><span class="icon"><img src="/static/images/guide/v2/ico-main-link1.svg" alt=""></span>출장우 현황</a>
				<a id="/dashboard/btc_price" href="javascript:;"><span class="icon"><img src="/static/images/guide/v2/ico-main-link2.svg" alt=""></span>도축장 시세</a>
			</div>
			<div>
				<a id="/dashboard/sbid_status" href="javascript:;" class="row2"><span class="icon"><img src="/static/images/guide/v2/ico-main-link3.svg" alt=""></span>낙찰시세</a>
			</div>
			<div>
				<a id="/guide" href="javascript:;"><span class="icon"><img src="/static/images/guide/v2/ico-main-link4.svg" alt=""></span>사용 안내</a>
			</div>
			<div>
				<a id="/dashboard/parti_status" href="javascript:;"><span class="icon"><img src="/static/images/guide/v2/ico-main-link5.svg" alt=""></span>가축시장<br>참가현황</a>
			</div>
		</div>
		<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
			<div class="list-link is-bottom">
				<a href="javascript:pageMove('/dashboard/staticInfo');"><span class="icon"><img src="/static/images/guide/v2/ico-main-link6.svg" alt=""></span>사용자 접속현황</a>
				<a href="javascript:pageMove('/dashboard/aucBidStaticInfo');"><span class="icon"><img src="/static/images/guide/v2/ico-main-link7.svg" alt=""></span>경매 낙찰현황</a>
				<a href="javascript:pageMove('/dashboard/aucStaticInfo');"><span class="icon"><img src="/static/images/guide/v2/ico-main-link7.svg" alt=""></span>출장우 내역</a>
			</div>
		</sec:authorize>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
//지역별 평균 낙찰가 만들기
var labelData = [];
var barData = [];
var colorData = ['#ff88a180', '#ff9f4080', '#ffd06080', '#55c4c480', '#41a7ec80', '#a97fff80', '#ff9f40', '#ff779380'];

<c:forEach items="${avgPlaceBidAmList}" var="vo">
	labelData.push("${vo.LOCNM}" ?? '없음');
	barData.push(Math.round("${vo.AVG_SBID_AM}" / 10000) ?? 0);
</c:forEach>

// 막대 차트 생성
const ctx = document.getElementById('myChart1');

new Chart (ctx, {
	type: 'bar',
	data: {
		labels: labelData,
		datasets: [
			{
				label: '평균낙찰가(만원)',
				data: barData,
				borderColor: colorData,
				backgroundColor: colorData,
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