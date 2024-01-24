$](function() {

    var setLayout = function() {
		$(".seeBox_slick ul.slider").slick({
			dots: false,
			arrows: false,
			adaptiveHeight: true,
		});
		
		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			var index = currentSlide+1;
			$('div[id^=player-remoteVideo]:not([id=player-remoteVideo'+index+'])').each(function(){
				var index = $(this).attr('index');
				agoraArr[index].stopAgora('video');
			});
			agoraArr[index].playAgora('video');
		});
		var chkTimeInterval = setInterval(function(){
			chekcTimeAgoraReave();
		},30 * 1000); //20초?
    };

    var setBinding = function() {
		for(var i=1;i<=$('#kkoSvcCnt').val();i++){
			joinChk = true;				 
			var agoraOptions = {
			  channel: null
			  , appid : $('#kkoSvcKey').val()
			  , uid: null
			  , token: null
			  , role: "audience"
			  , audienceLatency: 1
			  , channelNum : 'remoteVideo'+ i
			  , autoPlay : true
			  , tagNone : true
			  , target : 'div.seeBox_bottom.vidioSlide .seeBox_slick ul.slider li.video_item'
			  , height : '93vh'
			};
			agoraArr[i] = new Agora(agoraOptions);
			agoraArr[i].agoraJoin();
		}		
    };

    setLayout();    
    setBinding();
});
var agoraArr=[];

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