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
				$("form[name='frm_select']").attr("action", "/office/task/entry").submit();
			});
			
			// 유형 선택 화면으로 이동
			$(".btn_back").on(clickEvent, function() {
//				var params = {
//					aucDt : $("input[name='aucDt']").val(),
//					aucObjDsc : $("input[name='aucObjDsc']:checked").val()
//				};
//				appendFormSubmit("frm_main", "/office/task/main", params);
				window.location.href = "/office/task/main";
			});
			
			// 큰소 정보 입력 팝업 
			$(".btn_info_pop.cow").on(clickEvent, function() {
				fnLayerPop("cow_input");
			});
			// 경매상태 입력 팝업 
			$(".btn_info_pop.status").on(clickEvent, function() {
				fnLayerPop("status_change");
			});
			
			// 정보 조회
			$(document).on(clickEvent, ".cow_input .btn_cow_search", function() {
				var aucPrgSq = $("form[name='frm_cow_info']").find("input[name='aucPrgSq']").val();
				
				if(aucPrgSq ==null || aucPrgSq == ''){
					modalAlert("", '경매번호를 입력해주세요.');
					return
				}
				$("form[name='frm_select']").find("input[name='aucPrgSq']").val(aucPrgSq);
				$("form[name='frm_select']").find("input[name='regType']").val("I");
				
				$.ajax({
					url: '/office/task/cowInfo',
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
						modalAlert("", message, fnReset);
					}
					else {
						fnSetCowInfo(body);
					}
				});
			});
			
			//경매상태 정보 조회
			$(document).on(clickEvent, ".status_change .btn_cow_search", function() {
				var aucPrgSq = $("form[name='frm_cow_info']").find("input[name='aucPrgSq']").val();
				
				if(aucPrgSq ==null || aucPrgSq == ''){
					modalAlert("", '경매번호를 입력해주세요.');
					return
				}
				$("form[name='frm_select']").find("input[name='aucPrgSq']").val(aucPrgSq);
				$("form[name='frm_select']").find("input[name='regType']").val("S");
				
				$.ajax({
					url: '/office/task/cowInfo',
					data: JSON.stringify($("form[name='frm_select']").serializeObject()),
					type: 'POST',
					dataType: 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					}
				}).done(function (data) {
					fnStatusReload(data);
				});
			});
			
			$(document).on("change", ".modal-wrap.status_change select[name=selStsDsc]", function() {
				if($(this).val() == '22'){
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("disabled", false);
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("disabled", false);
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).focus();					
				}else{
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val('');
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val('');
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("disabled", true);
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("disabled", true);
					
				}
			});
			// 큰소 구분 선택 이벤트
			$(document).on("change", "#ppgcowFeeDsc", function() {
				var ppgcowFeeDsc = $(this).val();
				// 임신우, 임신우 + 송아지인 경우 임신개월 활성화
				if (ppgcowFeeDsc == "1" || ppgcowFeeDsc == "3") {
					$("select[name='prnyMtcn']").prop("disabled", false);
				}
				else {
					$("select[name='prnyMtcn']").val("").prop("disabled", true);
				}
				
				// 임신우 + 송아지, 비임신우 + 송아지인 경우 송아지 성별, 개월 활성화
				if (ppgcowFeeDsc == "3" || ppgcowFeeDsc == "4") {
					$("select[name='ccowIndvSexC']").prop("disabled", false);
					$("select[name='ccowBirthMonth']").prop("disabled", false);
				}
				else {
					$("select[name='ccowIndvSexC']").val("").prop("disabled", true);
					$("select[name='ccowBirthMonth']").val("").prop("disabled", true);
				}
				$("select").selectric("refresh");
				// 비고에 임신 정보 append
				fnSetRmkCntn();
			});

			// 임신 개월, 송아지 구분, 송아지 개월 선택 이벤트
			$(document).on("change", "#prnyMtcn, #ccowIndvSexC, #ccowBirthMonth", function() {
				// 비고에 임신 정보 append
				fnSetRmkCntn();
			});
			
			// 송아지 구분 선택 이벤트
