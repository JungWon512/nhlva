$(function() {

    var setLayout = function() {
		$('.chart').easyPieChart({
			barColor: '#007eff',
			trackColor: '#dbdbdb',
			lineCap: 'round',
			lineWidth: 12,
			size: 260,
			animate: 1000,
			scaleColor: false,
		});
		$(".seeBox_slick ul.slider").slick({
			dots: true,
			adaptiveHeight: true,
		});
		
		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			$('video[id^=remoteVideo]').each(function(){
				if($(this).attr('id') == 'remoteVideo1') return;
				$(this).get(0).muted = true;
				if(!$(this).get(0).paused) $(this).get(0).pause();
			});
			
			var callback = function(currentSlide){
			var video =$('#remoteVideo'+(currentSlide+1)).get(0);
				if($('#remoteVideo1').get(0) && currentSlide == 0){
					$('#remoteVideo1').get(0).muted = false;
				}
				if(video && video.paused) video.play();
			}
			setRemonJoinRemote(currentSlide+1,callback);
		});
		afAuctionNum = new Set($('#listAucNum').val().split(','));
    };

    var setBinding = function() {
		setRemon();
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
var messageHandler = function(data) {
	console.log(data);
	var dataArr = data.split('|');
	var gubun = dataArr[0];
	                  
	switch(gubun){
		case "AR" :
		break;	
		case "AR" : //인증정보 response
			auctionConfig.status = dataArr[2]=='2000'?'succ':'fail';
			if(auctionConfig.status=='fail'){
				socketDisconnect();
			}
		break;	
		case "SC" : //현재 출품정보
				$('table.tblAuctionSt tbody tr td.val p.auctionNum').text(dataArr[2]);	//경매번호
				
				$('table.tblAuctionSt tbody tr td td p.ftsnm').text(nameEnc(dataArr[9])); //출하주
				$('table.tblAuctionSt tbody tr td td p.cowSogWt').text(dataArr[25]); //중량
				$('table.tblAuctionSt tbody tr td td p.kpnNo').text(dataArr[12]&&dataArr[12].replace('KPN','')); //kpn
				$('table.tblAuctionSt tbody tr td td p.mcowDsc').text(dataArr[14]); //어미
				$('table.tblAuctionSt tbody tr td td p.sex').text(dataArr[13]); //성별				
				$('table.tblAuctionSt tbody tr td td p.lowsSbidLmtAm').text(dataArr[27]); //최저가
					
				$('table.tblAuctionSt tbody tr td td p.sraIndvPasgQcn').text(dataArr[18]); //계대	
				$('table.tblAuctionSt tbody tr td td p.matime').text(dataArr[16]); //산차
				$('table.tblAuctionSt tbody tr td td p.rmkCntn').text(dataArr[28]); //산차
		break;	
		case "AS" : //현재 경매상태			
			if(auctionConfig.auctionSt && (dataArr[6]=='8006' || dataArr[6]=='8002')){auctionConfig.auctionSt=dataArr[6]; return};
			auctionConfig.auctionSt=dataArr[6];
			$('table.tblAuctionSt tbody tr td.val p.auctionNum').text(dataArr[2]);
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
				case "8001" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8001); break; 
				case "8002" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8002); break; 
				case "8003" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8003); break; 
				case "8004" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8004); break; 
				case "8005" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8005); break; 
				case "8006" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8006); break; 
				case "8007" : $('table.tblAuctionSt tbody tr.st td.count-td p').text(aucStConfig.t8007); break; 
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
				$('table.tblAuctionSt tbody tr.st td.count-td p').text('유찰되었습니다.');				
			}
			calcPiePercent(dataArr[2]);
		break;	
		case "SD" : //경매종료 카운트		
			if(dataArr[2]=='C'){
				$('table.tblAuctionSt tbody tr.st td.complate').hide();
				$('table.tblAuctionSt tbody tr.st td.count-td').show();
				$('table.tblAuctionSt tbody tr.st td.count-td p').html('경매마감 <b>'+dataArr[3]+'</b>초 전');				
			}	
		break;	
		case "AN" :
			// 재경매 대상 수신
			auctionConfig.anStatus = true;
			$('table.tblAuctionSt tbody tr.st td.count-td p').text('재경매 진행 중');
			break;
		case "AY" :
			// 재경매 대상 수신
			if(dataArr[2]!=$('table.tblAuctionSt tbody tr td.val p.auctionNum').text()) return;
			if((auctionConfig.auctionSt =='8003' || auctionConfig.auctionSt =='8004') && 'F' == dataArr[3].trim()) $('table.tblAuctionSt tbody tr.st td.count-td p').text('응찰 종료');
			else if(auctionConfig.anStatus) $('table.tblAuctionSt tbody tr.st td.count-td p').text('재경매 진행중');
			else $('table.tblAuctionSt tbody tr.st td.count-td p').text('경매 진행중');
			break;
		default:break;
	}
}
function calcPiePercent(aucNum){
	if(afAuctionNum.has(aucNum)) return;
	afAuctionNum .add(aucNum);
	var stantNotCnt = new Number($('.chart_label .gp_tit1 span').text()) +1;
	var stantCnt = new Number($('.chart_label .gp_tit2 span').text()) -1;
	var totCnt = stantNotCnt+stantCnt;
	var result = new Number(stantNotCnt)/new Number(totCnt) *100;
	result = result.toFixed(0);
	$('.chart').attr('data-percent',result);
	$('.chart_label .gp_tit1 span').text(stantNotCnt);
	$('.chart_label .gp_tit2 span').text(stantCnt);
	$('.chart span.count').text(result+'%');
	$('.chart').data('easyPieChart').update(result);
}
//remon 영성 관련 로직
let dummyRemon,loop;
	
