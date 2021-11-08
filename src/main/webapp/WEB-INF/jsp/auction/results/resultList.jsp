<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<div class="auction_list">
	<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
	<h3>경매결과</h3>
	<div class="list_search">
		<ul class="radio_group">
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra1" value="" ${(paramVo.searchAucObjDsc == null || paramVo.searchAucObjDsc == '' ) ? 'checked':'' }><label for="ra1">전체</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra2" value="1" ${paramVo.searchAucObjDsc == 1?'checked':'' }><label for="ra2">송아지</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra3" value="2" ${paramVo.searchAucObjDsc == 2 ?'checked':'' }><label for="ra3">비육우</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra4" value="3" ${paramVo.searchAucObjDsc == 3 ?'checked':'' }><label for="ra4">번식우</label>
			</li>
		</ul>
		<ul class="sch_area">
			<li class="txt">
				<input type="text" name="searchTxt" id="searchTxt" maxlength="4" value="${paramVo.searchTxt }" placeholder="개체" />
			</li>
<!-- 			<li class="sort"> -->
<!-- 				<select name="searchOrder" id="searchOrder"> -->
<%-- 					<option value="" ${(paramVo.searchOrder == null || paramVo.searchOrder == '') ? 'selected':'' } >정렬</option> --%>
<%--                     <option value="AUC_PRG_SQ" ${paramVo.searchOrder == 'AUC_PRG_SQ' ?'selected':'' }>경매번호</option> --%>
<%--                     <option value="SRA_PDMNM" ${paramVo.searchOrder == 'SRA_PDMNM' ?'selected':'' }>출하주</option> --%>
<%--                     <option value="KPN_NO" ${paramVo.searchOrder == 'KPN_NO' ?'selected':'' }>KPN</option> --%>
<!-- 				</select> -->
<!-- 			</li> -->
			<li class="date">
				<select name="searchDate" id="searchDate">
					<option value="" ${ dateList.size() > 0 ? '':'selected' }> 선택 </option>
					<c:forEach items="${ dateList }" var="vo"> 
						<option value="${vo.AUC_DT }" ${paramVo.searchDate == vo.AUC_DT ?'selected':'' }> ${vo.AUC_DT_STR } </option>
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
				<dd class="pd_pay1">최저가</dd>
				<dd class="pd_pay2">낙찰가</dd>
				<dd class="pd_state">경매상태</dd>
				<dd class="pd_etc">비고</dd>
			</dl>
		</div>
		<div class="list_body">
			<ul class="mCustomScrollBox">
				<c:if test="${resultList.size() <= 0}">
					<li>
						<dl>
							<dd>
								검색결과가 없습니다.
							</dd>
						</dl>
					</li>
				</c:if>
				<c:forEach items="${ resultList }" var="item">
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
						
						<c:set var="SRA_PD_RGNNM_ARR" value="${fn:split(item.SRA_PD_RGNNM,' ')}"></c:set>
						<input type="hidden" class="sraPdRgnnm" name="sraPdRgnnm_${st.index }" value="${SRA_PD_RGNNM_ARR[0]} ${SRA_PD_RGNNM_ARR[1]} ${SRA_PD_RGNNM_ARR[2]}"/>
						<input type="hidden" class="sraIndvPasgQcn" name="sraIndvPasgQcn_${st.index }" value="${item.SRA_INDV_PASG_QCN}"/>
						<input type="hidden" class="sraSbidUpr" name="sraSbidUpr_${st.index }" value="${fn:split(item.SRA_SBID_UPR,'.')[0]}"/>
						<input type="hidden" class="rmkCntn" name="rmkCntn_${st.index }" value="${item.RMK_CNTN}"/>
						<input type="hidden" class="cowSogWt" name="cowSogWt_${st.index }" value="${fn:split(item.COW_SOG_WT,'.')[0] }"/>
						
						<input type="hidden" class="lvstAucPtcMnNo" name="lvstAucPtcMnNo_${st.index}" value="${item.LVST_AUC_PTC_MN_NO}" />
						<dl>
							<dd class="date">${ item.AUC_DT_STR }</dd>
							<dd class="num">${ item.AUC_PRG_SQ }</dd>
							<dd class="name">${ item.SRA_PDMNM }</dd>
							<dd class="pd_ea"><a href="javascript:;"><span class="" fullstr="${ item.SRA_INDV_AMNNO}">${ item.SRA_INDV_AMNNO_FORMAT }</span></a></dd>
							<dd class="pd_sex">${item.INDV_SEX_C_NAME }</dd>
							<dd class="pd_kg textNumber">${ (item.COW_SOG_WT == '' || item.COW_SOG_WT == null || item.COW_SOG_WT <= 0 ) ? '-' : fn:split(item.COW_SOG_WT,'.')[0] }</dd>
							<dd class="pd_kpn">${ item.KPN_NO_STR }</dd>
							<dd class="pd_num1">${ item.SRA_INDV_PASG_QCN }</dd>
							<dd class="pd_pay1 textNumber">${(item.LOWS_SBID_LMT_AM == '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0 ) ? '-' :  fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
							<dd class="pd_pay2 textNumber">${(item.SRA_SBID_UPR eq '' || item.SRA_SBID_UPR == null || item.SRA_SBID_UPR <= 0 ) ? '-' : fn:split(item.SRA_SBID_UPR,'.')[0] }</dd>
							<dd class="pd_state">${ item.SEL_STS_DSC_NAME }</dd>
							<dd class="pd_etc">${ item.RMK_CNTN }</dd>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</div>
<!-- //auction_list e -->
