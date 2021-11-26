$(function() {
	
	var COMMONS = window.auction["commons"];
	
	var setLayout = function() {
		if(!isApp() && chkOs() == 'web'){
			$('input[name=bidAmt]').attr('readonly',false);
			$('input[name=bidAmt]').on('input',function(){
				inputNumberVaildBidAmt(this,5);
			});
			$('input[name=bidAmt]').on('keyup',function(e){
				if (e.keyCode == 13 && fnCheckBidData()) {
					fnBid();
				}				
			});
			$('input[name=aucNum]').attr('readonly',false);
			$('input[name=aucNum]').on('input',function(){
				inputNumberVaildBidAmt(this,5);
			});
			$('input[name=aucNum]').on('keyup',function(e){
				if (e.keyCode == 13) {
					fnSetAuctionInfo();
				}
			});
			$('input[name=aucNum]').focus();
		}
		$('.chart').easyPieChart({
			barColor: '#007eff',
			trackColor: '#dbdbdb',
			lineCap: 'round',
			lineWidth: 18,
			size: 344,
			animate: 1000
		});		
		
		$(".seeBox_slick ul.slider").slick({
			dots: true,
			adaptiveHeight: true
		});

		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			$('video[id^=remoteVideo]').each(function(){
				if(!$('.m_sound').hasClass('off') && $(this).attr('id') == 'remoteVideo1') return;
				$(this).get(0).muted = true;
				if(!$(this).get(0).paused) $(this).get(0).pause();
			});
			var callback = function(currentSlide){
			var video =$('#remoteVideo'+currentSlide).get(0);
			if($('#remoteVideo1').get(0) && currentSlide == 1 && !$('.m_sound').hasClass('off')){
				$('#remoteVideo1').get(0).muted = false;
			}
			if(video && video.paused) video.play();
			}
			setRemonJoinRemote(currentSlide,callback);
		});
	};
	
	var clickEvent = (function() {
		if ('ontouchstart' in document.documentElement === true && navigator.userAgent.match(/Android/i)) {
			return 'touchstart';
		}
		else {
			return 'click';
		}
	})();

	var setBinding = function() {
		// 관전 영상 음소거
		$(".m_sound").on(clickEvent, function(e){
			if($('video[id^=remoteVideo]').get(0)){
				var mute = $('video[id^=remoteVideo]').get(0).muted;
				$('video[id^=remoteVideo]').get(0).muted = !mute;
				if(!$(this).get(0).paused) $('video[id^=remoteVideo]').get(0).play();
				else $('video[id^=remoteVideo]').get(0).pause();
			}
			$(".m_sound").toggleClass('off');
		});
		
		// 숫자 입력
		$("button.num").on(clickEvent, function(){
			var maxLength = 5;
			if (auctionConfig.seData && auctionConfig.seData.clearYn && auctionConfig.seData.clearYn == "Y") {
				$("input[name='bidAmt']").val("");
			}
			var num = $(this).val();
			var $target = $(".calculator_top").find("input.active");
			var inputVal = $target.val().split(",").join("");
			if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
			if (inputVal == "" && num == "0") return;
			if (inputVal.length >= maxLength) return;
			$target.removeClass("txt-blue");
			$target.val(("" + inputVal + num).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
			auctionConfig.seData.clearYn = "N";	
		});
		
		// 입력 숫자 삭제
		$("button.num_back").on(clickEvent, function(){
			var $target = $(".calculator_top").find("input.active");
			var inputVal = $target.val().split(",").join("");
			$target.val(inputVal.substring(0, inputVal.length-1).replace(/\B(?=(\d{3})+(?!\d))/g, ","))
			if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
		});
		
		// 경매 예정, 결과, 응찰내역 조회 팝업
		$(".btn_popup").on(clickEvent, function(){
			COMMONS.callAjax("/pop/entryList"
							, "post"
							, {naBzPlcNo : $("#naBzPlcNo").val(), place : $("#naBzPlcNo").val()}
							, 'application/json'
							, 'html'
							, function(data){
								$(".pop_auction").html("");
								$(data).appendTo($(".pop_auction"));
								modalPopup('.pop_auction');
							});	
		});
		
		// 경매 예정, 결과, 응찰내역 탭 이동
		$(document).on(clickEvent, ".btnTabMove", function(){
			if ($(this).hasClass("act")) return;
			$(".btnTabMove").removeClass("act");
			$(this).addClass("act");
			$(".tab_area").hide();
			var tabId = $(this).data("tabId");
			$("." + tabId + "_area").show();
		});

		// 찜하기 팝업
		$(document).on(clickEvent, ".pd_pav a", function(){
			$('.popup .modal-wrap.pop_jjim_input.zim').remove();
			var li = $(this).closest('li');
			
			var jjim_price = li.find('input.sbidUpr').val();
			var naBzPlc = li.find('input.naBzPlc').val();
			var aucDt = li.find('input.aucDt').val();
			var aucObjDsc = li.find('input.aucObjDsc').val();
			var oslpNo = li.find('input.oslpNo').val();
			var aucPrgSq = li.find('input.aucPrgSq').val();
			var lowsSbidLmtUpr = li.find('input.lowsSbidLmtUpr').val();
			var place = $('#naBzPlcNo').val();

			var div = $('.popup');
			var sHtml="";
			sHtml += '<div id="" class="modal-wrap pop_jjim_input zim"">';
			sHtml += '	<div class="modal-content"> ';
			sHtml += '		<h3>찜가격</h3>';
			sHtml += '		<button class="modal_popup_close" onclick="modalPopupClose(\'.popup .modal-wrap.pop_jjim_input.zim\');">닫기</button>';
			sHtml += '		<form name="frm_zim" method="post">';
			sHtml += '			<input type="hidden" name="naBzPlc" value="'+naBzPlc+'"/> ';
			sHtml += '			<input type="hidden" name="aucDt" value="'+aucDt+'"/> ';
			sHtml += '			<input type="hidden" name="aucObjDsc" value="'+aucObjDsc+'"/> ';
			sHtml += '			<input type="hidden" name="oslpNo" value="'+oslpNo+'"/> ';
			sHtml += '			<input type="hidden" name="aucPrgSq" value="'+aucPrgSq+'"/> ';
			sHtml += '			<input type="hidden" name="lowsSbidLmtUpr" value="'+lowsSbidLmtUpr+'"/>';
			sHtml += '			<input type="hidden" name="place" value="'+place+'"/>';
			sHtml += '			<input type="hidden" name="oldJjimPrice" value="'+jjim_price+'"/> ';
			sHtml += '			<dl class="jjim_dl">';
			sHtml += '				<dt>';
			sHtml += '					<p>경매번호 : ' + aucPrgSq + '번 <span>|</span>최저가 : ' + fnSetComma(lowsSbidLmtUpr) + '</p>';
			sHtml += '				</dt>';
			sHtml += '				<dd>';
			sHtml += '					<input type="text" name="inputUpr" id="inputUpr" oninput="inputNumberVaild(this, 5)" value="'+jjim_price+'" placeholder="찜가격 입력" pattern="\d*" inputmode="numeric" />';
			sHtml += '				</dd>';
			sHtml += '			</dl>';
			sHtml += '			<div class="btn_area"> ';
			sHtml += '				<button type="button" class="btn_cancel" onclick="return false;">찜삭제</button>';
			sHtml += '				<button type="button" class="btn_ok">찜하기</button>';
			sHtml += '			</div>';
			sHtml += '		</form>';
			sHtml += '	</div> ';
			sHtml += '</div>';
			
			div.append($(sHtml));
			modalPopup('.popup .modal-wrap.pop_jjim_input.zim');
		});
		
		// 찜하기 이벤트
		$(document).on(clickEvent, ".pop_jjim_input .btn_ok", function(){
			var inputUpr = $(".pop_jjim_input").find("input[name=inputUpr]").val() == "" ? 0 : $(".pop_jjim_input").find("input[name=inputUpr]").val();
			var lowsSbidLmtUpr = $(".pop_jjim_input").find("input[name=lowsSbidLmtUpr]").val() == "" ? 0 : $(".pop_jjim_input").find("input[name=lowsSbidLmtUpr]").val();
			if(inputUpr && parseInt(lowsSbidLmtUpr) <= parseInt(inputUpr)) {
//				modalComfirm(""
//							,"찜가격을 저장하시겠습니까?"
//							, function() {
//							});

					COMMONS.callAjax("/auction/api/inserttZimPrice"
						, "post"
						, $("form[name=frm_zim]").serializeObject()
						, 'application/json'
						, 'json'
						, function(data){
							modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
							if(data && !data.success){
								modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
								return;
							}
							COMMONS.callAjax("/pop/entryList"
								, "post"
								, {naBzPlcNo : $("#naBzPlcNo").val(), place : $("#naBzPlcNo").val(), tabAct : $('div.entryList div.tab_list ul li a.act').data('tabId')}
								, 'application/json'
								, 'html'
								, function(data){
									$(".pop_auction").html("");
									$(data).appendTo($(".pop_auction"));
									modalPopup('.pop_auction');
								});
						});
				return;
			}
			else {
				modalAlert("", "최저가 이상의 가격을 입력해주세요.");
				return;
			}
		});
		
		// 찜삭제 이벤트
		$(document).on(clickEvent, ".pop_jjim_input .btn_cancel", function(){
			var oldJjimPrice = $('.modal-wrap.pop_jjim_input.zim input[name=oldJjimPrice]').val();
			if(!oldJjimPrice || oldJjimPrice == 0){
				modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
				return;
			}
			
			modalComfirm(""
						,"찜가격을 삭제하시겠습니까?"
						, function() {
							COMMONS.callAjax("/auction/api/deleteZimPrice"
								, "post"
								, $("form[name=frm_zim]").serializeObject()
								, 'application/json'
								, 'json'
								, function(){
									modalPopupClose('.popup .modal-wrap.pop_jjim_input.zim');
									COMMONS.callAjax("/pop/entryList"
										, "post"
										, {naBzPlcNo : $("#naBzPlcNo").val(), place : $("#naBzPlcNo").val(), tabAct : $('div.entryList div.tab_list ul li a.act').data('tabId')}
										, 'application/json'
										, 'html'
										, function(data){
											$(".pop_auction").html("");
											$(data).appendTo($(".pop_auction"));
											modalPopup('.pop_auction');
										});
							});
						});
			return;
		});
		
		// 경매 번호 입력
		$(".btn_confirm").on(clickEvent, function(){
			fnSetAuctionInfo();
		});
		
		// 경매 번호 입력화면 이동
		$(".btn_before").on(clickEvent, function(){
			fnBefore();
		});
		
		// 경매 입찰
		$(".btn_bid").on(clickEvent, function(){
			if (fnCheckBidData()) {
				fnBid();
			}
			if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
		});
		
		// 입찰 취소
		$(".btn_bid_cancel").on(clickEvent, function(){
			fnBidCancel();
			if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
		});
		
		socketStart();
		setRemon();
	};

	setLayout();
	setBinding();
});

