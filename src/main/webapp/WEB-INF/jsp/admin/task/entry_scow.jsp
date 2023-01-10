<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="list_table"> 
	<div class="list_head">
		<dl>
			<dd class="col1">번호</dd>
			<dd class="col3">귀표</dd>
			<dd class="col4">출하자</dd>
			<dd class="col4">계류대</dd>
			<dd class="col4">중량</dd>
			<dd class="col4">예정가</dd>
			<dd class="col4">임신개월</dd>
		</dl>
	</div>
	<div class="list_body">
		<ul style="overflow-y:scroll;">
			<c:if test="${entryList.size() <= 0}">
				<li>
					<dl>
						<dd>검색결과가 없습니다.</dd>
					</dl>
				</li>
			</c:if>
			<c:forEach items="${entryList}" var="item" varStatus="st">
				<li id="${item.AUC_PRG_SQ}">
					<dl>
						<dd class="col1" data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-oslp-no="${item.OSLP_NO}" data-led-sqno="${item.LED_SQNO}">${item.AUC_PRG_SQ}</dd>
						<dd class="col3">${item.SRA_INDV_AMNNO_FORMAT}</dd>
						<dd class="col4">${item.FTSNM_ORI}</dd>
						<dd class="col4">${item.MODL_NO}</dd>
						<dd class="col4">${item.COW_SOG_WT}</dd>
						<dd class="col4">${item.LOWS_SBID_LMT_UPR}</dd>
						<dd class="col4">${item.PRNY_MTCN}</dd>
					</dl>
					<div class="pd_etc">
						<p>${!empty item.RMK_CNTN ? item.RMK_CNTN : ''}</p>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>