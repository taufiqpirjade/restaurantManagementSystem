angular.module('restonza').controller('HotelAnalysisController', function ($scope,$filter, $http,RESTONZACONSTANTS/* $scope, $location, $http */) {
	//expense generator
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var updatejson = {};
	updatejson["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getTotalEarningReport"; 
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		var chart = AmCharts.makeChart("chartdiv", {
		    "theme": "light",
		    "type": "serial",
			"startDuration": 2,
		    "dataProvider": data.response,
		    "valueAxes": [{
		        "position": "left",
		        "title": "Total Earning"
		    }],
		    "graphs": [{
		        "balloonText": "[[category]]: <b>[[value]]</b>",
		        "fillColorsField": "colorcode",
		        "fillAlphas": 1,
		        "lineAlpha": 0.1,
		        "type": "column",
		        "valueField": "total_earnings"
		    }],
		    "depth3D": 20,
			"angle": 30,
		    "chartCursor": {
		        "categoryBalloonEnabled": false,
		        "cursorAlpha": 0,
		        "zoomable": false
		    },
		    "categoryField": "month",
		    "categoryAxis": {
		        "gridPosition": "start",
		        "labelRotation": 0
		    },
		    "export": {
		    	"enabled": true
		     }

		});
    }).error(function(data, status, headers, config) {
	});
	
	//trends report
	var url = RESTONZACONSTANTS.url + "/trendingdishesgenerator"; 
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.dishlist = data.response;
    }).error(function(data, status, headers, config) {
    	$scope.dishlist = "";
	});
});