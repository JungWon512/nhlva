;(function ($, win, doc) {
    var COMMONS = win.auction["commons"];
    function getList() {
        $('#example').DataTable({	
            bFilter: false
            , bInfo: true
            ,"language": {
                "lengthMenu": "페이지별 _MENU_ 개씩",
                "zeroRecords": "해당되는 데이터가 없습니다.",
                "info": "페이지 _PAGE_ / _PAGES_",
                "infoEmpty": "데이터가 없습니다."
            }, "order": [[0, "desc"]]
        });
    }

    // Callback Proc
    var procCallback = function(resultData) {

    };

    var setLayout = function() {
        getList();
    };

    var setBinding = function() {
        $(document).on("click","a.delete-notice",
            function(e) {
                var seqNo = $(this).closest("tr").data("seqno");
                var johpCdVal = $("#johpCdVal").val();
                var delYn = "1";

                var params = {
                    seqNo: seqNo,
                    naBzPlc: johpCdVal,
                    delYn: delYn
                }

                var con_test = confirm("해당 공지사항을 삭제 하시겠습니까?["+johpCdVal+"]["+seqNo+"]");
                if(con_test == true){
                    COMMONS.callAjax("/office/auction/aucNotice/"+johpCdVal+"/"+seqNo+".json"+window.location.search,  "put", params, 'application/json','json',  procCallbackDelete );
                }
            }
        );
        
        
    };

    var procCallbackDelete = function(resultData) {

		//Swal.fire(
		//    "삭제되었습니다.",
		//    "success"
		//)
        location.reload();
    }

    var namespace = win.auction;
    var __COMPONENT_NAME = "AUCTION_LIST";
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
