
class Agora {
	constructor(options) {
	  this.client = AgoraRTC.createClient({mode: "live",codec: "vp8"});
	  this.options = options;
	  this.options.uid = null;
	  this.options.token = null;
	  this.options.role= "audience";
	  this.options.audienceLatency = this.options.audienceLatency||2;
	  
	  this.options.channel = $('#naBzPlc').val()+'-'+options.channelNum;
	  this.target = options.target;
	  this.height = options.height;
	  this.autoPlay = options.autoPlay||false;
	}

	agoraJoin = async function() {
		// create Agora client
		this.client.setClientRole(this.options.role, {level: this.options.audienceLatency});
		// add event listener to play remote tracks when remote user publishs.
		var agora = this;
		this.client.on("user-published", function(user, mediaType){
			agora.handleUserPublished(user, mediaType);
		});
		this.client.on("user-unpublished", function(user, mediaType){
			agora.handleUserUnpublished(user, mediaType,agora);
		});
	
		this.options.uid = await this.client.join(this.options.appid, this.options.channel, null, null);
	}
	subscribe = async function(user, mediaType) {
		const uid = user.uid;
	  	// subscribe to a remote user
		await this.client.subscribe(user, mediaType);
		if(mediaType === 'video' && this.autoPlay && this.options.channelNum == 'remoteVideo1') this.playAgora(mediaType);
	}
	stopAgora = (mediaType) => {
		if(this.user){ //user 존재 여부
			if (mediaType === 'video' && this.user.hasVideo) {
				this.user.videoTrack.stop();
				$('#player-'+this.options.channelNum).empty();
				$('#player-'+this.options.channelNum).append("<video id='remoteVideo"+this.options.channelNum+"' class='init' index='"+this.options.channelNum+"' style=\"width: 100%;background: black;\" poster=\"/static/images/assets/no_video_18980.png\" muted=\"muted\" autoplay playsinline webkit-playsinline controls></video>");
				$(this.target+' div#player-'+this.options.channelNum).height(this.height);
			}
			if (mediaType === 'audio' && this.user.hasAudio) {
				this.user.audioTrack.stop();
			}			
		}
	}
	playAgora = (mediaType) => {
		if(this.user){
			if (mediaType === 'video') {
				$('#player-'+this.options.channelNum).empty();
				//$('#player-'+this.options.channelNum+' video.init').remove();
				if(this.user.hasVideo){
					this.user.videoTrack.play('player-'+this.options.channelNum, {fit: "contain",mirror:false});
					$(this.target+' div#player-'+this.options.channelNum).height(this.height);		
					$(this.target+' #player-'+this.options.channelNum+' video').attr('controls',true);             
					$(this.target+' #player-'+this.options.channelNum+' video').attr('autopictureinpicture',false);						
				}else{
					$('#player-'+this.options.channelNum).empty();
					$('#player-'+this.options.channelNum).append("<video id='"+this.options.channelNum+"' index='"+this.options.channelNum+"' class='init' style=\"width: 100%;background: black;\" poster=\"/static/images/assets/no_video_18980.png\" muted=\"muted\" autoplay playsinline webkit-playsinline controls></video>");
					$(this.target+' div#player-'+this.options.channelNum).height(this.height);					
				}
			}
			if (mediaType === 'audio') {
				if(this.user.hasAudio) this.user.audioTrack.play();
			}
		}
	}
	
	handleUserPublished = async function (user, mediaType) {
		//print in the console log for debugging
		var agora=this; 
		const id = user.uid;  
		this.user = user;
		await this.subscribe(user, mediaType);
	}
	handleUserUnpublished = function(user, mediaType,agora) {
	  //print in the console log for debugging
		$('#player-'+this.options.channelNum).empty();
		$('#player-'+agora.options.channelNum).append("<video id='"+agora.options.channelNum+"' index='"+agora.options.channelNum+"' class='init' style=\"width: 100%;background: black;\" poster=\"/static/images/assets/no_video_18980.png\" muted=\"muted\" autoplay playsinline webkit-playsinline></video>");
		$(this.target+' div#player-'+this.options.channelNum).height(this.height);
	}
}