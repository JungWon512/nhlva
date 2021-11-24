(function ($, win, doc) {

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
        $(document).on("click",".list_sch", function(){
			fnSearch();
        });
        $(document).on("click",".btn_print", function(){
			var head = new Array();
			var body = new Array();
			$('.schedule_tb .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.schedule_tb .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportPrint('auction_sales','경매예정',head,body);
        });
        $(document).on("click",".btn_excel", function(){
			var head = new Array();
			var body = new Array();
			$('.schedule_tb .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.schedule_tb .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('경매예정',head,body);
        });

        $(document).on("click",".pd_pav a", function(){
			$('.popup .modal-wrap.pop_jjim_input.zim').remove();
			
			var li = $(this).closest('li');
			
			var jjim_price = li.find('input.sbidUpr').val();
			var naBzPlc = li.find('input.naBzPlc').val();
			var aucDt = li.find('input.aucDt').val();
			var aucObjDsc = li.find('input.aucObjDsc').val();
			var oslpNo = li.find('input.oslpNo').val();
			var aucPrgSq = li.find('input.aucPrgSq').val();
			var lowsSbidLmtUpr = li.find('input.lowsSbidLmtUpr').val();
			
			var div = $('.popup');
			var sHtml="";
			sHtml += ' <div id="" class="modal-wrap pop_jjim_input zim">';
			sHtml += ' 	<input type="hidden" name="lowsSbidLmtUpr" value="'+lowsSbidLmtUpr+'"/> ';
			sHtml += ' 	<input type="hidden" name="oldJjimPrice" value="'+jjim_price+'"/> ';
			sHtml += ' 	<div class="modal-content">';
			sHtml += ' 		<h3>찜가격</h3>';
			sHtml += '		<button class="modal_popup_close" onclick="modalPopupClose(\'.popup .modal-wrap.pop_jjim_input.zim\');">닫기</button>';
			sHtml += ' 		<dl class="jjim_dl">';
			sHtml += ' 			<dt>';
//			sHtml += ' 				<p>경매번호: 101번 <span>|</span> 최저가: '+lowsSbidLmtUpr+'만 </p>';
			sHtml += ' 				<p>최저가: '+lowsSbidLmtUpr+'만 </p>';
			sHtml += ' 			</dt>';
			sHtml += ' 			<dd>';
			//sHtml += ' 				<input type="text" placeholder="찜가격 입력 (금액 만 원)" name="inputUpr" id="inputUpr" maxlength=4 value="'+jjim_price+'">';
			sHtml += '					<input type="text" name="inputUpr" id="inputUpr" oninput="inputNumberVaild(this, 4)" value="'+jjim_price+'" placeholder="찜가격 입력 (금액 만 원)" pattern="\d*" inputmode="numeric" />';
			sHtml += ' 			</dd>';
			sHtml += ' 		</dl>';
			sHtml += ' 		<div class="btn_area">';
			sHtml += ' 			<button class="btn_cancel" onclick="return false;">찜 삭제</button>';
			sHtml += ' 			<button class="btn_ok">찜 하기</button>';
			sHtml += ' 		</div>';
			sHtml += ' 	</div>';
			sHtml += ' </div>';

			
			div.append($(sHtml));
			modalPopup('.popup .modal-wrap.pop_jjim_input.zim');
			
			
        	$('.modal-wrap.pop_jjim_input.zim .modal_popup_close').on("click", function(){			
				modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');			
			});
						
			var params ={
				naBzPlc      : naBzPlc
				, aucDt      : aucDt
				, aucObjDsc  : aucObjDsc
				, oslpNo     : oslpNo
				, aucPrgSq   : aucPrgSq				
				, place   : $('#johpCdVal').val()
			}
	        $('.modal-wrap.pop_jjim_input.zim .btn_cancel').on("click", function(){
				var inputUpr = $('.modal-wrap.pop_jjim_input.zim input[name=inputUpr]').val();
				var lowsSbidLmtUpr = $('.modal-wrap.pop_jjim_input.zim input[name=lowsSbidLmtUpr]').val();
				var oldJjimPrice = $('.modal-wrap.pop_jjim_input.zim input[name=oldJjimPrice]').val();
				if(!oldJjimPrice || oldJjimPrice == 0){
					modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');					
					location.reload();
					return;
				}
				
				params.inputUpr   = inputUpr;
				modalComfirm('찜삭제',"찜가격을 삭제하시겠습니까?",function(){
					 COMMONS.callAjax("/auction/api/deleteZimPrice", "post", params, 'application/json', 'json', function(data){
						modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');					
						location.reload();
					 });	
				 });
			});
	        $('.modal-wrap.pop_jjim_input.zim .btn_ok').on("click", function(){
				var inputUpr = $('.modal-wrap.pop_jjim_input.zim input[name=inputUpr]').val();
				var lowsSbidLmtUpr = $('.modal-wrap.pop_jjim_input.zim input[name=lowsSbidLmtUpr]').val();
				params.inputUpr   = inputUpr;		
				if(inputUpr && parseInt(lowsSbidLmtUpr) <= parseInt(inputUpr)) {
					modalComfirm('찜하기',"찜가격을 저장하시겠습니까?",function(){
						 COMMONS.callAjax("/auction/api/inserttZimPrice", "post", params, 'application/json', 'json', function(data){
							modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
							if(data && !data.success){
								modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
								return;
							}
							location.reload();
						 });
					 });
				 }else{
					modalAlert('',"최저가 이상의 가격을 입력해주세요.");
				}
			});
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
			sHtml += '  		<h3>출장우 상세</h3>                                                                                                       ';
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
			sHtml += '  					<th>생년월일</th>                                                                                              ';
			sHtml += '  					<td colspan="3" class="tal">'+convertStrDate(tr.find('.birthMonth').val())+'</td>                                                           ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>개체번호</th>                                                                                              ';
			sHtml += '  					<td colspan="3" class="num tal">'+pdEa.substring(6,10)+' <span>'+pdEa.substring(10,14)+'</span> '+pdEa.substring(14,15)+'</td>                                                              ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>KPN</th>                                                                                                   ';
			sHtml += '  					<td colspan="3" class="fwb tal">'+tr.find('.kpnNoStr').val()+'</td>                                                                                      ';			
			sHtml += '  				</tr>                                                                                                              ';			
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>중량</th>                                                                                                  ';
			sHtml += '  					<td class="tal">'+tr.find('.cowSogWt').val()+'kg</td>                                                                                     ';
			sHtml += '  					<th>성별</th>                                                                                                  ';
			sHtml += '  					<td class="sex">'+tr.find('.indvSexCName').val()+'</td>                                                                                        ';			
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>최저가</th>                                                                                                ';
			sHtml += '  					<td class="tal">'+tr.find('.lowsSbidLmtUpr').val()+'만원</td>                                                                                  ';
			sHtml += '  					<th>어미</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.mcowDsc').val()+'</td>                                                                                      ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th>산차</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.matime').val()+'</td>                                                                                         ';
			sHtml += '  					<th>계대</th>                                                                                                  ';
			sHtml += '  					<td class="fwb">'+tr.find('.sraIndvPasgQcn').val()+'</td>                                                                                         ';
			sHtml += '  				</tr>                                                                                                              ';
			sHtml += '  				<tr>                                                                                                               ';
			sHtml += '  					<th class="vtt">특이사항</th>                                                                                  ';
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
	form.attr('action', '/sales'+window.location.search);
	form.attr('method', 'post');
	form.appendTo('body');
	form.append($("<input type='hidden' value='BUY' name='searchGubun'>"));
	form.append($("<input type='hidden' value="+$('input[name=searchTxt]').val()+" name='searchTxt'>"));
	form.append($("<input type='hidden' value="+$('input[name=searchAucObjDsc]:checked').val()+" name='searchAucObjDsc'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchType]').val()+" name='searchType'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchOrder]').val()+" name='searchOrder'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchDate]').val()+" name='searchDate'>"));
	
	form.submit();
}