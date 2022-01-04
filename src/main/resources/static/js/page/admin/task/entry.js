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
			// 바코드 검색
			$(".btn_search").on("click", function() {
				$("form[name='frm']").attr("action", "/office/task/entry").submit();
			});
			
			// 검색어 지우기
			$(".btn_input_reset").on("click", function(e) {
				$("input[name='searchTxt']").val("");
				$("form[name='frm']").attr("action", "/office/task/entry").submit();
			});-
			
			// 리스트 선택
			$(document).on("dblclick", ".list_body > ul > li", function(e){
				var regType = $("input[name='regType']").val();
				if (regType == "AW") return;
				$(".list_body ul li").not(this).removeClass("act");
				$(this).toggleClass("act");
				
				fnModifyPop($(this));
			});
			
			// 리스트 선택
			$(document).on("click", ".list_body > ul > li", function(e){
//				var regType = $("input[name='regType']").val();
//				if (regType == "AW") return;
//				$(".list_body ul li").not(this).removeClass("act");
//				$(this).toggleClass("act");
			});
			
			// 유형 선택 화면으로 이동
			$(".btn_back").on("click", function(){
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("select[name='aucObjDsc']:checked").val()
				};
				appendFormSubmit("frm_main", "/office/task/select", params);
			});
			
			// 변경 팝업
			$(document).on("click", ".btn_modify", function(){
				fnModifyPop($(this).closest("li"));
			});
			
			// 변경 팝업
			$(".btn_modify_pop").on("click", function(){
				if ($(".list_body > ul").find("li.act").length == 0) {
					modalAlert("", "변경할 대상을 선택하세요.");
					return
				}
				
				var amnno = $(".list_body > ul").find("li.act").find("dd.col1").data("amnno");
				var aucObjDsc = $(".list_body > ul").find("li.act").find("dd.col1").data("aucObjDsc");
				var ledSqno = $(".list_body > ul").find("li.act").find("dd.col1").data("ledSqno");
				var oslpNo = $(".list_body > ul").find("li.act").find("dd.col1").data("oslpNo");
				
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					regType : $("input[name='regType']").val(),
					aucObjDsc : aucObjDsc,
					oslpNo : oslpNo,
					ledSqno : ledSqno,
					amnno : amnno,
					searchTxt : $("input[name='searchTxt']").val()
				}
				
				$.ajax({
					url: '/office/task/cowInfo',
					data: JSON.stringify(params),
					type: 'POST',
					dataType: 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					success : function() {
					},
					error: function(xhr, status, error) {
					}
				}).done(function (body) {
					var success = body.success;
					var message = body.message;
					if (!success) {
						modalAlert("", message);
					}
					else {
						// layer popup 그리기
						fnLayerPop(body.params, body.cowInfo);
					}
				});
			});
			
			// 하한가, 계류대, 중량 숫자 입력만 가능하도록
			$(document).on("keyup", ".onlyNumber", function(){
				var temp = $(this).val().toString().replace(/[^0-9]/, "")
				$(this).val(temp);
			});
			
			// 팝업 닫기
			$(document).on("click", ".btn_pop_close", function(){
				modalPopupClose('.pop_mod_weight');
			});
			
			// 변경내용 저장
			$(document).on("click", ".btn_save", function(){
				var regType = $("input[name='regType']").val();
				var regTypeNm = regType == "W" ? "중량" : regType == "N" ? "계류대 번호" : "하한가"; 
				if ($("input.required").val() == "") {
					modalAlert("", regTypeNm + "을(를) 입력하세요");
					return;
				}
				
				$.ajax({
					url : '/office/task/saveCowInfo',
					data : JSON.stringify($("form[name='frm_cow']").serializeObject()),
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
					var message = body.message;
					modalAlert("", message, fnReload(body));
					return;
				});
			});
			
			// 검색 초기화
			$(".btn_input_reset").on("click", function(){
				$("input[name='searchTxt']").val("");
			});
			
			// 중량입력 input에서 focus out되는 경우 중량정보 저장
			$("input[name='cowSogWt']").on('keydown',function(){
				if(!$(this).val() || $(this).val() == '0') $(this).val('');
			});
			$("input[name='cowSogWt']").on("focusout", function(){				
				if(!$(this).val() || $(this).val() == '') $(this).val('0');
				var li = $(this).closest("li");
				var params = {
					regType : $("input[name='regType']").val()
					, naBzplc : $("input[name='naBzplc']").val()
					, aucDt : $("input[name='aucDt']").val()
					, aucObjDsc : li.find("dd.col1").data("aucObjDsc")
					, oslpNo : li.find("dd.col1").data("oslpNo")
					, ledSqno : li.find("dd.col1").data("ledSqno")
					, cowSogWt : $(this).val()
				}
				
				$.ajax({
					url : '/office/task/saveCowInfo',
					data : JSON.stringify(params),
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
					debugConsole(body);
				});
			});
			
			// 중량입력 input에 focus가 가는 경우 스크롤 이동
			$("input[name='cowSogWt']").on("focus", function(){
				var len =  $(this).val().length;
				$(this)[0].setSelectionRange(len,len);
				var li = $(this).closest("li");
				$(".list_body > ul").animate({
					scrollTop : ($(".list_body > ul > li").index(li) - 1) * ($(".list_body > ul > li:first").height() + 1)
				}, 500);
			});
		};
		
		var fnModifyPop = function(obj) {
			var amnno = obj.find("dd.col1").data("amnno");
			var aucObjDsc = obj.find("dd.col1").data("aucObjDsc");
			var ledSqno = obj.find("dd.col1").data("ledSqno");
			var oslpNo = obj.find("dd.col1").data("oslpNo");
			
			var params = {
				aucDt : $("input[name='aucDt']").val(),
				regType : $("input[name='regType']").val(),
				aucObjDsc : aucObjDsc,
				oslpNo : oslpNo,
				ledSqno : ledSqno,
				amnno : amnno,
				searchTxt : $("input[name='searchTxt']").val()
			}
			
			$.ajax({
				url: '/office/task/cowInfo',
				data: JSON.stringify(params),
				type: 'POST',
				dataType: 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function() {
				},
				error: function(xhr, status, error) {
				}
			}).done(function (body) {
				var success = body.success;
				var message = body.message;
				if (!success) {
					modalAlert("", message);
				}
				else {
					// layer popup 그리기
					fnLayerPop(body.params, body.cowInfo);
				}
			});
		}
		
		var fnReload = function(data) {
			modalPopupClose('.pop_mod_weight');
			var entryList = data.entryList;
			var regType = data.params.regType;
			var aucPrgSq = data.params.aucPrgSq;
			
			var listHtml = [];
			if (entryList != undefined) {
				for (var i = 0; i < entryList.length; i++) {
					var item  = entryList[i];
					listHtml.push('<li id="' + item.AUC_PRG_SQ + '">');
					if (regType != "AW") {
						listHtml.push('<span><button type="button" class="btn_modify" >수정</button></span>');
					}
					listHtml.push('	<dl>');
					listHtml.push('		<dd class="col1" data-amnno="' + item.SRA_INDV_AMNNO+ '" data-auc-obj-dsc="' + item.AUC_OBJ_DSC+ '" data-oslp-no="' + item.OSLP_NO+ '" data-led-sqno="' + item.LED_SQNO+ '">' + item.AUC_PRG_SQ + '</dd>');
					if (regType == "W") {
						listHtml.push('<dd class="col2">' + (item.COW_SOG_WT == null || parseInt(item.COW_SOG_WT) == '0' || parseInt(item.COW_SOG_WT) == 'NaN' ? '-' : parseInt(item.COW_SOG_WT)) + '</dd>');
					}
					else if (regType == "L") {
						listHtml.push('<dd class="col2">' + (item.LOWS_SBID_LMT_UPR == null || parseInt(item.LOWS_SBID_LMT_UPR) == '0' || parseInt(item.LOWS_SBID_LMT_UPR) == 'NaN' ? '-' : fnSetComma(item.LOWS_SBID_LMT_UPR)) + '</dd>');
					}
					else {
						listHtml.push('<dd class="col2" '+(item.MODL_NO != item.AUC_PRG_SQ?'style="color:#ff0000"':'')+'>' + item.MODL_NO + '</dd>');
					}
					listHtml.push('		<dd class="col3">' + item.SRA_INDV_AMNNO_FORMAT + '</dd>');
					listHtml.push('		<dd class="col4">' + item.FTSNM_ORI + '</dd>');
					if(regType != "AW") {
//						listHtml.push('		<dd class="col4 col5"><button type="button" class="btn_modify">수정</button></dd>');
						listHtml.push('		<dd class="col4 col5"></dd>');
					}
					listHtml.push('	</dl>');
					if(regType == "W") {
						listHtml.push('		<div class="pd_etc"><p>'+(item.RMK_CNTN ? item.RMK_CNTN:'')+'</p></div>');
					}
					listHtml.push('</li>');
				}
				
				$(".admin_weight_list").find(".list_body > ul").html(listHtml.join(""));
			}
			var li = $(".list_body > ul").find("li#" + aucPrgSq).next() == undefined ? $(".list_body > ul").find("li#" + aucPrgSq) : $(".list_body > ul").find("li#" + aucPrgSq).next();
			$(".list_body > ul").animate({
				scrollTop : ($(".list_body > ul > li").index(li) - 1) * ($(".list_body > ul > li:first").height() + 1)
			}, 500);
//			if (regType == "L") {
//				li.addClass("act");
//			}
//			else {
				$(".list_body > ul").find("li#" + aucPrgSq).addClass("act");
//			}
		};
		
		var fnLayerPop = function(params, cowInfo) {
			var title = ['W', 'AW'].indexOf(params.regType) > -1 ? '중량 등록' : params.regType == 'N' ? '계류대 변경' : '하한가 등록';
			modalPopupClose('.pop_mod_weight');
			$('.pop_mod_weight').remove();

			var sHtml = [];
			sHtml.push('<div id="" class="modal-wrap pop_mod_weight">');
			sHtml.push('	<div class="modal-content pop_ad_mod">');
			sHtml.push('		<form name="frm_cow" method="post">');
			sHtml.push('			<input type="hidden" name="regType" value="' + params.regType + '" />');
			sHtml.push('			<input type="hidden" name="aucDt" value="' + params.aucDt + '" />');
			sHtml.push('			<input type="hidden" name="naBzplc" value="' + params.naBzplc + '" />');
			sHtml.push('			<input type="hidden" name="aucObjDsc" value="' + params.aucObjDsc + '" />');
			sHtml.push('			<input type="hidden" name="oslpNo" value="' + params.oslpNo + '" />');
			sHtml.push('			<input type="hidden" name="ledSqno" value="' + params.ledSqno + '" />');
			sHtml.push('			<input type="hidden" name="aucPrgSq" value="' + cowInfo.AUC_PRG_SQ + '" />');
			sHtml.push('			<input type="hidden" name="searchTxt" value="' + params.searchTxt + '" />');
			
			sHtml.push('			<div class="modal-head">');
			sHtml.push('				<h3>' + title + '</h3>');
			sHtml.push('				<button class="modal_popup_close btn_pop_close" type="button">닫기</button>');
			sHtml.push('			</div>');
			// modal-body [s]
			sHtml.push('			<div class="modal-body">');
			// table-mod [s]
			sHtml.push('				<div class="table-mod">');
			sHtml.push('					<table>');
			sHtml.push('						<colgroup>');
			sHtml.push('							<col class="col1" width="30%" />');
			sHtml.push('							<col class="" width="*" />');
			sHtml.push('						</colgroup>');
			sHtml.push('						<tr>');
			sHtml.push('							<th>경매번호</th>');
			sHtml.push('							<td>' + cowInfo.AUC_PRG_SQ + '</td>');
			sHtml.push('						</tr>');
			sHtml.push('						<tr>');
			sHtml.push('							<th>귀표번호</th>');
			sHtml.push('							<td>' + cowInfo.SRA_INDV_AMNNO_FORMAT + '</td>');
			sHtml.push('						</tr>');
			sHtml.push('						<tr>');
			sHtml.push('							<th>어미 / 성별</th>');
			sHtml.push('							<td>' + cowInfo.MCOW_DSC_NM + ' / ' + cowInfo.INDV_SEX_NAME + '</td>');
			sHtml.push('						</tr>');
			if (['W', 'AW'].indexOf(params.regType) > -1) {
				sHtml.push('					<tr>');
				sHtml.push('						<th>중량</th>');
				sHtml.push('						<td class="input-td">');
				sHtml.push('							<input type="text" name="cowSogWt" class="pd5 required onlyNumber" value="' + (cowInfo.COW_SOG_WT == null ? "0" : cowInfo.COW_SOG_WT) + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:70%;" /> kg');
				sHtml.push('						</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>비고</th>');
				sHtml.push('						<td class="input-td">');
				sHtml.push('							<input type="text" name="rmkCntn" class="pd5" value="' + (cowInfo.RMK_CNTN == null ? "" : cowInfo.RMK_CNTN) + '" style="width:100%;" />');
//				sHtml.push('							<textarea name="rmkCntn" class="pd5" value="' + cowInfo.RMK_CNTN + '" style="width:100%;" />');
				sHtml.push('						</td>');
				sHtml.push('					</tr>');
			}
			else if (params.regType == 'L') {
				sHtml.push('					<tr>');
				sHtml.push('						<th>산차</th>');
				sHtml.push('						<td>' + cowInfo.MATIME + '</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>중량</th>');
				sHtml.push('						<td>' + cowInfo.COW_SOG_WT + ' kg</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>하한가</th>');
				sHtml.push('						<td class="input-td">');
				sHtml.push('							<input type="text" name="firLowsSbidLmtAm" class="pd5 required onlyNumber" value="' + ((cowInfo.LOWS_SBID_LMT_AM == null || cowInfo.LOWS_SBID_LMT_AM == "0")? "" : cowInfo.LOWS_SBID_LMT_AM) + '" maxlength="5" pattern="\d*" inputmode="numeric" style="width:70%;" />');
				sHtml.push('						</td>');
				sHtml.push('					</tr>');
			}
			else if (params.regType == 'N') {
				sHtml.push('					<tr>');
				sHtml.push('						<th>현 계류대</th>');
				sHtml.push('						<td>' + cowInfo.MODL_NO + '</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>변경</th>');
				sHtml.push('						<td class="input-td">');
				sHtml.push('							<input type="text" name="modlNo" class="pd5 required onlyNumber" value="' + cowInfo.MODL_NO + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:70%;" />');
				sHtml.push('						</td>');
				sHtml.push('					</tr>');
			}
			sHtml.push('					</table>');
			sHtml.push('				</div>');
			// table-mod [e]
			sHtml.push('			</div>');
			// modal-body [e]
			sHtml.push('			<div class="modal-foot">');
			sHtml.push('				<div class="btn_area">');
			sHtml.push('						<a href="javascript:;" class="btn_save">저장</a>');
			sHtml.push('						<a href="javascript:;" class="btn_pop_close">닫기</a>');
			sHtml.push('				</div>');
			sHtml.push('			</div>');
			sHtml.push('		</form>')
			sHtml.push('	</div>');
			sHtml.push('</div>');
			
			$("body").append(sHtml.join(""));
			modalPopup('.pop_mod_weight');
			$(".pop_mod_weight").find("input.required").focus();
			$(".pop_mod_weight").find("input.required").on('keydown',function(){
				if(!$(this).val() || $(this).val() == '0') $(this).val('');
			});
			$(".pop_mod_weight").find("input.required").on('focusout',function(){
				if(!$(this).val() || $(this).val() == '') $(this).val('0');
			});
			var len = $(".pop_mod_weight").find("input.required").val().length;
			$(".pop_mod_weight").find("input.required")[0].setSelectionRange(len,len);
		}
		
		//모달레이어팝업
		var modalPopup = function(target){
			var srlTop = $(window).scrollTop();
			$('.modal-content').css("marginTop", 0);
			var $modalContent = $(target).find($('.modal-content'));
			$(target).css({'overflow': 'auto'}).show().addClass('open');
			$(target).focus();
			var $modalContentH = $(target).find($('.modal-content')).outerHeight();
			if( $winH > $modalContentH ){
				$modalContent.css({marginTop: 20});
			} else {
				$modalContent.css({marginTop: 0});
			}
			$("<div class='overlay'></div>").appendTo('#wrap');
			$('body').addClass('scroll_fixed');
			$("#wrap").css('margin-top', -srlTop + 'px');
			$("#wrap").attr('data',srlTop);
			if($(".modal-content").find('.mCustomScrollBox').length > 0){
				$(".pop_TermsBox").mCustomScrollbar({
					theme:"dark-thin",
					scrollInertia: 200
				});
			}
			return false;
		};
		
		//모달레이어팝업닫기
		var modalPopupClose = function(target){
			$(target).find($('.modal-content')).css('margin-top',0);
			$(target).hide().removeClass('open');
			if (!$(".pop_notice").hasClass('open')) {
				$('html, body').off('scroll touchmove mousewheel');
			}
			$(".overlay").remove();
			$('body').removeClass('scroll_fixed');
			$("#wrap").css('margin-top', 0)
		};

		return {
			// public functions
			init: function () {
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
