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
                    console.log("ERROR :: data :: ", error, xhr);
                },
                complete : function(data) {
                    // SKIP
                    console.log("COMPLETE :: data :: ", data);
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
            $(document).on('click','.m_header .m_back',function(){				 
				if(window.location.pathname == '/main'){pageMove('/home'); return;}
				pageMove('/main');
			});
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
})(window, window.jQuery);