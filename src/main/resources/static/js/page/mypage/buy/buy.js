(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    //var COMMONS = window.auction["commons"];

    var setBinding = function() {
        $(document).on("click",".tab_list ul > li", function(){
			var tabId = $(this).find('a.act').attr('data-tab-id');
			$('div.tab_area.'+tabId).show();
			$('div.auction_list h3').text($(this).find('a').text());
			convertScroll();
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
		COMMONS.callAjax("/auction/api/select/myBuyList", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
			
			//if(data.buyCnt && data.totPrice){
				//0 : 암,1:수,2:기타,3:합계
			$('.buy_list div.sum_table dl').each((i,o) => {
				switch(i){
					case 0 :
						if(data.buyCnt && data.buyCnt.CNT_SEX_W) $(o).find('dd p.cowCnt').text(fnSetComma(data.buyCnt.CNT_SEX_W)+'두');
						else $(o).find('dd p.cowCnt').text(0+'두');
						if(data.totPrice && data.totPrice.SRA_SBID_AM_SEX_W) $(o).find('dd p.cowPrice').text(fnSetComma(data.totPrice.SRA_SBID_AM_SEX_W)+' 원');
						else $(o).find('dd p.cowPrice').text(0+' 원');
					break;
					case 1 :
						if(data.buyCnt && data.buyCnt.CNT_SEX_M) $(o).find('dd p.cowCnt').text(fnSetComma(data.buyCnt.CNT_SEX_M)+'두');
						else $(o).find('dd p.cowCnt').text(0+'두');
						if(data.totPrice && data.totPrice.SRA_SBID_AM_SEX_M) $(o).find('dd p.cowPrice').text(fnSetComma(data.totPrice.SRA_SBID_AM_SEX_M)+' 원');
						else $(o).find('dd p.cowPrice').text(0+' 원');
					break;
					case 2 :
						if(data.buyCnt && data.buyCnt.CNT_SEX_ETC) $(o).find('dd p.cowCnt').text(fnSetComma(data.buyCnt.CNT_SEX_ETC)+'두');
						else $(o).find('dd p.cowCnt').text(0+'두');
						if(data.totPrice && data.totPrice.SRA_SBID_AM_SEX_ETC) $(o).find('dd p.cowPrice').text(fnSetComma(data.totPrice.SRA_SBID_AM_SEX_ETC)+' 원');
						else $(o).find('dd p.cowPrice').text(0+' 원');
					break;
//					case 3 :
//						if(data.buyCnt && data.buyCnt.CNT) $(o).find('dd p.cowCnt').text(fnSetComma(data.buyCnt.CNT)+'두');
//						else $(o).find('dd p.cowCnt').text(0+'두');
//						if(data.totPrice && data.totPrice.SRA_SBID_AM) $(o).find('dd p.cowPrice').text(fnSetComma(data.totPrice.SRA_SBID_AM)+' 원');
//						else $(o).find('dd p.cowPrice').text(0+' 원');
//					break;
				}
			});
			
			if(data.totPrice && data.totPrice.SRA_SBID_AM) $("div.sumTxt p").text('총 금액 : '+fnSetComma(data.totPrice.SRA_SBID_AM)+' 원');
			else $("div.sumTxt p").text('총 금액 : 0 원');
			
			//$('.buy_list div.list_txts span').text(data.totPrice ? getStringValue(data.totPrice.SRA_SBID_AM,0).replace(/\B(?=(\d{3})+(?!\d))/g, ",") :'0');
			$('.auction_result div.list_body ul').empty();
			if(data.data.length == 0){
				$('.auction_result div.list_body ul').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");
			}
			data.data.forEach(function(item,i){
				var sHtml = "";							
				sHtml += "<li><dl>";
				sHtml += " <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>";	
				sHtml += " <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>";	
				sHtml += " <dd class='name'>"+getStringValue(item.FTSNM)+"</dd>";	
				sHtml += " <dd class='pd_ea'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>";	
				sHtml += " <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>";	
				sHtml += " <dd class='pd_kg'>"+getStringValue(item.COW_SOG_WT)+"</dd>";	
				sHtml += " <dd class='pd_kpn'>"+getStringValue(item.KPN_NO_STR)+"</dd>";	
				sHtml += " <dd class='pd_num1'>"+getStringValue(item.SRA_INDV_PASG_QCN)+"</dd>";	
				sHtml += " <dd class='pd_pay1'>"+fnSetComma(getStringValue(item.LOWS_SBID_LMT_UPR))+"</dd>";	
				sHtml += " <dd class='pd_pay2'>"+fnSetComma(getStringValue(item.SRA_SBID_UPR))+"</dd>";	
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
		COMMONS.callAjax("/auction/api/select/myBidList", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
			$('.auction_bid div.list_body ul').empty();
			if(data.data.length == 0){
				$('.auction_bid div.list_body ul').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");								
			}
			data.data.forEach(function(item,i){
				var sHtml = "";							
				sHtml += "<li><dl>";
				sHtml += " <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>";	
				sHtml += " <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>";	
				sHtml += " <dd class='pd_ea'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>";	
				sHtml += " <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>";	
				sHtml += " <dd class='pd_kg'>"+getStringValue(item.COW_SOG_WT)+"</dd>";	
				sHtml += " <dd class='pd_pay1'>"+fnSetComma(getStringValue(item.LOWS_SBID_LMT_UPR))+"</dd>";	
				sHtml += " <dd class='pd_pay3'>"+fnSetComma(getStringValue(item.ATDR_AM))+"</dd>";	
				sHtml += " <dd class='pd_pay2'>"+fnSetComma(getStringValue(item.SRA_SBID_UPR))+"</dd>";	
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
	var gubun = $('.tab_list li a.act').data('tabId');	
	$('.tab_area.'+gubun).find(".list_table .list_body ul, div.modal-wrap .pop_TermsBox").each(function(){						
		$(this).removeClass('mCustomScrollBox');		
								
		$(this).css('-webkit-overflow-scrolling','touch');
		$(this).css('overflow-y','auto');
		$(this).css('overflow-x','hidden');
		
		var gubun = $('.tab_list li a.act').data('tabId');
		$('.tab_area.'+gubun).find('.list_table .list_body ul').css('height','');		
		resultH= $('section.header').outerHeight() + ($('section.contents').outerHeight() - $('.tab_area.'+gubun+' .list_table .list_body ul').outerHeight()) +$('section.footer').outerHeight() +1;
			
		resultH = $('body').outerHeight() - resultH;
		console.log(resultH);
		$(this).css('height','  '+resultH+'px');
		$(this).css('min-height','70px');
	});	
}