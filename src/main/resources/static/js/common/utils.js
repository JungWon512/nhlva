var sFlag=false;
// 페이지 이동 함수
var pageMove = function(uri, chkSearch, addParam) {
	var temp = !chkSearch ? window.location.search.split("&") : [];
	var params = temp.filter(function(el) {return el != "type=0" && el != "type=1" && el.startsWith("?place=")});
	
	if(addParam != undefined){
		params.push(addParam);
	}
	
	var url = (uri == "/home" || uri == "/district") ? window.location.origin + uri : window.location.origin + uri + params.join("&");
	
	if ($("#deviceMode").val() == "PC") {
		location.href = url;
	}
	else {
		try {
			// 안드로이드
			if (window.auctionBridge) {
				window.auctionBridge.moveWebPage(url);
			}
			// 아이폰
			else if(isIos()) {
				window.webkit.messageHandlers.moveWebPage.postMessage(url);
			}
			else {
				location.href = url;
			}
		}
		catch(e) {
			location.href = url;
		}
	}
};

var isAnd = function() {
	if(navigator.userAgent.match(/Android/i)
	&& window.auctionBridge != undefined) {
		return true
	}
	else {
		return false;
	}
}

var isIos = function(){
	if(navigator.userAgent.match(/iPhone|iPad|iPod/i)
	&& window.webkit !=undefined
	&& window.webkit.messageHandlers != undefined) {
		return true;
	}
	else {
		return false;
	}
};

var isApp = function() {
	if (isIos() || isAnd()) {
		return true;
	}
	else {
		return false;
	}
};


// 경매 앱으로 이동
var goAuctionApp = function(place) {
	try {
		// 안드로이드
		if (window.auctionBridge) {
			window.auctionBridge.moveAuctionBid();
		}
		// 아이폰
		else if(isIos()) {
			window.webkit.messageHandlers.moveAuctionBid.postMessage(true);
		}
		else {
			location.href = window.location.origin + '/bid' + window.location.search
		}
	}
	catch(e) {
		location.href = window.location.origin + '/bid' + window.location.search
	}
};

var goWatchApp = function() {
	try {
		var params = {
			"url" : window.location.origin + '/watchApp' + window.location.search
			, "watch_token" : getCookie('watch_token')
		};
		// 안드로이드
		if (window.auctionBridge) {
			window.auctionBridge.moveAuctionWatch(JSON.stringify(params));
		}
		// 아이폰
		else if(isIos()) {
			window.webkit.messageHandlers.moveAuctionWatch.postMessage(JSON.stringify(params));
		}
		else {
			location.href = window.location.origin + '/watch' + window.location.search;
		}
	}
	catch(e) {
		location.href = window.location.origin + '/watch' + window.location.search;
	}
}

var goLoginPage = function() {
	modalComfirm(""
				, "로그인이 필요한 서비스입니다.<br/>로그인 하시겠습니까?"
				, function(){
					pageMove('/user/login');
				});
	return;
};

var logoutProc = function() {
	$("nav#menu-lnb").data( "mmenu" ).close();
	var token = getCookie('access_token');
	modalComfirm(""
				, "로그아웃 하시겠습니까?"
				, function(){
					pageMove('/user/logoutProc', false, 'connChannel=' + connChannel() + '&token=' + token);
				});
	return;
};

var dashboardProc = function(uri, bzPlcLoc) {
	// 20230118 대시보드 하단 버튼 전국으로 고정
//	pageMove(uri, true, '?searchPlace=' + bzPlcLoc);
	pageMove(uri);
	return;
};

var getStringValue = function(str, defaultStr) {
	str = new String(str);
	defaultStr = (defaultStr == undefined) ? "-" : defaultStr;
	return (str == undefined || str == null || str.trim() == "" ||  str.trim().toLowerCase() == "null") ? defaultStr : str;
};

