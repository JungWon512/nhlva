<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

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

<div class="save-box">
	<div class="info">
		<em>${infoData.AUC_OBJ_DSC_NAME} |</em>
		<em>${infoData.INDV_SEX_C_NAME} |</em>
		<em>${fn:substring(infoData.SRA_INDV_AMNNO, 3, 6)} ${fn:substring(infoData.SRA_INDV_AMNNO, 6, 10)} ${fn:substring(infoData.SRA_INDV_AMNNO, 10, 14)} ${fn:substring(infoData.SRA_INDV_AMNNO, 14, 15)} </em>
	</div>
</div>
<h3 class="tit2"><span class="subTxt" style="position: absolute;right: 10px;"></span></h3>
<h3 class="tit">
	혈통정보
</h3>
<div class="cow-father">
	<table class="table-detail">
		<colgroup>
			<col width="15%">
			<col width="16%">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>부</th>
				<td colspan="2" class="bg-gray fz-32" name="blInfo_1"> - </td>
			</tr>
			<tr>
				<th colspan="2">조부</th>
				<td class="fz-32" name="blInfo_0"> - </td>
			</tr>
			<tr>
				<th colspan="2">조모</th>
				<td class="fz-32" name="blInfo_2"> - </td>
			</tr>
		</tbody>
	</table>
</div>
<div class="cow-mother">
	<table class="table-detail">
		<colgroup>
			<col width="15%">
			<col width="16%">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>모</th>
				<td colspan="2" class="bg-gray fz-32" name="blInfo_4"> - </td>
			</tr>
			<tr>
				<th colspan="2">외조부</th>
				<td class="fz-32" name="blInfo_3"> - </td>
			</tr>
			<tr>
				<th colspan="2">외조모</th>
				<td class="fz-32" name="blInfo_5"> - </td>
			</tr>
		</tbody>
	</table>
</div>
<h3 class="tit">형매정보</h3>
<div class="cow-sibiling">
	<table class="table-detail sibTbl">
		<colgroup>
			<col width="8%">
			<col width="33%">
			<col width="14%">
			<col width="15%">
			<col width="30%">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">산차</th>
				<th rowspan="2">개체번호</th>
				<th>등록</th>
				<th>성별</th>
				<th>생년월일</th>
			</tr>
			<tr>
				<th>등급</th>
				<th>도체</th>
				<th>도축일</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td rowspan="2" colspan="5" class="ta-C">-</td>
			</tr>
		</tbody>
	</table>
</div>	
<h3 class="tit">후대정보</h3>
<div class="cow-sibiling">
	<table class="table-detail postTbl">
		<colgroup>
			<col width="8%">
			<col width="33%">
			<col width="14%">
			<col width="15%">
			<col width="30%">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2">산차</th>
				<th rowspan="2">개체번호</th>
				<th>등록</th>
				<th>성별</th>
				<th>생년월일</th>
			</tr>
			<tr>
				<th>등급</th>
				<th>도체</th>
				<th>도축일</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td rowspan="2" colspan="5" class="ta-C">-</td>
			</tr>
		</tbody>
	</table>
</div>


<script type="text/javascript">

$(document).ready(function(){
	//getAiakInfo();
});
</script>