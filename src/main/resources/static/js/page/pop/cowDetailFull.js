;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    	if($('div.tab_list ul li a.cowTab_2').length > 0){
    		$('div.tab_area.cowTab_2').show();
    		showGradient(); // 데이터 불러올때 마지막에 재호출 해주세요
    		//epdChartDraw();
    	}
		//getAiakInfo();
    	/* s: 종축개량 로직추가로 인한 주석처리  
    	var indvData = getInfMca({cgtrmCd : '4700', SRA_INDV_AMNNO : $('input[name=sraIndvAmnno]').val()});
    	if(indvData && indvData.success){
			var json = JSON.parse(indvData.data);
			var infoData = {}; 
			Object.keys(json.data).map((e,i)=>{
				infoData[e] = json.data[e]??''.trim();
			});	
			console.log(infoData);
			if(infoData.INQ_CN*1 == 0){
				modalAlert('','해당 귀표번호로 조회된 개체가 없습니다.');
				return;
			}else{
				$('td.fCowSraIndvData').html('[KPN] '+infoData.SRA_KPN_NO+'<br/>'+infoData.FCOW_SRA_INDV_EART_NO);
				$('td.grfCowSraIndvData').html('[KPN] '+infoData.GRFA_SRA_KPN_NO+'<br/>'+infoData.GRFA_SRA_INDV_EART_NO);
				$('td.grmCowSraIndvData').html(infoData.GRMO_SRA_INDV_EART_NO);
				$('td.mCowSraIndvData').html(infoData.MCOW_SRA_INDV_EART_NO);
				$('td.mgrfCowSraIndvData').html('[KPN] '+infoData.MTGRFA_SRA_KPN_NO+'<br/'+infoData.MTGRFA_SRA_INDV_EART_NO);
				$('td.mgrmCowSraIndvData').html(infoData.MTGRMO_SRA_INDV_EART_NO);
			}
    	}else{
			modalAlert('',indvData.message);
			return;
    	}
    	
    	if($('td.mCowSraIndvData').html().trim().length >= 15){
	    	var sibIndvData = getInfMca({cgtrmCd : '4900', SRA_INDV_AMNNO : $('td.mCowSraIndvData').html().trim()});    	
	    	if(sibIndvData && sibIndvData.success){
				var json = JSON.parse(sibIndvData.data);
				var infoData = {}; 
				Object.keys(json.data).map((e,i)=>{
					infoData[e] = json.data[e]??''.trim();
				});
				console.log(infoData);
				if(infoData.INQ_CN == 0){
					modalAlert('','해당 개체의 귀표번호로 조회된 형매정보가 없습니다.');
					return;
				}else{
					$('table.sibIndvTable tbody').empty();
					infoData.RPT_DATA.forEach((item,i)=>{
						var indvSexCNm = '',rgDsc='';
						switch(item.INDV_SEX_C.trim()){
							case '1': indvSexCNm ='암'; break;
							case '2': indvSexCNm ='수'; break;
							case '3': indvSexCNm ='거세'; break;
							case '4': indvSexCNm ='미경산'; break;
							case '5': indvSexCNm ='비거세'; break;
							case '6': indvSexCNm ='프리마틴'; break;
							case '9': indvSexCNm ='공통'; break;
							case '0' : indvSexCNm ='없음'; break;
						}
						switch(item.RG_DSC.trim()){
							case '01': rgDsc ='기초'; break;
							case '02': rgDsc ='혈통'; break;
							case '03': rgDsc ='고등'; break;
							case '09': rgDsc ='미등록우'; break;
						}
						
						var sHtml = '';						
						sHtml += '  <tr>	';
						sHtml += '  	<td rowspan="2" class="ta-C bg-gray">'+((item.SRA_INDV_LS_MATIME??'').trim()||'-') +'</td>	';
						sHtml += '  	<td rowspan="2" class="ta-C">'+((item.SRA_INDV_AMNNO??'').trim()||'-') +'</td>	';
						sHtml += '  	<td class="ta-C">'+((item.RG_DSC_NAME??'').trim()||'-') +'</td>	';
						sHtml += '  	<td class="ta-C">'+ (indvSexCNm||'-') +'</td>	';
						sHtml += '  	<td class="ta-C">'+((item.MIF_SRA_INDV_BIRTH??'').trim()||'-') +'</td>	';
						sHtml += '  </tr>	';
						sHtml += '  <tr>	';
						sHtml += '  	<td class="ta-C">'+((item.METRB_METQLT_GRD??'').trim()||'-') +'</td>	';
						sHtml += '  	<td class="ta-C">'+((item.METRB_BBDY_WT??'').trim()||'-') +'</td>	';
						sHtml += '  	<td class="ta-C">'+((item.MIF_BTC_DT??'').trim()||'-') +'</td>	';
						sHtml += '  </tr>	';						
						$('table.sibIndvTable tbody').append(sHtml);
					});				
				}
	    	}else{
				modalAlert('',sibIndvData.message);
				return;
	    	}
    	}
    	
    	var postIndvData = getInfMca({cgtrmCd : '4900', SRA_INDV_AMNNO : $('input[name=sraIndvAmnno]').val()});    	
    	if(postIndvData && postIndvData.success){
			var json = JSON.parse(postIndvData.data);
			var infoData = {}; 
			Object.keys(json.data).map((e,i)=>{
				infoData[e] = json.data[e]??''.trim();
			});
			console.log(infoData);
			if(infoData.INQ_CN == 0){
				modalAlert('','해당 개체의 귀표번호로 조회된 후대정보가 없습니다.');
				return;
			}else{
				$('table.postIndvTable tbody').empty();
				infoData.RPT_DATA.forEach((item,i)=>{
					var indvSexCNm = '';
					switch(item.INDV_SEX_C.trim()){
						case '1': indvSexCNm ='암'; break;
						case '2': indvSexCNm ='수'; break;
						case '3': indvSexCNm ='거세'; break;
						case '4': indvSexCNm ='미경산'; break;
						case '5': indvSexCNm ='비거세'; break;
						case '6': indvSexCNm ='프리마틴'; break;
						case '9': indvSexCNm ='공통'; break;
						case '0' : indvSexCNm ='없음'; break;
					}
					var sHtml = '';						
					sHtml += '  <tr>	';
					sHtml += '  	<td rowspan="2" class="ta-C bg-gray">'+((item.SRA_INDV_LS_MATIME??'').trim()||'-') +'</td>	';
					sHtml += '  	<td rowspan="2" class="ta-C">'+((item.SRA_INDV_AMNNO??'').trim()||'-') +'</td>	';
					sHtml += '  	<td class="ta-C">'+((item.RG_DSC_NAME??'').trim()||'-') +'</td>	';
					sHtml += '  	<td class="ta-C">'+ (indvSexCNm||'-') +'</td>	';
					sHtml += '  	<td class="ta-C">'+((item.MIF_SRA_INDV_BIRTH??'').trim()||'-') +'</td>	';
					sHtml += '  </tr>	';
					sHtml += '  <tr>	';
					sHtml += '  	<td class="ta-C">'+((item.METRB_METQLT_GRD??'').trim()||'-') +'</td>	';
					sHtml += '  	<td class="ta-C">'+((item.METRB_BBDY_WT??'').trim()||'-') +'</td>	';
					sHtml += '  	<td class="ta-C">'+((item.MIF_BTC_DT??'').trim()||'-') +'</td>	';
					sHtml += '  </tr>	';							
					$('table.postIndvTable tbody').append(sHtml);
				});			
			}
    	}else{
			modalAlert('',postIndvData.message);
			return;
    	}
    	
    	var epdData = getInfMca({cgtrmCd : '4200', RC_NA_TRPL_C : '8808990768700', INDV_ID_NO : $('input[name=sraIndvAmnno]').val()});
    	if(epdData && epdData.success){
			var json = JSON.parse(epdData.data);
			var infoData = {}; 
			Object.keys(json.data).map((e,i)=>{
				infoData[e] = json.data[e]??''.trim();
			});
			console.log(infoData);
			if(infoData.INQ_CN == 0){
				modalAlert('','해당 개체의 귀표번호로 조회된 유전체 정보가 없습니다.');
				return;
			}else{
				if(!isNaN(parseFloat(infoData.GENE_BREDR_VAL2))){
					$('td[name=reProduct1]').html(parseFloat((infoData.GENE_BREDR_VAL2))||'-');
				}else{
					$('td[name=reProduct1]').html('-');
				}
				$('td[name=dscReProduct1]').html((infoData.GENE_EVL_RZT_DSC2??'').trim()||'-');
				if(!isNaN(parseFloat(infoData.GENE_BREDR_VAL3))){
					$('td[name=reProduct2]').html(parseFloat(infoData.GENE_BREDR_VAL3)||'-');
				}else{
					$('td[name=reProduct2]').html('-');
				}
				$('td[name=dscReProduct2]').html((infoData.GENE_EVL_RZT_DSC3??'').trim()||'-');
				if(!isNaN(parseFloat(infoData.GENE_BREDR_VAL4))){
					$('td[name=reProduct3]').html(parseFloat(infoData.GENE_BREDR_VAL4)||'-');
				}else{
					$('td[name=reProduct3]').html('-');
				}
				$('td[name=dscReProduct3]').html((infoData.GENE_EVL_RZT_DSC4??'').trim()||'-');
				if(!isNaN(parseFloat(infoData.GENE_BREDR_VAL5))){
					$('td[name=reProduct4]').html(parseFloat(infoData.GENE_BREDR_VAL5)||'-');
				}else{
					$('td[name=reProduct4]').html('-');
				}
				$('td[name=dscReProduct4]').html((infoData.GENE_EVL_RZT_DSC5??'').trim()||'-');
			}
    	}else{
			modalAlert('',epdData.message);
			return;
    	}    	
    	*/
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "COW_DETAIL";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
    		$(document).off('click','div.winpop button.winpop_back');
			$(document).on('click','div.winpop button.winpop_back',function(){
				var target = 'cowDetail';
				window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
				let obj = JSON.parse($('input[name=parentObj]').val());
				$('form[name=frm_parent]').append('<input type="hidden" name="place" value="'+obj.place+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="naBzplc" value="'+obj.naBzplc+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="aucDt" value="'+obj.aucDt+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="aucObjDsc" value="'+obj.aucObjDsc+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="sraIndvAmnno" value="'+obj.sraIndvAmnno+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="aucPrgSq" value="'+obj.aucPrgSq+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="oslpNo" value="'+obj.oslpNo+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="ledSqno" value="'+obj.ledSqno+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="bidPopYn" value="'+obj.bidPopYn+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="resultPopYn" value="'+obj.resultPopYn+'" />');
				$('form[name=frm_parent]').append('<input type="hidden" name="tabId" value="1" />');
				var form = document.frm_parent;
				form.action = "/cowDetail";
				form.target=target;
				form.submit();
			});
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);


