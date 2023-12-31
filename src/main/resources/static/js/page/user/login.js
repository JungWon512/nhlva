;(function (win, $,COMMONS) {
// Class definition
	var Login = function () {	
		if($('input[name=type]').val() =='0' && !isIos()){
		//if(($('input[name=type]').val() =='0' || !isIos()) && $('input[name=place]').val() == '114'){
			$('div.sns_login').show();
		}
		var get_msg = function(message) {
			if(message!=undefined){
				alert(message);
				//Swal.fire("Login", message, "error");
			}
		};

		var addEvent = function(){
			
			// 전체동의 클릭시, [필수]해제
			$(document).find('ul.agree_list li:not(:first-child) input[type=checkbox]').click(function(){
				var chkekedLen = $('ul.agree_list li:not(:first-child) input[type=checkbox]:checked').length;
				var allChkLen = $('ul.agree_list li:not(:first-child) input[type=checkbox]').length;
				
				if(allChkLen > chkekedLen){
					$('ul.agree_list li:first-child input[type=checkbox]').attr('checked',false);
				}else if(allChkLen == chkekedLen){
					$('ul.agree_list li:first-child input[type=checkbox]').attr('checked',true);
				}
			});
			
			$(document).on('keydown', "input[name=userName],input[name=birthDate]",function(e){
				if(e.keyCode == '13') $('.btn_login').click();
			});
			
			// 로그인 버튼
			$(document).on('click', ".btn_login",function(){
				fnLoginProc();
			});
			
			// 시스템 이용약관 동의 팝업
			$(document).on('click', ".btn_agreement_pop",function() {
				modalPopup('.pop_privacy');
			});
			
			// 개인정보 이용약관 동의 팝업
			$(document).on('click', ".btn_privacy_pop",function() {
				modalPopup('.pop_agreement');
			});
			
			// SMS 재발송
			$(".btn_resend").click(function() {
				fnSmsResend();
			});
			
			// 인증번호 확인
			$(".btn_confirm").click(function() {
				fnConfirm();
			});
			
			// 취소
			$(".btn_cancel").click(function() {
				$("form[name='frm_auth']").find("input").val("");
				$("#login_info").show();
				$("#login_sms_auth").hide();
			});
			
			//전체 동의 클릭 
			$("#agree_chkAll").click(function(){
				if($(this).prop("checked")){
					$(".agree_chk").prop("checked", true);
				}else{
					$(".agree_chk").prop("checked", false);
				}
				
			});
		};
		
		
		// 로그인 처리
		var fnLoginProc = function() {
			if ($("input#userName").val() == "") {
				modalAlert('', "이름을 입력하세요."
					 , function(){$("input#userName").focus();}
				);
				return;
			}
			
			if ($("input#password").val() == "") {
				modalAlert('', $("input#password").attr("placeHolder") + "을(를) 입력하세요."
					 , function(){$("input#password").focus();}
				);
				return;
			}
			
			if (!$("input#agree_chk1").is(":checked")) {
				modalAlert('', "개인정보 이용약관에 동의하세요.");
				return;
			}

			if (!$("input#agree_chk2").is(":checked")) {
				modalAlert('', "가축시장 시스템 이용약관에 동의하세요.");
				return;
			}
			
			if (!$("input#agree_chk3").is(":checked")) {
				modalAlert('', "가축시장 제3자 정보제공에 동의하세요.");
				return;
			}
			
			var reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi;
			$.ajax({
				url: '/user/loginProc',
				data: JSON.stringify({
					"place" : $("input[name='place']").val(),
					"type" : $("input[name='type']").val(),
					"userName" : $("input[name='userName']").val(),
					"birthDate" : $("input#password").val(),
					"telNo" : $("input#password").val().replace(reg, ""),
//						"userName" : hex_sha512($("input[name='userName']").val()).toString(),
//						"birthDate" : hex_sha512($("input#password").val()).toString(),
//						"telNo" : hex_sha512($("input#password").val().replace(reg, "")).toString()
					"connChannel" : connChannel()
//					,"returnUrl" : $("input[name='rtnUrl']").val()
				}),
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
					modalAlert('', message);
				}
				else {
					if (body.branchInfo && body.branchInfo.SMS_AUTH_YN == '1') {	/*&& $("input[name='type']").val() == '0' */
						$("#login_info").hide();
						$("#login_sms_auth").show();
						$("input[name='naBzplc']", $("form[name='frm_auth']")).val(body.branchInfo.NA_BZPLC);
						$("input[name='token']", $("form[name='frm_auth']")).val(body.token);
						$("input[name='connChannel']", $("form[name='frm_auth']")).val(connChannel());
						if(body.alreadyFlag != undefined) modalAlert('', message);
						if (body.smsNo != undefined) $("input#authNumber").val(body.smsNo);
					}
					else {
						var uri = body.returnUrl == null ? '/main' : body.returnUrl;
						sendUserInfo(body.access_token, body.info, body.branchInfo, uri);
					}
				}
			});
		}
		
		// SMS 재발송
		var fnSmsResend = function() {
			$.ajax({
				url: '/user/resendSms',
				data: JSON.stringify($("form[name='frm_auth']").serializeObject()),
				type: 'POST',
				dataType: 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				}
			}).done(function (body) {
				var message = body.message;
				modalAlert('', message);
			});
		}
		
		// 인증번호 확인
		var fnConfirm = function() {

			if ($("input[name='authNumber']").val() == "") {
				modalAlert('', $("input#authNumber").attr("placeHolder") + "을(를) 입력하세요."
					 , function(){$("input#authNumber").focus();}
				);
				return;
			}

			$.ajax({
				url: '/user/loginAuthProc',
				data: JSON.stringify($("form[name='frm_auth']").serializeObject()),
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
					modalAlert('', message);
				}
				else {
					var uri = body.returnUrl == null ? '/main' : body.returnUrl;
					sendUserInfo(body.access_token, body.info, body.branchInfo, uri);
				}
			});
		}
		
		// app에 로그인 정보 전송
		var sendUserInfo = function(token, info, branchInfo, uri) {
			try {
				var userInfo = {
					userToken : token,
					auctionCode : info.auctionHouseCode,
					auctionCodeName : branchInfo.CLNTNM,
					userName : info.userName,
					userNum : info.userMemNum,
					nearestBranch : getCookie("naBzplc")
				};

				// 안드로이드
				if (window.auctionBridge) {
					window.auctionBridge.setUserInfo(JSON.stringify(userInfo));
				}
				// 아이폰
				else if(isIos()) {
					webkit.messageHandlers.setUserInfo.postMessage(JSON.stringify(userInfo));
				}
				pageMove(uri);
			}
			catch(e) {
				debugConsole(e);
			}
		}
		return {
			// public functions
			init: function () {
				addEvent();
				get_msg();
			}
			,sendUserInfo : sendUserInfo
		};
	}();

	jQuery(document).ready(function () {
		Login.init();
		
	    var namespace = win.auction;
	    var __COMPONENT_NAME = "LOGIN";
    	namespace[__COMPONENT_NAME] = Login;
		if($('input[name=kkoLoginResult]').val()){
			modalAlert('', $('input[name=kkoLoginResultMsg]').val());
		}
	});
})(window, window.jQuery);
function loginWithKakao() {
	var url = 'https://kauth.kakao.com/oauth/authorize?';
	url+='client_id='+kko_api_id;
	url+='&redirect_uri='+kko_redirect_url;
	url+='&response_type=code';
	url+='&state='+location.search.substr(1);
	if(!getCookie('nhlva_k_r_token')){
		url+='&prompt=login';		
	}
	location.href=url;
  }
  
function redirectKaKaoLogin(param){	
	console.log(param);
	var reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi;
	$.ajax({
		url: '/user/kkologinProc',
		data: JSON.stringify(param),
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
			modalAlert('', message);
		}
		else {
			var uri = body.returnUrl == null ? '/main' : body.returnUrl;
			window.auction.LOGIN.sendUserInfo(body.access_token, body.info, body.branchInfo, uri);
		}
	});
	
};
  
  