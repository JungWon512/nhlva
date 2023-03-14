<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<input type="hidden" id="loginNo" name="loginNo" value="${inputParam.loginNo}"/>
<input type="hidden" id="searchDate" value="${inputParam.searchDate}"/>
<input type="hidden" id="stateFlag" value="${inputParam.stateFlag}"/>
<input type="hidden" id="naBzPlcNo" value="${johapData.NA_BZPLCNO}"/>
<input type="hidden" id="johpCd" value="${johapData.NA_BZPLC}"/>
<div class="bill-detail cow-detail">
	<div class="list_search">
		<ul class="radio_group">
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra1" value="" <c:if test="${inputParam.searchAucObjDsc eq '0' || empty inputParam.searchAucObjDsc }">
	                checked
	             </c:if>/><label for="ra1">전체</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra2" value="1" <c:if test="${inputParam.searchAucObjDsc eq '1'}">
	                checked
	            </c:if>/><label for="ra2">송아지</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra3" value="2" <c:if test="${inputParam.searchAucObjDsc eq '2'}">
	                checked
	            </c:if>/><label for="ra3">비육우</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra4" value="3" <c:if test="${inputParam.searchAucObjDsc eq '3'}">
	                checked
	            </c:if>/><label for="ra4">번식우</label>
			</li>
		</ul>
	</div>
	<div class="cow-basic">
		<table class="table-detail">
			<colgroup>
				<col width="28%">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<th>조합명</th>
					<td>${johapData.AREANM}조합</td>
				</tr>
				<tr>
					<th>경매일자</th>
					<td>${inputParam.aucDt}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="cow-basic auction_result">
		<table class="table-detail stateInfo">
			<colgroup>
				<col width="28%">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<th>참가번호</th>
					<td class="info no">${ empty stateInfo.LVST_AUC_PTC_MN_NO ? '내용이 없습니다.' : stateInfo.LVST_AUC_PTC_MN_NO }</td>
				</tr>
				<tr>
					<th>성명</th>
					<td class="info nm">${ empty stateInfo.USER_NM ? '내용이 없습니다.' : stateInfo.USER_NM }</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="cow-basic auction_result">
		<table class="table-detail stateTotPrice">
			<colgroup>
				<col width="28%">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<th>낙찰금액①</th>
					<fmt:formatNumber value="${ stateTotPrice.SRA_SBID_AM }" type="number" var="SRA_SBID_AM"/>
					<td class="ta-R price bid">${empty SRA_SBID_AM ? '0':SRA_SBID_AM} 원</td>
				</tr>
				<tr>
					<th>총수수료②</th>
					<fmt:formatNumber value="${ stateTotPrice.SRA_TR_FEE }" type="number" var="SRA_TR_FEE"/>
					<td class="ta-R price fee"><span class="c-red">${empty SRA_TR_FEE ? '0':SRA_TR_FEE}</span> 원</td>
				</tr>
				<tr>
					<th>정산금액(원)<br>①+②</th>
					<fmt:formatNumber value="${stateTotPrice.SRA_SBID_AM + stateTotPrice.SRA_TR_FEE}" type="number" var="TOT_BID_AM"/>
					<td class="ta-R fz-50 price total">${empty TOT_BID_AM ? '0':TOT_BID_AM} 원</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="bank-info">
		입금계좌 : ${accountInfo.ACNO } <br>
		예금주 : ${accountInfo.NA_BZPLNM }
	</div>
	<div class="cow-basic auction_result">
		<table class="table-detail stateBuyCnt">
			<colgroup>
				<col width="33.33%">
				<col width="33.33%">
				<col>
			</colgroup>
			<thead>
				<tr>
					<th>낙찰</th>
					<th>암</th>
					<th class="bdr-n">수</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td id="T" class="ta-C bdr-y c-blue stateCowCnt">
						<c:choose>
							<c:when test="${ stateBuyCnt.CNT_SEX_STATE_W == 0 && stateBuyCnt.CNT_SEX_STATE_M == 0 }">0</c:when>
							<c:otherwise>
								${ stateBuyCnt.CNT_SEX_STATE_W + stateBuyCnt.CNT_SEX_STATE_M }
							</c:otherwise>
						</c:choose>
					</td>
					<td id="W" class="ta-C bdr-y c-blue stateCowCnt">
						<c:choose>
							<c:when test="${ stateBuyCnt.CNT_SEX_STATE_W == 0 }">0</c:when>
							<c:otherwise>
								${ stateBuyCnt.CNT_SEX_STATE_W }
							</c:otherwise>
						</c:choose>
					</td>
					<td id="M" class="ta-C c-blue stateCowCnt">
						<c:choose>
							<c:when test="${ stateBuyCnt.CNT_SEX_STATE_M == 0 }">0</c:when>
							<c:otherwise>
								${ stateBuyCnt.CNT_SEX_STATE_M }
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<p class="info-text">* 입금은 영업일 3일 이내입니다.<br>* ${johapData.CLNTNM} 문의) ${johapData.TEL_NO }</p>
	<div class="cow-sibiling">
		<table class="table-detail">
			<colgroup>
				<col width="13%">
				<col width="20%">
				<col width="12%">
				<col width="*">
				<col width="27%">
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>개체번호</th>
					<th>성별</th>
					<th>낙찰금액(원)</th>
					<th>수수료(원)</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${ list.size() <= 0 }">
					<tr>
						<td class="ta-C" colspan="5">상세 내역이 없습니다.</td>
					</tr>
				</c:if>
				<c:forEach items="${ list }" var="vo">
					<tr class="detail_tr" id="${ vo.SRA_INDV_AMNNO }">
						<td class="ta-C bg-gray">${ vo.AUC_PRG_SQ }</td>
						<td class="ta-C">${ vo.SRA_INDV_AMNNO_FORMAT }</td>
						<td class="ta-C">${ vo.INDV_SEX_C_NAME }</td>
						<fmt:formatNumber value="${vo.SRA_SBID_AM}" type="number" var="SRA_SBID_AM"/>
						<td class="ta-C">${ SRA_SBID_AM }</td>
						<fmt:formatNumber value="${empty vo.SRA_TR_FEE ? 0 : vo.SRA_TR_FEE }" type="number" var="SRA_TR_FEE"/>
						<c:choose>
							<c:when test="${ SRA_TR_FEE ne '0' }">
								<td class="ta-C"><a href="javascript:;" class="c-red">${ SRA_TR_FEE }</a></td>
							</c:when>
							<c:otherwise>
								<td class="ta-C">-</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<!-- //auction_list e -->