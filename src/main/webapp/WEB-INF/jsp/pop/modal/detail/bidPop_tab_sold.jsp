<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
			

<div class="search-area">
	<div class="search-box">
		<div class="date">
			<select name="searchDateSold" id="searchDateSold">        
				<option value="" ${(inputParam.searchDateAuc == null || inputParam.searchDateAuc == '')? 'selected' : ''}> 선택 </option>        
                <c:forEach items="${ dateList }" var="vo">
                	<option value="${vo.AUC_DT }" ${inputParam.searchDate eq vo.AUC_DT ? 'selected' : ''}> ${vo.AUC_DT_STR } </option>
                </c:forEach>                    
			</select>	
		</div>
		<div class="sort">
			<select name="searchAucObjDscSold" id="searchAucObjDscSold">
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
			<button type="button" id="btn_refreshSold" class="btn-refresh"></button>
		</div>
	</div>
	<!-- // search-top -->
	<c:choose>
		<c:when test="${empty inputParam.searchAucObjDsc }">
			<div class="sum_table">
				<div>
					<dl>
						<dt><p>구분</p></dt>
						<dd><p>송아지</p></dd>
						<dd><p>비육우</p></dd>
						<dd><p>번식우</p></dd>
						<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
							<dd><p>염소</p></dd>
						</c:if>
						<dd><p>합계</p></dd>
					</dl>
					<dl>
						<dt><p>전체</p></dt>
						<dd>
							<p><span class="ea">${buyCnt.CNT_CALF}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_NO_COW}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_COW}</span>두</p>
						</dd>
						<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
							<dd>
								<p><span class="ea">${buyCnt.CNT_5}</span>두</p>
							</dd>
						</c:if>
						<dd>
							<p><span class="ea">${buyCnt.CNT}</span>두</p>
						</dd>
					</dl>
					<dl>
						<dt><p>암</p></dt>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_W_F_1}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_W_F_2}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_W_F_3}</span>두</p>
						</dd>
						<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_W_F_5}</span>두</p>
							</dd>
						</c:if>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_W_F}</span>두</p>
						</dd>
					</dl>
					<dl>
						<dt><p>수</p></dt>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_M_F_1}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_M_F_2}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_M_F_3}</span>두</p>
						</dd>
						<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_M_F_5}</span>두</p>
							</dd>
						</c:if>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_M_F}</span>두</p>
						</dd>
					</dl>
					<dl>
						<dt><p>기타</p></dt>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_ETC_F_1}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_ETC_F_2}</span>두</p>
						</dd>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_ETC_F_3}</span>두</p>
						</dd>
						<c:if test="${johapData.ETC_AUC_OBJ_DSC eq '5'}">
							<dd>
								<p><span class="ea">${buyCnt.CNT_SEX_ETC_F_5}</span>두</p>
							</dd>
						</c:if>
						<dd>
							<p><span class="ea">${buyCnt.CNT_SEX_ETC_F}</span>두</p>
						</dd>
					</dl>
				</div>
			</div> 
		</c:when>
		<c:when test="${empty inputParam.searchAucObjDsc }">
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
		</c:when>
	</c:choose>
	<!-- // sum_table -->
	<div class="list_table auction_result">
		<div class="list_head">
			<dl>
				<dd class="date">경매일자</dd>
				<dd class="num"><span class="w_view_in">경매</span>번호</dd>
				<dd class="name">출하주</dd>
				<dd class="pd_ea">개체번호</dd>
				<dd class="pd_sex">성별</dd>
				<dd class="pd_kg textNumber">중량<span class="w_view_in">(kg)</span></dd>
				<dd class="pd_kpn">KPN</dd>
				<dd class="pd_num1">계대</dd>
				<dd class="pd_pay1 textNumber">최저가</dd>
				<dd class="pd_pay2 textNumber">낙찰가</dd>
				<dd class="pd_state">경매상태</dd>
				<dd class="pd_etc">비고</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="">				
				<c:if test="${soldList.size() <= 0}">
					<li>
						<dl>
							<dd>
								낙찰내역이 등록되지 않았습니다.
							</dd>
						</dl>
					</li>
				</c:if>
                <c:forEach items="${ soldList }" var="vo" varStatus="st">
                    <li>
						<dl>
							<dd class="date">${vo.AUC_DT }</dd>
							<dd class="num">${vo.AUC_PRG_SQ }</dd>
							<dd class="name">${vo.FTSNM }</dd>
							<dd class="pd_ea">${vo.SRA_INDV_AMNNO_FORMAT_F }</dd>
							<dd class="pd_sex">${vo.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg textNumber">${vo.COW_SOG_WT }</dd>
							<dd class="pd_kpn">${vo.KPN_NO_STR }</dd>
							<dd class="pd_num1"><fmt:formatNumber value="${fn:split(vo.COW_SOG_WT,'.')[0]}" type="number" /></dd>
							<dd class="pd_pay1 textNumber">
								<c:choose>
									<c:when test="${vo.LOWS_SBID_LMT_AM eq '' || vo.LOWS_SBID_LMT_AM == null || vo.LOWS_SBID_LMT_AM <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.LOWS_SBID_LMT_UPR,'.')[0]}" type="number"/>
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_pay2 textNumber">
								<c:choose>
									<c:when test="${vo.SRA_SBID_UPR eq '' || vo.SRA_SBID_UPR == null || vo.SRA_SBID_UPR <= 0}">-</c:when>
									<c:otherwise>
										<fmt:formatNumber value="${fn:split(vo.SRA_SBID_UPR,'.')[0]}" type="number"/>
									</c:otherwise>
								</c:choose>
							</dd>
							<dd class="pd_state">${vo.SEL_STS_DSC_NAME }</dd>
							<dd class="pd_etc">${vo.RMK_CNTN }</dd>
						</dl>
                   	</li>
                  	</c:forEach>
			</ul>
		</div>
	</div>
</div>