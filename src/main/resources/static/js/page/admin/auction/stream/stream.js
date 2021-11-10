$(function() {

    var setLayout = function() {
    };

    var setBinding = function() {
		socketStart();
		setRemon();
    };

    setLayout();    
    setBinding();
});

//소켓통신 connect 및 이벤트 바인딩
var socket=null,scCnt=0,auctionConfig={};
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
			var scInterval = setInterval(function() {
				if(scCnt > 0 && (auctionConfig.auctionSt == '8003' || auctionConfig.auctionSt == '8002')) return;
				clearInterval(scInterval);					

				$('table.tblAuctionSt tbody tr.val td.auctionNum').text(dataArr[2]);	//경매번호
				
				$('table.tblAuctionBoard tbody tr.val td.ftsnm').text(nameEnc(dataArr[9])); //출하주
				$('table.tblAuctionBoard tbody tr.val td.cowSogWt').text(dataArr[25]); //중량
				$('table.tblAuctionBoard tbody tr.val td.kpnNo').text(dataArr[12]&&dataArr[12].replace('KPN','')); //kpn
				$('table.tblAuctionBoard tbody tr.val td.mcowDsc').text(dataArr[143]); //어미
				$('table.tblAuctionBoard tbody tr.val td.sex').text(dataArr[13]); //성별				
				$('table.tblAuctionBoard tbody tr.val td.lowsSbidLmtAm').text(dataArr[27]); //최저가
				scCnt++;							  
			}, 500);
		break;	
		case "AS" : //현재 경매상태			
			if(auctionConfig.auctionSt && (dataArr[6]=='8006' || dataArr[6]=='8002')){auctionConfig.auctionSt=dataArr[6]; return};
			auctionConfig.auctionSt=dataArr[6];
			$('.tblBoard table tbody tr.title td.auctionNum').text(dataArr[2]);
			$('.tblBoard table tbody tr.title td.tdUser').hide();
			$('.tblBoard table tbody tr.title td.tdPrice').hide();
			$('.tblBoard table tbody tr.title td.tdTitle').show();
			var aucStConfig ={
				t8001:'경매 대기중입니다.'
				,t8002:'경매 대기중입니다.'
				,t8003:'경매 시작되었습니다.'
				,t8004:'경매 진행중입니다.'
				,t8005:'경매 정지되었습니다.'
				,t8006:'경매 완료되었습니다.'
				,t8007:'경매 종료되었습니다.'
			}
			switch(dataArr[6]){
				case "8001" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8001); break; 
				case "8002" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8002); break; 
				case "8003" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8003); break; 
				case "8004" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8004); break; 
				case "8005" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8005); break; 
				case "8006" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8006); break; 
				case "8007" : $('table.tblAuctionSt tbody tr td.tdBoard p').text(aucStConfig.t8007); break; 
				default:break;
			}
		break;	
		case "AF" :
			$('.tblBoard table tbody tr.title td.auctionNum').text(dataArr[2]);
			if(dataArr[3]=='22'){ //낙찰
				var user =dataArr[5],price = dataArr[6];
				$('table.tblAuctionSt tbody tr .tdBiddAmt').show();
				$('table.tblAuctionSt tbody tr .tdBiddNum').show();
				$('table.tblAuctionSt tbody tr td.tdBoard').hide();
				$('table.tblAuctionSt tbody tr td.tdBiddNum').html(''+ user);
				$('table.tblAuctionSt tbody tr td.tdBiddAmt').html(''+ price);
			}else{
				$('table.tblAuctionSt tbody tr .tdBiddNum').hide();
				$('table.tblAuctionSt tbody tr .tdBiddAmt').hide();
				$('table.tblAuctionSt tbody tr td.tdBoard').show();
				$('table.tblAuctionSt tbody tr td.tdBoard p').text('유찰되었습니다.');				
			}
		break;	
		default:break;
	}
}

//출품정보 변경시 row변경 로직
var changeTrRow = function(tr) {
	tr.css('background-color','#00ffff');			
	var scroll = tr.offset().top - $('#kt_header_menu').height()-$('.tblAuction thead tr').height();
	//현재 출품번호
	tr.css('background-color','#00ffff');
	$(document).scrollTop(scroll);
}
var getTrRow = function(curAucSeq) {
	return $('.tblAuction tbody tr').filter(function(i,obj){
		var aucPrgSq = $(this).find('.aucPrgSq').text().trim();
		if(aucPrgSq==curAucSeq) return this;
	});
}

//remon 영성 관련 로직
let dummyRemon,loop;
const serviceId = '37924178-ee14-4f8a-9caa-ff858defea7f';
const serviceKey = '5f7bc510a5607072e1a648926e7bfe1df6a44dfb800e750a698bf5265469e6ce';
	
