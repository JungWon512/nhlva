var agoraArr=[];
var joinChk = false;

$(function() {

    var setLayout = function() {
		var chkTimeInterval = setInterval(function(){
			chekcTimeAgoraReave();
		},30 * 1000); //20초?
		$('div.seeBox_bottom.vidioSlide .seeBox_slick ul.slider li.video_item video').each((i,o)=>{
			$(o).height('55vh');
		});
		$('.chart').easyPieChart({
			barColor: '#007eff',
			trackColor: '#dbdbdb',
			lineCap: 'round',
			lineWidth: 12,
			size: 220,
			animate: 1000,
			scaleColor: false,
		});
		$(".seeBox_slick ul.slider").slick({
			dots: true,
			adaptiveHeight: true,
		});
		
		//22.08.23 jjw 경매번호클릭시 영상 mute toggle
		$('.auctionNum').closest('td').on('click',function(){
			//var currentSlide = $('.seeBox_slick ul.slider').slick('slickCurrentSlide');
			if(!agoraArr[1].user.audioTrack.isPlaying){
				agoraArr[1].playAgora('audio');
			}else{
				agoraArr[1].stopAgora('audio');
			}
		});

		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			var index = currentSlide+1;
			$('div[id^=player-remoteVideo]:not([id=player-remoteVideo'+index+'])').each(function(){
				var index = $(this).attr('index');
				agoraArr[index].stopAgora('video');
			});
			agoraArr[index].playAgora('video');
		});
		afAuctionNum = new Set($('#listAucNum').val().split(','));
    };

    var setBinding = function() {
		//setRemon();
		for(var i=1;i<=$('#kkoSvcCnt').val();i++){
			joinChk = true;				 
			var agoraOptions = {
			  channel: null
			  , appid : $('#kkoSvcKey').val()
			  , uid: null
			  , token: null
			  , role: "audience"
			  , channelNum : 'remoteVideo'+ i
			  , autoPlay : true
			  , target : 'div.seeBox_bottom.vidioSlide .seeBox_slick ul.slider li.video_item'
			  , height : '55vh'
			};
			agoraArr[i] = new Agora(agoraOptions);
			agoraArr[i].agoraJoin();
		}
		socketStart();		
    };

    setLayout();    
    setBinding();
});

