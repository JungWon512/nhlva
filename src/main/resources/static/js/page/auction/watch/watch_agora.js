$(function() {    
	
    var setLayout = function() {	
		videoHeight = $('.seeBox_slick ul.slider li.boarder').height();	
		$('.seeBox_slick ul.slider li.video_item video').each((i,o)=>{
			$(o).height(videoHeight);
		});
		$(".seeBox_slick ul.slider").slick({dots: true});
		$('.m_sound').removeClass('fix_right');
		$('.m_sound').addClass('off');
		
		$('.seeBox_slick ul.slider').on('afterChange', function(event, slick, currentSlide, nextSlide){
			$('div[id^=player-remoteVideo]:not([id=player-remoteVideo'+currentSlide+'])').each(function(){
				var index = $(this).attr('index');
				agoraArr[index].stopAgora('video');
				agoraArr[index].stopAgora('audio');
			});
			if(currentSlide == '0'){
				if(!$('.m_sound').hasClass('off')) agoraArr[1].playAgora('audio');				
			}else{
				agoraArr[currentSlide].playAgora('video');
				if(!$('.m_sound').hasClass('off')){
					if(currentSlide == 0) agoraArr[1].playAgora('audio');
					else agoraArr[currentSlide].playAgora('audio');
				}
				//if(!$('.m_sound').hasClass('off') && currentSlide == '1') agoraArr[1].playAgora('audio');				
			}
			//currentSlide
		});
		
		$('.chart').easyPieChart({
			barColor: '#007eff',
			trackColor: '#dbdbdb',
			lineCap: 'round',
			lineWidth: 18,
			size: 344,
			animate: 1000,
		});		
		if($('#aucDsc').val() == '2'){
			calcPiePercent();
			$(".tblAuction .list_body ul li").removeClass('act');
			$(document).on('click',".tblAuction .list_body ul li",function(){
				$('.boarder ul li dd.auctionNum').text($(this).find('dd.aucPrgSq').text());
				//$('.boarder ul li dd.ftsnm').html(($(this).find('dd.ftsnm').text()||'').substring(0,3));
				$('.boarder ul li dd.ftsnm').html(($(this).find('dd.ftsnm').text()||''));
				$('.boarder ul li dd.sex').text($(this).find('dd.indvSexC').text());
				$('.boarder ul li dd.cowSogWt').text(fnSetComma($(this).find('dd.cowSogWt').text()));
				$('.boarder ul li dd.matime').text($(this).find('input.matime').val());	//산차
				$('.boarder ul li dd.sraIndvPasgQcn').text($(this).find('dd.sraIndvPasgQcn').text());
				$('.boarder ul li dd.mcowDsc').text($(this).find('input.mcowDsc').val());	//어미
				$('.boarder ul li dd.lowsSbidLmtAm').text(fnSetComma($(this).find('dd.lowsSbidLmtAm').text()));
				$('.boarder ul li dd.kpnNo').text($(this).find('dd.kpnNo').text());
				$('.boarder ul li dd.rmkCntn p').text($(this).find('dd.rmkCntn').text());
			});
		}
    };

    var setBinding = function() {
		$(".m_sound").on('click', function(e){
			if(joinChk && agoraArr[1].user && agoraArr[1].user.audioTrack){
				if(!agoraArr[1].user.audioTrack.isPlaying){
					agoraArr[1].playAgora('audio');
				}else{
					agoraArr[1].stopAgora('audio');
				}
			}
			$(".m_sound").toggleClass('off');
		});
		
//		$("button.btn_reload").click(function(){
//			location.reload();
//		});
		
		if($('#aucDate').val() != getTodayStr().replaceAll('-','')){
			modalAlert('','경매일이 아닙니다.',function(){pageMove('/main', false);});
			return;
		}else{
			socketStart();
			setAgora();
		}
		
    };

    setLayout();    
    setBinding();
});

//소켓통신 connect 및 이벤트 바인딩
var socket=null,auctionConfig={},scData={};
var socketStart = function(){
	if(!$('#naBzPlc').val()) return;        
	if(socket){ socket.connect(); return;}
	var socketHost = (active == 'production')?"cowauction.kr":(active == 'develop')?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
//	var socketHost = '192.168.0.129';
//	socketHost += ':'+$('#webPort').val();
	socketHost += ':9001';
	socket = io.connect('https://'+socketHost+ '/6003' + '?auctionHouseCode='  + $('#naBzPlc').val(), {secure:true});
//	socket = io.connect('http://'+socketHost+ '/6003' + '?auctionHouseCode='  + $('#naBzPlc').val());

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
	modalAlert('','경매가 종료되었습니다.',function(){pageMove('/main', false);});
	
//	if(socket.connected) {clearInterval(socketConnectTimeInterval);}
//	else socket.connect();
}

