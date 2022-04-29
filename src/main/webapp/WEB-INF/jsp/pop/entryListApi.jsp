<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<input type="hidden" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>

<div id="" class="entryList pop_auction">
	<div class="tab_list">
		<ul class="tab_2">
			<c:choose>
				<c:when test="${johapData.AUC_DSC eq '1' }">
					<li><a href="javascript:;" class="${(empty inputParam.tabAct or inputParam.tabAct eq 'sold')?'act':'' } sold" data-tab-id='sold'>낙찰내역</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="javascript:;" class="${(empty inputParam.tabAct or inputParam.tabAct eq 'bid')?'act':'' } bid" data-tab-id='bid'>응찰내역</a></li>
				</c:otherwise>				
			</c:choose>
			<li><a href="javascript:;" class="auc ${(inputParam.tabAct eq 'auc')?'act':'' }" data-tab-id='auc'>출장우조회</a></li>
			
		</ul>
	</div>
		<!-- //tab_list e -->
	<div class="tab_area auction_list auc" style="display:none;">
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
				<ul class="mCustomScrollBox">
					<c:if test="${aucList.size() <= 0}">
						<li>
							<dl>
								<dd>
									경매예정이 등록되지 않았습니다.
								</dd>
							</dl>
						</li>
					</c:if>
	                <c:forEach items="${ aucList }" var="vo" varStatus="st">
	                    <li>	                    
	                    	<input type="hidden" class="naBzPlc" name="naBzPlc_${st.index }" value="${vo.NA_BZPLC}"/>
	                    	<input type="hidden" class="aucDt" name="aucDt_${st.index }" value="${vo.AUC_DT}"/>
	                    	<input type="hidden" class="aucObjDsc" name="aucObjDsc_${st.index }" value="${vo.AUC_OBJ_DSC}"/>
	                    	<input type="hidden" class="oslpNo" name="oslpNo_${st.index }" value="${vo.OSLP_NO}"/>
	                    	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(vo.SBID_UPR,'.')[0]}"/>
	                    	<input type="hidden" class="aucPrgSq" name="aucPrgSq_${st.index }" value="${vo.AUC_PRG_SQ}"/>
	                    	<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${empty vo.LOWS_SBID_LMT_AM or vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}"/>
	                    	
	                    	
	                    	<input type="hidden" class="birth" name="birth_${st.index }" value="${vo.BIRTH}"/>
	                    	<input type="hidden" class="sraPdmNm" name="sraPdmNm_${st.index }" value="${vo.FTSNM}"/>
	                    	<input type="hidden" class="indvSexCName" name="indvSexCName_${st.index }" value="${vo.INDV_SEX_C_NAME}"/>
	                    	<input type="hidden" class="sraIndvAmnno" name="sraIndvAmnno_${st.index }" value="${vo.SRA_INDV_AMNNO}"/>
	                    	<input type="hidden" class="kpnNoStr" name="kpnNoStr_${st.index }" value="${vo.KPN_NO_STR}"/>
	                    	<input type="hidden" class="mcowDsc" name="mcowDsc_${st.index }" value="${vo.MCOW_DSC_NAME}"/>
	                    	<input type="hidden" class="matime" name="matime_${st.index }" value="${vo.MATIME}"/>
	                    	
	                    	<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${vo.SRA_PD_RGNNM}"/>
	                    	<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${vo.SRA_INDV_PASG_QCN}"/>
	                    	<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}"/>
	                    	<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${vo.RMK_CNTN}"/>
	                    	
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
										<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number"/>
										</c:otherwise>
									</c:choose>
								</dd>
							</dl>
							<div class="pd_etc">
								<p>${vo.RMK_CNTN }</p>
							</div>
							<div class="pd_pav">
                            	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(vo.SBID_UPR,'.')[0]}"/>
                            	<c:if test="${inputParam.loginNo != null && inputParam.loginNo != '' }">
		                            <a href="javascript:;" class="${(vo.SBID_UPR eq '' || vo.SBID_UPR eq null )?'':'act'}">
			                            <span class="ico_pav">                            
				                        	<c:choose>
				                        		<c:when test="${vo.SBID_UPR eq '' || vo.SBID_UPR eq null }">
				                        			찜가격
				                        		</c:when>
				                        		<c:otherwise>
				                        			<fmt:formatNumber value="${fn:split(vo.SBID_UPR,'.')[0]}" type="number"  />
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
	<div class="tab_area auction_list sold" style="display:none;">
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
					<dd class="pd_sex">성별</dd>
					<dd class="pd_kg">중량<span class="w_view_in">(kg)</span></dd>
					<dd class="pd_pay1">최저가</dd>
					<dd class="pd_pay2">낙찰가</dd>
				</dl>
			</div>
			<div class="list_body pop_style">
				<ul class="mCustomScrollBox">
					<c:if test="${soldList.size() <= 0}">
						<li>
							<dl>
								<dd>
									경매결과가 등록되지 않았습니다.
								</dd>
							</dl>
						</li>
					</c:if>
	                <c:forEach items="${ soldList }" var="vo" varStatus="st">
						<li>
							<dl>
								<dd class="num">${vo.AUC_PRG_SQ }</dd>
								<dd class="name">${vo.FTSNM }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_kg"><fmt:formatNumber value="${empty vo.COW_SOG_WT or vo.COW_SOG_WT <= 0 ? '0' : fn:split(vo.COW_SOG_WT,'.')[0]}" type="number" /></dd>
								<dd class="pd_pay1"><fmt:formatNumber value="${vo.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" /></dd>
								<dd class="pd_pay2"><fmt:formatNumber value="${ fn:split(vo.SRA_SBID_UPR,'.')[0] }" type="number" /></dd>
							</dl>
						</li>
	                </c:forEach>	
				</ul>
			</div>
		</div>
	</div>
		<!-- // -->
	<div class="tab_area auction_list bid" style="display:none;">
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
					<dd class="pd_dsc">유형</dd>
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
								<dd class="pd_dsc">${vo.AUC_OBJ_DSC_NAME }</dd>
								<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
								<dd class="pd_pay1"><fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" /></dd>
								<dd class="pd_pay3"><fmt:formatNumber value="${fn:split(vo.ATDR_AM,'.')[0]}" type="number" /></dd>
								<dd class="pd_pay2"><fmt:formatNumber value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}" type="number" /></dd>
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