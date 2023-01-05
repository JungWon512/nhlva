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
	/* div.list_body ul{height: 410px !important;} */
</style>

<!--  admin_weight_list [s] -->
<div class="${ params.regType eq 'AWL' ? 'admin_weight_list admin_all_reg' : 'admin_weight_list'}">
	<form name="frm" action="" method="post">
		<input type="hidden" name="regType" value="${params.regType}" />
		<input type="hidden" name="aucDt" value="${params.aucDt}" />
		<input type="hidden" name="naBzplc" value="${params.naBzplc}" />
		
		<c:if test="${ params.regType eq 'AWL' }">
			<div class="tab_list">
				<ul class="entry tab_2">
					<li><a href="javascript:;" class="${ params.regType eq 'AWL' || params.regType eq 'AW' ? 'act' : '' }" data-tab-id="AW">중량 등록</a></li>
					<li><a href="javascript:;" class="${ params.regType eq 'AL' ? 'act' : '' }" data-tab-id="AL">예정가 등록</a></li>
				</ul>
			</div>
		</c:if>
		
		<!-- sub_search [s] -->
		<div class="sub_search">
			<!-- sch_area [s] -->
			<ul class="sch_area">
				<c:if test="${params.regType ne 'SMCOW'}">
				<li class="sort">
					<select name="aucObjDsc" id="aucObjDsc">
						<option value="" ${empty params.aucObjDsc ? 'selected' : ''}> 전체 </option>
						<option value="1" ${params.aucObjDsc eq '1' ? 'selected' : ''}> 송아지 </option>
						<option value="2" ${params.aucObjDsc eq '2' ? 'selected' : ''}> 비육우 </option>
						<option value="3" ${params.aucObjDsc eq '3' ? 'selected' : ''}> 번식우 </option>
					</select>
				</li>
				</c:if>
				<c:if test="${ params.regType ne 'AW' && params.regType ne 'AL' && params.regType ne 'AWL' }">
				<li class="barcoad">
					<p>바코드</p>
					<div class="barcoad_input">
						<input type="text" name="searchTxt" maxlength="15" value="${params.searchTxt}" placeholder="바코드/경매번호" pattern="\d*" inputmode="numeric" />
						<button type="button" class="btn_input_reset">X</button>
					</div>
				</li>
				</c:if>
				<li class="btn">
					<a href="javascript:;" class="btn_search">조회</a>
				</li>
			</ul>
			<!-- sch_area [e] -->
		</div>
		<!-- sub_search [e] -->
		<!-- list_table [s] -->
		<c:choose>
			<c:when test="${ params.regType eq 'AW' || params.regType eq 'AL' || params.regType eq 'AWL' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_awl.jsp" %>
			</c:when>
			<c:when test="${ params.regType eq 'LW' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_lw.jsp" %>
			</c:when>
			<c:when test="${ params.regType eq 'SB' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_sb.jsp" %>
			</c:when>
			<c:when test="${ params.regType eq 'SCOW' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_scow.jsp" %>
			</c:when>
			<c:when test="${ params.regType eq 'SMCOW' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_smcow.jsp" %>
			</c:when>
			<c:when test="${ params.regType eq 'N' }">
				<%@ include file="/WEB-INF/jsp/admin/task/entry_n.jsp" %>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>
		<!-- list_table [e] -->
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