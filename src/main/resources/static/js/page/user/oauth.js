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
		console.log('popup : '+param);
		//opener.redirectKaKaoLogin(param);
		localStorage.setItem("kkoRedirectParam", JSON.stringify(param));
		window.close();
	});
});