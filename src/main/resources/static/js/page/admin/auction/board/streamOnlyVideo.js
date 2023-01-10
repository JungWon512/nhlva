$(function() {

    var setLayout = function() {
		$(".seeBox_slick ul.slider").slick({
			dots: false,
			arrows: false,
			adaptiveHeight: true,
		});
		
		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			$('video[id^=remoteVideo]').each(function(){
				if($(this).attr('id') == 'remoteVideo1') return;
				$(this).get(0).muted = true;
				if(!$(this).get(0).paused) $(this).get(0).pause();
			});
			slickIndex=currentSlide;
			
			var callback = function(currentSlide){
				var video =$('#remoteVideo'+(currentSlide)).get(0);				
				if(video && video.paused) video.play();
			}
			setRemonJoinRemote(currentSlide+1,callback);
		});
    };

    var setBinding = function() {
		setRemon();
    };

    setLayout();    
    setBinding();
});

//소켓통신 connect 및 이벤트 바인딩
var scCnt=0,auctionConfig={},slickIndex=0;
//쿠키값얻는 function
var getCookie = function(name){
	var cookies = document.cookie.split(';');
	for(var i = 0;i<cookies.length;i++){
		if(cookies[i].split('=')[0].trim() == name){
			return cookies[i].split('=')[1].trim();
		}
	}
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
	config.credential.serviceId =  $('#kkoSvcId').val();
	config.credential.key = $('#kkoSvcKey').val();
	dummyRemon = new Remon({ config, listener });	
    setLoopJoinEvent();
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
	debugConsole(sortingCastList);
		
	if(sortingCastList.length > 0){
		for(var i=0;i<sortingCastList.length;i++){
			if($('#kkoSvcCnt').val() <= i) return;
			 $('#remoteVideo'+(i+1)).attr('castName',sortingCastList[i].name);
			 if(i==0) setLoopChJoinInIn(sortingCastList[i],i+1);
	 	};	
		
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
		debugConsole(data);		
		var castList = data.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo'+index)>=0) return this;})
			.sort(function(castPre,castNext){
			var pre = castPre.name.split('_')[1].replace('remoteVideo','');
			var next = castNext.name.split('_')[1].replace('remoteVideo','');
			return pre-next;
		});
		debugConsole(castList);
		
		if(castList.length > 0){
			if($('#kkoSvcCnt').val() < index) return;
			 $('#remoteVideo'+(index)).attr('castName',castList[0].name);
			 setLoopChJoinInIn(castList[0],index);
		
			var castName = $('#remoteVideo1').attr('castName');
			if(callback)callback(index);
		}
	});
}

var setLoopChDraw = function(castList){
	debugConsole(castList);
	var sortingCastList = castList.filter(function(cast){if(cast.name.indexOf($('#naBzPlc').val()+'_remoteVideo')>=0) return this;})
		.sort(function(castPre,castNext){
		var pre = castPre.name.split('_')[1].replace('remoteVideo','');
		var next = castNext.name.split('_')[1].replace('remoteVideo','');
		
		return pre-next;
	});
			
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
	}else{
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
