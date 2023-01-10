<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<!--begin::Container-->
<!--${data} -->
<!--end::Container-->
<form name="frm" action="" method="post">
	<input type="hidden" name="place" value="${param.place}" />
	<input type="hidden" name="naBzplc" value="${param.naBzplc}" />
	<input type="hidden" name="aucDt" value="${param.aucDt}" />
	<input type="hidden" name="aucObjDsc" value="${param.aucObjDsc}" />
	<input type="hidden" name="sraIndvAmnno" value="${param.sraIndvAmnno}" />
	<input type="hidden" name="aucPrgSq" value="${param.aucPrgSq}" />
	<input type="hidden" name="oslpNo" value="${param.oslpNo}" />
	<input type="hidden" name="ledSqno" value="${param.ledSqno}" />
	<input type="hidden" name="bidPopYn" value="${inputParam.bidPopYn}" />
</form>
<div class="winpop cow-detail">
	<button type="button" class="winpop_close"><span class="sr-only">윈도우 팝업 닫기</span></button>
	<h2 class="winpop_tit">${subheaderTitle}</h2>
	<div class="tab_list item-${tabList[0].TOT_CNT+1}" style="display:none;">
		<ul>
			<c:forEach items="${ tabList }" var="item" varStatus="st">			
				<c:if test="${item.VISIB_YN eq '1'}">
					<li><a 0href="javascript:;" class="cowTab_${item.SIMP_C }" data-tab-id="${item.SIMP_C }">${item.SIMP_CNM }</a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- //tab_list e -->
	<div class="tab_area not mo-pd bloodInfo cowTab_1">	
		<h3 class="tit">혈통정보</h3>
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
						<td colspan="2" class="bg-gray fz-32 fCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.MIF_FCOW_SRA_INDV_EART_NO_FORMAT }">
								</c:when>
								<c:otherwise>
									[KPN] ${infoData.KPN_NO } 
									<br/>${infoData.MIF_FCOW_SRA_INDV_EART_NO_FORMAT }
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">조부</th>
						<td class="fz-32 grfCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.GRFA_SRA_INDV_EART_NO_FORMAT }">
								</c:when>
								<c:otherwise>
									[KPN] ${infoData.GRFA_SRA_KPN_NO }  
									<br/>${infoData.GRFA_SRA_INDV_EART_NO_FORMAT }
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">조모</th>
						<td class="fz-32 grmCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.GRMO_SRA_INDV_EART_NO_FORMAT }">
								</c:when>
								<c:otherwise>
									${infoData.GRMO_SRA_INDV_EART_NO_FORMAT } 
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
						<td colspan="2" class="bg-gray fz-32 mCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.MCOW_SRA_INDV_AMNNO_FORMAT }">
								</c:when>
								<c:otherwise>
									${infoData.MCOW_SRA_INDV_AMNNO_FORMAT }
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">외조부</th>
						<td class="fz-32 mgrfCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.MTGRFA_SRA_INDV_EART_NO_FORMAT }">
								</c:when>
								<c:otherwise>
									[KPN] ${infoData.MTGRFA_SRA_KPN_NO } 
									<br/>${infoData.MTGRFA_SRA_INDV_EART_NO_FORMAT }
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr>
						<th colspan="2">외조모</th>
						<td class="fz-32 mgrmCowSraIndvData">
							<c:choose>
								<c:when test="${empty infoData.MTGRMO_SRA_INDV_EART_NO_FORMAT }">
								</c:when>
								<c:otherwise>
									${infoData.MTGRMO_SRA_INDV_EART_NO_FORMAT }
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<h3 class="tit">형매정보</h3>
		<div class="cow-sibiling">
			<table class="table-detail sibIndvTable">
				<colgroup>
					<col width="13%">
					<col width="28%">
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
					<tr>
						<td rowspan="2" colspan="5" class="ta-C"></td>
					</tr>
					<c:forEach items="${ sibList }" var="item" varStatus="st">
						<tr>
							<td rowspan="2" class="ta-C bg-gray">${item.MATIME }</td>
							<td rowspan="2" class="ta-C">${item.SIB_SRA_INDV_AMNNO_STR }</td>
							<td class="ta-C">${item.RG_DSC_NAME }</td>
							<td class="ta-C">${item.INDV_SEX_C_NAME }</td>
							<td class="ta-C">${item.BIRTH_STR }</td>
						</tr>
						<tr>
							<td class="ta-C">${item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</td>
							<td class="ta-C">${item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</td>
							<td class="ta-C">${item.MIF_BTC_DT}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>	
		<h3 class="tit">후대정보</h3>
		<div class="cow-sibiling">
			<table class="table-detail postIndvTable">
				<colgroup>
					<col width="13%">
					<col width="28%">
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
					<tr>
						<td rowspan="2" colspan="5" class="ta-C"></td>
					</tr>
					<c:forEach items="${ postList }" var="item" varStatus="st">
						<tr>
							<td rowspan="2" class="ta-C bg-gray">${item.MATIME }</td>
							<td rowspan="2" class="ta-C">${item.POST_SRA_INDV_AMNNO_STR }</td>
							<td class="ta-C">${item.RG_DSC_NAME }</td>
							<td class="ta-C">${item.INDV_SEX_C_NAME }</td>
							<td class="ta-C">${item.BIRTH_STR }</td>
						</tr>
						<tr>
							<td class="ta-C">${item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</td>
							<td class="ta-C">${item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</td>
							<td class="ta-C">${item.MIF_BTC_DT}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
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
						<td name="reProduct1" class="ta-C bdr-y">${infoData.RE_PRODUCT_1 }</td>
						<td class="ta-C dscReProduct1" name="dscReProduct1"><span class="c-blue">${infoData.RE_PRODUCT_1_1 }</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #a4d509;"></i>배최장근(cm2)</th>
						<td name="reProduct2" class="ta-C bdr-y">${infoData.RE_PRODUCT_2}</td>
						<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${infoData.RE_PRODUCT_2_1 }</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #5bacff;"></i>등지방두께(mm)</th>
						<td name="reProduct3" class="ta-C bdr-y">${infoData.RE_PRODUCT_3 }</td>
						<td class="ta-C dscReProduct3" name="dscReProduct3"><span class="c-blue">${infoData.RE_PRODUCT_3_1 }</span></td>
					</tr>
					<tr>
						<th><i class="dot" style="background-color: #ff7bc2;"></i>근내지방도(점)</th>
						<td name="reProduct4" class="ta-C bdr-y">${infoData.RE_PRODUCT_4 }</td>
						<td class="ta-C dscReProduct4" name="dscReProduct4"><span class="c-blue">${infoData.RE_PRODUCT_4_1 }</span></td>
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