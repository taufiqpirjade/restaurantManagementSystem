angular.module('restonza').controller('CustomHotelAnalysisController', function ($scope,$filter, $http,RESTONZACONSTANTS/* $scope, $location, $http */) {
	//expense generator
	$scope.flag = false;
	var url = RESTONZACONSTANTS.url + "/getActiveHotelsDetails";
	var json = {};
	 $http.post(url, json).success(function(data, status, headers, config) {
		 var hotelnos = [];
		 $scope.hotelList = data.response;
		  for (var i = 0; i < data.response.length; i++) {
			  hotelnos.push(data.response[i].id);
		  }
		  $scope.hotelno = hotelnos;
		  $("#hotelno").select2({
			  data: $scope.hotelno
			});
	   }).error(function(data, status, headers, config) {
	   	$scope.hotelno = "";
	  });
	//hotel number change event
	 $scope.getHotelAnalysis = function(){
		$scope.flag = false;
		var selectedhotel = $("#hotelno").val();
		for (var int = 0; int < $scope.hotelList.length; int++) {
			if ($scope.hotelList[int].id == selectedhotel) {
				$scope.hotel_name = $scope.hotelList[int].hotel_name;
			}
		}
		
		$http.post(url, json).success(function(data, status, headers, config) {
			//success 
			$scope.flag = true;
			var updatejson = {};
			updatejson["hotel_id"] = selectedhotel;
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
	  	}).error(function(data, status, headers, config) {
	  	  $.bootstrapGrowl("Something went wrong please call administrator" ,{
	             type: 'danger',
	             delay: 2000,
	             offset: {
	            	 from: "top",
	            	 amount: 0
	             },
	             align: "center",
	             allow_dismiss: false
	         });
	  	});
	 };
});