//			$(document).on("change", "#ccowIndvSexC", function() {
//				// 비고에 임신 정보 append
//				fnSetRmkCntn();
//			});
//			
//			// 송아지 개월 선택 이벤트
//			$(document).on("change", "#ccowBirthMonth", function() {
//				// 비고에 임신 정보 append
//				fnSetRmkCntn();
//			});
			
			$(document).on(clickEvent, ".modal-wrap.cow_input .btn_reset", function(){
				fnReset();
			});
			
			$(document).on(clickEvent, ".modal-wrap.cow_input .btn_save", function(){
				fnSave();
			});
			
			$(document).on(clickEvent, ".modal-wrap.status_change .btn_reset", function(){
				fnReset();
			});
			
			$(document).on(clickEvent, ".modal-wrap.status_change .btn_save", function(){
				fnStatusSave();
			});
			
			// 출장우 관리 페이지 이동
			$(document).on(clickEvent, ".btn_cow_list", function(){
				$("form[name='frm_select']").attr("action", "/office/task/cowList").submit();
			});
		};
		
		// 큰소 정보 입력 팝업
		var fnLayerPop = function(className) {
			modalPopupClose("." + className);
			$("." + className).remove();
			
			var popHtml = [];
			popHtml.push('<div id="" class="modal-wrap pop_cow_input ' + className + '">');
			popHtml.push('	<form name="frm_cow_info" method="post">');
			popHtml.push('		<input type="hidden" name="aucObjDsc" value="" />');
			if(className == 'cow_input'){
				popHtml.push('		<input type="hidden" name="regType" value="I" />');
			}else if(className =='status_change'){				
				popHtml.push('		<input type="hidden" name="qcnAucObjDsc" value="" />');
				popHtml.push('		<input type="hidden" name="regType" value="S" />');				
			}
			popHtml.push('		<input type="hidden" name="oslpNo" value="" />');
			popHtml.push('		<input type="hidden" name="ledSqno" value="" />');
			popHtml.push('	<div class="modal-content pop_ad_mod">');
			popHtml.push('		<div class="modal-head">');
			if(className == 'cow_input'){
				popHtml.push('			<h3>큰소 정보 입력</h3>');
			}else if(className =='status_change'){
				popHtml.push('			<h3>경매상태 변경</h3>');				
			}			
			popHtml.push('			<button type="button" class="modal_popup_close" onclick="javascript:fnLayerClose(\'' + className + '\'); return false;">닫기</button>');
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
			popHtml.push('								<li><input type="text" name="aucPrgSq" class="required" id="aucPrgSq" alt="경매번호" maxlength="4" pattern="\d*" inputmode="numeric" value="" /></li>'); 
			popHtml.push('								<li><a href="javascript:;" class="btn_cow_search">조회</a></li>');
			popHtml.push('							</ul>');
			popHtml.push('						</td>');
			popHtml.push('					</tr>');
			if(className == 'cow_input'){
				popHtml.push('					<tr>');
				popHtml.push('						<th>출하주</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<input type="text" name="ftsnm" id="ftsnm" disabled="disabled" alt="출하주" value="" />');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>큰소 구분</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<select name="ppgcowFeeDsc" class="required" id="ppgcowFeeDsc" alt="큰소 구분" disabled="disabled">');
				popHtml.push('								<option value="">선택</option>');
				popHtml.push('							</select>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>임신 개월</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<select name="prnyMtcn" class="required" id="prnyMtcn" alt="임신 개월" disabled="disabled">');
				popHtml.push('								<option value="">선택</option>');
				popHtml.push('								<option value="0">0개월</option>');
				popHtml.push('								<option value="1">1개월</option>');
				popHtml.push('								<option value="2">2개월</option>');
				popHtml.push('								<option value="3">3개월</option>');
				popHtml.push('								<option value="4">4개월</option>');
				popHtml.push('								<option value="5">5개월</option>');
				popHtml.push('								<option value="6">6개월</option>');
				popHtml.push('								<option value="7">7개월</option>');
				popHtml.push('								<option value="8">8개월</option>');
				popHtml.push('								<option value="9">9개월</option>');
				popHtml.push('								<option value="10">10개월</option>');
				popHtml.push('							</select>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>(송)<span>성별<br>개월</span></th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<ul class="harf_ul">');
				popHtml.push('								<li>');
				popHtml.push('									<select name="ccowIndvSexC" class="required" alt="송아지 성별" id="ccowIndvSexC" disabled="disabled">');
				popHtml.push('										<option value="">선택</option>');
				popHtml.push('									</select>');
				popHtml.push('								</li>');
				popHtml.push('								<li>');
				popHtml.push('									<select name="ccowBirthMonth" class="required" alt="송아지 개월" id="ccowBirthMonth" disabled="disabled">');
				popHtml.push('										<option value="">선택</option>');
				popHtml.push('										<option value="1">1개월</option>');
				popHtml.push('										<option value="2">2개월</option>');
				popHtml.push('										<option value="3">3개월</option>');
				popHtml.push('										<option value="4">4개월</option>');
				popHtml.push('										<option value="5">5개월</option>');
				popHtml.push('										<option value="6">6개월</option>');
				popHtml.push('										<option value="7">7개월</option>');
				popHtml.push('										<option value="8">8개월</option>');
				popHtml.push('										<option value="9">9개월</option>');
				popHtml.push('									</select>');
				popHtml.push('								</li>');
				popHtml.push('							</ul>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>수의사</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<select name="lvstMktTrplAmnno" class="" id="lvstMktTrplAmnno" alt="수의사" disabled="disabled">');
				popHtml.push('								<option value="0">선택</option>');
				popHtml.push('							</select>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				//popHtml.push('						<th>비고</th>');
				popHtml.push('						<td class="input-td" colspan="2">');
				popHtml.push('							<input type="text" name="rmkCntn" id="rmkCntn" value="" alt="비고" placeholder="비고" />');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				
			}else if(className =='status_change'){
				popHtml.push('					<tr>');
				popHtml.push('						<th>예정가</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<input type="text" name="lowsSbidLmtAm" id="lowsSbidLmtAm" value="" alt="예정가" disabled="disabled"/>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>경매상태</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<select name="selStsDsc" class="required" alt="경매상태" id="selStsDsc">');
				popHtml.push('								<option value="">선택</option>');
				popHtml.push('								<option value="11">송장등록</option>');
				popHtml.push('								<option value="21">경매</option>');
				popHtml.push('								<option value="22">낙찰</option>');
				popHtml.push('								<option value="23">유찰</option>');
				popHtml.push('							</select>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>낙찰자<br/>참가번호</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<input type="text" name="lvstAucPtcMnNo" id="lvstAucPtcMnNo" class="required" value="" alt="낙찰자" disabled="disabled"/>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				popHtml.push('					<tr>');
				popHtml.push('						<th>낙찰단가</th>');
				popHtml.push('						<td class="input-td">');
				popHtml.push('							<input type="text" name="sraSbidUpr" id="sraSbidUpr" class="required" value="" alt="낙찰단가" disabled="disabled"/>');
				popHtml.push('						</td>');
				popHtml.push('					</tr>');
				
			}
		
			popHtml.push('				</table>');
			popHtml.push('			</div>');
			popHtml.push('		</div>');
			popHtml.push('		<div class="modal-foot">');
			popHtml.push('			<div class="btn_area">');
			popHtml.push('				<a href="javascript:;" class="btn_reset">초기화</a>');
			popHtml.push('				<a href="javascript:;" class="btn_save">저장</a>');
			popHtml.push('				<a href="javascript:fnLayerClose(\'' + className + '\');">종료</a>');
			popHtml.push('			</div>');
			popHtml.push('		</div>');
			popHtml.push('	</div>');
			popHtml.push('	</form>');
			popHtml.push('</div>');
			
			$("body").append(popHtml.join(""));
			modalPopup("." + className);
			$("." + className).find("input:first").focus();
			$("select").selectric("refresh");
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
		
		// 출장우 정보 set
		var fnSetCowInfo = function(data) {
			var cowInfo = data.cowInfo;
			var ppgcowList = data.ppgcowList;
			var indvList = data.indvList;
			var vetList = data.vetList;
			
			$("input[name='ftsnm']", $("form[name='frm_cow_info']")).val(cowInfo.FTSNM);
			$("input[name='rmkCntn']", $("form[name='frm_cow_info']")).val(cowInfo.RMK_CNTN);
			$("input[name='aucObjDsc']", $("form[name='frm_cow_info']")).val(cowInfo.AUC_OBJ_DSC);
			$("input[name='oslpNo']", $("form[name='frm_cow_info']")).val(cowInfo.OSLP_NO);
			$("input[name='ledSqno']", $("form[name='frm_cow_info']")).val(cowInfo.LED_SQNO);
//			$("input[name='aucPrgSq']", $("form[name='frm_select']")).val(cowInfo.AUC_PRG_SQ);

			var $lvstMktTrplAmnno = $("select[name='lvstMktTrplAmnno']");
			if (vetList.length > 0) {
				$lvstMktTrplAmnno.prop("disabled", false);
				$lvstMktTrplAmnno.find("option").not(":first").remove();
				for (var i in vetList) {
					if (vetList[i].LVST_MKT_TRPL_AMNNO == cowInfo.LVST_MKT_TRPL_AMNNO) {
						$lvstMktTrplAmnno.append($("<option>", {value: vetList[i].LVST_MKT_TRPL_AMNNO, text : vetList[i].BRKR_NAME, selected : "selected"}));
					}
					else {
						$lvstMktTrplAmnno.append($("<option>", {value: vetList[i].LVST_MKT_TRPL_AMNNO, text : vetList[i].BRKR_NAME}));
					}
				}
			}
			else {
				$lvstMktTrplAmnno.prop("disabled", true);
			}

			// 큰소 구분
			var $ppgcowFeeDsc = $("select[name='ppgcowFeeDsc']");
			$ppgcowFeeDsc.prop("disabled", false);
			$ppgcowFeeDsc.find("option").not(":first").remove();
			for (var i in ppgcowList) {
				if(ppgcowList[i].SIMP_C == cowInfo.PPGCOW_FEE_DSC) {
					$ppgcowFeeDsc.append($("<option>", {value: ppgcowList[i].SIMP_C, text : ppgcowList[i].SIMP_CNM, selected : "selected"}));
				}
				else {
					$ppgcowFeeDsc.append($("<option>", {value: ppgcowList[i].SIMP_C, text : ppgcowList[i].SIMP_CNM}));
				}
			}
			
			// 임신 개월 > PPGCOW_FEE_DSC가 임신우, 임신 + 송아지인 경우 
			var $prnyMtcn = $("select[name='prnyMtcn']");
			if (cowInfo.PPGCOW_FEE_DSC == "1" || cowInfo.PPGCOW_FEE_DSC == "3") {
				$prnyMtcn.prop("disabled", false);
				$prnyMtcn.find("option").each(function() {
					if (cowInfo.PRNY_MTCN == $(this).val()) $(this).prop("selected", "selected");
				});
			}
			else {
				$prnyMtcn.val("");
				$prnyMtcn.prop("disabled", true);
			}
			
			// 송아지 정보 > PPGCOW_FEE_DSC가 임신 + 송아지, 비임신 + 송아지인 경우 
			var $ccowIndvSexC = $("select[name='ccowIndvSexC']");
			var $ccowBirthMonth = $("select[name='ccowBirthMonth']");
			// 송아지 성별 설정
			$ccowIndvSexC.find("option").not(":first").remove();
			for (var i in indvList) {
				if(indvList[i].SIMP_C == cowInfo.CCOW_INDV_SEX_C) {
					$ccowIndvSexC.append($("<option>", {value: indvList[i].SIMP_C, text : indvList[i].SIMP_CNM, selected : "selected"}));
				}
				else {
					$ccowIndvSexC.append($("<option>", {value: indvList[i].SIMP_C, text : indvList[i].SIMP_CNM}));
				}
			}

			if (cowInfo.PPGCOW_FEE_DSC == "3" || cowInfo.PPGCOW_FEE_DSC == "4") {
				$ccowIndvSexC.prop("disabled", false);
				$ccowBirthMonth.prop("disabled", false);
				// 송아지 개월 수 설정
				$ccowBirthMonth.find("option").each(function() {
					if (cowInfo.GAP_MONTH == $(this).val()) $(this).prop("selected", "selected");
				});
			}
			else {
				$ccowIndvSexC.val("");
				$ccowIndvSexC.prop("disabled", true);
				$ccowBirthMonth.val("");
				$ccowBirthMonth.prop("disabled", true);
			}
			
			$("select").selectric("refresh");
			fnSetRmkCntn();
		};
		
		// 큰소 구분, 임신 개월, 송아지 정보 변경에 따라 비고내용 자동완성
		var fnSetRmkCntn = function() {
			// 어미소 정규식 패턴
			var mcowPattern = /임신[0-9]{1,2}개월|만삭/gi;
			// 송아지 정규식 패턴
			var ccowPattern = /[암|수|거세|미경산|비거세|프리마틴|공통]{0,4}송아지[0-9]{1,2}개월/gi;
			
			var ppgcowFeeDsc = $("#ppgcowFeeDsc").val();
			var prnyMtcn = $("#prnyMtcn").val() == "" ? 0 : parseInt($("#prnyMtcn").val());
			var ccowIndvSexC = ($("#ccowIndvSexC").val() == "0" || $("#ccowIndvSexC").val() == "") ? "" : $("#ccowIndvSexC").find("option:selected").text();
			var ccowBirthMonth = $("#ccowBirthMonth").val() == "" ? 0 : parseInt($("#ccowBirthMonth").val());
			
			var mcowText = "";
			if ((ppgcowFeeDsc == "1" || ppgcowFeeDsc == "3") && prnyMtcn > 0) {
				if(prnyMtcn > 9)mcowText = "만삭";
				else mcowText = "임신 " + prnyMtcn + "개월";
			}
			
			var ccowText = "";
			if ((ppgcowFeeDsc == "3" || ppgcowFeeDsc == "4") && ccowBirthMonth > 0) {
				ccowText = ccowIndvSexC + "송아지 " + ccowBirthMonth + "개월";
			}
			
			var rmkCntn = $("#rmkCntn");
			var arrRmkCntn = $("#rmkCntn").val().split(",");
			var newArrRmkCntn = [];
			if (rmkCntn.val().replace(/ /g, "").search(mcowPattern) == -1 && rmkCntn.val().replace(/ /g, "").search(ccowPattern) == -1) {
				newArrRmkCntn = arrRmkCntn;
				if (mcowText != "") newArrRmkCntn.push(mcowText);
				if (ccowText != "") newArrRmkCntn.push(ccowText);
			}
			else {
				for (var i in arrRmkCntn) {
					if (arrRmkCntn[i].replace(/ /g, "").search(mcowPattern) == -1 && arrRmkCntn[i].replace(/ /g, "").search(ccowPattern) == -1) {
						newArrRmkCntn.push(arrRmkCntn[i]);
						continue;
					}
					if (arrRmkCntn[i].replace(/ /g, "").search(mcowPattern) > -1) {
						newArrRmkCntn.push(arrRmkCntn[i].replace(/ /g, "").replace(mcowPattern, mcowText));
						continue;
					}
					if (arrRmkCntn[i].replace(/ /g, "").search(ccowPattern) > -1) {
						newArrRmkCntn.push(arrRmkCntn[i].replace(/ /g, "").replace(ccowPattern, ccowText));
						continue;
					}
				}
				if (mcowText != "") newArrRmkCntn.push(mcowText);
				if (ccowText != "") newArrRmkCntn.push(ccowText);
			}

			const uniqueArr = newArrRmkCntn.filter((element, index) => {
				return (newArrRmkCntn.indexOf(element) === index && element != "")
			});
			rmkCntn.val(uniqueArr.join(","));
		};
		
		// 저장
		var fnSave = function() {
			if (fnValidation()) {
				var frmCowInfo = $("form[name='frm_cow_info']");
				var frmSelect = $("form[name='frm_select']");
				frmCowInfo.find("input[name='aucDt']").remove();
				frmCowInfo.append(frmSelect.find("input[name='aucDt']").clone(false));
				frmCowInfo.find("input[name='regType']").val("I");
				
				$.ajax({
					url : '/office/task/saveCowInfo',
					data : JSON.stringify($("form[name='frm_cow_info']").serializeObject()),
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
					modalAlert("", message);
					return;
				});
			}
		};
		
		// 입력 초기화
		var fnReset = function() {
			$("form[name='frm_cow_info']").find("input, select").val("");
			$("form[name='frm_cow_info']").find("select").not("select[name='selStsDsc']").prop("disabled", true);
			$("select").selectric("refresh");
		}
		
		//경매 상태데이터 리로드
		var fnStatusReload = function(data){			
			var success = data.success;
			var message = data.message;
			if (!success) {
				modalAlert("", message, fnReset);
			}
			else {
				var cowInfo = data.cowInfo;
				
				$("input[name='qcnAucObjDsc']", $("form[name='frm_cow_info']")).val(cowInfo.QCN_AUC_OBJ_DSC);
				$("input[name='aucObjDsc']", $("form[name='frm_cow_info']")).val(cowInfo.AUC_OBJ_DSC);
				$("input[name='oslpNo']", $("form[name='frm_cow_info']")).val(cowInfo.OSLP_NO);
				$("input[name='ledSqno']", $("form[name='frm_cow_info']")).val(cowInfo.LED_SQNO);
	
				$("input[name='cowSogWt']", $("form[name='frm_cow_info']")).val(cowInfo.COW_SOG_WT);
				$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val(cowInfo.LVST_AUC_PTC_MN_NO);
				$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val(cowInfo.SRA_SBID_UPR);
				$("input[name='lowsSbidLmtAm']", $("form[name='frm_cow_info']")).val(cowInfo.LOWS_SBID_LMT_AM);
				
				//$("input[name='ledSqno']", $("form[name='frm_cow_info']")).val(cowInfo.LED_SQNO);
				var $selStsDsc = $("select[name='selStsDsc']");							
				$selStsDsc.find("option").each(function() {
					if (cowInfo.SEL_STS_DSC == $(this).val()) $(this).prop("selected", "selected");
				});
				
				if(cowInfo.SEL_STS_DSC == '22'){
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val(cowInfo.LVST_AUC_PTC_MN_NO);
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val(cowInfo.SRA_SBID_UPR);
					
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("disabled", true);
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("disabled", true);
					$("select[name='selStsDsc']", $("form[name='frm_cow_info']")).prop("disabled", true);
				}else{
					$("select[name='selStsDsc']", $("form[name='frm_cow_info']")).prop("disabled", false);
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("disabled", true);
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("disabled", true);
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val('');
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val('');
				}
				
				$("select").selectric("refresh");
			}			
		}
		
		// 저장
		var fnStatusSave = function() {
			if(!fnValidation()) return;
			if($("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("disabled") && $("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("disabled") && $("select[name='selStsDsc']", $("form[name='frm_cow_info']")).prop("disabled")){
				modalAlert('','낙찰상태에서 경매상태를<br/> 변경할수 없습니다.',function(){
					$('#aucPrgSq').focus();
				});
				return;
			}
			
			var lowsSbidLmtAm = $("input[name='lowsSbidLmtAm']", $("form[name='frm_cow_info']")).val();
			var sraSbidUpr = $("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val();
			var selStsDsc = $("select[name='selStsDsc']", $("form[name='frm_cow_info']")).val();
			if(selStsDsc == '22' && (sraSbidUpr == '0' || sraSbidUpr == '') ){
				modalAlert('','낙찰가을 입력하세요.');
				$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).focus();
				return;
			}
			
			var frmCowInfo = $("form[name='frm_cow_info']");
			var frmSelect = $("form[name='frm_select']");
			frmCowInfo.find("input[name='aucDt']").remove();
			frmCowInfo.append(frmSelect.find("input[name='aucDt']").clone(false));
			frmCowInfo.find("input[name='regType']").val("S");
			
			$.ajax({
				url : '/office/task/saveCowInfo',
				data : JSON.stringify($("form[name='frm_cow_info']").serializeObject()),
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
				modalAlert("", message, fnReset);
				return;
			});
		}
		
		// 필수 입력값 체크
		var fnValidation = function() {
			if($("input[name='aucPrgSq']", $("form[name='frm_select']")).val()
			!= $("input[name='aucPrgSq']", $("form[name='frm_cow_info']")).val()) {
				modalAlert("", "경매번호로 조회 후 저장 가능합니다.");
				return false;
			}
			
			var chkList = $(".required").not(":disabled");
			for (var i in chkList) {
				if (chkList[i].value == "") {
					if ($(chkList[i]).prop("tagName") == "INPUT") {
						modalAlert("", $(chkList[i]).attr("alt") + "을 입력하세요.");
					}
					else {
						modalAlert("", $(chkList[i]).attr("alt") + "을 선택하세요.");
					}
					return false;
				}
			}
			
			return true;
		}
		
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
var fnLayerClose = function(className){
	modalPopupClose('.' + className);		
	$("." + className).remove();	
}
