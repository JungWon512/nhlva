(function ($, win, doc) {
    var setLayout = function() {
    };
    
    var setBinding = function() {
        // 경매대상 변경 이벤트
		$(document).on("click","div.aucObjBtn button",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
			
			// 지역별 평균 낙찰가 버튼 변경
			// 송아지(MONTH_OLD_C) - 전체 / 4~5개월(01) / 6~7개월(02) / **8개월~(03) - 안쓰임
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
			const searchRaDate = $("input[name=searchRaDate]").val() ?? "";
			const searchFlag = $("input[name=searchFlag]").val() ?? "";
			const searchPlace = $("input[name=searchPlace]").val() ?? "";
			window.open('/dashFilter?searchRaDate='+ searchRaDate+'&searchFlag='+searchFlag+'&searchPlace='+searchPlace, '필터', 'width=700, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		});
		// TOP10 클릭 이벤트
		$(document).on("click","#btn-top10",function(){
			window.open('/dashTop10','TOP10', 'width=700, height=900, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
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
            setLayout();
            setBinding();
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
	}
	
	searchAjax();
}
var searchAjax = function () {
	const params = {
		searchRaDate: $("input[name=searchRaDate]").val() || "range10",
		searchFlag: $("input[name=searchFlag]").val() || "A",
		searchPlace:  $("input[name=searchPlace]").val() || "",
		searchAucObjDsc: $("input[name=searchAucObjDsc]").val() || "1",
		searchMonthOldC: $("input[name=searchMonthOldC]").val() || ""
	}
	COMMONS.callAjax("/dashboard_ajax", "post", params, 'application/json', 'json', function(data){
		if(!data || !data.success){
			modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
			return;
		}
		// 날짜 및 지역명
//		$(".period-area").html(data.title + searchPlace == "" ? '<strong>전국</strong>' : '<strong>'+data.cowPriceList[0].LOCNM+'</strong>');
		$(".period-area").html(data.title + "<strong>전국</strong>");
		
		// 최근 가축시장 시세
		$("ul.list-market").empty();
		let sHtml = [];
		
		if (data.cowPriceList.length > 0) {
			sHtml.push('<li>');
			sHtml.push('	<dl class="item dash-all">	');
			sHtml.push('		<dt>전체</dt>	');
			sHtml.push('		<dd>');
			sHtml.push('			<div class="price">3,850,000 원 <span class="per fc-blue">▼ 0.1 %</span></div>');
			sHtml.push('			<div class="num">2,753 두 <span class="per fc-red">▲ 0.1 %</span></div>');
			sHtml.push('		</dd>');
			sHtml.push('	</dl>');
			sHtml.push('</li>');
			
			for (item of data.cowPriceList) {
				sHtml.push('<li>');
				sHtml.push('	<dl class="item">');
				sHtml.push('		<dt>'+item.MONTH_OLD_C_NM+'</dt>');
				sHtml.push('		<dd>');
				sHtml.push('			<div class="price">'+ fnSetComma(item.AVG_SBID_AM) +' 원 <span class="per fc-blue">▼ 0.1 %</span></div>');
				sHtml.push('			<div class="num">'+ fnSetComma(item.SUM_SBID_CNT) +' 두 <span class="per fc-red">▲ 0.1 %</span></div>');
				sHtml.push('		</dd>');
				sHtml.push('	</dl>');
				sHtml.push('</li>');
			}
		}
		
		$("ul.list-market").append(sHtml.join(" "));
		
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