var inputNumberVaild = function(el,len){
	if(el.value.length > len) {
		el.value = el.value.substr(0, len);
	}
}

// 소켓통신 connect 및 이벤트 바인딩 [s]
var socket = null, auctionConfig = {seData : {}};
var socketStart = function(){
	if(!$('#naBzPlc').val()) return;
	if(socket){ socket.connect(); return;}
	
	var socketHost = (active == 'production')?"cowauction.kr":(active == 'develop')?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
	//socketHost += ':'+$('#webPort').val();
	socketHost += ':9001';
	socket = io.connect('https://'+socketHost + '/6003' + '?auctionHouseCode='  + $('#naBzPlc').val(), {secure:true});
	if (active == "local") {
		socket = io.connect('https://xn--e20bw05b.kr:9001/6003?auctionHouseCode=' + $('#naBzPlc').val(), {secure:true});
//		socket = io.connect('http://192.168.0.34:9001/6003?auctionHouseCode=' + $('#naBzPlc').val());
	}

//	socket.on('connect_error', socketDisconnect);
	
	socket.on('connect', connectHandler);

	socket.on('disconnect', disconnectHandler);

	// 경매 서버 접속 정보 유효 확인
	socket.on('AuctionCheckSession', messageHandler);

	// 접속 정보 인증 결과 수신
	socket.on('ResponseConnectionInfo', messageHandler);

	// 경매 시작/종료 카운트 다운 정보 수신
	socket.on('AuctionCountDown', messageHandler);
	
	// 메시지 수신
	socket.on('ToastMessage', messageHandler);
	
	// 관심출품 정보 수신
	socket.on('FavoriteEntryInfo', messageHandler);
	
	// 서버 응답 코드 수신
	socket.on('ResponseCode', messageHandler);

	// 현재 춤품 자료 수신
	socket.on('CurrentEntryInfo', messageHandler);
	
	// 접속자 정보 수신
	socket.on('BidderConnectInfo', messageHandler);
	
	// 경매 상태 정보 수신
	socket.on('AuctionStatus', messageHandler);
	
	// 낙/유찰 결과 수신
	socket.on('AuctionResult', messageHandler);
	
	// 응찰 취소 요청
	socket.on('CancelBidding', messageHandler);
	
	// 출품 정보 노출 수신
	socket.on('ShowEntryInfo', messageHandler);
	
	// 재경매 대상 수신
	socket.on('RetryTargetInfo', messageHandler);
	
	// 경매 응찰 종료 상태 전송 처리
	socket.on('AuctionBidStatus', messageHandler);
	
	// 응찰 정보 수신
	socket.on('ResponseBiddingInfo', messageHandler);
}
//소켓통신 disconnect Event
//var socketDisconnect = function(){
//	//sFlag = true;
//	socket.disconnect();
//	modalAlert('', "경매가 종료되었습니다."
//			 , function(){pageMove('/main', false);}
//	);
//}

