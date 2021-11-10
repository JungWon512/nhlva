<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="modal-content">
	<button class="modal_popup_close" onclick="modalPopupClose('.pop_auction');return false;">닫기</button>
	<div class="tab_list">
		<ul>
			<li style="width: 50%;"><a href="javascript:;" class="act btnTabMove" data-tab-id="schedule">예정조회</a></li>
			<li style="width: 50%;"><a href="javascript:;" class="btnTabMove" data-tab-id="result">결과조회</a></li>
<!-- 			<li><a href="javascript:;" class="btnTabMove" data-tab-id="bid">응찰내역</a></li> -->
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area auction_list schedule_area">
		<!-- //list_search e -->
		<div class="list_table schedule_tb_mo pop_style">
			<div class="list_head pop_style">
				<dl>
					<dd class="num">번호</dd>
					<dd class="pd_ea">개체</dd>
					<dd class="pd_date">생년월</dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kpn">KPN</dd>
					<dd class="pd_pay">예정가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="pop_TermsBox mCustomScrollBox">
					<c:if test="${aucList.size() <= 0}">
						<li style="min-height:40px;">
							<dl>
								<dd>경매 예정이 등록되지 않았습니다.</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${aucList}" var="vo" varStatus="st">
						<li>
							<input type="hidden" class="naBzPlc" name="naBzPlc_${st.index }" value="${vo.NA_BZPLC}"/>
							<input type="hidden" class="aucDt" name="aucDt_${st.index }" value="${vo.AUC_DT}"/>
							<input type="hidden" class="aucObjDsc" name="aucObjDsc_${st.index }" value="${vo.AUC_OBJ_DSC}"/>
							<input type="hidden" class="oslpNo" name="oslpNo_${st.index }" value="${vo.OSLP_NO}"/>
							<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(vo.SBID_UPR,'.')[0]}"/>
							<input type="hidden" class="aucPrgSq" name="aucPrgSq_${st.index }" value="${vo.AUC_PRG_SQ}"/>
							<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}"/>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="pd_ea">
									<a href="javascript:;">${vo.SRA_INDV_AMNNO_FORMAT }</a>
								</dd>
								<dd class="pd_date">${vo.BIRTH_MO }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kpn">${vo.KPN_NO_STR }</dd>
								<dd class="pd_pay">${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
							</dl>
							<div class="pd_etc">
								<p>${vo.RMK_CNTN }</p>
							</div>
							<div class="pd_pav">
								<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(vo.SBID_UPR,'.')[0]}"/>
								<c:if test="${!empty params.loginNo}">
									<a href="javascript:;" class="${(vo.SBID_UPR eq '' || vo.SBID_UPR eq null )?'':'act'}">
										<span class="ico_pav">                            
											<c:choose>
											<c:when test="${vo.SBID_UPR eq '' || vo.SBID_UPR eq null }">
												찜가격
											</c:when>
											<c:otherwise>
												${fn:split(vo.SBID_UPR,'.')[0]}
											</c:otherwise>
											</c:choose>
										</span>
									</a>
								</c:if>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<div class="tab_area auction_list result_area" style="display:none;">
		<!-- //list_search e -->
		<div class="list_table auction_result">
			<div class="list_head pop_style">
				<dl>
					<dd class="num"><span class="w_view_in">경매</span>번호</dd>
					<dd class="name">출하주</dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
					<dd class="pd_pay1">최저가</dd>
					<dd class="pd_pay2">낙찰가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="pop_TermsBox mCustomScrollBox">
					<c:if test="${aucList.size() <= 0}">
						<li>
							<dl>
								<dd>경매 결과가 등록되지 않았습니다.</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${aucList}" var="vo" varStatus="st">
						<li>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="name">${vo.SRA_PDMNM }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg">${fn:split(vo.COW_SOG_WT,'.')[0]}</dd>
								<dd class="pd_pay1">${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${ fn:split(vo.SRA_SBID_UPR,'.')[0] }</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<!-- // -->
	<div class="tab_area auction_list bid_area" style="display:none;">
		<!-- //list_search e -->
		<div class="list_table auction_bid">
			<div class="list_head pop_style">
				<dl>
					<dd class="num"><span class="w_view_in">경매</span>번호</dd>
					<dd class="pd_ea">개체<span class="w_view_in">번호</span></dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_pay1">예정가</dd>
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_pay3">응찰가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="pop_TermsBox mCustomScrollBox">
					<c:if test="${aucList.size() <= 0}">
						<li>
							<dl>
								<dd>경매 응찰 내역이 없습니다.</dd>
							</dl>
						</li>
					</c:if>
	                <c:forEach items="${ aucList }" var="vo" varStatus="st">
						<li>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="pd_ea">${vo.SRA_INDV_AMNNO_FORMAT }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_pay1">${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
								<dd class="pd_pay2">${ fn:split(vo.SRA_SBID_UPR,'.')[0] }</dd>
								<dd class="pd_pay3">${ fn:split(vo.SRA_SBID_UPR,'.')[0] }</dd>
							</dl>
						</li>
	                </c:forEach>				
				</ul>
			</div>
		</div>
		<!-- //응찰내역 e -->
	</div>
	<!-- //tab_area e -->
</div>
<!-- //modal-content e -->
