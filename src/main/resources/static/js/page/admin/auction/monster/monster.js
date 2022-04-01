$(function() {
	init();
});
var socket=null;
var init = function(){
	setLayout();
	setEventListener();
};

var setLayout = function(){
	setTileInit();
	$('footer').hide();
};
var setTileInit = function(){	
	$('.bill-table tbody td p.tit1 span').text(0);
	$('.bill-table tbody td p.tit2 span').text(0);
	$('table.connMnTable td').each(function(i){
		$(this).removeClass('bid').removeClass('connect').removeClass('ing');	
		$(this).find('p').removeClass('add').addClass('not');
		$(this).find('p').text(i+1);
	});
}

var setEventListener = function(){
	$('#btnStart').click(function(){
		$(this).prop('disabled',true);
		$('#btnStop').prop('disabled',true);
		$(this).closest('td').addClass('mt-deep').removeClass('mt-gray');
		$('#btnStop').closest('td').removeClass('mt-deep').addClass('mt-gray');
		setTileInit();
		socketStart();
	});
	$('#btnStop').click(function(){
		$('.connMnTable tbody td').removeClass("connect").removeClass("bid").removeClass("ing");
		$(this).prop('disabled',true);
		$('#btnStart').prop('disabled',false);
		$(this).closest('td').addClass('mt-deep').removeClass('mt-gray');
		$('#btnStart').closest('td').addClass('mt-gray').removeClass('mt-deep');
		if(socket){
			socket.disconnect();
		} 
	});
	$('#btnSort').click(function(){
		//setTileInit();
		//if(socket && socket.io.connected.length > 0){ 
		//	var packet = 'AK|'+$('#naBzPlc').val();
		//	socket.emit('packetData', packet);	
		//}	
				
		$.ajax({
			url: '/office/monster/getBidderCnt',
			data: {naBzplc:$('#naBzPlc').val()},
			type: 'POST',
			dataType: 'json',
			success : function(json) {
				if(json && json.data){
					$('p.bidEntrCnt').text(fnSetComma(json.data)+'명');
				}else{
					$('p.bidEntrCnt').text('0명');					
				}
			},
			error: function(xhr, status, error) {
			}
		});
		var tdArr = [];
		
		//나가지 않은 접속자 sorting 및 td 저장
		var sortingList = $('table.connMnTable').find('td.connect,td.ing').find('p.add').sort(function(a,b){
			var pre = $(a).text(),next = $(b).text(); return pre-next;
		}).each((i,item)=>{
			tdArr.push($(item).closest('td').get(0).outerHTML);
		});
		//나간 접속자 sorting 및 td 저장
		var last =$('table.connMnTable').find('td:not(.connect,.ing)').find('p.add').length;		
		for(var i=0;i<last;i++){
			tdArr.push('<td class=""><p class="not">'+(sortingList.length+1+i)+'</p></td>');
		}
		
		//남은 접속판 td 저장
		$('table.connMnTable td p:not(.add)').each((i,item)=>{
			tdArr.push($(item).closest('td').get(0).outerHTML);
		});
		
		$('.connMnTable tbody').empty();		
		var sHtml ='';
		tdArr.forEach((item,i)=>{
			var tmpStr = new String(i);
			var trIndex = tmpStr[tmpStr.length-1];
			if(trIndex == 0) sHtml+= '<tr>';
			sHtml+= item;
			if(trIndex == 9) sHtml+= '</tr>'; 
		});
		$('.connMnTable tbody').append(sHtml);
			
	});
	
	$(document).on('dblclick','.connMnTable tr td.connect,.connMnTable tr td.ing',function(){
		if(!socket || socket.disconnected){
			return;
		}
		var num = $(this).find('p').attr('userNum');
		var gubun = $(this).find('p').attr('gubun');
		
		modalComfirm('　',num+'번 응찰자의 접속을 끊으시겠습니까?',function(){ 
			$.ajax({
				url: '/api/v2/auction/select/mwmn',
				//data: {naBzplc:$('#naBzPlc').val(),aucObjDsc:aucObjDsc,lvstAucPtcMnNo:num},
				data: {naBzplc:'8808990656656',aucObjDsc:aucObjDsc,lvstAucPtcMnNo:num},
				type: 'POST',
				dataType: 'json',
				success : function(json) {
					console.log(json);
					if(json != null && json.success && json.data){
						var naBzplc = json.data.NA_BZPLC;
						var trmnAmnno = json.data.TRMN_AMNNO;
						var packet = 'AL|'+naBzplc+'|'+trmnAmnno+'|6001|'+gubun;
						socket.emit('packetData', packet);
						debugConsole(packet);
					}	
				},
				error: function(xhr, status, error) {
				}
			});		
		});
	});
}