var config = {
  	credential: {
    	serviceId: serviceId,
    	key: serviceKey
  	},view: {
		remote: '#remoteVideo'
	},
    media: {
      recvonly: true,
      audio: false,
      video: false
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
	dummyRemon = new Remon({ config, listener });	
    setLoopJoinEvent();
    loop = setInterval(setLoopJoinEvent,1000*15);
}

//특정주기마다 castlist 목록 불러와 html Draw
var setLoopJoinEvent = async function () {  
	dummyRemon.config.credential.serviceId = serviceId;
    dummyRemon.config.credential.key = serviceKey;
	var castLists = await dummyRemon.fetchCasts();
	
	setLoopChDraw(castLists);
}
var setLoopChDraw = function(castList){
	var slideIndex= $('.vidioSlide li.active').attr('data-slide-to');
	var compIndex = $('.vidioComp div.active').attr('data-item-index');
	$('.remon').remove();
	
	var sHtml='',cHtml='';
	var sortingCastList = castList.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[0];
		var next = castNext.name.split('_')[0];
		return $(pre).text() - $(next).text();
	});
	
	for(var i=0;i<sortingCastList.length;i++){
		sHtml = '<li data-target="#carouselExampleIndicators" data-slide-to="'+(i+1)+'" class="remon '+(0==(i)?'active':'')+'"></li>';
		cHtml = '<div class="remon carousel-item '+(0==(i)?'active':'')+'" data-item-index="'+(i+1)+'" style="width: 100%;height: 100%;"><div id="remoteVideoIe'+(i+1)+'"><video id="remoteVideo'+(i+1)+'" style="width: 100%;height: 100%;background-color: black;" poster="/static/images/assets/no_video_18980.png" autoplay></video></div></div>';
		
		$('.vidioComp').append(cHtml);
		$('.vidioSlide').append(sHtml);
				
		setLoopChJoinInIn(sortingCastList[i],i+1);
	};
	
	if($('.vidioSlide .active').length < 1 || $('.vidioComp .active').length < 1) $('.board').addClass('active');
	else $('.board').removeClass('active');
};
var setLoopChJoinInIn = function(cast,i){
	dummyRemon.config.credential.serviceId = serviceId;
    dummyRemon.config.credential.key = serviceKey;
    dummyRemon.config.view.remote = '#remoteVideo'+(i);
	new Remon({ config:dummyRemon.config, listener }).joinCast(cast.name);
};



//remon관련 실행 로직
function setRemon(){	
	dummyRemon = new Remon({ config, listener });	
    setLoopJoinEvent();
//    setTimeout(setLoopJoinEvent,1000)
    // loop = setInterval(setLoopJoinEvent,1000*60);
}

//특정주기마다 castlist 목록 불러와 html Draw
var setLoopJoinEvent = async function () {  
	dummyRemon.config.credential.serviceId = serviceId;
    dummyRemon.config.credential.key = serviceKey;
	await dummyRemon.fetchCasts().then(function(data){
		setLoopChDraw(data);
	});
}
var setLoopChDraw = function(castList){
	var sortingCastList = castList.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[0].replace('remoteVideo','');
		var next = castNext.name.split('_')[0].replace('remoteVideo','');
		
		return pre-next;
	});
	
	var height = $('div.seeBox_slick ul.slider .boarder').closest('.slick-slide').height();
	
	for(var i=0;i<sortingCastList.length;i++){		 
		sHtml = '<li data-target="#carouselExampleIndicators" data-slide-to="'+(i+1)+'" class="remon '+(0==(i)?'active':'')+'"></li>';
		cHtml = '<div class="remon carousel-item '+(0==(i)?'active':'')+'" data-item-index="'+(i+1)+'" style="width: 100%;height: 100%;"><div id="remoteVideoIe'+(i+1)+'"><video id="remoteVideo'+(i+1)+'" style="width: 100%;height: 100%;" poster="/static/images/assets/no_video_18980.png" playsinline webkit-playsinline controls></video></li></div></div></div>';
		
		$('.vidioComp').append(cHtml);
		$('.vidioSlide').append(sHtml);
		 
		 setLoopChJoinInIn(sortingCastList[i],i+1);
	};
};
var remoteVideoArr = {};
var setLoopChJoinInIn = function(cast,i){
	var id = '#remoteVideo'+(i);
	var castName = cast.name;
//	if(castName.indexOf('remoteVideo1') < 0) return;
	config.credential.serviceId = serviceId;
    config.credential.key = serviceKey;
    config.view.remote = id;
    
	listener.onComplete = function(){
		console.log('comple '+castName);
		var video = remoteVideoArr[castName].context.remoteVideo;
		
		function errPlay(e){
			console.log('err : '+e);
			video.muted = true;
			video.play().then(succPlay).catch(errPlay);
		};
		function succPlay(){
			console.log('succ');
		};
		video.play().then(succPlay).catch(errPlay);		
	}
    var tmpRemon = new Remon({ config, listener }); 
    remoteVideoArr[castName] = tmpRemon;
	tmpRemon.joinCast(castName);
};