;(function (win, $,COMMONS) {
// Class definition
	var Login = function () {
		var get_msg = function(message) {
			if(message!=undefined){
				alert(message);
			}
		};

		var addEvent = function(){
			$(document).on('keydown', "input[name=usrid],input[name=pw]",function(e){
				if(e.keyCode == '13') $('.action-submit').click();
			});
			
			$(document).on('click', ".btnApkDownload", function(e){
				var apk = (active == 'production')?"auctionmanager0.0.3-release.apk":"auctionmanager0.0.3-debug.apk";
			    var pom = document.createElement('a');
			    pom.setAttribute('href', '/static/apk/'+apk);
			    pom.setAttribute('type', 'application/vnd.android.package-archive');
			    pom.setAttribute('download', apk);
			 
			    if (document.createEvent) {
			        var event = document.createEvent('MouseEvents');
			        event.initEvent('click', true, true);
			        pom.dispatchEvent(event);
			    }
			    else {
			        pom.click();
			    }
			});
			// 로그인 버튼
			$(document).on('click', ".action-submit", function(e){
				e.preventDefault();
		    	var rsa = new RSAKey();    	
		    	rsa.setPublic($("#RSAModulus").val(), $("#RSAExponent").val());
		    	var user_id = rsa.encrypt($("#usrid").val());
		    	var user_pw = rsa.encrypt($("#pw").val());
				var param = $("input").serializeObject();
				param.rsaKey = $('#RSAKey').val();
				param.usrid = user_id;
				param.pw = user_pw;
				
				$.ajax({
					url: './loginProc',
					data: JSON.stringify(param),
					type: 'POST',
					dataType: 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					}
				}).done(function (body) {
					var error = body.error;
					var message = body.message;
					if (error) {
						get_msg(message);
					}
					else {
						var uri = '/office/main';
//						if (chkOs() != 'web') {
//							uri = '/office/task/main'
//						}
						if ($("#save_id").is(":checked")) {
							setCookieLimitDay("eno", $("#usrid").val());
						}
						else {
							deleteCookie("eno");
						}
						pageMove(uri);
					}
				});
			});
		};

		return {
			// public functions
			init: function () {
				if (getCookie("eno") != undefined) {
					$("#usrid").val(getCookie("eno"));
					$("#save_id").prop("checked", true);
				}
		        if(!isApp()){
			        $('.btnApkDownload').show();	
				}
				if(active != 'production'){
					$('.login_box h1 span').text($('.login_box h1 span').text()+" DEV");			
				}
				
				deleteCookie("access_token"); 
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Login.init();
	});

})(window, window.jQuery);
