(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		let aucObjDscNm = opener.$(".auc_obj_dsc").find("button.on").text();
		let monthOldCNm = opener.$(".birth_weight_dsc").find("button.on").text();
		let searchPlaceNm = "낙찰가 현황 I " + opener.$("#searchPlaceNm").val();
		
		$(".winpop_tit").html(searchPlaceNm);
		$(".sort2").html(aucObjDscNm);
		$(".sort3").html(monthOldCNm);
    };
    
    var setBinding = function() {
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "SBID_POP"; 
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
