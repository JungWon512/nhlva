<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<c:set var="aucCnt" value="0" />
<c:forEach items="${ bizList }" var="vo" varStatus="st">
	<c:if test ="${vo.CUR_AUC_CNT > 0 }">
		<c:set var="aucCnt" value="${aucCnt + vo.CUR_AUC_CNT}" />
	</c:if>
</c:forEach>
<div class="chk_step1">
	<div class="banner_box">
		<ul>
			<li>
				<a href="javascript:;">
					<img src="/static/images/guide/pc_banner.jpg" alt="" class="pc_banner">
					<img src="/static/images/guide/mo_banner.jpg" alt="" class="mo_banner">
				</a>
			</li>
		</ul>
	</div>
	<c:if test="${aucCnt>0 }">
		<div class="move-btn">
			<ul>
				<li style="width:100%;">
					<a href="javascript:pageMove('/district?auctingYn=Y');"><span>오늘경매 ( <b>${aucCnt }</b> )</span></a>
				</li>
			</ul>
		</div>
	</c:if>
	<h2><span>지역</span>을 <span>선택</span>해 주세요.<!-- img class="gps_fix" src="/static/images/guide/gps_fixed.svg" alt=""--></h2>
	<ul class="choice_area">
		<c:forEach items="${ bizList }" var="vo" varStatus="st">		
			<li class="${vo.NA_BZPLCLOC }">
				<a href="javascript:pageMove('/district?loc=${vo.NA_BZPLCLOC }');" class="${vo.AUC_YN > 0 ? 'act' : '' }">
					${vo.CUR_AUC_CNT > 0 ? '<span>경매일</span>' : '' } ${vo.NA_BZPLCLOC_NM }
				</a>
			</li>
			<c:if test ="${vo.CUR_AUC_CNT > 0 }">
				<c:set var="aucCnt" value="${aucCnt+1}" />
			</c:if>
		</c:forEach>
		
		
		<!-- UI 판 LAYOUT -->
<!-- 		<li><a href="javascript:pageMove('/district');" class="act"><span>경매중</span>활성화 :: class 확인</a></li> -->
<!-- 		<li><a href="javascript:pageMove('/district');"><span>경매중</span>정적</a></li> -->
<!-- 		<li><a href="javascript:pageMove('/district');"><span class="heart-eft">경매중</span>깜빡깜빡</a></li> -->
	</ul>
</div>