//소켓통신 connect시 Event
var connectHandler = function() {
	// 구분자|조합구분코드|거래인관리번호|인증토큰|접속요청채널|사용채널
	var packet = [];
	packet.push("AI");
	packet.push($('#naBzPlc').val());
	packet.push($("#trmnAmnno").val());
	packet.push(getCookie('access_token'));
	packet.push("6001|WEB");
	socket.emit('packetData', packet.join('|'));
}

var disconnectHandler = function() {
	if(socket.connected) {
		socket.disconnect();
	};
	if(!sFlag) modalAlert('','서버와 연결이 끊어졌습니다.'
			 , function(){pageMove('/main', false);});
}

//소켓통신 수신시 제어하는 로직
/** 
	AR : 경매상태정보
	AS : 현재 경며정보
	SC : 현재 출품정보
	CF : 낙찰데이터 정보	
**/
var messageHandler = function(data) {
	debugConsole("messageHandler : ", data);
	var dataArr = data.split('|');
	var gubun = dataArr[0];

	switch(gubun){
		case "AF" :
			// 출품 경매 최종 결과 전송
			// 구분자 | 조합구분코드 | 출품번호 | 낙/유찰결과코드(11/22/23/24) | 낙찰자회원번호(거래인번호) | 낙찰자경매참가번호 | 낙찰금액
			// 11. 대기, 22. 낙찰, 23. 보류, 24. 경매취소
			if(!auctionConfig.afData) auctionConfig.afData = {};
			auctionConfig.afData.status = dataArr[3];

			// 출품 번호 체크
			if (auctionConfig.arData.naBzplc != dataArr[1]
			 || auctionConfig.asData.aucSeq != dataArr[2]) return;
			 
			var tmpAsDAta = { 
				aucPrgSq: dataArr[2]
				, selSts: dataArr[3]=='22'?'낙찰':(dataArr[3]=='23'?'유찰':dataArr[3]=='11'?'대기':'')
				, sraSbidAm: dataArr[6]
			};
			var tr = getTrRow(tmpAsDAta.aucPrgSq);
			tr.find('dl dd.selSts').text(tmpAsDAta.selSts);

			// 낙찰인 경우
			if (auctionConfig.afData.status == "22") {
				var amt = dataArr[6];
				if (auctionConfig.arData.trmnAmnno == dataArr[4]
				&& auctionConfig.arData.entryNum == dataArr[5]) {
					$("div.auc-txt > div.info_board").html(fnSetComma(amt) + "만 원 <span class='txt-green'>낙찰</span>");
				}
				else {
					amt = amt.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
					$("div.auc-txt > div.info_board").html("<span class='txt-green'>낙찰금액 " + fnSetComma(amt) + "만 원 / " + dataArr[5] + "번</span>");
				}
				tr.find('dl dd.sraSbidAm').text(Math.round(tmpAsDAta.sraSbidAm));
			}
			else if (auctionConfig.afData.status == "23") {
				$("div.auc-txt > div.info_board").html("<span class='txt-green'>유찰</span>되었습니다.");
			}
			else if (auctionConfig.afData.status == "24") {
				$("div.auc-txt > div.info_board").html("경매 <span class='txt-yellow'>대기 중</span>입니다.");
			}
			
			auctionConfig.seData.clearYn = "Y";
			auctionConfig.retryYn = null;
			break;
		case "AN" :
			// 재경매 대상 수신
			// 구분자 | 조합구분코드 | 출품번호 | 대상자참여번호1,대상자참여번호2,대상자참여번호3….대상자참여번호n

			// 출품 번호 체크
			if (auctionConfig.arData.naBzplc != dataArr[1]
			 || auctionConfig.asData.aucSeq != dataArr[2]) return;

			auctionConfig.retryYn = "Y";
			auctionConfig.retryTargetYn = "N";

			var retryTarget = (dataArr[3] == null ? "" : dataArr[3]).split(',');
			if (retryTarget.indexOf(auctionConfig.arData.entryNum) > -1) {
				$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>재응찰</span>하시기 바랍니다.");
				auctionConfig.retryTargetYn = "Y";
			}
			else {
				$("input[name='bidAmt']").val("");
				$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>재경매</span> 진행 중입니다.");
			}
			break;
		case "AP" :
			// 응찰 정보 수신
			// 구분자 | 조합구분코드 | 거래인관리번호 | 출품번호 | 응찰금액(만원) | 응찰시간(yyyyMMddhhmmssSSS)
			if (auctionConfig.arData.naBzplc != dataArr[1]
			 || auctionConfig.scData.curAucSeq != dataArr[3]) return;
			
			if (dataArr[4] != "null") {
				$("input[name=bidAmt]").val(dataArr[4].replace(/\B(?=(\d{3})+(?!\d))/g, ","));
			}
			break;
		case "AR" :
			// 접속 정보 인증 응답
			// 구분자 | 조합구분코드 | 접속결과코드 | 거래인관리번호 | 경매참가번호
			if (!auctionConfig.arData) auctionConfig.arData = {};
			auctionConfig.arData.naBzplc = dataArr[1];
			auctionConfig.arData.status = dataArr[2];
			auctionConfig.arData.trmnAmnno = dataArr[3];
			auctionConfig.arData.entryNum = dataArr[4];
			if(auctionConfig.arData.status != '2000') {
				var msg = "";
				if (dataArr[2] == "2001") msg = "경매를 참여하실 수 없습니다.</br>경매 참여를 원하실 경우 관리자에게 문의하세요."
				else if (dataArr[2] == "2002"){ sFlag = true;msg = "이미 접속한 사용자가 있습니다.";}
				else {	sFlag = true;msg = "현재 참여 가능한 경매가 없습니다.</br>관리자에게 문의하세요.";}
				modalAlert('', msg
						 , function(){pageMove('/main', false);}
				);
			}
			else {
				$("b.join-num").find("span").text(dataArr[4]);
				$(".calculator_head_pc b.join-num").text(dataArr[4]);
				setLoopJoinEvent();
			}
			break;
		case "AS" :
			// 현재 경매상태
			// 구분자 | 조합구분코드 | 출품번호 | 경매회차 | 시작가 | 현재응찰자수 | 경매상태 | 1순위회원번호 | 2순위회원번호 | 3순위회원번호 | 경매진행완료출품수 | 경매잔여출품수
			// 8001. NONE(기본값-미진행), 8002. READY(대기), 8003. START(시작), 8004. PROGRESS(진행)
			// 8005. PASS(정지), 8006. COMPLETED(완료), 8007. FINISH(종료)
			if(!auctionConfig.asData) auctionConfig.asData = {};
			auctionConfig.asData.status = dataArr[6];
			auctionConfig.asData.aucSeq = dataArr[2];

			if (auctionConfig.asData.status == "8001") {
				$("input[name='bidAmt']").val("");
				modalAlert('', "진행중인 경매가 없습니다."
						 , function(){pageMove('/main', false);}
				);
			}
			else if(auctionConfig.asData.status == "8002" || auctionConfig.asData.status == "8006") {
				$("input[name='bidAmt']").val("");
				toast("경매 <span class='txt-green'>대기 중</span>입니다.", 60*60*24);
				$("div.auc-txt > div.info_board").html("경매 <span class='txt-yellow'>대기 중</span>입니다.");
				auctionConfig.enableBid = "N";
			}
			else if(auctionConfig.asData.status == "8003") {
				$("input[name='bidAmt']").val("");
				$("#toast").removeClass("reveal");
				fnBefore();
				auctionConfig.asData.bidYn = "N";
				auctionConfig.enableBid = "Y";
			}
			else if(auctionConfig.asData.status == "8004") {
				$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>경매 번호</span>를 입력하세요.");
				$("#toast").removeClass("reveal");
				$("input[name='bidAmt']").val("");
				fnBefore();
				// 출품번호, 경매대상구분코드
				auctionConfig.enableBid = "Y";
			}
			else if(auctionConfig.asData.status == "8005") {
				auctionConfig.enableBid = "N";
			}
			else if(auctionConfig.asData.status == "8007") {
				modalAlert('', "<span style='color: #007eff;'>경매 종료</span>되었습니다.", function(){
					pageMove('/main', false);
				});
			}
			break;
		case "AY" :
			// 경매 응찰 종료 상태
			// 구분자 | 조합구분코드 | 경매번호 | 응찰상태코드(F/P)
			// F : 응찰 종료, P : 응찰 진행
			if(auctionConfig.asData.aucSeq != dataArr[2]) return;
			
			var bidStatus = dataArr[3];
			if (auctionConfig.asData
			&& (auctionConfig.asData.status == "8003" || auctionConfig.asData.status == "8004")) {
				if (bidStatus == "F") {
					$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>응찰</span> 종료되었습니다.");
					auctionConfig.enableBid = "N";
				}
				else if (bidStatus == "P") {
					if (auctionConfig.retryYn == "Y" && auctionConfig.retryTargetYn == "Y") {
						$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>재응찰</span>하시기 바랍니다.");
					}
					else if (auctionConfig.retryYn != "Y") {
						$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>응찰</span>하시기 바랍니다.");
					}
					auctionConfig.enableBid = "Y";
				}
			}
			break;
		case "SC" :
			// 현재 출품정보
			// 구분자 | 조합구분코드 | 출품번호 | 경매회차 | 경매대상구분코드(1.송아지, 2.비육우, 3.번식우) | 축산개체관리번호 | 축산축종구분코드 | 농가식별번호 | 농장관리번호 | 농가명 |
			// 브랜드명 | 생년월일 | KPN번호 | 개체성별코드 | 어미소구분코드 | 어미소축산개체관리번호 | 산차 | 임신개월수 | 계대 | 계체식별번호 |
			// 축산개체종축등록번호 | 등록구분번호 | 출하생산지역 | 친자검사결과여부 | 신규여부 | 우출하중량 | 최초최저낙찰한도금액 | 최저낙찰한도금액 | 비고내용 | 낙유찰결과 |
			// 낙찰자 | 낙찰금액 | 응찰일시 | 마지막출품여부 | 계류대번호 | 초과출장우여부
			if(!auctionConfig.scData) auctionConfig.scData = {};
				auctionConfig.scData.curAucSeq = dataArr[2];
				auctionConfig.scData.minBidAmt = dataArr[27];
				auctionConfig.scData.aucObjDsc = dataArr[4];
				
				// 관전 전광판 데이터 update
				$('dd.auctionNum', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[2]);		// 출품번호
				$('dd.ftsnm', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[9]);			// 출하주
				$('dd.sex', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[13]);			// 성별
				$('dd.cowSogWt', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[25]);		// 중량
				$('dd.matime', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[16]);			// 산차
				$('dd.mcowDsc', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[14]);		// 어미
				$('dd.sraIndvPasgQcn', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[18]);	// 계대
				$('dd.kpnNo', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[11] && dataArr[12].replace('KPN',''));	// KPN번호
				$('dd.lowsSbidLmtAm', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[27]); // 최저가 > 24 :최초낙찰 ,25:최저낙찰
				$('dd.rmkCntn, p.rmkCntn', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[28]);			// 비고
				$('dd.sraPdRgnnm, p.sraPdRgnnm', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[22]);	// 지역명
				$('dd.dnaYn', $(".seeBox_slick_inner, .mo_seeBox")).text(dataArr[23]);			// 친자여부
				
				convertDefaultValue($('dd', $(".seeBox_slick_inner, .mo_seeBox")));
				var tr = getTrRow(auctionConfig.scData.curAucSeq);
				tr.find('dl dd.ftsnm').text(nameEnc(dataArr[9]));
				tr.find('dl dd.cowSogWt').text(dataArr[25]);
				tr.find('dl dd.lowsSbidLmtAm').text(dataArr[27]);
				tr.find('dl dd.sraSbidAm').text(dataArr[31]+'');
				tr.find('dl dd.rmkCntn').text(dataArr[28]);
				tr.find('dl dd.selSts').text(dataArr[29]==11?'대기':dataArr[29]==22?'낙찰':dataArr[29]==23?'유찰':'대기');
				convertDefaultValue(tr.find('dl dd'));

				fnGetFavoritePrice(dataArr[2], dataArr[4]);
				$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>응찰 금액</span>을 입력하세요.");

				$(".aucNum").hide().find("input, button").prop("disabled", true).removeClass("active").val("");
				$(".bidAmt").show().find("input, button").prop("disabled", false).addClass("active").val("");
				$(".btn_before").prop("disabled", false);

				auctionConfig.enableBid = "Y";
				if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
			break;
		case "SD" :
			// 경매 시작/종료 카운트 다운 정보
			// 구분자 | 조합구분코드 | 상태구분(R : 준비 / C : 카운트다운 / F : 카운트다운 완료) | 카운트다운 시간(second)
			// 조합코드 체크
			if (auctionConfig.arData.naBzplc != dataArr[1]) return;

			var status = dataArr[2];
			if (status == "C") {
				var msgHtml = [];
				msgHtml.push("응찰 마감 <span class='txt-red txt-green'>" + dataArr[3] + "초 전</span>");
				msgHtml.push("<ol class='dot-count'>");
				msgHtml.push("<li class='" + (parseInt(dataArr[3]) >= 3 ? "dot-red" : "") + "'>1</li>");
				msgHtml.push("<li class='" + (parseInt(dataArr[3]) >= 2 ? "dot-red" : "") + "'>2</li>");
				msgHtml.push("<li class='" + (parseInt(dataArr[3]) >= 1 ? "dot-red" : "") + "'>3</li>");
				msgHtml.push("</ol>");
				$("div.auc-txt > div.info_board").html(msgHtml.join("\n"));
			}
			else if (status == "R") {
				$("div.auc-txt > div.info_board").html("경매 <span class='txt-yellow'>대기 중</span>입니다.");
			}
			break;
		case "SE" :
			// 요청 결과 미존재		4001
			// 요청 처리 실패		4002
			// 시작가 이하 응찰 시도	4003
			// 출품 이관 전 상태		4004
			// 응찰 취소 불가		4005
			// 정상 응찰 응답		4006
			// 정상 응찰 취소 응답	4007
			// 구분자 | 조합구분코드 | 예외 상황 코드
			if(!auctionConfig.seData) auctionConfig.seData = {};
			
			var responseCode = dataArr[2];
			if (responseCode == "4001") {
				modalAlert("", "출장우 정보가 없습니다.");
				$(".aucNum").val("");
			}
			else if (responseCode == "4002") {
				messageSample("현재 응찰 가능상태가 아닙니다.");
			}
			else if (responseCode == "4003") {
				messageSample("<span class='txt-red'>응찰금액을 확인하세요.</span>");
			}
			else if (responseCode == "4004") {
				modalAlert("", "현재 응찰 가능상태가 아닙니다.");
			}
			else if (responseCode == "4005") {
				modalAlert("", "현재 취소 가능상태가 아닙니다.");
			}
			else if (responseCode == "4006") {
				auctionConfig.seData.clearYn = "Y";
//				messageSample("<span class='txt-yellow'>응찰</span>되었습니다.");
				toast("<span class='txt-yellow'>응찰</span>되었습니다.", 1);
				fnBefore();
				$("input[name=bidAmt]").removeClass("txt-blue");
			}
			else if (responseCode == "4007") {
				auctionConfig.seData.clearYn = "Y";
				messageSample("<span class='txt-yellow'>응찰 취소</span>되었습니다.");
				fnBefore();
				$("input[name=bidAmt]").val("");
			}
			
			break;
		case "SH" :
			// 출품 정보 노출 설정
			// 구분자 | 조합구분코드  | 노출항목1 | 노출항목2 | 노출항목3 | 노출항목4 | 노출항목5 | 노출항목6 | 노출항목7 | 노출항목8
			// SH | 1100 | 1 | 3 | 5 | 7 | 9 | 2 | 11 | 8
			// 1. 출품번호(auctionNum), 2. 출하주(ftsnm), 3. 성별(sex), 4. 중량(cowSogWt), 5. 어미(mcowDsc), 6. 계대(sraIndvPasgQcn)
			// 7. 산차(matime), 8. KPN(kpnNo), 9. 지역명(sraPdRgnnm), 10. 비고(rmkCntn), 11. 최처가(lowsSbidLmtAm), 12. 친자여부(dnaYn)
			
			// 조합코드 체크
			if (auctionConfig.arData.naBzplc != dataArr[1]) return;

			var titleList = ["0", "auctionNum", "ftsnm", "sex", "cowSogWt", "mcowDsc", "sraIndvPasgQcn", "matime", "kpnNo", "sraPdRgnnm", "rmkCntn", "lowsSbidLmtAm", "dnaYn"];
			var slideTitleList = ["rmkCntn"];
			var showList = [];
			dataArr.forEach(function(info, idx){
				if (idx > 1 && info != "null" && info != "") showList.push(titleList[info]);
			});

			showList.forEach(function(name, idx){
				var className = (slideTitleList.indexOf(name) > -1) ? "p." + name : "dd." + name;
				$(".mo_seeBox:eq(0)").find(className).closest("li").show().insertBefore($(".mo_seeBox:eq(0)").find("li").eq(idx));
				$(".mo_seeBox:eq(1)").find(className).closest("li").show().insertBefore($(".mo_seeBox:eq(1)").find("li").eq(idx));
			});
			$(".mo_seeBox:eq(0)").find("li:gt(" + (showList.length > 9 ? 9 : (showList.length - 1)) + ")").hide();
			$(".mo_seeBox:eq(1)").find("li:gt(" + (showList.length > 9 ? 9 : (showList.length - 1)) + ")").hide();
			break;
		case "SS" :
			// 경매 서버에서 AuctionCheckSession을 받은 경우 AuctionResponseSession을 보낸다
			// 구분자 | 회원번호 | 접속요청채널 | 사용채널
			var num = $("#trmnAmnno").val();	// 회원번호
			var packet = 'SS|'+num+'|6001|WEB'
			socket.emit('packetData', packet);
		case "ST" :
			// 경매 응찰 참가자에게 경매 서버에서 받은 메시지를 보여준다
			// 구분자 | 조합구분코드 | 메시지 내용
			modalAlert('', dataArr[2]);
		default:break;
	}
}

