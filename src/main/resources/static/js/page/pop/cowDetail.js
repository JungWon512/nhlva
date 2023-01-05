;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    	tabLoad('0');
    };

    var setBinding = function() {
    	$('div.tab_list a').click(function(e){
    		if($(this).hasClass('act')) return;
    		const id = $(this).data('tabId');
    		tabLoad(id);
    	});
    	
    	$(document).on('click','.btnCowSearch',function(e){
    		var indvNo = ""+$(this).data('indvNo');
    		if(indvNo){
				var param = {
					naBzplc : $("form[name='frmDetail'] input[name='naBzplc']").val()
					,sraIndvAmnno : indvNo
				}
    			
    			var title = $(this).closest('tr').find('th').text().trim();
	    		$("form[name='frmDetail'] input[name='title']").val(title);
	    		$("form[name='frmDetail'] input[name='sraIndvAmnno']").val(indvNo);			
				var temp = window.location.search.split("&");
				var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
				var target = 'cowDetailFull';
				window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
				var form = document.frmDetail;
				form.action = "/cowDetailFull"+params.join("&");
				form.target=target;
				form.submit();
				//$.ajax({
				//	url: '/info/getIndvInfo',
				//	data: param,
				//	type: 'POST',
				//	dataType: 'json',
				//	success : function(result) {
				//		if(result.success){			    		    
				//    		$("form[name='frmDetail'] input[name='title']").val(title);
				//    		$("form[name='frmDetail'] input[name='sraIndvAmnno']").val(indvNo);			
				//			var temp = window.location.search.split("&");
				//			var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
				//			var target = 'cowDetailFull';
				//			window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
				//			var form = document.frmDetail;
				//			form.action = "/cowDetailFull"+params.join("&");
				//			form.target=target;
				//			form.submit();
				//		}else{
				//			modalAlert('',title+"개체의 정보가 없습니다.");
				//		}
				//	},
				//	error: function(xhr, status, error) {
				//	}
				//});
    		
    		
    		}
			
			return;
    	});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "RESULT_LIST";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
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


var tabLoad = function(tabId){
	var param = $('form[name=frm]').serializeObject();
	param.tabId = tabId;
	
	$.ajax({
		url: '/info/getCowInfo',
		data: param,
		type: 'POST',
		dataType: 'html',
		async : false,
		success : function(html) {
    		$('div.cow-detail div.tab_area').empty();
    		$('div.cow-detail div.tab_area').append(html);
		},
		error: function(xhr, status, error) {
		}
	}).done(function (json) {
	});
};