function getInfMca(params){
	var result;
	$.ajax({
		url: '/auction/api/getInfMca',
		data: JSON.stringify(params),
		type: 'POST',
		contentType: 'application/json',
		dataType: 'json',
		async: false,
		success : function() {
		},
		error: function(xhr, status, error) {
		}
	}).done(function (data) {
		result = data;
	});
	console.log(result);
	return result;
}

var getAiakInfo = function(callback){
	fnShowLoadingImg();
	var param = new Object();
	param.barcode = ($('form[name="frm"] input[name=sraIndvAmnno]').val()||'').substr(3);
	$.ajax({
		url: '/info/getAiakInfo',
		data: param,
		type: 'POST',
		dataType: 'html',
		async : true,
		success : function(html) {
			if($(html).find('table') && $(html).find('table').length > 0){		
				var bloodInfo = $(html).find('table').get(1);
				$(bloodInfo).find('tbody tr').each((i,e)=>{
					var bloodTxt ='';
					if(i != '2' && i != '4' && i != '5'){
						bloodTxt+='[KPN] '+($.trim($(e).find('td:eq(1)').text())||'-')+'<br/>';
					}
					bloodTxt+=' '+($.trim($(e).find('td:eq(3)').text())||'-')||'-';					
					$('td[name=blInfo_'+(i)+']').html(bloodTxt);
				});
				var postIndvInfo = $(html).find('table').get(6);
				$('table.postIndvTable tbody').empty();
				var postHtml = '<tr><td rowspan="2" colspan="5" class="ta-C">-</td></tr>';
				if($(postIndvInfo).find('tbody tr td').length > 1){
					postHtml='';
					$(postIndvInfo).find('tbody tr').each((i,e)=>{
						postHtml += '<tr>';
						postHtml += '	<td rowspan="2" class="ta-C bg-gray matime">'+(($(e).find('td:eq(0)').text()||'-'))+'</td>';
						postHtml += '	<td rowspan="2" class="ta-C sraIndvAmnno">'+(($(e).find('td:eq(2)').text()||'-'))+'</td>';
						postHtml += '	<td class="ta-C rgDsc">'+(($.trim($(e).find('td:eq(3)').text())||'-'))+'</td>';
						postHtml += '	<td class="ta-C indvSexC">'+(($.trim($(e).find('td:eq(4)').text())||'-'))+'</td>';
						postHtml += '	<td class="ta-C birth">'+(($.trim($(e).find('td:eq(5)').text())||'-'))+'</td>';
						postHtml += '</tr>';
						postHtml += '<tr>';
						postHtml += '	<td class="ta-C metrbMetqltGrd">'+(($.trim($(e).find('td:eq(9)').text())||'-'))+'</td>';
						postHtml += '	<td class="ta-C metrbBbdyWt">'+(($.trim($(e).find('td:eq(8)').text())||'-'))+'</td>';
						postHtml += '	<td class="ta-C mifBtcDt">'+(($.trim($(e).find('td:eq(10)').text())||'-'))+'</td>';
						postHtml += '</tr>';
					});
				}
				$('table.postIndvTable tbody').append(postHtml);
	
				var sibIndvInfo = $(html).find('table').get(7);
				$('table.sibIndvTable tbody').empty(); 
				var sibHtml = '<tr><td rowspan="2" colspan="5" class="ta-C">-</td></tr>';
				if($(sibIndvInfo).find('tbody tr td').length > 1){
					sibHtml='';
					$(sibIndvInfo).find('tbody tr').each((i,e)=>{						
						sibHtml += '<tr>';
						sibHtml += '	<td rowspan="2" class="ta-C bg-gray matime">'+(($(e).find('td:eq(0)').text()||'-'))+'</td>';
						sibHtml += '	<td rowspan="2" class="ta-C sraIndvAmnno">'+(($(e).find('td:eq(2)').text()||'-'))+'</td>';
						sibHtml += '	<td class="ta-C rgDsc">'+(($.trim($(e).find('td:eq(3)').text())||'-'))+'</td>';
						sibHtml += '	<td class="ta-C indvSexC">'+(($.trim($(e).find('td:eq(4)').text())||'-'))+'</td>';
						sibHtml += '	<td class="ta-C birth">'+(($.trim($(e).find('td:eq(5)').text())||'-'))+'</td>';
						sibHtml += '</tr>';
						sibHtml += '<tr>';
						sibHtml += '	<td class="ta-C metrbMetqltGrd">'+(($.trim($(e).find('td:eq(9)').text())||'-'))+'</td>';
						sibHtml += '	<td class="ta-C metrbBbdyWt">'+(($.trim($(e).find('td:eq(8)').text())||'-'))+'</td>';
						sibHtml += '	<td class="ta-C mifBtcDt">'+(($.trim($(e).find('td:eq(10)').text())||'-'))+'</td>';
						sibHtml += '</tr>';
					});
				}
				$('table.sibIndvTable tbody').append(sibHtml);				
				var epdInfo = $(html).find('table').get(4);
				$(epdInfo).find('tbody tr:eq(0) td:not(.t1)').each((i,e)=>{
					var txt = $.trim($(e).text()||'-');
					$('td[name=reProduct'+(i+1)+']').text(txt);
				});
				$(epdInfo).find('tbody tr:eq(1) td:not(.t1)').each((i,e)=>{
					var txt = $.trim($(e).text()||'');
					$('td[name=dscReProduct'+(i+1)+'] span').text(txt);
				});
				$('div.tab_area h3.tit2:eq(0) .subTxt').html('※한국종축개량협회 제공');	
			}else{
				//$('div.tab_area h3.tit2:eq(0) .subTxt').html('※축산경제통합시스템 제공');
				modalAlert('', '종축개량협회 등재 내역에<br/> 조회되지 않는 개체입니다.<br/>확인바랍니다.');
			}
		},
		error: function(xhr, status, error) {
			//$('div.tab_area h3.tit2:eq(0) .subTxt').html('※축산경제통합시스템 제공');
			modalAlert('', '종축개량협회 등재 내역에<br/> 조회되지 않는 개체입니다.<br/>확인바랍니다.');
		}
	}).done(function (json) {
		if(callback)callback();	
		setTimeout(function(){
			fnHideLoadingImg();
		},1000);
	});
};

