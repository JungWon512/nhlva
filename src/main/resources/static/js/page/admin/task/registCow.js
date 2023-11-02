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
			fnCallBrclIspSrch();
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
				var inNm = $(this).siblings("input").attr('name');
				if(inNm === 'brclIspDt'){
					$('input[name=brclElapseDate]').val('');
				}else if(inNm === 'vacnDt'){
					$('input[name=vacnElapseDate]').val('');
				}
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
			
			//브루셀라 경과일 계산
			$("input[name='brclIspDt']").on("change", function(){
				var aucDt = new Date($('td[name=aucDt]').text());
				if($(this).val()){
					var brclIspDt = new Date($(this).val());
					$('input[name=brclElapseDate]').val((aucDt.getTime() - brclIspDt.getTime()) /1000 / 60 / 60 / 24);
				}
			});
			
			//구제역 경과일 계산
			$("input[name='vacnDt']").on("change", function(){
				var aucDt = new Date($('td[name=aucDt]').text());
				if($(this).val()){
					var vacnDt = new Date($(this).val());
					$('input[name=vacnElapseDate]').val((aucDt.getTime() - vacnDt.getTime()) /1000 / 60 / 60 / 24);
				}
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
		
		var fnCallBrclIspSrch = function(){
			$.ajax({
				url : '/office/task/animalTrace',
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
					var vacnInfo = body.vacnInfo;
		            //$("#brclIspDt").val($.trim(vacnInfo["insepctDt"]));
		         	if(vacnInfo){
						console.log(vacnInfo);						
			            
			            if(!$('input[name=brclIspDt]').val()){
				        	$("input[name=brclIspDt]").val(fn_toDate($.trim(vacnInfo["inspectDt"])));						
				            // 브루셀라 접종결과 추후 추가 0:수기 1:음성 2:양성 9:미접종 brcl_isp_rzt_c
				            if($.trim(vacnInfo["inspectYn"]) == "음성") {
				            	$("input[name=brclIspRztC]").val("1");
				            } else if($.trim(vacnInfo["inspectYn"]) == "양성") {
				            	$("input[name=brclIspRztC]").val("2");
				            } else {
				            	$("input[name=brclIspRztC]").val("0");
				            }
						}
			            if(!$('input[name=vacnDt]').val()){
				        	$("input[name=vacnDt]").val(fn_toDate($.trim(vacnInfo["injectionYmd"])));
				        	$("input[name=vacnOrder]").val($.trim(vacnInfo["vaccineorder"]));      
			        	}
			            if(!$('input[name=bovineDt]').val()){
				        	$("input[name=bovineDt]").val(fn_toDate($.trim(vacnInfo["tbcInspctYmd"])));
				        	$("input[name=bovineRsltnm]").val($.trim(vacnInfo["tbcInspctRsltNm"]));		
			        	}
					 }else{						 
		            	$("input[name=brclIspRztC]").val("0");
			        	//$("input[name=brclIspDt]").val('');			        	
			        	//$("input[name=vacnOrder]").val('');        	
			        	//$("input[name=vacnDt]").val('');
			        	//$("input[name=bovineDt]").val('');
			        	//$("input[name=bovineRsltnm]").val('');						 
					 }
				}
				else {
					modalAlert("", body.message);
					return;
				}
				function fn_toDate(date,type){
				  if(!date || date.length == 0){
				  	return '';
				  }
				  var yyyy = date.substr(0,4);
				  var mm = date.substr(4,2);
				  var dd = date.substr(6,2);
				  if(type =="KR"){
					  return yyyy + '년 ' + mm + '월 ' +dd + '일';  
				  }else {
					  return yyyy + '-' + mm + '-' +dd; 
				  }
				  
				}
				
				//브루셀라 경과일
				var aucDt = new Date($('td[name=aucDt]').text());
				if($('input[name=brclIspDt]').val()){
					var brclIspDt = new Date($('input[name=brclIspDt]').val());
					$('input[name=brclElapseDate]').val((aucDt.getTime() - brclIspDt.getTime()) /1000 / 60 / 60 / 24);
				}
				if($('input[name=vacnDt]').val()){
					var vacnDt = new Date($('input[name=vacnDt]').val());
					$('input[name=vacnElapseDate]').val((aucDt.getTime() - vacnDt.getTime()) /1000 / 60 / 60 / 24);
				}
				;
			});			
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
