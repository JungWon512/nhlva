<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!-- 240306 혈통/후대  -->
<style>
    div.save-box {
        text-align: center;
        font-size: 18px;
    }
    div.save-box .info em {
        color: #1a1a1a;
        font-weight: 700;
    }
	.btnCowM.btn-search {
		background-image: url(/static/images/guide/v2/ico-search-pink.svg);
	}
	.btnCowF.btn-search {
		background-image: url(/static/images/guide/v2/ico-search-blue.svg);
	}
</style>
<div class="save-box">
    <div class="info">
		<em>${infoData.AUC_OBJ_DSC_NAME} |</em>
		<em>${infoData.INDV_SEX_C_NAME} |</em>
		<em>${fn:substring(infoData.SRA_INDV_AMNNO, 3, 6)} ${fn:substring(infoData.SRA_INDV_AMNNO, 6, 10)} ${fn:substring(infoData.SRA_INDV_AMNNO, 10, 14)} ${fn:substring(infoData.SRA_INDV_AMNNO, 14, 15)} </em>
    </div>
</div>
<fmt:parseDate value="${bloodInfo.LSCHG_DT}" pattern="yyyyMMdd" var="tmpSyncDt" />
<fmt:formatDate value="${tmpSyncDt}" pattern="yyyy-MM-dd" var="syncDt" />

<h3 class="tit mb10">혈통</h3>
<h3 class="tit2" style="margin-bottom:15px;"><span class="subTxt" style="font-size: 13px">※한국종축개량협회제공일 : ${syncDt}</span></h3>
<!-- <h3 class="tit2"><span class="subTxt" style="font-size: 13px;color:red;">※한국종축개량협회제공데이터입니다.ㅇㅁ럼ㄴ이람ㄴㅇ리ㅏ</span></h3> -->
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
                    <col style="width: 90px" />
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
                    <col style="width: 90px" />
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
<!-- //240306 혈통/후대  -->
