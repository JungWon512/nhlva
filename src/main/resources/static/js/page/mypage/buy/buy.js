(function ($, win, doc) {
	
    var COMMONS = win.auction["commons"];

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
			convertScroll();
		});
        $(document).on("click",".list_sch.sch_buy", function(){
			fnSearchBuy();
        });
        $(document).on("click",".list_sch.sch_bid", function(){
			fnSearchBid();
        });
        $(document).on("click",".btn_printBuy", function(){
			var head = new Array();
			var body = new Array();
			$('.buy_list .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.buy_list .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			reportPrint('auction_result','경매결과',head,body);
        });
        $(document).on("click",".btn_excelBuy", function(){
			var head = new Array();
			var body = new Array();
			$('.buy_list .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.buy_list .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('구매내역',head,body);
        });
		
        $(document).on("click",".btn_printBid", function(){
			var head = new Array();
			var body = new Array();
			$('.bid .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.bid .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			reportPrint('auction_bid','응찰내역',head,body);
        });
        $(document).on("click",".btn_excelBid", function(){
			var head = new Array();
			var body = new Array();
			$('.bid .list_table .list_head dd').each(function(i){
				var txt = $(this).text();
				head.push({text:txt,class:$(this).attr('class')});
			});
			$('.bid .list_table .list_body li').each(function(i,obj){
				var txt = $(this).text();
				var bodyTr = new Array(); 
				$(obj).find('dl dd').each(function(i){
					var txt = $(this).text();
					bodyTr.push(txt);
				});
				body.push(bodyTr);
			});
			
			reportExcel('응찰내역',head,body);
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
        $(document).on("click",".btn_johap_flag", function(){	
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
					, searchFlag: $(".btn_johap_flag.act").attr("id")
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
			const searchTrmnAmnno = $("#loginNo").val();
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
			form.attr('action', "/my/buyInfo"+location.search);
			form.attr('method', 'post');
			form.appendTo('body');			
			form.append($("<input type='hidden' value="+place+" name='searchnaBzPlcNo'>")); //실제 검색할 조합
			form.append($("<input type='hidden' value="+searchDate+" name='searchDate'>"));
			form.append($("<input type='hidden' value="+searchAucObjDsc+" name='searchAucObjDsc'>"));
			form.append($("<input type='hidden' value="+searchTrmnAmnno+" name='searchTrmnAmnno'>"));
			form.append($("<input type='hidden' value="+$("#stateFlag").val()+" name='stateFlag'>"));
			form.append($("<input type='hidden' value="+searchTrmnAmnno+" name='loginNo'>"));

			form.submit();
		});
    };
    
     // 구매내역 조회
    var fnSearchBuy = function(){
		if (!$("#searchDateBuy").val()) {
			modalAlert('','경매일자를 선택해주세요.');
			return;
		} 
	
		var param = {naBzPlcNo : $("#naBzPlcNo").val()
			, place : $("#naBzPlcNo").val()
			, loginNo : $("#loginNo").val()
			, searchDate : $("#searchDateBuy").val()
			, searchAucObjDsc : $("select[name=searchAucObjDscBuy]").val()
		};
		COMMONS.callAjax("/auction/api/select/myBuyList", "post", param, 'application/json', 'json'
		, function(data){
			if(!data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
			
			$('.buy_list div.sum_table div').empty();
			if($("select[name=searchAucObjDscBuy]").val() == ''){
				//0:합계,1:암,2:수,3:기타
				var tableHtml = '';
				tableHtml += '		<dl>';
				tableHtml += '			<dt><p>구분</p></dt>';
				tableHtml += '			<dd><p>송아지</p></dd>';
				tableHtml += '			<dd><p>비육우</p></dd>';
				tableHtml += '			<dd><p>번식우</p></dd>';
				tableHtml += '			<dd><p>합계</p></dd>';
				tableHtml += '		</dl>';
				tableHtml += '		<dl>';
				tableHtml += '			<dt><p>전체</p></dt>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_CALF)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_NO_COW)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_COW)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT)+'</span>두</p></dd>';
				tableHtml += '		</dl>';
				tableHtml += '		<dl>';
				tableHtml += '			<dt><p>암</p></dt>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_W_F_1)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_W_F_2)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_W_F_3)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_W_F)+'</span>두</p></dd>';
				tableHtml += '		</dl>';
				tableHtml += '		<dl>';
				tableHtml += '			<dt><p>수</p></dt>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_M_F_1)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_M_F_2)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_M_F_3)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_M_F)+'</span>두</p></dd>';
				tableHtml += '		</dl>';
				tableHtml += '		<dl>';
				tableHtml += '			<dt><p>기타</p></dt>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_ETC_F_1)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_ETC_F_2)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_ETC_F_3)+'</span>두</p></dd>';
				tableHtml += '			<dd><p><span class="ea">'+fnSetComma(data.buyCnt.CNT_SEX_ETC_F)+'</span>두</p></dd>';
				tableHtml += '		</dl>';
			
				$('.buy_list div.sum_table div').html(tableHtml);
				
			}else{
				//0:합계,1:암,2:수,3:기타
				var tableHtml = '';
				tableHtml += '	<dl class="sumBuy"> ';
				tableHtml += '		<dt><p>전체</p></dt> ';
				tableHtml += '		<dd> ';
				tableHtml += '			<p class="cowCnt"><span class="ea">'+ (data.buyCnt && data.buyCnt.CNT?fnSetComma(data.buyCnt.CNT):'0') +'</span>두</p> ';
				tableHtml += '		</dd> ';
				tableHtml += '	</dl> ';
				tableHtml += '	<dl> ';
				tableHtml += '		<dt><p>암</p></dt>';
				tableHtml += '		<dd>';
				tableHtml += '			<p class="cowCnt"><span class="ea">'+ (data.buyCnt && data.buyCnt.CNT_SEX_W_F?fnSetComma(data.buyCnt.CNT_SEX_W_F):'0') +'</span>두</p> ';
				tableHtml += '		</dd> ';
				tableHtml += '	</dl> ';
				tableHtml += '	<dl> ';
				tableHtml += '		<dt><p>수</p></dt>';
				tableHtml += '		<dd>';
				tableHtml += '			<p class="cowCnt"><span class="ea">'+ (data.buyCnt && data.buyCnt.CNT_SEX_M?fnSetComma(data.buyCnt.CNT_SEX_M):'0') +'</span>두</p>';
				tableHtml += '		</dd>';
				tableHtml += '	</dl>';
				tableHtml += '	<dl>';
				tableHtml += '		<dt><p>기타</p></dt>';
				tableHtml += '		<dd>';
				tableHtml += '			<p class="cowCnt"><span class="ea">'+ (data.buyCnt && data.buyCnt.CNT_SEX_ETC?fnSetComma(data.buyCnt.CNT_SEX_ETC):'0') +'</span>두</p>';
				tableHtml += '		</dd>';
				tableHtml += '	</dl>';
				$('.buy_list div.sum_table div').html(tableHtml);				
			}
			
			$('.auction_result div.list_body ul.bodyBuy').empty();
			if(data.data.length == 0){
				$('.auction_result div.list_body ul.bodyBuy').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");
			}
			data.data.forEach(function(item,i){
				var sHtml = [];							
				sHtml.push("<li><dl>");
				sHtml.push(" <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>");	
				sHtml.push(" <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>");	
				sHtml.push(" <dd class='name'>"+getStringValue(item.FTSNM)+"</dd>");	
				sHtml.push(" <dd class='pd_ea textNumber'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>");	
				sHtml.push(" <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>");	
				sHtml.push(" <dd class='pd_kg textNumber'>"+fnSetComma(getStringValue(item.COW_SOG_WT))+"</dd>");	
				sHtml.push(" <dd class='pd_kpn'>"+getStringValue(item.KPN_NO_STR)+"</dd>");	
				sHtml.push(" <dd class='pd_num1'>"+getStringValue(item.SRA_INDV_PASG_QCN)+"</dd>");	
				sHtml.push(" <dd class='pd_pay1 textNumber'>"+fnSetComma(getStringValue(item.LOWS_SBID_LMT_UPR))+"</dd>");	
				sHtml.push(" <dd class='pd_pay2 textNumber'>"+fnSetComma(getStringValue(item.SRA_SBID_UPR))+"</dd>");	
				sHtml.push(" <dd class='pd_state'>"+getStringValue(item.SEL_STS_DSC_NAME)+"</dd>");	
				sHtml.push(" <dd class='pd_etc'>"+getStringValue(item.RMK_CNTN)+"</dd>");
				sHtml.push("</dl></li>");
				$('.auction_result div.list_body ul.bodyBuy').append(sHtml.join(""));
			});
			convertScroll();
		});	
	}
	
	// 응찰내역 조회
	var fnSearchBid = function(){
		if (!$("#searchDateBid").val()) {
			modalAlert('','경매일자를 선택해주세요.');
			return;
		} 
		
		var param = {
			naBzPlcNo : $("#naBzPlcNo").val()
			, place : $("#naBzPlcNo").val()
			, loginNo : $("#loginNo").val()
			, searchDate : $("#searchDateBid").val()
			, searchAucObjDsc : $("select[name=searchAucObjDscBid]").val()
		};
		COMMONS.callAjax("/auction/api/select/myBidList", "post", param, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}

			$('.auction_bid div.list_body ul.bodyBid').empty();
			if(data.data.length == 0){
				$('.auction_bid div.list_body ul.bodyBid').append("<li><dl><dd>검색결과가 없습니다.</dd></dl></li>");								
			}
			data.data.forEach(function(item,i){
				var sHtml = [];							
				if (item.SEL_STS_DSC_NAME == '-') {
					sHtml.push("<li style=''><dl>");					
				} else{
					sHtml.push("<li style='background-color:#fbfbb0;'><dl>");					
				}
				sHtml.push(" <dd class='date'>"+getStringValue(item.AUC_DT_STR)+"</dd>");	
				sHtml.push(" <dd class='num'>"+getStringValue(item.AUC_PRG_SQ)+"</dd>");	
				sHtml.push(" <dd class='pd_ea textNumber'>"+getStringValue(item.SRA_INDV_AMNNO_FORMAT)+"</dd>");	
				sHtml.push(" <dd class='pd_sex'>"+getStringValue(item.INDV_SEX_C_NAME)+"</dd>");	
				sHtml.push(" <dd class='pd_kg textNumber'>"+fnSetComma(getStringValue(item.COW_SOG_WT))+"</dd>");	
				sHtml.push(" <dd class='pd_pay1 textNumber'>"+fnSetComma(getStringValue(item.LOWS_SBID_LMT_UPR))+"</dd>");	
				sHtml.push(" <dd class='pd_pay2 textNumber'>"+fnSetComma(getStringValue(item.ATDR_AM))+"</dd>");	
				sHtml.push(" <dd class='pd_pay3 textNumber'>"+fnSetComma(getStringValue(item.SRA_SBID_UPR))+"</dd>");	
				sHtml.push(" <dd class='pd_state'>"+getStringValue(item.SEL_STS_DSC_NAME)+"</dd>");	
				sHtml.push("</dl></li>");
				$('.auction_bid div.list_body ul.bodyBid').append(sHtml.join(""));
			});
			convertScroll();
		});	
	}
	// 정산서 조회
	var fnSetStateList = function(params){	
		//조회 기간을 현재 월 기준 최대 6개월까지 제한
		var flag = params.flag;
		var searchMonth = new Date(params.searchDateState.substr(0,4), params.searchDateState.substr(4,2) - 1, "01");
		var now = new Date();
		var monthDiff = now.getMonth() - searchMonth.getMonth() + (12 * (now.getFullYear() - searchMonth.getFullYear()));
		
		COMMONS.callAjax("/auction/api/select/buyList", "post", params, 'application/json', 'json'
		, function(data){
			if(data && !data.success){
				modalAlert('','작업중 오류가 발생했습니다. <br/>관리자에게 문의하세요.');
				return;
			}
			$(".date_board").empty();
			
			let sHtml = [];
			
			sHtml.push('<div class="area-top">');
			sHtml.push('	<input type="hidden" id="searchDateState" name="searchDateState" value="'+ data.inputParam.searchDateState +'"/>');
			sHtml.push('	<dl class="date_top">');
			sHtml.push('		<dl>');
			sHtml.push('			<dt>'+ data.title +'</dt>');
			sHtml.push('			<dd id="prev" class="btn_chg btn_prev '+((flag == undefined || flag == "prev") && monthDiff >= 4 ? 'disabled' : "")+'"><a href="javascript:;">이전</a></dd>');
			sHtml.push('			<dd id="next" class="btn_chg btn_next '+((flag == undefined || flag == "next") && monthDiff <= 1 ? 'disabled' : "")+'"><a href="javascript:;">다음</a></dd>');
			sHtml.push('		</dl>');
			sHtml.push('	</dl>');
			sHtml.push('	<div class="btns">');
			sHtml.push('	<p style="color:red; font-size:18px; margin-bottom: 10px;">*최근 6개월 내 정산서만 검색 가능합니다.</p>');
			sHtml.push('	<button type="button" id="johap" class="btn_johap_flag '+ (params.searchFlag == 'johap' ? "act" : "") +'">'+ data.johapData.AREANM +'축협</button>');
			sHtml.push('	<button type="button" id="all" class="btn_johap_flag '+ (params.searchFlag == 'all' ? "act" : "") +'">전체축협</button>');
			sHtml.push('</div>');
			sHtml.push('<p class="txt-result"><span class="fc-red">'+ data.info.length +'</span>건이 조회되었습니다.</p>');
			sHtml.push('<div class="date_bottom">');
			sHtml.push('	<ul>');
			if (data.info.length > 0) {
				for (let item of data.info) {
					sHtml.push('		<li id="'+ item.AUC_DT +'_'+ item.AUC_OBJ_DSC_NAME +'_'+ item.NA_BZPLCNO +'" class="move_info buy_tab3">');
					sHtml.push('			<dl>');																				
					sHtml.push('				<dt>');
					sHtml.push('					<p>'+ item.AUC_DAY +'</p>');
					sHtml.push('					<span>'+ item.AUC_WEEK_DAY +'요일</span>');
					sHtml.push('				</dt>');
					sHtml.push('				<dd>');
					sHtml.push('					<p class="auc-tit">'+ item.CLNTNM + ' ' + item.AUC_OBJ_DSC_NAME +' 경매</p>');
					sHtml.push('					<ul class="auc-info">');
					sHtml.push('						<li>');
					sHtml.push('							<strong>낙찰두수</strong>');
					sHtml.push('							<span><em class="fc-blue">'+ item.COW_CNT +'</em>두</span>');
					sHtml.push('						</li>');
					sHtml.push('						<li>');
					sHtml.push('							<strong>정산금액</strong>');
					sHtml.push('							<span><em class="fc-red">'+ fnSetComma(item.SRA_SBID_AM + item.SRA_TR_FEE) +'</em>원</span>');
					sHtml.push('						</li>');
					sHtml.push('					</ul>');
					sHtml.push('				</dd>');
					sHtml.push('			</dl>');
					sHtml.push('		</li>');																	
				}
			} else {
				sHtml.push('		<li>');								
				sHtml.push('			<dl class="no_date">');								
				sHtml.push('				<h4>정산내역이 없습니다.</h4>');								
				sHtml.push('			</dl>');								
				sHtml.push('		</li>');							
			}
			sHtml.push('	</ul>');
			sHtml.push('</div>');
			
			$(".date_board").append(sHtml.join(""));
		});
	};
    
    /** 초기화면 설정 */
	var setLayout = function(){
		// 탭화면 hide / show
		var tabId = $('div.tab_list li a.act').attr('data-tab-id');
		$('div.tab_area').hide();
		$('div.tab_area.'+tabId).show();
	};
	// My 현황 탭
	var setChart =  function(bidList) {
		var doughChart;
		var barChart;
		
		// 차트 초기화
		$('div.doughnut div.graph').empty();
    	$('div.doughnut div.graph').append('<canvas id="myDoughnutsample2"></canvas><span class="total"></span>');
    	
    	$('div.barChart').empty();
    	$('div.barChart').append('<canvas id="myChartSample3" class="bar_chart"></canvas>');
		
		//도넛 차트 생성
	    const ctx = $("#myDoughnutsample2");
	    
	    // 전체 낙찰율 데이터
	    var doughData = ""; 
	    // 송아지 낙찰두수 및 응찰두수
	    var barData1 = 0;
	    var barData2 = 0;
	    // 비육우 낙찰두수 및 응찰두수
	    var barData3 = 0;
	    var barData4 = 0;
	    // 번식우 낙찰두수 및 응찰두수
	    var barData5 = 0;
	    var barData6 = 0;
	    // 송아지, 비육우, 번식우 map 만들기
	    let cowData1 = bidList.forEach(item => {
			if (!item.AUC_OBJ_DSC) {
				doughData = item.SEL_STS_PERCENT ?? 0
			} else {
				if (item.AUC_OBJ_DSC == '1') {
					barData1 = item.SBID_COW_CNT ?? 0;
	   				barData2 = item.TOT_ATDR_CNT ?? 0;
				} else if (item.AUC_OBJ_DSC == '2') {
					barData3 = item.SBID_COW_CNT ?? 0;
	   				barData4 = item.TOT_ATDR_CNT ?? 0;
				} else if (item.AUC_OBJ_DSC == '3') {
					barData5 = item.SBID_COW_CNT ?? 0;
		   			barData6 = item.TOT_ATDR_CNT ?? 0;
				}	
			}
		})

	    var config = {
	        type : 'doughnut', 
	        data : {
//				labels: ['낙찰율', '유찰율'],
	            datasets:[
		           	{
		                data: [100 - doughData, doughData],
						backgroundColor: ['#cac3fa', '#8b7feb'],
						borderWidth: 0
		            }
	            ]
	        },
			options: {
				tooltips: {
					callbacks: {
						label : function(tooltipItem, data){
							var dataset = data.datasets[tooltipItem.datasetIndex];
							var currentValue = dataset.data[tooltipItem.index];
							return Math.floor(((currentValue / 100) * 100)) + "%";
						}
					}
				}
			}
	    };
	    doughChart = new Chart(ctx, config);
	    
	    // 막대 차트 생성
		const ctx2 = $('#myChartSample3');
		
		var config2 = {
			type: 'bar',
			data: {
				labels: ['송아지', '비육우', '번식우'],
				datasets: [
					{
						label: '낙찰두수',
						data: [barData1, barData3, barData5],
						borderColor: '#ffcf9f',
						backgroundColor: '#ffcf9f'
					},
					{
						label: '응찰두수',
						data: [barData2, barData4, barData6],
						borderColor: '#9ad0f5',
						backgroundColor: '#9ad0f5'
					}
				]
			}
		};
	
		barChart = new Chart(ctx2, config2);
	}
	// My 현황 탭
	var setDataBidTable = function (list, myJohapData) {
		
		$("tfoot.top-tfoot").empty();
		$("tbody.td_johap").empty();
		
		var fHtml = [];
		var sHtml = [];
		
		if (list.length > 0) {
			fHtml.push('					<tr>                                                                                        ');
			fHtml.push('						<td>'+ list[0].CLNTNM +'</td>                                                 ');
			fHtml.push('						<td class="ta-C">'+ list[0].TOT_COW_CNT +'</td>                               ');
			fHtml.push('						<td class="ta-C">'+ list[0].SBID_COW_CNT +'</td>                              ');
			fHtml.push('						<td class="ta-C">'+ list[0].TOT_ATDR_CNT +'</td>                              ');
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
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_COW_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].SBID_COW_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_ATDR_CNT +'</td>                                                  ');
					sHtml.push('			</tr>                                                                                           ');
				} else {
					sHtml.push('			<tr style="background-color:#ffcf9f;">                                                                                            ');
					sHtml.push('				<td>'+ list[i].CLNTNM +'</td>       ');									
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_COW_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].SBID_COW_CNT +'</td>                                                   ');
					sHtml.push('				<td class="ta-C">'+ list[i].TOT_ATDR_CNT +'</td>                                                  ');
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
			, ymFlag: $('input[name=ymFlag]').val() || "Y"
			, stateFlag: $('input[name=stateFlag]').val() || "buy"
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
			
			// 나의 구매현황			
			$("h3.johapNm").html(data.myJohapData.CLNTNM + '<i class="dash searchYearMonth">'+ searchDateFormat + (data.inputParam.ymFlag == 'M' ? "월" : "년") +'</i>');
			$("span.searchYearMonth").html(searchDateFormat + (data.inputParam.ymFlag == 'M' ? "월" : "년"));
			$("div.txt > strong.percent").html((data.cowBidPercent.length > 0 ? data.cowBidPercent[0].SEL_STS_PERCENT : 0) + " %");
			
			$("div.simple-board > dl.top > dt").html( + (data.inputParam.ymFlag == 'M' ? "이번달" : "이번해"));
			$("div.simple-board > dl.top .totSraSbidAm").html((data.cowBidCnt.length > 0 ? data.cowBidCnt[0].TOT_SRA_SBID_AM_FORMAT : 0) + " 원");
			
			$("div.cont > div.left > .totCowCnt").html((data.cowBidCnt.length > 0 ? data.cowBidCnt[0].TOT_COW_CNT : 0) + " 두");
			$("ul.cowCnt span.cow1").html((data.cowBidCnt.length > 0 && data.cowBidCnt[1] ? data.cowBidCnt[1].TOT_COW_CNT : 0) + "두");
			$("ul.cowCnt span.cow2").html((data.cowBidCnt.length > 0 && data.cowBidCnt[2] ? data.cowBidCnt[2].TOT_COW_CNT : 0) + "두");
			$("ul.cowCnt span.cow3").html((data.cowBidCnt.length > 0 && data.cowBidCnt[3] ? data.cowBidCnt[3].TOT_COW_CNT : 0) + "두");
			
			// 나의 응찰 현황
			setDataBidTable(data.cowBidCntList, data.myJohapData);
			
			// 차트
			setChart(data.cowBidPercent);
			
			// 축종별 응찰현황
			$("div.graph span.total").html("전체 <br>" + (data.cowBidCnt.length > 0 ? data.cowBidCnt[0].TOT_COW_CNT : 0) + "건");
		});
	}
	
    var namespace = win.auction;
    var __COMPONENT_NAME = "MY_LIST";
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

var inputNumberVaild = function(el,len){
	if(el.value.length > len)  {
		el.value  = el.value.substr(0, len);
  	}
}

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
