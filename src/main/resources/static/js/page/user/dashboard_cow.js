(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
		$(".sbidPop").on("click",function(){
			window.open('/dashSbidPop', '지역낙찰가', 'width=700, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
		});
    };
    
    var setChart =  function() {
		
	}

    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
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

