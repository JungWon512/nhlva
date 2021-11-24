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
}());

//모달레이어팝업
var $winW = $(window).width();
var $winH = $(window).height();
function modalPopup(target){
	var srlTop = $(window).scrollTop();
	$('.modal-content').css("marginTop", 0);
	var $modalContent = $(target).find($('.modal-content'));
	$(target).css({'overflow': 'auto'}).show().addClass('open');
	$(target).focus();
	var $modalContentH = $(target).find($('.modal-content')).outerHeight();
	var $conPos = ($winH / 2) - ($modalContentH / 2);
	if( $winH > $modalContentH ){
		$modalContent.css({marginTop: $conPos});
	} else {
		$modalContent.css({marginTop: 0});
	}
	$("<div class='overlay'></div>").appendTo('#wrap');
	$('body').addClass('scroll_fixed');
	$("#wrap").css('margin-top', -srlTop + 'px');
	$("#wrap").attr('data',srlTop);
	if($(".modal-content").find('.mCustomScrollBox').length > 0){
		$(".pop_TermsBox").mCustomScrollbar({
			theme:"dark-thin",
			scrollInertia: 200,
		});
	}
	return false;
};

//모달레이어팝업닫기
function modalPopupClose(target){
	$(target).find($('.modal-content')).css('margin-top',0);
	$(target).hide().removeClass('open');
	if (!$(".pop_notice").hasClass('open')) {
		$('html, body').off('scroll touchmove mousewheel');
	}
	$(".overlay").remove();
	$('body').removeClass('scroll_fixed');
	$("#wrap").css('margin-top', 0)
};



$(document).ready(function() {
	var baseUi = {
		init: function () {
			baseUi.resize();
			baseUi.pdFav();
			baseUi.notice();
			baseUi.tab();
			baseUi.btnAco();
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
			$(".m_sound").on('click', function(e){ 
				$(".m_sound").toggleClass('off');
			});
		},
		pdFav: function (e) {
			$(".pd_pav a").on('click', function(e){ 
				$(this).toggleClass('act');
			});
			$(window).on("load",function(){
				$(".list_body ul").mCustomScrollbar({
					theme:"dark-thin",
					scrollInertia: 200,
				});
			});
		},
		btnAco: function (e) {
			$(".btn_aco").on('click', function(e){ 
				$(this).parent('li').toggleClass('act');
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
					$('select').selectric(); //selectric ui load
				};
			})));
		},
		tab : function(e){
			$('.tab_list ul > li').each(function(i){this.i=i}).click(function(){
				var idx = $(this).index();
				$(this).parents().find(".tab_list li a").removeClass('act');
				$(this).parents().find(".tab_area").hide();
				$(this).parents().find(".tab_area:eq("+idx+")").show();
				$(this).find('a').addClass('act');
			});
		}
	}
	baseUi.init();
});