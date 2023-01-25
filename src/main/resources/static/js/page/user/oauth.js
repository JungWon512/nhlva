$(function() {   
	$(document).ready(function () {
		Kakao.Auth.setAccessToken($('#kakao_access_token').val(),true);
		debugger;
		var birthday = $("input#birthday").val()||'';
		var birthyear = $("input#birthyear").val()||'';
		var param = {
				"place" : $("input#place").val(),
				"type" : '0',
				"userName" : $("input#name").val(),
				"birthDate" : (birthyear.substr(2,2))+birthday,
				"connChannel" : connChannel()
			
		}
		console.log('popup : '+param);
		opener.redirectKaKaoLogin(param);
		window.close();
	});
});