//소켓통신 disconnect Event
var socketDisconnect = function(){
	socket.disconnect();	
}
//소켓통신 connect시 Event
var connectHandler = function() {
	//구분자|조합구분코드|회원번호|인증토큰|접속요청채널|사용채널|관전자여부
	$('#btnStop').prop('disabled',false);
	$('#btnStart').prop('disabled',true);
	//var token = getCookie('access_token');
	var johapCd = $('#naBzPlc').val();
	var num = 'WATCHER';
	var tmpToken = $('#token').val();
	var packet = 'AI|'+johapCd+'|'+num+'|'+tmpToken+'|6003|WEB'
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
				, selSts: dataArr[6]
				, lowsSbidLmtAm: dataArr[4]
			};							

			if(auctionConfig.asData.curAucSeq) auctionConfig.asData.preAucSeq = auctionConfig.asData.curAucSeq
			auctionConfig.asData.curAucSeq = dataArr[2];
			auctionConfig.asData.rgSqno = dataArr[12];
			auctionConfig.asData.aucObjDsc = dataArr[13];
			
			if($('#aucDsc').val() == '2' && (tmpAsDAta.selSts=='8001' || tmpAsDAta.selSts=='8002' || tmpAsDAta.selSts=='8006')){
				var params = {
					naBzplc : $('#naBzPlc').val()
					,aucObjDsc : auctionConfig.asData.aucObjDsc
					,rgSqno : auctionConfig.asData.rgSqno
					,aucDsc : $('#aucDsc').val()
				}
				fnAuctionReload(params,tmpAsDAta,function(){
					var tr = getTrRow(auctionConfig.asData.curAucSeq);
					tr.find('dl dd.lowsSbidLmtAm').text(fnSetComma(Math.round(tmpAsDAta.lowsSbidLmtAm))+'');
					if($('#aucDsc').val() == '1') changeTrRow(tr);
					calcPiePercent();					
				});
			}else if($('#aucDsc').val() == '1'){
				var tr = getTrRow(auctionConfig.asData.curAucSeq);
				tr.find('dl dd.lowsSbidLmtAm').text(fnSetComma(Math.round(tmpAsDAta.lowsSbidLmtAm))+'');
				changeTrRow(tr);
				calcPiePercent();					
			}
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
			calcPiePercent();
			//}
			tr.find('dl dd.sraSbidAm').text(fnSetComma(Math.round(tmpAsDAta.sraSbidAm)));
			changeTrRow(tr);	
		break;	
		default:break;
	}
}