// 경매 번호 입력
var fnSetAuctionInfo = function() {
	if (!auctionConfig.asData || !(auctionConfig.asData.status == "8003" || auctionConfig.asData.status == "8004")) {
		return;
	}
	
	if ($("input[name='auctionNum']").val() == "") {
		messageSample("경매 번호를 입력하세요.");
		return;
	}
	
	// 입력한 경매 번호로 출품 자료 정보를 요청한다.
	var reqData = [];
	reqData.push("AE");
	reqData.push(auctionConfig.arData.naBzplc);
	reqData.push(auctionConfig.arData.trmnAmnno);
	reqData.push($("input[name='aucNum']").val());
	socket.emit('packetData', reqData.join('|'));
	if(!isApp() && chkOs() == 'web') $('input[name=bidAmt]').focus();
	
	auctionConfig.seData.clearYn = "Y";
};

var fnBefore = function() {
	$("div.auc-txt > div.info_board").html("<span class='txt-yellow'>경매 번호</span>를 입력하세요.");
	$(".aucNum").show().find("input, button").prop("disabled", false).addClass("active").val("");
	$(".bidAmt").hide().find("input, button").prop("disabled", true).removeClass("active").val("");
	$(".btn_before").prop("disabled", true);
	$(".mo_seeBox").find("dd").text("");
	if(!isApp() && chkOs() == 'web') $('input[name=aucNum]').focus();
	auctionConfig.enableCancel == "N";
}

