<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="winpop winpop_top10">
	<div class="inner">
		<div class="winpop-head ta-C">
			<button type="button" class="winpop_close ta-R"><span class="sr-only">윈도우 팝업 닫기</span></button>
			<h2 class="winpop_tit">금주의 TOP 10</h2>
		</div>
		<ol class="list-top10">
			<c:choose>
				<c:when test="${ recentDateTopList.size() > 0 }">
					<c:forEach items="${ recentDateTopList }" var="vo" varStatus="i">
						<li>
							<dl class="union">
								<dt>	<img src="https://kr.object.ncloudstorage.com/smartauction-storage/logo/${vo.NA_BZPLC}.png" onerror="this.src='/static/images/guide/v2/sample_logo.jpg'" /></dt>
								<dd class="name">${ vo.CLNTNM }</dd>
								<fmt:formatNumber value="${empty vo.AMT ? 0 : vo.AMT}" type="number" var="AMT"/>
								<c:choose>
									<c:when test="${ vo.AMT < 0}">
										<dd class="change fc-blue">${AMT} 원</dd>
									</c:when>
									<c:otherwise>
										<dd class="change fc-red">+ ${AMT} 원</dd>
									</c:otherwise>
								</c:choose>
								<fmt:formatNumber value="${vo.SBID_AMT}" type="number" var="SBID_AMT"/>
								<dd class="price fc-red">${empty SBID_AMT ? '0' : SBID_AMT} 원</dd>
								<c:choose>
									<c:when test="${ vo.AUC_OBJ_DSC eq '1'}">
										<dd class="info">${ vo.INDV_SEX_C_NM }${ vo.AUC_OBJ_DSC_NM }<i class="dash"></i>${ vo.MONTH_C }개월<i class="dash"></i>${ vo.RG_DSC_NAME }</dd>
									</c:when>
									<c:otherwise>
										<dd class="info">${ vo.AUC_OBJ_DSC_NM }<i class="dash"></i>${ vo.MONTH_C } 개월<i class="dash"></i>${ vo.RG_DSC_NAME }</dd>										
									</c:otherwise>
								</c:choose>
							</dl>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li style="font-size:20px;">금주의 TOP 10이 없습니다.</li>
				</c:otherwise>
			</c:choose>
		</ol>
	</div>
	<div class="btn-area">
		<button type="button" class="btn-top10-confirm">확인</button>
	</div>
</div>