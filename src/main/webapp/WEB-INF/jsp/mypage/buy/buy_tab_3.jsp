<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<input type="hidden" id="percent" name="percent" value="${ cowBidPercent[0].SEL_STS_PERCENT }">
<input type="hidden" id="searchDateMy" name="searchDateMy" value="">
<input type="hidden" id="ymFlag" name="ymFlag" value="${ inputParam.ymFlag }">
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="dark-box calendar-box">
			<div class="c-years">
				<div class="has-btn btnMyDtType">연간
					<button class="btn-next bg-green" id="M"><span class="sr-only">월간</span></button>
				</div>
				<div class="has-btn btnMyDtType" style="display:none;">월간
					<button class="btn-prev bg-green" id="Y"><span class="sr-only">연간</span></button>
				</div>
			</div>
			<div class="c-days bd-left">
				<div class="has-btn">
					<button class="btn-prev btnMyChgDt" id="prev"><span class="sr-only">prev year</span></button>
					<span class="searchYearMonth"></span>
					<button class="btn-next btnMyChgDt" id="next"><span class="sr-only">next year</span></button>
				</div>
			</div>
		</div>
		<div class="white-box">
			<h3 class="sub-tit fc-blue johapNm"><i class="dash searchYearMonth"></i></h3>
			<div class="sub-link">
				<i class="dot">●</i> <a href="javascript:pageMove('/dashboard/main')">전국가축시장</a> >>
			</div>
			<div class="tit-area">
				<h2 class="sec-tit">나의 구매 현황</h2>
				<a href="javascript:;" class="btn-more fc-black">상세보기 &gt;</a>
			</div>
			<div class="simple-board">
				<dl class="top">
					<dt></dt>
					<dd class="fc-red totSraSbidAm"></dd>
				</dl>
				<div class="cont">
					<div class="left">
						<span class="searchYearMonth"></span>
						<strong>전체낙찰두수</strong>
						<em class="fc-red totCowCnt"></em>
					</div>
					<ul class="cowCnt">
						<%-- <li>송아지 <span class="num cow1"></span></li>
						<li>비육우 <span class="num cow2"></span></li>
						<li>번식우 <span class="num cow3"></span></li> --%>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<h2 class="sec-tit">축종별 응찰현황</h2>
			<span class="s-txt d-block">※응찰두수가 낙찰두수보다 적으면 낙찰율의 수치가 다를 수 있습니다.</span>
			<div class="doughnut">
				<div class="graph">
					<canvas id="myDoughnutsample2"></canvas>
					<span class="total"></span>
				</div>
				<div class="txt">
					나의 낙찰율
					<strong class="fc-red percent"></strong>
				</div>
			</div>
			<div class="barChart" style="margin-top: 20px;">
				<canvas id="myChartSample3" class="bar_chart"></canvas>
			</div>
			
		</div>
	</div>
	<div class="sec-board">
		<div class="table-simple">
			<h2 class="sec-tit">가축시장 나의 응찰 현황 
				<span class="s-txt d-block searchYearMonth"></span>
			</h2>
			<table>
				<colgroup>
					<col width="21%">
					<col width="25%">
					<col width="25%">
					<col>
				</colgroup>
				<thead>
					<tr>
						<th scope="col">조합</th>
						<th scope="col" class="ta-C">출장두수</th>
						<th scope="col" class="ta-C">낙찰두수</th>
						<th scope="col" class="ta-C">응찰두수</th>
					</tr>
				</thead>
				<tfoot class="top-tfoot"></tfoot>
				<tbody class="td_johap"></tbody>
			</table>
		</div>
	</div>
</div>