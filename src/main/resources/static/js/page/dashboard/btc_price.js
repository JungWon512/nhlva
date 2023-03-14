(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
		// 성별 클릭 시
		$(document).on("click","div.btnSex button",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			searchAjax("indvSexC");
		});
		
		//날짜 <, > 변경 시
		$(".btn-date-chg").unbind("click").click(function(e){
			e.preventDefault();
			var id = $(this).attr("id");
			$("#searchSbidDt").val(id);
			
			var addFlag = $(this).hasClass("btn-next") ? "next" : "prev";
			searchAjax("change", addFlag);
		});
		
		//등급 변경 시
		$("select[name='searchBtcAucGrd']").change(function(){
			searchAjax("chgGrade");
		});
		
    };
    
    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);

var filterCallBack = function (flag) {
	searchAjax(flag);
}
var searchAjax = function (flag, addFlag) {
	
	const params = {
		searchFlagAll : flag
		,addFlag : addFlag
		,searchDate: $("input[name=searchDate]").val()
		,searchFlag: $("input[name=searchFlag]").val()
		,searchPlace: $("input[name=searchPlace]").val()
		,searchIndvSexC : $(".btnSex button.on").attr("id")
		,SBID_DT: (flag == "indvSexC") ? "" : $("#searchSbidDt").val() 
		,SRA_GRD_DSC: (flag == "indvSexC") ? "" : $("#searchBtcAucGrd option:selected").val()
	}
	
	$.ajax({
		url: '/dashboard/btc_price_ajax',
		data: JSON.stringify(params),
		type: 'POST',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function() {
		},
		error: function(xhr, status, error) {
		}
	}).done(function (body) {
		var success = body.success;
		var message = body.message;
		if (!success) {
			modalAlert("", message);
		}
		else {
			//값 체인지 하기
			var btcAucDate = body.btcAucDate;
			var btcAucAvg = body.btcAucAvg;
			
			//기준일자 변경하기
			$("#searchSbidDt").val(btcAucDate.SBID_DT);
			$(".basicYm").text(body.basicYm);
			$(".basicDay").text(body.basicDay);
			
			//기준일자 이전 날 셋팅하기
			$(".btn-date-chg.btn-prev").attr("id" , btcAucDate.BEFORE_DT);
			if(btcAucDate.BEFORE_DT == "" || btcAucDate.BEFORE_DT == undefined){
				$(".btn-date-chg.btn-prev").attr("disabled" , true);
			}else{
				$(".btn-date-chg.btn-prev").attr("disabled" , false);
			}
			
			//기준일자 이후 날 셋팅하기
			$(".btn-date-chg.btn-next").attr("id" , btcAucDate.AFTER_DT);
			if(btcAucDate.AFTER_DT == "" || btcAucDate.AFTER_DT == undefined){
				$(".btn-date-chg.btn-next").attr("disabled" , true);
			}else{
				$(".btn-date-chg.btn-next").attr("disabled" , false);
			}
			
			//등급 selectbox 설정하기
			var btcAucGrd = body.btcAucGrd;
			var selBox = [];
			var selectedVal = $("#searchBtcAucGrd option:selected").val();
			
			$("#searchBtcAucGrd").empty();
			if(btcAucGrd != null && btcAucGrd.length){
				for(var j = 0; j < btcAucGrd.length; j++){
					if(flag == "indvSexC" && j == 0){
						$(".selectric .label").text(btcAucGrd[j].SRA_GRD_DSC);
					}
					selBox.push("<option value='"+btcAucGrd[j].SRA_GRD_DSC+"' "+(selectedVal == btcAucGrd[j].SRA_GRD_DSC ? 'selected' : '')+ ">" + btcAucGrd[j].SRA_GRD_DSC + "</option>");
				}
			}
			$("#searchBtcAucGrd").append(selBox.join(""));
			
			//평균가격, 평균가격 증감율 표시
			$(".avg_sra_sbid_upr").text(fnSetComma(btcAucAvg.AVG_SRA_SBID_UPR) + " 원");
			$(".avg_chg").removeClass("fc-blue fc-red fc-zero");
			if(btcAucAvg.AVG_CHG < 0){
				$(".avg_chg").text("▼ " + Math.abs(btcAucAvg.AVG_CHG) + " %");
				$(".avg_chg").addClass("fc-blue");
			}else if(btcAucAvg.AVG_CHG == 0){
				$(".avg_chg").text("- " + btcAucAvg.AVG_CHG + " %");
				$(".avg_chg").addClass("fc-zero");
			}else{
				$(".avg_chg").text("▲ " + btcAucAvg.AVG_CHG + " %");
				$(".avg_chg").addClass("fc-red");
			}
			
			//최고가격, 최고가격 증감율 표시
			$(".max_sra_sbid_upr").text(fnSetComma(btcAucAvg.MAX_SRA_SBID_UPR) + " 원");
			$(".max_chg").removeClass("fc-blue fc-red fc-zero");
			if(btcAucAvg.MAX_CHG < 0){
				$(".max_chg").text("▼ " + Math.abs(btcAucAvg.MAX_CHG) + " %");
				$(".max_chg").addClass("fc-blue");
			}else if(btcAucAvg.MAX_CHG == 0){
				$(".max_chg").text("- " + btcAucAvg.MAX_CHG + " %");
				$(".max_chg").addClass("fc-zero");
			}else{
				$(".max_chg").text("▲ " + btcAucAvg.MAX_CHG + " %");
				$(".max_chg").addClass("fc-red");
			}
			
			//최저가격, 최저가격 증감율 표시
			$(".min_sra_sbid_upr").text(fnSetComma(btcAucAvg.MIN_SRA_SBID_UPR) + " 원");
			$(".min_chg").removeClass("fc-blue fc-red fc-zero");
			if(btcAucAvg.MIN_CHG < 0){
				$(".min_chg").text("▼ " + Math.abs(btcAucAvg.MIN_CHG) + " %");
				$(".min_chg").addClass("fc-blue");
			}else if(btcAucAvg.MIN_CHG == 0){
				$(".min_chg").text("- " + btcAucAvg.MIN_CHG + " %");
				$(".min_chg").addClass("fc-zero");
			}else{
				$(".min_chg").text("▲ " + btcAucAvg.MIN_CHG + " %");
				$(".min_chg").addClass("fc-red");
			}
			
			//거래두수
			$(".brc_hdcn").text(fnSetComma(btcAucAvg.BRC_HDCN) + " 두");
			$(".brc_chg").removeClass("fc-blue fc-red fc-zero");
			if(btcAucAvg.BRC_CHG < 0){
				$(".brc_chg").text("▼ " + Math.abs(btcAucAvg.BRC_CHG) + " %");
				$(".brc_chg").addClass("fc-blue");
			}else if(btcAucAvg.BRC_CHG == 0){
				$(".brc_chg").text("- " + btcAucAvg.BRC_CHG + " %");
				$(".brc_chg").addClass("fc-zero");
			}else{
				$(".brc_chg").text("▲ " + btcAucAvg.BRC_CHG + " %");
				$(".brc_chg").addClass("fc-red");
			}
			
			//지역목록
			var areaList = body.areaList;
			var arrHtml = [];
			$("#tbodyAreaList").empty();
			
			if(areaList != null && areaList.length > 0){
				for(var i = 0 ; i < areaList.length; i++){
					var areaVo = areaList[i];
					arrHtml.push("<tr>");
					arrHtml.push("	<td>" + areaVo.CLNTNM  + "</td>");
					arrHtml.push("	<td class='ta-C'>" + fnSetComma(areaVo.AVG_SBID_PRC)  + "</td>");
					arrHtml.push("	<td class='ta-C'>" + fnSetComma(areaVo.SUM_HDCN)  + "</td>");
					arrHtml.push("</tr>");
				}
			}
			$("#tbodyAreaList").append(arrHtml.join(""));
			
			//평균가
			var areaSum = body.areaSum;
			$(".foot_avg_sbid_prc").text(fnSetComma(areaSum.AVG_SBID_PRC));
			$(".foot_sum_hdcn").text(fnSetComma(areaSum.SUM_HDCN));
				
			
			setChart(body.areaList);
		}
	});
	
}

