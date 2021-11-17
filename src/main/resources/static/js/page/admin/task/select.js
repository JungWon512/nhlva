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
				$("form[name='frm_select']").attr("action", "/admin/task/entry").submit();;
			});
			
			// 유형 선택 화면으로 이동
			$(".btn_back").on(clickEvent, function() {
//				var params = {
//					aucDt : $("input[name='aucDt']").val(),
//					aucObjDsc : $("input[name='aucObjDsc']:checked").val()
//				};
//				appendFormSubmit("frm_main", "/admin/task/main", params);
				window.location.href = "/admin/task/main";
			});
			
			// 큰소 정보 입력 팝업 
			$(".btn_info_pop").on(clickEvent, function() {
				fnLayerPop("pop_cow_input");
			});
			
			// 정보 조회
			$(document).on(clickEvent, ".btn_cow_search", function() {
				var aucPrgSq = $("form[name='frm_cow_info']").find("input[name='aucPrgSq']").val();
				$("form[name='frm_select']").find("input[name='aucPrgSq']").val(aucPrgSq);
				
				$.ajax({
					url: '/admin/task/cowInfo',
					data: JSON.stringify($("form[name='frm_select']").serializeObject()),
					type: 'POST',
					dataType: 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					}
				}).done(function (body) {
					var success = body.success;
					var message = body.message;
					if (!success) {
						modalAlert("", message);
					}
					else {
						fnSetCowInfo(body);
					}
				});
			});
			
			$(document).on("change", "#ppgcowFeeDsc", function() {
				console.log($(this).val());
			});
		};
		
		// 큰소 정보 입력 팝업
		var fnLayerPop = function(className) {
			modalPopupClose("." + className);
			$("." + className).remove();
			
			var popHtml = [];
			popHtml.push('<div id="" class="modal-wrap ' + className + '">');
			popHtml.push('	<form name="frm_cow_info" method="post">');
			popHtml.push('	<div class="modal-content pop_ad_mod">');
			popHtml.push('		<div class="modal-head">');
			popHtml.push('			<h3>큰소 정보 입력</h3>');
			popHtml.push('			<button class="modal_popup_close" onclick="modalPopupClose(\"' + className + '\");return false;">닫기</button>');
			popHtml.push('		</div>');
			popHtml.push('		<div class="modal-body">');
			popHtml.push('			<div class="table-mod">');
			popHtml.push('				<table>');
			popHtml.push('					<colgroup>');
			popHtml.push('						<col class="col1" style="width:27% !important;" />');
			popHtml.push('						<col class="col2" style="width:73% !important;" />');
			popHtml.push('					</colgroup>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>번호</th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<ul class="num_scr">');
			popHtml.push('								<li><input type="text" name="aucPrgSq" id="aucPrgSq" maxlength="4" pattern="\d*" inputmode="numeric" value="" /></li>'); 
			popHtml.push('								<li><a href="javascript:;" class="btn_cow_search">조회</a></li>');
			popHtml.push('							</ul>');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>출하주</th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<input type="text" name="ftsnm" id="ftsnm" disabled="disabled" value="" />');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>큰소 구분</th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<select name="ppgcowFeeDsc" id="ppgcowFeeDsc">');
			popHtml.push('								<option value="">선택</option>');
			popHtml.push('							</select>');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>임신 개월</th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<select name="matime" id="matime" disabled="disabled">');
			popHtml.push('								<option value="">0개월</option>');
			popHtml.push('								<option value="1">1개월</option>');
			popHtml.push('								<option value="2">2개월</option>');
			popHtml.push('								<option value="3">3개월</option>');
			popHtml.push('								<option value="4">4개월</option>');
			popHtml.push('								<option value="5">5개월</option>');
			popHtml.push('								<option value="6">6개월</option>');
			popHtml.push('								<option value="7">7개월</option>');
			popHtml.push('								<option value="8">8개월</option>');
			popHtml.push('								<option value="9">9개월</option>');
			popHtml.push('							</select>');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>(송)<span>성별<br>개월</span></th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<ul class="harf_ul">');
			popHtml.push('								<li>');
			popHtml.push('									<select name="" id="" disabled="disabled">');
			popHtml.push('										<option value="">선택</option>');
			popHtml.push('									</select>');
			popHtml.push('								</li>');
			popHtml.push('								<li>');
			popHtml.push('									<select name="" id="" disabled="disabled">');
			popHtml.push('										<option value="">0개월</option>');
			popHtml.push('									</select>');
			popHtml.push('								</li>');
			popHtml.push('							</ul>');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('					<tr>');
			popHtml.push('						<th>비고</th>');
			popHtml.push('						<td class="input-td">');
			popHtml.push('							<input type="text" name="rmkCntn" id="rmkCntn" disabled="disabled" value="" />');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			popHtml.push('				</table>');
			popHtml.push('			</div>');
			popHtml.push('		</div>');
			popHtml.push('		<div class="modal-foot">');
			popHtml.push('			<div class="btn_area">');
			popHtml.push('				<a href="javascript:;">초기화</a>');
			popHtml.push('				<a href="javascript:;">저장</a>');
			popHtml.push('				<a href="javascript:;">종료</a>');
			popHtml.push('			</div>');
			popHtml.push('		</div>');
			popHtml.push('	</div>');
			popHtml.push('	</form>');
			popHtml.push('</div>');
			
			$("body").append(popHtml.join(""));
			modalPopup("." + className);
			$("." + className).find("input:first").focus();
			$("select").selectric();
		};
		
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
		
		var fnSetCowInfo = function(data) {
			var cowInfo = data.cowInfo;
			var ppgcowList = data.ppgcowList;
			var indvList = data.indvList;
			
			$("input[name='ftsnm']").val(cowInfo.FTSNM);
			$("input[name='rmkCntn']").val(cowInfo.RMK_CNTN);

			// 큰소 구분
			var $ppgcowFeeDsc = $("select[name='ppgcowFeeDsc']");
			for (var i in ppgcowList) {
				if(ppgcowList[i].SIMP_C == cowInfo.PPGCOW_FEE_DSC) {
					$ppgcowFeeDsc.append($("<option>", {value: ppgcowList[i].SIMP_C, text : ppgcowList[i].SIMP_CNM, selected : "selected"}));
				}
				else {
					$ppgcowFeeDsc.append($("<option>", {value: ppgcowList[i].SIMP_C, text : ppgcowList[i].SIMP_CNM}));
				}
			}
			
			// 임신 개월 PPGCOW_FEE_DSC가 임신우, 임신 + 송아지인 경우 
			if (cowInfo.PPGCOW_FEE_DSC == "1" || cowInfo.PPGCOW_FEE_DSC == "3") {
				var $matime = $("select[name='matime']");
				$matime.prop("disabled", false);
				$matime.find("option").each(function() {
					if (cowInfo.MATIME == $(this).val()) $(this).prop("selected", "selected");
				});
			}
			
			if (cowInfo.PPGCOW_FEE_DSC == "3" || cowInfo.PPGCOW_FEE_DSC == "4") {
				
			}
			
			
			$("select").selectric("refresh");
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
