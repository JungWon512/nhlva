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
			<c:forEach items="${ regionList }" var="vo" varStatus="i">
				<li>
					<input type="radio" name="radioG3" id="${vo.NA_BZPLCLOC}" class="${vo.LOCNM}" value="${vo.NA_BZPLCLOC}" <c:if test="${inputParam.searchPlace eq vo.NA_BZPLCLOC}">checked</c:if>><label for="${vo.NA_BZPLCLOC}">${vo.LOCNM}</label>
				</li>
			</c:forEach>
		</ul>
		<div class="btn-area">
			<button type="button" id="btn-apply" class="btn-apply">적용</button>
		</div>
	</div>
</div>