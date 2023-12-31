(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		// 첫화면 전체 클릭
		$("ul.radio_group.step3 li input").attr("disabled", true);
    };
    
    var setBinding = function() {
        $(document).on("click","#btn-apply", function(){
			const params = {
				radioG1: $("input[name='radioG1']:checked").val(),
				radioG2: $("input[name='radioG2']:checked").val(),
				radioG3: $("input[name='radioG3']:checked").val()
			}
        	window.opener.filterCallBack(JSON.stringify(params));
        	window.close();
		});
		
		$(document).on("change","input[name='radioG2']", function() {
			if ($(this).val() == 'A') {
				$("ul.radio_group.step3 li input").attr("disabled", true);
			} else {
				$("ul.radio_group.step3 li input").attr("disabled", false);				
			}
		})
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "FILTER"; 
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
