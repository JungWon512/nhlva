<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<div class="search-area">
	<div class="search-box">
		<div class="date">
			<select name="searchDateAuc" id="searchDateAuc">        
				<option value="" ${(inputParam.searchDateAuc == null || inputParam.searchDateAuc == '')? 'selected' : ''}> 선택 </option>        
                <c:forEach items="${ dateList }" var="vo">
                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
                </c:forEach>                    
			</select>	
		</div>
		<div class="sort">
			<select name="searchAucObjDscAuc" id="searchAucObjDscAuc">
				<option value="">전체</option>
				<option value="1" ${inputParam.searchAucObjDsc eq '1' ? 'selected' : ''}>송아지</option>
				<option value="2" ${inputParam.searchAucObjDsc eq '2' ? 'selected' : ''}>비육우</option>
				<option value="3" ${inputParam.searchAucObjDsc eq '3' ? 'selected' : ''}>번식우</option>
				<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
					<option value="5" ${inputParam.searchAucObjDsc eq '5' ? 'selected' : ''}>염소</option>
				</c:if>
			</select>
		</div>
		<div class="btn">
			<button type="button" id="btn_refreshAuc" class="btn-refresh"></button>
		</div>
	</div>
	
	<div class="list_table schedule_tb">
		<div class="list_head">
			<dl>
				<dd class="date">경매일자</dd>
				<dd class="num">경매번호</dd>
				<dd class="name">출하주</dd>
				<dd class="pd_ea">개체번호</dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_date">생년월일</dd>
				<dd class="pd_kpn">KPN</dd>
				<dd class="pd_num1">계대</dd>
				<dd class="pd_num2">산차</dd>
				<dd class="pd_info">비고</dd>
				<dd class="pd_pay textNumber">예정가</dd>
				<dd class="pd_pav">찜가격</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="">
               	<c:forEach items="${ aucList }" var="vo" varStatus="st">
               	<input type="hidden" class="commitYn" name="commitYn_${st.index }" value="${vo.COMMIT_YN}"/>
					<li>
						<dl>
							<dd class="date">${vo.AUC_DT_STR}</dd>
							<dd class="num">${vo.AUC_PRG_SQ}</dd>
							<dd class="name">${vo.FTSNM}</dd>
							<dd class="pd_ea"><a href="javascript:;"><span class="ico_move">${vo.SRA_INDV_AMNNO_FORMAT_F}</span></a></dd>
							<dd class="pd_sex">${vo.INDV_SEX_C_NAME}</dd>
							<dd class="pd_date">${vo.BIRTH_STR}</dd>
							<dd class="pd_kpn">${vo.KPN_NO_STR}</dd>
							<dd class="pd_num1">${vo.SRA_INDV_PASG_QCN}</dd>
							<dd class="pd_num2">${vo.MATIME}</dd>
							<dd class="pd_info">${vo.RMK_CNTN}</dd>
							<dd class="pd_pay textNumber">							
								<c:choose>
									<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number"/>
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_pav">
								<a href="javascript:;">
									<span class="ico_pav">							
										<c:choose>
											<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
											<c:otherwise>
												<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number"/>
											</c:otherwise>
										</c:choose>
									</span>
								</a>
							</dd>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<div class="list_table schedule_tb_mo">
		<div class="list_head">
			<dl>
				<dd class="num">번호</dd>
				<c:choose>
					<c:when test="${inputParam.searchAucObjDsc eq '5'}">
						<dd class="name">출하주</dd>
					</c:when>
					<c:otherwise>
						<dd class="pd_ea">개체</dd>
					</c:otherwise>
				</c:choose>
				<dd class="pd_date"><a role="button" href="javascript:;" class="">생년월</a></dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_kpn">KPN</dd>
				<dd class="pd_pay textNumber"><a role="button" href="javascript:;" class="">예정가</a></dd>
			</dl>
		</div>
		<div class="list_body">
			<ul>
				<c:if test="${aucList.size() <= 0}">
					<li>
						<dl>
							<dd>
								출장우가 등록되지 않았습니다.
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
                    	<input type="hidden" class="ledSqno" name="ledSqno_${st.index }" value="${vo.LED_SQNO}"/>
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
		               	<input type="hidden" class="commitYn" name="commitYn_${st.index }" value="${vo.COMMIT_YN}"/>
						<dl>
							<dd class="num">${vo.AUC_PRG_SQ }</dd>
							<c:choose>
								<c:when test="${inputParam.searchAucObjDsc eq '5'}">
									<dd class="name">${vo.FTSNM}</dd>
								</c:when>
								<c:otherwise>
									<dd class="pd_ea">
										<a href="javascript:;">${vo.SRA_INDV_AMNNO_FORMAT_F }</a>
									</dd>
								</c:otherwise>
							</c:choose>
							<dd class="pd_date">${vo.BIRTH_MO }</dd>
							<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kpn">${vo.KPN_NO_STR }</dd>
							<dd class="pd_pay textNumber">
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
							<c:if test="${inputParam.authRole eq 'ROLE_BIDDER' && inputParam.loginNo != null && inputParam.loginNo != '' }">
	                            <a href="javascript:;" class="${(vo.SBID_UPR eq '' || vo.SBID_UPR eq null )?'':'act'}">
		                            <span class="ico_pav">                            
			                        	<c:choose>
			                        		<c:when test="${vo.SBID_UPR eq '' || vo.SBID_UPR eq null }">
			                        			찜가격
			                        		</c:when>
			                        		<c:otherwise>
			                        			<span class="ico_pav">
			                        				<fmt:formatNumber value="${fn:split(vo.SBID_UPR,'.')[0]}" type="number"  />
			                        			</span>
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