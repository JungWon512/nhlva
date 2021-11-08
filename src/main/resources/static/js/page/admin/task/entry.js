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
			$(".btn_search").on(clickEvent, function(){
				$("form[name='frm']").attr("action", "/admin/task/entry").submit();
			});
			
			// 리스트 선택
			$(".list_body > ul > li").on(clickEvent, function(){
				$(".list_body > ul > li").siblings().not(this).removeClass("act");
				$(this).toggleClass("act");
			});
			
			// 유형 선택 화면으로 이동
			$(".btn_back").on(clickEvent, function(){
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("select[name='aucObjDsc']:checked").val()
				};
				appendFormSubmit("frm_main", "/admin/task/select", params);
			});
			
			// 변경 팝업
			$(".btn_modify").on(clickEvent, function(){
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
					amnno : amnno
				}
				
				$.ajax({
					url: '/admin/task/cowInfo',
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
			
			// 팝업 닫기
			$(document).on(clickEvent, ".btn_pop_close", function(){
				modalPopupClose('.pop_mod_weight');
			});
			
			// 변경내용 저장
			$(document).on(clickEvent, ".btn_save", function(){
				var regType = $("input[name='regType']").val();
				var regTypeNm = regType == "W" ? "중량" : regType == "N" ? "계류대 번호" : "하한가"; 
				if ($("input.required").val() == "") {
					modalAlert("", regTypeNm + "을(를) 입력하세요");
					return;
				}
				
				$.ajax({
					url : '/admin/task/cowInfo',
					data : JSON.stringify($("form[name='frm_cow']").serializeObject()),
					type : 'PUT',
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
					modalAlert("", message, fnReload);
					return;
				});
			});
			
			$(".btn_input_reset").on(clickEvent, function(){
				$("input[name='searchTxt']").val("");
			});
		};
		
		var fnReload = function() {
			$("form[name='frm']").attr("action", "/admin/task/entry").submit();
		};
		
		var fnLayerPop = function(params, cowInfo) {
			var title = params.regType == 'W' ? '중량 등록' : params.regType == 'N' ? '계류대 변경' : '하한가 등록';
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
			sHtml.push('							<th>어미</th>');
			sHtml.push('							<td>' + cowInfo.MCOW_DSC_NM + '</td>');
			sHtml.push('						</tr>');
			sHtml.push('						<tr>');
			sHtml.push('							<th>성별</th>');
			sHtml.push('							<td>' + cowInfo.INDV_SEX_NAME + '</td>');
			sHtml.push('						</tr>');
			if (params.regType == 'W') {
				sHtml.push('					<tr>');
				sHtml.push('						<th>중량</th>');
				sHtml.push('						<td class="input-td">');
				sHtml.push('							<input type="text" name="cowSogWt" class="pd5 required" value="' + (cowInfo.COW_SOG_WT == 0 ? "" : cowInfo.COW_SOG_WT) + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:80%;" /> kg');
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
				sHtml.push('							<input type="text" name="firLowsSbidLmtAm" class="pd5 required" value="' + (cowInfo.LOWS_SBID_LMT_AM  == 0 ? "" : cowInfo.LOWS_SBID_LMT_AM) + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:80%;" />만 원');
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
				sHtml.push('							<input type="text" name="modlNo" class="pd5 required" value="' + cowInfo.MODL_NO + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:80%;" />');
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
			$(".pop_mod_weight").find("input").focus();
		}
		
		//모달레이어팝업
		var modalPopup = function(target){
			var srlTop = $(window).scrollTop();
			$('.modal-content').css("marginTop", 0);
			var $modalContent = $(target).find($('.modal-content'));
			$(target).css({'overflow': 'auto'}).show().addClass('open');
			$(target).focus();
			var $modalContentH = $(target).find($('.modal-content')).outerHeight();
			var $conPos = ($winH / 2) - ($modalContentH / 2);
			if( $winH > $modalContentH ){
				$modalContent.css({marginTop: $conPos});
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
					scrollInertia: 200,
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
