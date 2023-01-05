<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="radioG1" 		id="radioG1" 			value="">
	<input type="hidden" name="radioG2" 		id="radioG2" 			value="">
	<input type="hidden" name="radioG3" 		id="radioG3" 			value="">
</form>
<div class="chk_step1 dash-tit">
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
	<div class="board-main">
		<div class="sec-board">
			<div class="tit-area">
				<h2 class="sec-tit">최근 가축시장 시세</h2>
				<a href="javascript:;" id="dashboard_filter" class="btn-more">필터 ></a>
			</div>
			<div class="period-area">
				최근 10일 (10/1~10/1 까지)
				<strong>전국</strong>
			</div>
			<div class="btnBox">
				<button class="on">송아지</button>
				<button>비육우</button>
				<button>번식우</button>
			</div>
			<ul class="list-market">
				<li>
					<dl class="item">
						<dt>전체</dt>
						<dd>
							<div class="price">3,850,000 원 <span class="per fc-blue">▼ 0.1 %</span></div>
							<div class="num">2,753 두 <span class="per fc-red">▲ 0.1 %</span></div>
						</dd>
					</dl>
				</li>
				<li>
					<dl class="item">
						<dt>4 ~ 5 개월</dt>
						<dd>
							<div class="price">3,850,000 원 <span class="per fc-blue">▼ 0.1 %</span></div>
							<div class="num">2,753 두 <span class="per fc-red">▲ 0.1 %</span></div>
						</dd>
					</dl>
				</li>
				<li>
					<dl class="item">
						<dt>6 ~ 7 개월</dt>
						<dd>
							<div class="price">3,850,000 원 <span class="per fc-blue">▼ 0.1 %</span></div>
							<div class="num">2,753 두 <span class="per fc-red">▲ 0.1 %</span></div>
						</dd>
					</dl>
				</li>
			</ul>
		</div>
		<div class="sec-board">
			<div class="btnBox">
				<button class="on">전체</button>
				<button>4~5개월</button>
				<button>6~7개월</button>
			</div>
			<h2 class="sec-tit">지역별 평균 낙찰가</h2>
			<div class="white-box">
				<canvas id="myChart1"></canvas>
			</div>
		</div>
		<div class="sec-board">
			<div class="tit-area">
				<h2 class="sec-tit">금주의 TOP 3</h2>
				<a href="javascript:;" class="btn-top3">Top 10</a>
			</div>
			<ol class="list-top10">
				<li>
					<dl class="union">
						<dt><img src="/static/images/guide/v2/sample01.jpg" alt=""></dt>
						<dd class="name">횡성축협</dd>
						<dd class="change fc-red">+ 51,600 원</dd>
						<dd class="price fc-red">7,506,000 원</dd>
						<dd class="info">암송아지<i class="dash"></i>7개월<i class="dash"></i>혈통</dd>
					</dl>
				</li>
				<li>
					<dl class="union">
						<dt><img src="/static/images/guide/v2/sample01.jpg" alt=""></dt>
						<dd class="name">영주축협</dd>
						<dd class="change fc-red">+ 41,600 원</dd>
						<dd class="price fc-red">6,403,000 원</dd>
						<dd class="info">암송아지<i class="dash"></i>7개월<i class="dash"></i>혈통</dd>
					</dl>
				</li>
				<li>
					<dl class="union">
						<dt><img src="/static/images/guide/v2/sample01.jpg" alt=""></dt>
						<dd class="name">울산축협</dd>
						<dd class="change fc-blue">- 21,600 원</dd>
						<dd class="price fc-red">4,253,000 원</dd>
						<dd class="info">암송아지<i class="dash"></i>7개월<i class="dash"></i>혈통</dd>
					</dl>
				</li>
			</ol>
		</div>
		<hr class="hr-line"/>
		<div class="sec-board">
			<div class="tit-area">
				<h2 class="sec-tit">가축 시장의 더 많은 정보들 ...</h2>
			</div>
			<div class="list-link">
				<div>
					<a href="javascript:;">출장우 현황</a>
					<a href="javascript:;">도축장 시세</a>
					<a href="javascript:;">사용 안내</a>
				</div>
				<div>
					<a href="javascript:;" class="row2">낙찰시세</a>
					<a href="javascript:;">가축시장<br>참가현황</a>
				</div>
			</div>
			<div class="login-btn-area">
				<a href="javascript:;" class="btn-login">로그인 ></a>
				<a href="javascript:;" class="btn-logout">로그아웃 ></a>
			</div>
			<div class="list-link is-bottom">
				<a href="javascript:;">가축시장 접속자 현황</a>
				<a href="javascript:;">[추가] 축협용 통계 + 1</a>
				<a href="javascript:;">[추가] 축협용 통계 + 1</a>
			</div>
		</div>
	</div>
</div>