// 경매 요청 전 체크
var fnCheckBidData = function() {
	try {
		if (auctionConfig.enableBid != "Y") {
			messageSample("현재 응찰 가능상태가 아닙니다.");
			return false;
		}
		
		var bidAmt = $("input[name=bidAmt]").val() == "" ? "0" : $("input[name='bidAmt']").val().split(",").join("");
		if (bidAmt == "0") {
			messageSample("금액을 입력해주세요.");
			return false;
		}
		
		if (parseInt(auctionConfig.scData.minBidAmt) > parseInt(bidAmt)) {
			messageSample("<span class='txt-red'>응찰금액을 확인하세요.</span>");
			$("input[name=bidAmt]").val("");
			return false;
		}
	}
	catch(e) {
		return false;
	}
	return true;
};

// 응찰 정보 요청 후 경매서버에서 들어오는 응답(ResponseBiddingInfo)으로 응찰 여부 확인 후 응찰 프로세스 진행 
var fnBid = function() {
	// 응찰 정보 수신
	// 구분자 | 조합구분코드 | 거래인관리번호 | 출품번호 | 응찰금액(만원) | 응찰시간(yyyyMMddhhmmssSSS)
	var date = new Date();
	var bidDate = [date.getFullYear()
				  , (date.getMonth() + 1).toString().padStart("2", "0")
				  , date.getDate().toString().padStart("2", "0")
				  , date.getHours().toString().padStart("2", "0")
				  , date.getMinutes().toString().padStart("2", "0")
				  , date.getSeconds().toString().padStart("2", "0")
				  , "000"];
	
	var bidData = [];
	var bidAmt = $("input[name=bidAmt]").val() == "" ? "0" : $("input[name='bidAmt']").val().split(",").join("");
	bidData.push("AB");
	bidData.push(auctionConfig.arData.naBzplc);
	bidData.push("WEB");
	bidData.push(auctionConfig.arData.trmnAmnno);
	bidData.push(auctionConfig.arData.entryNum);
	bidData.push(auctionConfig.scData.curAucSeq);
	bidData.push(parseInt(bidAmt));
	bidData.push((auctionConfig.asData && auctionConfig.asData.bidYn == "N") ? "Y" : "N");
	bidData.push(bidDate.join(""));
	socket.emit('packetData', bidData.join("|"));
	auctionConfig.asData.bidYn = "Y";
	if(!isApp() && chkOs() == 'web') $('input[name=aucNum]').focus();
};

