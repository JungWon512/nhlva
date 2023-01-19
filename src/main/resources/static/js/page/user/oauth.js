$(function() {   
	$(document).ready(function () {
		Kakao.Auth.setAccessToken($('#kakao_access_token').val(),true);
		debugger;
		var param = {
				"place" : $("input#place").val(),
				"type" : '0',
				"userName" : '홍길동',
				"birthDate" : '711101',
				"connChannel" : connChannel()
			
		}
		console.log('popup : '+param);
		opener.redirectKaKaoLogin(param);
		window.close();
	});
});