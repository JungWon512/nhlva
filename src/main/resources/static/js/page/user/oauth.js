$(function() {   
	$(document).ready(function () {
		Kakao.Auth.setAccessToken($('#kakao_access_token').val(),true);
		var birthday = $("input#birthday").val()||'';
		var birthyear = $("input#birthyear").val()||'';
		var param = {
				"place" : $("input#place").val(),
				"type" : '0',
				"userName" : '홍길동',
				"birthDate" : '711101',
//				"userName" : $("input#name").val(),
//				"birthDate" : (birthyear.substr(2,2))+birthday,
				"connChannel" : connChannel()
			
		}
		console.log('popup : '+JSON.stringify(param));
		var reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"\s]/gi;
		$.ajax({
			url: '/user/kkologinProc',
			data: JSON.stringify(param) ,
			type: 'POST',
			dataType: 'json',
			beforeSend: function (xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
			}
		}).done(function (body) {
			localStorage.removeItem("kkoRedirectParam");
			var success = body.success;
			var message = body.message;
			if (!success) {
				modalAlert('', message);
			}
			else {
				var uri = body.returnUrl == null ? '/main' : body.returnUrl;
				sendUserInfo(body.access_token, body.info, body.branchInfo, (uri+'?place='+param.place));				
			}
		});	
		//opener.redirectKaKaoLogin(param);
		//localStorage.setItem("kkoRedirectParam", JSON.stringify(param));
		//window.close();
	});
});


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
		console.log(userInfo);
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