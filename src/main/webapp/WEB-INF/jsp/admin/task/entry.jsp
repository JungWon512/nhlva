<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
<style type="text/css">
	.modal-wrap.pop_exit_cow .modal-content table td {
		text-align:left !important;
	}
	input.pd5 {
		padding:5px;
	}
</style>
<!--begin::Container-->
<div class="container">
	<form name="frm" action="" method="post">
		<input type="hidden" name="regType" value="${params.regType}" />
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		
		<div class="list_search">
			<ul class="sch_area" style="height:50px;">
				<li class="date">
					<select name="aucObjDsc" id="aucObjDsc" style="width:100%;">
						<option value="" ${empty params.aucObjDsc ? 'selected' : ''}> 전체 </option>
						<option value="1" ${params.aucObjDsc eq '1' ? 'selected' : ''}> 송아지 </option>
						<option value="2" ${params.aucObjDsc eq '2' ? 'selected' : ''}> 비육우 </option>
						<option value="3" ${params.aucObjDsc eq '3' ? 'selected' : ''}> 번식우 </option>
					</select>
				</li>
			</ul>
			<ul class="sch_area">
				<li class="txt" style="width:70%;">
					<input type="text" name="searchTxt" maxlength="15" value="${params.searchTxt}" placeholder="바코드" pattern="\d*" inputmode="numeric"/>
				</li>
				<li class="btn" style="width:30%;">
					<a href="javascript:;" class="list_sch btn_search">검색</a>
				</li>
			</ul>
		</div>
		<div class="list_table schedule_tb_mo">
			<div class="list_head">
				<dl>
					<dd class="num">번호</dd>
					<c:choose>
						<c:when test="${params.regType eq 'W'}">
							<dd>중량</dd>
						</c:when>
						<c:when test="${params.regType eq 'L'}">
							<dd>하한가</dd>
						</c:when>
						<c:when test="${params.regType eq 'N'}">
							<dd>계류대</dd>
						</c:when>
					</c:choose>
					<dd>귀표</dd>
					<dd>출하자</dd>
				</dl>
			</div>
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
						<li>
							<dl>
								<dd class="num" data-amnno="${item.SRA_INDV_AMNNO}" data-auc-obj-dsc="${item.AUC_OBJ_DSC}" data-oslp-no="${item.OSLP_NO}" data-led-sqno="${item.LED_SQNO}">${item.AUC_PRG_SQ}</dd>
							<c:choose>
							<c:when test="${params.regType eq 'W'}">
								<dd>${fn:split(item.COW_SOG_WT,'.')[0]}</dd>
							</c:when>
							<c:when test="${params.regType eq 'L'}">
								<dd class="pd_pay textNumber">${(item.LOWS_SBID_LMT_AM eq '' || item.LOWS_SBID_LMT_AM == null || item.LOWS_SBID_LMT_AM <= 0 ) ? '-' : fn:split(item.LOWS_SBID_LMT_UPR,'.')[0]}</dd>
							</c:when>
							<c:when test="${params.regType eq 'N'}">
								<dd>계류대</dd>
							</c:when>
							</c:choose>
								<dd class="pd_ea">
									<a href="javascript:;"><span class="amnno">${item.SRA_INDV_AMNNO_FORMAT}</span></a>
								</dd>
								<dd class="pd_">${item.SRA_PDMNM_ORI}</dd>
							</dl>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="list_search">
			<ul class="sch_area">
				<li class="btn">
					<a href="javascript:;" class="list_sch btn_modify">변경</a>
				</li>
				<li class="btn">
					<a href="javascript:;" class="list_sch btn_back">이전</a>
				</li>
			</ul>
		</div>
	</form>
</div>
<!-- //auction_list e -->