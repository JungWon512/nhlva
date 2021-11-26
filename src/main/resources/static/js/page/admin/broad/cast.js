;(function ($, win, doc) {

	let videoInputSelect = $(".device-area");
	let viewId;
	let myChannelId;
	let remon;
	let dummyRemon;
	let connectedMap = new Map();
	let audioMap = new Map();
	let key;
	let serviceId;

	var waitingLoop;
	let cameraList = [];

	let config = {
		credential: {
			serviceId: serviceId,
			key: key,
			wsurl: 'wss://signal.remotemonster.com/ws',
			resturl: 'https://signal.remotemonster.com/rest'
		},
		view: {
			remote: '',
			local: ''
		},
		media: {
			video: {
				width: '1280',
				height: '720',
				codec: 'vp8',
				maxBandwidth: 3000,
				frameRate: 30,
			},
//			video : false,
			audio : true,
			audiogroup: ''
		}
	}

	const listener = {
		// 영상 송출이 시작되었을 때
		onCreate(chid) {
			debugConsole(`EVENT FIRED: onCreate: ${chid}`);
			$('.btn_start').filter('[data-view-id="' + viewId + '"]').prop("disabled", true);
			$('.btn_stop').filter('[data-view-id="' + viewId + '"]').prop("disabled", false);
		},
		onConnect(chid) {
			debugConsole(`EVENT FIRED: onConnect: ${chid}`);
		},
		onComplete() {
			debugConsole('EVENT FIRED: onComplete');
			setViewsViaParameters(false);
		},
		onDisconnectChannel() {
			// is called when other peer hang up.
			setViewsViaParameters(false);
		},
		onClose() {
			// is called when remon.close() method is called.
			debugConsole('EVENT FIRED: onClose');
			setViewsViaParameters(false);
			$('.btn_start').filter('[data-view-id="' + viewId + '"]').prop("disabled", false);
			$('.btn_stop').filter('[data-view-id="' + viewId + '"]').prop("disabled", true);
		},
		onError(error) {
			debugConsole(`EVENT FIRED: onError: ${error}`);
		},
		onStat(result) {
			debugConsole(`EVENT FIRED: onStat: ${result}`, result);
		}
	}

	var setViewsViaParameters = function(runWaitLoop) {
		if (!runWaitLoop) {
			clearInterval(waitingLoop);
		}
	}

	var start = function() {
		setViewsViaParameters(true);
		remon = new Remon({config, listener});
		myChannelId = getRandomId();
		remon.setAudioDevice("default");
		remon.createCast(myChannelId);
		connectedMap.set(viewId, remon);
	}
	
	var stop = function() {
		if(connectedMap.has(viewId)) {
			connectedMap.get(viewId).close();
		}
	}

	var getRandomId = function() {
		var naBzplc = $("#naBzplc").val();
		var text = "";
		var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		for (var i = 0; i < 5; i++) {
			text += possible.charAt(Math.floor(Math.random() * possible.length));
		}
		return naBzplc + "_" + viewId + "_" + Date.now() + "_" + text;
	}


	var createDummyRemonForSearchLoop = function() {
		if (dummyRemon) dummyRemon.close();
		let cfg = {
			credential: {
				key: key,
				serviceId: serviceId,
				wsurl: 'wss://signal.remotemonster.com/ws',
				resturl: 'https://signal.remotemonster.com/rest'
			},
//			view: {local: '#localVideo1', remote: '#remoteVideo1'},
			media: {audio: true, video: true}
		};
		dummyRemon = new Remon({config: cfg});
	}

	var startSearchLoop = function() {
		setInterval(async function () {
			dummyRemon.config.credential.serviceId = serviceId;
			dummyRemon.config.credential.key = key;
			// 현재 Cast 리스트
			var searchResult = await dummyRemon.fetchCasts();
			searchResult.forEach((ch, i) => {
				if (ch.status === 'COMPLETE' && ch.id !== myChannelId) {
				}
			});
		}, 3000);
	}

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
					for (let i = 0; i < devices.length; i++) {
						let device = devices[i];
						debugConsole(devices[i]);
						if (device.kind === 'videoinput') {
//							debugConsole('video', device.label, device.groupId);
							cameraList.push({ text: device.label, id: device.deviceId, group : device.groupId });
						}
						else if (device.kind === 'audioinput') {
//							debugConsole('audio', device.label, device.groupId);
							audioMap.set(device.groupId, { text: device.label, id: device.deviceId });
						}
						// else if (device.kind === 'audiooutput') {
						//     speakerList.push({ text: device.label, id: device.deviceId })
						// }
					}
					for (let i=0; i< cameraList.length; i++){
						var optionText = cameraList[i].text ? cameraList[i].text : ("Device " + cameraList[i].id);
						var deviceId = cameraList[i].id;
						var videoOption = $("<option>", {text : optionText, value : deviceId, "data-group-id" : cameraList[i].group});
						videoInputSelect.append(videoOption);
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
			changeVideoInputDevice(deviceId);
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
				changeVideoInputDevice(deviceId);
				config.view.remote = "#" + viewId;
				config.view.local = "#" + viewId;
				config.media.audiogroup = audioMap.get(groupId);
				start();
			}
		});

		// 영상종료 이벤트
		$(document).on("click", ".btn_stop", function(){
			// 선택한 장치의 채널번호(viewId)를 가져온다
			viewId = $(this).data("viewId");
			config.view.remote = "#" + viewId;
			config.view.local = "#" + viewId;
			stop();
		});
	}

	var changeVideoInputDevice = function(deviceId) {
		config.media.video.deviceId = deviceId;
	}
	var changeAudioInputDevice = function(deviceId) {
		config.media.audio.deviceId = deviceId;
	}
	
	var namespace = win.auction;
	var __COMPONENT_NAME = "CAST_LIST";
	namespace[__COMPONENT_NAME] = (function () {
		var init = function(){
			videoInputSelect = $(".device-area");
			key = $("#svcKey").val();
			serviceId = $("#svcId").val();
			config.credential.key = key;
			config.credential.serviceId = serviceId;
//			createDummyRemonForSearchLoop();
//			startSearchLoop();
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
