(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    //var COMMONS = window.auction["commons"];

    var setBinding = function() {
        $(document).on("click",".tab_list ul > li", function(){
			var tabId = $(this).find('a.act').attr('data-tab-id');
			$('div.tab_area.'+tabId).show();
			$('div.auction_list h3').text($(this).find('a').text());
		});
         $(document).on("keydown","li.txt input.searchTxtBuy", function(e){
		 	if(e.keyCode == 13){
		 		fnSearchBuy();				
		 	}	
		 });
        $(document).on("click",".list_sch.sch_buy", function(){
			fnSearchBuy();
        });
         $(document).on("keydown","li.txt input.searchTxtBid", function(e){
		 	if(e.keyCode == 13){
		 		fnSearchBid();				
		 	}	
		 });
        $(document).on("click",".list_sch.sch_bid", function(){
			fnSearchBid();
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
	var setLayout  = function(){
		var tabId = $('div.tab_list li a.act').attr('data-tab-id');
		$('div.tab_area.'+tabId).show();
	};
    var namespace = win.auction;
    var __COMPONENT_NAME = "AUCTION_LIST";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();

            setBinding();
			fnSearchBuy();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });

	var fnSearchBuy = function(){
		var param = {naBzPlcNo : $("#naBzPlcNo").val()
			, place : $("#naBzPlcNo").val()
			, loginNo : $("#loginNo").val()
			, searchTxt : $("#searchTxtBuy").val()
			, searchDate : $("#searchDateBuy").val()
			, searchAucObjDsc : $("input[name=searchAucObjDscBuy]:checked").val()
		};
		console.log(param);
		COMMONS.callAjax("/auction/api/select/myBuyList", "post", param, 'application/json', 'json'
		, function(data){
			$('.auction_result div.list_body ul').empty();
			if(data.data.length == 0){
				$('.auction_result div.list_body ul').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");								
			}
			data.data.forEach((item,i)=>{
				var sHtml = "";							
				sHtml += "<li><dl>";
				sHtml += " <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>";	
				sHtml += " <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>";	
				sHtml += " <dd class='name'>"+getStringValue(item.SRA_PDMNM)+"</dd>";	
				sHtml += " <dd class='pd_ea'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>";	
				sHtml += " <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>";	
				sHtml += " <dd class='pd_kg'>"+getStringValue(item.COW_SOG_WT)+"</dd>";	
				sHtml += " <dd class='pd_kpn'>"+getStringValue(item.KPN_NO_STR)+"</dd>";	
				sHtml += " <dd class='pd_num1'>"+getStringValue(item.SRA_INDV_PASG_QCN)+"</dd>";	
				sHtml += " <dd class='pd_pay1'>"+getStringValue(item.LOWS_SBID_LMT_UPR)+"</dd>";	
				sHtml += " <dd class='pd_pay2'>"+getStringValue(item.SRA_SBID_UPR)+"</dd>";	
				sHtml += " <dd class='pd_state'>"+getStringValue(item.SEL_STS_DSC_NAME)+"</dd>";	
				sHtml += " <dd class='pd_etc'>"+getStringValue(item.RMK_CNTN)+"</dd>";
				sHtml += "</dl></li>";
				$('.auction_result div.list_body ul').append(sHtml);
			});
			convertScroll();
		});	
	}
	var fnSearchBid = function(){	
		var param = {naBzPlcNo : $("#naBzPlcNo").val()
			, place : $("#naBzPlcNo").val()
			, loginNo : $("#loginNo").val()
			, searchTxt : $("#searchTxtBid").val()
			, searchDate : $("#searchDateBid").val()
			, searchAucObjDsc : $("input[name=searchAucObjDscBid]:checked").val()
		};
		console.log(param);
		COMMONS.callAjax("/auction/api/select/myBidList", "post", param, 'application/json', 'json'
		, function(data){
			$('.auction_bid div.list_body ul').empty();
			if(data.data.length == 0){
				$('.auction_bid div.list_body ul').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");								
			}
			data.data.forEach((item,i)=>{
				var sHtml = "";							
				sHtml += "<li><dl>";
				sHtml += " <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>";	
				sHtml += " <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>";	
				sHtml += " <dd class='pd_ea'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>";	
				sHtml += " <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>";	
				sHtml += " <dd class='pd_kg'>"+getStringValue(item.COW_SOG_WT)+"</dd>";	
				sHtml += " <dd class='pd_pay1'>"+getStringValue(item.LOWS_SBID_LMT_UPR)+"</dd>";	
				sHtml += " <dd class='pd_pay2'>"+getStringValue(item.SRA_SBID_UPR)+"</dd>";	
				sHtml += " <dd class='pd_pay3'>"+getStringValue(item.ATDR_AM)+"</dd>";	
				sHtml += " <dd class='pd_state'>"+getStringValue(item.SEL_STS_DSC_NAME)+"</dd>";	
				sHtml += "</dl></li>";
				$('.auction_bid div.list_body ul').append(sHtml);
			});
			convertScroll();
		});	
	}
})(jQuery, window, document);

var inputNumberVaild = function(el,len){
	if(el.value.length > len)  {
		el.value  = el.value.substr(0, len);
  	}
}

var convertScroll = function(){	
	$(".list_table .list_body ul, div.modal-wrap .pop_TermsBox").each(function(){
		var preCss = $(this).closest('div.tab_area').attr("style");
		$(this).closest('div.tab_area').css({position:   'absolute', visibility: 'hidden',display:    'block'});						
		$(this).removeClass('mCustomScrollBox');
		
		var height = $('body').height() - ($(this).closest('.list_table').height() - $(this).closest('.list_body').height());
								
		$(this).css('-webkit-overflow-scrolling','touch');
		$(this).css('overflow-y','auto');
		$(this).css('overflow-x','hidden');
		//var tmpDate = $(this).closest('.tab_area').get(0)?150:"watch".indexOf(location.pathname.replace('/',''))>-1?382:290;
		var tmpDate = $(this).closest('.tab_area').get(0)?150:"watch".indexOf(location.pathname.replace('/',''))>-1?($('#aucDsc').val()==1?382:155):290;
		var resultH= ( height-tmpDate)<=0?'450':height-tmpDate;											
		$(this).css('height',resultH+'px');						
		$(this).closest('div.tab_area').attr("style", preCss ? preCss : "");
	});	
}