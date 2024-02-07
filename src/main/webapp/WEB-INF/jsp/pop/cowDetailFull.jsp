<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<form name="frm" action="" method="post">
	<input type="hidden" name="place" value="<c:out value="${inputParam.place}" />" />
	<input type="hidden" name="naBzplc" value="<c:out value="${inputParam.naBzplc}" />" />
	<input type="hidden" name="aucDt" value="<c:out value="${inputParam.aucDt}" />" />
	<input type="hidden" name="aucObjDsc" value="<c:out value="${inputParam.aucObjDsc}" />" />
	<input type="hidden" name="sraIndvAmnno" value="<c:out value="${inputParam.sraIndvAmnno}" />" />		
	<input type="hidden" name="aucPrgSq" value="<c:out value="${inputParam.aucPrgSq}" />" />
	<input type="hidden" name="oslpNo" value="<c:out value="${inputParam.oslpNo}" />" />
	<input type="hidden" name="ledSqno" value="<c:out value="${inputParam.ledSqno}" />" />
	<input type="hidden" name="bidPopYn" value="<c:out value="${inputParam.bidPopYn}" />" />
	<input type="hidden" name="parentObj" value="<c:out value="${inputParam.parentObj}" />" />
	<input type="hidden" name="tabId" value="1" />
</form>
<form name="frm_parent" action="" method="post">
</form>
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
<div class="winpop cow-detail">
	<button type="button" class="winpop_back"><span class="sr-only">윈도우 팝업 닫기</span>
		<h2 class="winpop_tit">${subheaderTitle}</h2>
	</button>
	<div class="tab_list item-${tabList[0].TOT_CNT+1}" style="display:none;">
		<ul>
			<c:forEach items="${ tabList }" var="item" varStatus="st">			
				<c:if test="${item.VISIB_YN eq '1'}">
					<li><a href="javascript:;" class="cowTab_${item.SIMP_C }" data-tab-id="${item.SIMP_C }">${item.SIMP_CNM }</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area not mo-pd bloodInfo cowTab_1">
		<div class="save-box">
			<div class="info">
				<em>${inputParam.title} |</em>
				<em>${fn:substring(inputParam.sraIndvAmnno, 3, 6)} ${fn:substring(inputParam.sraIndvAmnno, 6, 10)} ${fn:substring(inputParam.sraIndvAmnno, 10, 14)} ${fn:substring(inputParam.sraIndvAmnno, 14, 15)} </em>
			</div>
		</div>
		<h3 class="tit2"><span class="subTxt" style="position: absolute;right: 10px;"></span></h3>
		<h3 class="tit top">혈통정보</h3>		
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
								<c:when test="${empty bloodInfo.FCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									[KPN] ${bloodInfo.FCOW_KPN_NO }
									<br/>${fn:substring(bloodInfo.FCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }"> 
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.FCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">조부</th>
						<td class="fz-32" name="blInfo_0">
							<c:choose>
								<c:when test="${empty bloodInfo.GRFCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									[KPN] ${bloodInfo.GRFCOW_KPN_NO }
									<br/>${fn:substring(bloodInfo.GRFCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }"> 
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.GRFCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">조모</th>
						<td class="fz-32" name="blInfo_2">
							<c:choose>
								<c:when test="${empty bloodInfo.GRMCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${fn:substring(bloodInfo.GRMCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.GRMCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
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
								<c:when test="${empty bloodInfo.MCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise> 
									${fn:substring(bloodInfo.MCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.MCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">외조부</th>
						<td class="fz-32" name="blInfo_3">
							<c:choose>
								<c:when test="${empty bloodInfo.MTGRFCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									[KPN] ${bloodInfo.MTGRFCOW_KPN_NO } 
									<br/>${fn:substring(bloodInfo.MTGRFCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.MTGRFCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">외조모</th>
						<td class="fz-32" name="blInfo_5">
							<c:choose>
								<c:when test="${empty bloodInfo.MTGRMCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${fn:substring(bloodInfo.MTGRMCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btn-search btnCowSearch" data-indv-no="${bloodInfo.MTGRMCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
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
	<!-- //tab_area e -->
	
	<div class="tab_area not mo-pd epdInfo cowTab_2" style="display:none;">				
		<h3 class="tit">유전능력(EPD)</h3>
		<p class="txt">개체 유전능력은 절대값이 아니므로 참고용으로 사용 하시기 바랍니다.</p>
		<div class="cow-basic cow-epd">
			<table class="table-detail">
				<colgroup>
					<col width="45%">
					<col width="35%">
					<col width="20%">
				</colgroup>
				<tbody>
					<tr>
						<th><i class="dot" style="background-color: #ffaf00;"></i>냉도체중(Kg)</th>
						<td name="reProduct1" class="ta-C bdr-y">${not empty bloodInfo.EPD_VAL_1 ? bloodInfo.EPD_VAL_1 : '-'}</td>
						<td class="ta-C dscReProduct1" name="dscReProduct1"><span class="c-blue">${not empty bloodInfo.EPD_GRD_1 ?bloodInfo.EPD_GRD_1 : '-'}</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #a4d509;"></i>배최장근(cm2)</th>
						<td name="reProduct2" class="ta-C bdr-y">${not empty bloodInfo.EPD_VAL_2 ? bloodInfo.EPD_VAL_2 : '-'}</td>
						<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${not empty bloodInfo.EPD_GRD_2 ? bloodInfo.EPD_GRD_2 : '-'}</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #5bacff;"></i>등지방두께(mm)</th>
						<td name="reProduct2" class="ta-C bdr-y">${not empty bloodInfo.EPD_VAL_3 ?bloodInfo.EPD_VAL_3 : '-'}</td>
						<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${not empty bloodInfo.EPD_GRD_3 ? bloodInfo.EPD_GRD_3 : '-'}</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #ff7bc2;"></i>근내지방도(점)</th>
						<td name="reProduct2" class="ta-C bdr-y">${not empty bloodInfo.EPD_VAL_4 ?bloodInfo.EPD_VAL_4 : '-'}</td>
						<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${not empty bloodInfo.EPD_GRD_4 ? bloodInfo.EPD_GRD_4 : '-'}</span></td>
					</tr>
				</tbody>
			</table>
		</div>
<!-- 		<div style="margin:20px 0;padding: 70px 0;background-color: #fff;text-align: center;font-weight: 700;"> -->
<!-- 			<canvas id="epdChart" style="width:100%;height: 320px"> -->
<!-- 			</canvas> -->
<!-- 		</div> -->
		<div class="info">
			육종가코드 :<br>
			각 형질별 육종가 순위를 A,B,C,D 4단계로 구분함
			<ul>
				<li>· A코드 : 육종가 순위 1 ~ 20% (아주 좋음)</li>
				<li>· B코드 : 육종가 순위 20 ~ 45% (좋음)</li>
				<li>· C코드 : 육종가 순위 45 ~ 70% (약간 나쁨)</li>
				<li>· D코드 : 육종가 순위 70 ~ 100% (나쁨)</li>
			</ul>
		</div>
	</div>
</div>


<script src="/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
var chart;
</script>