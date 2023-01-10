<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!-- admin_new_reg [s] -->
<div class="admin_new_reg">
	<form name="frm" action="" method="post">
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
		
		<!-- sub_search[s] -->
		<div class="sub_search">
			<ul class="sch_area">
				<li class="sort">
					<select name="aucObjDsc" id="aucObjDsc">
						<option value="" ${empty params.aucObjDsc ? 'selected' : ''}> 전체 </option>
						<option value="1" ${params.aucObjDsc eq '1' ? 'selected' : ''}> 송아지 </option>
						<option value="2" ${params.aucObjDsc eq '2' ? 'selected' : ''}> 비육우 </option>
						<option value="3" ${params.aucObjDsc eq '3' ? 'selected' : ''}> 번식우 </option>
					</select>
				</li>
				<li class="barcoad full">
					<div class="barcoad_input">
						<input type="text" name="searchTxt" maxlength="15" value="${params.searchTxt}" placeholder="바코드/경매번호" pattern="\d*" inputmode="numeric" />
						<button type="button" class="btn_input_reset">X</button>
					</div>
				</li>
				<li class="btn">
					<a href="javascript:;" class="btn_search">검색</a>
				</li>
			</ul>
		</div>
		<!-- sub_search[e] -->
		
		<!-- top-btns [s] -->
		<div class="top-btns">
			<button type="button" class="btn_chg_result">낙찰변경</button>
			<button type="button" class="btn_modify">출장우 수정</button>
			<button type="button" class="btn_regist">신규 등록</button>
		</div>
		<!-- top-btns [e] -->
		
		<!-- list_table [s] -->
		<div class="list_table admin_new_reg2">
			<!-- list_head[s] -->
			<div class="list_head">
				<dl>
					<dd class="col1">선택</dd>
					<dd class="col2">번호</dd>
					<dd class="col3">귀표</dd>
					<dd class="col4">출하자</dd>
					<dd class="col5">성별</dd>
					<dd class="col6">사진</dd>
				</dl>
			</div>
			<!-- list_head[e] -->
			<!-- list_body[s] -->
			<div class="list_body">
				<ul>
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
								<dd class="col1" >
									<input type="checkbox" name="oslpNo" id="oslpNo_${st.index}" value="${item.OSLP_NO}" 
										   data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-led-sqno="${item.LED_SQNO}" />
									<label for="oslpNo_${st.index}"></label>
								</dd>
								<dd class="col2">${item.AUC_PRG_SQ}</dd>
								<dd class="col3">${item.SRA_INDV_AMNNO_FORMAT}</dd>
								<dd class="col4">${item.FTSNM_ORI}</dd>
								<dd class="col5">${item.INDV_SEX_C_NAME}</dd>
								<dd class="col6">
									<button type="button" class="btn_image btn-reg-pic" data-oslp-no="${item.OSLP_NO}"
											data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-led-sqno="${item.LED_SQNO}" >
									<c:choose>
										<c:when test="${empty item.IMG_CNT or item.IMG_CNT eq '0'}">이미지</c:when>
										<c:otherwise>${item.IMG_CNT} 장</c:otherwise>
									</c:choose>
									</button>
								</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
			<!-- list_body[e] -->
		</div>
		<!-- list_table [e] -->
	</form>
</div>
<!-- admin_new_reg [e] -->