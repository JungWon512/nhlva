;(function ($, win, doc) {
    var COMMONS = win.auction["commons"];
    var initYear = $('#searchYear').val();
    var fSearchCh = false;
    var intervalProgress;
    var pIndex=0;
//	$('#searchYear').datetimepicker({
//		format: 'LT'
//	});
    function getList() {
	    $('#example').DataTable({
		    "paging" : true,
		    "searching" : false,
		    "info" : true,
		    "autoWidth" : false,
		    "responsive" : true,
		    "lengthChange" : true,          // 페이지 조회 시 row를 변경할 것인지
		    "lengthMenu" : [ 10,30,50, 100, 150 ],  // lengthChange가 true인 경우 선택할 수 있는 값 설정
		    "ordering" : false,
		    "columns" : [
				 {"data" : "SEQ_NO"}
				 ,{"data" : "VISIT_IP"}
				 ,{"data" : "VISIT_REFER"}
				 ,{"data" : "VISIT_AGENT"}
				 ,{"data" : "VISIT_DTM"}
		    ],
		    "processing" : false,
		    "serverSide" : true,        // serverside 사용 여부
		    "ajax" : {
		        url : "/office/getReportList"
		        ,data : function(d) { 
					d.searchDate = $('#searchYear').val();
					d.searchFlag = fSearchCh;
					
					//progress modal 적용
					var preCss = $('.modal').attr("style");
					$('.modal').css({position:   'absolute', visibility: 'hidden',display:    'block'});
					var $conPos = ($(window).height() / 2) - ($('.modal').find('.modal-content').outerHeight() / 2) -40;
					$('.modal').attr("style", preCss ? preCss : "");
					$('.modal').css({top: $conPos});
					$('.modal').show();
					$('body').append('<div class="overlay_modal"></div>');
					intervalProgress = setInterval(function(){
						if(pIndex>=100)pIndex=0;
						$('.modal .progress .progress-bar').css({width:(pIndex+=10)+'%'});					
					},300)	
		        }
		        ,dataSrc : function(d) { 					
					$('#searchYear').val(d.inputParam.searchDate);
					//progress modal 제거
					$('.modal').hide();
					clearInterval(intervalProgress);
					pIndex=0;
					$('.modal .progress .progress-bar').css({width:'0%'});
					$('.overlay_modal').remove();
					
					return d.data;
		        },
		    }
		});
    }

    var setLayout = function() {
        getList();
    };

    var setBinding = function() {
		$(document).on('click','#btnSearch',function(){			
			if(initYear != $('#searchYear').val()) fSearchCh=true;
			$('#example').DataTable().ajax.reload();			
		});
		$(document).on('keydown','#searchYear',function(e){			
			if(e.keyCode == '13') $('#btnSearch').click();		
		});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "VISIT_LIST";
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
