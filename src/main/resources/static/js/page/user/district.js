;(function (win, $,COMMONS) {
// Class definition
	var District = function () {

		var addEvent = function(){
			$(".banner_box ul li").click(function(){
				moveExUrl($(this).attr('moveUrl'));						
			
				var param = {
					banner_file_path: $('.banner_box img:visible').attr('src')
					,pgid : 'district'
					,url_nm: $(this).attr('moveUrl')
					,proc_flag:"C"
					,device_type:($('.banner_box img.pc_banner').is(':visible')?'PC':'MO')
				};				
				commonAdsLog(param);	
			});
			$(".banner_box ul").slick({
				dots: true,
				adaptiveHeight: true,
				arrows:false,
			    autoplay: true,
			    autoplaySpeed: 2000
			});	
		};

		return {
			init: function () {
				$('.m_header a').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header .side_menu .notice').hide();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();
				if($('ul.choice_area li').length <= 6){
					$('.btn_more').hide();
				}
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		District.init();
	});
})(window, window.jQuery);
