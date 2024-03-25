;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    	//tabLoad('0');
    	var tabId = $('input[name=tabId]').val()||'0';
    	tabLoad(tabId);
    };

    var setBinding = function() {
    	$('div.tab_list a').click(function(e){
    		if($(this).hasClass('act')) return;
    		const id = $(this).data('tabId');
    		tabLoad(id);
    	});
    	
    	$(document).on('click','.btnCowSearch',function(e){
    		var indvNo = ""+$(this).data('indvNo');
    		var indvBldDsc = ""+$(this).data('indvBldDsc');
    		if(indvNo && indvNo.length == 15){
				//var param = {
				//	naBzplc : $("form[name='frmDetail'] input[name='naBzplc']").val()
				//	,sraIndvAmnno : indvNo
				//}
    			
    			//var title = $(this).closest('tr').find('th').text().trim();
    			var title = $(this).closest('dl').find('dt').text().trim();
    			
	    		$("form[name='frmDetail'] input[name='title']").val(title);
	    		$("form[name='frmDetail'] input[name='sraIndvAmnno']").val(indvNo);
	    		$("form[name='frmDetail'] input[name='indvBldDsc']").val(indvBldDsc);
	    		//$("form[name='frmDetail'] input[name='aucDt']").val(aucDt);
	    		$("form[name='frmDetail'] input[name='parentObj']").val(JSON.stringify($('form[name=frm]').serializeObject()));
				var temp = window.location.search.split("&");
				var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
				var form = document.frmDetail;
				form.action = "/cowDetailFull"+params.join("&");
				//form.target=target;
				form.submit();
    		}
			
			return;
    	});
    };

    var namespace = win.auction;
    var __COMPONENT_NAME = "RESULT_LIST";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);


var tabLoad = function(tabId){
	var param = $('form[name=frm]').serializeObject();
	param.tabId = tabId;
	
	$.ajax({
		url: '/info/getCowInfo',
		data: param,
		type: 'POST',
		dataType: 'html',
		async : false,
		success : function(html) {
    		$('div.cow-detail div.tab_area').empty();
    		$('div.cow-detail div.tab_area').append(html);
		},
		error: function(xhr, status, error) {
		}
	}).done(function (json) {
		if(tabId == '1'){
//			//getAiakInfo();
		    showGradient(); // 데이터 불러올때 마지막에 재호출 해주세요
		    $(".scroll_wrap .fixdCellGrup").on("scroll", showGradient);
		}else if(tabId == '2'){
			//getAiakInfo(drawChart);
			drawChart();
		}
	});
};


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
					if($(e).find('td:eq(3)').text()){
						bloodTxt+= '<button type="button" class="btn-search btnCowSearch" data-indv-no="'+('410'+($.trim($(e).find('td:eq(3)').text())||''))+'"><span class="sr-only">검색</span></button>'
					}
					$('td[name=blInfo_'+(i)+']').html(bloodTxt);
				});
				var postIndvInfo = $(html).find('table').get(6);
				$('table.postTbl tbody').empty();
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
				$('table.postTbl tbody').append(postHtml);
	
				var sibIndvInfo = $(html).find('table').get(7);
				$('table.sibTbl tbody').empty(); 
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
				$('table.sibTbl tbody').append(sibHtml);
				
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
//				$('div.tab_area h3.tit2:eq(0) .subTxt').html('※축산경제통합시스템 제공');
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

var drawChart = function(){
	$('#epdChartDiv').empty();
	$('#epdChartDiv').append('<canvas id="epdChart" style="width:100%;height: 320px"></canvas>');
	//차트 생성
    ctx = $("div#epdChartDiv #epdChart");
    var labels = [];
    var epdData = [];
    var mEpdData = [];
    $('td[name^=dscReProduct]').each((i,o)=>{
    	var data = $(o).next().text();    	
    	epdData.push((data == 'A'? 4:(data == 'B'? 3 : (data == 'C' ? 2 : (data == 'D' ? 1 :0)))));
    	labels.push($(o).text().trim());
    });
    console.log(epdData);
    var config = {
        type : 'radar',
        data : {
            //
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
            }
            ,tooltips : {
            	displayColors : false
            	, callbacks:{
            		label : function(item,data){
            			const rData = data.datasets[item.datasetIndex].data[item.index];
            			return '' + (rData == 4 ? 'A' :(rData == 3? 'B' : (rData == 2 ? 'C' : (rData == 1 ? 'D' :''))));
            		}
            	}
            }
            ,scale:{
            	ticks: {
            		beginAtZero: true
            		,fontSize : 14
            		, suggestedMax:4
            		, display: true
            		, callback : function(data, index){
            			return (data == 4 ? 'A' :(data == 3? 'B' : (data == 2 ? 'C' : (data == 1 ? 'D' :''))));
            		}
				}
            }
        }           
    };
    chart = new Chart(ctx, config);
}; 

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