<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<select name="${param.selectName}" id="${param.selectName}" >
	<option id="ra0" value="">전체</option>	
	<c:forEach items="${aucObjDscList}" var="vo">
		<option id="ra${vo.AUC_OBJ_DSC }" value="${vo.AUC_OBJ_DSC}" <c:if test="${inputParam.searchAucObjDsc eq vo.AUC_OBJ_DSC}">selected</c:if>>${vo.AUC_OBJ_DSC_NM }</option>
	</c:forEach>
</select>