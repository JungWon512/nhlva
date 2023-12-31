function d3_korea_map(_mapContainerId, _spots){
	
	console.log("containerId : ", _mapContainerId, ", spots : ", _spots);
    var WIDTH, HEIGHT,
        CENTERED,
        MAP_CONTAINER_ID = _mapContainerId,
        KOREA_PROVINCE_OBJECT = 'kor';
 
    var SPECIAL_CITIES = ['서울특별시', '인천광역시', '대전광역시', '대구광역시', '부산광역시', '울산광역시', '광주광역시', '세종특별자치시', '제주특별자치도'];
    var projection, path, svg,
        geoJson, features, bounds, center,
        map, places;
 
    function create(callback){
        HEIGHT = document.getElementById(MAP_CONTAINER_ID.replace('#', '')).offsetHeight;
        WIDTH = document.getElementById(MAP_CONTAINER_ID.replace('#', '')).offsetWidth;
 
        console.log('Map scale', {'height': HEIGHT, 'width': WIDTH});
 
        projection = d3.geoMercator().translate([WIDTH / 2, HEIGHT / 2]);
        path = d3.geoPath().projection( projection);
 
        svg = d3.select(MAP_CONTAINER_ID).append("svg")
            .attr("width", WIDTH)
            .attr("height", HEIGHT);
 
        map = svg.append( "g").attr( "id", "map"),
        places = svg.append( "g").attr( "id", "places");
 
        d3.json(KOREA_JSON_DATA_URL).then(function(_data){
            geoJson = topojson.feature(_data, _data.objects[KOREA_PROVINCE_OBJECT]);
            features = geoJson.features;
 
            bounds = d3.geoBounds(geoJson);
            center = d3.geoCentroid(geoJson);
 
            var distance = d3.geoDistance(bounds[0], bounds[1]);
            var scale = HEIGHT / distance / Math.sqrt(2) * 1.2;
 
            projection.scale(scale).center(center);
 
            console.log("center : ", center, " scale : ", scale);
 
            map.selectAll("path")
                .data(features)
                .enter().append( "path")
                .attr("class", function(d) {return "municipality c " + d.properties.code;})
                .attr("d", path)
                .on("click", province_clicked_event);
 
            if(callback) callback(_spots);
        });
    }
 
    function spotting_on_map(){
        var circles = map.selectAll("circle")
            .data(_spots).enter()
            .append("circle")
            .attr("class", "spot")
            .attr("cx", function(d){ return projection( [d.lon, d.lat])[0]; })
            .attr("cy", function(d){ return projection( [d.lon, d.lat])[1]; })
            .attr("r", function(d){ return d.cnt >= 10 ? "10px" : "5px"; })
            .attr("fill", function(d){ return d.cnt >= 10 ? "black" : "#00ffff"; })
            .text("1111")
            .on('click', spot_clicked_event)
            .transition()
            .ease(d3.easeElastic);
    }
 
    function spot_clicked_event(d){
        alert(d['tag']);
    }
 
    function province_clicked_event(d){
        var x, y, zoomLevel;
        if (d && CENTERED != d){
            var centroid = path.centroid( d);
            x = centroid[0];
            y = centroid[1];
            if (d.properties.name == '제주특별자치도' || d.properties.name == '인천광역시')
                zoomLevel = 10;
            else if (SPECIAL_CITIES.indexOf( d.properties.name) != -1)
                zoomLevel = 15;
            else
                zoomLevel = 3;
            CENTERED = d;
            console.log('centered', CENTERED);
        } else {
            x = WIDTH / 2;
            y = HEIGHT / 2;
            zoomLevel = 1;
            CENTERED = null;
        }
 
        map.selectAll("path")
            .classed("active", CENTERED && function(d) { return d === CENTERED;});
 
        map.transition()
            .duration(750)
            .attr("transform", "translate(" + WIDTH / 2 + "," + HEIGHT / 2 + ")scale(" + zoomLevel + ")translate(" + -x + "," + -y + ")")
            .style("stroke-width", 1.5 / zoomLevel + "px");
    }
 
    create(function(){
        spotting_on_map();
    });
}