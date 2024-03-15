<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<select name="${param.selectName}" id="${param.selectName}" >
    <option id="ra1" value="">전체</option>
    <option id="ra2" value="1" <c:if test="${param.selectValue eq '1'}">selected</c:if>>송아지</option>
    <option id="ra3" value="2" <c:if test="${param.selectValue eq '2'}">selected</c:if>>비육우</option>
    <option id="ra4" value="3" <c:if test="${param.selectValue eq '3'}">selected</c:if>>번식우</option>  
<!-- 기타 가축 경매를 사용하는 경우 -->
<c:if test="${!empty johapData.ETC_AUC_OBJ_DSC and !empty johapData.ETC_AUC_OBJ_DSC_NM}">
<c:set var="etcList" value="${fn:split(johapData.ETC_AUC_OBJ_DSC, ',')}" />
<c:set var="etcNmList" value="${fn:split(johapData.ETC_AUC_OBJ_DSC_NM, ',')}" />
<c:forEach items="${etcList}" var="etc" varStatus="i">
    <option id="ra${etc}" value="${etc}" <c:if test="${param.selectValue eq etc}">selected</c:if>>${etcNmList[i.index]}</option>  
</c:forEach>
</c:if>
</select>