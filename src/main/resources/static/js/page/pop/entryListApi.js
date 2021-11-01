(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };

    

    var setBinding = function() {

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
				, loginNo   : $('input[name=loginNo]').val()
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
							location.reload();
						 });
					 });
				 }else{
					modalAlert('',"최저가 이상의 가격을 입력해주세요.");
				}
			});
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
