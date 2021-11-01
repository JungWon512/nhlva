<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<div class="auction_list">
	<input type="hidden" name="loginNo" value="${inputParam.loginNo}"/>
	<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
    <h3>경매예정</h3>
    <div class="list_search">
        <ul class="radio_group">
            <li>
                <input type="radio" name="searchAucObjDsc" checked id="ra1" value=""><label for="ra1">전체</label>
            </li>
            <li>
                <input type="radio" name="searchAucObjDsc" id="ra2" value="1" <c:if test="${inputParam.searchAucObjDsc eq '1'}">
                checked
                </c:if>><label for="ra2">송아지</label>
            </li>
            <li>
                <input type="radio" name="searchAucObjDsc" id="ra3" value="2" <c:if test="${inputParam.searchAucObjDsc eq '2'}">
                       checked
                </c:if>><label for="ra3">비육우</label>
            </li>
            <li>
                <input type="radio" name="searchAucObjDsc" id="ra4" value="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">
                       checked
                </c:if>><label for="ra4">번식우</label>
            </li>
        </ul>
        <ul class="sch_area">
            <li class="txt">
            	<input type="text" name="searchTxt" id="searchTxt" maxlength="4" value="${inputParam.searchTxt }" placeholder="개체"/>
            </li>
<!--             <li class="sort"> -->
<!--                 <select name="searchOrder" id="searchOrder"> -->
<!--                     <option value="">정렬</option> -->
<%--                     <option value="AUC_PRG_SQ"${inputParam.searchOrder eq 'AUC_PRG_SQ' ? 'selected': ''}">경매번호</option> --%>
<%--                     <option value="SRA_PDMNM" "${inputParam.searchOrder eq 'SRA_PDMNM' ? 'selected': ''}">출하주</option> --%>
<%--                     <option value="KPN_NO" ${inputParam.searchOrder eq 'KPN_NO' ? 'selected': ''}">KPN</option> --%>
<!--                 </select> -->
<!--             </li> -->
            <li class="date">
                <select name="searchDate" id="searchDate">
                <option value="" ${(inputParam.searchDate == null || inputParam.searchDate == '')? 'selected' : ''}> 선택 </option>                
                <c:forEach items="${ dateList }" var="vo">
                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
                </c:forEach>
                    
                </select>
            </li>
            <li class="btn">
                <a href="javascript:;" class="list_sch">검색</a>
            </li>
        </ul>
    </div>
    <!-- //list_search e -->
    <div class="list_downs">
        <ul>
            <li><a href="javascript:;" class="btn_print"><span class="ico_print">인쇄하기</span></a></li>
            <li><a href="javascript:;" class="btn_excel"><span class="ico_excel">엑셀다운</span></a></li>
        </ul>
    </div>
    <!-- //list_downs e -->
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
                <dd class="pd_pay">예정가</dd>
                <dd class="pd_pav">찜가격</dd>
            </dl>
        </div>
        <div class="list_body">
            <ul class="mCustomScrollBox">
				<c:if test="${salesList.size() <= 0}">
					<li>
						<dl>
							<dd>검색결과가 없습니다.</dd>
						</dl>
					</li>
				</c:if>
                <c:forEach items="${ salesList }" var="item" varStatus="st">
                    <li>
                    	<input type="hidden" class="naBzPlc" name="naBzPlc_${st.index }" value="${item.NA_BZPLC}"/>
                    	<input type="hidden" class="aucDt" name="aucDt_${st.index }" value="${item.AUC_DT}"/>
                    	<input type="hidden" class="aucObjDsc" name="aucObjDsc_${st.index }" value="${item.AUC_OBJ_DSC}"/>
                    	<input type="hidden" class="oslpNo" name="oslpNo_${st.index }" value="${item.OSLP_NO}"/>
                    	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(item.SBID_UPR,'.')[0]}"/>
                    	<input type="hidden" class="aucPrgSq" name="aucPrgSq_${st.index }" value="${item.AUC_PRG_SQ}"/>
                    	<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}"/>
                    	
                    	<input type="hidden" class="birth" name="birth_${st.index }" value="${item.BIRTH}"/>
                    	<input type="hidden" class="birthMonth" name="birthMonth_${st.index }" value="${item.BIRTH_MONTH}"/>
                    	<input type="hidden" class="sraPdmNm" name="sraPdmNm_${st.index }" value="${item.SRA_PDMNM}"/>
                    	<input type="hidden" class="indvSexCName" name="indvSexCName_${st.index }" value="${item.INDV_SEX_C_NAME}"/>
                    	<input type="hidden" class="sraIndvAmnno" name="sraIndvAmnno_${st.index }" value="${item.SRA_INDV_AMNNO}"/>
                    	<input type="hidden" class="kpnNoStr" name="kpnNoStr_${st.index }" value="${item.KPN_NO_STR}"/>
                    	<input type="hidden" class="mcowDsc" name="mcowDsc_${st.index }" value="${item.MCOW_DSC_NAME}"/>
                    	<input type="hidden" class="matime" name="matime_${st.index }" value="${item.MATIME}"/>
                    	
                    	<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${item.SRA_PD_RGNNM}"/>
                    	<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${item.SRA_INDV_PASG_QCN}"/>
                    	<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(item.SRA_SBID_UPR,'.')[0]}"/>
