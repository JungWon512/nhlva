;(function (win, $,COMMONS) {
// Class definition
	var Select = function () {
		var addEvent = function(){
			// 메뉴 선택 이벤트
			$(".btn_move").click(function(){
				$("input[name='regType']").val($(this).data("type"));
				$("form[name='frm_select']").attr("action", "/admin/task/entry").submit();;
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
