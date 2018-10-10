angular.module('restonza').controller('FeedbackController', function ($scope,$filter, $route, $http, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var json = {};
	json["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getFeedBackAnalysis";
	$http.post(url, json).success(function(data, status, headers, config) {
		var chart = AmCharts.makeChart("chartdiv", {
		    "theme": "light",
		    "type": "serial",
			"startDuration": 2,
		    "dataProvider": data.response.feedbackCounts,
		    "valueAxes": [{
		        "position": "left",
		        "title": "Rating"
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
		
		$scope.feedbacklist = data.response.feedbackComments;
		
		
	}).error(function(data, status, headers, config) {
		
	});
});