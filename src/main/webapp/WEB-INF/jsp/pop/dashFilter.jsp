<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<form name="frm" action="" method="post">
	<input type="hidden" name="searchRaDate" 	id="searchRaDate" 		value="${inputParam.searchRaDate}">
	<input type="hidden" name="searchFlag" 		id="searchFlag" 			value="${inputParam.searchFlag}">
	<input type="hidden" name="searchPlace" 		id="searchPlace" 			value="${inputParam.searchPlace}">
</form>
<div class="winpop winpop_filter">
	<div class="winpop-head ta-C">
		<button type="button" class="winpop_close ta-R"><span class="sr-only">윈도우 팝업 닫기</span></button>
		<h2 class="winpop_tit">검색 필터</h2>
	</div>
	<div class="filter-area">
		<ul class="radio_group step1">
			<li>
				<input type="radio" name="radioG1" id="ra1_1" value="range10" <c:if test="${inputParam.searchRaDate eq 'range10' || inputParam.searchRaDate eq ''}">checked</c:if>><label for="ra1_1">10일</label>
			</li>
			<li>
				<input type="radio" name="radioG1" id="ra1_2" value="range20" <c:if test="${inputParam.searchRaDate eq 'range20'}">checked</c:if>><label for="ra1_2">20일</label>
			</li>
			<li>
				<input type="radio" name="radioG1" id="ra1_3" value="range30" <c:if test="${inputParam.searchRaDate eq 'range30'}">checked</c:if>><label for="ra1_3">30일</label>
			</li>
		</ul>
		<ul class="radio_group step2">
			<li>
				<input type="radio" name="radioG2" id="ra2_1" value="A" <c:if test="${inputParam.searchFlag eq 'A' || inputParam.searchFlag eq ''}">checked</c:if>><label for="ra2_1">전국</label>
			</li>
			<li>
				<input type="radio" name="radioG2" id="ra2_2"  value="R" <c:if test="${inputParam.searchFlag eq 'R'}">checked</c:if>><label for="ra2_2">지역</label>
			</li>
		</ul>
		<ul class="radio_group step3">
			<li>
				<input type="radio" name="radioG3" id="ra3_1" class="경기/인천" value="A31" <c:if test="${inputParam.searchPlace eq 'A31' || inputParam.searchPlace eq ''}">checked</c:if>><label for="ra3_1">경기/인천</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_2" class="강원" value="A33" <c:if test="${inputParam.searchPlace eq 'A33'}">checked</c:if>><label for="ra3_2">강원</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_3" class="충북" value="A43" <c:if test="${inputParam.searchPlace eq 'A43'}">checked</c:if>><label for="ra3_3">충북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_4" class="충남" value="A41" <c:if test="${inputParam.searchPlace eq 'A41'}">checked</c:if>><label for="ra3_4">충남</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_5" class="전북" value="A63" <c:if test="${inputParam.searchPlace eq 'A63'}">checked</c:if>><label for="ra3_5">전북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_6" class="전남" value="A61" <c:if test="${inputParam.searchPlace eq 'A61'}">checked</c:if>><label for="ra3_6">전남</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_7" class="경북" value="A54" <c:if test="${inputParam.searchPlace eq 'A54'}">checked</c:if>><label for="ra3_7">경북</label>
			</li>
			<li>
				<input type="radio" name="radioG3" id="ra3_8" class="경남" value="A55" <c:if test="${inputParam.searchPlace eq 'A55'}">checked</c:if>><label for="ra3_8">경남</label>
			</li>
		</ul>
		<div class="btn-area">
			<button type="button" id="btn-apply" class="btn-apply">적용</button>
		</div>
	</div>
</div>