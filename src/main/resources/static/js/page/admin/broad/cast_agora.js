;(function ($, win, doc) {

	let videoInputSelect = $(".device-area");
	let viewId;
	let audioMap = new Map();

	// 선택 가능한 카메라, 오디오 장치를 가져온다.
	var getDevices = async function () {
		if (!navigator.mediaDevices || !navigator.mediaDevices.enumerateDevices) {
			debugConsole("enumerateDevices()를 지원하지 않습니다.");
			return;
		}
		
		await navigator.mediaDevices.getUserMedia({audio:true, video:true})
			.then(async function(stream){
				if (stream.active) {
					var devices = await navigator.mediaDevices.enumerateDevices();
					var videoIndex = 0;
					for (let i = 0; i < devices.length; i++) {
						let device = devices[i];						
						//debugConsole(devices[i]);
						if (device.kind === 'videoinput') {
							console.log(stream.getVideoTracks());
							var cameraDevice = { text: device.label, id: device.deviceId, group : device.groupId };
							var optionText = cameraDevice.text ? cameraDevice.text : ("Device " + cameraDevice.id);
							var videoOption = $("<option>", {text : optionText, value : cameraDevice.id, "data-group-id" : cameraDevice.group,"data-index":videoIndex++});
							videoInputSelect.append(videoOption);							
							
							cameraList.push(cameraDevice);
						}
						else if (device.kind === 'audioinput') {
							audioMap.set(device.groupId, { text: device.label, id: device.deviceId });
						}
					}
				}
			})
			.catch(function(e){
				debugConsole("getUserMedia()를 사용할 수 없습니다 : ", e);
			});
	}
	
	var setBinding = function() {
		// 채널 Video장치 Dropdown 선택 이벤트
		$(document).on("click", ".dropdown-item", function(){
			$(this).addClass("active");
			$(this).siblings("a").removeClass("active");
			// 선택한 장치의 채널번호(viewId)를 가져온다
			viewId = $(this).closest(".dropdown-menu").data("viewId");
			// 선택한 장치 ID
			var deviceId = $(this).data("deviceId");
		});
		
		// 채널 Mic장치 Dropdown 선택 이벤트
		$(document).on("click", ".dropdown-item-mic", function(){
			// 선택한 장치의 채널번호(viewId)를 가져온다
			viewId = $(this).closest(".dropdown-menu").data("viewId");
			// 선택한 장치 ID
			var deviceId = $(this).data("deviceId");
			changeAudioInputDevice(deviceId);
		});

		// 방송시작 이벤트
		$(document).on("click", ".btn_start", function(){
			// 선택한 장치의 채널번호(viewId)를 가져온다
			viewId = $(this).data("viewId");
			var deviceId = $('select.custom-select').filter('[data-view-id="' + viewId + '"]').val();
			var groupId = $('select.custom-select').filter('[data-view-id="' + viewId + '"]').find("option:selected").data("groupId");
			if (deviceId == "" || deviceId == null) {
				alert("채널을 선택하세요");
			}
			else {
				$('.btn_start').filter('[data-view-id="' + viewId + '"]').prop("disabled", true);
				$('.btn_stop').filter('[data-view-id="' + viewId + '"]').prop("disabled", false);
				agoraArr[viewId]= new Agora({
//				  appid: agoraAppKey
				  appid : $('#svcKey').val()
				  , channel: null
				  , uid: null
				  , token: null
				  , role: "host"
				  , audienceLatency: 2
				  , channelNum : viewId
				  , cameraIndex : $('select[data-view-id='+viewId+'] option:checked').data('index')				  
				});
				agoraArr[viewId].agoraJoin();
			}
		});

		// 영상종료 이벤트
		$(document).on("click", ".btn_stop", function(){
			// 선택한 장치의 채널번호(viewId)를 가져온다
			viewId = $(this).data("viewId");
			$('.btn_start').filter('[data-view-id="' + viewId + '"]').prop("disabled", false);
			$('.btn_stop').filter('[data-view-id="' + viewId + '"]').prop("disabled", true);
			leave(agoraArr[viewId]);
		});
	}

	var changeVideoInputDevice = function(deviceId) {
	}
	var changeAudioInputDevice = function(deviceId) {
	}
	
	var namespace = win.auction;
	var __COMPONENT_NAME = "CAST_LIST";
	namespace[__COMPONENT_NAME] = (function () {
		var init = function(){
			videoInputSelect = $(".device-area");
			getDevices();	// 선택 가능한 카메라, 오디오 장치 세팅
			setBinding();	// 버튼 이벤트
		};
		return {
			init: init
		}
	})();
	$(function () {
		namespace[__COMPONENT_NAME].init();
	});
})(jQuery, window, document);

//Agora 관련 로직
var agoraArr=[];
let cameraList = [];
var joinChk = false;
var remoteUser = [];
let videoHeight=null;
class Agora {
	
	localTracks = {videoTrack: null,audioTrack: null};
	constructor(options) {
	  this.options = options;
	  this.options.channel = $('#naBzplc').val()+'-'+options.channelNum;    
	  this.client = AgoraRTC.createClient({mode: "live",codec: "vp8"});
	}
	agoraJoin = async function() {
		// create Agora client
		this.client.setClientRole(this.options.role);
	
		this.options.uid = await this.client.join(this.options.appid, this.options.channel, null, null);
		  // create local audio and video tracks
		  if (!this.localTracks.audioTrack) {
		    this.localTracks.audioTrack = await AgoraRTC.createMicrophoneAudioTrack({encoderConfig: "music_standard"});
		  }
		  if (!this.localTracks.videoTrack) {
			const newTrack = await navigator.mediaDevices.getUserMedia({audio: true, video: true}).then(mediaStream => {
				console.log(mediaStream.getVideoTracks());
				mediaStream.getVideoTracks()[0];
			});
//		    {
//				mediaStreamTrack: newTrack
//			}
		    this.localTracks.videoTrack = await AgoraRTC.createCameraVideoTrack({
				cameraId: cameraList[this.options.cameraIndex].id
				,mediaStreamTrack: newTrack
				,encoderConfig : '720p_1'
			});
		    //const newTrack = this.localVideoTrack.getMediaStreamTrack();
			// Replaces and stops the current video track		    
			//await this.localTracks.videoTrack.replaceTrack(newTrack, true);
		  }
		  // play local video track
		  $('#'+this.options.channelNum).empty();
		  this.localTracks.videoTrack.play(this.options.channelNum,{fit:'fit'});	
		  // publish local tracks to channel
		  await this.client.publish(Object.values(this.localTracks));
		  console.log("publish success");
		  $('#'+this.options.channelNum+' video').height(300);
	}
}
async function leave(agora) {	
	for (trackName in agora.localTracks) {
		var track = agora.localTracks[trackName];
		if (track) {
		  track.stop();
		  track.close();
		  await agora.client.unpublish(agora.localTracks[trackName]);
		  agora.localTracks[trackName] = undefined;
		}
	}
	
	// leave the channel
	await agora.client.leave();
	$('#'+agora.options.channelNum).append("<div style='width: 100%; height: 100%; position: relative; overflow: hidden; background-color: black;'><video autoplay muted style='height:300px;'></video></div>");
	agoraArr[agora.options.channelNum] = undefined;						
}