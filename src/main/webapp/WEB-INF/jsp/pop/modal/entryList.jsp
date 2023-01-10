<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="modal-content entryList">
	<button class="modal_popup_close" onclick="modalPopupClose('.pop_auction');return false;">닫기</button>
	<div class="tab_list">
		<ul>		
			<c:choose>
				<c:when test="${johapData.AUC_DSC eq '1' }">
					<li style="width: 50%;"><a href="javascript:;" class="${(empty params.tabAct or params.tabAct eq 'result')?'act':'' } btnTabMove" data-tab-id="result">낙찰내역</a></li>
					<li style="width: 50%;"><a href="javascript:;" class="btnTabMove ${(params.tabAct eq 'schedule')?'act':'' }" data-tab-id="schedule">출장우조회</a></li>
				</c:when>
				<c:otherwise>
					<li style="width: 33%;"><a href="javascript:;" class="${(empty params.tabAct or params.tabAct eq 'bid')?'act':'' } btnTabMove" data-tab-id="bid">응찰내역</a></li>
					<li style="width: 33%;"><a href="javascript:;" class="${(params.tabAct eq 'result')?'act':'' } btnTabMove" data-tab-id="result">낙찰내역</a></li>
					<li style="width: 33%;"><a href="javascript:;" class="btnTabMove ${(params.tabAct eq 'schedule')?'act':'' }" data-tab-id="schedule">출장우조회</a></li>
				</c:otherwise>				
			</c:choose>			
<!-- 			<li><a href="javascript:;" class="btnTabMove" data-tab-id="bid">응찰내역</a></li> -->
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area auction_list schedule_area" style="display:none;">
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
							<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${empty vo.LOWS_SBID_LMT_AM or vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}"/>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="pd_ea">
									<a href="javascript:;">${vo.SRA_INDV_AMNNO_FORMAT }</a>
								</dd>
								<dd class="pd_date">${vo.BIRTH_MO }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kpn">${vo.KPN_NO_STR }</dd>
								<dd class="pd_pay">											
									<c:choose>
										<c:when test="${vo.LOWS_SBID_LMT_UPR eq '' || vo.LOWS_SBID_LMT_UPR == null || vo.LOWS_SBID_LMT_UPR <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
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
												<fmt:formatNumber value="${fn:split(vo.SBID_UPR,'.')[0]}" type="number" />
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
			<div class="sum_table">
				<div>
					<dl>
						<dt><p>암</p></dt>
						<dd>
							<p class="cowCnt">${buyCnt.CNT_SEX_W}두</p>
						</dd>
					</dl>
					<dl>
						<dt><p>수</p></dt>
						<dd>
							<p class="cowCnt">${buyCnt.CNT_SEX_M}두</p>
						</dd>
					</dl>
					<dl>
						<dt><p>기타</p></dt>
						<dd>
							<p class="cowCnt">${buyCnt.CNT_SEX_ETC}두</p>
						</dd>
					</dl>
				</div>
				<div class="sumTxt">
					<fmt:formatNumber value="${totPrice.SRA_SBID_AM}" type="number" var="TOT_SRA_SBID_AM"/>
					<p>총 금액 : ${empty TOT_SRA_SBID_AM?'0':TOT_SRA_SBID_AM} 원</p>
				</div>
			</div>
			<div class="list_head pop_style">
				<dl>
					<dd class="num"><span class="w_view_in">경매</span>번호</dd>
					<dd class="name">출하주</dd>
					<dd class="pd_dsc">유형</dd>
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
					<dd class="pd_pay1">예정가</dd>
					<dd class="pd_pay2">낙찰가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="pop_TermsBox mCustomScrollBox">
					<c:if test="${soldList.size() <= 0}">
						<li>
							<dl>
								<dd>경매 결과가 등록되지 않았습니다.</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${soldList}" var="vo" varStatus="st">
						<li>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="name">${vo.FTSNM }</dd>
								<dd class="pd_dsc">${vo.AUC_OBJ_DSC_NAME }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg"><fmt:formatNumber value="${empty vo.COW_SOG_WT or vo.COW_SOG_WT <= 0 ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]}" type="number" /></dd>
								<dd class="pd_pay1">
									<c:choose>
										<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
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
	</div>
	<!-- // -->
	<div class="tab_area auction_list bid_area" style="display:none;">
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
					<dd class="pd_pay2">낙찰가</dd>
					<dd class="pd_pay3">응찰가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="pop_TermsBox mCustomScrollBox">
					<c:if test="${bidList.size() <= 0}">
						<li>
							<dl>
								<dd>경매 응찰 내역이 없습니다.</dd>
							</dl>
						</li>
					</c:if>
	                <c:forEach items="${ bidList }" var="bvo" varStatus="st">
						<li class="${bvo.SEL_STS_DSC_NAME eq '본인낙찰' ?'act':''}">
							<dl>
								<dd class="num">${bvo.AUC_PRG_SQ }</dd>
								<dd class="pd_ea">${bvo.SRA_INDV_AMNNO_FORMAT }</dd>								
								<dd class="pd_sex">${bvo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_pay1">							
									<c:choose>
										<c:when test="${bvo.LOWS_SBID_LMT_UPR eq '' || bvo.LOWS_SBID_LMT_UPR == null || bvo.LOWS_SBID_LMT_UPR <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(bvo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay2">							
									<c:choose>
										<c:when test="${bvo.SRA_SBID_UPR eq '' || bvo.SRA_SBID_UPR == null || bvo.SRA_SBID_UPR <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(bvo.SRA_SBID_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
								<dd class="pd_pay3">							
									<c:choose>
										<c:when test="${bvo.ATDR_AM eq '' || bvo.ATDR_AM == null || bvo.ATDR_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(bvo.ATDR_AM,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
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
<script>
var tabId = $('div.tab_list li a.act').attr('data-tab-id');
switch(tabId){
	case 'schedule' : $('div.schedule_area').show(); break;
	case 'result': $('div.result_area').show(); break;
	case 'bid': $('div.bid_area').show(); break;
	default:modalPopupClose('.pop_auction'); break;
}
</script>
