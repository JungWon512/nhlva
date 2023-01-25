<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<c:set var="bdln_yn" value="0" />
<c:forEach items="${tabList}" var="item" varStatus="st">
<c:if test="${item.SIMP_C eq '4' && item.VISIB_YN eq '1'}">
	<c:set var="bdln_yn" value="1" />
</c:if>
</c:forEach>

<h3 class="tit">출장우</h3>
<div class="cow-basic">
	<table class="table-detail">
		<colgroup>
			<col width="23%">
			<col width="27%">
			<col width="23%">
			<col width="27%">
		</colgroup>
		<tbody>
			<tr>
				<th>경매번호</th>
				<td colspan="2" class="bdr-y">${infoData.AUC_PRG_SQ }번</td>
				<td rowspan="2" class="ta-C bg-gray" style="padding:0;">
					<span class="fz-80 c-blue">
						${infoData.INDV_SEX_C_NAME.length() > 2 ? infoData.INDV_SEX_C_NAME.substring(0,2) : infoData.INDV_SEX_C_NAME }
					</span>
				</td>
			</tr>
			<tr>
				<th>예정가</th>
				<td colspan="2" class="bdr-y"><span class="c-red">${infoData.LOWS_SBID_LMT_UPR }</span></td>
			</tr>
			<tr>
				<th>개체번호</th>
				<td colspan="3">${infoData.SRA_INDV_AMNNO}</td>
			</tr>
			<tr>
				<th>개월령</th>
				<td colspan="3">${infoData.BIRTH_MONTH }</td>
			</tr>
			<tr>
				<th>출하주</th>
				<td colspan="3">${infoData.FTSNM }</td>
			</tr>
			<tr>
				<th>지역</th>
				<td colspan="3">${infoData.SRA_PD_RGNNM }</td>
			</tr>
			<tr>
				<th>등록구분</th>
				<td colspan="3">${infoData.RG_DSC_NAME }</td>
			</tr>
			<tr>
				<th>중량</th>
				<td class="ta-C bdr-y">${infoData.COW_SOG_WT }Kg</td>
				<th>KPN</th>
				<td class="ta-C">${infoData.KPN_NO }</td>
			</tr>
			<c:if test="${bdln_yn eq '1'}">
			<tr>
				<th>체장</th>
				<td class="ta-C bdr-y">${infoData.BDLN_VAL }</td>
				<th>체고</th>
				<td class="ta-C">${infoData.BDHT_VAL }</td>
			</tr>
			</c:if>
			<tr>
				<th>계대</th>
				<td class="ta-C bdr-y">${infoData.SRA_INDV_PASG_QCN }</td>
				<th>산차</th>
				<td class="ta-C">${infoData.MATIME }</td>
			</tr>
			<tr>
				<th>비고</th>
				<td colspan="3">${infoData.RMK_CNTN }</td>
			</tr>
		</tbody>
	</table>
</div>

<c:if test="${matingList.size() > 0 }">
	<h3 class="tit">교배정보</h3>
	<div class="cow-basic">
		<table class="table-detail">
			<colgroup>
				<col width="10%">
				<col width="12%">
				<col width="18%">
				<col width="12%">
				<col width="16%">
				<col width="16%">
				<col width="14%">
			</colgroup>
			<tbody>
				<tr>
					<th class="ta-C">산차</th>
					<th class="ta-C">교배 차수</th>
					<th class="ta-C">교배일자</th>
					<th class="ta-C">수정방법</th>
					<th class="ta-C">KPN</th>
					<th class="ta-C">인공수정사</th>
					<th class="ta-C">교배농가</th>
				</tr>			
				<c:forEach items="${ childBirthList }" var="item" varStatus="st">
					<tr>
						<td class="ta-C fz-32 bdr-y">${ item.CRSBD_MATIME }</td>
						<td class="ta-C fz-32 bdr-y">${ item.CRSBD_QCN }</td>
						<td class="ta-C fz-32 bdr-y">${ item.CRSBD_DT_STR }</td>
						<td class="ta-C fz-32 bdr-y">${ item.FERT_METHC }</td>
						<td class="ta-C fz-32 bdr-y">${ item.SRA_KPN_NO }</td>
						<td class="ta-C fz-32 bdr-y">${ item.FERT_AMRNM }</td>
						<td class="ta-C fz-32 bdr-y">${ item.SRA_FARMNM }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>

