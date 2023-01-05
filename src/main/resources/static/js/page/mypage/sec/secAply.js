(function ($, win, doc) {
	
    var COMMONS = win.auction["commons"];

    var setBinding = function() {
		$(".btn_apply").unbind("click").click(function(e){
			e.preventDefault();
			fnSecApplyInfo();
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

var inputNumberVaild = function(el,len){
	if(el.value.length > len)  {
		el.value  = el.value.substr(0, len);
  	}
}

var fnSecApplyInfo = function() {
	modalComfirm(""
		, "이용해지 신청 하시겠습니까?<br/>이용해지 시, 모든 회원정보가 삭제됩니다."
		, function(){
			$.ajax({
				url: '/my/ins_mySecAply',
				data: JSON.stringify($("form[name='frm_secAply']").serializeObject()),
				type: 'POST',
				dataType: 'json',
				beforeSend: function (xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				}
			}).done(function (body) {
				if (body && body.success) {
					modalAlert("", body.message, function(){
						pageMove('/my/info');
					});
				}
				else {
					modalAlert("", body.message);
					return;
				}
			});
		}
	);
	
}



