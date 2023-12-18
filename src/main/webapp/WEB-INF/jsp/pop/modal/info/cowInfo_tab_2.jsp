<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>

<fmt:parseDate value="${bloodInfo.LSCHG_DT}" pattern="yyyyMMdd" var="tmpSyncDate" />
<fmt:formatDate value="${tmpSyncDate}" pattern="yyyy-MM-dd" var="syncDate" />
<h3 class="tit2"><span class="subTxt" style="position: absolute;right: 10px;font-size:13px;">※종축개량협회제공일 : ${syncDate}</span></h3>
<h3 class="tit">
	유전능력(EPD)	
</h3>
<p class="txt">개체 유전능력은 절대값이 아니므로 참고용으로 사용 하시기 바랍니다.</p>
<div class="cow-basic cow-epd">
	<table class="table-detail">
		<colgroup>
			<col width="45%">
			<col width="35%">
			<col width="20%">
		</colgroup>
		<tbody>
			<tr>
				<th><i class="dot" style="background-color: #ffaf00;"></i>냉도체중(Kg)</th>
				<td name="reProduct1" class="ta-C bdr-y">${bloodInfo.EPD_VAL_1}</td>
				<td class="ta-C dscReProduct1" name="dscReProduct1"><span class="c-blue">${bloodInfo.EPD_GRD_1 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #a4d509;"></i>배최장근(cm2)</th>
				<td name="reProduct2" class="ta-C bdr-y">${bloodInfo.EPD_VAL_2 }</td>
				<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${bloodInfo.EPD_GRD_2 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #5bacff;"></i>등지방두께(mm)</th>
				<td name="reProduct3" class="ta-C bdr-y">${bloodInfo.EPD_VAL_3 }</td>
				<td class="ta-C dscReProduct3" name="dscReProduct3"><span class="c-blue">${bloodInfo.EPD_GRD_3 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #ff7bc2;"></i>근내지방도(점)</th>
				<td name="reProduct4" class="ta-C bdr-y">${bloodInfo.EPD_VAL_4 }</td>
				<td class="ta-C dscReProduct4" name="dscReProduct4"><span class="c-blue">${bloodInfo.EPD_GRD_4 }</span></td>
			</tr>
		</tbody>
	</table>
</div>
<div id="epdChartDiv" style="margin:20px 0;padding: 0px 0;background-color: #fff;text-align: center;font-weight: 700;">
</div>
<div class="info">
	육종가코드 :<br>
	각 형질별 육종가 순위를 A,B,C,D 4단계로 구분함
	<ul>
		<li>· A코드 : 육종가 순위 1 ~ 20% (아주 좋음)</li>
		<li>· B코드 : 육종가 순위 20 ~ 45% (좋음)</li>
		<li>· C코드 : 육종가 순위 45 ~ 70% (약간 나쁨)</li>
		<li>· D코드 : 육종가 순위 70 ~ 100% (나쁨)</li>
	</ul>
</div>

<script src="/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
var chart;
$(document).ready(function(){
});
</script>