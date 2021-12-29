var preLoadDate;
(function(){
    'use strict';
    var agent = navigator.userAgent.toLowerCase(),
        name = navigator.appName,
        browser;
        
    if(name === 'Microsoft Internet Explorer' || agent.indexOf('trident') > -1 || agent.indexOf('edge/') > -1) {
        browser = 'ie';
        $("body").addClass("msie");
        if(name === 'Microsoft Internet Explorer') {
            agent = /msie ([0-9]{1,}[\.0-9]{0,})/.exec(agent);
            browser += parseInt(agent[1]);
        } else {
            if(agent.indexOf('trident') > -1) {
                browser += 11;
            } else if(agent.indexOf('edge/') > -1) {
                browser = 'edge';
            }
        }
    } else if(agent.indexOf('safari') > -1) {
        if(agent.indexOf('opr') > -1) {
            browser = 'opera';
        } else if(agent.indexOf('chrome') > -1) {
            browser = 'chrome';
        } else {
            browser = 'safari';
        }
    } else if(agent.indexOf('firefox') > -1) {
        browser = 'firefox';
    };
    document.getElementsByTagName('html')[0].className = browser;
    if( /Android/i.test(navigator.userAgent)) {
    	// 안드로이드
		$('html').addClass("android")
	}else if (/iPhone|iPad|iPod/i.test(navigator.userAgent)) {
		// iOS 아이폰, 아이패드, 아이팟
		$('html').addClass("ios")
	}else {
		// 그 외 디바이스
		$('html').addClass("other")
	}
	//page unload page reload체크
	if(location.pathname.indexOf('office') < 0 || location.pathname.indexOf('office/task') > 0)
		window.document.addEventListener("visibilitychange", function(e) {
			if(window.document.visibilityState == 'hidden'){
				preLoadDate=new Date();			
			}else{
				var tmpDate = new Date();
				tmpDate.setMinutes(tmpDate.getMinutes()-15);
				
				if(preLoadDate && tmpDate > preLoadDate){
					try{
						sFlag = true;
						if(socket && socket.connected) socket.disconnect();
						location.reload();						
					}catch(e){
						debugConsole(e);
					}
				}
			}
		});	
		if(active != 'production'){
			$('.m_header .m_tit').text($('.m_header .m_tit').text()+" DEV");
			$('section.footer li:eq(2) a').text(" DEV"+$('section.footer li:eq(2) a').text());			
		}
}());

var $winW = $(window).width();
var $winH = $(window).height();
//모달레이어팝업
function modalPopup(target){
	var $winH = $(window).height();
	var preCss = $(target).attr("style");
	var srlTop = $(window).scrollTop();
	$(target).css({position:   'absolute', visibility: 'hidden',display:    'block'});
	$(target).find('.modal-content').css("marginTop", 0);
	
	var $modalContent = $(target).find($('.modal-content'));
	$(target).focus();
	var $modalContentH = $(target).find($('.modal-content')).outerHeight();
	var $conPos = ($winH / 2) - ($modalContentH / 2);	
	
	if( ($winH > $modalContentH && $modalContentH != 0) && target != '.pop_auction'){
		$modalContent.css({marginTop: $conPos});
	} else {
		$modalContent.css({marginTop: 0});
	}
	if($('.popup .modal-wrap.open').length < 1){
		$("<div class='overlay'></div>").appendTo('#wrap');
		$('body').addClass('scroll_fixed');
		$("#wrap").css('margin-top', -srlTop + 'px');			
		$("#wrap").attr('data',srlTop);
	}
	
	if($(target).find(".modal-content").find('.mCustomScrollBox').length > 0){		
		if(isApp() || chkOs() != 'web'){
			$("div.modal-wrap .pop_TermsBox").each(function(){
				var preCss = $(this).closest('div.tab_area').attr("style");
				$(this).closest('div.tab_area').css({position:   'absolute', visibility: 'hidden',display:    'block'});						
				$(this).removeClass('mCustomScrollBox');
				
				var height = $('body').height() - ($(this).closest('.list_table').height() - $(this).closest('.list_body').height());
										
				$(this).css('-webkit-overflow-scrolling','touch');
				$(this).css('overflow-y','auto');
				$(this).css('overflow-x','hidden');
				var tmpDate = $(this).closest('.tab_area').get(0)?150:"watch".indexOf(location.pathname.replace('/',''))>-1?($('#aucDsc').val()==1?382:155):290;
				var resultH= ( height-tmpDate)<=0?'450':height-tmpDate;											
				$(this).css('height',resultH+'px');						
				$(this).closest('div.tab_area').attr("style", preCss ? preCss : "");
			});
		}else{
			$(".pop_TermsBox,.pop_detailBox").mCustomScrollbar({
				theme:"dark-thin",
				scrollInertia: 200
			});
		}
		
	}
	$(target).attr("style", preCss ? preCss : "");
	$(target).css({'overflow': 'auto'}).show().addClass('open');
	
	return false;
};

