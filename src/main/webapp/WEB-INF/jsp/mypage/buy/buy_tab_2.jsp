<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="auction_date auction_account">
	<div class="date_board">
		<div class="area-top">
			<input type="hidden" id="searchDateState" name="searchDateState" value="${ inputParam.searchDateState }"/>
			<dl class="date_top">
				<dl>
					<dt>${ title }</dt>
					<dd id="prev" class="btn_chg btn_prev"><a href="javascript:;">이전</a></dd>
					<dd id="next" class="btn_chg btn_next disabled"><a href="javascript:;">다음</a></dd>
				</dl>
			</dl>
			<div class="btns">
				<p style="color:red; font-size:18px; margin-bottom: 10px;">*최근 6개월 내 정산서만 검색 가능합니다.</p>
				<button type="button" id="johap" class="btn_johap_flag act"><c:out value="${ johapData.AREANM }축협" /></button>
				<button type="button" id="all" class="btn_johap_flag">전체축협</button>
			</div>
		</div>
		<p class="txt-result"><span class="fc-red">${calendarList.size()}</span>건이 조회되었습니다.</p>
		<div class="date_bottom">
			<ul>
				<c:if test="${ calendarList.size() <= 0 }">
					<li>
						<dl class="no_date">
							<h4>정산내역이 없습니다.</h4>
						</dl>
					</li>
				</c:if>
				<c:forEach items="${ calendarList }" var="vo">
					<li id="${vo.AUC_DT}_${vo.AUC_OBJ_DSC_NAME}_${vo.NA_BZPLCNO}" class="move_info buy_tab3">
						<dl>
							<dt>
								<p>${vo.AUC_DAY }</p>
								<span>${vo.AUC_WEEK_DAY }요일</span>
							</dt>
							<dd>
								<p class="auc-tit">${vo.CLNTNM } ${fn:replace(vo.AUC_OBJ_DSC_NAME,'일괄','번식우,송아지,비육우') } 경매</p>
								<ul class="auc-info">
									<li>
										<strong>낙찰두수</strong>
										<span><em class="fc-blue">${vo.COW_CNT}</em>두</span>
									</li>
									<li>
										<strong>정산금액</strong>
										<fmt:formatNumber value="${vo.SRA_SBID_AM + vo.SRA_TR_FEE}" type="number" var="TOT_SBID_AM"/>
										<span><em class="fc-red">${empty TOT_SBID_AM ? '0' : TOT_SBID_AM}</em>원</span>
									</li>
								</ul>
							</dd>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!-- //date_bottom e -->
	</div>
	<!-- //date_board e -->
</div>
<!-- //auction_date e -->