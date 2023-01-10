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
		
		var addEvent = function(){
//			addCalendarEvent();
		};
		
		var addBtnEvent = function(){
			// 개체검색 화면으로 이동
			$(".btn_back").on("click", function(){
				fnClose();
			});
			
			// 닫기 이벤트
			$(".btn_close").on(clickEvent, function() {
				fnClose();
			});
			// 초기화 이벤트
			$(".btn_clear").on(clickEvent, function() {
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("input[name='aucObjDsc']").val()
				};
				appendFormSubmit("frm_main", "/office/task/searchIndv", params);
			});
			// 저장 이벤트
			$(".btn_save").on(clickEvent, function() {
				fnSave();
			});
			// 재입력 이벤트
			$(".btn_reEnter").on(clickEvent, function() {
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("input[name='aucObjDsc']").val()
				};
				appendFormSubmit("frm_main", "/office/task/searchIndv", params);
			});
			
			// input 값 초기화 이벤트
			$(".btn_input_reset").on(clickEvent, function() {
				$(this).siblings("input").val("");
			});
			
			// 임신구분 변경 이벤트
			$("select[name='ppgcowFeeDsc']").on("change", function(){
				var ppgCow = ["1", "3"];
				if (!ppgCow.includes($(this).val())) {
					$(".ppgcow").find("input").val("");
					$(".ppgcow").find("input").prop("checked", false).trigger("change");
				}
				else {
					$(".ppgcow").find("input#prnyJugYn").prop("checked", true).trigger("change");
				}
			});
			
			// 인공수정일자 변경시 임신개월수 자동 입력
			$("input[name='afismModDt']").on("change", function(){
				var aucDt = dayjs($("input[name='aucDt']").val());
				if ($(this).val().length == 10) {
					var afismModDt = dayjs($(this).val());
					if (afismModDt.isAfter(aucDt)) {
						modalAlert("", "인공수정일을 확인하세요.");
						$("input[name='afismModDt']").val("");
						return;
					}
					$("input[name='prnyMtcn']").val(aucDt.diff(afismModDt, "month") + 1);
					$("input[name='pturPlaDt']").val(afismModDt.add(285, "day").format("YYYY-MM-DD"));
				}
			});
			
			// 체크박스 체크/해제에 따라 value, label 수정
			$("input:checkbox").on("change", function() {
				$(this).val($(this).is(":checked") ? "1" : "0");
				$(this).next("label").text($(this).is(":checked") ? "여" : "부");
			});
		};

		var fnClose = function() {
			var regMode = $("input[name='regMode']").val();
			var url = (regMode == "modify") ? "/office/task/cowList" : "/office/task/searchIndv";
			var params = {
				aucDt : $("input[name='aucDt']").val(),
				aucObjDsc : (regMode == "modify" ? "" : $("input[name='aucObjDsc']").val())
			};
			appendFormSubmit("frm_main", url, params);
		};
		
		var fnSave = function() {
			if(fnRequriredChk() && fnValueChk()) {
				$.ajax({
					url : '/office/task/registCowAjax',
					data : JSON.stringify($("form[name='frm']").serializeObject()),
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
						modalAlert("", body.message, function(){
							var params = {
								aucDt : $("input[name='aucDt']").val()
							};
							appendFormSubmit("frm_main", "/office/task/cowList", params);
						});
					}
					else {
						modalAlert("", body.message);
						return;
					}
				});
			}
		};
		
		// 필수인자 입력여부 체크
		var fnRequriredChk = function() {
			var required = $(".required");
			for (var i = 0; i < required.length; i++) {
				if ($(required[i]).val() != "") continue;
				 
				var tagName = $(required[i]).prop("tagName");
				var desc = $(required[i]).attr("placeholder");
				if (tagName == "INPUT" || tagName == "TEXTAREA") {
					modalAlert("", desc + "을/를 입력하세요.", function(){$(required[i]).focus()});
					return false;
				}
				else if (tagName == "SELECT") {
					modalAlert("", desc + "을/를 입력하세요.", function(){$(required[i]).focus()});
					return false;
				}
			}
			return true;
		};
		
		// 입력값 체크
		var fnValueChk = function() {
			var baseLmtAm = $("input[name='baseLmtAm']").val();																				// 경매차수관리에서 등록한 응찰한도금액
			var firLowsSbidLmtAm = $("input[name='firLowsSbidLmtAmTmp']").val() == "" ? "0" : $("input[name='firLowsSbidLmtAmTmp']").val();	// 예정가
			var divisionPrice = $("input[name='divisionPrice']").val();																		// 응찰단위
			
			$("input[name='firLowsSbidLmtAm']").val("");
			$("input[name='lowsSbidLmtAm']").val("");
			// 응찰한도금액보다 예정가가 큰 경우 입력X
			if (parseInt(firLowsSbidLmtAm) * divisionPrice > parseInt(baseLmtAm)) {
				modalAlert("", "예정가가 최고 응찰 한도금액을 </br>초과 하였습니다.", function(){$("input[name='firLowsSbidLmtAmTmp']").focus()});
				return false;
			}
			$("input[name='firLowsSbidLmtAm']").val(parseInt(firLowsSbidLmtAm) * parseInt(divisionPrice||'0'));
			$("input[name='lowsSbidLmtAm']").val(parseInt(firLowsSbidLmtAm) * parseInt(divisionPrice||'0'));
			
			return true;
		}

		return {
			// public functions
			init: function () {
				addEvent();
				addBtnEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
