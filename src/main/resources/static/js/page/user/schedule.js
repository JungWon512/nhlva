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
				$(".pop_schedule_map").find("dd.tel").html(telno.replace(/[^0-9|-]|/gi, "") + "<a href='tel:" + telno.replace(/[^0-9|-]/gi, "") + "'></a>");
				$(".pop_schedule_map").find("dd.addr").text(addr);
			});
			
			// Tab 이동
			$(".btn_tab").click(function() {
				var params = {type : $(this).data("type")};
				appendFormSubmit("frm_move", "/schedule", params);
			});
			
			$(".call").click(function(){
				alert("123")
				$(location).attr('href', "tel:010-8965-3832");
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
