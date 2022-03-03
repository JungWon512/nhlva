$(function() {
	if($('#aucDsc').val() == '2'){
		$(".tblAuction .list_body ul li").removeClass('act');
		$(".tblAuction .list_body ul li").on('click',function(){
				var str ="";
				str += $(this).find('dd.aucPrgSq').text().trim();
				str +="|"+$(this).find('dd.indvSexC').text().trim();
				str +="|"+$(this).find('dd.ftsnm').text().trim();
				str +="|"+fnSetComma($(this).find('dd.cowSogWt').text().trim());
				str +="|"+$(this).find('dd.matime').text().trim();
				str +="|"+$(this).find('dd.mcowDsc').text().trim();
				str +="|"+$(this).find('dd.sraIndvPasgQcn').text().trim();
				str +="|"+$(this).find('dd.kpnNo').text().trim();
				str +="|"+fnSetComma($(this).find('dd.lowsSbidLmtAm').text().trim());
				str +="|"+$(this).find('dd.rmkCntn').text().trim();
				try{			
					window.auctionBridge.setCowInfo(str);					
				}catch(e){
					console.log(e);
				};
		});
	}
	socketStart();
});

//소켓통신 connect 및 이벤트 바인딩
var socket=null,auctionConfig={},scData={};
var socketStart = function(){
	if(!$('#naBzPlc').val()) return;        
	if(socket){ socket.connect(); return;}
	var socketHost = (active == 'production')?"cowauction.kr":(active == 'develop')?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
	//socketHost += ':'+$('#webPort').val();
	socketHost += ':9001';
	socket = io.connect('https://'+socketHost+ '/6003' + '?auctionHouseCode='  + $('#naBzPlc').val(), {secure:true});

	socket.on('connect_error', connectErr);
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
}
var connectErr = function(){
	socketDisconnect();
}

//소켓통신 disconnect Event
var socketDisconnect = function(){
	socket.disconnect();	
}
//소켓통신 connect시 Event
var connectHandler = function() {
	//구분자|조합구분코드|회원번호|인증토큰|접속요청채널|사용채널|관전자여부
	var token = getCookie('watch_token');
	var johapCd = $('#naBzPlc').val();
	var num = 'WATCHER';
	var packet = 'AI|'+johapCd+'|'+num+'|'+token+'|6003|WEB'
	socket.emit('packetData', packet);	
}

var disconnectHandler = function() {
}

