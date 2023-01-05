(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
        // 경매대상 변경 이벤트
		$("div.btnBox button").on("click",function(){
			$(this).siblings("button").removeClass();
			$(this).addClass("on");
		});
		// 필터 클릭 이벤트
		$("#dashboard_filter").on("click",function(){
			window.open('/filter', '필터', 'width=800, height=700, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
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

var filterCallBack = function (params) {
	searchAjax(params);
}
var searchAjax = function (params) {
	console.log(params);
//	$.ajax({
//		url: '/info/getCowInfo',
//		data: param,
//		type: 'POST',
//		dataType: 'html',
//		async : false,
//		success : function(html) {
//    		$('div.cow-detail div.tab_area').empty();
//    		$('div.cow-detail div.tab_area').append(html);
//		},
//		error: function(xhr, status, error) {
//		}
//	}).done(function (json) {
//		console.log(json);
//	});
}

