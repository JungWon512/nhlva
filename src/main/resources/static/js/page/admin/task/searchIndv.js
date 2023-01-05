;(function (win, $, COMMONS) {
// Class definition
	var Entry = function () {
		var clickEvent = (function() {
			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
				return 'touchstart';
			}
			else {
				return 'click';
			}
		})();
		
		var addBtnEvent = function() {
			// 경매일자 선택 후 선택 가능한 경매유형 변경 이벤트
			$("#aucDt").change(function(){
//				fnInitAucObjDsc();
			});
			
			// 경매유형 버튼 클릭 이벤트
			$(".cow-btns button").on(clickEvent, function(){
				$(this).siblings().removeClass("on");
				$(this).addClass("on");
				$("input[name='aucObjDsc']").val($(this).data("aucObjDsc"));
			});
			
			// 귀표번호 input 삭제 이벤트
			$(".btn_del").on(clickEvent, function(){
				$("#subSraIndvAmnno").val("");
			});
			
			// 개체 조회 이벤트
			$(".btn-search").on(clickEvent, function(){
				fnAucDtSearch();
			});
			
			// 출장우 조회 화면으로 이동
			$(".btn_back").on("click", function(){
				var params = {
					aucDt : $("select[name='aucDt']").val(),
					aucObjDsc : $("select[name='aucObjDsc']").val()
				};
				appendFormSubmit("frm_main", "/office/task/cowList", params);
			});
		};
		
		// 경매유형 변경 이벤트
		var fnInitAucObjDsc = function() {
			$("#aucObjDsc > option").not(":first").prop("disabled", true);
			$("#aucObjDsc").val("").selectric("refresh");
			var arrAucObjDsc = String($("#aucDt > option:selected").data("aucObjDsc")).split(",");
			for (var aucObjDsc of arrAucObjDsc) {
				if (aucObjDsc == "0") {
					$("#aucObjDsc > option").prop("disabled", false);
					break;
				}
				$("#aucObjDsc > option[value='" + aucObjDsc + "']").prop("disabled", false);
			}
		};
		
		// 경매차수 등록여부 확인
		var fnAucDtSearch = function() {
			if($("#aucDt").val() == "") {
				modalAlert("", "경매일자를 선택하세요.");
				return false;
			}

			if($("#aucObjDsc").val() == "") {
				modalAlert("", "경매대상을 선택하세요.");
				return false;
			}
			
			$.ajax({
				url : '/office/task/checkAucDt',
				data : JSON.stringify($("form[name='frm']").serializeObject()),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function() {},
				error: function(xhr, status, error) {
				}
			}).done(function (body) {
				if (body && body.success) {
					fnIndvAmnnoSearch();
				}
				else {
					modalAlert("", body.message);
					return false;
				}
			});
		}
		
		// 개체 정보 조회 후 정보가 있는 경우 등록 페이지로 이동
		// 개체 정보가 없는 경우 진행여부 확인 후 등록 페이지로 이동
		var fnIndvAmnnoSearch = function() {
			if ($("#subSraIndvAmnno").val() == "" || $("#subSraIndvAmnno").val().length != 12) {
				modalAlert("", "개체번호를 정확히 입력하세요.");
				return;
			}
			$.ajax({
				url : '/office/task/searchIndvAjax',
				data : JSON.stringify({
					"na_bzplc" : $("input[name='naBzplc']").val(),
					"auc_dt" : $("select[name='aucDt']").val(),
					"sra_indv_amnno" : "410" + $("input[name='subSraIndvAmnno']").val(),
					"auc_obj_dsc" : $("select[name='aucObjDsc']").val()
				}),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function() {
				},
				error: function(xhr, status, error) {
				}
			}).done(function (body) {
				if (body && body.success) {
					$("#sraIndvAmnno").val("410" + $("input[name='subSraIndvAmnno']").val());
					$("form[name='frm']").attr("action", "/office/task/registCow").submit();
				}
				else {
					if (body.type == "A") {
						modalAlert("", body.message);
						return;
					}
					else {
						modalComfirm("", body.message, function() {
															$("#sraIndvAmnno").val("410" + $("input[name='subSraIndvAmnno']").val());
															$("form[name='frm']").attr("action", "/office/task/registCow").submit();
														});
						return;
					}
				}
			});
		};

		return {
			// public functions
			init: function () {
				addBtnEvent();
//				fnInitAucObjDsc();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