// 응찰 취소 요청
var fnBidCancel = function() {
	// 응찰 취소 요청
	// 구분자 | 조합구분코드 | 출품번호 | 경매회원번호(거래인번호) | 응찰자경매참가번호 | 접속채널(ANDROID/IOS/WEB) | 취소요청시간(yyyyMMddHHmmssSSS)
//	if (auctionConfig.enableCancel == "Y") {
		var date = new Date();
		var cancelDate = [date.getFullYear()
						 , (date.getMonth() + 1).toString().padStart("2", "0")
						 , date.getDate().toString().padStart("2", "0")
						 , date.getHours().toString().padStart("2", "0")
						 , date.getMinutes().toString().padStart("2", "0")
						 , date.getSeconds().toString().padStart("2", "0")
						 , "000"];
		
		var cancelData = [];
		cancelData.push("AC");
		cancelData.push(auctionConfig.arData.naBzplc);
		cancelData.push(auctionConfig.scData.curAucSeq);
		cancelData.push(auctionConfig.arData.trmnAmnno);
		cancelData.push(auctionConfig.arData.entryNum);
		cancelData.push("WEB");
		cancelData.push(cancelDate.join(""));
		socket.emit('packetData', cancelData.join("|"));
//	}
//	else {
		$(".aucNum").val("");
//	}
};

