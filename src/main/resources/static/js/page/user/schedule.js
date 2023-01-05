;(function (win, $,COMMONS) {
// Class definition
	var Schedule = function () {

		var addEvent = function(){
			// 가축시장 정보 팝업
			$(document).on("click",".btn_detail_pop", function(){
				var name = $(this).parent().siblings(".s_market").text();
				var telno = $(this).parent().siblings(".s_telno").text();
				var addr = $(this).parent().siblings(".s_addr").text();
				$(".pop_schedule_map").find("p.name").text(name);
				$(".pop_schedule_map").find("dd.tel").text(telno);
				$(".pop_schedule_map").find("dd.addr").text(addr);
			});
			
			// Tab 이동
			$(".btn_tab").click(function() {
				var params = {type : $(this).data("type")};
				appendFormSubmit("frm_move", "/schedule", params);
			});
		};

		return {
			init: function () {
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Schedule.init();
	});
})(window, window.jQuery);
