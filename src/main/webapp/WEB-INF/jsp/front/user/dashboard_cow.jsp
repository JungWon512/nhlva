<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<div class="tit-area">
			<h2 class="sec-tit">가축시장 출장우</h2>
		</div>
		<div class="dark-box calendar-box sty-round">
			<div class="c-months">
				<div class="has-btn">
					<button class="btn-prev"><span class="sr-only">이전</span></button>
					2022년 11월
					<button class="btn-next"><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="white-box">
			<div class="cow-board-top v2">
				<strong>25,690 두</strong>  <span class="fc-red"> ▲ 1,500두</span>
			</div>
			<div style="margin: 20px auto;max-width:500px;">
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
							<th scope="col" class="ta-C">10월</th>
							<th scope="col" class="ta-C on">11월</th>
							<th scope="col" class="ta-C">12월</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th scope="row">송아지</td>
							<td class="ta-C">6,500</td>
							<td class="ta-C on">6,500</td>
							<td class="ta-C">6,500</td>
						</tr>
						<tr>
							<th scope="row">비육우</th>
							<td class="ta-C">6,500</td>
							<td class="ta-C on">6,500</td>
							<td class="ta-C">6,500</td>
						</tr>
						<tr>
							<th scope="row">번식우</th>
							<td class="ta-C">6,500</td>
							<td class="ta-C on">6,500</td>
							<td class="ta-C">6,500</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<hr class="hr-line" />
	<div class="sec-board">
		<h2 class="sec-tit">지역별 출장우 현황 <span class="s-txt">연도별</span></h2>
		<div class="sort-area">
			<div class="btnBox">
				<button class="on">암송아지</button>
				<button>숫송아지</button>
				<button>비육우</button>
				<button>번식우</button>
			</div>
			<hr class="hr-line"/>
			<div class="btnBox sty-blue">
				<button>전체</button>
				<button class="on">4~5개월</button>
				<button>6~7개월</button>
			</div>
		</div>
		<div class="white-box">
			<ul class="board-number average">
				<li class="sbidPop">
					<dl class="on">
						<dt>경기.인천</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>강원</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>충남</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>충북</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>전남</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>전북</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>경남</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>경북</dt>
						<dd class="won">999,999원</dd>
						<dd>9,999두</dd>
					</dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit">월별 출장우 현황</h2>
		<div class="white-box">
			<canvas id="myCharSample1"></canvas>
		</div>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
const ctx1 = document.getElementById('myCharSample1');

new Chart(ctx1, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '전년도 출장우',
				data: [35, 49, 79, 92, 76, 80, 60, 37, 62, 50, 93, 88],
				borderColor: '#37a2eb',
				background:'',
				type: 'line',
				order: 0
			},
			{
				label: '출장우',
				data: [65, 59, 80, 82, 56, 55, 40, 22, 57, 70, 103, 110],
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
		}
	},
});
const ctx2 = document.getElementById('myPieSample1');

new Chart(ctx2, {
		type: 'pie',
	data: {
		labels: ['송아지', '비육우', '번식우'],
		datasets: [
			{
				data: ['50','20','30'],
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
</script>
