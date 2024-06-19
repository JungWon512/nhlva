<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
			
<div class="list_txts" style="text-align: center;">
	응찰 ${bidCnt.TOT_CNT}건 (암 : ${bidCnt.W_CNT}건 / 수 : ${bidCnt.M_CNT}건
	<c:if test="${bidCnt.NEUTER_CNT>0}">
		/ 거세 : ${bidCnt.NEUTER_CNT }건
	</c:if> 	
	<c:if test="${bidCnt.NOT_BIRTH_CNT>0}">
		/ 미경산 : ${bidCnt.NOT_BIRTH_CNT }건
	</c:if> 
	<c:if test="${bidCnt.NOT_NEUTER_CNT>0}">
		/ 비거세 : ${bidCnt.NOT_NEUTER_CNT }건
	</c:if> 
	<c:if test="${bidCnt.FREEMARTIN_CNT>0}">
		/ 프리마틴 : ${bidCnt.FREEMARTIN_CNT }건
	</c:if> 
	<c:if test="${bidCnt.COMMON_CNT>0}">
		/ 공통 : ${bidCnt.COMMON_CNT }건
	</c:if> 
	)
</div>

<!-- //list_search e -->
<div class="list_table auction_bid">
	<div class="list_head pop_style">
		<dl>
			<dd class="num"><span class="w_view_in">경매</span>번호</dd>
			<dd class="pd_ea">개체<span class="w_view_in">번호</span></dd>
			<dd class="pd_sex">성별</dd>
			<dd class="pd_pay1">예정가</dd>
			<dd class="pd_pay3">응찰가</dd>
			<dd class="pd_pay2">낙찰가</dd>
		</dl>
	</div>
	<div class="list_body pop_style">
		<ul class="mCustomScrollBox">
			<c:if test="${bidList.size() <= 0}">
				<li>
					<dl>
						<dd>
							경매결과가 등록되지 않았습니다.
						</dd>
					</dl>
				</li>
			</c:if>
               <c:forEach items="${ bidList }" var="vo" varStatus="st">
				<li class="${vo.SEL_STS_DSC_NAME eq '본인낙찰' ?'act':''}">
					<dl>
						<dd class="num">${vo.AUC_PRG_SQ }</dd>
						<dd class="pd_ea">${vo.SRA_INDV_AMNNO_FORMAT }</dd>
						<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
						<dd class="pd_pay1">							
							<c:choose>
								<c:when test="${vo.LOWS_SBID_LMT_UPR eq '' || vo.LOWS_SBID_LMT_UPR == null || vo.LOWS_SBID_LMT_UPR <= 0}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />	
								</c:otherwise>
							</c:choose>
						</dd>
						<dd class="pd_pay3">								
							<c:choose>
								<c:when test="${vo.ATDR_AM eq '' || vo.ATDR_AM == null || vo.ATDR_AM <= 0}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${fn:split(vo.ATDR_AM,'.')[0]}" type="number" />
								</c:otherwise>
							</c:choose>
						</dd>
						<dd class="pd_pay2">								
							<c:choose>
								<c:when test="${vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0}">-</c:when>
								<c:otherwise>
									<fmt:formatNumber value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}" type="number" />
								</c:otherwise>
							</c:choose>
						</dd>
					</dl>
				</li>
               </c:forEach>				
		</ul>
	</div>
</div>