var setChart =  function(areaList) {
	var labelData = [];
	var barData = [];
	$(".chart_area").empty();
	$(".chart_area").append('<canvas id="myCharSample5" class="bar_chart"></canvas>');
	
	const ctx = $('#myCharSample5');
	
	areaList.forEach(item => {
		labelData.push(item.CLNTNM ?? '없음');
		barData.push(item.AVG_SBID_PRC ?? 0);
	});
		
	var config = {
		type: 'bar',
		data: {
			labels: labelData,
			datasets: [
				{
					label: '거래가',
					data: barData,
					borderColor: '#a5dfdf',
					backgroundColor: '#a5dfdf',
					yAxisID: 'y-left'
				}
			]
		},
		options: {
			interaction: {
				intersect: false,
				mode: 'index'
			},
			scales: {
				yAxes: [
					{
						id: 'y-left',
						position: 'left',
						display: true,
						ticks: {
							beginAtZero: true,
							callback: function(value, index) {
								return parseInt(value, 10).toLocaleString();
							}
						}
					}
				]
			},
			tooltips: {
				enabled: true,
				callbacks: {
					label: function(tooltipItem, data) {
						const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
						return parseInt(tooltipValue, 10).toLocaleString();
					}
				}
			}
		}
	};

	barChart = new Chart(ctx, config);
}
