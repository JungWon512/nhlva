(function ($, win, doc) {
	
    var COMMONS = win.auction["commons"];

    var setBinding = function() {
		setAuthNoCountDown();
		
		//키오스크 인증번호 확인하기
		$(".kiosk_btn").unbind("click").click(function(){
			if($(".kiosk_num_area").css("display") == "none"){
				$(".kiosk_num_area").show();
				$(".kiosk_btn").addClass("on");
				$(".kiosk_btn").removeClass("off");
			}else{
				$(".kiosk_num_area").hide();
				$(".kiosk_btn").addClass("off");
				$(".kiosk_btn").removeClass("on");
			}
		});
		
		//키오스크 인증번호 발급하기
		$(".btn-re-auth").unbind("click").click(function(e){
			e.preventDefault();
			fnIssueKioskAuthNum();
		});
    };
    
    var namespace = win.auction;
    var __COMPONENT_NAME = "AUCTION_LIST";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
			var showFlag = 
            setBinding();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
	
})(jQuery, window, document);

var secApplyProc = function() {
	modalComfirm(""
		, "이용해지 신청 하시겠습니까?"
		, function(){
			$.ajax({
				url: '/my/mySecAplyCheck',
				data: JSON.stringify($("form[name='frm_info']").serializeObject()),
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
					pageMove('/my/secAply');
				}
			});
		});
	return;
}

var secWithdrawProc = function() {
	modalComfirm(""
		, "해지신청 철회하시겠습니까?"
		, function(){
			$.ajax({
				url: '/my/mySecWithdrawCheck',
				data: JSON.stringify($("form[name='frm_info']").serializeObject()),
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
					pageMove('/my/secWithdraw');
				}
			});
		});
	return;
}