//모달레이어팝업닫기
function modalPopupClose(target){
	$(target).find($('.modal-content')).css('margin-top',0);
	$(target).hide().removeClass('open');
	if (!$(".pop_notice").hasClass('open')) {
		$('html, body').off('scroll touchmove mousewheel');
	}
	if($('.popup .modal-wrap.open').length < 1){
		$(".overlay").remove();
		$('body').removeClass('scroll_fixed');
		$("#wrap").css('margin-top', 0);
	}
};

$(document).ready(function() {
	var baseUi = {
		init: function () {
			baseUi.resize();
			baseUi.pdFav();
			baseUi.notice();
			baseUi.tab();
			var $mmenu = $('nav#menu-lnb');
			$mmenu.mmenu({
				position	: 'right',
				zposition		: 'front',
				iconPanels : false,
				slidingSubmenus: false
			});
			var API = $("nav#menu-lnb").data( "mmenu" );
			$(".nav_close").click(function() {
				API.close();
			});
			$('body').append('<div class="popup"></div>');
		},
		pdFav: function (e) {
			$(".pd_pav a").on('click', function(e){ 
				// $(this).toggleClass('act');
			});
			$(window).on("load",function(){
				if(isApp() || chkOs() != 'web'){
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
				}else{
					$(".list_body ul").mCustomScrollbar({
						theme:"dark-thin",
						scrollInertia: 200,
						setTop :0
						,setWidth: false
						,setHeight: false
					});
				}
			});
		},
		notice :  function(e){
			$(".notice_btn a").on('click', function(e){ 
				$(this).toggleClass('act');
				if($(this).hasClass('act')){
					var focus_top = $(this).offset().top;
					var speed = 500;
					$('body, html').stop().animate({scrollTop:focus_top}, speed);
					$(this).parents('li').find('.notice_view').show()
				} else{
					$(this).parents('li').find('.notice_view').hide()
				}
			});
		},
		resize : function(e){
			function throttle(ms, fn) {
				var allow = true;
				function enable() {
					allow = true;
				}
				return function(e) {
					if(allow) {
						allow = false;
						setTimeout(enable, ms);
						fn.call(this, e);
					}
				}
			}
			$(window).on('resize load',(throttle(100, function(e) {
				var winW = $(window).width();
				if(winW < 1025 && $("div").is('.chk_step1') == true || winW < 1025 && $("div").is('.chk_step2') == true){
					$('body').addClass('bg-gray');
				} else{
					$('body').removeClass('bg-gray');
				}
				if($("select").length > 0 ) {
					var chk = $('select').data('selectric');
					if(!chk) $('select').selectric(); //selectric ui load
				};
			})));
		},
		tab : function(e){
			$('.tab_list ul > li').each(function(i){this.i=i}).click(function(){
				var idx = $(this).index();
				$(".tab_list li a").removeClass('act');
				$(".tab_area").hide();
				//$(".tab_area:eq("+idx+")").show();
				$(this).find('a').addClass('act');
			});
		}
	}
	baseUi.init();
});