<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<form name="frm" action="" method="post">
	<input type="hidden" name="place" value="<c:out value="${inputParam.place}" />" />
	<input type="hidden" name="naBzplc" value="<c:out value="${inputParam.naBzplc}" />" />
	<input type="hidden" name="aucDt" value="<c:out value="${inputParam.aucDt}" />" />
	<input type="hidden" name="aucObjDsc" value="<c:out value="${inputParam.aucObjDsc}" />" />
	<input type="hidden" name="sraIndvAmnno" value="<c:out value="${inputParam.sraIndvAmnno}" />" />
	<input type="hidden" name="aucPrgSq" value="<c:out value="${inputParam.aucPrgSq}" />" />
	<input type="hidden" name="oslpNo" value="<c:out value="${inputParam.oslpNo}" />" />
	<input type="hidden" name="ledSqno" value="<c:out value="${inputParam.ledSqno}" />" />
	<input type="hidden" name="bidPopYn" value="<c:out value="${inputParam.bidPopYn}" />" />
	<input type="hidden" name="resultPopYn" value="<c:out value="${inputParam.resultPopYn}" />" />
	<input type="hidden" name="tabId" value="<c:out value="${inputParam.tabId}" />" />
</form>
<form name="frmDetail" action="" method="post">
	<input type="hidden" name="place" value="<c:out value="${param.place}" />" />
	<input type="hidden" name="naBzplc" value="<c:out value="${param.naBzplc}" />" />
	<input type="hidden" name="sraIndvAmnno" value="" />
	<input type="hidden" name="title" value="" />
	<input type="hidden" name="parentObj" value="" />
</form>
<div class="winpop cow-detail">
		<button type="button" class="${inputParam.bidPopYn eq 'N' ? 'winpop_close' : 'winpop_back'}"><span class="sr-only">윈도우 팝업 닫기</span>
			<h2 class="winpop_tit">${subheaderTitle}</h2>
		</button>
		<c:set var="tabTotCnt" value="0"/>
		<c:forEach items="${ tabList }" var="item" varStatus="st">
			<c:if test="${(inputParam.aucObjDsc ne '5' and inputParam.aucObjDsc ne '6') and ((inputParam.bidPopYn ne 'N' and item.SIMP_C ne '3' and item.SIMP_C ne '4' and item.VISIB_YN eq '1') or (inputParam.bidPopYn eq 'N' and item.SIMP_C ne '4' and item.VISIB_YN eq '1'))}">
				<c:set var="tabTotCnt" value="${tabTotCnt+1}"/>
			</c:if>
		</c:forEach>
		<div class="tab_list item-${tabTotCnt+1}">
		<ul>
			<li><a href="javascript:;" class="${param.tabId eq '0' or empty param.tabId ? 'act':'' }" data-tab-id="0">${(inputParam.aucObjDsc ne '5' and inputParam.aucObjDsc ne '6')?'출장우':'출장염소'}</a></li>
			<c:forEach items="${ tabList }" var="item" varStatus="st">
				<c:if test="${(inputParam.aucObjDsc ne '5' and inputParam.aucObjDsc ne '6') and (item.VISIB_YN eq '1' && item.SIMP_C ne '4')}">
					<c:if test="${!(item.SIMP_C eq '3' and inputParam.bidPopYn ne 'N')}">
						<li><a href="javascript:;" class="${(param.tabId eq item.SIMP_C)?'act':'' }" data-tab-id="${item.SIMP_C }">${item.SIMP_CNM }</a></li>
					</c:if>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area not mo-pd info"></div>		
	<!-- //tab_area e -->
</div>