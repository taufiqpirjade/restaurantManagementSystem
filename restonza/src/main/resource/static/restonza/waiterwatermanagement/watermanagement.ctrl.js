angular.module('restonza').controller('WaterCallController',function ($scope, $http,$filter, RESTONZACONSTANTS) {
	  var json={};
	  $scope.showModal = false;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['id'] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getWaterTable";
	  $("#divLoading").addClass('show');
	  $http.post(url, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.calls = data.response;
		  $("#divLoading").removeClass('show');
	  }).error(function(data, status, headers, config) {
		  $("#divLoading").removeClass('show');
	      $scope.calls = "";
	  });
	  
	  $scope.approve = function(index, call_id) {
			var json = {};
			json["id"] = call_id;
			var url = RESTONZACONSTANTS.url + "/mobile/approveCall";
			$http.post(url, json).success(function(data, status, headers, config) {
				 $scope.calls.splice(index, 1);
			}).error(function(data, status, headers, config) {
			});
	      };
});


