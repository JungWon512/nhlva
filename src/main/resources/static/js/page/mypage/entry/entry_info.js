(function ($, win, doc) {
	
    var COMMONS = win.auction["commons"];

    var setBinding = function() {
        $(document).on("change","input[name=searchAucObjDsc]", function(){
			fnSearchState();
        });
        $(document).on("click","p.stateCowCnt a", function(){
			stateDetailPop($(this).closest('p.stateCowCnt').attr('id'));
		});
        $(document).on("click","table.table-detail td a", function(e){
			e.preventDefault();
			feeDetailPop($(this).closest('tr.detail_tr').attr('id'));
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

	var fnSearchState = function(){
		var param = {
			searchnaBzPlcNo : $("#naBzPlcNo").val()
			, place : location.search.replace("?place=","").replace("&aucYn=Y","")
			, stateFlag : $("#stateFlag").val()
			, searchDate : $("#searchDate").val()
			, searchAucObjDsc : $("input[name=searchAucObjDsc]:checked").val()
		};
		COMMONS.callAjax("/auction/api/select/myStateInfo", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}

			// 매수인정보	
			if(data.stateInfo) {
				$("div.auction_result table.stateInfo td.nm").text(data.stateInfo.USER_NM);
			}else{
				$("div.auction_result table.stateInfo td.info").text('-');
			}
			
			// 낙찰가
			if(data.stateTotPrice) {
				$("div.auction_result table.stateTotPrice td.bid").text(fnSetComma(data.stateTotPrice.SRA_SBID_AM)+' 원');	
				$("div.auction_result table.stateTotPrice td.fee span.c-red").text(fnSetComma(data.stateTotPrice.SRA_TR_FEE));	
				$("div.auction_result table.stateTotPrice td.total").text(fnSetComma(data.stateTotPrice.SRA_SBID_AM - data.stateTotPrice.SRA_TR_FEE)+' 원');	
			}else{
				$("div.auction_result table.stateTotPrice td.bid").text('0 원');
				$("div.auction_result table.stateTotPrice td.fee span.c-red").text('0');
				$("div.auction_result table.stateTotPrice td.total").text('0 원');
			} 
			
			// 출하두수
			if(data.stateBuyCnt) {
				$("div.auction_result table.stateBuyCnt td#O").text(fnSetComma(data.stateBuyCnt.CNT_BID + data.stateBuyCnt.CNT_HOLD));	
				$("div.auction_result table.stateBuyCnt td#B").text(fnSetComma(data.stateBuyCnt.CNT_BID));	
				$("div.auction_result table.stateBuyCnt td#H").text(fnSetComma(data.stateBuyCnt.CNT_HOLD));	
			}else{
				$("div.auction_result table.stateBuyCnt td.stateCowCnt").text('0');
			} 
			
			// 상세내역
			$('div.cow-sibiling').empty();
			var sHtml='';
			
			sHtml += '  			<table class="table-detail">                                                                                                                ';
			sHtml += '  				<colgroup>                                                                                                         ';
			sHtml += '  					<col width="13%">                                                                                              ';
			sHtml += '  					<col width="20%">                                                                                              ';
			sHtml += '  					<col width="15%">                                                                                              ';
			sHtml += '  					<col width="*">                                                                                              ';
			sHtml += '  					<col width="27%">                                                                                                ';
			sHtml += '  				</colgroup>                                                                                                        ';
			sHtml += '  				<thead>                                                                                                        ';
			sHtml += '  					<tr>                                                                                                               ';
			sHtml += '  						<th>번호</th>                                                                                          ';
			sHtml += '  						<th>개체번호</th>                                                                                          ';
			sHtml += '  						<th>성별</th>                                                                                          ';
			sHtml += '  						<th>낙찰금액(원)</th>                                                                                          ';
			sHtml += '  						<th>수수료(원)</th>                                                                                          ';
			sHtml += '  					</tr>                                                                                                              ';
			sHtml += '  				</thead>                                                                                                              ';
			sHtml += '  				<tbody>                                                                                                              ';
			if (data.list.length > 0) {
				data.list.forEach((e,i) => {
					sHtml += '  				<tr class="detail_tr" id='+ e.SRA_INDV_AMNNO +'>                                                                                                               ';
					sHtml += '  					<td class="ta-C bg-gray">'+ e.AUC_PRG_SQ + '</td>                                                                                                  ';
					sHtml += '  					<td class="ta-C">'+ e.SRA_INDV_AMNNO_FORMAT + '</td>                                                                                                  ';
					sHtml += '  					<td class="ta-C">'+ e.INDV_SEX_C_NAME +'</td>                                                                                                  ';
					sHtml += '  					<td class="ta-C" id="textContainer_'+i+'" class="textContainer">'+ fnSetComma(e.SRA_SBID_AM) +'</td>                                                                                                  ';
					if (e.SRA_TR_FEE) {
						sHtml += '  					<td class="ta-C"><a href="javascript:;" class="c-red">'+ fnSetComma(e.SRA_TR_FEE) +'</a></td>             ';						
					} else {
						sHtml += '  					<td class="ta-C">-</td>                                                                                                  ';
					}
					sHtml += '  				</tr>                                                                                                              ';					
				});
			} else {
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<td class="ta-C" colspan="5">상세 내역이 없습니다.</td>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				
			}
			sHtml += '  				</tbody>                                                                                                               ';
			sHtml += '  			</table>                                                                                                               ';
			
			$('div.cow-sibiling').append(sHtml);
			$('.table-detail td').each(function(){
				if(!$(this).text()) $(this).text('-');
			});
		});	
	}
        
    var feeDetailPop = function(sraIndvAmnno){
		var param = {
			searchnaBzPlcNo : $("#naBzPlcNo").val()
			, place : location.search.replace("?place=","").replace("&aucYn=Y","")
			, stateFlag : "entry"
			, searchDate : $("#searchDate").val()
			, searchAucObjDsc : $("input[name=searchAucObjDsc]:checked").val()
			, sraIndvAmnno : sraIndvAmnno
		};
		
		COMMONS.callAjax("/auction/api/select/myFeeStateInfo", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}else{
				modalPopupClose('.popup .modal-wrap.pop_fee');
				$('.popup .modal-wrap.pop_fee').remove();
				var sHtml=''; 
				
				sHtml += '  <div id="" class="modal-wrap pop_fee">                                                                                        ';
				sHtml += '  	<div class="modal-content">                                                                                                    '; 
				sHtml += '  		<p class="name">수수료상세</p>                                                                                                       ';
				sHtml += '  		<div class="cow-basic">                                                                                                    ';
				sHtml += '  			<table class="table-detail">                                                                                                                ';
				sHtml += '  				<colgroup>                                                                                                         ';
				sHtml += '  					<col width="35%">                                                                                              ';
				sHtml += '  					<col>                                                                                              ';
				sHtml += '  				</colgroup>                                                                                                        ';
				sHtml += '  				<tbody>                                                                                                        ';
				data.feeList.forEach((e, i) => {
					sHtml += '  				<tr>                                                                                                               ';
					sHtml += '  					<th>'+ e.SRA_TR_FEE_NM +'</th>                                                                                          ';
					sHtml += '  					<td>'+fnSetComma(e.SRA_TR_FEE)+' 원</td>                                                                                ';
					sHtml += '  				</tr>                                                                                                              ';						
				})
				sHtml += '  				</tbody>                                                                                                               ';
				sHtml += '  			</table>                                                                                                               ';
				sHtml += '  		</div>                                                                                                                     ';
				sHtml += '  		<div class="btn_area">                                                                                                                     ';
				sHtml += '  			<button class="btn_ok" onclick="modalPopupClose(\'.pop_fee\');return false;">확인</button>                                                                                                                     ';
				sHtml += '  		</div>                                                                                                                     ';
				sHtml += '  	</div>                                                                                                                         ';
				sHtml += '  </div>                                                                                                                             ';
				
				$('.popup').append($(sHtml));
				
				$('.cow-basic table-detail td').each(function(){
					if(!$(this).text()) $(this).text('-');
				});
				modalPopup('.popup .modal-wrap.pop_fee');
			}
		});
	};
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
		$(this).css('height','  '+resultH+'px');
		$(this).css('min-height','70px');
	});	
}
