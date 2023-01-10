<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<!--  admin_simple_reg [s] -->
<div class="admin_simple_reg">
	<!-- regBox [s] -->
	<div class="regBox">
		<form name="frm" action="" method="post" autocomplete="on">
			<input type="hidden" name="naBzplc" value="<sec:authentication property="principal.naBzplc"/>" />
			<input type="hidden" name="sraIndvAmnno" id="sraIndvAmnno" value="" />
			<input type="hidden" name="aucObjDsc" id="aucObjDsc" value="${empty params.aucObjDsc ? '1' : params.aucObjDsc}" />
			
			<!-- date [s] -->
			<dl class="date">
				<dt>경매일자</dt>
				<dd class="choice-date">
					<select name="aucDt" id="aucDt">
						<option value="" selected="selected"> 선택 </option>
						<c:forEach items="${dateList}" var="vo">
							<option value="${vo.AUC_DT}" ${params.aucDt eq vo.AUC_DT ? 'selected' : ''} data-auc-obj-dsc="${vo.AUC_OBJ_DSC}">${vo.AUC_DT_STR}</option>
						</c:forEach>
					</select>
				</dd>
				<dd>
					<div class="cow-btns">
						<button type="button" class="${(empty params.aucObjDsc or params.aucObjDsc eq '1') ? 'on' : ''}" data-auc-obj-dsc="1">송아지</button>
						<button type="button" class="${params.aucObjDsc eq '2' ? 'on' : ''}" data-auc-obj-dsc="2">비육우</button>
						<button type="button" class="${params.aucObjDsc eq '3' ? 'on' : ''}" data-auc-obj-dsc="3">번식우</button>
					</div>
				</dd>
			</dl>
			<!-- date [e] -->
			<dl>
				<dt>개체번호 입력</dt>
				<dd>
					<div class="num_input">
						<span class="num">410</span>
						<input type="text" name="subSraIndvAmnno" id="subSraIndvAmnno" placeholder="귀표번호" maxlength="12" pattern="\d*" inputmode="numeric" />
						<button type="button" class="btn_del"></button>
					</div>
				</dd>
			</dl>
		</form>
	</div>
	<!-- regBox [e] -->
	<button type="button" class="btn-search">조회하기</button>
</div>
<!--  admin_simple_reg [e] -->