<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<form name="frm_select" action="" method="post">
	<input type="hidden" name="place" value="${param.place}" />
	<input type="hidden" name="naBzplc" value="" />
	<input type="hidden" name="aucDt" value="" />
	<input type="hidden" name="aucObjDsc" value="" />
	<input type="hidden" name="sraIndvAmnno" value="" />
	<input type="hidden" name="aucPrgSq" value="" />
	<input type="hidden" name="oslpNo" value="" />
	<input type="hidden" name="ledSqno" value="" />
	<input type="hidden" id="bidPopYn" name="bidPopYn" value="N" />
</form>

<input type="hidden" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" class="rDate"  id="startDate" name="startDate"  value="${ inputParam.startDate }">
<input type="hidden" class="rDate" id="endDate" name="endDate"  value="${inputParam.endDate }">
<input type="hidden" class="price"  id ="lowPrice" name="lowPrice"  value="${inputParam.lowPrice}">
<input type="hidden" class="price"  id ="highPrice" name="highPrice" value="${inputParam.highPrice}" >
<input type="hidden" id="searchTxt"  name="searchTxt" class="searchTxt" value="${inputParam.searchTxt}"/>
<input type="hidden" id="birthUpDown"  name="birthUpDown"  value="${inputParam.birthUpDown}"/>
<input type="hidden" id="priceUpDown"  name="priceUpDown"  value="${inputParam.priceUpDown}"/>
<input type="hidden" id="numUpDown"  name="numUpDown"  value="${inputParam.numUpDown}"/>
<input type="hidden" id="upDownFlag"  name="upDownFlag"  value="${inputParam.upDownFlag}"/>



		<!-- //header e -->
