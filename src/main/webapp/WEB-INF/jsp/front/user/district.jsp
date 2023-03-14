<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!--begin::Container-->
<div class="chk_step2 pc-mt0">
	<div class="main-tab">
		<ul>
			<li class="on">
				<a href="javascript:pageMove('/home')">경매<span class="sub-txt">인터넷 <br>스마트 경매</span></a>
			</li>
			<li>
				<a href="javascript:pageMove('/dashboard/main')">현황<span class="sub-txt">전국 <br>가축시장현황</span></a>
			</li>
		</ul>
	</div>
	<div class="back_area">
		<a href="javascript:pageMove('/home');"><img src="/static/images/guide/ico_back.svg">전국 가축시장 보기</a>
	</div>
	<c:if test="${locList.size() <= 0}">
		<div class="list_none">
			<span>경매중인 지역이 없습니다.</span>
		</div>
	</c:if>
	<ul class="choice_area">
		<c:forEach items="${ locList }" var="vo" varStatus="st">	
			<li>
				<a href="javascript:pageMove('/main?place=${vo.NA_BZPLCNO }',true);" class="${vo.AUC_YN==1 ? 'act':'' }" >				
					${(vo.AUC_YN==1 && vo.AUC_CNT > 0) ? '<span class="tag">경매일</span>' : '' }
					${fn:replace(vo.AREANM ,'.','<br/>')}
				</a>
			</li>
		</c:forEach>
	</ul>
<!-- 	<div class="btn_more"> -->
<!-- 		<a href="javascript:;">더보기</a> -->
<!-- 	</div> -->
</div>

<!--end::Container-->