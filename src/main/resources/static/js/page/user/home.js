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
			
			//시스템 점검 공지 안내 팝업 닫기 버튼 event
			$(".pop_cow_input .modal_popup_close").unbind("click").click(function(){
				modalPopupClose('.pop_cow_input');
				$(".pop_cow_input").remove();
			});
		};
		
		return {
			init: function () {
				if(chkOs() != 'web') $('.gps_fix').show();
				$('.w_header_inner .w_header nav[id="nav"] ul li').hide();
				$('.w_header .side_menu .login').hide();
				$('.w_header .side_menu .notice').hide();
				$('.m_header a').hide();
				
//				if ($("#deviceMode").val() == "MOBILE") {
//					getNearestBranch();
//				}

				var today = new Date();
				var chkStDate = new Date(2023, (2-1), 11, 15, 00, 00);		//점검시작일자 : 2023.02.11. 15:00:00
				var chkEdDate = new Date(2023, (2-1), 13, 00, 00, 00);		//점검종료일자 : 2023.02.13. 00:00:00
				
				//시스템 점검일자 시간에만 팝업 띄우도록 함
				if(today >= chkStDate &&  today < chkEdDate){
					var popHtml = [];
					popHtml.push("<div id='chg_result' class='modal-wrap pop_cow_input'>");
					popHtml.push("		<div class='modal-content pop_ad_mod'>");
					popHtml.push("			<div class='modal-head'>");
					popHtml.push("				<h3>시스템 점검 공지 안내</h3>");
					popHtml.push("				<button type='button' class='modal_popup_close'>닫기</button>");
					popHtml.push("			</div>");
					popHtml.push("			<div class='modal-body'>");
					popHtml.push("				<ul style='padding:12px;margin-top:-15px;'>");
					popHtml.push("					<li style='font-size:16px;margin-bottom:10px;'>스마트 가축시장 시스템 점검을 안내합니다.</li>");
					popHtml.push("					<li style='font-size:16px;line-height:1.5em;font-weight:bold;'>작업일시 : 2월 11일 15:00 ~ 2월 12일 24:00</li>");
					popHtml.push("					<li style='font-size:16px;line-height:1.5em;font-weight:bold;margin-bottom:10px;'>작업내용 : 시스템 점검</li>");
					popHtml.push("					<li style='font-size:16px;margin-bottom:10px;'>※ 위 기간 동안 제공되는 정보는 오류가 있을 수 있으니 서비스 이용을 제한 바랍니다.</li>");
					popHtml.push("					<li style='font-size:16px;'>감사합니다.</li>");
					popHtml.push("				</ul>");
					popHtml.push("			</div>");
					popHtml.push("		</div>");
					popHtml.push("</div>");
					
					$("body").append(popHtml.join(""));
					modalPopup(".pop_cow_input");
				}
				
				addEvent();
			}
		};
	}();
		
	jQuery(document).ready(function () {
		Home.init();
	});
})(window, window.jQuery);
