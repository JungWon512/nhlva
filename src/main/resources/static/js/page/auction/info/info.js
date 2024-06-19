$(function() {
    var setLayout = function() {
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

    var setBinding = function() {
    };

    setLayout();    
    setBinding();
});