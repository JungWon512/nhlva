;(function (win, $, COMMONS) {
// Class definition
	var Entry = function () {

		var clickEvent = (function() {
			if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
				return 'touchstart';
			}
			else {
				return 'click';
			}
		})();
		
		var addEvent = function(){
			// 바코드 검색
			$(".btn_search").on(clickEvent, function(){
				$("form[name='frm']").attr("action", "/admin/task/entry").submit();
			});
			
			// 리스트 선택
			$(".list_body > ul > li").on(clickEvent, function(){
					$(".list_body > ul > li").siblings().not(this).removeClass("active");
					$(this).toggleClass("active");
			});
			
			// 변경 팝업
			$(".btn_modify").on(clickEvent, function(){
				if ($(".list_body > ul").find("li.active").length == 0) {
					alert("변경할 대상을 선택하세요.");
				}
				
				var amnno = $(".list_body > ul").find("li.active").find("dd.num").data("amnno");
				var aucObjDsc = $(".list_body > ul").find("li.active").find("dd.num").data("aucObjDsc");
				var ledSqno = $(".list_body > ul").find("li.active").find("dd.num").data("ledSqno");
				var oslpNo = $(".list_body > ul").find("li.active").find("dd.num").data("oslpNo");
				
				var params = {
					aucDt : $("input[name='aucDt']").val(),
					regType : $("input[name='regType']").val(),
					aucObjDsc : aucObjDsc,
					oslpNo : oslpNo,
					ledSqno : ledSqno,
					amnno : amnno
				}
				
				$.ajax({
					url: '/admin/task/cowInfo',
					data: JSON.stringify(params),
					type: 'POST',
					dataType: 'json',
					beforeSend: function (xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					success : function() {
					},
					error: function(xhr, status, error) {
					}
				}).done(function (body) {
					var success = body.success;
					var message = body.message;
					if (!success) {
						alert(message);
					}
					else {
						// layer popup 그리기
						fnLayerPop(body.params, body.cowInfo);
					}
				});
			});
			
			var fnLayerPop = function(params, cowInfo) {
				console.log(params, cowInfo);
				modalPopupClose('.popup .modal-wrap.pop_exit_cow');
				$('.popup .modal-wrap.pop_exit_cow').remove();
				var sHtml=''; 
				
				sHtml += '  <div id="" class="modal-wrap pop_exit_cow">                                                                                        ';
				sHtml += '  	<div class="modal-content">                                                                                                    ';
				sHtml += '  		<button class="modal_popup_close" onclick="modalPopupClose(\'.pop_exit_cow\');return false;">닫기</button>                   ';
				sHtml += '  		<h3>출장우 상세</h3>                                                                                                       ';
				sHtml += '  		<div class="cow-table">                                                                                                    ';
				sHtml += '  			<table>                                                                                                                ';
				sHtml += '  				<colgroup>                                                                                                         ';
				sHtml += '  					<col width="20%">                                                                                              ';
				sHtml += '  					<col width="30%">                                                                                              ';
				sHtml += '  					<col width="20%">                                                                                              ';
				sHtml += '  					<col width="*">                                                                                                ';
				sHtml += '  				</colgroup>                                                                                                        ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>경매<br>번호</th>                                                                                          ';
				sHtml += '  					<th>출하주</th>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>지역</th>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>생년월일</th>                                                                                              ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>개체번호</th>                                                                                              ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>KPN</th>                                                                                                   ';
				sHtml += '  				</tr>                                                                                                              ';			
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>중량</th>                                                                                                  ';
				sHtml += '  					<th>성별</th>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>최저가</th>                                                                                                ';
				sHtml += '  					<th>어미</th>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th>산차</th>                                                                                                  ';
				sHtml += '  					<th>계대</th>                                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  				<tr>                                                                                                               ';
				sHtml += '  					<th class="vtt">특이사항</th>                                                                                  ';
				sHtml += '  				</tr>                                                                                                              ';
				sHtml += '  			</table>                                                                                                               ';
				sHtml += '  		</div>                                                                                                                     ';
				sHtml += '  	</div>                                                                                                                         ';
				sHtml += '  </div>                                                                                                                             ';
				$('.popup').append($(sHtml));
				modalPopup('.popup .modal-wrap.pop_exit_cow');
				}
		};

		return {
			// public functions
			init: function () {
				addEvent();
			}
		};
	}();

	jQuery(document).ready(function () {
		Entry.init();
	});
})(window, window.jQuery);
