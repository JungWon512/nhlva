;(function (win, $) {
    "use strict";
    if ("undefined" === typeof win.auction) {
        win.auction = {};
    }
    var __COMPONENT_NAME = "commons";
    var namespace = win.auction;
    var options;

    namespace[__COMPONENT_NAME] = (function () {

        var callAjax = function(url,method,param,contentType,dataType,callback,complete) {
            var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            var headers = {};
            headers[csrfHeader] = csrfToken;
            var paramData = param;
            if(param ==null || param ==undefined) {
                paramData = null;
            }

            $.ajax({
                url: url,
                type: method,
                headers: headers,
                data:JSON.stringify(param),
                contentType: contentType,
                dataType: dataType,
                beforeSend: function(){
                },
                success: function(data) {
                    $(".spinner").hide();
                    callback(data);
                },
                error: function(xhr, status, error) {
                    debugConsole("ERROR :: data :: ", error, xhr);
                },
                complete : function(data) {
                    // SKIP
                    debugConsole("COMPLETE :: data :: ", data);
                    // if(!param.nospinner) {
                        $(".spinner").hide();
                    // }
                    if(complete != undefined) {
                        $(".spinner").hide();
                        complete(data);
                    }
                }
            });
        };

        var simpleCallAjax = function(url,param,callback) {
            callAjax(url,'post',param,'application/json','json',callback,null);
        };
        var addCommas = function (nStr)
        {
            nStr += '';
            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? '.' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        };

        $(window).resize(function (){
            // width값을 가져오기
            var width_size = window.innerWidth;
            $("#windowWidth").val(width_size);
            // 800 이하인지 if문으로 확인
            if (width_size <= 991.98) {
                $("#deviceMode").val("MOBILE");
                $(".pc-only").hide();
                $(".mobile-only").show();

            } else {
                $("#deviceMode").val("PC");
                $(".pc-only").show();
                $(".mobile-only").hide();
            }
        })

        var setBinding = function(){
            $(document).on('click','.m_header .m_backTit',function(){
				//history back 해야하는 pathname (, 하고 추가하기)
				var backPathNm = "/my/buyInfo, /my/entryInfo, /cowDetail, /my/secAply, /my/secWithdraw";
				
				if(window.location.pathname.indexOf('/agreement') > -1){pageMove('/agreement/new'); return;} 
				if(window.location.pathname.indexOf('/dashboard') > -1 || window.location.search.indexOf('dashYn') > -1){pageMove('/dashboard'); return;} 
				if(window.location.pathname == '/main'){pageMove('/home'); return;} 
				else if(backPathNm.indexOf(window.location.pathname) > -1){history.back(); return;}
				pageMove('/main');    
			});
			
			if(document.URL.indexOf('agreement') > 0){
				if(document.URL.indexOf('new') < 0){
					$('.hideFoot').hide();
				} 
			}
			
        }
        
        var init = function (data) {
            $(window).trigger('resize');
            setBinding();
        };
        
        

        return {
            init: init,
            addCommas: addCommas,
            callAjax: callAjax,
            simpleCallAjax: simpleCallAjax
        }
    })();

    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(window, window.jQuery);

(function(win, $){
    $.fn.serializeObject = function () {
        "use strict";

        var result = {};
        var extend = function (i, element) {
            var node = result[element.name];

            // If node with same name exists already, need to convert it to an array as it
            // is a multi-value field (i.e., checkboxes)

            if ('undefined' !== typeof node && node !== null) {
                if ($.isArray(node)) {
                    node.push(element.value);
                } else {
                    result[element.name] = [node, element.value];
                }
            } else {
                result[element.name] = element.value;
            }
        };

        $.each(this.serializeArray(), extend);
        return result;
    };
    
    $.fn.setCursorPosition = function( pos ) {
	  "use strict";
	  this.each( function( index, elem ) {
	    if( elem.setSelectionRange ) {
	      elem.setSelectionRange(pos, pos);
	    } else if( elem.createTextRange ) {
	      var range = elem.createTextRange();
	      range.collapse(true);
	      range.moveEnd('character', pos);
	      range.moveStart('character', pos);
	      range.select();
	    }
	  });
  
	  return this;
	};
	/* 20220103 모바일 더블클릭 이벤트 제거 
	$.event.special.dblclick = {
	    setup: function(data, namespaces) {
	        var elem = this,
	            $elem = $(elem);
	        $elem.bind('touchend.dblclick', $.event.special.dblclick.handler);
	    },
	
	    teardown: function(namespaces) {
	        var elem = this,
	            $elem = $(elem);
	        $elem.unbind('touchend.dblclick');
	    },
	
	    handler: function(event) {
	        var elem = event.target,
	            $elem = $(elem),
	            lastTouch = $elem.data('lastTouch') || 0,
	            now = new Date().getTime();
	
	        var delta = now - lastTouch;
	        if(delta > 20 && delta<500){
	            $elem.data('lastTouch', 0);
	            $elem.trigger('dblclick');
	        }else
	            $elem.data('lastTouch', now);
	    }
	};
	*/
})(window, window.jQuery);