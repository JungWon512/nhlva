;(function (win, $,COMMONS) {
// Class definition
	var Main = function () {
		
		var clickEvent = (function() {
			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
				return 'touchstart';
			}
			else {
				return 'click';
			}
		})();
		
		var addEvent = function(){
			// 전체, 송아지, 비육우, 번식우 선택 이벤트
			$("input:radio[name='searchAucObjDsc']").change(function(){
				var aucObjDsc = $(this).val();
				$("select[name='searchDate'] > option").show();
				if (aucObjDsc != "") {
					$("select[name='searchDate'] > option").each(function(idx){
						if (idx != 0) {
							if ($(this).data("aucObjDsc").toString().indexOf(aucObjDsc) == -1) {
								$(this).hide();
							}
						}
					});
				}
			});
			
			// 작업 선택 페이지 이동
			$(".btn_start").on(clickEvent, function(){
				if ($("select[name='searchDate']").val() == "") {
					alert("경매일자를 선택하세요");
					return;
				}
				var params = {
					aucDt : $("select[name='searchDate']").val(),
					aucObjDsc : $("input:radio[name='searchAucObjDsc']:checked").val()
				}
				appendFormSubmit("frm_main", "/office/task/select", params);
			});
			
			// 작업 선택 페이지 이동
			$(".btn_end").on(clickEvent, function(){
				pageMove('/office/user/login');
			});
			
			$(".btn_back").on(clickEvent, function() {
				pageMove('/office/main');
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
		Main.init();
	});
})(window, window.jQuery);
