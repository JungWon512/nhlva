<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<style>
div.save-box {
    text-align: center;
	font-size: 18px;
}
div.save-box .info em {
	color: #1a1a1a;
	font-weight: 700;
}
</style>

<div class="save-box">
	<div class="info">
		<em>${infoData.AUC_OBJ_DSC_NAME} |</em>
		<em>${infoData.INDV_SEX_C_NAME} |</em>
		<em>${fn:substring(infoData.SRA_INDV_AMNNO, 3, 6)} ${fn:substring(infoData.SRA_INDV_AMNNO, 6, 10)} ${fn:substring(infoData.SRA_INDV_AMNNO, 10, 14)} ${fn:substring(infoData.SRA_INDV_AMNNO, 14, 15)} </em>
	</div>
</div>
<h3 class="tit2"><span class="subTxt" style="position: absolute;right: 10px;"></span></h3>
<h3 class="tit">
	혈통정보
</h3>
<div class="cow-father">
	<table class="table-detail">
		<colgroup>
			<col width="15%">
			<col width="16%">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>부</th>
				<td colspan="2" class="bg-gray fz-32" name="blInfo_1">
					<c:choose>
						<c:when test="${empty infoData.MIF_FCOW_SRA_INDV_EART_NO_FORMAT }">
							-
						</c:when>
						<c:otherwise>
							[KPN] ${infoData.KPN_NO }
							<br/>${fn:substring(infoData.MIF_FCOW_SRA_INDV_EART_NO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }"> 
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.MIF_FCOW_SRA_INDV_EART_NO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th colspan="2">조부</th>
				<td class="fz-32" name="blInfo_0">
					<c:choose>
						<c:when test="${empty infoData.GRFA_SRA_INDV_EART_NO_FORMAT }">
							-
						</c:when>
						<c:otherwise>
							[KPN] ${infoData.GRFA_SRA_KPN_NO }
							<br/>${fn:substring(infoData.GRFA_SRA_INDV_EART_NO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }"> 
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.GRFA_SRA_INDV_EART_NO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th colspan="2">조모</th>
				<td class="fz-32" name="blInfo_2">
					<c:choose>
						<c:when test="${empty infoData.GRMO_SRA_INDV_EART_NO_FORMAT }">
							-
						</c:when>
						<c:otherwise>
							${fn:substring(infoData.GRMO_SRA_INDV_EART_NO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }">
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.GRMO_SRA_INDV_EART_NO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="cow-mother">
	<table class="table-detail">
		<colgroup>
			<col width="15%">
			<col width="16%">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>모</th>
				<td colspan="2" class="bg-gray fz-32" name="blInfo_4">
					<c:choose>
						<c:when test="${empty infoData.MCOW_SRA_INDV_AMNNO_FORMAT }">
							-
						</c:when>
						<c:otherwise> 
							${fn:substring(infoData.MCOW_SRA_INDV_AMNNO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }">
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.MCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th colspan="2">외조부</th>
				<td class="fz-32" name="blInfo_3">
					<c:choose>
						<c:when test="${empty infoData.MTGRFA_SRA_INDV_EART_NO_FORMAT }">
							-
						</c:when>
						<c:otherwise>
							[KPN] ${infoData.MTGRFA_SRA_KPN_NO } 
							<br/>${fn:substring(infoData.MTGRFA_SRA_INDV_EART_NO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }">
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.MTGRFA_SRA_INDV_EART_NO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th colspan="2">외조모</th>
				<td class="fz-32" name="blInfo_5">
					<c:choose>
						<c:when test="${empty infoData.MTGRMO_SRA_INDV_EART_NO_FORMAT }">
							-
						</c:when>
						<c:otherwise>
							${fn:substring(infoData.MTGRMO_SRA_INDV_EART_NO, 3, 15)}
							<c:if test="${'N' eq inputParam.bidPopYn }">
								<button type="button" class="btn-search btnCowSearch" data-indv-no="${infoData.MTGRMO_SRA_INDV_EART_NO}"><span class="sr-only">검색</span></button>
							</c:if>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<h3 class="tit">형매정보</h3>
<div class="cow-sibiling">
	<table class="table-detail sibTbl">
		<colgroup>
			<col width="8%">
			<col width="33%">
			<col width="14%">
			<col width="15%">
			<col width="30%">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">산차</th>
				<th rowspan="2">개체번호</th>
				<th>등록</th>
				<th>성별</th>
				<th>생년월일</th>
			</tr>
			<tr>
				<th>등급</th>
				<th>도체</th>
				<th>도축일</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${ sibList.size() == '0' }">
				<tr>
					<td rowspan="2" colspan="5" class="ta-C">-</td>
				</tr>
			</c:if>
			<c:forEach items="${ sibList }" var="item" varStatus="st">
				<tr>
					<td rowspan="2" class="ta-C bg-gray matime">${item.MATIME }</td>
					<td rowspan="2" class="ta-C sraIndvAmnno">${item.SIB_SRA_INDV_AMNNO_STR }</td>
					<td class="ta-C rgDsc">${item.RG_DSC_NAME }</td>
					<td class="ta-C indvSexC">${item.INDV_SEX_C_NAME }</td>
					<td class="ta-C birth">${not empty item.BIRTH_STR ? item.BIRTH_STR :'-' }</td>
				</tr>
				<tr>
					<td class="ta-C metrbMetqltGrd">${not empty item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</td>
					<td class="ta-C metrbBbdyWt">${not empty item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</td>
					<td class="ta-C mifBtcDt">${not empty item.MIF_BTC_DT_STR ? item.MIF_BTC_DT_STR : '-'}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>	
<h3 class="tit">후대정보</h3>
<div class="cow-sibiling">
	<table class="table-detail postTbl">
		<colgroup>
			<col width="8%">
			<col width="33%">
			<col width="14%">
			<col width="15%">
			<col width="30%">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">산차</th>
				<th rowspan="2">개체번호</th>
				<th>등록</th>
				<th>성별</th>
				<th>생년월일</th>
			</tr>
			<tr>
				<th>등급</th>
				<th>도체</th>
				<th>도축일</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${ postList.size() == '0' }">
				<tr>
					<td rowspan="2" colspan="5" class="ta-C">-</td>
				</tr>
			</c:if>
			<c:forEach items="${ postList }" var="item" varStatus="st">
				<tr>
					<td rowspan="2" class="ta-C bg-gray matime">${item.MATIME }</td>
					<td rowspan="2" class="ta-C sraIndvAmnno">${item.POST_SRA_INDV_AMNNO_STR }</td>
					<td class="ta-C rgDsc">${item.RG_DSC_NAME }</td>
					<td class="ta-C indvSexC">${item.INDV_SEX_C_NAME }</td>
					<td class="ta-C birth">${not empty item.BIRTH_STR ? item.BIRTH_STR :'-' }</td>
				</tr>
				<tr>
					<td class="ta-C metrbMetqltGrd">${not empty item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</td>
					<td class="ta-C metrbBbdyWt">${not empty item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</td>
					<td class="ta-C mifBtcDt">${not empty item.MIF_BTC_DT_STR ? item.MIF_BTC_DT_STR : '-'}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>


<script type="text/javascript">

$(document).ready(function(){
	//getAiakInfo();
});
</script>