<!-- 		<section class="contents"> -->
		
		<div class="auction_list">
  			  <h3>출장우 조회</h3>
			<div class="search-area">
				<div class="search-box">
					<div class="date">
						<select name="searchDate" id="searchDate">
							<option value="" ${(inputParam.searchDate == null || inputParam.searchDate == '')? 'selected' : ''}> 선택 </option>    
							<c:forEach items="${ dateList }" var="vo">
			                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
			                </c:forEach>     
						</select>	
					</div>
					<div class="sort">
						<select name="searchAucObjDsc" id="searchAucObjDsc" >
							<option id="ra1" value="">전체</option>
							<option id="ra2" value="1" <c:if test="${inputParam.searchAucObjDsc eq '1'}">selected</c:if>>송아지</option>
							<option id="ra3" value="2" <c:if test="${inputParam.searchAucObjDsc eq '2'}">selected</c:if>>비육우</option>
							<option id="ra4" value="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">selected</c:if>>번식우</option>  
						</select>	
					</div>
					<div class="kpn">
						<div class="filter-text">
							<input type="text" id="searchTxt"  name="searchTxt" class="searchTxt" maxlength="12"  <c:if test="${inputParam.searchTxt  != ''}"> 
												value="${inputParam.searchTxt }"  </c:if> placeholder="출하주/KPN/개체번호/경매번호" /> 
							<button class="btn_del"></button>
						</div>
					</div>
					<div class="btn visible-mo">
						<button type="button" class="btn-filter"></button>
					</div>
					<div class="hidden-mo">
						<button type="button" class="btn-apply">검색</button><button type="button"  class="btn-initial">초기화</button>
					</div>
				</div>
				<!-- // search-top -->
					<div class="filter-box <c:if test= "${ inputParam.searchTxt != null || inputParam.indvSexC != null || inputParam.startDate != null || inputParam.endDate != null || inputParam.lowPrice != null 
																								|| inputParam.highPrice != null }"> active </c:if>">
					<div class="filter-text">
						<input type="text" id="searchTxt"  name="searchTxt" class="searchTxt" maxlength="12"  <c:if test="${inputParam.searchTxt  != ''}"> 
												value="${inputParam.searchTxt }"  </c:if> placeholder="출하주/KPN/개체번호/경매번호" /> 
						<button class="btn_del"></button>
					</div>
					<div class="filter-check-box">
						<strong class="title">성별</strong>
						<ul class="filter-check">
							<li>
								<input type="checkbox" id="filter_chk1" name="indvSexC" class="indvSexC" value="1" <c:if test="${fn:contains(inputParam.indvSexC, '1,4,6')}">
	            					    checked </c:if> ><label for="filter_chk1">암</label>
							</li>
							<li>
								<input type="checkbox" id="filter_chk2" name="indvSexC"  class="indvSexC" value="2"  <c:if test="${fn:contains(inputParam.indvSexC,'0,2,5,9')}">
	            					    checked </c:if> ><label for="filter_chk2">수</label>
							</li>
							<li>
								<input type="checkbox" id="filter_chk3" name="indvSexC"  class="indvSexC" value="3"  <c:if test="${fn:contains(inputParam.indvSexC, '3')}">
	            					    checked </c:if> ><label for="filter_chk3">거세</label>
							</li>
						</ul>
					</div>
					<div class="range-box">
						<strong class="title">월령</strong>
						<span class="txt rangeDate">0 ~ 10개월</span>
						<div id="filterDate"  >
							<span class="range-label label-0">0</span>
							<span class="range-label label-50">5개월</span>
							<span class="range-label label-100">10개월</span>
						</div>
					</div>
 
					<div class="range-box">
						<strong class="title">예정가</strong>
						<span class="txt rangePrice">0 ~ 1000만 원</span>
						<div id="filterPrice">
							<span class="range-label label-0">0</span>
							<span class="range-label label-50">500</span>
							<span class="range-label label-100">1000</span>
						</div>
					</div>

					<div class="btns hidden-mo">
						<button type="button" class="btn-initial">초기화</button>
						<button type="button" class="btn-apply">적용하기</button>
					</div>
				</div>
				<div class="filter-opt active">
					<button class="del_btn searchBtn" style="display : none;">검색</button>
					<button class="del_btn sexBtn" style="display : none;">성별</button>
					<button class="del_btn dateBtn" style="display : none;">월령</button>
					<button class="del_btn priceBtn" style="display : none;">예정가</button>
				</div>
				<div class="sum_table">
					<div>
						<dl>
							<dt><p>전체</p></dt>
							<dd>
								<p><span class="ea">${buyCnt.CNT}</span>두</p>
							</dd>
						</dl>
						<dl>
							<dt><p>암</p></dt>
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_W_F}</span>두</p>
							</dd>
						</dl>
						<dl>
							<dt><p>수</p></dt>
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_M_F}</span>두</p>
							</dd>
						</dl>
						<dl>
							<dt><p>기타</p></dt>
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_ETC_F}</span>두</p>
							</dd>
						</dl>
					</div>
				</div> 
				<!-- // sum_table -->
				<div class="list_txt">
					<p>*경매번호는 변경될 수 있으며 <span>경매당일</span> 확정됩니다.</p>
				</div>
				<div class="list_downs">
			        <ul>
			            <li><a href="javascript:;" class="btn_print"><span class="ico_print">인쇄하기</span></a></li>
			            <li><a href="javascript:;" class="btn_excel"><span class="ico_excel">엑셀다운</span></a></li>
			        </ul>
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
			                   	<input type="hidden" class="ledSqno" name="ledSqno_${st.index }" value="${item.LED_SQNO}"/>
			                   	<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}"/>
			                   	
			                   	
			                   	<input type="hidden" class="birth" name="birth_${st.index }" value="${item.BIRTH}"/>
			                   	<input type="hidden" class="birthMonth" name="birthMonth_${st.index }" value="${item.BIRTH_MONTH}"/>
			                   	<input type="hidden" class="sraPdmNm" name="sraPdmNm_${st.index }" value="${item.FTSNM}"/>
			                   	<input type="hidden" class="indvSexCName" name="indvSexCName_${st.index }" value="${item.INDV_SEX_C_NAME}"/>
			                   	<input type="hidden" class="sraIndvAmnno" name="sraIndvAmnno_${st.index }" value="${item.SRA_INDV_AMNNO}"/>
			                   	<input type="hidden" class="kpnNoStr" name="kpnNoStr_${st.index }" value="${item.KPN_NO_STR}"/>
			                   	<input type="hidden" class="mcowDsc" name="mcowDsc_${st.index }" value="${item.MCOW_DSC_NAME}"/>
                   			 	<input type="hidden" class="matime" name="matime_${st.index }" value="${item.MATIME}"/>
                   			 	
                   			 	<c:set var="SRA_PD_RGNNM_ARR" value="${fn:split(item.SRA_PD_RGNNM,' ')}"></c:set>
								<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${SRA_PD_RGNNM_ARR[0]} ${SRA_PD_RGNNM_ARR[1]} ${SRA_PD_RGNNM_ARR[2]}"/>
		                    	<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${item.SRA_INDV_PASG_QCN}"/>
	                    	 	<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(item.SRA_SBID_UPR,'.')[0]}"/>
			                   	<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${item.RMK_CNTN}"/>
			                   	<input type="hidden" class="cowSogWt" name="cowSogWt_${st.index }" value="${fn:split(item.COW_SOG_WT,'.')[0] }"/>
