<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<c:choose>
<c:when test="${param.type eq 'select'}">
	<select name="${param.selectName}" id="${param.selectName}" >
		<option id="ra0" value="">전체</option>	
		<c:forEach items="${aucObjDscList}" var="vo">
			<option id="ra${vo.AUC_OBJ_DSC }" value="${vo.AUC_OBJ_DSC}" <c:if test="${param.selectValue eq vo.AUC_OBJ_DSC}">selected</c:if>>${vo.AUC_OBJ_DSC_NM}</option>
		</c:forEach>
	</select>
</c:when>
<c:when test="${param.type eq 'radio'}">
	<ul class="radio_group">
		<li style="display: ${!fn:contains('0,1,2,3', param.selectValue) ? 'none' : ''}">
			<input type="radio" name="${param.selectName}" id="ra0" value="0" ${param.selectValue eq '0' or empty param.selectValue ? 'checked' : '' } />
			<label for="ra0">전체</label>
		</li>
	<c:forEach items="${aucObjDscList}" var="vo">
		<li style="display: ${(fn:contains('0,1,2,3', param.selectValue) and fn:contains('0,1,2,3', vo.AUC_OBJ_DSC)) or (!fn:contains('0,1,2,3', param.selectValue) and !fn:contains('0,1,2,3', vo.AUC_OBJ_DSC)) ? '' : 'none'}">
			<input type="radio" name="${param.selectName}" id="ra${vo.AUC_OBJ_DSC}" value="${vo.AUC_OBJ_DSC}" ${param.selectValue eq vo.AUC_OBJ_DSC ? 'checked' : ''} />
			<label for="ra${vo.AUC_OBJ_DSC}">${vo.AUC_OBJ_DSC_NM}</label>
		</li>
	</c:forEach>
	</ul>
</c:when>
</c:choose>