var config = {
  	credential: {
    	serviceId: $('#kkoSvcId').val(),
    	key: $('#kkoSvcKey').val()
  	},view: {
		remote: '#remoteVideo'
	},
    media: {
      recvonly: true,
      audio: false,
      video: false
    },  dev:{
		logLevel: 'SILENT'
	}
};
//remon Event listener
listener = {
    onCreate(chid) { console.log(`EVENT FIRED: onCreate: ${chid}`); },
    onJoin(chid) { 
		console.log(`EVENT FIRED: onJoin: ${chid}`);
	},
    onClose() { 
		console.log('EVENT FIRED: onClose'); 
    },
    onError(error) { 
		console.log(`EVENT FIRED: onError: ${error}`);
    }, onStat(result) { 
	}
};

//remon관련 실행 로직
function setRemon(){	
	config.credential.serviceId =  $('#kkoSvcId').val();
	config.credential.key = $('#kkoSvcKey').val();
	dummyRemon = new Remon({ config, listener });	
    setLoopJoinEvent();
    //loop = setInterval(setLoopJoinEvent,1000*15);
}

//특정주기마다 castlist 목록 불러와 html Draw
var setLoopJoinEvent = async function () {  
	
	dummyRemon.config.credential.serviceId =  $('#kkoSvcId').val();
	dummyRemon.config.credential.key = $('#kkoSvcKey').val();
	var castLists = await dummyRemon.fetchCasts();
	
	setLoopChDraw(castLists);
}

var setLoopChDraw = function(castList){
	var sortingCastList = castList.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo')>=0) return this;})
		.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[1].replace('remoteVideo','');
		var next = castNext.name.split('_')[1].replace('remoteVideo','');
		
		return pre-next;
	});
	console.log(sortingCastList);
//	var height = $('div.seeBox_slick ul.slider .boarder').closest('.slick-slide').height();
//	$('div.seeBox_slick ul.slider li.video_item').height(height-1);
	$('div.seeBox_slick ul.slider li.video_item').height('360px');
	
	if(sortingCastList.length > 0){
		for(var i=0;i<sortingCastList.length;i++){
			if($('#kkoSvcCnt').val() <= i) return;
			 $('#remoteVideo'+(i+1)).attr('castName',sortingCastList[i].name);
			 //setLoopChJoinInIn(sortingCastList[i],i+1);
	 	};
	
		var castName = $('#remoteVideo1').attr('castName');
		if(castName && castName.indexOf('remote') >= 0){
			if(sortingCastList.length > 0) $('div.seeBox_slick ul.slider').slick('goTo', 0);
		}
	}
};
var setLoopChJoinInIn = function(cast,i){
	dummyRemon.config.credential.serviceId = $('#kkoSvcId').val();
    dummyRemon.config.credential.key = $('#kkoSvcKey').val();
    dummyRemon.config.view.remote = '#remoteVideo'+(i);
	new Remon({ config:dummyRemon.config, listener }).joinCast(cast.name);
};

//특정주기마다 castlist 목록 불러와 html Draw
var setLoopJoinEvent = async function () {  
	dummyRemon.config.credential.serviceId = $('#kkoSvcId').val();
    dummyRemon.config.credential.key = $('#kkoSvcKey').val();
	await dummyRemon.fetchCasts().then(function(data){
		setLoopChDraw(data);
	});
}

var setRemonJoinRemote =async function (index,callback) {  
	dummyRemon.config.credential.serviceId = $('#kkoSvcId').val();
    dummyRemon.config.credential.key = $('#kkoSvcKey').val();
	await dummyRemon.fetchCasts().then(function(data){
		console.log(data);		
		var castList = data.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo'+index)>=0) return this;})
			.sort(function(castPre,castNext){
			var pre = castPre.name.split('_')[1].replace('remoteVideo','');
			var next = castNext.name.split('_')[1].replace('remoteVideo','');
			return pre-next;
		});
		console.log(castList);
//		var height = $('div.seeBox_slick ul.slider .boarder').closest('.slick-slide').height();
//		$('div.seeBox_slick ul.slider li.video_item').height(height-1);
	$('div.seeBox_slick ul.slider li.video_item').height('100%');
		
		if(castList.length > 0){
			if($('#kkoSvcCnt').val() < index) return;
			 $('#remoteVideo'+(index)).attr('castName',castList[0].name);
			 setLoopChJoinInIn(castList[0],index);
		
			var castName = $('#remoteVideo1').attr('castName');
		}
		if(callback)callback(index);
	});
}

var setLoopChDraw = function(castList){
	console.log(castList);
	var sortingCastList = castList.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo')>=0) return this;})
		.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[1].replace('remoteVideo','');
		var next = castNext.name.split('_')[1].replace('remoteVideo','');
		
		return pre-next;
	});
	
	//$('div.seeBox_slick ul.slider li.video_item').height('360px');
	$('div.seeBox_slick ul.slider li.video_item').height('100%');
	
	if(sortingCastList.length > 0){
		for(var i=0;i<sortingCastList.length;i++){
			if($('#kkoSvcCnt').val() <= i) return;
			 $('#remoteVideo'+(i+1)).attr('castName',sortingCastList[i].name);
			 //setLoopChJoinInIn(sortingCastList[i],i+1);
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
	
	config.credential.serviceId = $('#kkoSvcId').val();
    config.credential.key = $('#kkoSvcKey').val();
    config.view.remote = id;
    
    var tmpRemon = new Remon({ config, listener }); 
    remoteVideoArr[castName] = tmpRemon;
	tmpRemon.joinCast(castName);
};