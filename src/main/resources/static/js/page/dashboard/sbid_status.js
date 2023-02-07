(function ($, win, doc) {
    var COMMONS = win.auction["commons"];
    
    var setLayout = function() { 
    };
    
    var setBinding = function() {
		//지역 선택 시, 팝업 띄우기
		$(".sbidPop").unbind("click").click(function(e){
			e.stopPropagation();
			var frm = $("form[name='frm']");
			$(this).siblings().children("dl").removeClass();
			$(this).children("dl").addClass("on");
			$("#searchPlace").val($(this).attr("id"));
			$("#searchPlaceNm").val($(this).find(".locnm").text());
			
			var param = [];
			param.push("searchMonth=" + $("#searchMonth").val());
			param.push("searchPlace=" + $("#searchPlace").val());
			param.push("aucObjDsc=" + $("#aucObjDsc").val());
			param.push("monthOldC=" + $("#monthOldC").val());
			param.push("aucObjDscNm=" + $(".auc_obj_dsc").find("button.on").text());
			param.push("monthOldCNm=" + $(".birth_weight_dsc").find("button.on").text());
			param.push("searchPlaceNm=" + $(this).find(".locnm").text());
			window.open('/dashboard/dashSbidPop?' + param.join("&"), '지역낙찰가', 'width=700, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
			
		});
		
		//경매대상구분 클릭 (송아지/비육우/번식우)
		$(".auc_obj_dsc button").unbind("click").click(function(e){
			e.preventDefault();
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			$("#aucObjDsc").val($(this).attr("id"));
			
			$(".birth_weight_dsc").empty();
			var btnHtml = [];
			if($(this).attr("id") == "1"){
				btnHtml.push('<button id="" class="on">전체</button>');
				btnHtml.push('<button id="01">4~5개월</button>');
				btnHtml.push('<button id="02">6~7개월</button>');
				btnHtml.push('<button id="03">8개월 이상</button>');
			}else{
				btnHtml.push('<button id="" class="on">전체</button>');
				btnHtml.push('<button id="11">400kg 미만</button>');
				btnHtml.push('<button id="12">400kg 이상</button>');		
			}
			
			$(".birth_weight_dsc").append(btnHtml.join(""));
			searchAjax();
		});
		
		//월령, 중량 구분 클릭 (월령 : 4-5, 6-7, 8이상 / 중량 : 400미만, 400이상)
		$(".birth_weight_dsc button").unbind("click").click(function(e){
			e.preventDefault();
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			$("#monthOldC").val($(this).attr("id"));
			searchAjax();
		});
		
		//월 prev, next 클릭 시
		$(".btn-chg").unbind("click").click(function(e){
			e.preventDefault();
			if($(this).hasClass("btn-prev")){
				searchAjax("prev");
			}else{
				searchAjax("next");
			}
		});
    };
    
    var searchAjax = function(flag){
		const params = {
			searchMonth: $("input[name=searchMonth]").val()
			,searchFlag: flag
			,aucObjDsc : $(".auc_obj_dsc button.on").attr("id")		//경매대상구분
			,monthOldC : $(".birth_weight_dsc button.on").attr("id")		//월령 or 중량
		}
		
		$.ajax({
			url: '/dashboard/sbid_status_ajax',
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
				$("#searchMonth").val(body.inputParam.searchMonth)
				$(".month_txt_title").text(body.inputParam.searchMonTxt);
				if($("#nowMonth").val() == body.inputParam.searchMonth){
//					$(".btn-next").attr("disabled", true);
					$("button.btn-next").hide();
				}else{
//					$(".btn-next").attr("disabled", false);
					$("button.btn-next").show();
				}
				
				var sbidInfo = body.sbidInfo;
				if(body.sbidInfo == null || body.sbidInfo == undefined){
					sbidInfo = setSbidInfoNull(body);
					body["sbidInfo"] = sbidInfo;
					sbidInfo = body.sbidInfo;
				}
				
				var totChgHtml = "";
				//낙찰 총 두수 (증감두수 표시)
				$(".tot_sbid_cnt").text(fnSetComma(sbidInfo.TOT_SBID_CNT) + " 두");
				if(sbidInfo.TOT_SBID_CHG > 0) {
					totChgHtml = "▲ "+ sbidInfo.TOT_SBID_CHG + " 두";
				}else if(sbidInfo.TOT_SBID_CHG == 0){
					totChgHtml = "- "+ sbidInfo.TOT_SBID_CHG + " 두";
				}else{
					totChgHtml = "▼ "+ Math.abs(sbidInfo.TOT_SBID_CHG) + " 두";
				}
				$(".tot_sbid_chg_txt").text(totChgHtml);
				$(".month_old_c_nm").text(sbidInfo.MONTH_OLD_C_NM == null ? "전체" : sbidInfo.MONTH_OLD_C_NM);
				
				//최고, 평균, 최저 가격 
				$(".max_sbid_upr").text(fnSetComma(sbidInfo.MAX_SBID_UPR) + " 원");
				$(".max_sbid_chg").removeClass("fc-blue fc-red fc-zero");
				if(sbidInfo.MAX_SBID_CHG > 0) {
					$(".max_sbid_chg").text("+ " + fnSetComma(sbidInfo.MAX_SBID_CHG) + " 원");
					$(".max_sbid_chg").addClass("fc-red");
				}else if(sbidInfo.MAX_SBID_CHG == 0){
					$(".max_sbid_chg").text(fnSetComma(sbidInfo.MAX_SBID_CHG) + " 원");
					$(".max_sbid_chg").addClass("fc-zero");
				}else{
					$(".max_sbid_chg").text("- " + fnSetComma(Math.abs(sbidInfo.MAX_SBID_CHG)) + " 원");
					$(".max_sbid_chg").addClass("fc-blue");
				}
				
				$(".avg_sbid_upr").text(fnSetComma(sbidInfo.AVG_SBID_UPR) + " 원");
				$(".avg_sbid_chg").removeClass("fc-blue fc-red fc-zero");
				if(sbidInfo.AVG_SBID_CHG > 0) {
					$(".avg_sbid_chg").text("+ " + fnSetComma(sbidInfo.AVG_SBID_CHG) + " 원");
					$(".avg_sbid_chg").addClass("fc-red");
				}else if(sbidInfo.AVG_SBID_CHG == 0){
					$(".avg_sbid_chg").text(fnSetComma(sbidInfo.AVG_SBID_CHG) + " 원");
					$(".avg_sbid_chg").addClass("fc-zero");
				}else{
					$(".avg_sbid_chg").text("- " + fnSetComma(Math.abs(sbidInfo.AVG_SBID_CHG)) + " 원");
					$(".avg_sbid_chg").addClass("fc-blue");
				}
				
				$(".min_sbid_upr").text(fnSetComma(sbidInfo.MIN_SBID_UPR) + " 원");
				$(".min_sbid_chg").removeClass("fc-blue fc-red fc-zero");
				if(sbidInfo.MIN_SBID_CHG > 0) {
					$(".min_sbid_chg").text("+ " + fnSetComma(sbidInfo.MIN_SBID_CHG) + " 원");
					$(".min_sbid_chg").addClass("fc-red");
				}else if(sbidInfo.MIN_SBID_CHG == 0){
					$(".min_sbid_chg").text(fnSetComma(sbidInfo.MIN_SBID_CHG) + " 원");
					$(".min_sbid_chg").addClass("fc-zero");
				}else{
					$(".min_sbid_chg").text("- " + fnSetComma(Math.abs(sbidInfo.MIN_SBID_CHG)) + " 원");
					$(".min_sbid_chg").addClass("fc-blue");
				}
				
				//낙찰예정가, 낙찰평균가
				$(".expri_sbid_sum_amt").text(fnSetComma(sbidInfo.EXPRI_SBID_SUM_AMT) + " 원");
				$(".avg_sbid_upr").text(fnSetComma(sbidInfo.AVG_SBID_UPR) + " 원");
				var expri_gap = sbidInfo.AVG_SBID_UPR - sbidInfo.EXPRI_SBID_SUM_AMT;
				$(".expri_gap").removeClass("up down");
				if(expri_gap >= 0){
					$(".expri_gap").text("+ " + fnSetComma(expri_gap));
					$(".expri_gap").addClass("down");
				}else{
					$(".expri_gap").text(fnSetComma(expri_gap));
					$(".expri_gap").addClass("up");
				}
				
				//지역별 평균 시세
				var areaSbidList = body.areaSbidList;
				var arrHtml = [];
				$("#area_sbid_list").empty();
				
				if(areaSbidList.length > 0){
					for(var i = 0; i < areaSbidList.length; i++ ){
						var areaVo = areaSbidList[i];
						arrHtml.push("<li class='sbidPop' id='"+areaVo.NA_BZPLCLOC+"'>");
						arrHtml.push("	<dl class=''>");
						arrHtml.push("		<dt class='locnm'>" + areaVo.NA_BZPLCLOC_NM + "</dt>");
						arrHtml.push("		<dd class='won'>" + fnSetComma(areaVo.AVG_SBID_UPR) + "원</dd>");
						arrHtml.push("		<dd>" + fnSetComma(areaVo.TOT_SBID_CNT) + "두</dd>");
						arrHtml.push("	</dl>");
						arrHtml.push("</li>");
					}
				}
				
				$("#area_sbid_list").append(arrHtml.join(""));
				
				//chart 그리기
				setChart(body);
				setBinding();
			}
		});
		
	}
	
    var setChart =  function(body) {
		$(".chart_area").empty();
		$("#chart_area_2").append('<canvas id="myChartSample2"></canvas>');
		$("#chart_area_3").append('<canvas id="myChartSample3"></canvas>');
		$("#chart_area_4").append('<canvas id="myChartSample4"></canvas>');
		
		var labelData = [];
		var barSbidData = [];	//평균 낙찰가
		var barExpriData = [];	//평균 예상가
		const ctx2 = document.getElementById('myChartSample2');
		
		var monSbidPriceList = body.monSbidPriceList;
		
		monSbidPriceList.forEach(item => {
			var aucDt = item.AUC_DT;
			var aucDtTxt = parseInt(aucDt.substring(aucDt, 4));
			
			var avgSbidUpr = item.AVG_SBID_UPR;
			var avgExpriUpr = item.AVG_EXPRI_UPR ;
	
			labelData.push(aucDtTxt ?? '없음');
			barSbidData.push(Math.round(avgSbidUpr / 10000) ?? 0);
			barExpriData.push(Math.round(avgExpriUpr / 10000) ?? 0);
		});
		
		new Chart(ctx2, {
			type: 'bar',
			data: {
				labels: labelData,
				datasets: [
					{
						label: '평균예상가(만원)',
						data: barExpriData,
						borderColor: '#37a2eb',
						type: 'line',
						order: 0
					},
					{
						label: '평균낙찰가(만원)',
						data: barSbidData,
						borderColor: '#a5dfdf',
						backgroundColor: '#a5dfdf',
						type: 'bar',
						borderWidth: 0,
						order: 1
					}
				]
			},
			options: {
				responsive: true,
				plugins: {
					legend: {
						position: 'top'
					},
				},
				legend: {
					labels:{
						fontSize : 10
					}
				}
			},
		});
		
		var preCntData = [];	// 전년도 출장우
		var thisCntData = [];	// 올해 출장우
		var monSogCowList = body.monSogCowList;
		
		monSogCowList.forEach(item => {
			var month = item.MONTH;
			var preCowCnt = item.PRE_COW_CNT;
			var thisCowCnt = item.THIS_COW_CNT ;
	
			labelData.push(month ?? '없음');
			preCntData.push(preCowCnt);
			thisCntData.push(thisCowCnt);
		});
		
		const ctx3 = document.getElementById('myChartSample3');
		
		new Chart(ctx3, {
			type: 'bar',
			data: {
				labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
				datasets: [
					{
						label: '전년도 출장우',
						data: preCntData,
						borderColor: '#37a2eb',
						type: 'line',
						order: 0
					},
					{
						label: '출장우',
						data: thisCntData,
						borderColor: '#ffb1c1',
						backgroundColor: '#ffb1c1',
						type: 'bar',
						borderWidth: 0,
						order: 1
					}
				]
			},
			options: {
				responsive: true,
				plugins: {
					legend: {
						position: 'top',
					},
				},
				legend: {
					labels:{
						fontSize : 10
					}
				}
			},
		});
		
		const ctx4 = document.getElementById('myChartSample4');
		var currData = [];
		var prevData = [];
		var sbidInfo = body.sbidInfo;
		
		currData.push(Math.round(sbidInfo.MAX_SBID_UPR / 10000) ?? 0);
		currData.push(Math.round(sbidInfo.MIN_SBID_UPR / 10000) ?? 0);
		currData.push(Math.round(sbidInfo.AVG_SBID_UPR / 10000) ?? 0);
		
		prevData.push(Math.round(sbidInfo.MAX_SBID_UPR_B / 10000) ?? 0);
		prevData.push(Math.round(sbidInfo.MIN_SBID_UPR_B / 10000) ?? 0);
		prevData.push(Math.round(sbidInfo.AVG_SBID_UPR_B / 10000) ?? 0);
		
		new Chart(ctx4, {
			type: 'bar',
			data: {
				labels: ['최고', '최저', '평균'],
				datasets: [
					{
						label: '현재(만원)',
						data: currData,
						backgroundColor: '#9ad0f5',
						borderWidth: 0
					},
					{
						label: '이전(만원)',
						data: prevData,
						backgroundColor: '#ffb1c1',
						borderWidth: 0
					}
				]
			},
			options: {
				scales: {
					yAxes : [{
						ticks : {
							beginAtZero: true
						}
					}]
				},
				legend: {
					labels:{
						fontSize : 10
					}
				}
			}
		});
	}

	//sbidInfo 가 아예 null 인 경우, 기본 0 셋팅
	var setSbidInfoNull = function(body){
		var sbidInfo = new Object();
		sbidInfo["AUC_OBJ_DSC"] = body.inputParam.aucObjDsc;
		sbidInfo["MONTH_OLD_C"] = body.inputParam.monthOldC == null ? "전체" : body.inputParam.monthOldC;
		sbidInfo["TOT_SBID_CNT"] = 0;
		sbidInfo["TOT_SBID_CNT_B"] = 0;
		sbidInfo["MAX_SBID_UPR"] = 0;
		sbidInfo["MAX_SBID_UPR_B"] = 0;
		sbidInfo["MIN_SBID_UPR"] = 0;
		sbidInfo["MIN_SBID_UPR_B"] = 0;
		sbidInfo["SBID_SUM_AMT"] = 0;
		sbidInfo["SBID_SUM_AMT_B"] = 0;
		sbidInfo["AVG_SBID_UPR"] = 0;
		sbidInfo["AVG_SBID_UPR_B"] = 0;
		sbidInfo["EXPRI_SBID_SUM_AMT"] = 0;
		sbidInfo["TOT_SBID_CHG"] = 0;
		sbidInfo["MAX_SBID_CHG"] = 0;
		sbidInfo["MIN_SBID_CHG"] = 0;
		sbidInfo["AVG_SBID_CHG"] = 0;
		sbidInfo["SBID_SUM_CHG"] = 0;
		return sbidInfo;
	}
	
    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD_SBID_STATUS"; 
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

