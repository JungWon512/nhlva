var sFlag=false;
// 페이지 이동 함수
var pageMove = function(uri,chkSearch) {
	
	var temp = !chkSearch ? window.location.search.split("&") : [];
	var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
	
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
				webkit.messageHandlers.moveWebPage.postMessage(url);
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
		//var place = location.search.replace(/[^0-9]/g, "");
		//if(place && place != 119){
		//	modalAlert('','서비스 준비중입니다.');
		//	return;
		//}
		// 안드로이드
		if (window.auctionBridge) {
			window.auctionBridge.moveAuctionBid();
		}
		// 아이폰
		else if(isIos()) {
			webkit.messageHandlers.moveAuctionBid.postMessage(true);
		}
		else {
			location.href = window.location.origin + '/bid' + window.location.search
		}
	}
	catch(e) {
		location.href = window.location.origin + '/bid' + window.location.search
	}
};

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
	modalComfirm(""
				, "로그아웃 하시겠습니까?"
				, function(){
					pageMove('/user/logoutProc');
				});
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
			sHtml += '             <td class="'+head[j].class+'" '+(tdSpan !=0?'colspan='+tdSpan:'')+'>'+body[i][j]+'</td>  ';
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
	
	$(target).datepicker({
		dateFormat: 'yy.mm.dd' //Input Display Format 변경
		,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
		,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
//		,changeYear: true //콤보박스에서 년 선택 가능
//		,changeMonth: true //콤보박스에서 월 선택 가능                
//		,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
//		,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
//		,buttonImageOnly: false //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
//		,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
		,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
		,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
		,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
		,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
		,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
		,minDate: "-1Y" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
		,maxDate: "0D" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후) 
	});
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