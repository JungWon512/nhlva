<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<div class="board-new">
	<div class="box-select ver-place">
		<div>
			<select name="loc" id="loc">
				<option value="">전체</option>
				<c:forEach items="${ bizList }" var="item" varStatus="st">
					<option value="${item.NA_BZPLCLOC }">${item.NA_BZPLCLOC_NM }</option>
				</c:forEach>
			</select>	
		</div>
		<div>
			<select name="bzLoc" id="bzLoc">
				<option value="">전체</option>
			</select>	
		</div>
	</div>
	<div class="box-select ver-date">
		<div>
			<input type="text" name="stSearchDt" id="stSearchDt" value="" class=>	
		</div>
		<div>
			<input type="text" name="edSearchDt" id="edSearchDt" value="" class="">
		</div>
	</div>
	<div class="box-select ver-cow aucObjDscBtn">
		<button type="button" name="btnAucObjDscAll" value="" class="on">전체</button>
		<button type="button" name="btnAucObjDsc1" value="1">송아지</button>
		<button type="button" name="btnAucObjDsc2" value="2">비육우</button>
		<button type="button" name="btnAucObjDsc3" value="3">번식우</button>
	</div>
	<div class="box-btns searchBtn">
		<button type="button" name="btnInit">초기화</button>
		<button type="button" name="btnSearch">조회</button>
		<button type="button" name="btnExcelDown">엑셀다운</button>
	</div>
	<ul class="simple-list cnt_list">
		<li class="qcnCnt">
			<strong>개장횟수</strong>
			<em><span>-</span> 건</em>
		</li>
		<li class="cowCnt">
			<strong>출장두수</strong>
			<em><span>-</span> 두</em>
		</li>
		<li class="bidCnt">
			<strong>낙찰두수</strong>
			<em><span>-</span> 두</em>
		</li>
		<li class="noBidCnt">
			<strong>유찰두수</strong>
			<em><span>-</span> 두</em>
		</li>
	</ul>
	<p class="notice">주) 엑셀로 저장하면 상세 내용을 제공합니다.</p>
</div>