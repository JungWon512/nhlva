(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		// 첫화면 전체 클릭 & 경기/인천
		const searchFlag = $("input[name=searchFlag]").val();
		if (searchFlag == "A" || searchFlag == "") {
			$("ul.radio_group.step3 li input").attr("disabled", true);
			$("#A31").prop("checked", true);
		}
    };
    
    var setBinding = function() {
        $(document).on("click","#btn-apply", function(){
			const params = {
				searchRaDate: $("input[name='radioG1']:checked").val(),
				searchFlag : $("input[name='radioG2']:checked").val(),
				searchPlace : $("input[name='radioG2']:checked").val() == 'A' ? '' : $("input[name='radioG3']:checked").val(),
				placeNm : $("input[name=radioG3]:checked")[0].className
			}
        	opener.filterCallBack(JSON.stringify(params));
        	window.close();
		});
		
		$(document).on("change","input[name='radioG2']", function() {
			if ($(this).val() == 'A') {
				$("ul.radio_group.step3 li input").attr("disabled", true);
				$("#A31").prop("checked", true);
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
