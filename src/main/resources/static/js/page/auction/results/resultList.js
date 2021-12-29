;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		$('.list_table .list_body li dd').each(function(){
			if(!$(this).text()) $(this).text('-');
		});
		$('.list_table .list_body li dd span').each(function(){
			if(!$(this).text()) $(this).text('-');
		});
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
			reportPrint('auction_result','경매결과',head,body);
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
			var tr = $(this).closest('li');
			
			modalPopupClose('.popup .modal-wrap.pop_exit_cow');
			$('.popup .modal-wrap.pop_exit_cow').remove();
			var sHtml=''; 
			
			sHtml += '  <div id="" class="modal-wrap pop_exit_cow">                                                                                        ';
			sHtml += '  	<div class="modal-content">                                                                                                    ';
			sHtml += '  		<button class="modal_popup_close" onclick="modalPopupClose(\'.pop_exit_cow\');return false;">닫기</button>                   ';
			sHtml += '  		<h3>경매결과 상세</h3>                                                                                                       ';
			sHtml += '  		<div class="cow-table">                                                                                                    ';
			sHtml += '  			<table>                                                                                                                ';
			sHtml += '  				<colgroup>                                                                                                         ';
			sHtml += '  					<col width="20%">                                                                                              ';
			sHtml += '  					<col width="30%">                                                                                              ';
			sHtml += '  					<col width="20%">                                                                                              ';
			sHtml += '  					<col width="*">                                                                                                ';
			sHtml += '  				</colgroup>                                                                                                        ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>경매<br>번호</th>                                                                                          ';
			sHtml += '  					<td class="auc-num">'+tr.find('.aucPrgSq').val()+'</td>                                                                                   ';
			sHtml += '  					<th>출하주</th>                                                                                                  ';
			sHtml += '  					<td class="name">'+tr.find('.sraPdmNm').val()+'</td>                                                                                   ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>지역</th>                                                                                                  ';
			sHtml += '  					<td colspan="3" class="tal">'+tr.find('.sraPdRgnnm').val()+'</td>                                                             ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>생년<br/>월일</th>                                                                                              ';
			sHtml += '  					<td colspan="3" class="tal">'+convertStrDate(tr.find('.birthMonth').val())+'</td>                                                           ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>개체<br/>번호</th>                                                                                              ';
			sHtml += '  					<td colspan ="3" class="num tal">'+pdEa.substring(6,10)+' <span>'+pdEa.substring(10,14)+'</span> '+pdEa.substring(14,15)+'</td>                                                              ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>KPN</th>                                                                                                   ';
			sHtml += '  					<td colspan="3" class="fwb tal">'+tr.find('.kpnNoStr').val()+'</td>                                                                                      ';
			sHtml += '  				</tr>                                                                                                                        ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>중량</th>                                                                                                  ';
			sHtml += '  					<td class="tal">'+tr.find('.cowSogWt').val()+'kg</td>                                                                                     ';
			sHtml += '  					<th>성별</th>                                                                                                  ';
			sHtml += '  					<td class="sex">'+tr.find('.indvSexCName').val()+'</td>                                                                                        ';			
			sHtml += '  				</tr>                                                                                                    ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>최저가</th>                                                                                                ';
			sHtml += '  					<td class="tal">'+fnSetComma(tr.find('.lowsSbidLmtUpr').val())+'</td>                                                                                  ';
			sHtml += '  					<th>어미</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.mcowDsc').val()+'</td>                                                                                      ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                                                      ';
			sHtml += '  					<th>산차</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.matime').val()+'</td>';
			sHtml += '  					<th>계대</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.sraIndvPasgQcn').val()+'</td>                                                                                         ';                                                               
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>낙찰자</th>                                                                                                  ';
			if (tr.find('.lvstAucPtcMnNo').val() != "") {
				sHtml += '  					<td class="tal">'+tr.find('.lvstAucPtcMnNo').val()+' 번</td>                                                                                         ';
			}
			else {
				sHtml += '  					<td class="tal">-</td>                                                                                         ';
			}
			sHtml += '  					<th>낙찰가</th>                                                                                                  ';
			sHtml += '  					<td class="tal">'+fnSetComma(tr.find('.sraSbidUpr').val())+'</td>                                                                                         ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th class="vtt">특이<br/>사항</th>                                                                                  ';
			sHtml += '  					<td colspan="3" class="tal vtt">'+tr.find('.rmkCntn').val()+'</td>                                                                          ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  			</table>                                                                                                               ';
			sHtml += '  		</div>                                                                                                                     ';
			sHtml += '  	</div>                                                                                                                         ';
			sHtml += '  </div>                                                                                                                             ';
			
							
			$('.popup').append($(sHtml));
			$('.cow-table td').each(function(){
				if(!$(this).text()) $(this).text('-');
			});
			modalPopup('.popup .modal-wrap.pop_exit_cow');
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
	form.attr('action', '/results'+window.location.search);
	form.attr('method', 'post');
	form.appendTo('body');			
	form.append($("<input type='hidden' value="+$('input[name=searchTxt]').val()+" name='searchTxt'>"));
	form.append($("<input type='hidden' value="+$('input[name=searchAucObjDsc]:checked').val()+" name='searchAucObjDsc'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchType]').val()+" name='searchType'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchOrder]').val()+" name='searchOrder'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchDate]').val()+" name='searchDate'>"));
	form.submit();
}