//출품정보 변경시 row변경 로직
var changeTrRow = function(tr) {
	if(tr && tr.length > 0){
		$(".tblAuction .list_body ul li").removeClass('act');
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

function calcPiePercent(){
	var totCnt = $('.tblAuction li').length; //table 전체 row 수
	var stantNotCnt = $('.tblAuction li:not(.noInfo)').filter(function(i,obj){var text = $(obj).find('.selSts').text(); if(text !='대기') return obj;}).length; //table 대기 상태가아닌 row수
	var stantCnt = $('.tblAuction li').filter(function(i,obj){var text = $(obj).find('.selSts').text(); if(text =='대기') return obj;}).length; //table 대기 상태인 row 수
	var result = new Number(stantNotCnt)/new Number(totCnt) *100;
	result = result.toFixed(0);
	$('.chart').attr('data-percent',result);
	$('.chart dl dd.stand span').text(stantCnt);
	$('.chart dl dt').text(result+'%');
	$('.chart dl dd.bid span').text(stantNotCnt);
	$('.chart').data('easyPieChart').update(result);
	

	$('div.stats-box div.num.stand span').text(stantCnt);
	$('div.stats-box div.num.bid span').text(stantNotCnt);
}

//agora 영성 관련 로직
var agoraArr=[];
var joinChk = false;
let videoHeight=null;


var setAgora = function(){	
	for(var i=1;i<=$('#kkoSvcCnt').val();i++){
		joinChk = true;				 
		var agoraOptions = {
		  audienceLatency : 1
		  , appid : $('#kkoSvcKey').val()
		  , channelNum : 'remoteVideo'+ i
		  , target : '.seeBox_slick ul.slider li.video_item'
		  , height : videoHeight+'px'
		};
		agoraArr[i] = new Agora(agoraOptions);
		agoraArr[i].agoraJoin();		
	}				
}
var scLoad = function(dataArr){	
	if($('#aucDsc').val() == '2') return;
	if(!auctionConfig.scData) auctionConfig.scData = {};
	//if(auctionConfig.scData.curAucSeq) auctionConfig.scData.preAucSeq = auctionConfig.scData.curAucSeq
	auctionConfig.scData.curAucSeq = dataArr[2];
				
	//관전 전광판 데이터 update				
	$('.vidioSlide li.boarder ul dl dd.auctionNum').text(dataArr[2]);
	$('.vidioSlide li.boarder ul dl dd.ftsnm').html(nameEnc(dataArr[9]));
	$('.vidioSlide li.boarder ul dl dd.sex').text(dataArr[13]);
	$('.vidioSlide li.boarder ul dl dd.cowSogWt').text(fnSetComma(dataArr[25]));
	$('.vidioSlide li.boarder ul dl dd.matime').text(dataArr[16]);
	$('.vidioSlide li.boarder ul dl dd.mcowDsc').text(dataArr[14]); //어미								
	$('.vidioSlide li.boarder ul dl dd.sraIndvPasgQcn').text(dataArr[18]);
	$('.vidioSlide li.boarder ul dl dd.kpnNo').text(dataArr[11] && dataArr[12].replace('KPN',''));
	$('.vidioSlide li.boarder .seeBox_slick_inner ul dl dd.lowsSbidLmtAm').text(fnSetComma(Math.round(dataArr[27]))+''); //24 :최초낙찰 ,25:최저낙찰
	$('.vidioSlide li.boarder .mo_seeBox ul dl dd.lowsSbidLmtAm').text(fnSetComma(Math.round(dataArr[27]))+''); //24 :최초낙찰 ,25:최저낙찰
	$('.vidioSlide li.boarder ul dl dd.rmkCntn p').text(dataArr[28]);
	convertDefaultValue('.vidioSlide li.boarder ul dl dd');
	var tr = getTrRow(auctionConfig.scData.curAucSeq);
	tr.find('dl dd.ftsnm').html(nameEnc(dataArr[9]));
	tr.find('dl dd.cowSogWt').text(fnSetComma(dataArr[25]));
	tr.find('dl dd.lowsSbidLmtAm').text(fnSetComma(dataArr[27])+'');
	tr.find('dl dd.sraSbidAm').text(fnSetComma(dataArr[31])+'');
	tr.find('dl dd.rmkCntn').text(dataArr[28]);
	
	convertDefaultValue(tr.find('dl dd'));			
	calcPiePercent();
	//changeTrRow(tr);
}

var fnAuctionReload = function(params,tmpAsDAta,callback){		
	$.ajax({
		url: '/front/getCowList',
		data: params,
		type: 'POST',
		dataType: 'json',
		success : function() {
		},
		error: function(xhr, status, error) {
		}
	}).done(function (json) {
		debugConsole(json);
		if(json && json.list ){						
			if(!isApp() && chkOs() == 'web'){
				$(".list_body ul").mCustomScrollbar('destroy');							
			}
			
			$(".list_body ul").empty();
			var arr = json.list;
			$('span.watchCowCnt').text(arr.length);
			if(arr.length > 1){
				arr.forEach(function(vo,i){
					var html = '';
					html +='<li class="'+(i==0?'':'')+'">';
					html +='  <input type="hidden" name="mcowDsc" class="mcowDsc" value="'+vo.MCOW_DSC_NAME +'"/>';
					html +='  <input type="hidden" name="hidden" class="hidden" value="'+vo.MATIME +'"/>';
					html +='  <dl>';
					html +='    <dd class="date aucDt">'+ vo.AUC_DT_STR +'</dd>';
					html +='    <dd class="num aucPrgSq">'+ vo.AUC_PRG_SQ +'</dd>';
					html +='    <dd class="name ftsnm">'+ vo.FTSNM +'</dd>';
					html +='    <dd class="pd_ea sraIndvAmnno">'+ vo.SRA_INDV_AMNNO_FORMAT +'</dd>';
					html +='    <dd class="pd_sex indvSexC">'+ vo.INDV_SEX_C_NAME +'</dd>';
					html +='    <dd class="pd_kg cowSogWt textNumber">'+fnSetComma(vo.COW_SOG_WT)+'</dd>';
					html +='    <dd class="pd_kpn kpnNo">'+ vo.KPN_NO_STR +'</dd>';
					html +='    <dd class="pd_num1 sraIndvPasgQcn">'+ vo.SRA_INDV_PASG_QCN +'</dd>';
					html +='    <dd class="pd_pay1 lowsSbidLmtAm textNumber">'+fnSetComma(vo.LOWS_SBID_LMT_UPR)+'</dd>';
					html +='    <dd class="pd_pay1 sraSbidAm textNumber">'+fnSetComma(vo.SRA_SBID_UPR)+'</dd>';
					html +='    <dd class="pd_state selSts">'+ vo.SEL_STS_DSC_NAME +'</dd>';
					html +='    <dd class="pd_etc rmkCntn">'+ (vo.RMK_CNTN || '').replace('|',',') +'</dd>';
					html +='  </dl>';
					html +='</li>';
					$(".list_body ul").append(html);
				});							
			}else{							
				$(".list_body ul").append('<li><dl><dd>경매관전 자료가 없습니다.</dd></dl></li>');
			}
			
			if(isApp() || chkOs() != 'web'){
				$('.tblAuction .list_body ul').animate({scrollTop: 0},0);
			}else{
				$(".list_body ul").mCustomScrollbar({theme:"dark-thin",scrollInertia: 200});						
				$(".list_body ul").mCustomScrollbar('scrollTo'
					,$(".list_body ul").find('.mCSB_container').find('li:eq(0)')
					,{scrollInertia:0}
				);
				
			}
			if(callback) callback();
		}	
	});
}