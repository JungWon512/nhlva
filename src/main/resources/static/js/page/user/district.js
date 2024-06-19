;(function (win, $,COMMONS) {
// Class definition
	var District = function () {

		var addEvent = function(){
			$(".banner_box ul li").click(function(){
				moveExUrl($(this).attr('moveUrl'));
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
