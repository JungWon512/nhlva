$(function() {
	init();
});

var init = function(){
	setLayout();
	setEventListener();
};
var setLayout = function(){
	infoInterval = setInterval(function(){
		$('section.billboard-info .time').html(getTimeStr());
	},1000*1);
};
var setEventListener = function(){
	setSocket();
}
var socket=null,scCnt=0,auctionConfig={};
var setSocket = function(){        
	if(!$('#naBzPlc').val()) return;
	if(socket){ socket.connect(); return;}
	var socketHost = (active == 'production')?"cowauction.kr":(active == 'develop')?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
	//socketHost += ':'+$('#webPort').val();
	socketHost += ':9001';
	socket = io.connect('https://'+socketHost+ '/6003' + '?auctionHouseCode='  + $('#naBzPlc').val(), {secure:true});

	socket.on('connect_error', socketDisconnect);	
	socket.on('connect', connectHandler);	
	socket.on('disconnect', disconnectHandler);	
	socket.on('ResponseConnectionInfo', messageHandler);
	socket.on('AuctionCountDown', messageHandler);	
	socket.on('ToastMessage', messageHandler);	
	socket.on('FavoriteEntryInfo', messageHandler);	
	socket.on('ResponseCode', messageHandler);
	socket.on('CurrentEntryInfo', messageHandler);	
	socket.on('BidderConnectInfo', messageHandler);	
	socket.on('AuctionStatus', messageHandler);	
	socket.on('AuctionResult', messageHandler);	
	socket.on('CancelBidding', messageHandler);
	socket.on('RetryTargetInfo', messageHandler);
	socket.on('AuctionBidStatus', messageHandler);
	socket.on('ShowFailBIdding', messageHandler);	
}

var socketDisconnect = function(){
	socket.disconnect();	
}
var connectHandler = function() {
	//구분자|조합구분코드|회원번호|인증토큰|접속요청채널|사용채널|관전자여부
	var token = getCookie('access_token');
	var johapCd = $('#naBzPlc').val();
	var num = $('#userId').val();
	var packet = 'AI|'+johapCd+'|'+num+'|'+token+'|6003|MANAGE'
	socket.emit('packetData', packet);	
}
var disconnectHandler = function() {	
}

var messageHandler = function(data) {
	debugConsole(data);
	var dataArr = data.split('|');
	var gubun = dataArr[0];
	switch(gubun){	
		case "AR" : //인증정보 response
			auctionConfig.status = dataArr[2]=='2000'?'succ':'fail';
			if(auctionConfig.status=='fail'){
				socketDisconnect();
			}
		break;	
		case "AS" : //현재 경매상태	
			var aucStConfig ={
				t8001:'<span class="txt-blue">일괄</span><span class="txt-green"> 경매 대기 중</span>'
				,t8002:'<span class="txt-blue">일괄</span><span class="txt-green"> 경매 대기 중</span>'
				,t8003:'<span class="txt-blue">일괄</span><span class="txt-green"> 경매 시작</span>'
				,t8004:'<span class="txt-blue">일괄</span><span class="txt-green"> 경매 진행 중</span><br>응찰하시기 바랍니다'
				,t8004:'<span class="txt-blue">일괄</span><span class="txt-green"> 경매 정지</span>'
				,t8006:'경매응찰 완료'
				,t8007:'경매 종료'
			}
			switch(dataArr[6]){
				case "8001" : $('section.billboard-info .infoTxt').html(aucStConfig.t8001); break; 
				case "8002" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8002); break; 
				case "8003" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8003); break; 
				case "8004" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8004); break; 
				case "8005" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8005); break; 
				case "8006" : 
					$('.billboard-info').hide();
					$('.billboard-noBid').hide();
					$('.billboard-view').show();
					clearInterval(infoInterval);
					clearInterval(noInfoInterval);
					viewIntervalFunc();
					
				break; 
				case "8007" : 
					socketDisconnect();
					$('.billboard-info').hide();
					$('.billboard-noBid').hide();
					$('.billboard-view').show();
					clearInterval(infoInterval);
					clearInterval(noInfoInterval);
					viewIntervalFunc();
					$('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8007); 
				break; 
				default:break;
			}
			if(dataArr[6]=='8006'){
				//location.reload();
			};		
		break;	
		case "SZ" : 
			//구분자 | 조합구분코드 | 경매일자(YYYYmmdd) | 경매구분(송아지 : 1 / 비육우 : 2 / 번식우 : 3 / 일괄 : 0) | 경매등록일련번호
			
			var params = {
				naBzplc : dataArr[1]
				, aucDt : dataArr[2]
				, aucObjDsc : dataArr[3]
			}
			$.ajax({
				url: '/office/getAbsentCowList',
				data: params,
				type: 'POST',
				dataType: 'json',
				success : function() {
				},
				error: function(xhr, status, error) {
				}
			}).done(function (json) {
				console.log(json);				
				var success = json.success;
				var message = json.message;
				if (!success) {
					modalAlert("", message);
				}
				else {
					if(json && json.entry){
						var body = $('.billboard-noBid .list-body ul');
						body.empty();
						
						var entryArr = json.entry.split('|');
						var len = json.entry.split('|').length;
						var blankLen = len%15;
						var totLen=len+(blankLen<=0?0:15-blankLen);
						for(var i =0;i<totLen;i++){
							if(entryArr[i]){
								body.append('<li><div class="bg bg-gray"><span class="fz120 txt-bold">'+entryArr[i]+'</span></div></li>');
							}else{
								body.append('<li><div class="bg"></div></li>');								
							}
						}
					}
					$('.billboard-info').hide();
					$('.billboard-view').hide();
					$('.billboard-noBid').show();
					$(".billboard-noBid .cow-number-list").mCustomScrollbar({
						theme:"dark-thin",
						scrollInertia: 200,
					});
					clearInterval(infoInterval);
					clearInterval(viewInterval);
					noInfoIntervalFun();
				}
			});
		break;	
		default:break;
	}
}
//쿠키값얻는 function
var getCookie = function(name){
	var cookies = document.cookie.split(';');
	for(var i = 0;i<cookies.length;i++){
		if(cookies[i].split('=')[0].trim() == name){
			return cookies[i].split('=')[1].trim();
		}
	}
} 


var noInfoInterval,viewInterval,infoInterval;
var viewIntervalFunc = function(index){
	var index = 0;
	setInterval(function(){
		var len = $(".billboard-view .list_body ul li").length;
		if(len<=index){
			index = 0;
		}else{
			index += 10;
		}
		if(isApp() || chkOs() != 'web'){
			var scH = $('.tblAuction .list_body ul li').outerHeight();					
			$('.billboard-view .tblAuction .list_body ul').animate({scrollTop: (scH*index)},1000);
		}else{
			$(".billboard-view .list_body ul").mCustomScrollbar('scrollTo'
				,$(".billboard-view .list_body ul").find('.mCSB_container').find('li:eq('+(index)+')')
				,{scrollInertia:0}
			);
		}	
	},1000*5)
};
var noInfoIntervalFun = function(index){
	var index = 0;
	noInfoInterval = setInterval(function(){
		var len = $(".billboard-noBid .list-body ul li").length;
		if(len<=index){
			index = 0;
		}else{
			index += 15;
		}
		
		$(".billboard-noBid .list-body ul").mCustomScrollbar('scrollTo'
			,$(".billboard-noBid .list-body ul").find('.mCSB_container').find('li:eq('+(index)+')')
			,{scrollInertia:0}
		);
	},1000*5)
};