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
			<h2 class="sec-tit">가축시장 낙찰가 시세</h2>
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
		<div class="sort-area">
			<div class="btnBox">
				<button class="on">송아지</button>
				<button>비육우</button>
				<button>번식우</button>
			</div>
			<hr class="hr-line"/>
			<div class="btnBox sty-blue">
				<button class="on">전체</button>
				<button>4~5개월</button>
				<button>6~7개월</button>
			</div>
		</div>
		<div class="white-box">
			<div class="cow-board-top">
				<strong>5,999 두</strong>  ▼ 516두<br>(6~7개월령)
			</div>
			<ul class="board-number col-3">
				<li>
					<dl>
						<dt>최고</dt>
						<dd class="won">3,900,000 원</dd>
						<dd><span class="fc-red">+ 50000 원</span></dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>평균</dt>
						<dd class="won">3,800,000 원</dd>
						<dd><span class="fc-blue">- 50000 원</span></dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt>최저</dt>
						<dd class="won">3,500,000 원</dd>
						<dd><span class="fc-blue">- 50000 원</span></dd>
					</dl>
				</li>
			</ul>
			<div style="margin-top: 20px;">
				<canvas id="myChartSample4"></canvas>
			</div>
			<div class="gap-area mt50">
				<div class="gap-box">
					<em>낙찰예정가</em>
					<strong>666,666원</strong>
				</div>
				<div>
					<span class="gap up">+ 999</span>
					<!-- <span class="gap down">- 999</span> -->
				</div>
				<div class="gap-box">
					<em>낙찰평균가</em>
					<strong>999,999원</strong>
				</div>
			</div>
			<hr class="hr-line" />
			<h2 class="sec-tit">지역별 평균 시세</h2>
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
		<h2 class="sec-tit">월별 낙찰가 현황</h2>
		<div class="white-box">
			<canvas id="myCharSample2"></canvas>
		</div>
	</div>
	<div class="sec-board">
		<h2 class="sec-tit">월별 출장우 현황</h2>
		<div class="white-box">
			<canvas id="myCharSample3"></canvas>
		</div>
	</div>
</div>
<script src="/static/js/common/chart/chart.js"></script>
<script>
const ctx2 = document.getElementById('myCharSample2');

new Chart(ctx2, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '평균낙찰가',
				data: [65, 59, 80, 82, 56, 55, 40, 22, 57, 70, 103, 110],
				borderColor: '#a5dfdf',
				backgroundColor: '#a5dfdf',
				type: 'bar',
				borderWidth: 0,
				order: 1
			},
			{
				label: '평균에상가',
				data: [35, 49, 79, 92, 76, 80, 60, 37, 62, 50, 93, 88],
				borderColor: '#37a2eb',
				type: 'line',
				order: 0
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
const ctx3 = document.getElementById('myCharSample3');

new Chart(ctx3, {
	type: 'bar',
	data: {
		labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
		datasets: [
			{
				label: '출장우',
				data: [65, 59, 80, 82, 56, 55, 40, 22, 57, 70, 103, 110],
				borderColor: '#ffb1c1',
				backgroundColor: '#ffb1c1',
				type: 'bar',
				borderWidth: 0,
				order: 1
			},
			{
				label: '전년도 출장우',
				data: [35, 49, 79, 92, 76, 80, 60, 37, 62, 50, 93, 88],
				borderColor: '#37a2eb',
				type: 'line',
				order: 0
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
const ctx4 = document.getElementById('myChartSample4');

new Chart(ctx4, {
	type: 'bar',
	data: {
		labels: ['최고', '최저', '평균'],
		datasets: [
			{
				label: '현재',
				data: [70, 80, 90],
				backgroundColor: '#9ad0f5',
				borderWidth: 0
			},
			{
				label: '이전',
				data: [60, 70, 80],
				backgroundColor: '#ffb1c1',
				borderWidth: 0
			}
		]
	},
	options: {
		scales: {
			y: {
				beginAtZero: true
			},
			x: {
				beginAtZero: true
			}
		}
	}
});
</script>