var convertDefaultValue = function(target,defaultStr) {	
	$(target).each(function(){
		if($(this).children().get(0)){
			var tagNm = $(this).children().get(0).localName;
			$(this).find(tagNm).text(getStringValue($(this).text().trim(),defaultStr));
		}else{
			$(this).text(getStringValue($(this).text().trim(),defaultStr));			
		}
	});
};

var chkOs = function() {
	var osChk = navigator.userAgent.toLowerCase();
	if(osChk.indexOf('android') > -1) return 'aos';
	else if(osChk.indexOf('iphone') > -1 || osChk.indexOf('ipad') > -1 || osChk.indexOf('ipod') > -1) return 'ios';
	else return 'web';
};


//쿠키값얻는 function
var getCookie = function(name){
	var cookies = document.cookie.split(';');
	for(var i = 0;i<cookies.length;i++){
		if(cookies[i].split('=')[0].trim() == name){
			return cookies[i].split('=')[1].trim();
		}
	}
};

var setCookieLimitDay = function(name, value) {
	var tmpDate = new Date();
	tmpDate.setDate(tmpDate.getDate() +1);
    var date = new Date(getDateStr(tmpDate)+' 00:00');
    date.setTime(date.getTime() );
    var expires = "; expires=" + date.toGMTString();
	document.cookie = name + "=" + value + expires + "; path=/";
};

var setCookie = function(name, value, days) {
	if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        var expires = "; expires=" + date.toGMTString();
	} else {
	       var expires = "";
	}
	document.cookie = name + "=" + value + expires + "; path=/";
};

var deleteCookie = function(name) {
	document.cookie = name + "= ; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
};

var getTodayStr = function(gubun) {
	var date = new Date();
	var gubun = gubun?gubun:'-';
	var month = (date.getMonth() + 1).toString().padStart("2", "0");
	var day = date.getDate().toString().padStart("2", "0")
	return date.getFullYear()+gubun+month+gubun+day;
};
var getTimeStr = function(gubun) {
	var date = new Date();
	var gubun = gubun?gubun:':';
	var sec = date.getSeconds().toString().padStart("2", "0");
	var min = date.getMinutes().toString().padStart("2", "0");
	var hour = date.getHours().toString().padStart("2", "0");
	return hour+gubun+min;
};

var getTodayStrForStream = function() {
	var date = new Date();
	var gubun = gubun?gubun:'-';
	var month = (date.getMonth() + 1).toString().padStart("2", "0");
	var day = date.getDate().toString().padStart("2", "0")
	return month+'월'+day+'일';
};

var getDateStr = function(date,gubun) {
	var gubun = gubun?gubun:'-';
	var month = (date.getMonth() + 1).toString().padStart("2", "0");
	var day = date.getDate().toString().padStart("2", "0")
	return date.getFullYear()+gubun+month+gubun+day;
};

var convertStrDate = function(str,gubun) {	
	var gubun = gubun?gubun:'-';
	if(str && str.length ==8){
		return str.substr(0,4) + gubun + str.substr(4,2) + gubun +str.substr(6,2);
	}else{
		return str;
	}
};

var getLoginUserInfo = function(token) {
	var result = {success : false};

	if (token == "" || token == undefined) {
		return JSON.stringify(result);
	}
	
	$.ajax({
		url: '/user/getTokenInfo',
		data: JSON.stringify({token : token}),
		type: 'POST',
		async: false,
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function(data) {
			result = data;
			result.nearestBranch = getCookie("naBzplc");
		}
	});
	return JSON.stringify(result);
};

function reportPrint(name,title,head,body,width,height){
    const setting = "width="+width?width:900+", height="+height?height:840;
    const objWin = window.open('', 'print', setting);
    
    objWin.document.open(); 
    objWin.document.write('<html><head>');
    objWin.document.write('  <meta charset="utf-8" />	');
	objWin.document.write('</head><body>');
    objWin.document.write(makePrintCss());
    objWin.document.write('<h3>'+title+'</h3>');
    objWin.document.write(makePrintHtml(head,body,name));
    objWin.document.write('</body></html>');
    
    objWin.focus();
    objWin.document.close();
    
 	setTimeout(function(){ objWin.print(); objWin.close(); },500);
}

