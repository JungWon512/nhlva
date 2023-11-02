;(function (win, $,COMMONS) {
// Class definition
	var Home = function () {

		var addEvent = function(){
			$(".banner_box ul").slick({
				dots: true,
				adaptiveHeight: true,
				arrows:false,
			    autoplay: true,
			    autoplaySpeed: 2000
			});
			
		};
		
		var searchNoticeList = function(){
			
			const params = {	}
			window.auction.commons.callAjax("/home/notice", "post", params, 'application/json', 'json', function(data){
				console.log(data);
				if(data && data.result?.length > 0){
					var noticeInfo = data.result[0];
					var cntn = noticeInfo.BBRD_CNTN;
			        cntn = cntn.replace(/&lt;/gi  , "<").replace(/&gt;/gi  , ">");
					var popHtml = [];
					popHtml.push("<div id='chg_result' class='modal-wrap pop_cow_input'>");
					popHtml.push("		<div class='modal-content pop_ad_mod'>");
					popHtml.push("			<div class='modal-head'>");
					popHtml.push("				<h3>"+noticeInfo.BBRD_TINM+"</h3>");
					popHtml.push("				<button type='button' class='modal_popup_close'>닫기</button>");
					popHtml.push("			</div>");
					popHtml.push("			<div class='modal-body'>");
					popHtml.push("				<ul style='padding:12px;margin-top:-15px;'>");
					popHtml.push("					<li style='font-size:16px;margin-bottom:10px;'>"+cntn+"</li>");
					popHtml.push("				</ul>");
					popHtml.push("			</div>");
					popHtml.push("		</div>");
					popHtml.push("</div>");
					
					$("body").append(popHtml.join(""));
					modalPopup(".pop_cow_input");
					
					//시스템 점검 공지 안내 팝업 닫기 버튼 event
					$(document).find(".pop_cow_input .modal_popup_close").unbind("click").click(function(){
						modalPopupClose('.pop_cow_input');
						$(".pop_cow_input").remove();
					});
				}
			});
		}
		
		return {
			init: function () {
				if(chkOs() != 'web') $('.gps_fix').show();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header .side_menu .notice').hide();
				$('.m_header a').hide();
				
				searchNoticeList();
				addEvent();
			}
		};
	}();
		
	jQuery(document).ready(function () {
		Home.init();
	});
})(window, window.jQuery);
