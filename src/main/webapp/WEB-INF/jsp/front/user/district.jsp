<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<!--begin::Container-->
<div class="chk_step2">
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
			<li style="${st.index > 5?'display:none;':'' }">
				<a href="javascript:pageMove('/main?place=${vo.NA_BZPLCNO }&aucYn=${vo.AUC_YN==1 ? 'Y':'N' }',true);" class="${vo.AUC_YN==1 ? 'act':'' }" >				
					${vo.AUC_CNT > 0 ? '<span>경매일</span>' : '' }
					${fn:replace(vo.AREANM ,'.','<br/>')}
				</a>
			</li>
		</c:forEach>
	</ul>
	<div class="btn_more">
		<a href="javascript:;">더보기</a>
	</div>
</div>

<!--end::Container-->