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
    
    var setChart =  function() {
	}

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

var searchAjax = function(flag){
	const params = {
		searchMonth: $("input[name=searchMonth]").val()
		,searchFlag: flag
		,searchPlace:  $("input[name=searchPlace]").val()
	}
	
	$.ajax({
		url: '/dashboard_parti_status_ajax',
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
			$("#searchMonth").val(body.inputParam.searchMonth)
			$(".month_txt_title").text(body.inputParam.searchMonTxt);
			$(".mon_txt_block").text(body.inputParam.searchMonTxt + " 기준");
			
			console.log(body.bidderInfo)
			var sbid_per = body.bidderInfo == null ? "0" : body.bidderInfo.SBID_PER
			$("#sbid_per").val(sbid_per);
			$(".sbid_per_red").text(sbid_per + " %");
	
			var tot_sog_cnt = body.bidderInfo == null ? "0" : body.bidderInfo.TOT_SOG_CNT;
			var tot_sbid_cnt = body.bidderInfo == null ? "0" : body.bidderInfo.TOT_SBID_CNT;
			$(".tot_sog_cnt").text(tot_sog_cnt+ " 두");
			$(".tot_sbid_cnt").text(tot_sbid_cnt + " 두");
			$(".bid_minus_cnt").text("+ " + (tot_sog_cnt - tot_sbid_cnt));
			$(".douhnut_txt").html("전체<br/>"+ tot_sog_cnt + "두")

			//낙찰율 현황 그리기
			var arrHtml = [];
			var bidderPerList = body.bidderPerList;
			$('#bidder_list').empty();
			
			if(bidderPerList != null && bidderPerList.length > 0){
				for(var i = 0; i < bidderPerList.length; i++){
					arrHtml.push("<tr>");
					arrHtml.push("	<td>" + bidderPerList[i].NA_BZPLCLOC_NM + "</td>");
					arrHtml.push("	<td>" + bidderPerList[i].TOT_SBID_CNT + "</td>");
					arrHtml.push("	<td>" + bidderPerList[i].MINUS_SOG_BID + "</td>");
					arrHtml.push("	<td>" + bidderPerList[i].SBID_PER + "%</td>");
					arrHtml.push("</tr>");
				}
			}else{
				arrHtml.push("<tr><td colspan='4' style='text-align:center;'>조회된 결과가 없습니다.</td></tr>");	
			}
			
			$("#bidder_list").append(arrHtml.join(""));
		}
	});
	
}