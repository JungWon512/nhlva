<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>
<script src="/static/js/socket.io/socket.io.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery-1.12.4.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.easing.1.3.min.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.mmenu.min.all.js"></script>
<script type="text/javascript" src="/static/js/guide/slick.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.placeholder.ls.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.mCustomScrollbar.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.selectric.min.js"></script>
<script type="text/javascript" src="/static/js/guide/jquery.easypiechart.js"></script>
<html>	
	<div class="option">
		<select id="selMode">
			<option value="1" selected>개발</option>
			<option value="2">운영</option>
			<option value="0">local(23)</option>
		</select>
		<select id="selNaBzPlc">
			<option value="8808990837314" selected>안성축협</option>
			<option value="8808990656656">하동축협</option>
			<option value="8808990657202">무진장축협</option>
			<option value="8808990660783">임실축협</option>
		</select>
		<button type="button" onclick="getWatchToken();">토큰</button>
		<button type="button" onclick="socketStart();">시작</button>
		<button type="button" onclick="socketStop();">중지</button>
	</div>
	<div class="log_div">
		<table border = 1>
			<tbody>
				<tr>
					<td style="width:100vh;height:25px;"></td>
				</tr>
			</tbody>
		</table>
	</div>
</html>

<script>

var socket=null,token=null;
var socketStart = function(){
	if(token == null){alert('token정보가 없습니다.'); return;};
	
	if(socket && !socket.disconnected){
		socket.disconnect();
		socket=null;
	}
	
	if($('#selMode').val() == '0'){		
		socket = io.connect('http://'+'192.168.0.23:9001'+ '/6003' + '?auctionHouseCode='  + $('#selNaBzPlc').val());
	}else if($('#selMode').val() == '1'){
		socket = io.connect('https://'+'xn--e20bw05b.kr:9001'+ '/6003' + '?auctionHouseCode='  + $('#selNaBzPlc').val(), {secure:true});
	}else if($('#selMode').val() == '2'){
		socket = io.connect('https://'+'cowauction.kr:9001'+ '/6003' + '?auctionHouseCode='  + $('#selNaBzPlc').val(), {secure:true});
	}
	
	//socket.on('connect_error', connectErr);
	socket.on('connect', connectHandler);
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
var socketStop = function(){
	socket.disconnect();
	socket = null,token = null;
	var tr = $('table tbody tr:eq(0)').clone();
	$('table tbody').empty();	
	tr.appendTo('table tbody');
}


var socketDisconnect = function(){
	socket.disconnect();	
}
var connectHandler = function() {
	var johapCd = $('#selNaBzPlc').val();
	var num = 'WATCHER';
	var tmpToken = token;
	var packet = 'AI|'+johapCd+'|'+num+'|'+tmpToken+'|6003|WEB'
	socket.emit('packetData', packet);	
}
var messageHandler = function(data) {
	console.log(data);
	var tr = $('table tbody tr:eq(0)').clone();
	//var td = $('table tbody tr:eq(0) td').clone();
	tr.find('td').text(data);
	tr.appendTo('table tbody');
}
var getWatchToken = function(){
	var param = {naBzPlc:$('#selNaBzPlc').val()};
	$.ajax({
	    url: "/auction/api/getWatchToken",
	    type: "post",	    
	    data:JSON.stringify(param),
	    contentType: "application/json",
	    dataType: "json",
	    success: function(data) {
	        console.log(data);
	        if(data && data.success){
	        	token=data.token;
	        }else{
	        	token=null;
	        }	        
	    }
	});
};
</script>