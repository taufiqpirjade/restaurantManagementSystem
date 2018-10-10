angular.module('restonza').controller('HomeController', function ($scope, $http, RESTONZACONSTANTS) {
	$scope.loginCheck = function(){
		var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
		if (sessionVO != null) {
			return false;
		}
		return true;
	};
	
	$scope.getRoleCheck = function(){
		var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
		var orderCount = sessionStorage.getItem("ordercounter");
		if (sessionVO != null && sessionVO.role === 'superadmin' && orderCount != null) {
			$scope.ordercounter = orderCount;
			return true;
		}
		return false;
	};
	
	$scope.getUpdateOrderCount = function() {
		var json = {};
		var url = RESTONZACONSTANTS.url + "/getTotalOrderCount";
		  $http.post(url, json).success(function(data, status, headers, config) {
			  sessionStorage.setItem("ordercounter", data.response);
			  $scope.ordercounter = data.response;
		  }).error(function(data, status, headers, config) {
			  $scope.ordercounter = "";
		  });
	};

});