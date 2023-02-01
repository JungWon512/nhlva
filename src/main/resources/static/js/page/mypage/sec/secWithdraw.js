(function ($, win, doc) {
	
    var COMMONS = win.auction["commons"];

    var setBinding = function() {
		
		$(".select_na_bzplc").unbind("click").click(function(){
			var id = $(this).find("input[name='selelctNaBzPlc']").attr("id");
			if(id == "select_bzplc"){
				$(".secAplyDl").hide();
			}else{
				$(".secAplyDl").show();
			}
		});
		
		$(".btn_withdraw").unbind("click").click(function(e){
			e.preventDefault();
			fnSecWithdrawInfo();
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

var fnSecWithdrawInfo = function() {
	modalComfirm(""
		, "이용해지 철회하시겠습니까?<br/>철회는 관리자 승인 이전까지 가능합니다."
		, function(){
			$.ajax({
				url: '/my/del_mySecAply',
				data: JSON.stringify($("form[name='frm_secWithdraw']").serializeObject()),
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



