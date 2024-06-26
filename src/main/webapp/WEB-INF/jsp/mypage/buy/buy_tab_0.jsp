<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="search-area">
	<!-- //search-box s -->
	<div class="search-box">
		<div class="date">
			<select name="searchDateBuy" id="searchDateBuy">
                <option value=""> 선택 </option>
                <c:forEach items="${ dateList }" var="vo">
                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
                </c:forEach>
               </select>
		</div>
		<div class="sort">
		<%--
			<select name="searchAucObjDscBuy" id="">
				<option id="ra1" value="" <c:if test="${inputParam.searchAucObjDscBuy eq ''}">selected</c:if>>전체</option>
				<option id="ra2" value="1" <c:if test="${inputParam.searchAucObjDscBuy eq '1'}">selected</c:if>>송아지</option>
				<option id="ra3" value="2" <c:if test="${inputParam.searchAucObjDscBuy eq '2'}">selected</c:if>>비육우</option>
				<option id="ra4" value="3" <c:if test="${inputParam.searchAucObjDscBuy eq '3'}">selected</c:if>>번식우</option>
			<!-- 기타 가축 경매를 사용하는 경우 -->
			<c:if test="${!empty johapData.ETC_AUC_OBJ_DSC and !empty johapData.ETC_AUC_OBJ_DSC_NM}">
			<c:set var="etcList" value="${fn:split(johapData.ETC_AUC_OBJ_DSC, ',')}" />
			<c:set var="etcNmList" value="${fn:split(johapData.ETC_AUC_OBJ_DSC_NM, ',')}" />
			<c:forEach items="${etcList}" var="etc" varStatus="i">
				<option id="ra${etc}" value="${etc}" <c:if test="${inputParam.searchAucObjDscBuy eq etc}">selected</c:if>>${etcNmList[i.index]}</option>  
			</c:forEach>
			</c:if>
			</select>
		--%>
			<c:import url="/common/searchAucObjDsc">
				<c:param name="type"        value="select" />
				<c:param name="selectName"  value="searchAucObjDscBuy" />
				<c:param name="naBzplc"     value="${johapData.NA_BZPLC}" />
				<c:param name="selectValue" value="${inputParam.searchAucObjDscBuy}" />
			</c:import>
		</div>
		<div class="btn">
			<button type="button" class="btn-refresh list_sch sch_buy"></button>
		</div>
	</div>
	<!-- //search-box e -->
	<%@ include file="/WEB-INF/jsp/auction/common/auctionStat.jsp" %>

	<div class="list_table auction_result">
		<div class="list_head">
			<dl>
				<dd class="date">경매일자</dd>
				<dd class="num"><span class="w_view_in">경매</span>번호</dd>
				<dd class="name">출하주</dd>
				<dd class="pd_ea">개체번호</dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
				<dd class="pd_kpn">KPN</dd>
				<dd class="pd_num1">계대</dd>
				<dd class="pd_pay1">예정가</dd>
				<dd class="pd_pay2">낙찰가</dd>
				<dd class="pd_state">경매결과</dd>
				<dd class="pd_etc">비고</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="mCustomScrollBox bodyBuy">
				<c:if test="${buyList.size() <= 0}">
					<li>
						<dl>
							<dd>
								검색결과가 없습니다.
							</dd>
						</dl>
					</li>
				</c:if>
				<c:forEach items="${ buyList }" var="item" varStatus ="st">
					<li>
						<dl>
							<dd class="date">${ item.AUC_DT_STR }</dd>
							<dd class="num">${ item.AUC_PRG_SQ }</dd>
							<dd class="name">${ item.FTSNM }</dd>
							<dd class="pd_ea textNumber">${ item.SRA_INDV_AMNNO_FORMAT }</dd>
							<dd class="pd_sex">${ item.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg textNumber">
								<fmt:formatNumber value="${item.COW_SOG_WT}" var="COW_SOG_WT" type="number" pattern="#"/>
								${(empty COW_SOG_WT || COW_SOG_WT <= 0) ? 0 : COW_SOG_WT}
							</dd>
							<dd class="pd_kpn">${ item.KPN_NO_STR }</dd>
							<dd class="pd_num1">${ item.SRA_INDV_PASG_QCN }</dd>
							<dd class="pd_pay1 textNumber">
								<c:choose>
									<c:when test="${empty item.LOWS_SBID_LMT_AM || item.LOWS_SBID_LMT_AM <= 0}">0</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_pay2 textNumber">
								<c:choose>
									<c:when test="${empty item.SRA_SBID_UPR || item.SRA_SBID_UPR <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(item.SRA_SBID_UPR,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_state">${ item.SEL_STS_DSC_NAME }</dd>
							<dd class="pd_etc">${ item.RMK_CNTN ? item.RMK_CNTN : '-' }</dd>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>