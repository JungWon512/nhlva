<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!-- admin_choice [s] -->
<div class="admin_chocie">
	<form name="frm_select" action="" method="post">
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="aucObjDsc" value="${params.aucObjDsc}" />
		<input type="hidden" name="regType" value="" />
		<input type="hidden" name="aucPrgSq" value="" />
	</form>
	<h3>작업 선택</h3>
	<p>
		<c:choose>
			<c:when test="${empty params.aucDt}"></c:when>
			<c:when test="${fn:length(params.aucDt) eq 6}">${fn:substring(params.aucDt, 0, 4)}-${fn:substring(params.aucDt, 4, 6)}</c:when>
			<c:when test="${fn:length(params.aucDt) eq 8}">${fn:substring(params.aucDt, 0, 4)}-${fn:substring(params.aucDt, 4, 6)}-${fn:substring(params.aucDt, 6, 8)}</c:when>
		</c:choose>
	</p>
	<ul class="choice_area">
		<!--<li><a href="javascript:;" class="card-link btn_move act" data-type="W">중량등록</a></li>-->
		<!--<li><a href="javascript:;" class="card-link btn_move act" data-type="L">하한가등록</a></li>-->
		
<%-- 		<c:forEach items="${fn:split(johapData.SMS_BUFFER_2,',')}" var="item"> --%>
<!-- 			<li> -->
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${'I' eq item}">					 --%>
<%-- 						<a href="javascript:;" class="card-link btn_info_pop cow act" data-type="${item}"> --%>
<%-- 					</c:when> --%>
<%-- 					<c:when test="${'S' eq item}">					 --%>
<%-- 						<a href="javascript:;" class="card-link btn_info_pop status act" data-type="${item}"> --%>
<%-- 					</c:when>	 --%>
								
<%-- 					<c:otherwise> --%>
<%-- 						<a href="javascript:;" class="card-link btn_move act" data-type="${item}"> --%>
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose>					 --%>
<%-- 					<c:choose> --%>
<%-- 						<c:when test="${'I' eq item}">큰소 정보 입력</c:when> --%>
<%-- 						<c:when test="${'S' eq item}">호가 낙찰 등록</c:when> --%>
<%-- 						<c:when test="${'LW' eq item}">하한가/중량등록</c:when> --%>
<%-- 						<c:when test="${'AW' eq item}">중량일괄등록</c:when> --%>
<%-- 						<c:when test="${'AL' eq item}">하한가일괄등록</c:when> --%>
<%-- 						<c:when test="${'N' eq item}">계류대변경</c:when> --%>
<%-- 						<c:when test="${'SB' eq item}">낙찰결과 조회</c:when> --%>
<%-- 						<c:when test="${'SCOW' eq item}">출장우 리스트</c:when> --%>
<%-- 						<c:when test="${'SMCOW' eq item}">미감정 임신우</c:when> --%>
<%-- 					</c:choose>					 --%>
<!-- 				</a> -->
<!-- 			</li> -->
<%-- 		</c:forEach> --%>
		
			<li><a href="javascript:;" class="card-link btn_info_pop cow act" data-type="I">큰소 정보 입력</a></li>
			<li><a href="javascript:;" class="card-link btn_info_pop status act" data-type="S">호가 낙찰 등록</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="LW">하한가/중량등록</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="AW">중량일괄등록</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="AL">하한가일괄등록</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="N">계류대변경</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="SB">낙찰결과 조회</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="SCOW">출장우 리스트</a></li>
			<li><a href="javascript:;" class="card-link btn_move act" data-type="SMCOW">미감정 임신우</a></li>
<!-- 		<li><a href="javascript:;">낙찰 조회</a></li> -->
<!-- 		<li><a href="javascript:;">예비</a></li> -->
	</ul>
</div>
<!-- admin_choice [e] -->