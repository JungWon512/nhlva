;(function (win, $, COMMONS) {
// Class definition
	var Select = function () {
		
		var clickEvent = (function() {
			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
				return 'touchstart';
			}
			else {
				return 'click';
			}
		})();
		
		var addEvent = function(){
			// 메뉴 선택 이벤트
			$(".btn_move").on(clickEvent, function() {
				$("input[name='regType']").val($(this).data("type"));
				$("form[name='frm_select']").attr("action", "/office/task/entry").submit();;
			});
			
			// 선택 화면으로 이동
			$(".btn_back").on(clickEvent, function() {
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("input[name='aucObjDsc']").val()
				};
				appendFormSubmit("frm_main", "/office/task/select", params);
//				window.location.href = "/office/task/select";
			});
		};
		
		return {
			// public functions
			init: function () {
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Select.init();
	});
})(window, window.jQuery);
var fnLayerClose = function(className){
	modalPopupClose('.' + className);		
	$("." + className).remove();	
}
