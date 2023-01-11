(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		var tabId = $('div.tab_list li a.act').attr('data-tab-id');
		$('div.tab_area.'+tabId).show();
		$('div.winpop button.winpop_close').hide();
		fn_Load(tabId);	
    };
    
    var setBinding = function() {
        $(document).on("click","#btn_refreshAuc", function(){
        	fn_Load('auc');
		});
        $(document).on("click","#btn_refreshSold", function(){
        	fn_Load('sold');
		});
		
        $(document).on("click",".tab_list ul > li", function(){
			var tabId = $(this).find('a.act').attr('data-tab-id');
			fn_Load(tabId);
		});
                
      	$(document).on("click",".tab_area.auc .schedule_tb_mo .list_body .pd_ea a", function(){			
			var pdEa = new String($(this).find('span').attr('fullstr'));	
			var tr = $(this).closest('li');
			$("form[name='frm_select'] input[name='naBzplc']").val(tr.find('.naBzPlc').val());
			$("form[name='frm_select'] input[name='aucDt']").val(tr.find('.aucDt').val());
			$("form[name='frm_select'] input[name='aucObjDsc']").val(tr.find('.aucObjDsc').val());
			$("form[name='frm_select'] input[name='oslpNo']").val(tr.find('.oslpNo').val());
			$("form[name='frm_select'] input[name='sraIndvAmnno']").val(tr.find('.sraIndvAmnno').val());
			$("form[name='frm_select'] input[name='aucPrgSq']").val(tr.find('.aucPrgSq').val());
			$("form[name='frm_select'] input[name='ledSqno']").val(tr.find('.ledSqno').val());
			$("form[name='frm_select'] input[name='place']").val($('#johpCdVal').val());
			
			var temp = window.location.search.split("&");
			var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
			var target = 'cowDetail';
			
			window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
			var form = document.frm_select;
			form.action = "/cowDetail"+params.join("&");
			form.target=target;
			form.submit();
			
			return;
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
			sHtml += ' 				<p>예정가: '+fnSetComma(lowsSbidLmtUpr)+'</p>';
			sHtml += ' 			</dt>';
			sHtml += ' 			<dd>';
			sHtml += '					<input type="text" name="inputUpr" id="inputUpr" oninput="inputNumberVaild(this, 5)" value="'+jjim_price+'" placeholder="찜가격 입력" pattern="\d*" inputmode="numeric" />';
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
				, loginNo   : $('input[name=loginNo]').val()
			}
	        $('.modal-wrap.pop_jjim_input.zim .btn_cancel').on("click", function(){
				var inputUpr = $('.modal-wrap.pop_jjim_input.zim input[name=inputUpr]').val();
				var lowsSbidLmtUpr = $('.modal-wrap.pop_jjim_input.zim input[name=lowsSbidLmtUpr]').val();
				var oldJjimPrice = $('.modal-wrap.pop_jjim_input.zim input[name=oldJjimPrice]').val();
				if(!oldJjimPrice || oldJjimPrice == 0){
					modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
					var searchObj = convertStrToObj(location.search);
					searchObj.tabAct = $('.tab_list ul li a.act').data('tabId');
					var search = convertObjToStr(searchObj);
					return;
				}								
				
				params.inputUpr   = inputUpr;
				modalComfirm('찜삭제',"찜가격을 삭제하시겠습니까?",function(){
					 COMMONS.callAjax("/auction/api/deleteZimPrice", "post", params, 'application/json', 'json', function(data){
						modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
						if(data.aucInfo){
							var aucPrgSq =$('.auc .list_table.schedule_tb li dl dd.num').toArray().filter((obj) => {if($(obj).text() == data.aucInfo.AUC_PRG_SQ) return obj;});
							var li = $(aucPrgSq[0]).closest('li');
							li.find('input.sbidUpr').val(data.aucInfo.SBID_UPR ? data.aucInfo.SBID_UPR : '찜가격');
							li.find('.pd_pav span').text(data.aucInfo.SBID_UPR ? data.aucInfo.SBID_UPR : '찜가격');
							if(data.aucInfo.SBID_UPR && data.aucInfo.SBID_UPR != 0) li.find('.pd_pav a').addClass('act');
							else li.find('.pd_pav a').removeClass('act');
							
							var aucPrgSq =$('.auc .list_table.schedule_tb_mo li dl dd.num').toArray().filter((obj) => {if($(obj).text() == data.aucInfo.AUC_PRG_SQ) return obj;});
							var li = $(aucPrgSq[0]).closest('li');
							li.find('input.sbidUpr').val(data.aucInfo.SBID_UPR ? data.aucInfo.SBID_UPR : '찜가격');
							li.find('.pd_pav span').text(data.aucInfo.SBID_UPR ? data.aucInfo.SBID_UPR : '찜가격');
							if(data.aucInfo.SBID_UPR && data.aucInfo.SBID_UPR != 0) li.find('.pd_pav a').addClass('act');
							else li.find('.pd_pav a').removeClass('act');
						}					
					 });	
				 });
			});
	        $('.modal-wrap.pop_jjim_input.zim .btn_ok').on("click", function(){
				var inputUpr = $('.modal-wrap.pop_jjim_input.zim input[name=inputUpr]').val();
				var lowsSbidLmtUpr = $('.modal-wrap.pop_jjim_input.zim input[name=lowsSbidLmtUpr]').val();
				params.inputUpr   = inputUpr;		
				if(inputUpr && parseInt(lowsSbidLmtUpr) <= parseInt(inputUpr)) {
					 COMMONS.callAjax("/auction/api/inserttZimPrice", "post", params, 'application/json', 'json', function(data){
						modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
						if(data && !data.success){
							modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
							return;
						}
						if(data.aucInfo){
							//PC용 찜가격수정
							var aucPrgSq =$('.auc .list_table.schedule_tb li dl dd.num').toArray().filter((obj) => {if($(obj).text() == data.aucInfo.AUC_PRG_SQ) return obj;});
							var li = $(aucPrgSq[0]).closest('li');
							li.find('input.sbidUpr').val(data.aucInfo.SBID_UPR);
							li.find('.pd_pav span').text(fnSetComma(data.aucInfo.SBID_UPR));
							if(data.aucInfo.SBID_UPR && data.aucInfo.SBID_UPR != 0) li.find('.pd_pav a').addClass('act');
							else li.find('.pd_pav a').removeClass('act');
							
							//모바일용 찜가격수정
							var aucPrgSq =$('.auc .list_table.schedule_tb_mo li dl dd.num').toArray().filter((obj) => {if($(obj).text() == data.aucInfo.AUC_PRG_SQ) return obj;});
							var li = $(aucPrgSq[0]).closest('li');
							li.find('input.sbidUpr').val(data.aucInfo.SBID_UPR);
							li.find('.pd_pav span').text(fnSetComma(data.aucInfo.SBID_UPR));
							if(data.aucInfo.SBID_UPR && data.aucInfo.SBID_UPR != 0) li.find('.pd_pav a').addClass('act');
							else li.find('.pd_pav a').removeClass('act');
						}
					 });
				 }else{
					modalAlert('',"예정가 이상의 가격을 입력해주세요.");
				}
			});
        });
        
        $(document).on("click", ".auction_bid > .list_body > ul > li", function(){
			try{
				const aucPrgSq = $(this).find("dd.num").text().replace(/[^0-9.]/g,'').replace(/(\..*)\./g,'$1');;
				// 안드로이드
				if (window.auctionBridge) {
					window.auctionBridge.setAucPrgSq(aucPrgSq);
				}
				// 아이폰
				else if(isIos()) {
					window.webkit.messageHandlers.setAucPrgSq.postMessage(aucPrgSq);
				}
			}
			catch(e){
				console.log(e);
			};
		}); 
    };
    

	
    var namespace = win.auction;
    var __COMPONENT_NAME = "AUCTION_ENTRY_LIST";
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


