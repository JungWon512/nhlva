
$(document).ready(function () {
//	Kakao.init('bcc997fd4a44d8ab7eff284d32f83f59');
//	var str = findGetParameter('code');
//	str += ' <br/>'+findGetParameter('state');
//	console.log('${token.access_token}');
//	$('h1.test').html('${token.access_token}');
	Kakao.Auth.setAccessToken($('#kakao_access_token').val(),true);
//	setCookie('nhlva_k_r_token',$('#kakao_refresh_token').val(),'60');
//	Kakao.API.request({
//		url:'//oauth/token'
//	}).then(function(response) {
//	    console.log(response)
//  	}).catch(function(error) {
//    	console.error(error)
//  	})
});