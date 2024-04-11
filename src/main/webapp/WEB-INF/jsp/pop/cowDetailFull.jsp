<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<link rel="stylesheet" type="text/css" href="/static/css/new_style.css" />
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
		<h3 class="tit2"><span class="subTxt" style="font-size: 13px;color:red;">※해당정보는 참고용으로,최종구매전 종축개량협회 홈페이지에서 확인하시기바랍니다.</span></h3>
		<h3 class="tit2"><span class="subTxt" style="position: absolute;right: 10px;"></span></h3>
		<h3 class="tit mb10">혈통</h3>
		<div class="newGrapy">
		    <div class="item line-type">
		        <dl>
		            <dt>조부</dt>
		            <dd>
		                <span>
							<c:choose>
								<c:when test="${empty bloodInfo.GRFCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${bloodInfo.GRFCOW_KPN_NO }
									<br/>${fn:substring(bloodInfo.GRFCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }"> 
										<button type="button" class="btnCowF btn-search btnCowSearch" data-indv-bld-dsc="GF" data-indv-no="${bloodInfo.GRFCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
						</span>	
		            </dd>
		        </dl>
		    </div>
		    <div class="item line-type">
		        <dl>
		            <dt>외조부</dt>
		            <dd>
		                <span>
							<c:choose>
								<c:when test="${empty bloodInfo.MTGRFCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${bloodInfo.MTGRFCOW_KPN_NO } 
									<br/>${fn:substring(bloodInfo.MTGRFCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btnCowM btn-search btnCowSearch" data-indv-bld-dsc="MGF" data-indv-no="${bloodInfo.MTGRFCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
		                </span>
		            </dd>
		        </dl>
		    </div>
		    <div class="item line-type">
		        <dl>
		            <dt>조모</dt>
		            <dd>
		            	<span>            	
							<c:choose>
								<c:when test="${empty bloodInfo.GRMCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${fn:substring(bloodInfo.GRMCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btnCowF btn-search btnCowSearch" data-indv-bld-dsc="GM" data-indv-no="${bloodInfo.GRMCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
		            	</span>
		            </dd>
		        </dl>
		    </div>
		    <div class="item line-type">
		        <dl>
		            <dt>외조모</dt>
		            <dd>
		            	<span>
							<c:choose>
								<c:when test="${empty bloodInfo.MTGRMCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${fn:substring(bloodInfo.MTGRMCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btnCowM btn-search btnCowSearch" data-indv-bld-dsc="MGM" data-indv-no="${bloodInfo.MTGRMCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
		            	</span>
		            </dd>
		        </dl>
		    </div>
		    <div class="item bg-type">
		        <dl>
		            <dt>부</dt>
		            <dd>
		                <span>
							<c:choose>
								<c:when test="${empty bloodInfo.FCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise>
									${bloodInfo.FCOW_KPN_NO }<br/>${fn:substring(bloodInfo.FCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }"> 
										<button type="button" class="btnCowF btn-search btnCowSearch" data-indv-bld-dsc="F" data-indv-no="${bloodInfo.FCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
		                </span>
		            </dd>
		        </dl>
		    </div>
		    <div class="item bg-type">
		        <dl>
		            <dt>모</dt>
		            <dd>
		            	<span>
							<c:choose>
								<c:when test="${empty bloodInfo.MCOW_SRA_INDV_AMNNO }">
									-
								</c:when>
								<c:otherwise> 
									${fn:substring(bloodInfo.MCOW_SRA_INDV_AMNNO, 3, 15)}
									<c:if test="${'N' eq inputParam.bidPopYn }">
										<button type="button" class="btnCowM btn-search btnCowSearch" data-indv-bld-dsc="M" data-indv-no="${bloodInfo.MCOW_SRA_INDV_AMNNO}"><span class="sr-only">검색</span></button>
									</c:if>
								</c:otherwise>
							</c:choose>
		            	</span>
		            </dd>
		        </dl>
		    </div>
		</div>
		<h3 class="tit">형매정보</h3>
		
		<div class="tbl_SwpBox scroll_wrap left_fixdTbl">
		    <div class="fixdCellGrup">
		        <div class="fixCell_list" style="width: 660px">
		            <table class="swipTble cow">
		                <caption>
		                    형매정보
		                </caption>
		                <colgroup>
		                    <col style="width: 80px" />
		                    <col style="width: 40px" />
		                    <col style="width: 70px" />
		                    <col style="width: 40px" />
		                    <col style="width: 98px" />
		                    <col style="width: 88px" />
		                    <col style="width: 60px" />
		                    <col style="width: 60px" />
		                    <col style="width: 100px" />
		                </colgroup>
		                <thead>
		                    <tr>
		                        <th class="fixd_box thBg">개체번호</th>
		                        <th>산차</th>
		                        <th>혈통구분</th>
		                        <th>성별</th>
		                        <th>생년월일</th>
		                        <th>씨수소명</th>
		                        <th>육질등급</th>
		                        <th>도체중</th>
		                        <th>도축일</th>
		                    </tr>
		                </thead>
		                <tbody>
							<c:if test="${ sibList.size() == '0' }">
								<tr>
									<td rowspan="2" colspan="9" class="ta-C">-</td>
								</tr>
							</c:if>
							<c:forEach items="${ sibList }" var="item" varStatus="st">
		                    	<tr>
		                        	<th class="fixd_box tdBg"><strong>${item.SIB_SRA_INDV_AMNNO_STR }</strong></th>
			                        <td><strong>${item.MATIME }</strong></td>
		    	                    <td><strong>${item.RG_DSC_NAME }</strong></td>
		        	                <td><strong>${item.INDV_SEX_C_NAME }</strong></td>
		            	            <td><strong>${not empty item.BIRTH_STR ? item.BIRTH_STR :'-' }</strong></td>
		                	        <td><strong>${not empty item.KPN_NO ? item.KPN_NO : '-'}</strong></td>
		                	        <td><strong>${not empty item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</strong></td>
		                	        <td><strong>${not empty item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</strong></td>
		                	        <td><strong>${not empty item.MIF_BTC_DT_STR ? item.MIF_BTC_DT_STR : '-'}</strong></td>
		                    	</tr>
							</c:forEach>
		                </tbody>
		            </table>
		        </div>
		    </div>
		</div>
		
		<h3 class="tit">후대정보</h3>
		
		<div class="tbl_SwpBox scroll_wrap left_fixdTbl">
		    <div class="fixdCellGrup">
		        <div class="fixCell_list" style="width: 660px">
		            <table class="swipTble cow">
		                <caption>
		                    형매정보
		                </caption>
		                <colgroup>
		                    <col style="width: 80px" />
		                    <col style="width: 40px" />
		                    <col style="width: 70px" />
		                    <col style="width: 40px" />
		                    <col style="width: 98px" />
		                    <col style="width: 88px" />
		                    <col style="width: 60px" />
		                    <col style="width: 60px" />
		                    <col style="width: 100px" />
		                </colgroup>
		                <thead>
		                    <tr>
		                        <th class="fixd_box thBg">개체번호</th>
		                        <th>산차</th>
		                        <th>혈통구분</th>
		                        <th>성별</th>
		                        <th>생년월일</th>
		                        <th>씨수소명</th>
		                        <th>육질등급</th>
		                        <th>도체중</th>
		                        <th>도축일</th>
		                    </tr>
		                </thead>
		                <tbody>
							<c:if test="${ postList.size() == '0' }">
								<tr>
									<td rowspan="2" colspan="9" class="ta-C">-</td>
								</tr>
							</c:if>
							<c:forEach items="${ postList }" var="item" varStatus="st">
		                    	<tr>
		                        	<th class="fixd_box tdBg"><strong>${item.POST_SRA_INDV_AMNNO_STR }</strong></th>
			                        <td><strong>${item.MATIME }</strong></td>
		    	                    <td><strong>${item.RG_DSC_NAME }</strong></td>
		        	                <td><strong>${item.INDV_SEX_C_NAME }</strong></td>
		            	            <td><strong>${not empty item.BIRTH_STR ? item.BIRTH_STR :'-' }</strong></td>
		                	        <td><strong>${not empty item.KPN_NO ? item.KPN_NO : '-'}</strong></td>
		                	        <td><strong>${not empty item.METRB_METQLT_GRD ? item.METRB_METQLT_GRD :'-' }</strong></td>
		                	        <td><strong>${not empty item.METRB_BBDY_WT ? item.METRB_BBDY_WT :'-' }</strong></td>
		                	        <td><strong>${not empty item.MIF_BTC_DT_STR ? item.MIF_BTC_DT_STR : '-'}</strong></td>
		                    	</tr>
							</c:forEach>
		                </tbody>
		            </table>
		        </div>
		    </div>
		</div>
<!-- //240306 혈통/후대  -->
	<!-- //tab_area e -->
	
	<div class="tab_area not mo-pd epdInfo cowTab_2" style="display:none;">					
		<h3 class="tit">유전능력(EPD)</h3>
		<p class="txt">개체 유전능력은 절대값이 아니므로 참고용으로 사용 하시기 바랍니다.</p>		
		<span class="sub_info_txt">유전체분석 미수행 개체</span>
		<table class="sub_tble">
		    <caption>
		        대상형질, 평가수치, 평가등급
		    </caption>
		    <colgroup>
		        <col width="" />
		        <col width="90" />
		        <col width="80" />
		    </colgroup>
		    <thead>
		        <tr>
		            <th>대상형질</th>
		            <th class="txtR">평가수치</th>
		            <th>평가등급</th>
		        </tr>
		    </thead>
		    <tbody>
		        <tr>
		            <td><b class="new_mark"></b>도체중(kg)</td>
		            <td class="txtR" name="dscReProduct1">${not empty bloodInfo.EPD_VAL_1 ? bloodInfo.EPD_VAL_1 : '-'}</td>
		            <td><span class="tag${bloodInfo.EPD_GRD_1}">${not empty bloodInfo.EPD_GRD_1 ?bloodInfo.EPD_GRD_1 : '-'}</span></td>
		        </tr>
		        <tr>
		            <td>배최장근(㎠)</td>
		            <td class="txtR" name="dscReProduct2">${not empty bloodInfo.EPD_VAL_2 ? bloodInfo.EPD_VAL_2 : '-'}</td>
		            <td><span class="tag${bloodInfo.EPD_GRD_2}">${not empty bloodInfo.EPD_GRD_2 ?bloodInfo.EPD_GRD_2 : '-'}</span></td>
		        </tr>
		        <tr>
		            <td>등지방두께(㎜)</td>
		            <td class="txtR" name="dscReProduct3">${not empty bloodInfo.EPD_VAL_3 ? bloodInfo.EPD_VAL_3 : '-'}</td>
		            <td><span class="tag${bloodInfo.EPD_GRD_3}">${not empty bloodInfo.EPD_GRD_3 ?bloodInfo.EPD_GRD_3 : '-'}</span></td>
		        </tr>
		        <tr>
		            <td>근내지방도</td>
		            <td class="txtR" name="dscReProduct4">${not empty bloodInfo.EPD_VAL_4 ? bloodInfo.EPD_VAL_4 : '-'}</td>
		            <td><span class="tag${bloodInfo.EPD_GRD_4}">${not empty bloodInfo.EPD_GRD_4 ?bloodInfo.EPD_GRD_4 : '-'}</span></td>
		        </tr>
		    </tbody>
		</table>
		
		<div class="tag-info">
		    <dl class="tag-tit">
		        <dt>육종가 코드</dt>
		        <dd>각 형지별 육종가 순위를 A, B, C, D 단계로 구분함</dd>
		    </dl>
		    <table>
		        <colgroup>
		            <col style="width: 80px" />
		            <col />
		        </colgroup>
		        <tr>
		            <th><i class="tagA"></i><span>A코드</span></th>
		            <td>육종가 순위 1 ~ 20% (아주 좋음)</td>
		        </tr>
		        <tr>
		            <th><i class="tagB"></i><span>B코드</span></th>
		            <td>육종가 순위 20 ~ 45% (좋음)</td>
		        </tr>
		        <tr>
		            <th><i class="tagC"></i><span>C코드</span></th>
		            <td>육종가 순위 45 ~ 70% (약간 나쁨)</td>
		        </tr>
		        <tr>
		            <th><i class="tagD"></i><span>D코드</span></th>
		            <td>육종가 순위 70 ~ 100% (나쁨)</td>
		        </tr>
		    </table>
		</div>
	</div>
</div>


<script src="/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
var chart;
</script>