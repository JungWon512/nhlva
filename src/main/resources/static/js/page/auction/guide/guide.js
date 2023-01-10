$(function() {
    

    var setLayout = function() {
		if(window.location.search.indexOf('place') < 0){
			if (window.location.search.indexOf("?dashYn=Y") < 0) {
				$('.m_header a').hide();
				$('.w_header .side_menu .notice').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();						
			} else {
				$('.m_header a').hide();
				$('.w_header .side_menu .notice').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();
				$('.m_backTit > a.m_back').show();
			}
		}
    };

    var setBinding = function() {
    };

    setLayout();
    
    setBinding();
});

