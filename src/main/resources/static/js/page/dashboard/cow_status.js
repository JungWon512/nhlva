(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
		//지역 선택 시, 팝업 띄우기
		$(".sCowPop").unbind("click").click(function(e){
			e.stopPropagation();
			var frm = $("form[name='frm']");
			$(this).siblings().children("dl").removeClass();
			$(this).children("dl").addClass("on");
			$("#searchPlace").val($(this).attr("id"));
			
			var param = [];
			param.push("searchMonth=" + $("#searchMonth").val());
			param.push("searchPlace=" + $(this).attr("id"));
			param.push("aucObjDsc=" + $("#aucObjDsc").val() || "1");
			param.push("monthOldC=" + $(".birth_weight_dsc").find("button.on").attr("id"));
			param.push("aucObjDscNm=" + $(".auc_obj_dsc").find("button.on").text());
			param.push("monthOldCNm=" + $(".birth_weight_dsc").find("button.on").text());
			param.push("searchPlaceNm=" + $(this).find(".locnm").text());
			
			// 송아지인경우 성별필요
			if($("#aucObjDsc").val() == "1"){
				param.push("indvSexC=" + $(".auc_obj_dsc").find("button.on").attr("id").substring(1));				
			}
			
			window.open('/dashboard/dashSogCowPop?' + param.join("&"), '출장우 현황', 'width=700, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		});
		
		//경매대상구분 클릭 (암송아지/수송아지/비육우/번식우)
		$(".auc_obj_dsc button").unbind("click").click(function(e){
			e.preventDefault();
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			
			if ($(this).attr("id").startsWith("1")) {
				$("#aucObjDsc").val($(this).attr("id").substring(0,1));
				$("#indvSexC").val($(this).attr("id").substring(1));
			} else {
				$("#aucObjDsc").val($(this).attr("id"));
				$("#indvSexC").val("");
			}
			
			$(".birth_weight_dsc").empty();
			var btnHtml = [];
			if($(this).attr("id").startsWith("1")){
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
			$("input[name=monthOldC]").val($(this).attr("id"));
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
		// 송아지가 아닐경우 성별코드 제거
		if($("#aucObjDsc").val() != "1"){
			$("#indvSexC").val("");
		}
	
		const params = {
			searchMonth: $("input[name=searchMonth]").val()
			,searchFlag: flag
			,aucObjDsc : $("input[name=aucObjDsc]").val() || "1"		//경매대상구분
			,monthOldC : $("input[name=monthOldC]").val()				//월령 or 중량
			,indvSexC : $("input[name=indvSexC]").val()					//성별구분
		}
		
		$.ajax({
			url: '/dashboard/cow_status_ajax',
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
			} else {
				$("#searchMonth").val(body.inputParam.searchMonth)
				$(".month_txt_title").text(body.inputParam.searchMonTxt);
				
				var sogCowInfo = body.sogCowInfo;
				if(body.sogCowInfo == null || body.sogCowInfo == undefined){
					sogCowInfo = setsogCowInfoNull(body);
					body["sogCowInfo"] = sogCowInfo;
				}
				
				//Next 버튼 처리
				if ($('#nowMonth').val() == body.inputParam.searchMonth) {
//					$("button.btn-next").attr("disabled", true);
					$("button.btn-next").hide();
				} else {
//					$("button.btn-next").attr("disabled", false);
					$("button.btn-next").show();
				}
				
				var totChgHtml = "";
				//출장우 총 두수 (증감두수 표시)
				$("div.cow-board-top").empty();
				$("div.cow-board-top").append('<strong class="tot_cow_cnt">' + fnSetComma(sogCowInfo.TOT_SOG_CNT) + ' 두</strong>');
				if(sogCowInfo.ASC_SOG_CNT > 0) {
					totChgHtml = '<span class="tot_sCow_chg_txt fc-red">▲ '+ fnSetComma(sogCowInfo.ASC_SOG_CNT) + ' 두';
				}else if(sogCowInfo.ASC_SOG_CNT < 0){
					totChgHtml = '<span class="tot_sCow_chg_txt fc-blue">▼ '+ fnSetComma(sogCowInfo.ASC_SOG_CNT) + ' 두';
				}else{
					totChgHtml = '<span class="tot_sCow_chg_txt fc-blue">- '+ fnSetComma(sogCowInfo.ASC_SOG_CNT) + ' 두';
				}
				$("div.cow-board-top").append(totChgHtml);
				
				//출장우 월표시
				$(".before2Month").html(body.inputParam.before2Month.substring(4, 6) + "월");
				$(".beforeMonth").html(body.inputParam.beforeMonth.substring(4, 6) + "월");
				$(".searchMonth").html(body.inputParam.searchMonth.substring(4, 6) + "월");
				
				//송아지, 비육우, 번식우 :: 전전월, 전월, 이번월 표시
				var sogCowInfoList = body.sogCowInfoList;
				
				if(sogCowInfoList.length > 0){
					$("tbody.sogCowInfoList").empty();
					
					let sHtml = [];
					for (let item of sogCowInfoList) {
						sHtml.push('<tr class="sogCow">');
						sHtml.push('	<th>'+ item.AUC_OBJ_DSC_NAME +'</th>');
						sHtml.push('	<td class="ta-C">'+ fnSetComma(item.CNT_1) +'</td>');
						sHtml.push('	<td class="ta-C">'+ fnSetComma(item.CNT_2) +'</td>');
						sHtml.push('	<td class="ta-C on">'+ fnSetComma(item.CNT_3) +'</td>');
						sHtml.push('</tr>');
					}
					
					$("tbody.sogCowInfoList").append(sHtml.join(""));
				}
				
				//지역별 평균 시세
				var areaSbidList = body.areaSbidList;
				var arrHtml = [];
				$("#area_scow_list").empty();
				
				if(areaSbidList.length > 0){
					for(var i = 0; i < areaSbidList.length; i++ ){
						var areaVo = areaSbidList[i];
						arrHtml.push("<li class='sCowPop' id='"+areaVo.NA_BZPLCLOC+"'>");
						arrHtml.push("	<dl class=''>");
						arrHtml.push("		<dt class='locnm'>" + areaVo.NA_BZPLCLOC_NM + "</dt>");
						arrHtml.push("		<dd class='won'>" + fnSetComma(areaVo.AVG_EXPRI_UPR) + "원</dd>");
						arrHtml.push("		<dd>" + fnSetComma(areaVo.TOT_SOG_CNT) + "두</dd>");
						arrHtml.push("	</dl>");
						arrHtml.push("</li>");
					}
				}
				$("#area_scow_list").append(arrHtml.join(""));
				
				// 차트
				setChart(body);
				setBinding();
			}
		});
	}
	
    var setChart =  function(body) {
		//초기화
		$(".chart_area").empty();
		$("#chart_area1").append('<canvas id="myPieSample1"></canvas>');
		$("#chart_area2").append('<canvas id="myCharSample1"></canvas>');
	
		// 막대 차트 생성
		var labelData = [];
		var preCntData = [];	// 전년도 출장우
		var thisCntData = [];	// 올해 출장우
		
		const ctx1 = document.getElementById('myCharSample1');
		
		var monSogCowList = body.monSogCowList;
		
		monSogCowList.forEach(item => {
			var month = item.MONTH;
			var preCowCnt = item.PRE_COW_CNT;
			var thisCowCnt = item.THIS_COW_CNT ;
	
			labelData.push(month ?? '없음');
			preCntData.push(preCowCnt);
			thisCntData.push(thisCowCnt);
		});

		new Chart(ctx1, {
			type: 'bar',
			data: {
				labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
				datasets: [
					{
						label: '전년도 출장우',
						data: preCntData,
						borderColor: '#37a2eb',
						background:'',
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
					, xAxes : [{
						ticks : {
							fontSize : 9
						}
					}]
				},
				tooltips: {
					enabled: true,
					callbacks: {
						label: function(tooltipItem, data) {
							const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
							return parseInt(tooltipValue, 10).toLocaleString();
						}
					}
				},
				legend: {
					labels:{
						fontSize : 10
					}
				}
			}
		});
		
		// 도넛 차트 생성
		const ctx2 = document.getElementById('myPieSample1');
		const sogCowInfoList = body.sogCowInfoList;
		
		let aucData = [];
		aucData.push(sogCowInfoList[0].CNT_3, sogCowInfoList[1].CNT_3, sogCowInfoList[2].CNT_3);
		
		new Chart(ctx2, {
				type: 'pie',
			data: {
				labels: ['송아지', '비육우', '번식우'],
				datasets: [
					{
						data: aucData,
						borderColor: ['#ffcd56','#ff9f40','#ff4069'],
						backgroundColor: ['#ffcd56','#ff9f40','#ff4069'],
						borderWidth: 0,
					}
				]
			},
			options: {
				responsive: true,
				maintainAspectRatio: true,
				plugins: {
					legend: {
						position: 'right'
					},
				}
			},
		});
	}

	//sogCowInfo 가 아예 null 인 경우, 기본 0 셋팅
	var setSogCowInfoNull = function(body){
		var sogCowInfo = new Object();
		sogCowInfo["AUC_OBJ_DSC"] = body.inputParam.aucObjDsc;
		sogCowInfo["MONTH_OLD_C"] = body.inputParam.monthOldC == null ? "전체" : body.inputParam.monthOldC;
		sogCowInfo["TOT_SOG_CNT"] = 0;
		sogCowInfo["ASC_SOG_CNT"] = 0;
		return sogCowInfo;
	}

    var namespace = win.auction;
    var __COMPONENT_NAME = "COW_STATUS"; 
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

