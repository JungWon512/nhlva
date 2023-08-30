(function ($, win, doc) {
    var COMMONS = win.auction["commons"];
    
    var setLayout = function() {
    };
    
    var setBinding = function() {
		$(".btn-chg").unbind("click").click(function(e){
			e.preventDefault();
			if($(this).hasClass("btn-prev")){
				searchAjax("prev");
			}else{
				searchAjax("next");
			}
		});
    };
    
    var namespace = win.auction;
    var __COMPONENT_NAME = "DASHBOARD_SBID_STATUS"; 
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

var setChart =  function() {
	$(".chart_area").empty();
	$(".chart_area").append('<canvas id="myDoughnutSample1"></canvas>');
	
	const ctx3 = document.getElementById('myDoughnutSample1');
	var sbid_per = document.getElementById('sbid_per').value;
	var non_bid_per = 100 - sbid_per;
	
	new Chart(ctx3, {
		type: 'doughnut',
		data: {
			datasets: [{
				data: [non_bid_per, sbid_per],
				backgroundColor: ['#cac3fa', '#8b7feb'],
				borderWidth: 0
			}]
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
	});
}

var searchAjax = function(flag){
	const params = {
		searchMonth: $("input[name=searchMonth]").val()
		,searchFlag: flag
		,searchPlace:  $("input[name=searchPlace]").val()
	}
	
	$.ajax({
		url: '/dashboard/parti_status_ajax',
		data: JSON.stringify(params),
		type: 'POST',
		dataType: 'json',
		beforeSend: function (xhr) {
			xhr.setRequestHeader("Accept", "application/json");
			xhr.setRequestHeader("Content-Type", "application/json");
		},
		success : function() {
		},
		error: function(xhr, status, error) {
		}
	}).done(function (body) {
		var success = body.success;
		var message = body.message;
		if (!success) {
			modalAlert("", message);
		}
		else {
			$("#searchMonth").val(body.inputParam.searchMonth);
			$("div.tit-area span.cntNaBzplc").text(body.bidderInfo == null ? "0" : body.bidderInfo.CNT_NABZPLC);
			$(".month_txt_title").text(body.inputParam.searchMonTxt);
			$(".mon_txt_block").text(body.inputParam.searchMonTxt + " 기준");
			
			var sbid_per = body.bidderInfo == null ? "0" : body.bidderInfo.SBID_PER
			$("#sbid_per").val(sbid_per);
			$(".sbid_per_red").text(sbid_per + " %");
	
			var tot_sog_cnt = body.bidderInfo == null ? "0" : body.bidderInfo.TOT_SOG_CNT;
			var tot_sbid_cnt = body.bidderInfo == null ? "0" : body.bidderInfo.TOT_SBID_CNT;
			$(".tot_sog_cnt").text(fnSetComma(tot_sog_cnt)+ " 두");
			$(".tot_sbid_cnt").text(fnSetComma(tot_sbid_cnt) + " 두");
			$(".bid_minus_cnt").text("- " + fnSetComma(tot_sog_cnt - tot_sbid_cnt));
			$(".douhnut_txt").html("전체<br/>"+ fnSetComma(tot_sog_cnt) + "두")

			//Next 버튼 처리
			if ($('#nowMonth').val() == body.inputParam.searchMonth) {
				$("button.btn-next").hide();
			} else {
				$("button.btn-next").show();
			}

			//낙찰율 현황 그리기
			var arrHtml = [];
			var bidderPerList = body.bidderPerList;
			$('#bidder_list').empty();
			
			if(bidderPerList != null && bidderPerList.length > 0){
				for(var i = 0; i < bidderPerList.length; i++){
					arrHtml.push("<tr class='"+(bidderPerList[i].NUM_NABZPLC == '1'?'total':'')+"'>");
					if(bidderPerList[i].NUM_NABZPLC == '1') arrHtml.push("	<td rowspan="+bidderPerList[i].CNT_NABZPLC+">" + bidderPerList[i].NA_BZPLCLOC_NM + "</td>");
					arrHtml.push("	<td>" + bidderPerList[i].GUBUN + "</td>");
					arrHtml.push("	<td>" + fnSetComma(bidderPerList[i].TOT_SBID_CNT) + "</td>");
					arrHtml.push("	<td>" + fnSetComma(bidderPerList[i].MINUS_SOG_BID) + "</td>");
					arrHtml.push("	<td>" + bidderPerList[i].SBID_PER + "%</td>");
					arrHtml.push("</tr>");
				}
			}else{
				arrHtml.push("<tr><td colspan='5' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>");	
			}
			
			$("#bidder_list").append(arrHtml.join(""));
		}
		
		setChart();
	});
	
}