function reportExcel(title,head,body){
 	var data_type = 'data:application/vnd.ms-excel;charset=utf-8';
    var table_html = encodeURIComponent(makePrintHtml(head,body));
    var a = document.createElement('a');
    a.href = data_type + ',%EF%BB%BF' + table_html;
    a.download = title+'.xls';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}

function makePrintCss(){
	var css ='';	
	css += '<style>';
	css += ' table.auction_result tr .date {  width: 10%;}                          ';
	css += ' table.auction_result tr .num {  width: 5%;}                           ';
	css += ' table.auction_result tr .name {  width: 10%;}                           ';
	css += ' table.auction_result tr .pd_ea {  width: 12.5%;  text-align: center;}  ';
	css += ' table.auction_result tr .pd_sex {  width: 5%;}                         ';
	css += ' table.auction_result tr .pd_kg {  width: 7%;}                          ';
	css += ' table.auction_result tr .pd_kpn{  width: 7%;}                          ';
	css += ' table.auction_result tr .pd_num1{  width: 4%;}                         ';
	css += ' table.auction_result tr .pd_pay1{  width: 9%;}                         ';
	css += ' table.auction_result tr .pd_pay2{  width: 8%;}                         ';
	css += ' table.auction_result tr .pd_state{  width: 9%;}                        ';
	css += ' table.auction_result tr .pd_etc{  width: 13.5%;}                       ';
	
	css += ' table.auction_sales tr .date{ width: 10%;}      ';
	css += ' table.auction_sales tr .num{  width: 7%;}       ';
	css += ' table.auction_sales tr .name{  width: 6%;}      ';
	css += ' table.auction_sales tr .pd_ea{  width: 12.5%;}  ';
	css += ' table.auction_sales tr .pd_sex{  width: 5%;}    ';
	css += ' table.auction_sales tr .pd_date{  width: 10%;}  ';
	css += ' table.auction_sales tr .pd_kpn{  width: 7%;}    ';
	css += ' table.auction_sales tr .pd_num1{  width: 4%;}   ';
	css += ' table.auction_sales tr .pd_num2{  width: 4%;}   ';
	css += ' table.auction_sales tr .pd_info{  width: 15%;}  ';
	css += ' table.auction_sales tr .pd_pay{  width: 8.5%;}  ';
	css += ' table.auction_sales tr .pd_pav{  width: 11%;}   ';
	css += '</style>';
	return css;
}

function makePrintHtml(head,body,name){
    var sHtml='';
    sHtml += ' <table class="'+name+'" border=1>	               ';
	sHtml += ' 	<thead>           ';
	sHtml += ' 	    <tr style="background-color:#efefef;">           ';
	for(var i=0;i<head.length;i++){
		sHtml += ' 	        <th class="'+head[i].class+'">'+head[i].text+'</th>  ';
	}
	sHtml += ' 	    </tr>          ';
	sHtml += ' 	</thead>          ';
	sHtml += '     <tbody>            ';
	var tdSpan = (body.length ==1 && body[0].length==1)?head.length:0;

	for(var i=0;i<body.length;i++){
		sHtml += '         <tr>           ';
		for(var j=0;j<body[i].length;j++){
			sHtml += '             <td class="'+head[j].class+'" style="' + (head[j].class == "pd_ea" ? "mso-number-format:\'@\';" : "") + '" '+(tdSpan !=0?'colspan='+tdSpan:'')+'>'+body[i][j]+'</td>  ';
		}
		sHtml += '         </tr>          ';
	}
	sHtml += '     </tbody>           ';
	sHtml += ' </table>               ';
	return sHtml;
}

