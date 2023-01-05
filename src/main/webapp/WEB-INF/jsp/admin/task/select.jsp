<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!-- admin_choice [s] -->
<div class="admin_chocie">
	<form name="frm_select" action="" method="post">
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="aucObjDsc" value="${params.aucObjDsc}" />
		<input type="hidden" name="regType" value="" />
		<input type="hidden" name="aucPrgSq" value="" />
		<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
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
		<li><a href="javascript:;" class="card-link btn_info_pop cow act" data-type="I">큰소 정보 입력</a></li>
		<li><a href="javascript:;" class="card-link btn_info_pop status act" data-type="S">호가 낙찰 등록</a></li>
		<li><a href="javascript:;" class="card-link btn_move act" data-type="LW">중량/예정가등록</a></li>
		<!--<li><a href="javascript:;" class="card-link btn_move act" data-type="W">중량등록</a></li>-->
		<!--<li><a href="javascript:;" class="card-link btn_move act" data-type="L">예정가등록</a></li>-->
		<li><a href="javascript:;" class="card-link btn_cow_list act" data-type="CM">출장우 관리</a></li>
		<li><a href="javascript:;" class="card-link btn_move act" data-type="AWL">일괄 등록</a></li>
<%-- 		<li><a href="javascript:pageMove('/office/task/menu?aucDt=${params.aucDt}&aucObjDsc=${params.aucObjDsc}&menuId=AR')" class="card-link act">일괄 등록</a></li> --%>
		<li><a href="javascript:pageMove('/office/task/menu?aucDt=${params.aucDt}&aucObjDsc=${params.aucObjDsc}&menuId=SM')" class="card-link act">조회 업무</a></li>
		<!-- 무진장(163) 축협만 노출 -->
		<c:if test="${params.NA_BZPLCNO == '163'}">
		<li><a href="javascript:;" class="card-link btn_move act" data-type="N">계류대변경</a></li>
<%-- 			<li><a href="javascript:pageMove('/office/task/menu?aucDt=${params.aucDt}&aucObjDsc=${params.aucObjDsc}&menuId=SG')" class="card-link act">스마트 계류</a></li> --%>
		</c:if>
<!-- 		<li><a href="javascript:;">낙찰 조회</a></li> -->
<!-- 		<li><a href="javascript:;">예비</a></li> -->
	</ul>
</div>
<!-- admin_choice [e] -->