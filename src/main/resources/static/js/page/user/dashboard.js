(function ($, win, doc) {
    var setLayout = function() {
		// 초기 송아지 설정
		$("button#1").click();
		
    };
    
    var setBinding = function() {
		// 메뉴 선택 이벤트
		$(document).on("click","div.main-tab-pc li",function(){
			console.log("aa");
			$(this).siblings("li").removeClass();
			$(this).addClass("on");
		});
		
        // 경매대상 변경 이벤트
		$(document).on("click","div.aucObjBtn button",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			
			// 지역별 평균 낙찰가 버튼 변경
			// 송아지(MONTH_OLD_C) - 전체 / 4~5개월(01) / 6~7개월(02) / 8개월~(03)
			// 비육우 / 번식우(MONTH_OLD_C) - 전체 / 400kg 미만(11) / 400kg 이상(12)
			$("div.sec-board > div.avgSbidBtn").empty();
			let btnHtml = [];
			if ($(this).attr("id") == "1") {
				btnHtml.push('<button id="" class="on">전체</button>');
				btnHtml.push('<button id="01">4~5개월</button>');
				btnHtml.push('<button id="02">6~7개월</button>');
				btnHtml.push('<button id="03">8개월 이상</button>');
			} else {
				btnHtml.push('<button id="" class="on">전체</button>');
				btnHtml.push('<button id="11">400kg 미만</button>');
				btnHtml.push('<button id="12">400kg 이상</button>');				
			}
			$("div.sec-board > div.avgSbidBtn").append(btnHtml.join(" "));
			
			$("input[name=searchMonthOldC]").val("");
			$("input[name=searchAucObjDsc]").val($(this).attr("id"));
			searchAjax();
		});
		// 지역별 평균 낙찰가 변경 이벤트
		$(document).on("click","div.avgSbidBtn button",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			
			$("input[name=searchMonthOldC]").val($(this).attr("id"));
			searchAjax();
		});
		// 필터 클릭 이벤트
		$(document).on("click","#dashboard_filter",function(){
			const searchRaDate = $("input[name=searchRaDate]").val() || "";
			const searchFlag = $("input[name=searchFlag]").val() || "";
			const searchPlace = $("input[name=searchPlace]").val() || "";
			window.open('/dashFilter?searchRaDate='+ searchRaDate+'&searchFlag='+searchFlag+'&searchPlace='+searchPlace, '필터', 'width=700, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		});
		// TOP10 클릭 이벤트
		$(document).on("click","#btn-top10",function(){
			const searchFlag = $("input[name=searchFlag]").val() || "";
			const searchPlace = $("input[name=searchPlace]").val() || "";
			const searchAucObjDsc = $("input[name=searchAucObjDsc]").val() || "1";
			const searchMonthOldC = $("input[name=searchMonthOldC]").val() || "";
			window.open('/dashTop10?searchFlag='+searchFlag+'&searchPlace='+searchPlace+'&searchAucObjDsc='+searchAucObjDsc+'&searchMonthOldC='+searchMonthOldC,'TOP10', 'width=700, height=900, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		});
		// 사용안내 이벤트
		$(document).on("click","#alertPop",function(){
			modalAlert('','사용안내 페이지는 준비중입니다. <br/>관리자에게 문의하세요.');
			return;
		});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setBinding();
            setLayout();
            searchAjax();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);

var COMMONS = window.auction["commons"];

var filterCallBack = function (tempParam) {
	let param = JSON.parse(tempParam); 
	
	if (param) {
		$("input[name=searchRaDate]").val(param.searchRaDate);
		$("input[name=searchFlag]").val(param.searchFlag);
		$("input[name=searchPlace]").val(param.searchPlace);
		
		param.searchFlag != "A" ? $("input[name=placeNm]").val(param.placeNm) : $("input[name=placeNm]").val("전국");
	}
	
	searchAjax();
}
var searchAjax = function () {
	const params = {
		searchRaDate: $("input[name=searchRaDate]").val() || "range10",
		searchFlag: $("input[name=searchFlag]").val() || "A",
		searchPlace:  $("input[name=searchPlace]").val() || "",
		searchAucObjDsc: $("input[name=searchAucObjDsc]").val() || "1",
		searchMonthOldC: $("input[name=searchMonthOldC]").val() || "",
		placeNm: $("input[name=placeNm]").val() || "전국"
	}
	COMMONS.callAjax("/dashboard_ajax", "post", params, 'application/json', 'json', function(data){
		if(!data || !data.success){
			modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
			return;
		}
		$(".period-area").html(data.title + '<strong>'+ data.inputParam.placeNm +'</strong>');
		
		// 최근 가축시장 시세
		$("ul.list-market").empty();
		let sHtml = [];
		
		if (data.cowPriceList.length > 0) {
			data.cowPriceList.forEach((e,i) => {				
				sHtml.push('<li class="row_'+ i +'">');
				sHtml.push('	<dl class="item">');
				sHtml.push('		<dt>'+e.MONTH_OLD_C_NM+'</dt>');
				sHtml.push('		<dd>');
				if (e.ACS_SBID_AM > 0) {
					sHtml.push('		<div class="price">'+ fnSetComma(e.THIS_AVG_SBID_AM || 0) +' 원 <span class="per fc-red">▲ '+ e.ACS_SBID_AM +' %</span></div>');					
				} else {
					sHtml.push('		<div class="price">'+ fnSetComma(e.THIS_AVG_SBID_AM || 0) +' 원 <span class="per fc-blue">▼ '+ e.ACS_SBID_AM +' %</span></div>');										
				}
				if (e.ACS_SBID_CNT > 0) {
					sHtml.push('		<div class="num">'+ fnSetComma(e.THIS_SUM_SBID_CNT || 0) +' 두 <span class="per fc-red">▲ '+ e.ACS_SBID_CNT +' %</span></div>');
				} else {
					sHtml.push('		<div class="num">'+ fnSetComma(e.THIS_SUM_SBID_CNT || 0) +' 두 <span class="per fc-blue">▼ '+ e.ACS_SBID_CNT +' %</span></div>');
				}
				sHtml.push('		</dd>');
				sHtml.push('	</dl>');
				sHtml.push('</li>');
			});
		
			$("ul.list-market").append(sHtml.join(" "));
		}
		
		// 전국 TOP 3
		$("ol.list-top10").empty();
		let tHtml = [];
		
		if (data.recentDateTopList.length > 0) {
			for (var i = 0; i < data.recentDateTopList.length; i++) {
				if (i < 3) {
					tHtml.push('<li>');
					tHtml.push('	<dl class="union">');
					tHtml.push('		<dt><img src="/static/images/guide/v2/sample01.jpg" alt=""></dt>');
					tHtml.push('		<dd class="name">'+ data.recentDateTopList[i].CLNTNM +'</dd>');
					if (data.recentDateTopList[i].AMT > 0) {
						tHtml.push('		<dd class="change fc-blue">+'+ fnSetComma(data.recentDateTopList[i].AMT ?? 0) +' 원</dd>');
					} else {
						tHtml.push('		<dd class="change fc-red">'+ fnSetComma(data.recentDateTopList[i].AMT ?? 0) +' 원</dd>');						
					}
					tHtml.push('		<dd class="price fc-red">'+ fnSetComma(data.recentDateTopList[i].SBID_AMT ?? 0) +' 원</dd>');
					if (data.recentDateTopList[i].AUC_OBJ_DSC == '1') {
						tHtml.push('	<dd>'+ data.recentDateTopList[i].INDV_SEX_C_NM + data.recentDateTopList[i].AUC_OBJ_DSC_NM +'<i class="dash"></i>'+ data.recentDateTopList[i].MONTH_C +'개월<i class="dash"></i>'+ data.recentDateTopList[i].RG_DSC_NAME +'</dd>');					
					} else {
						tHtml.push('	<dd>'+ data.recentDateTopList[i].AUC_OBJ_DSC_NM +'<i class="dash"></i>'+ data.recentDateTopList[i].MONTH_C +'개월<i class="dash"></i>'+ data.recentDateTopList[i].RG_DSC_NAME +'</dd>');										
					}
					tHtml.push('	</dl>');
					tHtml.push('</li>');
				}
			}
		} else {
			tHtml.push('<li style="font-size:20px;">금주의 TOP 3이 없습니다.</li>')
		}
		
		$("ol.list-top10").append(tHtml.join(" "));
		
		// 지역별 평균 낙찰가 차트
		setChart(data.avgPlaceBidAmList);
	});
}

var setChart =  function(avgPlaceBidAmList) {
	    // 지역별 평균 낙찰가 만들기
	    var labelData = [];
	    var barData = [];
	    var colorData = ['#ff88a180', '#ff9f4080', '#ffd06080', '#55c4c480', '#41a7ec80', '#a97fff80', '#ff9f40', '#ff779380'];
	    
	    avgPlaceBidAmList.forEach(item => {
			labelData.push(item.LOCNM ?? '없음');
			barData.push(item.AVG_SBID_AM ?? 0);
		})

	    // 막대 차트 생성
		const ctx = $('#myChart1');
		
		new Chart (ctx, {
			type: 'bar',
			data: {
				labels: labelData,
				datasets: [
					{
						label: '평균낙찰가',
						data: barData,
						borderColor: colorData,
						backgroundColor: colorData,
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
		});
}

