<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!-- admin_new_reg [s] -->
<div class="admin_new_reg">
	<div class="top-btns">
		<button type="button" class="btn_close" data-mode="${empty sogCowInfo.OSLP_NO ? 'regist' : 'modify'}">닫기</button>
		<button type="button" class="btn_clear">초기화</button>
		<button type="button" class="btn_save">저장</button>
	</div>
	<form name="frm" action="" method="post" autocomplete="off">
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
		<div class="regist_form" style="display:none;">
			변경PG : <input type="text" name="chgPgid" value="registCow" />
			등록모드 : <input type="text" name="regMode" value="${empty sogCowInfo.OSLP_NO ? 'regist' : 'modify'}" />
			등록모드 : <input type="text" name="chgRmkCntn" value="${empty sogCowInfo.OSLP_NO ? '신규등록' : '수정'}" />
			접수번호 : <input type="text" name="oslpNo" value="${sogCowInfo.OSLP_NO}" />
			접수번호 : <input type="text" name="ledSqno" value="${sogCowInfo.LED_SQNO}" />
			최고응찰한도 : <input type="text" name="baseLmtAm" value="${qcnInfo[0].BASE_LMT_AM_ORI}" />
			응찰단위 : <input type="text" name="divisionPrice" value="${sogCowInfo.DIVISION_PRICE}" />
			출하자거래처코드 : <input type="text" name="naTrplC" value="${fn:trim(sogCowInfo.NA_TRPL_C)}" />
