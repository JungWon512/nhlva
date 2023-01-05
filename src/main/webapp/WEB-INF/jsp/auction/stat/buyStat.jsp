<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/__system/taglibs.jsp"%>

<script src="//cdnjs.cloudflare.com/ajax/libs/d3/3.5.3/d3.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/topojson/1.6.9/topojson.min.js"></script>
<script src="/static/js/page/chart/datamaps.kor.js"></script>

<form name="frm" method="post" action="">
	<div class="auction_list">
		<input type="hidden" id="johpCdVal" value="${johapData.NA_BZPLCNO}"/>
		<h3>나의 구매현황</h3>
		<div class="list_search">
			<ul class="radio_group">
				<li>
					<input type="radio" name="searchAucObjDsc" id="ra1" value="" ${(params.searchAucObjDsc == null || params.searchAucObjDsc == '' ) ? 'checked':'' }><label for="ra1">전체</label>
				</li>
				<li>
					<input type="radio" name="searchAucObjDsc" id="ra2" value="1" ${params.searchAucObjDsc == 1?'checked':'' }><label for="ra2">송아지</label>
				</li>
				<li>
					<input type="radio" name="searchAucObjDsc" id="ra3" value="2" ${params.searchAucObjDsc == 2 ?'checked':'' }><label for="ra3">비육우</label>
				</li>
				<li>
					<input type="radio" name="searchAucObjDsc" id="ra4" value="3" ${params.searchAucObjDsc == 3 ?'checked':'' }><label for="ra4">번식우</label>
				</li>
			</ul>
			<ul class="sch_area">
				<li class="date">
					<input type="text" class="calendar" name="searchStDt" value="${params.searchStDt}" readonly="readonly" />
				</li>
				<li class="date">
					<input type="text" class="calendar" name="searchEnDt" value="${params.searchEnDt}" readonly="readonly" />
				</li>
				<li class="btn">
					<a href="javascript:;" class="list_sch">검색</a>
				</li>
			</ul>
		</div>
		<!-- //list_downs e -->
		<div class="list_table auction_result">
			<div id="korea" style="position: relative;min-height:700px;"></div>
		</div>
	</div>
</form>
<!-- //auction_list e -->

<script type="text/javascript">
var KOREA_JSON_DATA_URL = '/static/js/page/chart/json/kor.topo.json';

var bubble_map = new Datamap({
	element: document.getElementById('korea'),
	scope: 'kor',
	geographyConfig: {
		popupOnHover: true,
		highlightOnHover: true,
		borderColor: '#444',
		borderWidth: 0.5,
		dataUrl: KOREA_JSON_DATA_URL
	},
	fills: {
		'MAJOR': '#306596',
		'MEDIUM': '#0fa0fa',
		'MINOR': '#bada55',
		defaultFill: '#dddddd'
	},
	setProjection: function (element) {
		var HEIGHT = element.offsetHeight;
		var WIDTH = element.offsetWidth;
		var projection = d3.geo.mercator()
						   .translate([WIDTH / 2, HEIGHT / 2])
						   .center([127.8097284926632, 36.35480897006954])
						   .scale(5000);
		var path = d3.geo.path().projection(projection);
		return { path: path, projection: projection };
	}
});

var bubbles = [];
<c:forEach items="${list}" var="vo">
	bubbles.push({
		fillKey: "${vo.N_CNT > 50 ? 'MAJOR' : vo.N_CNT > 20 ? 'MEDIUM' : 'MINOR'}",
		radius: "${vo.N_CNT > 50 ? '20' : vo.N_CNT > 20 ? '15' : '10'}",
		state: "${vo.CLNTNM}",
		latitude: "${vo.LAT}",
		longitude: "${vo.LNG}",
		text: "${vo.N_CNT}"
	});
</c:forEach>

var legends = {labels : {
							"MAJOR" : "50두 이상",
							"MEDIUM" : "20두 이상 50두 미만",
							"MINOR" : "20두 미만"
						},
				radius : 10,
				spacing : 30,
				textOffset : 14,
				bgRectWidth : 240
			  };
// var bubbles = [
// 	{
// 		fillKey: "MAJOR",
// 		radius: 20,
// 		state: "화순축협",
// 		latitude: 35.0442757,
// 		longitude: 126.995257,
// 		text: 80
// 	},
// 	{
// 		fillKey: "MEDIUM",
// 		radius: 15,
// 		state: "하동축협",
// 		latitude: 34.9993643,
// 		longitude: 127.804337,
// 		text: 40
// 	},
// 	{
// 		fillKey: "MINOR",
// 		radius: 10,
// 		state: "무진장축협",
// 		latitude: 35.6473926,
// 		longitude: 127.519545,
// 		text: 10
// 	}
// ]
setTimeout(function(){
	bubble_map.bubbles(bubbles, {
		popupTemplate: function (geo, data) {
			return '<div class="hoverinfo">' + data.state + ', 구매횟수: ' + data.text + '두</div>';
		}
	});
	
	bubble_map.legend(legends);
}, 1000);
</script>