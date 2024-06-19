<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

 <div class="sum_table">
	<div>
	    <c:if test="${empty inputParam.searchAucObjDsc}">
			<dl>
				<dt><p>구분</p></dt>
				<c:forEach items="${ buyCnt }" var="obj" varStatus="st">
					<dd><p>${obj.AUC_OBJ_DSC_NM }</p></dd>
				</c:forEach>
			</dl>
		</c:if>
		<dl>
			<dt><p>전체</p></dt>
			<c:forEach items="${ buyCnt }" var="obj" varStatus="st">
				<dd><p><span class="ea">${obj.TOT_CNT }</span>두</p></dd>
			</c:forEach>
		</dl>
		<dl>
			<dt><p>암</p></dt>
			<c:forEach items="${ buyCnt }" var="obj" varStatus="st">
				<dd><p><span class="ea">${obj.W_CNT }</span>두</p></dd>
			</c:forEach>
		</dl>
		<dl>
			<dt><p>수</p></dt>
			<c:forEach items="${ buyCnt }" var="obj" varStatus="st">
				<dd><p><span class="ea">${obj.M_CNT }</span>두</p></dd>
			</c:forEach>
		</dl>
		<dl>
			<dt><p>기타</p></dt>
			<c:forEach items="${ buyCnt }" var="obj" varStatus="st">
				<dd><p><span class="ea">${obj.ETC_CNT }</span>두</p></dd>
			</c:forEach>
		</dl>
	</div>
</div>