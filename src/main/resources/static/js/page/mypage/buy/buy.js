	;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];

    var setBinding = function() {
         $(document).on("keydown","li.txt input", function(e){
		 	if(e.keyCode == 13){
		 		fnSearch();				
		 	}	
		 });
        $(document).on("click",".list_sch.sch_buy", function(){
			fnSearch();
        });        
		
        $(document).on("click",".btn_printBuy", function(){
			var head = new Array();
			var body = new Array();
			$('.buy_list .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.buy_list .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			reportPrint('auction_result','경매결과',head,body);
        });
        $(document).on("click",".btn_excelBuy", function(){
			var head = new Array();
			var body = new Array();
			$('.buy_list .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.buy_list .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('구매내역',head,body);
        });
    };
	var setLayout  = function(){};
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

var inputNumberVaild = function(el,len){
	if(el.value.length > len)  {
		el.value  = el.value.substr(0, len);
  	}
}

var fnSearch = function(){	
	var form = $('<form></form>');
	form.attr('action', '/my/buy'+window.location.search);
	form.attr('method', 'post');
	form.appendTo('body');			
	form.append($("<input type='hidden' value="+$('input[name=searchTxtBuy]').val()+" name='searchTxtBuy'>"));
	form.append($("<input type='hidden' value="+$('input[name=searchAucObjDscBuy]:checked').val()+" name='searchAucObjDscBuy'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchDateBuy]').val()+" name='searchDateBuy'>"));
	
	form.submit();
}