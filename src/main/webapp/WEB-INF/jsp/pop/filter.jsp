<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="winpop winpop_filter">
	<div class="winpop-head ta-C">
		<button type="button" class="winpop_close ta-R"><span class="sr-only">윈도우 팝업 닫기</span></button>
		<h2 class="winpop_tit">검색 필터</h2>
	</div>
	<div class="filter-area">
		<ul class="radio_group step1">
			<li>
				<input type="radio" name="radioG1" checked id="ra1_1" value="range10"><label for="ra1_1">10일</label>
			</li>
			<li>
				<input type="radio" name="radioG1" id="ra1_2" value="range20"><label for="ra1_2">20일</label>
			</li>
			<li>
				<input type="radio" name="radioG1" id="ra1_3" value="range30"><label for="ra1_3">30일</label>
			</li>
		</ul>
		<ul class="radio_group step2">
			<li>
				<input type="radio" name="radioG2" id="ra2_1" value="A" checked><label for="ra2_1">전국</label>
			</li>
			<li>
				<input type="radio" name="radioG2" id="ra2_2"  value="R"><label for="ra2_2">지역</label>
			</li>
		</ul>
		<ul class="radio_group step3">
			<li>
				<input type="radio" name="radioG3" checked id="ra3_1" value="A31"><label for="ra3_1">경기/인천</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_2"  value="A33"><label for="ra3_2">강원</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_3"  value="A43"><label for="ra3_3">충북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_4"  value="A41"><label for="ra3_4">충남</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_5"  value="A63"><label for="ra3_5">전북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_6"  value="A61"><label for="ra3_6">전남</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_7"  value="A54"><label for="ra3_7">경북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_8"  value="A55"><label for="ra3_8">경남</label>
			</li>
		</ul>
		<div class="btn-area">
			<button type="button" id="btn-apply" class="btn-apply">적용</button>
		</div>
	</div>
</div>