$(function() {
	init();
});

var init = function(){
	setLayout();
	setEventListener();
};
var setLayout = function(){
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

var auctionConfig,scData={};
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
			if(auctionConfig.curAucSeq) auctionConfig.preAucSeq = auctionConfig.curAucSeq
			auctionConfig.curAucSeq = dataArr[2];
			if(scData[auctionConfig.curAucSeq]) scLoad(scData[auctionConfig.curAucSeq].split('|'));
			
			//if(auctionConfig.auctionSt && (dataArr[6]=='8006' || dataArr[6]=='8002')){auctionConfig.auctionSt=dataArr[6]; return};
			auctionConfig.auctionSt=dataArr[6];
			auctionConfig.anStatus = false;
			$('table.tblBoard tbody tr.title td p.auctionNum').text(dataArr[2]);
			$('table.tblBoard tbody tr.title td p.bidUser').closest('td').hide();
			$('table.tblBoard tbody tr.title td p.bidPrice').closest('td').hide();
			$('table.tblBoard tbody tr.title td p.boardTitle').closest('td').show();
			var aucStConfig ={
				t8001:'경매 대기중'
				,t8002:'경매 대기중'
				,t8003:'경매 시작'
				,t8004:'경매 진행중'
				,t8005:'경매 정지'
				,t8006:'경매응찰 완료'
				,t8007:'경매 종료'
			}
			switch(dataArr[6]){
				case "8001" : $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8001); break; 
				case "8002" : $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8002); break; 
				case "8003" : $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8003); break; 
				case "8004" : $('table.tblBoard tbody tr.title td p.boardTitle').addClass('txt-red').text(aucStConfig.t8004); break; 
				case "8005" : $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8005); break; 
				case "8006" : $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8006); break; 
				case "8007" :
//					socketDisconnect();
					$('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text(aucStConfig.t8007);  
				break; 
				default:break;
			}
		break;	
		case "SC" : //현재 출품정보
			scData[dataArr[2]] = data;
			if(auctionConfig.curAucSeq || auctionConfig.curAucSeq != dataArr[2]) return;
			scLoad(dataArr);
		break;	
		case "SD" : //경매종료 카운트		
			if(dataArr[2]=='C'){				
				//$('table.tblBoard tbody tr.title td p.auctionNum').text(dataArr[2]);
				$('table.tblBoard tbody tr.title td p.bidUser').closest('td').hide();
				$('table.tblBoard tbody tr.title td p.bidPrice').closest('td').hide();
				$('table.tblBoard tbody tr.title td p.boardTitle').closest('td').show();	
				$('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').html('경매마감 <b>'+dataArr[3]+'</b>초 전');				
			}	
		break;	
		case "AF" : //낙유찰 결과
			//구분자 | 조합구분코드 | 출품번호 | 낙/유찰결과코드(01/02) | 낙찰자회원번호 | 낙찰금액
			//AF|8808990656656|65|02|null|null
			$('table.tblBoard tbody tr.title td.auctionNum').text(dataArr[2]);
			if(dataArr[3]=='22'){ //낙찰
				var user =dataArr[5],price = dataArr[6];
				$('table.tblBoard tbody tr.title td p.bidUser').closest('td').show();
				$('table.tblBoard tbody tr.title td p.bidPrice').closest('td').show();
				$('table.tblBoard tbody tr.title td p.boardTitle').closest('td').hide();
				$('table.tblBoard tbody tr.title td p.bidUser').html(''+ user);
				$('table.tblBoard tbody tr.title td p.bidPrice').html(''+ price);
			}else if(dataArr[3]=='23'){
				$('table.tblBoard tbody tr.title td p.bidUser').closest('td').hide();
				$('table.tblBoard tbody tr.title td p.bidPrice').closest('td').hide();
				$('table.tblBoard tbody tr.title td p.boardTitle').closest('td').show();
				$('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text('유찰되었습니다.');				
			}
		break;
		case "AN" :
			// 재경매 대상 수신
			auctionConfig.anStatus = true;
			$('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text('재경매 진행 중');
			break;
		case "AY" :
			// 재경매 대상 수신
			debugConsole(dataArr[2] +' : '+$('table.tblBoard tbody tr.title td p.auctionNum').text());
			if(dataArr[2]!=$('table.tblBoard tbody tr.title td p.auctionNum').text()) return;
			if((auctionConfig.auctionSt =='8003' || auctionConfig.auctionSt =='8004') && 'F' == dataArr[3].trim()) $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text('응찰 종료');
			else if(auctionConfig.anStatus) $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text('재경매 진행중');
			else $('table.tblBoard tbody tr.title td p.boardTitle').removeClass('txt-red').text('경매 진행중');
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

var scLoad = function(dataArr){	
	var sex = dataArr[13];
	
	$('table.tblBoard tbody tr.title td p.auctionNum').text(dataArr[2]);	//경매번호
	$('table.tblBoard tbody tr.val td p.ftsnm').text(nameEnc(dataArr[9],"●")); //출하주
	$('table.tblBoard tbody tr.val td p.cowSogWt').text(dataArr[25]); //중량
	$('table.tblBoard tbody tr.val td p.lowsSbidLmtAm').text(Math.round(parseInt(dataArr[27]))); //최저가
	// $('table.tblBoard tbody tr.val td p.prnyMtcn').text(Math.round(parseInt(dataArr[17]))); //임신
	$('table.tblBoard tbody tr.val td p.sraIndvPasgQcn').text(dataArr[18]); //계대
	$('table.tblBoard tbody tr.val td p.kpnNo').text(dataArr[11]&&dataArr[12].replace('KPN','')); //kpn
	$('table.tblBoard tbody tr.val td p.mcowDsc').text(dataArr[14]); //어미
	$('table.tblBoard tbody tr.val td p.matime').text(dataArr[16]); //산차
	
	$('table.tblBoard tbody tr.val td p.rmkCntn').removeAttr("style");
	$('table.tblBoard tbody tr.val td p.rmkCntn').css("white-space",'nowrap');
	$('table.tblBoard tbody tr.val td p.rmkCntn').removeClass("move-txt");
	$('table.tblBoard tbody tr.val td p.rmkCntn').text(dataArr[28]?dataArr[28]:'　'); //비고
	var width = $('table.tblBoard tbody tr.val td p.rmkCntn').width();
	var len = $('table.tblBoard tbody tr.val td p.rmkCntn').text().trim().length * 12;
	$('table.tblBoard tbody tr.val td p.rmkCntn').css('width',len>width?len:width);
	
	$('table.tblBoard tbody tr.val td p.rmkCntn').addClass("move-txt");
	$('table.tblBoard tbody tr.val td p.sex').text(sex); //성별
	convertDefaultValue('table.tblBoard tbody tr.val td p','-');
}