<c:if test="${childBirthList.size() > 0 }">
	<h3 class="tit">분만정보</h3>
	<div class="cow-basic">
		<table class="table-detail">
			<colgroup>
				<col width="10%">
				<col width="10%">
				<col width="25%">
				<col width="13%">
				<col width="22%">
				<col width="25%">
			</colgroup>
			<tbody>
				<tr>
					<th class="ta-C">산차</th>
					<th class="ta-C">분만<br/>구분</th>
					<th class="ta-C">분만일자</th>
					<th class="ta-C">성별</th>
					<th class="ta-C">생시체중</th>
					<th class="ta-C bdr-n">송아지 귀표번호<br/>(KPN)</th>
				</tr>			
				<c:forEach items="${ childBirthList }" var="item" varStatus="st">
					<tr>
						<td class="ta-C fz-32 bdr-y">${ item.MATIME }</td>
						<td class="ta-C fz-32 bdr-y">${ item.PTUR_DSC }</td>
						<td class="ta-C fz-32 bdr-y">${ item.PTUR_DT_STR }</td>
						<td class="ta-C fz-32 bdr-y">${ item.INDV_SEX_C_NAME }</td>
						<td class="ta-C fz-32 bdr-y">${ item.LFTM_WGH } kg</td>
						<td class="ta-C fz-32">${ item.CALF_SRA_INDV_EART_NO_STR }<br><span class="fz-32">${item.SRA_KPN_NO}</span></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>

<c:if test="${moveList.size() > 0 }">
	<h3 class="tit">이동정보</h3>
	<div class="cow-basic">
		<table class="table-detail">
			<colgroup>
				<col width="23%">
				<col width="37%">
				<col width="40%">
			</colgroup>
			<tbody>
				<tr>
					<th>구분</th>
					<th class="ta-C">소유자</th>
					<th class="ta-C bdr-n">사육지</th>
				</tr>			
				<c:forEach items="${ moveList }" var="item" varStatus="st">
					<td class="ta-C bdr-y">${ item.REGTYPE }</td>
					<td class="ta-C bdr-y">${ item.FARMERNM }<br><span class="fz-32">${item.REGYMD}</span></td>
					<td class="fz-32">${ item.FARMADDR }</td>
				</c:forEach>
			</tbody>
		</table>
	</div>
</c:if>
<h3 class="tit">브루셀라 / 질병검사</h3>
<div class="cow-basic">
	<table class="table-detail">
		<colgroup>
			<col width="23%">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>브루셀라</th>
				<td>
					<c:if test="${not empty infoData.BRCL_ISP_DT_STR}">
						${infoData.BRCL_ISP_DT_STR } 검사 <span class="c-blue">(${infoData.BRCL_ISP_RZT_C_NM })</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<th>백신접종</th>
				<td>
					<c:if test="${not empty infoData.VACN_ORDER}">
						${infoData.VACN_DT_STR } 접종 <span class="c-blue">(${infoData.VACN_ORDER })</span>
					</c:if>
				</td>
			</tr>
			<tr>
				<th>결핵검사</th>
				<td>
					<c:if test="${not empty infoData.BOVINE_DT_STR}">
						${infoData.BOVINE_DT_STR } <span class="c-blue">(${infoData.BOVINE_RSLTNM })</span>
					</c:if>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<p class="fz-36 c-gray">* 경매일 기준 정보입니다.</p>