function modalComfirm(title,content,succCallBack,failCallBack){
	modalPopupClose('.popup .modal-wrap.confirm');
	$('.popup .modal-wrap.confirm').remove();
	
	var sHtml="";
	sHtml += ' <div id="" class="modal-wrap confirm">';
	sHtml += ' 	<div class="modal-content"> ';
	sHtml += ' 		<h3>'+title+'</h3>';
	sHtml += ' 		<p>'+content+'</p>';
	sHtml += ' 		<div class="btn_area"> ';
	sHtml += ' 			<button class="btn_cancel">취소</button> ';
	sHtml += ' 			<button class="btn_ok">확인</button>';
	sHtml += ' 		</div> ';
	sHtml += ' 	</div> ';
	sHtml += ' </div>';		
		
	$('.popup').append($(sHtml));
	modalPopup('.popup .modal-wrap.confirm');
	
	$('.popup .modal-wrap.confirm .btn_ok').click(function(){
		if(succCallBack) succCallBack();
		modalPopupClose('.popup .modal-wrap.confirm');
	});
	$('.popup .modal-wrap.confirm .btn_cancel').click(function(){
		if(failCallBack) failCallBack();
		modalPopupClose('.popup .modal-wrap.confirm');
	});
}
function modalBzInfo(){
	modalPopupClose('.popup .modal-wrap.pop_Co_info');
	$('.popup .modal-wrap.pop_Co_info').remove();
	
	var sHtml="";
	
	sHtml += ' <div id="" class="modal-wrap pop_Co_info open" style="overflow: auto; display: block;"> ';
	sHtml += ' 	<div class="modal-content" style="margin-top: 310.5px;"> ';
	sHtml += ' 		<p class="name">농협경제지주 주식회사(축산경제)</p> ';
	sHtml += ' 		<dl> ';
	sHtml += ' 			<dt>•  대표자</dt> ';
	sHtml += ' 			<dd>안병우</dd> ';
	sHtml += ' 		</dl> ';
	sHtml += ' 		<dl> ';
	sHtml += ' 			<dt>•  주소지</dt> ';
	sHtml += ' 			<dd>서울특별시 중구 새문안로 16, 8층 (충청로 1가, 농협중앙회 본관)</dd> ';
	sHtml += ' 		</dl> ';
	sHtml += ' 		<dl> ';
	sHtml += ' 			<dt>•  사업자</dt> ';
	sHtml += ' 			<dd>845-85-00512</dd> ';
	sHtml += ' 		</dl> ';
	sHtml += ' 		<div class="btn_area"> ';
	sHtml += ' 			<button class="btn_ok" onclick="modalPopupClose(\'.pop_Co_info\');return false;">확인</button> ';
	sHtml += ' 		</div> ';
	sHtml += ' 	</div> ';
	sHtml += ' </div> ';
		
	$('.popup').append($(sHtml));
	modalPopup('.popup .modal-wrap.pop_Co_info');
	
	$('.popup .modal-wrap.pop_Co_info .btn_ok').click(function(){
		modalPopupClose('.popup .modal-wrap.pop_Co_info');
	});
	
}

function modalAlert(title,content, succCallBack){
	modalPopupClose('.popup .modal-wrap.message');
	$('.popup .modal-wrap.message').remove();
	
	var sHtml="";
	sHtml += ' <div id="" class="modal-wrap message">';
	sHtml += ' 	<div class="modal-content"> ';
	if(title) sHtml += ' 		<h3>'+title+'</h3>';
	sHtml += ' 		<p>'+content+'</p>';
	sHtml += ' 		<div class="btn_area"> ';
	sHtml += ' 			<button class="btn_ok">확인</button>';
	sHtml += ' 		</div> ';
	sHtml += ' 	</div> ';
	sHtml += ' </div>';
		
	$('.popup').append($(sHtml));
	modalPopup('.popup .modal-wrap.message');
	
	$('.popup .modal-wrap.message .btn_ok').click(function(){
		if(succCallBack) succCallBack();
		modalPopupClose('.popup .modal-wrap.message');
	});
}

