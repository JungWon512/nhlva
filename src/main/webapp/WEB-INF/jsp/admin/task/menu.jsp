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
		<c:choose>
			<c:when test="${params.menuId == 'AR'}">
				<li><a href="javascript:;" class="card-link btn_move act" data-type="AW">중량일괄등록</a></li>
				<li><a href="javascript:;" class="card-link btn_move act" data-type="AL">예정가일괄등록</a></li>
			</c:when>
			<c:when test="${params.menuId == 'SM'}">
				<li><a href="javascript:;" class="card-link btn_move act" data-type="SB">낙찰결과 조회</a></li>
				<li><a href="javascript:;" class="card-link btn_move act" data-type="SCOW">출장우 리스트</a></li>
				<li><a href="javascript:;" class="card-link btn_move act" data-type="SMCOW">미감정 임신우</a></li>
			</c:when>
			<c:when test="${params.menuId == 'SG'}">
				<li><a href="javascript:;" class="card-link btn_move act" data-type="N">계류대변경</a></li>
				<li><a href="javascript:;" class="card-link btn_move act" data-type="F">경매번호 표출</a></li>
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose>
	</ul>
</div>
<!-- admin_choice [e] -->