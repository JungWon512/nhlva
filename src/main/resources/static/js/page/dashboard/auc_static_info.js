var COMMONS = window.auction["commons"];

(function ($, win, doc) {
    var setLayout = function() {
    	addCalendarEvent();
    	var today= new Date();
    	var preDate= new Date();
    	preDate.setMonth(preDate.getMonth()-3);
    	$('input#stSearchDt').val(getDateStr(preDate)).datepicker({dateFormat: 'yy-mm-dd'});
    	$('input#edSearchDt').val(getDateStr(today)).datepicker({dateFormat: 'yy-mm-dd'});
    };
    
    var setBinding = function() {
    	$(document).on('click','button[name=btnInit]',function(){
    		$('div.aucObjDscBtn button').removeClass("on");
    		$('div.aucObjDscBtn button[name=btnAucObjDscAll]').addClass("on");	
	    	var today= new Date();    	
	    	var preDate= new Date();
	    	preDate.setMonth(preDate.getMonth()-3);    	
	    	$('input#stSearchDt').val(getDateStr(preDate));
	    	$('input#edSearchDt').val(getDateStr(today));
    	});
    	$(document).on('click','button[name=btnSearch]',function(){
    		getAucStaticInfo();
    	});
    	
    	$(document).on('click','button[name=btnExcelDown]',function(){
    		getAucStaticList();
    	});
    	$(document).on('click','div.aucObjDscBtn button',function(){
    		$('div.aucObjDscBtn button').removeClass("on");
    		$(this).addClass("on");
    	});
    	$(document).on('change','select[name=loc]',function(){
    		
			COMMONS.callAjax("/dashboard/getBzLocList", "post", {naBzPlcLoc:$(this).val()}, 'application/json', 'json', function(data){
				console.log(data);
				$('select#bzLoc').empty();
				$('select#bzLoc').append('<option value="">전체</option>');
				if(data && data.locList){
					data.locList.forEach((e,i)=>{
						$('select#bzLoc').append('<option value="'+e.NA_BZPLC+'">'+e.AREANM+'</option>');
					});
					$("select").selectric("refresh");
				}
			});
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

var getAucStaticInfo = function(){
	var stDate = $('input#stSearchDt').val().replaceAll('-','');
	var edDate = $('input#edSearchDt').val().replaceAll('-','');
	var chkStDate = new Date(convertStrDate(stDate));
	var chkEdDate = new Date(convertStrDate(edDate));
	chkEdDate.setMonth(chkEdDate.getMonth()-3);
	
	if(chkEdDate > chkStDate){
		modalAlert('','검색기간을 3개월이상<br/>조회하실수없습니다.');
		return;
	}

	var params = {
		stSearchDt : stDate
		, edSearchDt : edDate
		, naBzplc : $('select[name=bzLoc]').val()
		, naBzplcloc : $('select[name=loc]').val()
		, aucObjDsc : $('div.aucObjDscBtn button.on').val()
		, selStsDsc : '22'
	}
	COMMONS.callAjax("/dashboard/getAucStaticInfo", "post", params, 'application/json', 'json', function(data){
		console.log(data);
		if(data && data.success){
			$('.cnt_list .cowCnt em span').text((fnSetComma(data.staticInfo.COW_CNT)||'-'));
			$('.cnt_list .bidAm em span').text((fnSetComma(data.staticInfo.SUM_SRA_SBID_AM)||'-'));
			$('.cnt_list .fhsFee em span').text((fnSetComma(data.staticInfo.SUM_FHS_FEE)||'-'));
			$('.cnt_list .mwmnFee em span').text((fnSetComma(data.staticInfo.SUM_MWMN_FEE)||'-'));
			
		}
	});
}
var getAucStaticList = function(){
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
		, selStsDsc : '22'
	}
	COMMONS.callAjax("/dashboard/getAucStaticExcelList", "post", params, 'application/json', 'json', function(data){
		console.log(data);
		var head = new Array();		
		if(!data || !data.success){
			modalAlert("",data.message);
			return;
		}else if(data.list.length == 0){
			modalAlert("","조회된 정보가 없습니다.");		
			return;
		}
		
		head.push({text:'조합명', class:''});
		head.push({text:'경매일자', class:''});
		head.push({text:'경매대상', class:''});
		head.push({text:'경매순번', class:''});
		head.push({text:'출하자', class:''});
		head.push({text:'우편번호', class:''});
		head.push({text:'주소', class:''});
		head.push({text:'귀표번호', class:''});
		head.push({text:'생일', class:''});
		head.push({text:'산차', class:''});
		head.push({text:'계대', class:''});
		head.push({text:'KPN번호', class:''});
		head.push({text:'등록구분', class:''});
		head.push({text:'성별', class:''});
		head.push({text:'어미귀표', class:''});
		head.push({text:'어미구분', class:''});
		head.push({text:'중량(kg)', class:''});
		head.push({text:'예정가', class:''});
		head.push({text:'낙찰가', class:''});
		head.push({text:'낙찰단가', class:''});
		head.push({text:'제각여부', class:''});
		head.push({text:'수송자', class:''});
		head.push({text:'참가번호', class:''});
		head.push({text:'낙찰자', class:''});
		head.push({text:'낙찰자연락처', class:''});
		head.push({text:'출하자수수료', class:''});
		head.push({text:'낙찰자수수료', class:''});
		
		var body = new Array();
		data.list.forEach((e,i)=>{
			var bodyTr = new Array();
			bodyTr.push(e['CLNTNM']||'');
			bodyTr.push(e['AUC_DT']||'');
			bodyTr.push(e['AUC_OBJ_DSC_NM']||'');
			bodyTr.push(e['AUC_PRG_SQ']||'');
			bodyTr.push(e['FTSNM']||'');
			bodyTr.push(e['ZIP']||'');
			bodyTr.push(e['ADDRESS']||'');
			bodyTr.push(e['SRA_INDV_AMNNO']||'');
			bodyTr.push(e['BIRTH']||'');
			bodyTr.push(e['MATIME']||'');
			bodyTr.push(e['SRA_INDV_PASG_QCN']||'');
			bodyTr.push(e['KPN_NO']||'');
			bodyTr.push(e['RG_DSC_NM']||'');
			bodyTr.push(e['INDV_SEX_C_NM']||'');
			bodyTr.push(e['MCOW_SRA_INDV_AMNNO']||'');
			bodyTr.push(e['MCOW_DSC_NM']||'');
			bodyTr.push(e['COW_SOG_WT']||'');
			bodyTr.push(e['LOWS_SBID_LMT_AM']||'');
			bodyTr.push(e['SRA_SBID_AM']||'');
			bodyTr.push(e['SRA_SBID_UPR']||'');
			bodyTr.push(e['RMHN_YN']||'');
			bodyTr.push(e['VHC_DRV_CAFFNM']||'');
			bodyTr.push(e['LVST_AUC_PTC_MN_NO']||'');
			bodyTr.push(e['SRA_MWMNNM']||'');
			bodyTr.push(e['CUS_MPNO']||'');
			bodyTr.push(e['FHS_SRA_TR_FEE']||'');
			bodyTr.push(e['MWMN_SRA_TR_FEE']||'');
			body.push(bodyTr);
		});
		
		console.log(body);
		reportExcel('경매 낙찰현황',head,body);
	});
}