function modalPlay(succCallBack){
	modalPopupClose('.popup .modal-wrap.play');
	$('.popup .modal-wrap.play').remove();
	
	var sHtml="";
	sHtml += ' <div id="" class="modal-wrap play">';
	sHtml += ' 	<div class="modal-content"> ';
	sHtml += ' 		<div class="btn_area"> ';
	sHtml += ' 			<button class="btn_ok">▶ Play</button>';
	sHtml += ' 		</div> ';
	sHtml += ' 	</div> ';
	sHtml += ' </div>';		
		
	$('.popup').append($(sHtml));
	modalPopup('.popup .modal-wrap.play');
	
	$('.popup .modal-wrap.play .btn_ok').click(function(){
		if(succCallBack) succCallBack();
		modalPopupClose('.popup .modal-wrap.play');
	});
}

var nameEnc = function(name,encStr){	
	if(name.length > 1){
		return name.substr(0,1)+(encStr?encStr:'*')+name.substr(2,name.length-2); 
	}else{
		return name;
	}
};

var addCalendarEvent = function(target) {
	if (!target) target = $(".calendar");
	
	$.datepicker.setDefaults({
        dateFormat: 'yymmdd',
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년'
    });
	$(target).prop("readonly", true);
};

var onLocationPermissionsResult = function(permissionYn) {
	if (permissionYn == 'Y') {
//		getNearestBranch();
	}
}

var getNearestBranch = function() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			getLocation(position.coords.latitude, position.coords.longitude);
		}, function(error) {
			console.error(error);
		}, {
			enableHighAccuracy: false,
			maximumAge: 0,
			timeout: Infinity
		});
	}
	else {
	}
};

var getLocation = function(lat, lng) {
	$.ajax({
		url: '/api/v1/biz/nearest',
		data: JSON.stringify({"lat" : lat, "lng" : lng}),
		type: 'POST',
		async: false,
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function(data) {
			if (data.success) {
				setCookieLimitDay("naBzplc", data.branchInfo.NA_BZPLC);
				setCookieLimitDay("naBzplcno", data.branchInfo.NA_BZPLCNO);
			}
//			pageMove('/main?place=' + data.branchInfo.NA_BZPLCNO + '&aucYn=Y', false);
		}
	});
};

// form append submit
var appendFormSubmit = function(name, action, params, target) {
	var form = $("form[name='" + name + "']").length == 0 ? $("<form></form>") : $("form[name='" + name + "']");
	form.attr("name", name);
	form.attr("action", action);
	form.attr("method", "post");
	if (target != undefined) form.attr("target", target);
	
	if (params != undefined) {
		var keys = Object.keys(params);
		$(keys).each(function(idx, key){
			form.append($("<input/>", {type: "hidden", name: key, value: params[key]}))
		});
	}
	form.appendTo("body");
	form.submit();
};

var debugConsole = function() {
	if (active && (active == "local" || active == "develop" )){
		var arrConsole = [];
		for (var i in arguments) {
			arrConsole.push(arguments[i]);
		}
		console.log(arrConsole);
	}
};

// toast message
let removeToast;
var toast = function(string, sec, top) {
	if (!document.getElementById("toast")) {
		var div = document.createElement('div');
		div.id = "toast";
		document.body.appendChild(div);
	} 
	const toast = document.getElementById("toast");
	let tm = (sec == undefined ? 1 : sec) * 1000;
	if (top != undefined) toast.style["top"] = top + "px";

	toast.classList.contains("reveal") ?
		(clearTimeout(removeToast), removeToast = setTimeout(function () {
			document.getElementById("toast").classList.remove("reveal");
			toast.innerHTML = "<span>&nbsp;</span>";
		}, tm)) :
		removeToast = setTimeout(function () {
			document.getElementById("toast").classList.remove("reveal")
			toast.innerHTML = "<span>&nbsp;</span>";
		}, tm)
	toast.innerHTML = string;
	toast.classList.add("reveal");
}

