var COMMONS = window.auction["commons"];

(function ($, win, doc) {
    var setLayout = function() {
    	addCalendarEvent();
		fnSetInit();
    };
    
    var setBinding = function() {
    	$(document).on('click','button[name=btnInit]',function(){
    		$('div.aucObjDscBtn button').removeClass("on");
    		$('div.aucObjDscBtn button[name=btnAucObjDscAll]').addClass("on");
    		fnSetInit();
    		$('button[name=btnSearch]').click();
    	});
    	
    	$(document).on('click','button[name=btnSearch]',function(){
    		getStaticInfo();
    	});
    	
    	$(document).on('click','button[name=btnExcelDown]',function(){
    		getStaticList();
    	});
    	$(document).on('click','div.aucObjDscBtn button',function(){
    		$('div.aucObjDscBtn button').removeClass("on");
    		$(this).addClass("on");
    	});
    	$(document).on('change','select[name=loc]',function(){
    		fnLocChage($(this).val());
    	});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD_STATIC_INFO"; 
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setBinding();
            setLayout();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);

var getStaticInfo = function(){
	var stDate = $('input#stSearchDt').val();
	var edDate = $('input#edSearchDt').val();
	var chkStDate = new Date(convertStrDate(stDate));
	var chkEdDate = new Date(convertStrDate(edDate));
	chkEdDate.setMonth(chkEdDate.getMonth()-3);
	
	if(chkEdDate > chkStDate){
		modalAlert('','조회기간을 3개월이상<br/>조회하실수 없습니다.');
		return;
	}

	var params = {
		stSearchDt : stDate.replaceAll('-','')
		, edSearchDt : edDate.replaceAll('-','')
		, naBzplc : $('select[name=bzLoc]').val()
		, naBzplcloc : $('select[name=loc]').val()
		, aucObjDsc : $('div.aucObjDscBtn button.on').val()
	}
	COMMONS.callAjax("/dashboard/getStaticInfo", "post", params, 'application/json', 'json', function(data){
		console.log(data);
		if(data && data.success){
			$('.cnt_list .qcnCnt em span').text((fnSetComma(data.staticInfo.QCN_CNT)||'-'));
			$('.cnt_list .cowCnt em span').text((fnSetComma(data.staticInfo.COW_CNT)||'-'));
			$('.cnt_list .bidCnt em span').text((fnSetComma(data.staticInfo.BID_CNT)||'-'));
			$('.cnt_list .aucEntrCnt em span').text((fnSetComma(data.staticInfo.ENTR_CNT)||'-'));
			
		}
	});
}
var getStaticList = function(){
	var stDate = $('input#stSearchDt').val();
	var edDate = $('input#edSearchDt').val();
	var chkStDate = new Date(convertStrDate(stDate));
	var chkEdDate = new Date(convertStrDate(edDate));
	chkEdDate.setMonth(chkEdDate.getMonth()-3);
	
	if(chkEdDate > chkStDate){
		modalAlert('','검색기간을 3개월이상<br/>조회하실수없습니다.');
		return;
	}
	
	var params = {
		stSearchDt : $('input#stSearchDt').val().replaceAll('-','')
		, edSearchDt : $('input#edSearchDt').val().replaceAll('-','')
		, naBzplc : $('select[name=bzLoc]').val()
		, naBzplcloc : $('select[name=loc]').val()
		, aucObjDsc : $('div.aucObjDscBtn button.on').val()
	}
	COMMONS.callAjax("/dashboard/getStaticList", "post", params, 'application/json', 'json', function(data){
		console.log(data);				
		if(!data || !data.success){
			modalAlert("",data.message);
			return;
		}else if(data.list.length == 0){
			modalAlert("","조회된 정보가 없습니다.");		
			return;
		}
		var head = new Array();
		head.push({text:'조합명', class:''});
		head.push({text:'경매일자', class:''});
		head.push({text:'출장우 두수', class:''});
		head.push({text:'낙찰두수', class:''});
		head.push({text:'낙찰율', class:''});
		head.push({text:'응찰참여 매수인', class:''});
		head.push({text:'낙찰 매수인', class:''});
		head.push({text:'참여율', class:''});
		
		var body = new Array();
		data.list.forEach((e,i)=>{
			var bodyTr = new Array();
			
			bodyTr.push(e['CLNTNM']||'');
			bodyTr.push(e['AUC_DT']||'');
			bodyTr.push(e['COW_CNT']||'');
			bodyTr.push(e['BID_COW_CNT']||'');
			bodyTr.push(e['COW_PER']||'');
			bodyTr.push(e['ATDR_CNT']||'');
			bodyTr.push(e['BID_ATDR_CNT']||'');
			bodyTr.push(e['ATDR_PER']||'');
			body.push(bodyTr);
		});
		
		console.log(body);
		reportExcel('사용자 접속현황',head,body);
	});
}
var fnLocChage = function(naBzPlcLoc,initYn){
	COMMONS.callAjax("/dashboard/getBzLocList", "post", {naBzPlcLoc: naBzPlcLoc}, 'application/json', 'json', function(data){
		$('select#bzLoc').empty();
		if(data && data.locList){
			data.locList.forEach((e,i)=>{
				$('select#bzLoc').append('<option value="'+e.NA_BZPLC+'" '+((initYn && e.NA_BZPLC == $('#naBzplc').val())?'selected':'')+'>'+e.AREANM+'</option>');
			});
			$("select").selectric("refresh");
		}
		if(initYn) getStaticInfo();
	});
}

var fnSetInit = function(){
	var today= new Date();
	var preDate= new Date();
	preDate.setMonth(preDate.getMonth()-1);
	$('input#stSearchDt').val(getDateStr(preDate)).datepicker({dateFormat: 'yy-mm-dd'});
	$('input#edSearchDt').val(getDateStr(today)).datepicker({dateFormat: 'yy-mm-dd'});
	
	$('select[name=loc]').val($('#naBzplcloc').val());
	fnLocChage($('#naBzplcloc').val(),true);
	if($('input#loginGrpC').val() != '001') $('select').attr('disabled',true);
	$("select").selectric("refresh");
}