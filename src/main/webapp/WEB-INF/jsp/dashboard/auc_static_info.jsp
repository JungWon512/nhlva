<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<input type="hidden" id="na_Bzplcloc" value="${loginJohapData.NA_BZPLCLOC }"/>
<input type="hidden" id="naBzplc" value="${loginJohapData.NA_BZPLC }"/>
<input type="hidden" id="loginGrpC" value="${loginGrpC }"/>
<div class="board-new">
	<div class="box-select ver-place">
		<div>
			<select name="loc" id="loc">
				<c:forEach items="${ bizList }" var="item" varStatus="st">
					<option value="${item.NA_BZPLCLOC }" ${loginJohapData.NA_BZPLCLOC eq item.NA_BZPLCLOC ? 'selected' :''}>${item.NA_BZPLCLOC_NM }</option>
				</c:forEach>
			</select>	
		</div>
		<div>
			<select name="bzLoc" id="bzLoc">
			</select>	
		</div>
	</div>
	<div class="box-select ver-date">
		<div>
			<input type="text" name="stSearchDt" id="stSearchDt" value="" class="">	
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
		<li class="cowCnt">
			<strong>낙찰두수</strong>
			<em><span>-</span> 두</em>
		</li>
		<li class="bidAm">
			<strong>낙찰금액</strong>
			<em><span>-</span> 원</em>
		</li>
		<li class="fhsFee">
			<strong>출하수수료</strong>
			<em><span>-</span> 원</em>
		</li>
		<li class="mwmnFee">
			<strong>낙찰수수료</strong>
			<em><span>-</span> 원</em>
		</li>
	</ul>
	<p class="notice">주) 엑셀로 저장하면 상세 내용을 제공합니다.</p>
</div>