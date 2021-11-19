;(function (win, $,COMMONS) {
// Class definition
	var Login = function () {
		var get_msg = function(message) {
			if(message!=undefined){
				alert(message);
				//Swal.fire("Login", message, "error");
			}
		};

		var addEvent = function(){
			$(document).on('keydown', "input[name=userName],input[name=birthDate]",function(e){
				if(e.keyCode == '13') $('.btn_login').click();
			});
			// 로그인 버튼
			$(document).on('click', ".btn_login",function(){
				
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
				
				var reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi;
				$.ajax({
					url: './loginProc',
					data: JSON.stringify({
						"place" : $("input[name='place']").val(),
						"type" : $("input[name='type']").val(),
						"userName" : $("input[name='userName']").val(),
						"birthDate" : $("input#password").val(),
						"telNo" : $("input#password").val().replace(reg, "")
//						"userName" : hex_sha512($("input[name='userName']").val()).toString(),
//						"birthDate" : hex_sha512($("input#password").val()).toString(),
//						"telNo" : hex_sha512($("input#password").val().replace(reg, "")).toString()
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
						var uri = body.returnUrl == null ? '/main' : body.returnUrl;
						sendUserInfo(body.access_token, body.info, body.branchInfo, uri);
					}
				});
			
			});
			
			$(document).on('click', ".btn_agreement_pop",function() {
				modalPopup('.pop_privacy');
			});
			
			$(document).on('click', ".btn_privacy_pop",function() {
				modalPopup('.pop_agreement');
			});
		};
		
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
		};

		return {
			// public functions
			init: function () {
				addEvent();
				get_msg();
			}
		};
	}();

	jQuery(document).ready(function () {
		Login.init();
	});
})(window, window.jQuery);
