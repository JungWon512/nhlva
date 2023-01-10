<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<c:set var="aucCnt" value="0" />
<c:forEach items="${ bizList }" var="vo" varStatus="st">
	<c:if test ="${vo.CUR_AUC_CNT > 0 }">
		<c:set var="aucCnt" value="${aucCnt + vo.CUR_AUC_CNT}" />
	</c:if>
</c:forEach>
<div class="chk_step1 pc-mt0">
	<div class="main-tab">
		<ul>
			<li class="on">
				<a href="javascript:pageMove('/home')">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
			</li>
			<li>
				<a href="javascript:pageMove('/dashboard')">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
			</li>
		</ul>
	</div>
	<div class="banner_box">
		<ul>
			<li>
				<a href="javascript:;">
					<img src="/static/images/guide/pc_banner.jpg" alt="" class="pc_banner">
					<img src="/static/images/guide/new_mo_banner.jpg" alt="" class="mo_banner">
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
	</ul>
	<div class="bottom-link"><a href="javascript:pageMove('/schedule');">전국 가축시장 경매안내</a></div>
</div>
