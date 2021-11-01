;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };

    var setBinding = function() {
        $(document).on("keydown","li.txt input", function(e){
			if(e.keyCode == 13){
				fnSearch();				
			}	
		});
		$(document).on('click','.list_sch',function(){
			fnSearch();
		});
		
        $(document).on("click",".btn_print", function(){
			var head = new Array();
			var body = new Array();
			$('.auction_result .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.auction_result .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			reportPrint('auction_result','출장우내역',head,body);
        });
        $(document).on("click",".btn_excel", function(){
			var head = new Array();
			var body = new Array();
			$('.auction_result .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.auction_result .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('경매결과',head,body);
        });
        
        $(document).on("click",".list_body .pd_ea a", function(){
			var pdEa = new String($(this).find('span').attr('fullstr'));
			var content = pdEa.substring(6,10)+'&nbsp;&nbsp;'+pdEa.substring(10,14)+'&nbsp;&nbsp;'+pdEa.substring(14,15);
			modalAlert('개체번호',content);
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

var fnSearch = function(){	
	var form = $('<form></form>');
	form.attr('action', '/my/entry'+window.location.search);
	form.attr('method', 'post');
	form.appendTo('body');			
	form.append($("<input type='hidden' value="+$('input[name=searchTxt]').val()+" name='searchTxt'>"));
	form.append($("<input type='hidden' value="+$('input[name=searchAucObjDsc]:checked').val()+" name='searchAucObjDsc'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchType]').val()+" name='searchType'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchOrder]').val()+" name='searchOrder'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchDate]').val()+" name='searchDate'>"));
	form.submit();
}
