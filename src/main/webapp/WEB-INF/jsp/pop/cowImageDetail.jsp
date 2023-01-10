<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<div class="winpop">
	<button type="button" class="winpop_close"><span class="sr-only">윈도우 팝업 닫기</span></button>
	<h2 class="winpop_tit">출장우 이미지</h2>
	<div class="cow-imgBox">
	
		<c:forEach items="${ imgList }" var="item" varStatus="st">
			<div class="item" name="${item.NA_BZPLC}_${item.OSLP_NO}"><img src="${item.FILE_URL}" alt=""></div>
		</c:forEach>
	</div>
</div>