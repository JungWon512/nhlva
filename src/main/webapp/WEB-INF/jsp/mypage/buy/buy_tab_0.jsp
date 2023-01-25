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
			<select name="searchAucObjDscBuy" id="">
				<option id="ra1" value="">전체</option>
				<option id="ra2" value="1" <c:if test="${inputParam.searchAucObjDscBuy eq '1'}">selected</c:if>>송아지</option>
				<option id="ra3" value="2" <c:if test="${inputParam.searchAucObjDscBuy eq '2'}">selected</c:if>>비육우</option>
				<option id="ra4" value="3" <c:if test="${inputParam.searchAucObjDscBuy eq '3'}">selected</c:if>>번식우</option>
			</select>	
		</div>
		<div class="btn">
			<button type="button" class="btn-refresh list_sch sch_buy"></button>
		</div>
	</div>
	<!-- //search-box e -->
	<div class="sum_table">
		<div>
			<dl class="sumBuy">
				<dt><p>전체</p></dt>
				<dd>
					<p class="cowCnt"><span class="ea">${empty buyCnt.CNT? '0' : buyCnt.CNT}</span>두</p>
				</dd>
			</dl>
			<dl>
				<dt><p>암</p></dt>
				<dd>
					<p class="cowCnt"><span class="ea">${empty buyCnt.CNT_SEX_W_F? '0' : buyCnt.CNT_SEX_W_F}</span>두</p>
				</dd>
			</dl>
			<dl>
				<dt><p>수</p></dt>
				<dd>
					<p class="cowCnt"><span class="ea">${empty buyCnt.CNT_SEX_M_F? '0' : buyCnt.CNT_SEX_M_F }</span>두</p>
				</dd>
			</dl>
			<dl>
				<dt><p>기타</p></dt>
				<dd>
					<p class="cowCnt"><span class="ea">${empty buyCnt.CNT_SEX_ETC_F? '0' : buyCnt.CNT_SEX_ETC_F}</span>두</p>
				</dd>
			</dl>
		</div>
	</div>
	<!-- //sum_table e -->
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
								<c:choose>
									<c:when test="${empty item.COW_SOG_WT || item.COW_SOG_WT <= 0}">0</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(item.COW_SOG_WT,'.')[0]}" type="number" />
									</c:otherwise>
								</c:choose>
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