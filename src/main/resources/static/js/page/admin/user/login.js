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
			// 로그인 버튼
			$(document).on('click', ".action-submit", function(e){
				e.preventDefault();
				$.ajax({
					url: './loginProc',
					data: JSON.stringify($("input").serializeObject()),
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
				deleteCookie("access_token"); 
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Login.init();
	});

})(window, window.jQuery);