function convertStrToObj(str){
	var obj = new Object();
	str.replace('?','').split('&').forEach((item)=>{obj[item.split('=')[0]]=item.split('=')[1];})
	return obj;
}
function convertObjToStr(obj){
	var str ="";
	Object.keys(obj).forEach((item,i) => (str += (i==0?'':'&')+item+'='+obj[item]));
	return str;	
}

var inputNumberVaild = function(el,len){
	el.value= el.value.replace(/\B(?=(\d{3})+(?!\d))/g, "");
	if(el.value.length > len) {
		el.value = el.value.substr(0, len);
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


var fn_Load = function(tabId){
	console.log(tabId);
	var param = new Object();
	param.tabId = tabId;
	
	param.naBzPlcNo = $('#johpCdVal').val();
	param.naBzplc = $('#johpCd').val();
	param.naBzPlc = $('#johpCd').val();
	param.loginNo = $('#loginNo').val();
	
	
	if(tabId =='auc'){
		param.searchDate = $('select[name=searchDateAuc]').val()?$('select[name=searchDateAuc]').val():$('#searchDate').val();
		param.searchAucObjDsc =$('select[name=searchAucObjDscAuc]').val();
	}else if(tabId =='sold'){
		param.searchDate = $('select[name=searchDateSold]').val()?$('select[name=searchDateSold]').val():$('#searchDate').val();
		param.searchAucObjDsc =$('select[name=searchAucObjDscSold]').val();
	}else if(tabId =='bid'){
		param.searchDate ='';
	}
	
	$.ajax({
		url: '/info/getBidPopupDetail',
		data: param,
		type: 'POST',
		dataType: 'html',
		async : false,
		success : function(html) {
			$('div.tab_area.'+tabId).empty();
			$('div.tab_area.'+tabId).append(html);
			$("div.tab_area").not("."+tabId).hide();
			$('div.tab_area.'+tabId).show();
		},
		error: function(xhr, status, error) {
		}
	});
}