var socketStart = function(){
	if(!$('#naBzPlc').val()){
		$('#btnStart').closest('td').addClass('mt-gray').removeClass('mt-deep');        
		$('#btnStop').closest('td').addClass('mt-deep').removeClass('mt-gray');
		$('#btnStart').prop('disabled',false);
		$('#btnStop').prop('disabled',true);
		return;
	}
	if(socket){ socket.connect(); return;}
	//var socketHost = (active == 'production')?"cowauction.kr":(active == 'develop')?"xn--e20bw05b.kr":"xn--e20bw05b.kr";
	var socketHost = "cowauction.kr";
	//socketHost += ':'+$('#webPort').val();
	socketHost += ':9001';
	socket = io.connect('https://'+socketHost+ '/6005' + '?auctionHouseCode='  + $('#naBzPlc').val(), {secure:true});

	socket.on('connect_error', function(e) {
		debugConsole(e);
		socket.disconnect();
		$('#btnStop').prop('disabled',true);
		$('#btnStart').prop('disabled',false);
		$('#btnStop').closest('td').removeClass('mt-deep').addClass('mt-gray');
		$('#btnStart').closest('td').addClass('mt-gray').removeClass('mt-deep');
	});
	
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

var getCookie = function(name){
	var cookies = document.cookie.split(';');
	for(var i = 0;i<cookies.length;i++){
		if(cookies[i].split('=')[0].trim() == name){
			return cookies[i].split('=')[1].trim();
		}
	}
}                                                                                          

var connectHandler = function() {
	$('#btnStop').prop('disabled',false);
	$('#btnStart').prop('disabled',true);
	$('#btnStop').closest('td').addClass('mt-gray').removeClass('mt-deep');
	$('#btnStart').closest('td').addClass('mt-deep').removeClass('mt-gray');
	
	var johapCd = $('#naBzPlc').val();
	var token = $('#token').val();
	var num = $('#tokenNm').val();
	
	var packet = 'AI|'+johapCd+'|'+num+'|'+token+'|6005|MANAGE|N'
	debugConsole(packet);
	socket.emit('packetData', packet);	
}

var monsterConfig = {},aucObjDsc='';
var messageHandler = function(data) {
	debugConsole(data);
	var dataArr = data.split('|');
	var gubun = dataArr[0];
	
	switch(gubun){
		case "AR" :
			monsterConfig.status = dataArr[2]=='2000'?'succ':'fail';
			if(monsterConfig.status=='fail'){
				socketDisconnect();
			}
		break;	
		case "AS" : 
			aucObjDsc=dataArr[13];
			if(monsterConfig.aucSt != dataArr[6] && (dataArr[6]=='8003' || dataArr[6]=='8004')){
				//$('.connMnTable tbody td p.add').each((i,obj)=>{
				$('.connMnTable tbody td.connect p.add, .connMnTable tbody td.ing p.add').each((i,obj)=>{
					$(obj).closest('td').removeClass("connect").removeClass("bid").removeClass("ing").addClass("connect");					 
				});
				$('.bill-table tbody td p.tit2 span').text(0);
			}
			monsterConfig.aucSt =dataArr[6];
		break;	
		case "SI" :
			if(dataArr[2] == 'null') return;			
			if($('.connMnTable tbody td p.add').toArray().some(obj=>{if($(obj).attr('usernum')==dataArr[2])return true;})){
				var userbox = $('.connMnTable tbody td p.add').toArray().find(e=>{if($(e).attr('usernum')==dataArr[2])return e;});
				//$(userbox).closest('div.border').attr("class","p-2 col-sm-1 border");
				if(dataArr[5]=='N') $(userbox).closest('td').removeClass("bid").addClass("connect");
				else if(dataArr[5]=='B') $(userbox).closest('td').addClass("bid");
				else if(dataArr[5]=='C') $(userbox).closest('td').removeClass("bid").addClass("connect");
				else if(dataArr[5]=='L') $(userbox).closest('td').removeClass("connect").removeClass("bid");
				$(userbox).attr('userNum',dataArr[2]);
				$(userbox).attr('gubun',dataArr[4]);
				$(userbox).text(dataArr[2]);				
			}else{
				var blankBox = $('.connMnTable tbody td p.not')[0];
				$(blankBox).removeClass('not');
				$(blankBox).addClass('add');
				$(blankBox).attr('userNum',dataArr[2]);
				$(blankBox).attr('gubun',dataArr[4]);
				//$(blankBox).closest('td').attr("class","p-2 col-sm-1 border");
				if(dataArr[5]=='N') $(blankBox).closest('td').addClass("connect");
				else if(dataArr[5]=='B') $(blankBox).closest('td').addClass("bid");
				else if(dataArr[5]=='C') $(blankBox).closest('td').removeClass("bid").addClass("connect");
				else if(dataArr[5]=='L') $(blankBox).closest('td').removeClass("connect").removeClass("bid").removeClass("ing");
				$(blankBox).text(dataArr[2]);				
			}
			calcConnectorCnt();
		break;	
		case "AC" : //응찰 취소
			if($('.connMnTable tbody td p.add').toArray().some(obj=>{if($(obj).attr('usernum')==dataArr[3])return true;})){
				var userbox = $('.connMnTable tbody td p.add').toArray().find(e=>{if($(e).attr('usernum')==dataArr[3])return e;});
				
				//$(userbox).closest('div.border').attr("class","p-2 col-sm-1 border");
				$(userbox).closest('td').addClass("connect");
				
				$(userbox).attr('userNum',dataArr[3]);
				$(userbox).text(dataArr[3]);
			}else{
				var blankBox = $('.connMnTable tbody td p.not')[0];
				$(blankBox).removeClass('not');
				$(blankBox).addClass('add');
				$(blankBox).attr('userNum',dataArr[3]);
				//$(blankBox).closest('div.border').attr("class","p-2 col-sm-1 border");
				$(blankBox).closest('td').addClass("connect");
				$(blankBox).text(dataArr[3]);				
			}
		break;	
		
		case "AF" : //낙유찰 결과
			var userbox = $('.connMnTable tbody td p.add').toArray().find(e=>{if($(e).attr('usernum')==dataArr[5])return e;});
			
			$(userbox).closest('div.border').attr("class","p-2 col-sm-1 border");
			switch(dataArr[3]){
				case '11' : $(userbox).closest('td').removeClass("connect").removeClass("bid"); break; //대기
				case '22' : $(userbox).closest('td').removeClass("connect").removeClass("bid").addClass("ing"); break; //낙찰
				// case '23' : $(userbox).closest('div.border').addClass("bid"); break; //보류
				default:break;
			}
						
			$(userbox).attr('userNum',dataArr[5]);
			//$(blankBox).closest('div.border').attr("class","p-2 col-sm-1 border bg-warning");
			$(userbox).text(dataArr[5]);
		break;
		default:break;
	}
}

var disconnectHandler = function() {	
	$('#btnStop').prop('disabled',true);
	$('#btnStart').prop('disabled',false);
//	$('#btnStop').closest('td').removeClass('mt-deep').addClass('mt-gray');
//	$('#btnStart').closest('td').addClass('mt-gray').removeClass('mt-deep');
}
var calcConnectorCnt = function(){
	var connCnt = $('td.connect,td.ing',$('.connMnTable tbody')).length;
	var bidCnt = $('td.bid,td.ing',$('.connMnTable tbody')).length;
	$('.bill-table tbody td p.tit1 span').text(connCnt);
	$('.bill-table tbody td p.tit2 span').text(bidCnt);
}