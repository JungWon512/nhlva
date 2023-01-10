(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
		// 지역별 평균 낙찰가 변경 이벤트
		$(document).on("click","div.btnSex button",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
		});
    };
    
    var setChart =  function() {
		var barChart;
		const ctx = $('#myCharSample5');
		
		var config = {
			type: 'bar',
			data: {
				labels: ['음성', '부천', '나주', '고령'],
				datasets: [
					{
						label: '거래가',
						data: [30000, 50000, 10000, 90000],
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

    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
            setChart();
//            searchAjax();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);

var filterCallBack = function (param) {
	searchAjax(param);
}
var searchAjax = function (param) {
	const params = {
		searchDate: param.searchDate ?? $("input[name=searchDate]").val(),
		searchFlag: param.searchDate ?? $("input[name=searchFlag]").val(),
		searchPlace: param.searchDate ?? $("input[name=searchPlace]").val()
	}
	
	$.ajax({
		url: '/dashboard',
		data: params,
		type: 'POST',
		dataType: 'html',
		async : false,
		success : function(html) {
			console.log(html)
			
//    		$('div.dash-board').empty();
//    		$('div.dash-board').append(html);
		},
		error: function(xhr, status, error) {
		}
	}).done(function (json) {
		console.log(json);
	});
}

