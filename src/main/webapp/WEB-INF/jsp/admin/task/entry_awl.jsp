<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!-- 일괄 등록 -->
<div class="list_table"> 
	<div class="list_head">
		<dl>
			<dd class="col1">번호</dd>
			<dd class="col2 awl" id="AW" style="display: none;"><span class="txt_org">중량</span></dd>
			<dd class="col2 awl" id="AL" style="display: none;"><span class="txt_org">예정가</span></dd>
			<dd class="col3">귀표</dd>
			<dd class="col4">출하자</dd>
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
						<dd class="col2 awl" id="AW" style="display: none;">
							<input type="text" name="cowSogWt" id="cowSogWt${item.AUC_PRG_SQ}" class="onlyNumber noPop" value="${item.COW_SOG_WT ne 0 && item.COW_SOG_WT ne null ? fn:split(item.COW_SOG_WT,'.')[0] : ''}" maxlength="4" pattern="\d*" inputmode="numeric"/>
						</dd>
						<dd class="col2 awl" id="AL" style="display: none;">
							<input type="text" name="firLowsSbidLmtAm" id="firLowsSbidLmtAm${item.AUC_PRG_SQ}" class="onlyNumber noPop" value="${item.LOWS_SBID_LMT_UPR ne 0 && item.LOWS_SBID_LMT_UPR ne null ? fn:split(item.LOWS_SBID_LMT_UPR,'.')[0] : ''}" maxlength="5" pattern="\d*" inputmode="numeric"/>
						</dd>
						<dd class="col3">${item.SRA_INDV_AMNNO_FORMAT}</dd>
						<dd class="col4">${item.FTSNM_ORI}</dd>
					</dl>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>