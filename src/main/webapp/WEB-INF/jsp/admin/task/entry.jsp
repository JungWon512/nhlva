<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<style type="text/css">
	div.list_body input{padding-right:8px; text-align:right;border-radius:5px;font-size:16px;}
	div.barcoad_input input{font-size:20px;padding-right:0px !important;}
	div.barcoad_input input::placeholder{font-size:15px;}
/* 	li.barcoad p{font-size:18px !important;} */
	dd.col5 button{
		display: inline-block;
		width: 75%;
		text-align: center;
		margin: -5px 5px;
		padding: 0 5px;
		background: #007eff;
		color: #fff;
		font-size: 16px;
		font-family: "Noto Sans KR", "Roboto", "AppleGothic", "sans-serif";
		font-weight: 500;
		letter-spacing: 0.5px;
		border-radius: 5px;
		-webkit-border-radius: 5px;
		line-height:30px;
	}
	div.list_body ul{height: 410px !important;}
</style>

<!--  admin_weight_list [s] -->
<div class="admin_weight_list">
	<form name="frm" action="" method="post">
		<input type="hidden" name="regType" value="${params.regType}" />
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="naBzplc" value="${params.naBzplc}" />
		
		<!-- sub_search [s] -->
		<div class="sub_search">
			<!-- sch_area [s] -->
			<ul class="sch_area">
				<li class="sort">
					<select name="aucObjDsc" id="aucObjDsc">
						<option value="" ${empty params.aucObjDsc ? 'selected' : ''}> 전체 </option>
						<option value="1" ${params.aucObjDsc eq '1' ? 'selected' : ''}> 송아지 </option>
						<option value="2" ${params.aucObjDsc eq '2' ? 'selected' : ''}> 비육우 </option>
						<option value="3" ${params.aucObjDsc eq '3' ? 'selected' : ''}> 번식우 </option>
					</select>
				</li>
				<li class="barcoad">
					<p>바코드</p>
					<div class="barcoad_input">
						<input type="text" name="searchTxt" maxlength="15" value="${params.searchTxt}" placeholder="바코드/경매번호" pattern="\d*" inputmode="numeric" />
						<button type="button" class="btn_input_reset">X</button>
					</div>
				</li>
				<li class="btn">
					<a href="javascript:;" class="btn_search">검색</a>
				</li>
			</ul>
			<!-- sch_area [e] -->
		</div>
		<!-- sub_search [e] -->
		<!-- list_table [s] -->
		<div class="list_table">
			<div class="list_head">
				<dl>
					<dd class="col1">번호</dd>
					<c:choose>
						<c:when test="${params.regType eq 'W' or params.regType eq 'AW'}">
							<dd class="col2"><span class="txt_org">중량</span></dd>
						</c:when>
						<c:when test="${params.regType eq 'L'}">
							<dd class="col2"><span class="txt_org">하한가</span></dd>
						</c:when>
						<c:when test="${params.regType eq 'N'}">
							<dd class="col2"><span class="txt_org">계류대</span></dd>
						</c:when>
					</c:choose>
					<dd class="col3">귀표</dd>
					<dd class="col4">출하자</dd>
					<c:if test="${params.regType ne 'AW'}">
						<dd class="col4">수정</dd>
					</c:if>
				</dl>
			</div>
			<div class="list_body">
				<ul style="overflow-y:scroll;">
					<c:if test="${entryList.size() <= 0}">
						<li>
							<dl>
								<dd>검색결과가 없습니다.</dd>
							</dl>
						</li>
					</c:if>
					<c:forEach items="${entryList}" var="item" varStatus="st">
						<li id="${item.AUC_PRG_SQ}">
							<dl>
								<dd class="col1" data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-oslp-no="${item.OSLP_NO}" data-led-sqno="${item.LED_SQNO}">${item.AUC_PRG_SQ}</dd>
							<c:choose>
							<c:when test="${params.regType eq 'W'}">
								<dd class="col2">${fn:split(item.COW_SOG_WT,'.')[0] eq '0' ? '-' : fn:split(item.COW_SOG_WT,'.')[0]}</dd>
							</c:when>
							<c:when test="${params.regType eq 'AW'}">
								<dd class="col2">
<%-- 									<input type="text" name="cowSogWt" id="cowSogWt${item.AUC_PRG_SQ}" class="onlyNumber" value="${fn:split(item.COW_SOG_WT,'.')[0]}" maxlength="4" pattern="\d*" inputmode="numeric" onfocus="$(this).setCursorPosition(0); return false;"/> --%>
									<input type="text" name="cowSogWt" id="cowSogWt${item.AUC_PRG_SQ}" class="onlyNumber" value="${fn:split(item.COW_SOG_WT,'.')[0]}" maxlength="4" pattern="\d*" inputmode="numeric"/>
								</dd>
							</c:when>
							<c:when test="${params.regType eq 'L'}">
								<dd class="col2">
									<c:choose>
										<c:when test="${item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0}">-</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}" type="number" />
										</c:otherwise>
									</c:choose>
								</dd>
							</c:when>
							<c:when test="${params.regType eq 'N'}">
								<dd class="col2" style="${item.MODL_NO != item.AUC_PRG_SQ ? 'color:#ff0000':''}">${item.MODL_NO}</dd>
							</c:when>
							</c:choose>
								<dd class="col3">${item.SRA_INDV_AMNNO_FORMAT}</dd>
								<dd class="col4">${item.FTSNM_ORI}</dd>
								<c:if test="${params.regType ne 'AW'}">
									<dd class="col4 col5"><button type="button" class="btn_modify">수정</button></dd>
								</c:if>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- list_table [s] -->
		<!-- btn_area [s] -->
		<c:if test="${params.regType ne 'AW'}">
			<!-- div class="btn_area">
				<a href="javascript:;" class="list_sch btn_modify_pop">변경</a>
				<a href="javascript:;" class="list_sch btn_back">이전</a>
			</div -->
		</c:if>
		<!-- btn_area [e] -->
	</form>
</div>
<!--  admin_weight_list [s] -->