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

