<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<div class="admin_auc_main">
	<h3>경매일자 입력</h3>
	<div class="main_search">
		<ul class="radio_group">
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra1" value="" checked="checked"><label for="ra1">전체</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra2" value="1"><label for="ra2">송아지</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra3" value="2"><label for="ra3">비육우</label>
			</li>
			<li>
				<input type="radio" name="searchAucObjDsc" id="ra4" value="3"><label for="ra4">번식우</label>
			</li>
		</ul>
		<ul class="sch_area">
			<li class="date">
				<select name="searchDate" id="searchDate" style="width:100%;">
					<option value="" selected="selected"> 선택 </option>
					<c:forEach items="${dateList}" var="vo">
						<option value="${vo.AUC_DT}" data-auc-obj-dsc="${vo.AUC_OBJ_DSC}">${vo.AUC_DT_STR}</option>
					</c:forEach>
				</select>
			</li>
			<li class="btn">
				<a href="javascript:;" class="list_sch btn_start">시작</a>
				<a href="javascript:;" class="list_sch btn_end">종료</a>
			</li>
		</ul>
	</div>
</div>