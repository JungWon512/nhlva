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
	
<div class="chk_step1 pc-mt0 dash-tit">
	<div class="main-tab">
		<ul>
			<li>
				<a href="javascript:pageMove('/home')">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
			</li>
			<li class="on">
				<a href="javascript:pageMove('/dashboard')">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
			</li>
		</ul>
	</div>
</div>
<div class="board-main">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">최근 가축시장 시세</h2>
			<a href="javascript:;" id="dashboard_filter" class="btn-more"
				style="width: 30px;height: 30px;background: url(/static/images/guide/v2/ico-filter.svg) no-repeat 50% 50%;background-size: 23px auto;"></a>
		</div>
		<div class="period-area">
			최근 ${ range }일 (${searchPreDate}~${searchDate} 까지)
			<strong>전국</strong>
		</div>
		<div class="btnBox aucObjBtn">
			<button id="1" class="on">송아지</button>
			<button id="2" class="<c:if test="${inputParam.searchAucObjDsc eq '2'}">on</c:if>">비육우</button>
			<button id="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">on</c:if>>번식우</button>
		</div>
		<ul class="list-market">
			<li>
				<dl class="item">
					<dt>전체</dt>
					<dd>
						<div class="price"></div>
						<div class="num"></div>
					</dd>
				</dl>
			</li>
		</ul>
	</div>
	<div class="sec-board">
		<div class="btnBox avgSbidBtn">
			<button id="" class="on">전체</button>
			<button id="01" class="<c:if test="${inputParam.searchMonthOldC eq '01'}">on</c:if>">4~5개월</button>
			<button id="02" class="<c:if test="${inputParam.searchMonthOldC eq '02'}">on</c:if>">6~7개월</button>
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
								<dt><img src="/static/images/guide/v2/sample01.jpg" alt=""></dt>
								<dd class="name">${ vo.CLNTNM }</dd>
								<fmt:formatNumber value="${vo.AMT}" type="number" var="AMT"/>
								<dd class="change fc-red">${empty AMT ? '0' : AMT} 원</dd>
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
					<li style="font-size:20px;">금주의 TOP 3이 없습니다.</li>
				</c:otherwise>
			</c:choose>
		</ol>
	</div>
	<hr class="hr-line"/>
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축 시장의 더 많은 정보들 ...</h2>
		</div>
		<div class="list-link">
			<div>
				<a href="javascript:pageMove('/dashboard_cow')">출장우 현황</a>
				<a href="javascript:pageMove('/dashboard_btc_price')">도축장 시세</a>
				<a href="javascript:pageMove('/guide?dashYn=Y')">사용 안내</a>
			</div>
			<div>
				<a href="javascript:pageMove('/dashboard_sbid_status')" class="row2">낙찰시세</a>
				<a href="javascript:pageMove('/dashboard_parti_status')">가축시장<br>참가현황</a>
			</div>
		</div>
		<div class="login-btn-area">
			<c:choose>
				<c:when test="${ loginYn == 'Y' }">
					<a href="javascript:;" class="btn-logout">로그아웃 ></a>
				</c:when>
				<c:otherwise>
					<a href="javascript:;" class="btn-login">로그인 ></a>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if test="${ loginYn == 'Y' }">
			<div class="list-link is-bottom">
				<a href="javascript:;">가축시장 접속자 현황</a>
				<a href="javascript:;">[추가] 축협용 통계 + 1</a>
				<a href="javascript:;">[추가] 축협용 통계 + 1</a>
			</div>
		</c:if>
	</div>
</div>
