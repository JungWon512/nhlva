<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<form name="frm" action="" method="post">
	<input type="hidden" name="place" value="${param.place}" />
	<input type="hidden" name="naBzplc" value="${param.naBzplc}" />
	<input type="hidden" name="aucDt" value="${param.aucDt}" />
	<input type="hidden" name="aucObjDsc" value="${param.aucObjDsc}" />
	<input type="hidden" name="sraIndvAmnno" value="${param.sraIndvAmnno}" />
	<input type="hidden" name="aucPrgSq" value="${param.aucPrgSq}" />
	<input type="hidden" name="oslpNo" value="${param.oslpNo}" />
	<input type="hidden" name="ledSqno" value="${param.ledSqno}" />
	<input type="hidden" name="bidPopYn" value="${inputParam.bidPopYn}" />
</form>
<form name="frmDetail" action="" method="post">
	<input type="hidden" name="place" value="${param.place}" />
	<input type="hidden" name="naBzplc" value="${param.naBzplc}" />
	<input type="hidden" name="sraIndvAmnno" value="" />
	<input type="hidden" name="title" value="" />
</form>
<div class="winpop cow-detail">
	<button type="button" class="winpop_close"><span class="sr-only">윈도우 팝업 닫기</span></button>
	<h2 class="winpop_tit">${subheaderTitle}</h2>
	<div class="tab_list item-${tabList[0].TOT_CNT+1}">
		<ul>
			<li><a href="javascript:;" class="${param.tabId eq '0' or empty param.tabId ? 'act':'' }" data-tab-id="0">출장우</a></li>
			<c:forEach items="${ tabList }" var="item" varStatus="st">
				<c:if test="${item.VISIB_YN eq '1'}">
					<c:if test="${!(item.SIMP_C eq '3' and inputParam.bidPopYn eq 'Y')}">
						<li><a href="javascript:;" class="${(param.tabId eq item.SIMP_C)?'act':'' }" data-tab-id="${item.SIMP_C }">${item.SIMP_CNM }</a></li>
					</c:if>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area not mo-pd"></div>
	<!-- //tab_area e -->
</div>