var fnSetComma = function(str) {
	if (!str || str.toString().replace(/[^0-9]/gi, '') == "") {
		return (str ? str : "0");
	}
	
	const delim = str.toString().substring(0,1) == '-' ? '-' : '';
	
	return delim + str.toString().replace(/[^0-9]/gi, '').replace(/\B(?=(\d{3})+(?!\d))/g, ",") ;
}

var fnMcaInterface = function(strData, callback) {
	$.ajax({
		url : '/office/interface/mca',
		data : strData,
		type : 'POST',
		dataType : 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function() {
		},
		error: function(xhr, status, error) {
		}
	}).done(function (body) {
		if (body && body.code == "C000") {
			console.log(body.data);
			callback(body);
		}
		else {
			modalAlert("", body.message);
		}
	});
}

var setAuthNoCountDown = function(){
	var countdown = $("#countdown");
	var auth_no = $("input[name='auth_no']").val();
	var auth_ymd = $("input[name='auth_ymd']").val();
	
	if(auth_no != null && auth_no != ""){
		var countDownDate = new Date(auth_ymd.substr(0,4), auth_ymd.substr(4,2) - 1, auth_ymd.substr(6,2), auth_ymd.substr(8,2), auth_ymd.substr(10, 2), auth_ymd.substr(12, 2)).getTime();
		var x = setInterval(function(){
			var now = new Date().getTime();
			var distance = countDownDate - now;
			var days = Math.floor(distance / (1000 * 60 * 60 * 24));
			var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
			var seconds = Math.floor((distance % (1000 * 60)) / 1000);
			var arrHtml = [];
			
			if(distance < 0){
				clearInterval(x);
				$(".btn-re-auth").show();
				$(".authNoArea").hide();
			}else{
				if(days > 0){
					arrHtml.push(days + "일 ");
				}
				if(hours > 0){
					if(hours < 10) hours = "0" + hours;
					arrHtml.push(hours + ":");
				}	
				
				if(minutes >= 0 && minutes < 10)	minutes = "0"  + minutes;
				if(seconds >= 0 && seconds < 10)  seconds = "0"  + seconds;
				arrHtml.push(minutes + ":" + seconds);
				countdown.html(arrHtml.join(""));
				
				$(".btn-re-auth").hide();
				$(".authNoArea").show();
			}
			
		}, 1000);
	}
	else{
		$(".btn-re-auth").show();
	}
}

var fnIssueKioskAuthNum = function() {
//	modalComfirm(""
//		, "발급하시겠습니까?"
//		, function(){
		$.ajax({
			url: '/my/myAuthNumIssue',
			data: JSON.stringify($("form[name='frm_info']").serializeObject()),
			type: 'POST',
			dataType: 'json',
			beforeSend: function (xhr) {
				xhr.setRequestHeader("Accept", "application/json");
				xhr.setRequestHeader("Content-Type", "application/json");
			}
		}).done(function (body) {
			var success = body.success;
			var message = body.message;
			if (!success) {
				modalAlert('', message);
			}
			else {	//인증번호 발급된 경우
//				modalAlert('', message, 
//				function(){
				$("input[name='auth_no']").val(body.auth_no);
				$("input[name='auth_ymd']").val(body.auth_ymd);
				$(".authNoArea #authNo").text(body.auth_no);
				setAuthNoCountDown();
//				});
			}
		});
//		});
	return;
};

var connChannel = function(){
	try{
		if(isApp()){
			if(isIos())	
				return "IOS";
			if(isAnd())	
				return "AOS";
		}
		else{
			if($("#deviceMode").val() == "MOBILE")	
				return "MWEB";
			else 
				return "PC";	
		}
		
	}catch(e){
		debugConsole(e);
	}
};

var findGetParameter = function(paramName) {
    var result = null,
        tmp = [];
    var items = location.search.substr(1).split("&");
    for (var index = 0; index < items.length; index++) {
        tmp = items[index].split("=");
        if (tmp[0] === paramName) result = decodeURIComponent(tmp[1]);
    }
    return result;
}