<%--                     	<input type="hidden" class="sraMwmnnm" name="sraMwmnnm_${st.index }" value="${item.SRA_MWMNNM}"/> --%>
                    	<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${item.RMK_CNTN}"/>
                    	<input type="hidden" class="cowSogWt" name="cowSogWt_${st.index }" value="${fn:split(item.COW_SOG_WT,'.')[0] }"/>
                    	
                        <dl>
                            <dd class="date">${ item.AUC_DT_STR }</dd>
                            <dd class="num">${ item.AUC_PRG_SQ }</dd>
                            <dd class="name">${ item.SRA_PDMNM}</dd>
                            <dd class="pd_ea"><a href="javascript:;"><span class="" fullstr="${ item.SRA_INDV_AMNNO}">${ item.SRA_INDV_AMNNO_FORMAT}</span></a></dd>
                            <dd class="pd_sex">${ item.INDV_SEX_C_NAME}</dd>
                            <dd class="pd_date">${item.BIRTH}</dd>
                            <dd class="pd_kpn">${item.KPN_NO_STR}</dd>
                            <dd class="pd_num1">${item.SRA_INDV_PASG_QCN}</dd>
                            <dd class="pd_num2">${item.MATIME}</dd>
                            <dd class="pd_info">${item.RMK_CNTN}</dd>
