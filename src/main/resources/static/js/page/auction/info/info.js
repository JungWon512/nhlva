$(function() {
    var setLayout = function() {
		$(".banner_box ul li").click(function(){
			moveExUrl($(this).attr('moveUrl'));
			
			var param = {
				banner_file_path: $('.banner_box img:visible').attr('src')
				,pgid : 'info'
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

    var setBinding = function() {
    };

    setLayout();    
    setBinding();
});