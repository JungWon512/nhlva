$(function() {

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
    };

    var setBinding = function() {
		for(var i=1;i<=$('#kkoSvcCnt').val();i++){
			joinChk = true;				 
			var agoraOptions = {
			  appid: agoraAppKey
			  , channel: null
			  , uid: null
			  , token: null
			  , role: "audience"
			  , audienceLatency: 2
			  , channelNum : 'remoteVideo'+ i
			  , autoPlay : true
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