// 메시지 노출 샘플
var messageSamplePc = function(html, msg) {
	$("div.calculator_head_pc").find("div.auc-txt > div.info_board").hide();
	$("div.calculator_head_pc").find("div.auc-txt > div.message_board").html(html);
	$("div.calculator_head_pc").find("div.auc-txt > div.message_board").fadeToggle(500, function(){
		setTimeout(function(){
			if (msg) {
				$("div.calculator_head_pc").find("div.auc-txt > div.info_board").html(msg).show();
			}else {
				$("div.calculator_head_pc").find("div.auc-txt > div.info_board").show();
			}
			$("div.calculator_head_pc").find("div.auc-txt > div.message_board").html("").hide();
		}, 1500);
	});
};

var messageSample = function(html, msg) {
	$("div.auction_calculator").find("div.auc-txt > div.message_board").html(html);
	$("div.auction_calculator").find("div.auc-txt > div.message_board").css("z-index", "1000");
	setTimeout(function(){
		if (msg) {
			$("div.auction_calculator").find("div.auc-txt > div.info_board").html(msg);
		}
		$("div.auction_calculator").find("div.auc-txt > div.message_board").html("").css("z-index", "-1000");
	}, 1500);
	messageSamplePc(html, msg);
};

//출품정보 변경시 row변경 로직
var changeTrRow = function(tr) {
	$(".auction_see .list_body ul li").removeClass('act');
	if(tr){
		$(".list_body ul").mCustomScrollbar('scrollTo'
			,$(".list_body ul").find('.mCSB_container').find('li:eq('+tr.index()+')')
			,{scrollInertia:0}
		);
		tr.addClass('act');
	}
};

