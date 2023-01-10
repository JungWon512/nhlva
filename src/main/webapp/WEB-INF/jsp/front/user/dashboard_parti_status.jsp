<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="dark-box calendar-box sty-round">
			<div class="c-months">
				<div class="has-btn">
					<button class="btn-prev"><span class="sr-only">이전</span></button>
					2022년 11월
					<button class="btn-next"><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="white-box border-dot">
			<div class="gap-area">
				<div class="gap-box">
					<em>총 출장두수</em>
					<strong>415 두</strong>
				</div>
				<div>
					<span class="gap ico-red up">+ 32</span>
					<!-- <span class="gap ico-red down">- 999</span> -->
				</div>
				<div class="gap-box">
					<em>낙찰두수</em>
					<strong>382 두</strong>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="doughnut">
				<div class="graph">
					<canvas id="myDoughnutSample1"></canvas>
					<span class="total">전체<br>415두</span>
				</div>
				<div class="txt">
					낙찰율
					<strong class="fc-red">95 %</strong>
				</div>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit">가축시장 낙찰율 현황 <span class="s-txt d-block">2022년 11월 기준</span></h2>
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
				<tbody>
					<tr>
						<td>지역명</td>
						<td>90</td>
						<td>10</td>
						<td>90%</td>
					</tr>
					<tr>
						<td>지역명</td>
						<td>90</td>
						<td>10</td>
						<td>90%</td>
					</tr>
					<tr>
						<td>지역명</td>
						<td>90</td>
						<td>10</td>
						<td>90%</td>
					</tr>
					<tr>
						<td>지역명</td>
						<td>90</td>
						<td>10</td>
						<td>90%</td>
					</tr>
					<tr>
						<td>지역명</td>
						<td>90</td>
						<td>10</td>
						<td>90%</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
const ctx3 = document.getElementById('myDoughnutSample1');

new Chart(ctx3, {
	type: 'doughnut',
	data: {
		datasets: [{
			data: [15, 85],
			backgroundColor: ['#cac3fa', '#8b7feb'],
			borderWidth: 0
		}]
	},
});
</script>