function epdChartDraw(){

	//차트 생성
	var ctx = $("#epdChart");
	//ctx.height(340);
	var labels = [];
	var epdData = [];
	
	$('div.epdInfo td[name^=dscReProduct]').each((i,o)=>{
    	var data = $(o).text().trim();    	
    	epdData.push((data == 'A'? 4:(data == 'B'? 3 : (data == 'C' ? 2 : (data == 'D' ? 1 :0)))));
		labels.push($(o).closest('tr').find('th').text().trim());
	});
	var config = {
	    type : 'radar',
	    data : {
	        labels: labels,
	        datasets:[
	           	{
	                label:'개체 EPD',
	                data: epdData,
	                borderColor:"rgba(35,155,223,1)",
	                backgroundColor:"rgba(35,194,223,0.4)",
	                tension: 0
	            }
	        ]
	    },
	    options:{
	        responsive: false,
	        title:{
	            display:false,
	            text:'개체 Epd Chart'
	        },scale:{
	        	ticks: {
        			beginAtZero: true
        			,fontSize : 14
        		}
	        }
	        ,legend:{
	    		labels:{
	    			fontSize:14
	    		}            	
	        }
	    }           
	};
	chart = new Chart(ctx, config);
}


function showGradient() {
    $(".fixdCellGrup").each(function () {
        if ($(this).width() < $(this).children().innerWidth() == true) {
            //sticky 테이블 체크 여부
            if ($(this).parent(".scroll_wrap").hasClass("left_fixdTbl") == true) {
                //sticky 테이블 위치 설정
                var wd = 0;
                //prettier-ignore
                $(this).find("thead th.fixd_box").each(function (index, item) {
                    $(item).css("left", wd + "px");
                    wd += $(item).outerWidth();
                });
                //prettier-ignore
                $(this).find("tbody tr").each(function () {
                    var tbody_wd = 0;
                    //prettier-ignore
                    $(this).find("td.fixd_box").each(function (index, item) {
                        $(item).css("left", tbody_wd + "px");
                        tbody_wd += $(item).outerWidth();
                    });
                });
            }
        }
        if ($(this).height() < $(this).children().innerHeight() == true) {
            $(this).parents(".scroll_wrap").addClass("sc_v_use");
        }
    });
}