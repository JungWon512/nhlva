;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];

    // Callback Proc
    var procCallback = function (returnData) {
        var resultData = returnData.data;
        if (resultData != undefined) {
            CKEDITOR.instances.noticeContent.setData(resultData.content);
            $("form.kt-form .editRegDtm").val(resultData.regDtm);
            $("form.kt-form .editRegUsrid").val(resultData.regUsrid);
            $("form.kt-form .editUptDtm").val(resultData.uptDtm);
            $("form.kt-form .editUptUsrid").val(resultData.uptUsrid);
        }
    };
    function getDetail() {
        if($("#seqNoVal").val()!="0") {
			var place = $('#place').val();
            COMMONS.callAjax("/office/auction/aucNotice" + '/' + $("#johpCdVal").val() + '/' + $("#seqNoVal").val() + ".json?place="+place,
                "GET", null, 'application/json','json', procCallback);
        }
    }
    var setLayout = function() {		
    	CKEDITOR.replace('noticeContent', {});
        //getDetail();
    };

    var setBinding = function() {
        $(document).on("click","a.submit_notice",

            function(e) {
                var seqNo = $("#seqNoVal").val();
                var johpCdVal = $("#johpCdVal").val();
                var delYn = "N";
                var content = CKEDITOR.instances.noticeContent.getData();
                var title = $(".editTitle").val();
                
                var params = {
                    seqNo: seqNo,
                    johpCd: johpCdVal,
                    title : title,
                    content:content
                }
                    
                var con_test = confirm("해당 공지사항을 등록/수정 하시겠습니까?["+johpCdVal+"]["+seqNo+"]");
                if(con_test == true){
                    if($("#seqNoVal").val()!="0") {
                        COMMONS.callAjax("/office/auction/aucNotice/"+johpCdVal+"/"+seqNo+".json"+window.location.search,  "put", params, 'application/json','json',  procCallbackModify );
                    } else {
                        params.delYn = "0";
                        COMMONS.callAjax("/office/auction/aucNotice.json"+window.location.search, "post", params, 'application/json', 'json', procCallbackModify);
                    }
                }
                else if(con_test == false){
                    //alert("취소되었습니다.");
                }

            }
        );
        $(document).on("click","a.notice-cancel",function(){
			pageMove("/office/auction/aucNotice");	
		});

    };

    var procCallbackModify = function(resultData) {
        if($("#seqNoVal").val()!="0") {
            //alert("수정되었습니다.");

            location.reload();
        } else {
            //alert("등록 되었습니다.");
            pageMove("/office/auction/aucNotice");
        }

    }

    var namespace = win.auction;
    var __COMPONENT_NAME = "AUCTION_EDIT";
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
