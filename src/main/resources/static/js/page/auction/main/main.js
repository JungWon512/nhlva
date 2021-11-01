$(function() {
    function getList() {}

    var setLayout = function() {
        getList();
        if(!isApp() && chkOs() != 'web'){
	        if(getCookie('appChkDate') != getTodayStr()){
				popUp();
			}	
		}
    };

    var setBinding = function() {
    };

    setLayout();    
    setBinding();
});

function popUp(){
	var sHtml = '<div id="" class="modal-wrap pop_app">';
	sHtml += '	<div class="modal-content">';
	sHtml += '		<button class="modal_popup_close">닫기</button>';
	sHtml += '		<h4>지역선택 없이<br>바로 응찰 할 수 있어요.</h4>';
	sHtml += '		<a href="javascript:;" class="btn_app">가축시장 앱으로 보기</a>';
	sHtml += '		<a href="javascript:;" class="no_app">괜찮습니다. 모바일 웹으로 볼게요.</a>';
	sHtml += '	</div>';
	sHtml += '</div>';
	$('.popup').append($(sHtml));
	modalPopup('.popup .modal-wrap.pop_app');
	
	$('.popup .pop_app .modal_popup_close').click(function(){
		modalPopupClose(".pop_app");		
		$('.popup .modal-wrap.pop_app').remove();
	});
	
	$('.popup .pop_app .btn_app').click(function(){
		if(chkOs() == 'aos') {
			//location.href = 'app://com.nh.cowauction';
			 
			location.href = 'app://com.nh.cowauction?targetUrl=main'+location.search;

			setTimeout(function(){				
				setTimeout(function(){
					location.href = 'https://play.google.com/store/apps/details?id=com.nh.cowauction';
				},1000)					
			},500);
		}
		else if(chkOs() == 'ios') location.href = 'nhcowauction://com.nh.cowauction?targetUrl=main?place=119&aucYn=Y'; 
		modalPopupClose(".pop_app");		
		$('.popup .modal-wrap.pop_app').remove();
	});
	
	$('.popup .pop_app .no_app').click(function(){
		setCookieLimitDay('appChkDate',getTodayStr());
		modalPopupClose(".pop_app");		
		$('.popup .modal-wrap.pop_app').remove();
	});
	
}