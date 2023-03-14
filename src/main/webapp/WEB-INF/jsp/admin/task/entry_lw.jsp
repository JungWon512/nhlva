<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="list_table">
	<div class="list_head">
		<dl>
			<dd class="col1">번호</dd>
			<dd class="col2"><span class="txt_org">중량</span></dd>
			<dd class="col2"><span class="txt_org">예정가</span></dd>
			<dd class="col3">귀표</dd>
			<dd class="col4">출하자</dd>
			<dd class="col4">수정</dd>
		</dl>
	</div>
	<div class="list_body">
		<ul style="overflow-y:scroll;" class="mCustomScrollBox">
			<c:if test="${entryList.size() <= 0}">
				<li>
					<dl>
						<dd>검색결과가 없습니다.</dd>
					</dl>
				</li>
			</c:if>
			<c:forEach items="${entryList}" var="item" varStatus="st">
				<li id="${item.AUC_PRG_SQ}">
					<span><button type="button" class="btn_modify" >수정</button></span>
					<dl>
						<dd class="col1" data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-oslp-no="${item.OSLP_NO}" data-led-sqno="${item.LED_SQNO}">${item.AUC_PRG_SQ}</dd>
						<dd class="col2">
							<c:choose>
								<c:when test="${empty item.COW_SOG_WT or item.COW_SOG_WT <= 0}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${fn:split(item.COW_SOG_WT,'.')[0]}" type="number" />
								</c:otherwise>
							</c:choose>
						</dd>
						<dd class="col2">
							<c:choose>
								<c:when test="${item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
								</c:otherwise>
							</c:choose>
						</dd>
						<dd class="col3">${item.SRA_INDV_AMNNO_FORMAT}</dd>
						<dd class="col4">${item.FTSNM_ORI}</dd>
						<dd class="col4 col5"></dd>
	<!-- 									<dd class="col4 col5"><button type="button" class="btn_modify">수정</button></dd> -->
					</dl>
					<div class="pd_etc">
						<p>${!empty item.RMK_CNTN ? item.RMK_CNTN : ''}</p>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>