var getTrRow = function(curAucSeq) {
	return $('.auction_see .list_body li').filter(function(i,obj){
		var aucPrgSq = $(this).find('dl dd.aucPrgSq').text().trim();
		if(aucPrgSq==curAucSeq) return {obj:this,idx:i};
	});
};

var fnGetFavoritePrice = function(aucSeq, aucClass) {
	var params = {
		place : $("#naBzPlcNo").val(),
		naBzplc : $("#naBzPlc").val(),
		aucPrgSq : aucSeq,
		aucObjDsc : aucClass,
		trmnAmnno : $("#trmnAmnno").val(),
		lvstAucPtcMnNo : auctionConfig.arData.entryNum,
		groupYn : "Y"
	};
	
	$.ajax({
		url: "/my/favorite",
		type : 'POST',
		data : JSON.stringify(params),
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function(data) {
			if (data.success) {
				if (data.info.ATDR_AM > 0) {
					$("input[name=bidAmt]").removeClass("txt-blue").val(data.info.ATDR_AM);
					auctionConfig.enableCancel = "Y";
				}
				else if (data.info.SBID_UPR > 0) {
					$("input[name=bidAmt]").addClass("txt-blue").val(data.info.SBID_UPR);
					auctionConfig.enableCancel = "N";
				}
				else {
					auctionConfig.enableCancel = "N";
				}
			}
			else {
				$("input[name=bidAmt]").removeClass("txt-blue").val("");
			}
		}
	});
}
// 소켓통신 connect 및 이벤트 바인딩 [e]

// remon 영성 관련 로직 [s]
let dummyRemon,loop;
const serviceId =  $('#kkoSvcId').val();
const serviceKey = $('#kkoSvcKey').val();
	
var config = {
  	credential: {
    	key: serviceKey
    	, serviceId: serviceId
  	},view: {
		remote: '#remoteVideo'
	},  dev:{
		logLevel: 'SILENT'
	}
};
//remon Event listener
var listener = {
    onCreate(chid) { debugConsole(`EVENT FIRED: onCreate: ${chid}`); },
    onJoin(chid) { 
		debugConsole(`EVENT FIRED: onJoin: ${chid}`);
	},
    onClose() { 
		debugConsole('EVENT FIRED: onClose'); 
    },
    onError(error) { 
		debugConsole(`EVENT FIRED: onError: ${error}`);
    }, onStat(result) { 
	}
};

//remon관련 실행 로직
function setRemon(){	
	dummyRemon = new Remon({ config, listener });	
    setLoopJoinEvent();
}

//특정주기마다 castlist 목록 불러와 html Draw
var setLoopJoinEvent = async function () {  
	dummyRemon.config.credential.serviceId = serviceId;
    dummyRemon.config.credential.key = serviceKey;
	await dummyRemon.fetchCasts().then(function(data){
		setLoopChDraw(data);
	});
}
	
var setRemonJoinRemote =async function (index,callback) {  
	dummyRemon.config.credential.serviceId = serviceId;
    dummyRemon.config.credential.key = serviceKey;
	await dummyRemon.fetchCasts().then(function(data){
		var castList = data.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo'+index)>=0) return this;})
			.sort(function(castPre,castNext){
			var pre = castPre.name.split('_')[1].replace('remoteVideo','');
			var next = castNext.name.split('_')[1].replace('remoteVideo','');
			return pre-next;
		});
		var height = $('div.seeBox_slick ul.slider .boarder').closest('.slick-slide').height();
		$('div.seeBox_slick ul.slider li.video_item').height(height-1);
		
		if(castList.length > 0){
			if($('#kkoSvcCnt').val() < index) return;
			 $('#remoteVideo'+(index)).attr('castName',castList[0].name);
			 setLoopChJoinInIn(castList[0],index);
		
			var castName = $('#remoteVideo1').attr('castName');
		}
		if(callback)callback();
	});
}
var setLoopChDraw = function(castList){
	var sortingCastList = castList.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo')>=0) return this;})
	.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[1].replace('remoteVideo','');
		var next = castNext.name.split('_')[1].replace('remoteVideo','');
		return pre-next;
	});
	
	var height = $('div.seeBox_slick ul.slider .boarder').closest('.slick-slide').height();
	$('div.seeBox_slick ul.slider li.video_item').height(height-1);
	
	if(sortingCastList.length>0){
		for(var i=0;i<sortingCastList.length;i++){
			if($('#kkoSvcCnt').val() <= i) return;
			 $('#remoteVideo'+(i+1)).attr('castName',sortingCastList[i].name);
	 	};	
		var castName = $('#remoteVideo1').attr('castName');
		if(castName && castName.indexOf('remote') >= 0){
			if(sortingCastList.length > 0) $('div.seeBox_slick ul.slider').slick('goTo', 0);
		}		
	}
};

var remoteVideoArr = {};
var setLoopChJoinInIn = function(cast,i){
	var id = '#remoteVideo'+(i);
	var castName = cast.name;
	
	config.credential.serviceId = serviceId;
    config.credential.key = serviceKey;
    config.view.remote = id;

    var tmpRemon = new Remon({ config, listener }); 
    remoteVideoArr[castName] = tmpRemon;
	tmpRemon.joinCast(castName);
};
// remon 영성 관련 로직 [e]

var inputNumberVaildBidAmt = function(el,len){
	el.value= el.value.replace(/[^0-9]/g, "");
	if(el.value.length > len) {
		el.value = el.value.substr(0, len);
	}
	el.value= el.value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

window.addEventListener('beforeunload', function(){
	sFlag = true;
	if(socket.connected) socket.disconnect();
});