;(function (win, $, COMMONS) {
// Class definition
	var Entry = function () {
		var clickEvent = (function() {
//			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
//				return 'touchstart';
//			}
//			else {
				return 'click';
//			}
		})();
		
		var addEvent = function(){
			
			// 검색 버튼
			$(".btn_search").on(clickEvent, function() {
				$("form[name='frm']").attr("action", "/office/task/cowList").submit();
			});
			
			// li 클릭시 체크박스 선택/해제
			$(".list_body li").on(clickEvent, function() {
				$(this).find("input:checkbox").prop("checked", !$(this).find("input:checkbox").is(":checked")).trigger("change");
			});
			
			// 낙찰 변경
			$(".btn_chg_result").on(clickEvent, function() {
				fnSearchSogCow();
			});
			
			// 출장우 수정
			$(".btn_modify").on(clickEvent, function() {
				fnModify();
			});
			
			// 신규등록
			$(".btn_regist").on(clickEvent, function() {
				$("form[name='frm']").attr("action", "/office/task/searchIndv").submit();
			});
			
			// 작업 선택 화면으로 이동
			$(".btn_back").on(clickEvent, function(){
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("select[name='aucObjDsc']:selected").val()
				};
				appendFormSubmit("frm_main", "/office/task/select", params);
			});
			
			// 출장우 선택
			$("input[name='oslpNo']").on("change", function() {
				$("input[name='oslpNo']").not($(this)).prop("checked", false);
			});
			
			// 이미지 업로드 페이지 이동
			$(".btn_image").on(clickEvent, function(){
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $(this).data("aucObjDsc").toString(),
					oslpNo : $(this).data("oslpNo").toString(),
					sraIndvAmnno : $(this).data("amnno").toString()
				};
				appendFormSubmit("frm_main", "/office/task/uploadImage", params);
			});
			
			/******************************************** 낙찰변경 팝업 페이지 이벤트 [s] ********************************************/
			// 경매상태 변경 이벤트
			$(document).on("change", "select[name=selStsDsc]", function() {
				if($(this).val() == '22'){
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("readonly", false).focus()
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).prop("readonly", false);
					$("input[name='sraMwmnnm']", $("form[name='frm_cow_info']")).prop("readonly", false);
					$("input[name='cusRlno']", $("form[name='frm_cow_info']")).prop("readonly", false);
					$("input[name='entryYn']", $("form[name='frm_cow_info']")).prop("disabled", false);
				}
				else{
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
					$("input[name='sraSbidUpr']", $("form[name='frm_cow_info']")).val('0').prop("readonly", true);
					$("input[name='sraMwmnnm']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
					$("input[name='cusRlno']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
					$("input[name='entryYn']", $("form[name='frm_cow_info']")).prop("disabled", true);
					
				}
			});
			
			// 저장버튼 클릭 이벤트
			$(document).on(clickEvent, ".btn_save", function() {
				fnSave();
			});
			
			// 참가번호 여부 체크 이벤트
			$(document).on("change", "input[name='entryYn']", function() {
				$(".toggle_hide").removeClass("hide");
				if ($(this).is(":checked")) {
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
					$("input[name='sraMwmnnm']", $("form[name='frm_cow_info']")).prop("readonly", false);
					$("input[name='cusRlno']", $("form[name='frm_cow_info']")).prop("readonly", false);
				}
				else {
					$("input[name='lvstAucPtcMnNo']", $("form[name='frm_cow_info']")).prop("readonly", false);
					$("input[name='sraMwmnnm']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
					$("input[name='cusRlno']", $("form[name='frm_cow_info']")).val('').prop("readonly", true);
				}
			});
			
			// 예정가(단가) 변경시 실제 원단위 금액으로 환산
			$(document).on("change", "input[name='lowsSbidLmtUpr']", function() {
				fnSetLowsSbidLmtAm();
			});
			/******************************************** 낙찰변경 팝업 페이지 이벤트 [e] ********************************************/
		};
		
		// 출장우 수정 페이지 이동
		var fnModify = function() {
			if ($("input[name='oslpNo']").filter(":checked").length < 1) {
				modalAlert("", "변경할 출장우를 선택하세요.");
				return ;
			}
			var params = {
				aucDt : $("input[name='aucDt']").val(),
				aucObjDsc : $("input[name='oslpNo']").filter(":checked").data("aucObjDsc").toString(),
				oslpNo : $("input[name='oslpNo']").filter(":checked").val(),
				sraIndvAmnno : $("input[name='oslpNo']").filter(":checked").data("amnno").toString()
			};
			appendFormSubmit("frm_main", "/office/task/registCow", params);
		};
		
		// 경매 내역 조회
		var fnSearchSogCow = function() {
			if ($("input[name='oslpNo']").filter(":checked").length < 1) {
				modalAlert("", "변경할 출장우를 선택하세요.");
				return;
			}
			
			var params = {
				aucDt : $("input[name='aucDt']").val(),
				aucObjDsc : $("input[name='oslpNo']").filter(":checked").data("aucObjDsc").toString(),
				oslpNo : $("input[name='oslpNo']").filter(":checked").val(),
				sraIndvAmnno : $("input[name='oslpNo']").filter(":checked").data("amnno").toString(),
				ledSqno : $("input[name='oslpNo']").filter(":checked").data("ledSqno").toString()
			}
			
			$.ajax({
				url : '/office/task/searchSogCowAjax',
				data : JSON.stringify(params),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				}
			}).done(function (body) {
				if (body && body.success) {
					fnLayerPop(body);
				}
				else {
					modalAlert("", body.message);
					return;
				}
			});
		};
		
		// 경매내역 팝업 오픈
		var fnLayerPop = function(data) {
			modalPopupClose(".pop_cow_input");
			var popHtml = [];
			popHtml.push('<div id="chg_result" class="modal-wrap pop_cow_input">');
			popHtml.push('	<form name="frm_cow_info" method="post" autocomplete="off">');
			popHtml.push('		<input type="hidden" name="naBzPlc" value="" />');
			popHtml.push('		<input type="hidden" name="aucDt" value="" />');
			popHtml.push('		<input type="hidden" name="aucObjDsc" value="" />');
			popHtml.push('		<input type="hidden" name="qcnAucObjDsc" value="" />');
			popHtml.push('		<input type="hidden" name="aucPrgSq" value="" />');
			popHtml.push('		<input type="hidden" name="oslpNo" value="" />');
			popHtml.push('		<input type="hidden" name="ledSqno" value="" />');
			popHtml.push('		<input type="hidden" name="sraIndvAmnno" value="" />');
			popHtml.push('		<input type="hidden" name="divisionPrice" value="" />');
			popHtml.push('		<input type="hidden" name="baseLmtAmOri" value="" />');
			popHtml.push('		<input type="hidden" name="chg_pgid" value="cowList" />');
			popHtml.push('		<input type="hidden" name="chg_rmk_cntn" value="출장우조회 낙찰변경" />');
			popHtml.push('		<input type="hidden" name="lowsSbidLmtAm" value="" />');
			popHtml.push('		<div class="modal-content pop_ad_mod">');
			popHtml.push('			<div class="modal-head">');
			popHtml.push('				<h3>경매내역 변경</h3>');
			popHtml.push('				<button type="button" class="modal_popup_close" onclick="javascript:fnLayerClose(\'pop_cow_input\'); return false;">닫기</button>');
			popHtml.push('			</div>');
			popHtml.push('			<div class="modal-body">');
			popHtml.push('				<div class="table-mod">');
			popHtml.push('					<table>');
			popHtml.push('						<colgroup>');
			popHtml.push('							<col class="col1" style="width:25% !important;" />');
			popHtml.push('							<col class="col1" style="width:25% !important;" />');
			popHtml.push('							<col class="col1" style="width:25% !important;" />');
			popHtml.push('							<col class="col1" style="width:25% !important;" />');
			popHtml.push('						</colgroup>');
			popHtml.push('						<tr>');
			popHtml.push('							<th>번호</th>');
			popHtml.push('							<td class="input-td aucPrgSqTxt" style="text-align: center;"></td>');
			popHtml.push('							<th>귀표번호</th>');
			popHtml.push('							<td class="input-td sraIndvAmnnoTxt" style="text-align: center;"></td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr>');
			popHtml.push('							<th>예정가</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<input type="text" name="lowsSbidLmtUpr" value="" maxlength="5" pattern="\d*" inputmode="numeric" placeholder="예정가" />');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr>');
			popHtml.push('							<th>경매상태</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<select name="selStsDsc" class="required" alt="경매상태">');
			popHtml.push('									<option value="">선택</option>');
			popHtml.push('									<option value="11">송장등록</option>');
			popHtml.push('									<option value="21">경매</option>');
			popHtml.push('									<option value="22">낙찰</option>');
			popHtml.push('									<option value="23">유찰</option>');
			popHtml.push('								</select>');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr>');
			popHtml.push('							<th>참가번호</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<input type="text" name="lvstAucPtcMnNo" value="" maxlength="5" pattern="\d*" inputmode="numeric" style="width:70%;" placeholder="참가번호"/>');
			popHtml.push('								<input type="checkbox" name="entryYn" id="entryYn" value="1" />');
			popHtml.push('								<label for="entryYn" class="entryYnTxt">없음</label>');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr>');
			popHtml.push('							<th>낙찰단가</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<input type="text" name="sraSbidUpr" value="" maxlength="5" pattern="\d*" inputmode="numeric" placeholder="낙찰단가" />');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr class="toggle_hide hide">');
			popHtml.push('							<th>낙찰자명</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<input type="text" name="sraMwmnnm" value="" maxlength="20" placeholder="낙찰자명"/>');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('						<tr class="toggle_hide hide">');
			popHtml.push('							<th>낙찰자 생년월일</th>');
			popHtml.push('							<td colspan="3" class="input-td">');
			popHtml.push('								<input type="text" name="cusRlno" value="" maxlength="10" pattern="\d*" inputmode="numeric" placeholder="생년월일/사업자번호" />');
			popHtml.push('							</td>');
			popHtml.push('						</tr>');
			popHtml.push('					</table>');
			popHtml.push('				</div>');
			popHtml.push('			</div>');
			popHtml.push('			<div class="modal-foot">');
			popHtml.push('				<div class="btn_area">');
			popHtml.push('					<a href="javascript:;" class="btn_reset">초기화</a>');
			popHtml.push('					<a href="javascript:;" class="btn_save">저장</a>');
			popHtml.push('					<a href="javascript:fnLayerClose(\'pop_cow_input\');">종료</a>');
			popHtml.push('				</div>');
			popHtml.push('			</div>');
		if (data.sogCowLogList && data.sogCowLogList.length > 0) {
			popHtml.push('			<div class="list_txt"><p>▶ 최근 변경 이력(3건)</p></div>');
			popHtml.push('				<div class="list_table">                           ');
			popHtml.push('					<div class="list_head">                        ');
			popHtml.push('						<dl>                                       ');
			popHtml.push('							<dd class="col1">상태</dd>             ');
			popHtml.push('							<dd class="col1">낙찰단가</dd>         ');
			popHtml.push('							<dd class="col2">참가번호</dd>         ');
			popHtml.push('							<dd class="col3">담당</dd>             ');
			popHtml.push('							<dd class="col1">시간</dd>             ');
			popHtml.push('						</dl>                                      ');
			popHtml.push('					</div>                                         ');
			popHtml.push('					<div class="list_body">                        ');
			popHtml.push('						<ul class="sog_cow_log" style="overflow-y:scroll;">');
			for (var i = 0; i < data.sogCowLogList.length; i++) {
				popHtml.push('							<li>                                   ');
				popHtml.push('								<dl>                               ');
				popHtml.push('									<dd class="col1 selStsDscNm"></dd>   ');
				popHtml.push('									<dd class="col1 sraSbidUpr"></dd>    ');
				popHtml.push('									<dd class="col2 lvstAucPtcMnNo"></dd> ');
				popHtml.push('									<dd class="col3 usrnm"></dd>        ');
				popHtml.push('									<dd class="col1 chgHm"></dd>  ');
				popHtml.push('								</dl>                              ');
				popHtml.push('							</li>                                  ');
			}
			popHtml.push('						</ul>                                      ');
			popHtml.push('					</div>                                         ');
			popHtml.push('				</div>                                             ');
			popHtml.push('			</div>');
		}
			popHtml.push('		</div>');
			popHtml.push('	</form>');
			popHtml.push('</div>');
			
			$("body").append(popHtml.join(""));
			modalPopup(".pop_cow_input");

			fnSetValue(data);
		};
		
		// 변경 대상 상세정보 세팅
		var fnSetValue = function(data) {
			var qcnInfo = data.qcnInfo[0];
			var cowInfo = data.sogCowInfo;
			var logList = data.sogCowLogList;
			
			var frmCowInfo = $("form[name='frm_cow_info']");
			$("input[name='naBzPlc']", frmCowInfo).val(cowInfo.NA_BZPLC);
			$("input[name='aucDt']", frmCowInfo).val(cowInfo.AUC_DT);
			$("input[name='aucObjDsc']", frmCowInfo).val(cowInfo.AUC_OBJ_DSC);
			$("input[name='qcnAucObjDsc']", frmCowInfo).val(qcnInfo.AUC_OBJ_DSC);
			$("input[name='aucPrgSq']", frmCowInfo).val(cowInfo.AUC_PRG_SQ);
			$("input[name='oslpNo']", frmCowInfo).val(cowInfo.OSLP_NO);
			$("input[name='ledSqno']", frmCowInfo).val(cowInfo.LED_SQNO);
			$("input[name='sraIndvAmnno']", frmCowInfo).val(cowInfo.SRA_INDV_AMNNO);
			$("input[name='lowsSbidLmtUpr']", frmCowInfo).val(cowInfo.LOWS_SBID_LMT_UPR);
			$("input[name='lowsSbidLmtAm']", frmCowInfo).val(cowInfo.LOWS_SBID_LMT_AM);
			$("select[name='selStsDsc']", frmCowInfo).val(cowInfo.SEL_STS_DSC).trigger("change");
			$("input[name='lvstAucPtcMnNo']", frmCowInfo).val(cowInfo.LVST_AUC_PTC_MN_NO);
			$("input[name='sraSbidUpr']", frmCowInfo).val(cowInfo.SRA_SBID_UPR);
			$("input[name='divisionPrice']", frmCowInfo).val(qcnInfo["DIVISION_PRICE" + cowInfo.AUC_OBJ_DSC]);
			$("input[name='baseLmtAmOri']", frmCowInfo).val(qcnInfo.BASE_LMT_AM_ORI);
			
			$(".aucPrgSqTxt").text(cowInfo.AUC_PRG_SQ);
			$(".sraIndvAmnnoTxt").text(cowInfo.SRA_INDV_AMNNO.substring(10, 14) + "-" + cowInfo.SRA_INDV_AMNNO.substring(14, 15));
			
			$(logList).each(function(i, log){
				var ul = $("ul.sog_cow_log > li").eq(i);
				$("dd.selStsDscNm", ul).text(getStringValue(log.SEL_STS_DSC_NM));
				$("dd.sraSbidUpr", ul).text(getStringValue(log.SRA_SBID_UPR));
				$("dd.lvstAucPtcMnNo", ul).text(getStringValue(log.LVST_AUC_PTC_MN_NO));
				$("dd.usrnm", ul).text(getStringValue(log.USRNM));
				$("dd.chgHm", ul).text(getStringValue(log.CHG_HM));
			});
			
			$("select").trigger("change");
			$("select").selectric("refresh");
		};
		
		// 경매내역 변경 이벤트
		var fnSave = function() {
			if(fnSaveValidate()) {
				$.ajax({
					url : '/office/task/updateResultAjax',
					data : JSON.stringify($("form[name='frm_cow_info']").serializeObject()),
					type : 'POST',
					dataType : 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					}
				}).done(function (body) {
					if (body && body.success) {
						modalAlert("", body.message, function(){fnLayerClose("pop_cow_input");});
					}
					else {
						modalAlert("", body.message);
						return;
					}
				});
			}
		};
		
		// 경매내역 변경시 입력 값 체크
		var fnSaveValidate = function() {
			// 경매 상태가 낙찰인 경우 참가번호, 낙찰단가 수정
			var selStsDsc = $("select[name='selStsDsc']").val();
			var frmCowInfo = $("form[name='frm_cow_info']");
			var divisionPrice = $("input[name='divisionPrice']", frmCowInfo);		// 응찰단위금액
			var lvstAucPtcMnNo = $("input[name='lvstAucPtcMnNo']", frmCowInfo);		// 참가번호
			var sraMwmnnm = $("input[name='sraMwmnnm']", frmCowInfo);				// 낙찰자명
			var cusRlno = $("input[name='cusRlno']", frmCowInfo);					// 낙찰자 생년월일/사업자번호
			var sraSbidUpr = $("input[name='sraSbidUpr']", frmCowInfo);				// 낙찰단가
			var lowsSbidLmtUpr = $("input[name='lowsSbidLmtUpr']", frmCowInfo);		// 예정가
			var lowsSbidLmtAm = $("input[name='lowsSbidLmtAm']", frmCowInfo);		// 예정가(원 단위)
			var baseLmtAmOri = $("input[name='baseLmtAmOri']", frmCowInfo);			// 최고응찰한도금액

			if (selStsDsc == "22") {
				if ($("input[name='entryYn']").is(":checked")) {
					// 낙찰자명 입력 체크
					if(sraMwmnnm.val() == "") {
						modalAlert("", "낙찰자명을 입력하세요.", function(){sraMwmnnm.focus()});
						return false;
					}
					// 낙찰자 생년월일/사업자번호 입력 체크
					if(cusRlno.val() == "") {
						modalAlert("", "낙찰자 생년월일/사업자번호를 입력하세요.", function(){cusRlno.focus()});
						return false;
					}
				}
				else {
					// 참가번호 입력 체크
					if(lvstAucPtcMnNo.val() == "") {
						modalAlert("", "낙찰자 참가번호를 입력하세요.", function(){lvstAucPtcMnNo.focus()});
						return false;
					}
				}
				// 응찰 예정가 입력 체크
				if(lowsSbidLmtUpr.val() == "" || parseInt(lowsSbidLmtUpr.val()) <= 0) {
					modalAlert("", "예정가를 입력하세요.", function(){sraSbidUpr.focus()});
					return false;
				}
				// 낙찰 단가 입력 체크
				if(sraSbidUpr.val() == "" || parseInt(sraSbidUpr.val()) <= 0) {
					modalAlert("", "낙찰 단가를 입력하세요.", function(){sraSbidUpr.focus()});
					return false;
				}
				// 낙찰 단가 예정가 비교 
				if(parseInt(sraSbidUpr.val()) < parseInt(lowsSbidLmtUpr.val())) {
					modalAlert("", "낙찰 단가가 예정가보다 작습니다.", function(){sraSbidUpr.focus()});
					return false;
				}
				// 낙찰 단가 응찰한도금액 비교
				if(parseInt(sraSbidUpr.val() * divisionPrice.val()) > parseInt(baseLmtAmOri.val())) {
					modalAlert("", "낙찰 금액이 최고 응찰 한도금액을 <br/>초과 하였습니다.", function(){sraSbidUpr.focus()});
					return false;
				}
			}
			lowsSbidLmtAm.val(parseInt(getStringValue(lowsSbidLmtUpr.val(), "0")) * parseInt(getStringValue(divisionPrice.val())));
			
			return true;
		};
		
		// 응찰 예정가 변경시 실제 원단위 환산 입력
		var fnSetLowsSbidLmtAm = function() {
			var frmCowInfo = $("form[name='frm_cow_info']");
			var lowsSbidLmtUpr = $("input[name='lowsSbidLmtUpr']", frmCowInfo);		// 예정가(단가)
			var lowsSbidLmtAm = $("input[name='lowsSbidLmtAm']", frmCowInfo);		// 예정가(원 단위)
			var divisionPrice = $("input[name='divisionPrice']", frmCowInfo);		// 응찰단위금액
			lowsSbidLmtAm.val(parseInt(getStringValue(lowsSbidLmtUpr.val(), "0")) * parseInt(getStringValue(divisionPrice.val())));
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

var fnLayerClose = function(className){
	modalPopupClose('.' + className);
	$("." + className).remove();
};