<%--                             <dd class="pd_pay">${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd> --%>
                            <dd class="pd_pay textNumber">${(item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0 ) ? '-' :  fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
                            <dd class="pd_pav">
                            	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(item.SBID_UPR,'.')[0]}"/>
                            	<c:if test="${inputParam.loginNo != null && inputParam.loginNo != '' }">
		                            <a href="javascript:;" class="${(item.SBID_UPR eq '' || item.SBID_UPR eq null )?'':'act'}">
			                            <span class="ico_pav">                            
				                        	<c:choose>
				                        		<c:when test="${item.SBID_UPR eq '' || item.SBID_UPR eq null }">
				                        			찜가격
				                        		</c:when>
				                        		<c:otherwise>
				                        			${fn:split(item.SBID_UPR,'.')[0]}
				                        		</c:otherwise>
				                        	</c:choose> 
			                            </span>
		                            </a>
	                            </c:if>
                            </dd>
                        </dl>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <!-- //list_table :: pc 전용 e -->
    <div class="list_table schedule_tb_mo">
        <div class="list_head">
            <dl>
                <dd class="num">번호</dd>
                <dd class="pd_ea">개체</dd>
                <dd class="pd_date">생년월</dd>
                <dd class="pd_sex">성별</dd>
                <dd class="pd_kpn">KPN</dd>
                <dd class="pd_pay">예정가</dd>
            </dl>
        </div>
        <div class="list_body">
            <ul class="mCustomScrollBox">
				<c:if test="${salesList.size() <= 0}">
					<li>
						<dl>
							<dd>검색결과가 없습니다.</dd>
						</dl>
					</li>
				</c:if>
                <c:forEach items="${ salesList }" var="item" varStatus="st">
                    <li>
                    	<input type="hidden" class="naBzPlc" name="naBzPlc_${st.index }" value="${item.NA_BZPLC}"/>
                    	<input type="hidden" class="aucDt" name="aucDt_${st.index }" value="${item.AUC_DT}"/>
                    	<input type="hidden" class="aucObjDsc" name="aucObjDsc_${st.index }" value="${item.AUC_OBJ_DSC}"/>
                    	<input type="hidden" class="oslpNo" name="oslpNo_${st.index }" value="${item.OSLP_NO}"/>
                    	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(item.SBID_UPR,'.')[0]}"/>
                    	<input type="hidden" class="aucPrgSq" name="aucPrgSq_${st.index }" value="${item.AUC_PRG_SQ}"/>
                    	<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}"/>
                    	
                    	
                    	<input type="hidden" class="birth" name="birth_${st.index }" value="${item.BIRTH}"/>
                    	<input type="hidden" class="birthMonth" name="birthMonth_${st.index }" value="${item.BIRTH_MONTH}"/>
                    	<input type="hidden" class="sraPdmNm" name="sraPdmNm_${st.index }" value="${item.SRA_PDMNM}"/>
                    	<input type="hidden" class="indvSexCName" name="indvSexCName_${st.index }" value="${item.INDV_SEX_C_NAME}"/>
                    	<input type="hidden" class="sraIndvAmnno" name="sraIndvAmnno_${st.index }" value="${item.SRA_INDV_AMNNO}"/>
                    	<input type="hidden" class="kpnNoStr" name="kpnNoStr_${st.index }" value="${item.KPN_NO_STR}"/>
                    	<input type="hidden" class="mcowDsc" name="mcowDsc_${st.index }" value="${item.MCOW_DSC_NAME}"/>
                    	<input type="hidden" class="matime" name="matime_${st.index }" value="${item.MATIME}"/>
                    	
                    	<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${item.SRA_PD_RGNNM}"/>
                    	<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${item.SRA_INDV_PASG_QCN}"/>
<%--                     	<input type="hidden" class="sraMwmnnm" name="sraMwmnnm_${st.index }" value="${item.SRA_MWMNNM}"/> --%>
                    	<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(item.SRA_SBID_UPR,'.')[0]}"/>
                    	<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${item.RMK_CNTN}"/>
                    	<input type="hidden" class="cowSogWt" name="cowSogWt_${st.index }" value="${fn:split(item.COW_SOG_WT,'.')[0] }"/>
                        <dl>
                            <dd class="num">${ item.AUC_PRG_SQ }</dd>
                            <dd class="pd_ea">
                                <a href="javascript:;"><span fullstr="${ item.SRA_INDV_AMNNO}">${ item.SRA_INDV_AMNNO_FORMAT}</span></a>                                
                            </dd>
                            <dd class="pd_date">${ item.BIRTH_MO}</dd>
                            <dd class="pd_sex">${ item.INDV_SEX_C_NAME}</dd>
                            <dd class="pd_kpn">${item.KPN_NO_STR}</dd>
<%--                             <dd class="pd_pay">${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd> --%>
                            <dd class="pd_pay textNumber">${(item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0 ) ? '-' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>                            
                        	
                        </dl>
						<div class="pd_etc">
							<p>${item.RMK_CNTN}</p>
						</div>
                        <div class="pd_pav">         
                        	<c:if test="${inputParam.loginNo != null && inputParam.loginNo != '' }">               	
								<a href="javascript:;" class="${(item.SBID_UPR eq '' || item.SBID_UPR eq null )?'':'act'}">
									<span class="ico_pav">
			                        	<c:choose>
			                        		<c:when test="${item.SBID_UPR eq '' || item.SBID_UPR eq null }">
			                        			찜하기
			                        		</c:when>
			                        		<c:otherwise>
			                        			${fn:split(item.SBID_UPR,'.')[0]}
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
<!-- //auction_list e -->