<%-- 			생산자코드 : <input type="text" name="sogmnC" value="${fn:trim(sogCowInfo.SOGMN_C)}" /> --%>
			<!-- 생산자코드 기본값 빈값으로 셋팅 -->
			생산자코드 : <input type="text" name="sogmnC" value="" />
			생산자이름 : <input type="text" name="sraPdmnm" value="${fn:trim(sogCowInfo.SRA_PDMNM)}" />
			생산지역 : <input type="text" name="sraPdRgnnm" value="${fn:trim(sogCowInfo.SRA_PD_RGNNM)}" />
			자가운송여부 : <input type="text" name="trpcsPyYn" value="${fn:trim(sogCowInfo.TRPCS_PY_YN)}" />
		</div>
		
		<div class="cow-basic">
			<table class="table-detail">
				<colgroup>
					<col width="31%">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th><strong class="fc-red">*</strong>경매일자</th>
						<td class="bg-lilac">
							<fmt:parseDate value="${params.aucDt}" pattern="yyyyMMdd" var="tmpAucDt" />
							<fmt:formatDate value="${tmpAucDt}" pattern="yyyy-MM-dd" var="aucDt" />
							${aucDt}
						</td>
					</tr>
					<tr>
						<th><strong class="fc-red">*</strong>경매대상</th>
						<td class="bg-lilac">
							<c:set var="aucObjDsc" value="${empty sogCowInfo.AUC_OBJ_DSC ? params.aucObjDsc : sogCowInfo.AUC_OBJ_DSC}" />
							<c:forEach items="${aucObjDscList}" var="vo">
								<c:if test="${vo.SIMP_C eq aucObjDsc}">${vo.SIMP_CNM}</c:if>
							</c:forEach>
							<input type="hidden" name="aucObjDsc" value="${aucObjDsc}" />
						</td>
					</tr>
					<tr>
						<th><strong class="fc-red">*</strong>개체번호</th>
						<td class="bg-lilac">
							<div class="inp">
								${fn:substring(params.sraIndvAmnno, 3, 15)}
								<input type="hidden" name="sraIndvAmnno" value="${params.sraIndvAmnno}" readonly="readonly"/>
								<input type="hidden" name="sraSrsDsc" value="01" readonly="readonly"/>
								<button type="button" class="btn_reEnter">[재입력]</button>
							</div>
						</td>
					</tr>
					<tr>
						<th><strong class="fc-red">*</strong><span class="fc-red">경매번호</span></th>
						<td>
							<div class="inp">
								<input type="text" name="aucPrgSq" id="aucPrgSq" class="required" value="${sogCowInfo.AUC_PRG_SQ}" placeholder="경매번호" maxlength="3" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<h3 class="tit">출하주 정보</h3>
		<div class="cow-basic">
			<table class="table-detail">
				<colgroup>
					<col width="31%">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>출하주</th>
						<td>
							<div class="inp">
								<input type="text" name="ftsnm" value="${fn:trim(sogCowInfo.FTSNM)}" placeholder="출하주" readonly="readonly" />
								<input type="hidden" name="fhsIdNo" value="${fn:trim(sogCowInfo.FHS_ID_NO)}" placeholder="농가관리번호" />
								<input type="hidden" name="farmAmnno" value="${fn:trim(sogCowInfo.FARM_AMNNO)}" placeholder="농가관리번호" />
							</div>
						</td>
					</tr>
					<tr>
						<th>주소</th>
						<td>
							<div class="inp">
								<input type="text" name="dongup" value="${fn:trim(sogCowInfo.DONGUP)}" placeholder="주소" readonly="readonly" />
							</div>
						</td>
					</tr>
					<tr>
						<th>상세주소</th>
						<td>
							<div class="inp">
								<input type="text" name="dongbw" value="${fn:trim(sogCowInfo.DONGBW)}" placeholder="상세주소" readonly="readonly" />
							</div>
						</td>
					</tr>
					<tr>
						<th>연락처</th>
						<td>
							<div class="inp">
								<input type="text" name="cusMpno" value="${fn:trim(sogCowInfo.CUS_MPNO)}" placeholder="연락처" readonly="readonly" />
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<h3 class="tit">개체정보</h3>
		<div class="cow-basic">
			<table class="table-detail">
				<colgroup>
					<col width="31%">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>개체성별</th>
						<td>
							<select name="indvSexC">
								<c:forEach items="${indvSexCList}" var="vo">
									<option value="${vo.SIMP_C}" ${vo.SIMP_C eq sogCowInfo.INDV_SEX_C ? 'selected' : ''}>${vo.SIMP_CNM}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>생년월일</th>
						<td>
							<div class="inp">
								<input type="date" name="birth" class="calendar required" value="${sogCowInfo.BIRTH}" placeholder="생년월일"  maxlength="10" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>개체관리번호</th>
						<td>
							<div class="inp">
								<input type="text" name="indvIdNo" value="${fn:trim(sogCowInfo.INDV_ID_NO)}" placeholder="개체관리번호" maxlength="20" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>등록번호</th>
						<td>
							<div class="inp">
								<input type="text" name="sraIndvBrdsraRgNo" value="${fn:trim(sogCowInfo.SRA_INDV_BRDSRA_RG_NO)}" placeholder="등록번호" maxlength="20" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>등록구분</th>
						<td>
							<select name="rgDsc" class="required" placeholder="등록구분">
								<c:forEach items="${rgDscList}" var="vo">
									<option value="${vo.SIMP_C}" ${vo.SIMP_C eq sogCowInfo.RG_DSC ? 'selected' : ''}>${vo.SIMP_CNM}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>KPN번호</th>
						<td>
							<div class="inp">
								<input type="text" name="kpnNo" value="${fn:trim(sogCowInfo.KPN_NO)}" placeholder="KPN번호" maxlength="9" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>어미구분</th>
						<td>
							<select name="mcowDsc" class="required" placeholder="어미구분">
							<c:forEach items="${rgDscList}" var="vo">
								<option value="${vo.SIMP_C}" ${vo.SIMP_C eq sogCowInfo.MCOW_DSC ? 'selected' : ''}>${vo.SIMP_CNM}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>어미귀표</th>
						<td>
							<div class="inp">
								<input type="text" name="mcowSraIndvAmnno" value="${fn:trim(sogCowInfo.MCOW_SRA_INDV_AMNNO)}" placeholder="어미귀표" maxlength="15" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>어미산차</th>
						<td>
							<div class="inp">
								<input type="text" name="matime" class="required" value="${fn:trim(sogCowInfo.MATIME)}" placeholder="어미산차" maxlength="2" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>계대</th>
						<td>
							<div class="inp">
								<input type="text" name="sraIndvPasgQcn" class="required" value="${fn:trim(sogCowInfo.SRA_INDV_PASG_QCN)}" placeholder="계대" maxlength="2" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>중량(Kg)</th>
						<td>
							<div class="inp">
								<input type="text" name="cowSogWt" value="${sogCowInfo.COW_SOG_WT}" placeholder="중량" maxlength="4" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>예정가</th>
						<td>
							<div class="inp">
								<input type="text" name="firLowsSbidLmtAmTmp" value="${sogCowInfo.LOWS_SBID_LMT_UPR}" placeholder="예정가" maxlength="5" pattern="\d*" inputmode="numeric" />
								<input type="hidden" name="lowsSbidLmtAmTmp" value="${sogCowInfo.LOWS_SBID_LMT_UPR}" placeholder="수정예정가" maxlength="5" pattern="\d*" inputmode="numeric" />
								<input type="hidden" name="firLowsSbidLmtAm" value="${sogCowInfo.LOWS_SBID_LMT_AM}" placeholder="수정예정가" pattern="\d*" inputmode="numeric" />
								<input type="hidden" name="lowsSbidLmtAm" value="${sogCowInfo.LOWS_SBID_LMT_AM}" placeholder="수정예정가" maxlength="5" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>비고</th>
						<td>
							<div class="inp">
								<textarea name="rmkCntn" style="border:none;" maxlength="100">${sogCowInfo.RMK_CNTN}</textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<h3 class="tit" style="${params.aucObjDsc ne '3' ? 'display:none;' : ''}">번식우 정보</h3>
		<div class="cow-basic ppgcow" style="${params.aucObjDsc ne '3' ? 'display:none;' : ''}">
			<table class="table-detail">
				<colgroup>
					<col width="31%">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>임신구분</th>
						<td>
							<select name="ppgcowFeeDsc">
							<c:forEach items="${ppgcowFeeDscList}" var="vo">
								<option value="${vo.SIMP_C}" ${vo.SIMP_C eq sogCowInfo.PPGCOW_FEE_DSC ? 'selected' : ''}>${vo.SIMP_CNM}</option>
							</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<th>인공수정일자</th>
						<td>
							<div class="inp">
								<fmt:parseDate value="${sogCowInfo.AFISM_MOD_DT}" pattern="yyyyMMdd" var="tmpAfismModDt" />
								<fmt:formatDate value="${tmpAfismModDt}" pattern="yyyy-MM-dd" var="afismModDt" />
								<input type="date" name="afismModDt" class="calendar" value="${afismModDt}" placeholder="인공수정일자" maxlength="10" max="${params.aucDt}" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>임신개월수</th>
						<td>
							<div class="inp">
								<input type="text" name="prnyMtcn" value="${fn:trim(sogCowInfo.PRNY_MTCN)}" placeholder="임신개월수" maxlength="2" pattern="\d*" inputmode="numeric" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>임신감정여부</th>
						<td>
							<div class="inp">
								<input type="checkbox" name="prnyJugYn" id="prnyJugYn"
									   value="${empty fn:trim(sogCowInfo.PRNY_JUG_YN) ? '1' : fn:trim(sogCowInfo.PRNY_JUG_YN)}"
									   ${fn:trim(sogCowInfo.PRNY_JUG_YN) eq '0' || !fn:contains('1, 3', sogCowInfo.PPGCOW_FEE_DSC) ? '' : 'checked'}
									   placeholder="임신감정여부" />
								<label for="prnyJugYn">${fn:trim(sogCowInfo.PRNY_JUG_YN) eq '0' || !fn:contains('1, 3', sogCowInfo.PPGCOW_FEE_DSC) ? '부' : '여'}</label>
							</div>
						</td>
					</tr>
					<tr>
						<th>수정KPN</th>
						<td>
							<div class="inp">
								<input type="text" name="modKpnNo" value="${fn:trim(sogCowInfo.MOD_KPN_NO)}" placeholder="수정KPN" maxlength="9" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>분만예정일</th>
						<td>
							<div class="inp">
								<input type="date" name="pturPlaDt" class="calendar" value="${fn:trim(sogCowInfo.PTUR_PLA_DT)}" placeholder="분만예정일" maxlength="10" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<th>괴사감정여부</th>
						<td>
							<div class="inp">
								<input type="checkbox" name="ncssJugYn" id="ncssJugYn"
									   value="${empty fn:trim(sogCowInfo.NCSS_JUG_YN) ? '0' : fn:trim(sogCowInfo.NCSS_JUG_YN)}"
									   placeholder="괴사감정여부" />
								<label for="ncssJugYn">${fn:trim(sogCowInfo.NCSS_JUG_YN) eq '1' ? '여' : '부'}</label>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<h3 class="tit">백신정보</h3>
		<div class="cow-basic">
			<table class="table-detail">
				<colgroup>
					<col width="31%">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>브루셀라검사일</th>
						<td>
							<div class="inp">
								<fmt:parseDate value="${sogCowInfo.BRCL_ISP_DT}" pattern="yyyyMMdd" var="tmpBrclIspDt" />
								<fmt:formatDate value="${tmpBrclIspDt}" pattern="yyyy-MM-dd" var="brclIspDt" />
								<input type="date" name="brclIspDt" class="calendar" value="${brclIspDt}" placeholder="브루셀라 검사일" maxlength="10" max="${params.aucDt}" />
								<button type="button" class="btn_input_reset"></button>
							</div>
							<input type="hidden" name="brclIspRztC" value="${sogCowInfo.BRCL_ISP_RZT_C}" placeholder="브루셀라 결과" maxlength="1">							
						</td>
					</tr>
					<tr>
						<th>브루셀라검사증<br>제출여부</th>
						<td>
							<div class="inp">
								<input type="checkbox" name="brclIspCtfwSmtYn" id="brclIspCtfwSmtYn"
									   value="${empty fn:trim(sogCowInfo.BRCL_ISP_CTFW_SMT_YN) ? '0' : fn:trim(sogCowInfo.BRCL_ISP_CTFW_SMT_YN)}"
									   ${(fn:trim(sogCowInfo.BRCL_ISP_CTFW_SMT_YN) eq '0' || empty sogCowInfo.BRCL_ISP_CTFW_SMT_YN) ? '' : 'checked'}
									   placeholder="브루셀라 감정여부" />
								<label for="brclIspCtfwSmtYn">${fn:trim(sogCowInfo.BRCL_ISP_CTFW_SMT_YN) eq '0'? '부' : '여'}</label>
							</div>
						</td>
					</tr>
					<tr>
						<th>구제역<br/>접종차수</th>
						<td>			
							<div class="inp">
								<input type="text" name="vacnOrder" value="${sogCowInfo.VACN_ORDER}" placeholder="구제역 접종차수" maxlength="">
								<button type="button" class="btn_input_reset"></button>
							</div>						
						</td>
					</tr>
					<tr>					
						<th>구제역<br/>예방접종일</th>
						<td>							
							<div class="inp">
								<fmt:parseDate value="${sogCowInfo.VACN_DT}" pattern="yyyyMMdd" var="tmpVacnDt" />
								<fmt:formatDate value="${tmpVacnDt}" pattern="yyyy-MM-dd" var="vacnDt" />
								<input type="date" name="vacnDt" class="calendar" value="${vacnDt}" placeholder="구제역 접종일" maxlength="10" max="${params.aucDt}" />
								<button type="button" class="btn_input_reset"></button>
							</div>						
						</td>
					</tr>
					<tr>
						<th rowspan="2">우결핵<br/>검사일</th>
						<td>							
							<div class="inp">
								<fmt:parseDate value="${sogCowInfo.BOVINE_DT}" pattern="yyyyMMdd" var="tmpBovineDt" />
								<fmt:formatDate value="${tmpBovineDt}" pattern="yyyy-MM-dd" var="bovineDt" />
								<input type="date" name="bovineDt" class="calendar" value="${bovineDt}" placeholder="우결핵검사일" maxlength="10" max="${params.aucDt}" />
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="inp">
								<input type="text" name="bovineRsltnm" value="${sogCowInfo.BOVINE_RSLTNM}" placeholder="우결핵 접종결과" maxlength="">
								<button type="button" class="btn_input_reset"></button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</div>
<!-- admin_new_reg [e] -->
