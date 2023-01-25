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
	}
	COMMONS.callAjax("/dashboard/getStaticInfo", "post", params, 'application/json', 'json', function(data){
		console.log(data);
		if(data && data.success){
			$('.cnt_list .qcnCnt em span').text((fnSetComma(data.staticInfo.QCN_CNT)||'-'));
			$('.cnt_list .cowCnt em span').text((fnSetComma(data.staticInfo.COW_CNT)||'-'));
			$('.cnt_list .bidCnt em span').text((fnSetComma(data.staticInfo.BID_CNT)||'-'));
			$('.cnt_list .noBidCnt em span').text((fnSetComma(data.staticInfo.NO_BID_CNT)||'-'));
			
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
	}
	COMMONS.callAjax("/dashboard/getAucStaticExcelList", "post", params, 'application/json', 'json', function(data){
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
		head.push({text:'경매번호', class:''});                             
		head.push({text:'경매대상', class:''});                             
		head.push({text:'출하자코드', class:''});                           
		head.push({text:'출하자', class:''});                               
		head.push({text:'출하자 생년월일', class:''});                      
		head.push({text:'조합원 여부', class:''});                          
		head.push({text:'관내외 구분', class:''});                          
		head.push({text:'생산자', class:''});                               
		head.push({text:'접수일자', class:''});                             
		head.push({text:'진행상태', class:''});                             
		head.push({text:'낙찰자명', class:''});                             
		head.push({text:'참가번호', class:''});                             
		head.push({text:'귀표번호', class:''});                             
		head.push({text:'성별', class:''});                                 
		head.push({text:'자가운송여부', class:''});                         
		head.push({text:'생년월일', class:''});                             
		head.push({text:'월령', class:''});                                 
		head.push({text:'계대', class:''});                                 
		head.push({text:'등록번호', class:''});                             
		head.push({text:'등록구분', class:''});                             
		head.push({text:'제각여부', class:''});                             
		head.push({text:'KPN번호', class:''});                              
		head.push({text:'어미귀표번호', class:''});                         
		head.push({text:'어미구분', class:''});                             
		head.push({text:'산차', class:''});                                 
		head.push({text:'중량', class:''});                                 
		head.push({text:'수송자', class:''});                               
		head.push({text:'수의사', class:''});                               
		head.push({text:'예정가', class:''});                               
		head.push({text:'낙찰가', class:''});                               
		head.push({text:'브루셀라 검사일자', class:''});                    
		head.push({text:'브루셀라 검사 증명서 제출', class:''});            
		head.push({text:'예방접종일자', class:''});                         
		head.push({text:'괴사감정여부', class:''});                         
		head.push({text:'괴사여부', class:''});                             
		head.push({text:'임신감정 여부', class:''});                        
		head.push({text:'임신여부', class:''});                             
		head.push({text:'임신구분', class:''});                             
		head.push({text:'인공 수정일자', class:''});                        
		head.push({text:'수정KPN', class:''});                              
		head.push({text:'임신개월', class:''});                             
		head.push({text:'인공수정 증명서제출여부', class:''});              
		head.push({text:'우결핵 검사일', class:''});                        
		head.push({text:'전송', class:''});                                 
		head.push({text:'주소', class:''});                                 
		head.push({text:'휴대폰 번호', class:''});                          
		head.push({text:'비고', class:''});                                 
		head.push({text:'친자검사결과', class:''});                         
		head.push({text:'친자검사여부', class:''});                         
		head.push({text:'사료미사용여부', class:''});                       
		head.push({text:'추가운송비', class:''});                           
		head.push({text:'사료대금', class:''});                             
		head.push({text:'당일접수비', class:''});                           
		head.push({text:'브랜드명', class:''});                             
		head.push({text:'수의사구분', class:''});                           
		head.push({text:'고능력여부', class:''});                           
		head.push({text:'난소적출여부', class:''});                         
		head.push({text:'등록일사', class:''});                             
		head.push({text:'등록자', class:''});                               
		head.push({text:'계좌번호', class:''});                             
		head.push({text:'출자금', class:''});                               
		head.push({text:'딸린 송아지 귀표번호', class:''});                 
		head.push({text:'구분', class:''});                                 
		                                                                    
		var body = new Array();
		data.list.forEach((e,i)=>{
			var bodyTr = new Array();
			bodyTr.push(e['CLNTNM']||'');
			bodyTr.push(e['AUC_DT']||'');
			bodyTr.push(e['AUC_PRG_SQ']||'');
			bodyTr.push(e['AUC_OBJ_DSC_NM']||'');
			bodyTr.push(e['FHS_ID_NO']||'');
			bodyTr.push(e['FTSNM']||'');
			bodyTr.push(e['FHS_BIRTH']||'');
			bodyTr.push(e['MACO_YN']||'');
			bodyTr.push(e['JRDWO_DSC']||'');
			bodyTr.push(e['SRA_PDMNM']||'');
			bodyTr.push(e['RC_DT']||'');
			bodyTr.push(e['SEL_STS_DSC_NM']||'');
			bodyTr.push(e['SRA_MWMNNM']||'');
			bodyTr.push(e['LVST_AUC_PTC_MN_NO']||'');
			bodyTr.push(e['SRA_INDV_AMNNO']||'');
			bodyTr.push(e['INDV_SEX_C_NM']||'');
			bodyTr.push(e['TRPCS_PY_YN']||'');
			bodyTr.push(e['BIRTH']||'');
			bodyTr.push(e['MTCN']||'');
			bodyTr.push(e['SRA_INDV_PASG_QCN']||'');
			bodyTr.push(e['SRA_INDV_BRDSRA_RG_NO']||'');
			bodyTr.push(e['RG_DSC_NM']||'');
			bodyTr.push(e['RMHN_YN']||'');
			bodyTr.push(e['KPN_NO']||'');
			bodyTr.push(e['MCOW_SRA_INDV_AMNNO']||'');
			bodyTr.push(e['MCOW_DSC_NAME']||'');
			bodyTr.push(e['MATIME']||'');
			bodyTr.push(e['COW_SOG_WT']||'');
			bodyTr.push(e['VHC_DRV_CAFFNM']||'');
			bodyTr.push(e['BRKR_NAME']||'');
			bodyTr.push(e['LOWS_SBID_LMT_AM']||'');
			bodyTr.push(e['SRA_SBID_UPR']||'');
			//bodyTr.push(e['SRA_SBID_AM']||'');
			bodyTr.push(e['BRCL_ISP_DT']||'');
			bodyTr.push(e['BRCL_ISP_CTFW_SMT_YN']||'');
			bodyTr.push(e['VACN_DT']||'');
			bodyTr.push(e['NCSS_JUG_YN']||'');
			bodyTr.push(e['NCSS_YN']||'');
			bodyTr.push(e['PRNY_JUG_YN']||'');
			bodyTr.push(e['PRNY_YN']||'');
			bodyTr.push(e['PPGCOW_FEE_DSC_NM']||'');
			bodyTr.push(e['AFISM_MOD_DT']||'');
			bodyTr.push(e['MOD_KPN_NO']||'');
			bodyTr.push(e['PRNY_MTCN']||'');
			bodyTr.push(e['AFISM_MOD_CTFW_SMT_YN']||'');
			bodyTr.push(e['BOVINE_DT']||'');
			bodyTr.push(e['TMS_YN']||'');
			bodyTr.push(e['ADDRESS']||'');
			bodyTr.push(e['CUS_MPNO']||'');
			bodyTr.push(e['RMK_CNTN']||'');
			bodyTr.push(e['DNA_YN_NM']||'');
			bodyTr.push(e['DNA_YN_CHK']||'');
			bodyTr.push(e['SRA_FED_SPY_YN']||'');
			bodyTr.push(e['SRA_TRPCS']||'');
			bodyTr.push(e['SRA_FED_SPY_AM']||'');
			bodyTr.push(e['TD_RC_CST']||'');
			bodyTr.push(e['BRANDNM']||'');
			bodyTr.push(e['PDA_ID']||'');
			bodyTr.push(e['EPD_YN']||'');
			bodyTr.push(e['SPAY_YN']||'');
			bodyTr.push(e['FSRG_DTM']||'');
			bodyTr.push(e['USRNM']||'');
			bodyTr.push(e['SRA_FARM_ACNO']||'');
			bodyTr.push(e['SRA_PYIVA']||'');
			bodyTr.push(e['INDV_AMNNO']||'');
			bodyTr.push(e['CASE_COW']||'');
			body.push(bodyTr);
		});
		
		console.log(body);
		reportExcel('출장우 내역',head,body);
	});
}