<%--                     	<input type="hidden" class="sraMwmnnm" name="sraMwmnnm_${st.index }" value="${item.SRA_MWMNNM}"/> --%>
<%--                     	<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${item.SRA_PD_RGNNM}"/> --%>
                   			 	
								<dl>
									<dd class="date">${ item.AUC_DT_STR}</dd>
									<dd class="num">${ item.AUC_PRG_SQ }</dd>
									<dd class="name">${item.FTSNM}</dd>
								   <dd class="pd_ea">
		                               <a href="javascript:;"><span class="ico_move" fullstr="${ item.SRA_INDV_AMNNO}">${ item.SRA_INDV_AMNNO_FORMAT}</span></a>                                
		                           </dd>
									<dd class="pd_sex">${ item.INDV_SEX_C_NAME}</dd>
									<dd class="pd_date">${ item.BIRTH_STR}</dd>
									<dd class="pd_kpn">${item.KPN_NO_STR}</dd>
									<dd class="pd_num1">${item.SRA_INDV_PASG_QCN}</dd>
									<dd class="pd_num2">${item.MATIME}</dd>
									<dd class="pd_info">${item.RMK_CNTN}</dd>
									<dd class="pd_pay">
										<c:choose>
											<c:when test="${item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
											<c:otherwise>
												<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
											</c:otherwise>
										</c:choose>
									</dd>
									<dd class="pd_pav"><c:if test="${inputParam.loginNo != null && inputParam.loginNo != '' }">
										<a href="javascript:;"
											class="${(item.SBID_UPR eq '' || item.SBID_UPR eq null )?'':'act'}">
											<span class="ico_pav"> <c:choose>
													<c:when
														test="${item.SBID_UPR eq '' || item.SBID_UPR eq null }">
					                        			찜하기
					                        		</c:when>
													<c:otherwise>
														<fmt:formatNumber
															value="${fn:split(item.SBID_UPR,'.')[0]}" type="number" />
													</c:otherwise>
												</c:choose>
										</span>
										</a>
									</c:if>
	                        	</dd>
								</dl>
						   </c:forEach>
							</li>
						</ul>
					</div>
				</div>
				
				
				<!-- //list_table :: pc 전용 e -->
				<div class="list_table schedule_tb_mo">
					<div class="list_head">
						<dl>
							<dd class="num" style="width: 11%; "><a role="button"  href="javascript:;"   class="btn-sort numUpDown ${empty inputParam.numUpDown  ? '' :  inputParam.numUpDown eq '2' ? 'is-down' : 'is-up' }">번호</a></dd>
							<dd class="pd_ea">개체</dd>
							<dd class="pd_date"><a role="button"  href="javascript:;"  class="btn-sort birthUpDown ${empty inputParam.birthUpDown ? '' :  inputParam.birthUpDown eq '2' ? 'is-up' : 'is-down' }">생년월</a></dd>
							<dd class="pd_sex">성별</dd>
							<dd class="pd_kpn">KPN</dd>
							<dd class="pd_pay"><a role="button" href="javascript:;" class="btn-sort  priceUpDown  ${empty inputParam.priceUpDown ? '' :  inputParam.priceUpDown eq '2' ? 'is-up' : 'is-down' }">예정가</a></dd>
						</dl>
					</div>
					<div class="list_body">
						<ul>
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
			                   	<input type="hidden" class="ledSqno" name="ledSqno_${st.index }" value="${item.LED_SQNO}"/>
			                   	<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(item.SBID_UPR,'.')[0]}"/>
			                   	<input type="hidden" class="aucPrgSq" name="aucPrgSq_${st.index }" value="${item.AUC_PRG_SQ}"/>
			                   	<input type="hidden" class="lowsSbidLmtUpr" name="lowsSbidLmtUpr_${st.index }" value="${item.LOWS_SBID_LMT_AM <= 0 ? '0' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}"/>
			                   	
			                   	
			                   	<input type="hidden" class="birth" name="birth_${st.index }" value="${item.BIRTH}"/>
			                   	<input type="hidden" class="birthMonth" name="birthMonth_${st.index }" value="${item.BIRTH_MONTH}"/>
			                   	<input type="hidden" class="sraPdmNm" name="sraPdmNm_${st.index }" value="${item.FTSNM}"/>
			                   	<input type="hidden" class="indvSexCName" name="indvSexCName_${st.index }" value="${item.INDV_SEX_C_NAME}"/>
			                   	<input type="hidden" class="sraIndvAmnno" name="sraIndvAmnno_${st.index }" value="${item.SRA_INDV_AMNNO}"/>
			                   	<input type="hidden" class="kpnNoStr" name="kpnNoStr_${st.index }" value="${item.KPN_NO_STR}"/>
			                   	<input type="hidden" class="mcowDsc" name="mcowDsc_${st.index }" value="${item.MCOW_DSC_NAME}"/>
                   			 	<input type="hidden" class="matime" name="matime_${st.index }" value="${item.MATIME}"/>
                   			 	
                   			 	<c:set var="SRA_PD_RGNNM_ARR" value="${fn:split(item.SRA_PD_RGNNM,' ')}"></c:set>
								<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${SRA_PD_RGNNM_ARR[0]} ${SRA_PD_RGNNM_ARR[1]} ${SRA_PD_RGNNM_ARR[2]}"/>
		                    	<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${item.SRA_INDV_PASG_QCN}"/>
	                    	 	<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(item.SRA_SBID_UPR,'.')[0]}"/>
			                   	<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${item.RMK_CNTN}"/>
			                   	<input type="hidden" class="cowSogWt" name="cowSogWt_${st.index }" value="${fn:split(item.COW_SOG_WT,'.')[0] }"/>
								<dl>
									<dd class="num">${ item.AUC_PRG_SQ }</dd>
									<dd class="pd_ea">
										  <a href="javascript:;"><span class="" fullstr="${ item.SRA_INDV_AMNNO}">${ item.SRA_INDV_AMNNO_FORMAT}</span></a>               
									</dd>
									<dd class="pd_date">${ item.BIRTH_MO}</dd>
									<dd class="pd_sex">${ item.INDV_SEX_C_NAME}</dd>
									<dd class="pd_kpn">${item.KPN_NO_STR}</dd>
									<dd class="pd_pay">
										<c:choose>
											<c:when test="${item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
											<c:otherwise>
												<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
											</c:otherwise>
										</c:choose>
									</dd>
								</dl>
								<div class="pd_etc">
									<p>${item.RMK_CNTN}</p>
								</div>
								<div class="pd_pav">
									<input type="hidden" class="sbidUpr" name="sbidUpr_${st.index }" value="${fn:split(item.SBID_UPR,'.')[0]}"/>
	                            	<c:if test="${inputParam.loginNo != null && inputParam.loginNo != '' }">
			                            <a href="javascript:;" class="${(item.SBID_UPR eq '' || item.SBID_UPR eq null )?'':'act'}">
				                            <span class="ico_pav">                            
					                        	<c:choose>
					                        		<c:when test="${item.SBID_UPR eq '' || item.SBID_UPR eq null }">
					                        			찜가격
					                        		</c:when>
					                        		<c:otherwise>
					                        			<fmt:formatNumber value="${fn:split(item.SBID_UPR,'.')[0]}" type="number" />
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
		</div>
			<!-- //auction_list e -->
<!-- 		</section> -->
		

		