<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
</form>
<div class="board-main pt-0">
	<div class="sec-board">
		<h2 class="sec-tit">가축시장 도축장시세</h2>
		<div class="btnBox btnSex">
			<button class="on">암</button>
			<button>수</button>
		</div>
		<div class="dark-box calendar-box">
			<div class="c-years">
				<div>2022년 11월</div>
			</div>
			<div class="c-days bd-left">
				<div class="has-btn">
					<button class="btn-prev"><span class="sr-only">이전</span></button>
					24 일
					<button class="btn-next"><span class="sr-only">다음</span></button>
				</div>
			</div>
		</div>
		<div class="sort-grade">
			<select name="" id="">
				<option value="">1++A등급</option>
				<option value="">1+A등급</option>
			</select>	
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="market-price col-1">
				<dl class="pan">
					<dt>평균가격</dt>
					<dd class="fw-bold">999,999 원</dd>
					<dd class="per fc-red">▲ 0.1%</dd>
				</dl>
			</div>
			<div class="market-price col-2">
				<dl class="pan">
					<dt>최고가격</dt>
					<dd class="fw-bold">999,999 원</dd>
					<dd class="per fc-red">▲ 0.1%</dd>
				</dl>
				<dl class="pan">
					<dt>최저가격</dt>
					<dd class="fw-bold">999,999 원</dd>
					<dd class="per fc-red">▲ 0.1%</dd>
				</dl>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div class="market-price col-1">
				<dl class="pan">
					<dt>거래두수</dt>
					<dd class="fw-bold">999,999 두</dd>
					<dd class="fc-blue">▼ 0.1 %</dd>
				</dl>
			</div>
		</div>
	</div>
	<div class="sec-board">
		<div class="white-box">
			<div style="margin-bottom: 30px;">
				<canvas id="myCharSample5"></canvas>
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
					<tbody class="no-bd">
						<tr>
							<td>지역명</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
						<tr>
							<td>지역명</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
						<tr>
							<td>지역명</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
						<tr>
							<td>지역명</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
						<tr>
							<td>지역명</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
					</tbody>
					<tfoot class="bd-top">
						<tr>
							<td>평균</td>
							<td class="ta-C">22,944</td>
							<td class="ta-C">241</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</div>
