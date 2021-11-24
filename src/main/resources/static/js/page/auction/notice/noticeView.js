$(function() {

    function getDetail() {
        procCallAjax("/office/auction/aucNotice"+'/'+$(".johpCd").val()+'/'+$(".seqNo").val()+".json", "GET", null, null, procCallback);
    }

    var procCallback = function(result) {
        var dataItem = result.data;
        $("form.kt-form .johpCd").val(dataItem.johpCd);
        $("form.kt-form .seqNo").val(dataItem.seqNo);
        $("form.kt-form .title").val(dataItem.title);
        $("form.kt-form .content").val(dataItem.content);
        $("form.kt-form .delYn").val(dataItem.delYn);
        $("form.kt-form .rdCnt").val(dataItem.rdCnt);
        $("form.kt-form .regDtm").val(dataItem.regDtm);
        $("form.kt-form .regUsrid").val(dataItem.regUsrid);
        $("form.kt-form .uptDtm").val(dataItem.uptDtm);
        $("form.kt-form .uptUsrid").val(dataItem.uptUsrid);
    };

    var checkAndUpdate = function() {
        if ($("form.kt-form .johpCd").val().length == 0) {
            showModalOne("alert", "johpCd를 입력해 주세요.");
            return;
        }
        if ($("form.kt-form .seqNo").val().length == 0) {
            showModalOne("alert", "seqNo를 입력해 주세요.");
            return;
        }
        var params = {};
        params.johpCd = $("form.kt-form .johpCd").val();
        params.seqNo = $("form.kt-form .seqNo").val();
        params.title = $("form.kt-form .title").val();
        params.content = $("form.kt-form .content").val();
        params.delYn = $("form.kt-form .delYn").val();
        params.rdCnt = $("form.kt-form .rdCnt").val();
        params.regDtm = $("form.kt-form .regDtm").val();
        params.regUsrid = $("form.kt-form .regUsrid").val();
        params.uptDtm = $("form.kt-form .uptDtm").val();
        params.uptUsrid = $("form.kt-form .uptUsrid").val();
        var ajaxUrl = "";
        var ajaxMethod = "post";
        if (params.uuid == "0") {
            ajaxUrl = "/office/auction/aucNotice.json";
            ajaxMethod = "post";
        } else {
            ajaxUrl = "/office/auction/aucNotice"+'/'+$(".johpCd").val()+'/'+$(".seqNo").val();
            ajaxMethod = "put";
        }
        procCallAjax(ajaxUrl, ajaxMethod, params, null, function(data) {

            var btn = $("div.kt-portlet form.kt-form a.btn-submit");
            if (data.success) {

                KTApp.unprogress(btn);
                //KApp.unblock(formEl);

                swal.fire({
                    "title" : "",
                    "text" : "The application has been successfully submitted!",
                    "type" : "success",
                    "confirmButtonClass" : "btn btn-secondary"
                });

                location.replace('/office/auction/aucNotice');
            } else {
                KTApp.unprogress(btn);
                swal.fire({
                    "title" : "",
                    "text" : data.message,
                    "type" : "error"
                });
            }
        });
    }

    var setLayout = function() {
        if ($(".uuid").val() != "0") {
            getDetail();
        }
    };

    var setBinding = function() {
        $(document).on("click", "div.kt-portlet form.kt-form a.btn-submit", function() {
            checkAndUpdate();
        });
    };

    setLayout();

    setBinding();

});

    var procCallAjax = function(reqUrl, reqMethod, params, preFunc, callback, complete) {

        debugConsole('reqMethod=>'+reqMethod);

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
            beforeSend: function(){
                ///preFunc
            },
            success: function(data) {
                callback(data);
            },
            error: function(xhr, status, error) {
                //alert("api error message");
                debugConsole("ERROR :: data :: ", error, xhr);
            },
            complete : function(data) {
                // SKIP
                //console.log("COMPLETE :: data :: ", data);
                if(complete != undefined)
                    complete(data);
            }
        });
    };
