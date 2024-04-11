<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
	
<fmt:parseDate value="${bloodInfo.LSCHG_DT}" pattern="yyyyMMdd" var="tmpSyncDt" />
<fmt:formatDate value="${tmpSyncDt}" pattern="yyyy-MM-dd" var="syncDt" />
<!-- 240306 유전능력 -->
<h3 class="tit mb10">유전평가결과(EPD)</h3>
<h3 class="tit2" style="margin-bottom:1px;"><span class="subTxt" style="font-size: 13px">※한국종축개량협회제공일 : ${syncDt}</span></h3>
<h3 class="tit2"><span class="subTxt" style="font-size: 13px;color:red;">※해당정보는 참고용으로,최종구매전 종축개량협회 홈페이지에서 확인하시기바랍니다.</span></h3>
<p class="txt">개체 유전능력은 절대값이 아니므로 참고용으로 사용 하시기 바랍니다.</p>

<!-- 기존 차트 -->
<div id="epdChartDiv" style="margin: 20px 0; padding: 0px 0; background-color: #fff; text-align: center; font-weight: 700">
    <canvas id="epdChart" style="width: 588px; height: 320px; display: block" width="882" height="480"></canvas>
</div>
<!-- //기존 차트 -->

<span class="sub_info_txt">유전체분석 미수행 개체</span>
<table class="sub_tble">
    <caption>
        대상형질, 평가수치, 평가등급
    </caption>
    <colgroup>
        <col width="" />
        <col width="90" />
        <col width="80" />
    </colgroup>
    <thead>
        <tr>
            <th>대상형질</th>
            <th class="txtR">평가수치</th>
            <th>평가등급</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><b class="new_mark"></b>도체중(kg)</td>
            <td class="txtR" name="dscReProduct1">${not empty bloodInfo.EPD_VAL_1 ? bloodInfo.EPD_VAL_1 : '-'}</td>
            <td><span class="tag${bloodInfo.EPD_GRD_1}">${not empty bloodInfo.EPD_GRD_1 ?bloodInfo.EPD_GRD_1 : '-'}</span></td>
        </tr>
        <tr>
            <td>배최장근(㎠)</td>
            <td class="txtR" name="dscReProduct2">${not empty bloodInfo.EPD_VAL_2 ? bloodInfo.EPD_VAL_2 : '-'}</td>
            <td><span class="tag${bloodInfo.EPD_GRD_2}">${not empty bloodInfo.EPD_GRD_2 ?bloodInfo.EPD_GRD_2 : '-'}</span></td>
        </tr>
        <tr>
            <td>등지방두께(㎜)</td>
            <td class="txtR" name="dscReProduct3">${not empty bloodInfo.EPD_VAL_3 ? bloodInfo.EPD_VAL_3 : '-'}</td>
            <td><span class="tag${bloodInfo.EPD_GRD_3}">${not empty bloodInfo.EPD_GRD_3 ?bloodInfo.EPD_GRD_3 : '-'}</span></td>
        </tr>
        <tr>
            <td>근내지방도</td>
            <td class="txtR" name="dscReProduct4">${not empty bloodInfo.EPD_VAL_4 ? bloodInfo.EPD_VAL_4 : '-'}</td>
            <td><span class="tag${bloodInfo.EPD_GRD_4}">${not empty bloodInfo.EPD_GRD_4 ?bloodInfo.EPD_GRD_4 : '-'}</span></td>
        </tr>
    </tbody>
</table>

<div class="tag-info">
    <dl class="tag-tit">
        <dt>육종가 코드</dt>
        <dd>각 형지별 육종가 순위를 A, B, C, D 단계로 구분함</dd>
    </dl>
    <table>
        <colgroup>
            <col style="width: 80px" />
            <col />
        </colgroup>
        <tr>
            <th><i class="tagA"></i><span>A코드</span></th>
            <td>육종가 순위 1 ~ 20% (아주 좋음)</td>
        </tr>
        <tr>
            <th><i class="tagB"></i><span>B코드</span></th>
            <td>육종가 순위 20 ~ 45% (좋음)</td>
        </tr>
        <tr>
            <th><i class="tagC"></i><span>C코드</span></th>
            <td>육종가 순위 45 ~ 70% (약간 나쁨)</td>
        </tr>
        <tr>
            <th><i class="tagD"></i><span>D코드</span></th>
            <td>육종가 순위 70 ~ 100% (나쁨)</td>
        </tr>
    </table>
</div>

<script src="https://www.가축시장.kr/static/js/common/chart/chart.js"></script>
<script type="text/javascript">
    var chart;
    $(document).ready(function () {});
</script>
<!-- //240306 유전능력 -->