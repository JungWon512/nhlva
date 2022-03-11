;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];

    // Callback Proc
    var procCallback = function(resultData) {
    };

    var setLayout = function() {
    };

    var setBinding = function() {
    	$('.notice_btn').on('click',function(){    	
			if(!$(this).find('a').hasClass('act')){
	    		var params = {};
		        params.naBzplc = $("#johpCdVal").val();
		        params.seqNo = $(this).find(".noticeSeqNo").val();
		        params.blbdDsc = $(this).find(".noticeDsc").val();
    			procCallAjax('/auction/api/updateNoticeReadCnt', 'post', params, null, function(data,ajax) {
    				console.log(data);
    				var param = JSON.parse(ajax.data);
    				$('.notice_btn .readCnt.sqNo_'+param.blbdDsc+'_'+param.seqNo).text(data.data.BBRD_INQ_CN);
		        });
		        
				var focus_top = $(this).offset().top;
				var speed = 500;
				$('body, html').stop().animate({scrollTop:focus_top}, speed);
				$(this).parents('li').find('.notice_view').show();
			} else{
				$(this).parents('li').find('.notice_view').hide()
			}	
			$(this).find('a').toggleClass('act');
    	});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "NOTICE_LIST";
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

var procCallAjax = function(reqUrl, reqMethod, params, preFunc, callback, complete) {

    console.log('reqMethod=>'+reqMethod);

    if(reqMethod.toLowerCase()=="post" || reqMethod.toLowerCase()=="put"){
        if(typeof(params)=="object"){
            params = JSON.stringify(params);
        }
    }
    $.ajax({
        url: reqUrl,
        method: reqMethod,
        type: reqMethod,
        data: params,
        dataType: 'JSON',
        async: false,
        contentType: 'application/json',
        success: function(data) {
            callback(data,this);
        },
        error: function(xhr, status, error) {
            //alert("api error message");
            console.log("ERROR :: data :: ", error, xhr);
        },
        complete : function(data) {
            // SKIP
            //console.log("COMPLETE :: data :: ", data);
            if(complete != undefined)
                complete(data);
        }
    });
};
