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
//				$(document).on("click", ".inp", function() {
//						$('.btn_input_reset').show();
//					});	
			
			// tab 이동 이벤트
			$(document).on("click", ".tab_list ul.tab_2 > li", function() {
				var tabId = $(this).find('a.act').attr('data-tab-id');
				
				//tab 이동시 새로 고침
//				fnSearchAwlList();
				
				$('dd.awl').hide();
				$('dd.awl#'+tabId).show();
			});
			
			// 중량/예정가 팝업 비고버튼 이벤트
			$(document).on("click", ".winpop_reg .admin_new_reg .tag button", function(e){
				let rmkCntn = $('textarea[name=rmkCntn]').val();
				
				let oriStr = rmkCntn.toString().trim();
				let str = "";
				
				console.log($(this).text());
				
				str = oriStr + (oriStr != '' && oriStr != null ? "," + $(this).text() : $(this).text());
				
				
				if(rmkCntn.includes( $(this).text() )){
					let bok = $(this).text()
					str = str.replace(','+bok,'');
					// return false;
				}
				
				inputValidation(oriStr, str, e);
				
				
				
			});
			// 초기화 이벤트
			$(document).on("click", ".admin_new_reg .btn_input_reset", function(e){
				$(this).siblings("input").val("");			
			});
			$(document).on("click", ".admin_new_reg .btn_tag_reset", function(e){
				$('textarea[name=rmkCntn]').val("");
			});
			
			// 검색어 영역 focus
			$("input[name='searchTxt']").focus();
			
			// 바코드 검색
			$(".btn_search").on("click", function() {
				var regType = $('input[name=regType]').val();
				if (regType == 'AW' || regType == 'AL' || regType == 'AWL') {
					fnSearchAwlList();
				} else {					
					$("form[name='frm']").attr("action", "/office/task/entry").submit();					
				}
			});
			

			// 정보 조회
			$(document).on(clickEvent, ".btn_cow_search", function() {
				
				const regExp = /^[0-9]*$/;
				const aucPrgSq = $("input[name='aucPrgSq2']").val();
				
				if(!regExp.test(aucPrgSq)){
					modalAlert("", '숫자만 입력해 주세요.');
					return false;
				}
				
				if(aucPrgSq ==null || aucPrgSq == '0'){
					modalAlert("", '경매번호를 입력해주세요.');
					return
				}
				$("form[name='frm_cow']").find("input[name='oslpNo']").val(aucPrgSq);
				$("form[name='frm_cow']").find("input[name='aucPrgSq']").val('');
				
				
				$.ajax({
					url: '/office/task/cowInfo',
					data: JSON.stringify($("form[name='frm_cow']").serializeObject()),
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
						fnLWLayerPop(body.params,body.cowInfo);
					}
				});
			});

		// 입력 초기화
		var fnReset = function() {
			$("form[name='frm_cow']").find("input, select").val("");
			$("form[name='frm_cow']").find("select").not("select[name='selStsDsc']").prop("disabled", true);
			$("select").selectric("refresh");
		}
			
			// 검색어 지우기
			$(".btn_input_reset").on("click", function(e) {
				$("input[name='searchTxt']").val("");
				$("form[name='frm']").attr("action", "/office/task/entry").submit();
			});
			
			// 리스트 선택
			$(document).on("dblclick", ".list_body > ul > li", function(e){
				var regType = $("input[name='regType']").val();
				if (regType == "AW" || regType == "AL") return;
				$(".list_body ul li").not(this).removeClass("act");
				$(this).toggleClass("act");
				
				fnModifyPop($(this));
			});
			
			// 리스트 선택
			$(document).on("click", ".list_body > ul > li", function(e){
				var regType = $("input[name='regType']").val();
				if (regType != "SB") return;
				$(".list_body ul li").not(this).removeClass("act");
				$(this).toggleClass("act");
			});
			
			// 유형 선택 화면으로 이동
			$(".btn_back").on("click", function(){
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					aucObjDsc : $("select[name='aucObjDsc']").val()
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
					searchTxt : $("input[name='searchTxt']").val(),
					searchAucObjDsc : $("select#aucObjDsc").val()
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
			
			// 예정가, 계류대, 중량 숫자 입력만 가능하도록
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
				var regTypeNm = "";
				if (regType == "W") {
					regTypeNm = "중량";
				} else if (regType == "N") {
					regTypeNm = "계류대 번호";
				} else if (regType == "LW") {
					regTypeNm = "중량/예정가";
				} else {
					regTypeNm = "예정가";
				}
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
					var success = body.success;
					var message = body.message;
					if(!success) {
						modalAlert("", message);
					}
					else {
						modalAlert("", message, fnReload(body));
					}
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
			
			// 중량 일괄등록
			$(document).on("focusout", "input[name='cowSogWt'].noPop", function(){			
				if(!$(this).val() || $(this).val() == '') $(this).val('0');
				var li = $(this).closest("li");
				var params = {
					regType : $("input[name='regType']").val()
					, naBzplc : $("input[name='naBzplc']").val()
					, aucDt : $("input[name='aucDt']").val()
					, aucObjDsc : '' + li.find("dd.col1").data("aucObjDsc")
					, oslpNo : '' + li.find("dd.col1").data("oslpNo")
					, ledSqno : '' + li.find("dd.col1").data("ledSqno")
					, cowSogWt : $(this).val()
					, firLowsSbidLmtAm : $("input[name='firLowsSbidLmtAm']").val()
					, searchAucObjDsc : $("select#aucObjDsc").val()
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
			
			// 예정가 일괄등록
			$(document).on("focusout", "input[name='firLowsSbidLmtAm'].noPop", function(){			
				if(!$(this).val() || $(this).val() == '') $(this).val('0');
				var li = $(this).closest("li");
				var params = {
					regType : $("input[name='regType']").val()
					, naBzplc : $("input[name='naBzplc']").val()
					, aucDt : $("input[name='aucDt']").val()
					, aucObjDsc : '' + li.find("dd.col1").data("aucObjDsc")
					, oslpNo : '' + li.find("dd.col1").data("oslpNo")
					, ledSqno : '' + li.find("dd.col1").data("ledSqno")
					, cowSogWt : $("input[name='cowSogWt']").val()
					, firLowsSbidLmtAm : $(this).val()
					, searchAucObjDsc : $("select#aucObjDsc").val()
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
				searchTxt : $("input[name='searchTxt']").val(),
				searchAucObjDsc : $("select#aucObjDsc").val()
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
//					fnLayerPop(body.params, body.cowInfo);
					fnLWLayerPop(body.params, body.cowInfo);
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
					if (regType != "AW" && regType != 'AL') {
						listHtml.push('<span><button type="button" class="btn_modify" >수정</button></span>');
					}
					listHtml.push('	<dl>');
					listHtml.push('		<dd class="col1" data-amnno="' + item.SRA_INDV_AMNNO+ '" data-auc-obj-dsc="' + item.AUC_OBJ_DSC+ '" data-oslp-no="' + item.OSLP_NO+ '" data-led-sqno="' + item.LED_SQNO+ '">' + item.AUC_PRG_SQ + '</dd>');
					if (regType == "LW") {
						listHtml.push('<dd class="col2">' + (item.COW_SOG_WT == null || parseInt(item.COW_SOG_WT) == '0' || parseInt(item.COW_SOG_WT) == 'NaN' ? '-' : fnSetComma(parseInt(item.COW_SOG_WT))) + '</dd>');
						listHtml.push('<dd class="col2">' + (item.LOWS_SBID_LMT_UPR == null || parseInt(item.LOWS_SBID_LMT_UPR) == '0' || parseInt(item.LOWS_SBID_LMT_UPR) == 'NaN' ? '-' : fnSetComma(item.LOWS_SBID_LMT_UPR)) + '</dd>');
					}
					else {
						listHtml.push('<dd class="col2" '+(item.MODL_NO != item.AUC_PRG_SQ?'style="color:#ff0000"':'')+'>' + item.MODL_NO + '</dd>');
					}
					listHtml.push('		<dd class="col3">' + item.SRA_INDV_AMNNO_FORMAT + '</dd>');
					listHtml.push('		<dd class="col4">' + item.FTSNM + '</dd>');
					if(regType != "AW" && regType != "AL") {
						listHtml.push('		<dd class="col4 col5"></dd>');
					}
					listHtml.push('	</dl>');
					if(regType == "LW") {
						listHtml.push('		<div class="pd_etc"><p>'+(item.RMK_CNTN ? item.RMK_CNTN:'')+'</p></div>');
					}
					listHtml.push('</li>');
				}
				
				$(".admin_weight_list").find(".list_body > ul").html(listHtml.join(""));
			}
			var li = $(".list_body > ul").find("li#" + aucPrgSq).next() == undefined ? $(".list_body > ul").find("li#" + aucPrgSq) : $(".list_body > ul").find("li#" + aucPrgSq).next();
			$(".list_body > ul").animate({
				scrollTop : $(".list_body > ul").find("li#" + aucPrgSq).offset().top - $(".list_body > ul > li:first").offset().top
			}, 500);
			
			$(".list_body > ul").find("li#" + aucPrgSq).addClass("act");
		};
		
		// 중량/예정가 등록 수정 팝업창
		var fnLWLayerPop = function(params,cowInfo) {
			var title = '예정가/중량등록';
		
			modalPopupClose('.pop_reg');
			$('.pop_reg').remove();
			
			// 예정가 / 중량등록 비고부분 , 자르기
			let smsBufferArray = [];
			if (cowInfo.SMS_BUFFER_2 != null && cowInfo.SMS_BUFFER_2 != '') {
				smsBufferArray = cowInfo.SMS_BUFFER_2.replaceAll(" ","").split(',');
			}

			var sHtml = [];
			
			sHtml.push('<div id="" class="modal-wrap pop_reg">																													');
			sHtml.push('	<div class="modal-content">');
			sHtml.push('		<form name="frm_cow" method="post">');
			sHtml.push('			<input type="hidden" name="regType" value="' + params.regType + '" />');
			sHtml.push('			<input type="hidden" name="aucDt" value="' + params.aucDt + '" />');
			sHtml.push('			<input type="hidden" name="naBzplc" value="' + params.naBzplc + '" />');
			sHtml.push('			<input type="hidden" name="aucObjDsc" value="' + params.aucObjDsc + '" />');
			sHtml.push('			<input type="hidden" name="oslpNo" value="' + params.oslpNo + '" />');
			sHtml.push('			<input type="hidden" name="ledSqno" value="' + params.ledSqno + '" />');
			sHtml.push('			<input type="hidden" name="aucPrgSq" value="' + cowInfo.AUC_PRG_SQ + '" />');
			sHtml.push('			<input type="hidden" name="searchTxt" value="' + params.searchTxt + '" />');
			sHtml.push('			<input type="hidden" name="searchAucObjDsc" value="' + params.searchAucObjDsc + '" />');
			
			sHtml.push('			<div class="winpop winpop_reg">                                                                                                                 ');
			sHtml.push('				<div class="winpop-head ta-C">                                                                                                              ');
			sHtml.push('					<button type="button" class="winpop_close ta-R" onclick="modalPopupClose(\'.pop_reg\');return false;"><span class="sr-only">팝업 닫기</span></button>                                         ');
			sHtml.push('					<h2 class="winpop_tit">중량 / 예정가 등록</h2>                                                                                          ');
			sHtml.push('				</div>                                                                                                                                      ');
			sHtml.push('				<div class="admin_new_reg">                                                                                                                 ');
			sHtml.push('					<div class="cow-basic">                                                                                                                 ');
			sHtml.push('						<table class="table-detail">                                                                                                        ');
			sHtml.push('							<colgroup>                                                                                                                      ');
			sHtml.push('								<col width="31%">                                                                                                           ');
			sHtml.push('								<col>                                                                                                                       ');
			sHtml.push('							</colgroup>                                                                                                                     ');
			sHtml.push('							<tbody>                                                                                                                         ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<th>경매번호</th>                                                                                                       ');
			sHtml.push('										<td class="input-td">');
			sHtml.push('											<ul class="num_scr">');
			sHtml.push('												<li><input type="text" name="aucPrgSq2" class="required onlyNumber" id="aucPrgSq2" alt="경매번호" maxlength="4" pattern="\d*" inputmode="numeric" value="'+ cowInfo.AUC_PRG_SQ +'" /></li>'); 
			sHtml.push('												<li><a href="javascript:;" class="btn_cow_search">조회</a></li>');
			sHtml.push('											</ul>');
			sHtml.push('										</td>');
//			sHtml.push('									<td class="bg-lilac">'+ cowInfo.AUC_PRG_SQ +'</td>                                                                                             ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<th>귀표번호</th>                                                                                                       ');
			sHtml.push('									<td class="bg-lilac">'+ cowInfo.SRA_INDV_AMNNO_FORMAT_FULL +'</td>                                                                                          ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<th>어미/성별</th>                                                                                                      ');
			sHtml.push('									<td class="bg-lilac">'+ cowInfo.MCOW_DSC_NM + ' / ' + cowInfo.INDV_SEX_NAME + '</td>                                                                                     ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<th>중량(Kg)</th>                                                                                                       ');
			sHtml.push('									<td>                                                                                                                    ');
			sHtml.push('										<div class="inp">                                                                                                   ');
			sHtml.push('											<input type="text" name="cowSogWt" class="pd5 required onlyNumber" value="' + ((cowInfo.COW_SOG_WT == null || cowInfo.COW_SOG_WT == '') ? "0" : cowInfo.COW_SOG_WT) + '" maxlength="4" pattern="\d*" inputmode="numeric" />');
			sHtml.push('											<button type="button" class="btn_input_reset"></button>                                                         ');
			sHtml.push('										</div>                                                                                                              ');
			sHtml.push('									</td>                                                                                                                   ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<th>예정가</th>                                                                                                         ');
			sHtml.push('									<td>                                                                                                                    ');
			sHtml.push('										<div class="inp">                                                                                                   ');
			sHtml.push('											<input type="text" name="firLowsSbidLmtAm" class="pd5 required onlyNumber" value="' + ((cowInfo.LOWS_SBID_LMT_AM == null || cowInfo.LOWS_SBID_LMT_AM == 0) ? "" : cowInfo.LOWS_SBID_LMT_AM) + '" maxlength="5" pattern="\d*" inputmode="numeric" />');
			sHtml.push('											<button type="button" class="btn_input_reset"></button>                                                         ');
			sHtml.push('										</div>                                                                                                              ');
			sHtml.push('									</td>                                                                                                                   ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('								<tr>                                                                                                                        ');
			sHtml.push('									<td colspan="2" class="tagBox">                                                                                         ');
			sHtml.push('										<div class="inp">                                                                                                   ');
			sHtml.push('											<textarea name="rmkCntn" class="pd5" style="height:32px;" placeholder="비고" maxlength="30" rows="1">'+(cowInfo.RMK_CNTN == null ? '' : cowInfo.RMK_CNTN)+'</textarea>');
			sHtml.push('											<button type="button" class="btn_tag_reset"></button>                                                               ');
			sHtml.push('										</div>                                                                                                   ');
			sHtml.push('									</td>                                                                                                                   ');
			sHtml.push('								</tr>                                                                                                                       ');
			sHtml.push('							</tbody>                                                                                                                        ');
			sHtml.push('						</table>                                                                                                                            ');
			sHtml.push('					</div>                                                                                                                                  ');
			sHtml.push('					<div class="tag">                                                                                                                       ');
			sHtml.push('						<button type="button">친자일치</button>                                                                                             ');
			sHtml.push('						<button type="button">이모색</button>                                                                                               ');
			sHtml.push('						<button type="button">자연분배</button>                                                                                             ');
			sHtml.push('						<button type="button">임신우</button>                                                                                               ');
			if (smsBufferArray.length > 0) {
				for (var i = 0; i < smsBufferArray.length; i++) {
					sHtml.push('				<button type="button">'+ smsBufferArray[i] +'</button>                                                                                 ');
				}
			}
			sHtml.push('					</div>                                                                                                                                  ');
			sHtml.push('					<div class="top-btns">                                                                                                                  ');
			sHtml.push('						<button type="button" class="btn_save">저장</button>                                                                                                 ');
			sHtml.push('						<button type="button" onclick="modalPopupClose(\'.pop_reg\');return false;">닫기</button>                                             ');
			sHtml.push('					</div>                                                                                                                                  ');
			sHtml.push('				</div>                                                                                                                                      ');
			sHtml.push('			</div>                                                                                                                                          ');
			sHtml.push('		</div>                                                                                                                                              ');
			sHtml.push('	</form>                                                                                                                                              ');
			sHtml.push('</div>                                                                                                                                                  ');
			
			$("body").append(sHtml.join(""));
			modalPopup('.pop_reg');

			$(".pop_reg").find("input.required").focus();
			$(".pop_reg").find("input.required").on('keydown',function(){
				if(!$(this).val() || $(this).val() == '0') $(this).val('');
			});
			$(".pop_reg").find("input.required:not([name=modlNo])").on('focusout',function(){
				if(!$(this).val() || $(this).val() == '') $(this).val('0');
			});
			var len = $(".pop_reg").find("input.required").val()?.length;
			$(".pop_reg").find("input.required")[0]?.setSelectionRange(len,len);
		}
		
		var fnLayerPop = function(params, cowInfo) {
			var title = '';
			switch(params.regType){
				case 'W': title='중량 등록'; break;
				case 'AW': title='중량 등록'; break;
				case 'N': title='계류대 변경'; break;
				case 'SB': title='낙찰결과'; break;
				case 'L': title='예정가등록'; break;
//				case 'LW': title='예정가/중량등록'; break;
			}
			modalPopupClose('.pop_mod_weight');
			$('.pop_mod_weight').remove();
			
			// 예정가 / 중량등록 비고부분 , 자르기
			let smsBufferArray = [];
			if (cowInfo.SMS_BUFFER_2 != null && cowInfo.SMS_BUFFER_2 != '') {
				smsBufferArray = cowInfo.SMS_BUFFER_2.split(',');
			}

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
			sHtml.push('			<input type="hidden" name="searchAucObjDsc" value="' + params.searchAucObjDsc + '" />');
			
			sHtml.push('			<div class="modal-head">');
			sHtml.push('				<h3>' + title + '</h3>');
			sHtml.push('				<button class="modal_popup_close btn_pop_close" type="button">닫기</button>');
			sHtml.push('			</div>');
			// modal-body [s]
			sHtml.push('			<div class="modal-body winpop_reg admin_new_reg">');
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
//			if (['LW'].indexOf(params.regType) > -1) {
//				sHtml.push('					<tr>');
//				sHtml.push('						<th>중량</th>');
//				sHtml.push('						<td class="input-td">');
//				sHtml.push('							<div class="inp">');
//				sHtml.push('								<input type="text" name="cowSogWt" class="pd5 required onlyNumber" value="' + (cowInfo.COW_SOG_WT == null ? "0" : cowInfo.COW_SOG_WT) + '" maxlength="4" pattern="\d*" inputmode="numeric" style="width:70%;" />');
//				sHtml.push('								<button type="button" class="btn_input_reset"></button>');
//				sHtml.push('							</div>');
//				sHtml.push('						</td>');
//				sHtml.push('					</tr>');
//				sHtml.push('					<tr>');
//				sHtml.push('						<th>예정가</th>');
//				sHtml.push('						<td class="input-td">');
//				sHtml.push('							<div class="inp">');
//				sHtml.push('								<input type="text" name="firLowsSbidLmtAm" class="pd5 required onlyNumber" value="' + ((cowInfo.LOWS_SBID_LMT_AM == null || cowInfo.LOWS_SBID_LMT_AM == "0")? "" : cowInfo.LOWS_SBID_LMT_AM) + '" maxlength="5" pattern="\d*" inputmode="numeric" style="width:70%;" />');
//				sHtml.push('								<button type="button" class="btn_input_reset"></button>');
//				sHtml.push('							</div>');
//				sHtml.push('						</td>');
//				sHtml.push('					</tr>');
//				sHtml.push('					<tr>');
//				sHtml.push('						<th>비고</th>');
//				sHtml.push('						<td class="input-td tagBox" colspan="2">');
//				sHtml.push('							<input type="text" name="rmkCntn" class="pd5" alt="비고" placeholder="비고" maxlength="30" value="' + (cowInfo.RMK_CNTN == null ? "" : cowInfo.RMK_CNTN) + '" style="width:100%;" />');
//				sHtml.push('							<button type="button" class="btn_tag_reset"></button>');
//				sHtml.push('						</td>');
//				sHtml.push('					</tr>');
//				sHtml.push('					<tr>');
//				sHtml.push('						<td class="input-td" id="input-etc" colspan="2">');
//													if (smsBufferArray.length > 0) {
//														for (var i = 0; i < smsBufferArray.length; i++) {
//				sHtml.push('						<a href="javascript:;" id="'+ smsBufferArray[i] +'">'+ smsBufferArray[i] +'</a>');
//														}
//													}
//				sHtml.push('						</td>');
//				sHtml.push('					</tr>');
//			}
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
				sHtml.push('						</td>');
				sHtml.push('					</tr>');
			}
			else if (['L', 'AL'].indexOf(params.regType) > -1) {
				sHtml.push('					<tr>');
				sHtml.push('						<th>산차</th>');
				sHtml.push('						<td>' + cowInfo.MATIME + '</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>중량</th>');
				sHtml.push('						<td>' + fnSetComma(cowInfo.COW_SOG_WT) + ' kg</td>');
				sHtml.push('					</tr>');
				sHtml.push('					<tr>');
				sHtml.push('						<th>예정가</th>');
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
				sHtml.push('							<input type="text" name="modlNo" class="pd5 required onlyNumber" value="" maxlength="4" pattern="\d*" inputmode="numeric" style="width:70%;" />');
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
			if (params.regType != 'SB'){
				sHtml.push('						<a href="javascript:;" class="btn_save">저장</a>');
			}
			sHtml.push('						<a href="javascript:;" class="btn_pop_close">닫기</a>');
			sHtml.push('				</div>');
			sHtml.push('			</div>');
			sHtml.push('		</form>')
			sHtml.push('	</div>');
			sHtml.push('</div>');
			
			$("body").append(sHtml.join(""));
			modalPopup('.pop_mod_weight');
			if (params.regType != 'SB') {
				$(".pop_mod_weight").find("input.required").focus();
				$(".pop_mod_weight").find("input.required").on('keydown',function(){
					if(!$(this).val() || $(this).val() == '0') $(this).val('');
				});
				$(".pop_mod_weight").find("input.required:not([name=modlNo])").on('focusout',function(){
					if(!$(this).val() || $(this).val() == '') $(this).val('0');
				});
				var len = $(".pop_mod_weight").find("input.required").val().length;
				$(".pop_mod_weight").find("input.required")[0].setSelectionRange(len,len);
			}
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
		
		// 초기 탭 설정
		var setLayout = function(){
			var tabId = $('div.tab_list li a.act').attr('data-tab-id');
			$('dd.awl#'+tabId).show();
		};
		
		// maxlength & 중복 체크
		var inputValidation = function(oriStr, str, e){
			e.preventDefault();
			
			if ((oriStr == undefined && oriStr == '') || (str == undefined && str == '')) {
				return;
			}
			
			console.log("str" + str)
			console.log("oriStr" + oriStr)
			
						
			if (str.length > 30) {
				modalAlert("","30자 이상 등록할 수 없습니다.");
				return;
			}
			
			$('textarea[name=rmkCntn]').val(str);
			
			$(".pop_reg ").find("textarea").focus();
			var len = $(".pop_reg ").find("textarea").val().length;
			$(".pop_reg ").find("textarea")[0].setSelectionRange(len,len);
		}
		
		// 일괄 등록 조회
		var fnSearchAwlList = function(){
			var params = {
				searchDate : $("input[name='aucDt']").val(),
				regType : $('div.tab_list li a.act').attr('data-tab-id'),
				searchTxt : $("input[name='searchTxt']").val(),
				searchAucObjDsc : $("select#aucObjDsc").val()
			}

			$.ajax({
				url : '/office/task/entryInfo',
				data : JSON.stringify(params),
				type : 'POST',
				dataType : 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				success : function(data) {
					$('div.list_table').empty();

					sHtml = [];
					
					sHtml.push('		<div class="list_head">');
					sHtml.push('			<dl>');
					sHtml.push('				<dd class="col1">번호</dd>');
					sHtml.push('				<dd class="col2 awl" id="AW" style="display: none;"><span class="txt_org">중량</span></dd>');
					sHtml.push('				<dd class="col2 awl" id="AL" style="display: none;"><span class="txt_org">예정가</span></dd>');
					sHtml.push('				<dd class="col3">귀표</dd>');
					sHtml.push('				<dd class="col4">출하자</dd>');
					sHtml.push('			</dl>');
					sHtml.push('		</div>');

					sHtml.push('		<div class="list_body">');
					sHtml.push('			<ul style="overflow-y:scroll;">');
					if (data.entryList.length <= 0) {
						sHtml.push('					<li>');
						sHtml.push('						<dl>');
						sHtml.push('							<dd>검색결과가 없습니다.</dd>');
						sHtml.push('						</dl>');
						sHtml.push('					</li>');													
					} else {
						for(var item of data.entryList) {
							var cowSogWt = (item.COW_SOG_WT != null && item.COW_SOG_WT != 0) ? item.COW_SOG_WT.split(".")[0] : "";  
							var lowSbidUpr = (item.LOWS_SBID_LMT_UPR != null && item.LOWS_SBID_LMT_UPR != 0) ? item.LOWS_SBID_LMT_UPR : "";  
							
							sHtml.push('				<li id="'+ (item.AUC_PRG_SQ ? item.AUC_PRG_SQ : "") + '">');
							sHtml.push('					<dl>');
							sHtml.push('						<dd class="col1" data-amnno="'+ item.SRA_INDV_AMNNO +'" data-auc-obj-dsc="'+ item.AUC_OBJ_DSC +'" data-oslp-no="'+ item.OSLP_NO +'" data-led-sqno="'+ item.LED_SQNO +'">'+ item.AUC_PRG_SQ +'</dd>');
							sHtml.push('						<dd class="col2 awl" id="AW" style="display: none;">');
							sHtml.push('							<input type="text" name="cowSogWt" id="cowSogWt' + item.AUC_PRG_SQ +'" class="onlyNumber noPop" value="'+ cowSogWt +'" maxlength="4" pattern="\d*" inputmode="numeric"/>');
							sHtml.push('						</dd>');
							sHtml.push('						<dd class="col2 awl" id="AL" style="display: none;">');
							sHtml.push('							<input type="text" name="firLowsSbidLmtAm" id="firLowsSbidLmtAm'+ item.AUC_PRG_SQ +'" class="onlyNumber noPop" value="'+ lowSbidUpr +'" maxlength="5" pattern="\d*" inputmode="numeric"/>');
							sHtml.push('						</dd>');
							sHtml.push('						<dd class="col3">'+ item.SRA_INDV_AMNNO_FORMAT +'</dd>');
							sHtml.push('						<dd class="col4">'+ (item.FTSNM ? item.FTSNM : "") +'</dd>');
							sHtml.push('					</dl>');
							sHtml.push('				</li>');
						}
					}
					sHtml.push('		</ul>');
					sHtml.push('	</div>');
					
					$('div.list_table').append(sHtml.join(""));
					
					setLayout();
				},
				error: function(xhr, status, error) {
					modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				}
			})
		}

		return {
			// public functions
			init: function () {
				addEvent();
				setLayout();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
