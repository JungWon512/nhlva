<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/__system/taglibs.jsp" %>
			
<h3 class="tit">유전능력(EPD)</h3>
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
				<td name="reProduct1" class="ta-C bdr-y">${infoData.RE_PRODUCT_1 }</td>
				<td class="ta-C dscReProduct1" name="dscReProduct1"><span class="c-blue">${infoData.RE_PRODUCT_1_1 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #a4d509;"></i>배최장근(cm2)</th>
				<td name="reProduct2" class="ta-C bdr-y">${infoData.RE_PRODUCT_2}</td>
				<td class="ta-C dscReProduct2" name="dscReProduct2"><span class="c-blue">${infoData.RE_PRODUCT_2_1 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #5bacff;"></i>등지방두께(mm)</th>
				<td name="reProduct3" class="ta-C bdr-y">${infoData.RE_PRODUCT_3 }</td>
				<td class="ta-C dscReProduct3" name="dscReProduct3"><span class="c-blue">${infoData.RE_PRODUCT_3_1 }</span></td>
			</tr>
			<tr>
				<th><i class="dot" style="background-color: #ff7bc2;"></i>근내지방도(점)</th>
				<td name="reProduct4" class="ta-C bdr-y">${infoData.RE_PRODUCT_4 }</td>
				<td class="ta-C dscReProduct4" name="dscReProduct4"><span class="c-blue">${infoData.RE_PRODUCT_4_1 }</span></td>
			</tr>
		</tbody>
	</table>
</div>
<div style="margin:20px 0;padding: 70px 0;background-color: #fff;text-align: center;font-weight: 700;">
	<canvas id="epdChart" style="width:100%;height: 320px">
	</canvas>
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
	
	//차트 생성
    ctx = $("#epdChart");
    //ctx.height(340);
    //var labels = ["냉도체중", "배최장근단면적", "등지방두께","근내지방도"];
    var labels = [];
    var epdData = [];
    var mEpdData = [];
    //dscReProduct
    $('td[name^=dscReProduct]').each((i,o)=>{
    	var data = $(o).text().trim();    	
    	epdData.push((data == 'A'? 4:(data == 'B'? 3 : (data == 'C' ? 2 : (data == 'D' ? 1 :0)))));
    	labels.push($(o).closest('tr').find('th').text().trim());
    });
    console.log(epdData);
    var config = {
        type : 'radar',
        data : {
            //
            labels: labels,
            datasets:[
	           	{
	                label:'개체 EPD',
	                data: epdData,
	                borderColor:"rgba(35,155,223,1)",
	                backgroundColor:"rgba(35,194,223,0.4)",
	                tension: 0
	            }
            ]
        },
        options:{
            responsive: false,
            title:{
                display:false,
                text:'개체 Epd Chart'
            },scale:{
            	ticks: {
            		beginAtZero: true
            		,fontSize : 14
            		, display: true
            		, callback : function(data, index){
            			return (data == 4 ? 'A' :(data == 3? 'B' : (data == 2 ? 'C' : (data == 1 ? 'D' :''))));
            		}
				}
            }
        }           
    };
    chart = new Chart(ctx, config);
});
</script>