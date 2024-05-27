;(function ($, win, doc) {

    var COMMONS = win.auction["commons"];

	if ($('#searchAucObjDsc').val() === '5') {
		$('.auction_result .list_head dl dd.pd_ea').addClass('goat');
		$('.auction_result .list_head dl dd.name').addClass('goat');
		$('.auction_result .list_body dl dd.pd_ea').addClass('goat');
		$('.auction_result .list_body dl dd.name').addClass('goat');
	}

    var setLayout = function() {
		$('.list_table .list_body li dd').each(function(){
			if(!$(this).text()) $(this).text('-');
		});
		$('.list_table .list_body li dd span').each(function(){
			if(!$(this).text()) $(this).text('-');
		});
		
		$(".btn_del").hide();
    };

    var setBinding = function() {
		$(".filter-box ").removeClass("active"); // 검색 후 필터 박스 초기화
        $(document).on("keydown","li.txt input", function(e){
			if(e.keyCode == 13){
				fnSearch();				
			}	
		});
		$(document).on('click','.list_sch',function(){
			fnSearch();
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
			reportPrint('auction_result','경매결과',head,body);
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
                
      	$(document).on("click",".list_body li dl dd:not(.pd_pav)", function(){			
			var pdEa = new String($(this).find('span').attr('fullstr'));	
			var tr = $(this).closest('li');
			
        	if(tr.find('.commitYn').val() != '1'){
        		return;
        	}
			$("form[name='frm_select'] input[name='naBzplc']").val(tr.find('.naBzPlc').val());
			$("form[name='frm_select'] input[name='aucDt']").val(tr.find('.aucDt').val());
			$("form[name='frm_select'] input[name='aucObjDsc']").val(tr.find('.aucObjDsc').val());
			$("form[name='frm_select'] input[name='oslpNo']").val(tr.find('.oslpNo').val());
			$("form[name='frm_select'] input[name='oslpNo']").val(tr.find('.oslpNo').val());
			$("form[name='frm_select'] input[name='sraIndvAmnno']").val(pdEa);
			$("form[name='frm_select'] input[name='aucPrgSq']").val(tr.find('.aucPrgSq').val());
			$("form[name='frm_select'] input[name='ledSqno']").val(tr.find('.ledSqno').val());
			
			var temp = window.location.search.split("&");
			var params = temp.filter(function(el) {return el != "type=0" && el != "type=1"});
			var target = 'cowDetail';
			
			//$("form[name='frm_select']").attr("action", "/cowDetail"+params.join("&")).submit();
			window.open('',target, 'width=600, height=800, toolbar=no, menubar=no, scrollbars=no, resizable=yes');
			var form = document.frm_select;
			form.action = "/cowDetail";
			form.target=target;
			form.submit();
			
			return;
      	});
      	
      	//-------------------------------------
        //          필터 관련 이벤트
        //-------------------------------------
        
        // 필터 활성
        $(document).on("click",".btn-filter", function(){
			if($(".filter-box").hasClass("active")){
				$(".filter-box ").removeClass("active");
			}else {
				$(".filter-box").addClass("active");
			}
        });
        
        // 성별활성시 삭제버튼 생성 
         $(document).on("click",".indvSexC", function(){
			$('.sexBtn').show();
		});
		        
        // 활성중인 필터 삭제 & 초기화
        $(document).on("click",".del_btn", function(){
			$(this).hide();
			if( $(this).text() == "검색" ) {
					$(".btn_del").hide();
					$(".searchTxt").val('');
			}
			if( $(this).text() == "성별" ) {
					$("input[name=indvSexC]:checked").prop("checked", false);
			}
			if( $(this).text() == "월령" ) {
			   		$("#startDate").val('');
					$("#endDate").val('');
					$("span.rangeDate").html( "0 ~ 10개월" );
					rangeSetDate();
			}
			if( $(this).text() == "예정가" ) {
					$("#lowPrice").val('');
					$("#highPrice").val('');
					$("span.rangePrice").html( "0 ~ 1000만 원" );
					rangeSetPrice();
			}
			fnSearch();		
        });
        
        $(document).on("keyup",".searchTxt", function(){
			$(".searchTxt").not($(this)).val($(this).val());
			if( $(this).val().length != 0) {
				$(".btn_del").show();
				$('.searchBtn').show();
			} else {
				$(".btn_del").hide();
			}
		});
        
        // 인풋검색 초기화
        $(document).on("click",".btn_del", function(){
			$('.searchBtn').hide();
			$(".btn_del").hide();
			$(".searchTxt").val('');
        });
        
        // 성별 초기화
        $(document).on("change","input[name=indvSexC]", function(){
			if ($("input[name=indvSexC]:checked").length === 0) {
				$('.sexBtn').hide();
			} 
        });
        
        // 필터옵션 초기화
        $(document).on("click",".btn-initial",function(){
			filterInit();	 
			$(".del_btn").hide();
		});
		
		// 날짜선택
	   $(document).on("change","#searchDate", function(){
			if(flag == 'mo'){
				fnSearch();
			}
        });
		// 경매대상선택
	   $(document).on("change","#searchAucObjDsc", function(){
			if(flag == 'mo'){
				fnSearch();
			}
        });
        
		// 적용하기 버튼 클릭. (txt / 중량 / 예정가)
	   $(document).on("click",".btn-apply", function(){
			fnSearch();
        });
		
		// 결과조회 오름차순, 내림차순 
	   $(document).on("click",".btn-sort", function(){
			$('.btn-sort').not($(this)).removeClass();
			if($(this).hasClass('is-down') ){
				$(this).removeClass('is-down');
				$(this).addClass('is-up');  
				if( $(this).hasClass('birthUpDown')){
					$('#birthUpDown').val('2');
					$('#upDownFlag').val('birth');
					
					$('#priceUpDown').val('');
					$('#numUpDown').val('');
				} 
				if( $(this).hasClass('priceUpDown')){
					$('#priceUpDown').val('2');
					$('#upDownFlag').val('price');
					
					$('#birthUpDown').val('');
					$('#numUpDown').val('');
				}
				if( $(this).hasClass('numUpDown')){
					$('#numUpDown').val('1');
					$('#upDownFlag').val('num');
					
					$('#birthUpDown').val('');
					$('#priceUpDown').val('');
				}
			} else { 
				$(this).removeClass('is-up');
				$(this).addClass('is-down'); 
				if( $(this).hasClass('birthUpDown') ){
					$('#birthUpDown').val('1');
					$('#upDownFlag').val('birth');
					
					$('#priceUpDown').val('');
					$('#numUpDown').val('');
				}
				if( $(this).hasClass('priceUpDown') ){
					$('#priceUpDown').val('1');
					$('#upDownFlag').val('price');
					
					$('#birthUpDown').val('');
					$('#numUpDown').val('');
				}
				if( $(this).hasClass('numUpDown') ){
					$('#numUpDown').val('2');
					$('#upDownFlag').val('num');
					
					$('#birthUpDown').val('');
					$('#priceUpDown').val('');
				}
			}
			fnSearch();
        });
    };
    // 모바일 화면과 웹화면에서 fn_Search의 이벤트가 다르다
	let flag = "";
    
    var webApp = function(){
	// Not web 일때, 필터 가림.
    	if(isApp() || chkOs() != 'web'){
	        flag = 'mo';
			$(".del_btn").hide();
        } else {
			$(".filter-box ").addClass("active"); // 검색 실행 후 active 관련
	        //  비활성 필터  숨겨버리기
	        $(".del_btn").hide();
	        flag = 'web';
		}
	}
	
	var filterInit = function(){
		$("input[name=indvSexC]:checked").prop("checked", false);
		$(".searchTxt").val('');
		$(".btn_del").hide();
		$(".del_btn").hide();
		
		$("#startDate").val('');
		$("#endDate").val('');
		$("span.rangeDate").html("0 ~ 10개월");
		rangeSetDate();		
		
		$('#lowPrice').val('');
		$('#highPrice').val('');
		$("span.rangePrice").html("0 ~ 1000만 원");
		rangeSetPrice();		
	}
	
	var filterDelBtnView = function() {
		if($('#searchTxt').val() != '' ) {
			$(".btn_del").css('display','inline-block');
			$('.searchBtn').css('display','inline-block');
		}
		if( $("input[name=indvSexC]:checked").val() != null ) {
			$('.sexBtn').css('display','inline-block');
		}
		if($('#startDate').val() != '' ) {
			$('.dateBtn').css('display','inline-block');
		}
		if($('#lowPrice').val() != '' ) {
			$('.priceBtn').css('display','inline-block');
		}
	}
	
	var rangeSetDate = function() {
		let stDate =$("#startDate").val();
		let enDate =$("#endDate").val();
		
		stDate = stDate == '' ? 0 : stDate; 
		enDate = enDate == '' ? 10 : enDate;
		  
		$("span.rangeDate").html(stDate +"~"+ enDate + "개월");
		
		$( "#filterDate" ).slider({
			range: true,
			min: 0, 
			max: 10,
			values: [ stDate, enDate ],
			slide : function(event , ui) {
				let startDate =  $("#startDate").val(ui.values[0]);
				let endDate = $("#endDate").val( ui.values[1])
				$("span.rangeDate").html(startDate.val() +"~"+ endDate.val() + "개월");
				if (startDate[0].defaultValue == '0' && endDate[0].defaultValue == '10') {
					$('.dateBtn').hide();
					$("#startDate").val('');
					$("#endDate").val('');
				}  else {
					$('.dateBtn').show();
				}
			}
		});
	}
	var rangeSetPrice = function() {
		let loPrice = $('#lowPrice').val();
		let hiPrice = $('#highPrice').val();
		
		loPrice = loPrice == '' ? 0 : loPrice; 
		hiPrice = hiPrice == '' ? 1000 : hiPrice; 
		$("span.rangePrice").html(loPrice +"~"+ hiPrice + "만원");
		
		$( "#filterPrice" ).slider({
			range: true,
			min: 0,
			max: 1000,
			values: [ loPrice, hiPrice ], 
			slide : function(event , ui) {
				const startPrice = $("#lowPrice").val(ui.values[0]);
				const endPrice = $("#highPrice").val(ui.values[1])
				$("span.rangePrice").html(startPrice.val() +"~"+ endPrice.val() + "만원");
				if (startPrice[0].defaultValue == '0' && endPrice[0].defaultValue == '1000') {
					$('.priceBtn').hide();
					$("#highPrice").val('');
					$("#lowPrice").val('');
				} else {
					$('.priceBtn').show();
				}
			}
		});
	}

    var namespace = win.auction;
    var __COMPONENT_NAME = "RESULT_LIST";
    namespace[__COMPONENT_NAME] = (function () {
        var init = function(){
            setLayout();
            setBinding();
            webApp();
        	rangeSetDate();
			rangeSetPrice();
			filterDelBtnView();
        };
        return {
            init: init
        }
    })();
    $(function () {
        namespace[__COMPONENT_NAME].init();
    });
})(jQuery, window, document);

var fnSearch = function(){	
	var temp = new Map();
	temp.set("1", ["1", "4", "6"]);
	temp.set("2", ["0", "2", "5", "9"]);
	temp.set("3", ["3"]);
	
	var chkArr = [];
	$("input[name=indvSexC]:checked").each(function(){
	    chkArr.push(...temp.get($(this).val()));
	});
	
	var form = $('<form></form>');
	form.attr('action', '/results'+window.location.search);
	form.attr('method', 'post');
	form.appendTo('body');			
	
	form.append($("<input type='hidden' value="+$("#searchAucObjDsc").val()+" name='searchAucObjDsc'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchType]').val()+" name='searchType'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchOrder]').val()+" name='searchOrder'>"));
	form.append($("<input type='hidden' value="+$('select[name=searchDate]').val()+" name='searchDate'>"));
	form.append($("<input type='hidden' value="+ chkArr.join(",") +" name='indvSexC'>"));
	form.append($("<input type='hidden' value="+ $(".searchTxt").val() +" name='searchTxt'>"));
	form.append($("<input type='hidden' value="+ $("#startDate").val() +" name='startDate'>"));
	form.append($("<input type='hidden' value="+ $("#endDate").val() +" name='endDate'>"));
	form.append($("<input type='hidden' value="+ $("#lowPrice").val() +" name='lowPrice'>"));
	form.append($("<input type='hidden' value="+ $("#highPrice").val() +" name='highPrice'>"));
	form.append($("<input type='hidden' value="+ $("#birthUpDown").val() +" name='birthUpDown'>"));
	form.append($("<input type='hidden' value="+ $("#priceUpDown").val() +" name='priceUpDown'>"));
	form.append($("<input type='hidden' value="+ $("#numUpDown").val() +" name='numUpDown'>"));
	form.append($("<input type='hidden' value="+ $("#upDownFlag").val() +" name='upDownFlag'>"));
	
	form.submit();
}
