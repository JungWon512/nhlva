$(function() {
	init();
});

var init = function(){
	setLayout();
	setEventListener();
};
var setLayout = function(){
	var index = 0;
	setInterval(function(){
//		console.log(index);
		var len = $(".list_body ul li").length;
		if(len<=index){
			index = 0;
		}else{
			index += 10;
		}
		if(isApp() || chkOs() != 'web'){
			var scH = $('.tblAuction .list_body ul li').outerHeight();					
			$('.tblAuction .list_body ul').animate({scrollTop: (scH*index)},1000);
		}else{
			$(".list_body ul").mCustomScrollbar('scrollTo'
				,$(".list_body ul").find('.mCSB_container').find('li:eq('+(index)+')')
				,{scrollInertia:0}
			);
		}	
	},1000*5);
};
var setEventListener = function(){
	setSocket();
}
var socket=null,scCnt=0,auctionConfig={};
var setSocket = function(){        
	if(!$('#naBzPlc').val()) return;
	if(socket){ socket.connect(); return;}
	var socketHost = (location.hostname.indexOf("xn--o39an74b9ldx9g.kr") >-1 || location.hostname.indexOf("nhlva.nonghyup.com") >-1)?"cowauction.kr":location.hostname.indexOf("xn--e20bw05b.kr")>-1?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
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
	console.log(data);
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
			if(dataArr[6]=='8006'){
				location.reload();
			};		
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