//소켓통신 connect 및 이벤트 바인딩
var socket=null,scCnt=0,auctionConfig={};
var afAuctionNum = null; 
var socketStart = function(){        
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
//쿠키값얻는 function
var getCookie = function(name){
	var cookies = document.cookie.split(';');
	for(var i = 0;i<cookies.length;i++){
		if(cookies[i].split('=')[0].trim() == name){
			return cookies[i].split('=')[1].trim();
		}
	}
}
//소켓통신 disconnect Event
var socketDisconnect = function(){
	socket.disconnect();	
}
//소켓통신 connect시 Event
var connectHandler = function() {
	var token = getCookie('access_token');
	var num = 'WATCHER';
	var packet = 'AI|'+$('#naBzPlc').val()+'|'+num+'|'+token+'|6003|WEB'
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
		case "SC" : //현재 출품정보
			scData[dataArr[2]] = data;
			if(auctionConfig.curAucSeq || auctionConfig.curAucSeq != dataArr[2]) return;
			scLoad(dataArr);
		break;	
		case "AS" : //현재 경매상태			
			if(auctionConfig.curAucSeq) auctionConfig.preAucSeq = auctionConfig.curAucSeq
			auctionConfig.curAucSeq = dataArr[2];
			if(scData[auctionConfig.curAucSeq]) scLoad(scData[auctionConfig.curAucSeq].split('|'));
			
			auctionConfig.auctionSt=dataArr[6];
			auctionConfig.anStatus = false;
			$('table.tblAuctionSt tbody tr td.val p.auctionNum').text(dataArr[2]?dataArr[2]:"-");
			$('table.tblAuctionSt tbody tr.st td.complate').hide();
			$('table.tblAuctionSt tbody tr.st td.count-td').show();
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
				case "8001" : $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8001); break; 
				case "8002" : $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8002); break; 
				case "8003" : $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8003); break; 
				case "8004" : $('table.tblAuctionSt tbody tr.st td.count-td p').addClass('txt-red').text(aucStConfig.t8004); break; 
				case "8005" : $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8005); break; 
				case "8006" : $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8006); break; 
				case "8007" : 
					socketDisconnect();
					$('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text(aucStConfig.t8007); 
				break; 
				default:break;
			}
		break;	
		case "AF" :
			$('.tblBoard table tbody tr.title td.auctionNum').text(dataArr[2]);
			if(dataArr[3]=='22'){ //낙찰
				var user =dataArr[5],price = dataArr[6];
				$('table.tblAuctionSt tbody tr.st td.count-td').hide();
				$('table.tblAuctionSt tbody tr.st td.complate').show();
				$('table.tblAuctionSt tbody tr.st td.complate p.tdBiddNum').html(''+ user);
				$('table.tblAuctionSt tbody tr.st td.complate p.tdBiddAmt').html(''+ price);
			}else{
				$('table.tblAuctionSt tbody tr.st td.complate').hide();
				$('table.tblAuctionSt tbody tr.st td.count-td').show();
				$('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text('유찰되었습니다.');				
			}
			calcPiePercent(dataArr[2]);
		break;	
		case "SD" : //경매종료 카운트		
			if(dataArr[2]=='C'){
				$('table.tblAuctionSt tbody tr.st td.complate').hide();
				$('table.tblAuctionSt tbody tr.st td.count-td').show();
				$('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').html('경매마감 <b>'+dataArr[3]+'</b>초 전');				
			}	
		break;	
		case "AN" :
			// 재경매 대상 수신
			auctionConfig.anStatus = true;
			$('table.tblAuctionSt tbody tr.st td.count-td p').addClass('txt-red').text('재경매 진행 중');
			break;
		case "AY" :
			// 재경매 대상 수신
			if(dataArr[2]!=$('table.tblAuctionSt tbody tr td.val p.auctionNum').text()) return;
			if((auctionConfig.auctionSt =='8003' || auctionConfig.auctionSt =='8004') && 'F' == dataArr[3].trim()) $('table.tblAuctionSt tbody tr.st td.count-td p').removeClass('txt-red').text('응찰 종료');
			else if(auctionConfig.anStatus) $('table.tblAuctionSt tbody tr.st td.count-td p').addClass('txt-red').text('재경매 진행중');
			else $('table.tblAuctionSt tbody tr.st td.count-td p').addClass('txt-red').text('경매 진행중');
			break;
		default:break;
	}
}
function calcPiePercent(aucNum){
	if(afAuctionNum.has(aucNum)) return;
	afAuctionNum .add(aucNum);
	var stantNotCnt = new Number($('.chart_label .standNot span').text()) +1;
	var stantCnt = new Number($('.chart_label .stand span').text()) -1;
	var totCnt = stantNotCnt+stantCnt;
	var result = new Number(stantNotCnt)/new Number(totCnt) *100;
	result = result.toFixed(0);
	$('.chart').attr('data-percent',result);
	$('.chart_label .standNot span').text(stantNotCnt);
	$('.chart_label .stand span').text(stantCnt);
	$('.chart span.count').text(result+'%');
	$('.chart').data('easyPieChart').update(result);
};

var scLoad = function(dataArr){
	$('table.tblAuctionSt tbody tr td.val p.auctionNum').text(dataArr[2]?dataArr[2]:"-");	//경매번호
	
	$('table.tblAuctionSt tbody tr td td p.ftsnm').html(nameEnc(dataArr[9],"●")); //출하주
	$('table.tblAuctionSt tbody tr td td p.cowSogWt').text(fnSetComma(dataArr[25])); //중량
	$('table.tblAuctionSt tbody tr td td p.kpnNo').text(dataArr[12]&&dataArr[12].replace('KPN','')); //kpn
	$('table.tblAuctionSt tbody tr td td p.mcowDsc').text(dataArr[14]); //어미
	$('table.tblAuctionSt tbody tr td td p.sex').text(dataArr[13]); //성별				
	$('table.tblAuctionSt tbody tr td td p.lowsSbidLmtAm').text(fnSetComma(dataArr[27])); //예정가
		
	$('table.tblAuctionSt tbody tr td td p.sraIndvPasgQcn').text(dataArr[18]); //계대	
	$('table.tblAuctionSt tbody tr td td p.matime').text(dataArr[16]); //산차
	$('table.tblAuctionSt tbody tr td td p.rmkCntn').text(dataArr[28]); //산차
}

function getKoreaDate(){
	const now = new Date(); // 현재 시간
	var utcNow = now.getTime() + (now.getTimezoneOffset() * 60 * 1000);
	// 3. UTC to KST (UTC + 9시간)
	const koreaTimeDiff = 9 * 60 * 60 * 1000
	return new Date(utcNow + koreaTimeDiff);	
}

function chekcTimeAgoraReave(){
	var kNow = getKoreaDate();
	console.log(kNow);
	if(kNow.getHours() > 13 && kNow.getMinutes() == '00'){
		for(let index in agoraArr){
			if(agoraArr[index]) agoraArr[index].client.leave();
		};
		agoraArr = [];		
	}
}
