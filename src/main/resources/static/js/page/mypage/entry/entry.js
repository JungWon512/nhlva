;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
		var tabId = $('div.tab_list li a.act').attr('data-tab-id');
		$('div.tab_area').hide();
		$('div.tab_area.'+tabId).show();
    };

    var setBinding = function() {
		$(document).on("click",".tab_list ul > li", function(){
			var tabId = $(this).find('a.act').attr('data-tab-id');
			$('div.tab_area').hide();
			if(tabId == "myMenu")  {
				$("#type").val('');
				$("#searchDateMy").val('');
				myMenuAjax();
			}
			
			$('div.tab_area.'+tabId).show();
			$('div.auction_list h3').text($(this).find('a').text());
		});
	
        $(document).on("keydown","li.txt input", function(e){
			if(e.keyCode == 13){
				fnSearch();				
			}	
		});
		$(document).on('click','.list_sch',function(){
			fnSearch();
		});
		// My현황 연간/월간 버튼
        $(document).on("click",".bg-green", function(){
			const btnId = $(this).attr("id");
			$(".btnMyDtType").show();
			$("#"+btnId).parent("div").hide();
			$("#ymFlag").val(btnId);
			$("#searchDateMy").val('');
			
			myMenuAjax();
        });
		// My현황 조합 변경 버튼
        $(document).on("click","a.johapLink", function(){
			myMenuAjax($(this).attr("id"), null);
        });
        // My현황 날짜 변경 버튼
        $(document).on("click",".btnMyChgDt", function(){
			myMenuAjax(null, $(this).attr("id"));
        });
        // My현황 상세보기 버튼
        $(document).on("click",".btn-more.fc-black", function(){
			$("#state").click();
        });
		// 조합변경 버튼
        $(document).on("click",".btn_flag", function(){	
			const params = {
				searchFlag: $(this).attr("id")
				, searchDateState: $('input[name=searchDateState]').val()
				, loginNo: $('input[name=loginNo]').val()
				, naBzPlcNo: $('input[name=naBzPlcNo]').val()
				, johpCd: $('input[name=johpCd]').val()
				, place: $("#naBzPlcNo").val()
			}
			
			fnSetStateList(params);
        });
        // 정산서 날짜 변경 버튼
        $(document).on('click','.btn_chg',function(){
			if(!$(this).hasClass("disabled")){
				const params = {
					flag: $(this).attr("id")
					, searchFlag: $(".btn_flag.act").attr("id")
					, searchDateState: $('input[name=searchDateState]').val()
					, loginNo: $('input[name=loginNo]').val()
					, naBzPlcNo: $('input[name=naBzPlcNo]').val()
					, johpCd: $('input[name=johpCd]').val()
					, place: $("#naBzPlcNo").val()
				}
				fnSetStateList(params);
			}
		});
		// 정산서 상세 버튼
		$(document).on('click','.move_info',function(){	
			const searchDate = $(this).attr("id").split("_")[0];
			const aucObjDsc	= $(this).attr("id").split("_")[1];
			const place	= $(this).attr("id").split("_")[2];
			let searchAucObjDsc = "";
			
			if (aucObjDsc.toString().indexOf(",") > -1) {
				searchAucObjDsc = "";
			} else {
				switch (aucObjDsc) {
					case "송아지" :
						searchAucObjDsc = "1"
					break;
					case "비육우" :
						searchAucObjDsc = "2"
					break;
					case "번식우" :
						searchAucObjDsc = "3"
					break;
				}
			}
			let form = $('<form></form>');
			form.attr('action', "/my/entryInfo"+location.search);
			form.attr('method', 'post');
			form.appendTo('body');			
			form.append($("<input type='hidden' value="+place+" name='searchnaBzPlcNo'>"));
			form.append($("<input type='hidden' value="+searchDate+" name='searchDate'>"));
			form.append($("<input type='hidden' value="+searchAucObjDsc+" name='searchAucObjDsc'>"));

			form.submit();
		});
		
        $(document).on("click",".btn_print", function(){
			var head = new Array();
			var body = new Array();
			$('.auction_result .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.auction_result .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			reportPrint('auction_result','출장우내역',head,body);
        });
        $(document).on("click",".btn_excel", function(){
			var head = new Array();
			var body = new Array();
			$('.auction_result .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.auction_result .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('경매결과',head,body);
        });
        
        $(document).on("click",".list_body .pd_ea a", function(){
			var pdEa = new String($(this).find('span').attr('fullstr'));
			var content = pdEa.substring(6,10)+'&nbsp;&nbsp;'+pdEa.substring(10,14)+'&nbsp;&nbsp;'+pdEa.substring(14,15);
			modalAlert('개체번호',content);
        });
    };
    
    // 정산서 조회
    var fnSetStateList = function(params){	
		//조회 기간을 현재 월 기준 최대 6개월까지 제한
		var flag = params.flag;
		var searchMonth = new Date(params.searchDateState.substr(0,4), params.searchDateState.substr(4,2) - 1, "01");
		var now = new Date();
		var monthDiff = now.getMonth() - searchMonth.getMonth() + (12 * (now.getFullYear() - searchMonth.getFullYear()));
		
		COMMONS.callAjax("/auction/api/select/entryList", "post", params, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
			$(".date_board").empty();
			
			let sHtml = "";
			
			sHtml += '<div class="area-top">';
			sHtml += '	<input type="hidden" id="searchDateState" name="searchDateState" value="'+ data.inputParam.searchDateState +'"/>';
			sHtml += '	<dl class="date_top">';
			sHtml += '		<dl>';
			sHtml += '			<dt>'+ data.title +'</dt>';
			sHtml += '			<dd id="prev" class="btn_chg btn_prev '+((flag == undefined || flag == "prev") && monthDiff >= 4 ? 'disabled' : "")+'"><a href="javascript:;">이전</a></dd>';
			sHtml += '			<dd id="next" class="btn_chg btn_next '+((flag == undefined || flag == "next") && monthDiff <= 1 ? 'disabled' : "")+'"><a href="javascript:;">다음</a></dd>';
			sHtml += '		</dl>';
			sHtml += '	</dl>';
			sHtml += '	<div class="btns">';
			sHtml += '	<p style="color:red; font-size:18px; margin-bottom: 10px;">*최근 6개월 내 정산서만 검색 가능합니다.</p>';
			sHtml += '	<button type="button" id="johap" class="btn_flag '+ (params.searchFlag == 'johap' ? "act" : "") +'">'+ data.johapData.AREANM +'축협</button>';
			sHtml += '	<button type="button" id="all" class="btn_flag '+ (params.searchFlag == 'all' ? "act" : "") +'">전체축협</button>';		//TODO : 농가를 아예 통합회원에서 제거하게 되면 수정 or 제거해야 할 부분
			sHtml += '</div>';
			sHtml += '<p class="txt-result"><span class="fc-red">'+ data.info.length +'</span>건이 조회되었습니다.</p>';
			sHtml += '<div class="date_bottom">';
			sHtml += '	<ul>';
			if (data.info.length > 0) {
				for (let item of data.info) {
					sHtml += '		<li id="'+ item.AUC_DT +'_'+ item.AUC_OBJ_DSC_NAME +'_'+ item.NA_BZPLCNO +'" class="move_info buy_tab3">';
					sHtml += '			<dl>';																				
					sHtml += '				<dt>';
					sHtml += '					<p>'+ item.AUC_DAY +'</p>';
					sHtml += '					<span>'+ item.AUC_WEEK_DAY +'요일</span>';
					sHtml += '				</dt>';
					sHtml += '				<dd>';
					sHtml += '					<p class="auc-tit">'+ item.CLNTNM + ' ' + item.AUC_OBJ_DSC_NAME +' 경매</p>';
					sHtml += '					<ul class="auc-info">';
					sHtml += '						<li>';
					sHtml += '							<strong>낙찰두수</strong>';
					sHtml += '							<span><em class="fc-blue">'+ item.COW_CNT +'</em>두</span>';
					sHtml += '						</li>';
					sHtml += '						<li>';
					sHtml += '							<strong>낙찰금액</strong>';
					sHtml += '							<span><em class="fc-red">'+ fnSetComma(item.SRA_SBID_AM - item.SRA_TR_FEE) +'</em>원</span>';
					sHtml += '						</li>';
					sHtml += '					</ul>';
					sHtml += '				</dd>';
					sHtml += '			</dl>';
					sHtml += '		</li>';																	
				}
			} else {
				sHtml += '		<li>';								
				sHtml += '			<dl class="no_date">';								
				sHtml += '				<h4>정산내역이 없습니다.</h4>';								
				sHtml += '			</dl>';								
				sHtml += '		</li>';								
			}
			sHtml += '	</ul>';
			sHtml += '</div>';
			
			$(".date_board").append(sHtml);
		});
	}
	
	var fnSearch = function(){
		if (!$("#searchDate").val()) {
			modalAlert('','경매일자를 선택해주세요.');
			return;
		} 
		
		var param = {
			naBzPlcNo : $("#naBzPlcNo").val()
			, place : $("#naBzPlcNo").val()
			, loginNo : $("#loginNo").val()
			, searchDate : $("#searchDate").val()
			, searchAucObjDsc : $("select[name=searchAucObjDsc]").val()
		};
		COMMONS.callAjax("/auction/api/select/myEntryBidList", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
	
			$('.auction_result div.list_body ul.bodyBid').empty();
			if(data.data.length == 0){
				$('.auction_result div.list_body ul.bodyBid').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");								
			}
			data.data.forEach(function(item,i){
				var sHtml = "";							
				sHtml += "<li><dl>";
				sHtml += " <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>";	
				sHtml += " <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>";	
				sHtml += " <dd class='name'>"+getStringValue(item.FTSNM)+"</dd>";					
				sHtml += " <dd class='pd_ea textNumber'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>";	
				sHtml += " <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>";	
				sHtml += " <dd class='pd_kg textNumber'>"+fnSetComma(getStringValue(item.COW_SOG_WT))+"</dd>";					
				sHtml += " <dd class='pd_kpn'>"+getStringValue(item.KPN_NO_STR)+"</dd>";	
				sHtml += " <dd class='pd_num1'>"+getStringValue(item.SRA_INDV_PASG_QCN)+"</dd>";	
				sHtml += " <dd class='pd_pay1 textNumber'>"+fnSetComma(getStringValue(item.LOWS_SBID_LMT_UPR))+"</dd>";	
				sHtml += " <dd class='pd_pay2 textNumber'>"+fnSetComma(getStringValue(item.SRA_SBID_UPR == 0 ? '-' : item.SRA_SBID_UPR))+"</dd>";	
				sHtml += " <dd class='pd_state'>"+getStringValue(item.SEL_STS_DSC_NAME)+"</dd>";	
				sHtml += " <dd class='pd_etc'>"+getStringValue(item.RMK_CNTN)+"</dd>";	
				sHtml += "</dl></li>";
				$('.auction_result div.list_body ul.bodyBid').append(sHtml);
			});
			convertScroll();
		});	
	}  
    
    // My 현황 탭
	var setChart =  function(entryList) {
		var doughChart;
		var barChart;
		
	    // 송아지 낙찰평균 및 평균시세
	    var barData1 = 0;
	    var barData2 = 0;
	    // 비육우 낙찰평균 및 평균시세
	    var barData3 = 0;
	    var barData4 = 0;
	    // 번식우 낙찰평균 및 평균시세
	    var barData5 = 0;
	    var barData6 = 0;
	    // 송아지, 비육우, 번식우 map 만들기
	    let cowData1 = entryList.forEach(item => {
			if (item.AUC_OBJ_DSC == '1') {
				barData1 = (Math.round(item.AVG_MY_SBID_AM / 1000 ?? 0));
	   			barData2 = (Math.round(item.AVG_SBID_AM / 1000 ?? 0));
			} else if (item.AUC_OBJ_DSC == '2') {
				barData3 = (Math.round(item.AVG_MY_SBID_AM / 1000 ?? 0));
	   			barData4 = (Math.round(item.AVG_SBID_AM / 1000 ?? 0));
			} else if (item.AUC_OBJ_DSC == '3') {
				barData5 = (Math.round(item.AVG_MY_SBID_AM / 1000 ?? 0));
	   			barData6 = (Math.round(item.AVG_SBID_AM / 1000 ?? 0));
			}
		});
		
		// 차트 초기화
		$('div.barChart').empty();
    	$('div.barChart').append('<canvas id="myChartSample4" class="bar_chart"></canvas>');
	    
	    // 막대 차트 생성
		const ctx = $('#myChartSample4');
		
		var config = {
			type: 'bar',
			data: {
				labels: ['송아지', '비육우', '번식우'],
				datasets: [
					{
						label: '낙찰평균(천원)',
						data: [barData1, barData3, barData5],
						borderColor: '#a5dfdf',
						backgroundColor: '#a5dfdf',
						yAxisID: 'y-left'
					},
					{
						label: '평균시세(천원)',
						data: [barData2, barData4, barData6],
						borderColor: '#9ad0f5',
						backgroundColor: '#9ad0f5',
						yAxisID: 'y-left'
					}
				]
			},
			options: {
				interaction: {
					intersect: false,
					mode: 'index'
				},
				scales: {
					yAxes: [
						{
							id: 'y-left',
							position: 'left',
							display: true,
							ticks: {
								beginAtZero: true,
								callback: function(value, index) {
									return parseInt(value, 10).toLocaleString();
								}
							}
						}
					]
				},
				tooltips: {
					enabled: true,
					callbacks: {
						label: function(tooltipItem, data) {
							const tooltipValue = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
							return parseInt(tooltipValue, 10).toLocaleString();
						}
					}
				}
			}
		};
	
		barChart = new Chart(ctx, config);
	}
	// My 현황 탭
	var setDataEntryTable = function (list, myJohapData) {
		
		$("tfoot.top-tfoot").empty();
		$("tbody.td_johap").empty();
		
		var fHtml = [];
		var sHtml = [];
		
		if (list.length > 0) {
			fHtml.push('					<tr>                                                                                        ');
			fHtml.push('						<td>'+ list[0].CLNTNM +'</td>                                                 ');
			fHtml.push('						<td class="ta-C">'+ list[0].TOT_MY_ENTRY_CNT +'</td>                               ');
			fHtml.push('						<td class="ta-C">'+ list[0].TOT_SBID_CNT +'</td>                              ');
			fHtml.push('						<td class="ta-C">'+ list[0].TOT_FBID_CNT +'</td>                              ');
			fHtml.push('					</tr>                                                                                       ');
		} else {
			fHtml.push('					<tr>                                                                                        ');
			fHtml.push('						<td colspan="4" style="text-align: center;">조회된 내역이 없습니다.</td>                ');
			fHtml.push('					</tr>                                                                                       ');			
		}
		
		if (list.length > 0) {
			for (var i = 1; i < list.length; i++) {
				if (myJohapData.NA_BZPLC != list[i].NA_BZPLC) {
					sHtml.push('			<tr>                                                                                            ');
					sHtml.push('				<td><a href="javascript:;" id="'+ list[i].NA_BZPLC +'" class="link johapLink">'+ list[i].CLNTNM +'</a></td>       ');				
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_MY_ENTRY_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_SBID_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_FBID_CNT +'</td>                                                  ');
					sHtml.push('			</tr>                                                                                           ');
				} else {
					sHtml.push('			<tr style="background-color:#ffcf9f;">                                                                                            ');
					sHtml.push('				<td>'+ list[i].CLNTNM +'</td>       ');									
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_MY_ENTRY_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_SBID_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_FBID_CNT +'</td>                                                  ');
					sHtml.push('			</tr>                                                                                           ');
				}
			}
		}
		
		$("tfoot.top-tfoot").append(fHtml.join(""));
		$("tbody.td_johap").append(sHtml.join(""));
	}
	// My 현황 탭
	var myMenuAjax = function (johapCd, type) {
		const params = {
			flag: type ?? ""
			, ymFlag: $('input[name=ymFlag]').val() || "M"
			, stateFlag: $('input[name=stateFlag]').val() || "entry"
			, searchDate: $('input[name=searchDateMy]').val()
			, naBzplc: $('input[name=johpCd]').val()
			, naBzPlcNo: $('input[name=naBzPlcNo]').val()
			, place: $("#naBzPlcNo").val()
			, johpCd: johapCd ?? $('input[name=johpCd]').val()
		}

		COMMONS.callAjax("/auction/api/select/myMenu", "post", params, 'application/json', 'json'
		, function(data){
			if(!data || !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}

			let searchDateFormat = data.inputParam.searchDateMy.length > 4 ? (data.inputParam.searchDateMy.substring(0, 4) + "년 " + data.inputParam.searchDateMy.substring(4)) : data.inputParam.searchDateMy; 
			
			$('input[name=searchDateMy]').val(data.inputParam.searchDateMy);
			
			// 출장우 현황			
			$("h3.johapNm").html(data.myJohapData.CLNTNM + '<i class="dash searchYearMonth">'+ searchDateFormat + (data.inputParam.ymFlag == 'M' ? "월" : "년") +'</i>');
			$("span.searchYearMonth").html(searchDateFormat + (data.inputParam.ymFlag == 'M' ? "월" : "년"));
			
			$("div.simple-board > dl.top > dt").html("총 낙찰금액");
			$("div.simple-board > dl.top .totSraSbidAm").html((data.cowEntryCnt.length > 0 ? data.cowEntryCnt[0].SUM_MY_SBID_AM_FORMAT : 0) + " 원");
			
			$("div.cont > div.left > .totCowCnt").html((data.cowEntryCnt.length > 0 ? data.cowEntryCnt[0].TOT_MY_ENTRY_CNT : 0) + " 두");
			$("div.cont > div.left > .fCowCnt").html("(유찰 " + (data.cowEntryCnt.length > 0 ? data.cowEntryCnt[0].TOT_FBID_CNT : 0) + "두)");
			$("ul.cowCnt span.cow1").html((data.cowEntryCnt.length > 0 && data.cowEntryCnt[1] ? data.cowEntryCnt[1].TOT_MY_ENTRY_CNT : 0) + "두");
			$("ul.cowCnt span.cow2").html((data.cowEntryCnt.length > 0 && data.cowEntryCnt[2] ? data.cowEntryCnt[2].TOT_MY_ENTRY_CNT : 0) + "두");
			$("ul.cowCnt span.cow3").html((data.cowEntryCnt.length > 0 && data.cowEntryCnt[3] ? data.cowEntryCnt[3].TOT_MY_ENTRY_CNT : 0) + "두");
			
			// 나의 출장우 현황
			setDataEntryTable(data.cowEntryCntList, data.myJohapData);
			
			// 차트
			setChart(data.cowEntryCnt);
		});
	}

    var namespace = win.auction;
    var __COMPONENT_NAME = "MY_COW_LIST";
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

var convertScroll = function(){
	var gubun = $('.tab_list li a.act').data('tabId');	
	$('.tab_area.'+gubun).find(".list_table .list_body ul, div.modal-wrap .pop_TermsBox").each(function(){						
		$(this).removeClass('mCustomScrollBox');		
								
		$(this).css('-webkit-overflow-scrolling','touch');
		$(this).css('overflow-y','auto');
		$(this).css('overflow-x','hidden');
		
		var gubun = $('.tab_list li a.act').data('tabId');
		$('.tab_area.'+gubun).find('.list_table .list_body ul').css('height','');		
		resultH= $('section.header').outerHeight() + ($('section.contents').outerHeight() - $('.tab_area.'+gubun+' .list_table .list_body ul').outerHeight()) +$('section.footer').outerHeight() +1;
			
		resultH = $('body').outerHeight() - resultH;
		$(this).css('height','  '+resultH+'px');
		$(this).css('min-height','70px');
	});	
}