//소켓통신 수신시 제어하는 로직
/** 
	AR : 경매상태정보
	AS : 현재 경며정보
	SC : 현재 출품정보
	CF : 낙찰데이터 정보	
**/
var messageHandler = function(data) {
	debugConsole(data);
	var dataArr = data.split('|');
	var gubun = dataArr[0];
	                  
	switch(gubun){
		case "AR" :
			auctionConfig.status = dataArr[2]=='2000'?'succ':'fail';
			if(auctionConfig.status=='fail'){
				socketDisconnect();
			}
		break;	
		case "SE" :
			//요청 결과 미존재		2	4001	
			//요청 처리 실패		2	4002	
			//시작가 이하 응찰 시도	2	4003	
			//출품 이관 전 상태		2	4004	
			//응찰 취소 불가		2	4005	
			//정상 응찰 응답		2	4006	
			//정상 응찰 취소 응답		2	4007
		break;	
		case "AS" : //현재 경매상태
			//AS|8808990656656|65|3700000|0|8004||||0|0
			if(auctionConfig.curAucSeq) auctionConfig.preAucSeq = auctionConfig.curAucSeq
			auctionConfig.curAucSeq = dataArr[2];
			if(scData[auctionConfig.curAucSeq]) scLoad(scData[auctionConfig.curAucSeq].split('|'));
			
			if(!auctionConfig.asData) auctionConfig.asData = {};
			var tmpAsDAta = { 
				aucPrgSq: dataArr[2]
				, selSts: dataArr[5]
				, lowsSbidLmtAm: dataArr[4]
			};							

			if(auctionConfig.asData.curAucSeq) auctionConfig.asData.preAucSeq = auctionConfig.asData.curAucSeq
			auctionConfig.asData.curAucSeq = dataArr[2];
			
			var tr = getTrRow(auctionConfig.asData.curAucSeq);
			tr.find('dl dd.lowsSbidLmtAm').text(fnSetComma(Math.round(tmpAsDAta.lowsSbidLmtAm)+''));
			if($('#aucDsc').val() == '2' && tmpAsDAta.selSts=='8006'){
				location.reload();
			};
			changeTrRow(tr);
		break;	
		case "SC" : //현재 출품정보
			scData[dataArr[2]] = data;
			if(auctionConfig.curAucSeq || auctionConfig.curAucSeq != dataArr[2]) return;
			scLoad(dataArr);
		break;	
		case "AF" :
			if($('#aucDsc').val() != '1') return; 
			//구분자 | 조합구분코드 | 출품번호 | 낙/유찰결과코드(01/02) | 낙찰자회원번호 | 낙찰금액	
			var tmpAsDAta = { 
				aucPrgSq: dataArr[2]
				, selSts: dataArr[3]=='22'?'낙찰':(dataArr[3]=='23'?'유찰':dataArr[3]=='11'?'대기':'')
				, sraSbidAm: dataArr[6]
			};						
							
			var tr = getTrRow(tmpAsDAta.aucPrgSq);
			
			 var oSelSts =tr.find('dl dd.selSts').text();
			//if(tmpAsDAta.selSts && oSelSts != tmpAsDAta.selSts){
			tr.find('dl dd.selSts').text(tmpAsDAta.selSts);
			tr.find('dl dd.sraSbidAm').text(fnSetComma(Math.round(tmpAsDAta.sraSbidAm)));
			changeTrRow(tr);	
		break;	
		default:break;
	}
}

//출품정보 변경시 row변경 로직
var changeTrRow = function(tr) {
	$(".tblAuction .list_body ul li").removeClass('act');
	if(tr){
		if(isApp() || chkOs() != 'web'){
			//var scH = tr.outerHeight();
			//$('.tblAuction .list_body ul').animate({scrollTop: (scH*tr.index())},1000);
			if(tr.index()< 0){$('.tblAuction .list_body ul').animate({scrollTop: 0},1000); return;}
			var scH = $('.tblAuction .list_body ul li').eq(tr.index()).position().top-$('.tblAuction .list_body ul li:first').position().top;
			$('.tblAuction .list_body ul').animate({scrollTop: scH},1000);
		}else{
			$(".list_body ul").mCustomScrollbar('scrollTo'
				,$(".list_body ul").find('.mCSB_container').find('li:eq('+tr.index()+')')
				,{scrollInertia:0}
			);			
		}		
		tr.addClass('act');
	}
}
var getTrRow = function(curAucSeq) {
	return $('.tblAuction .list_body li').filter(function(i,obj){
		var aucPrgSq = $(this).find('dl dd.aucPrgSq').text().trim();
		if(aucPrgSq==curAucSeq) return {obj:this,idx:i};
	});
}

var scLoad = function(dataArr){	
	if($('#aucDsc').val() == '2') return;
	if(!auctionConfig.scData) auctionConfig.scData = {};
	//if(auctionConfig.scData.curAucSeq) auctionConfig.scData.preAucSeq = auctionConfig.scData.curAucSeq
	auctionConfig.scData.curAucSeq = dataArr[2];
				
	//관전 전광판 데이터 update				
	var tr = getTrRow(auctionConfig.scData.curAucSeq);
	tr.find('dl dd.ftsnm').html(nameEnc(dataArr[9]));
	tr.find('dl dd.cowSogWt').text(fnSetComma(dataArr[25]));
	tr.find('dl dd.lowsSbidLmtAm').text(fnSetComma(dataArr[27])+'');
	tr.find('dl dd.sraSbidAm').text(fnSetComma(dataArr[31])+'');
	tr.find('dl dd.rmkCntn').text(dataArr[28]);
}