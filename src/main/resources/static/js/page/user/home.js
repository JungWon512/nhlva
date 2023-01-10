;(function (win, $,COMMONS) {
// Class definition
	var Home = function () {

		var addEvent = function(){
			$(".banner_box ul").slick({
				dots: false,
				adaptiveHeight: true,
				arrows:false
			});
		};

		return {
			init: function () {
				if(chkOs() != 'web') $('.gps_fix').show();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header .side_menu .notice').hide();
				$('.m_header a').hide();
				addEvent();
				
//				if ($("#deviceMode").val() == "MOBILE") {
//					getNearestBranch();
//				}
			}
		};
	}();

	jQuery(document).ready(function () {
		Home.init();
	});
})(window, window.jQuery);
