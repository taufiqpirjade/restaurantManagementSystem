angular.module('restonza').controller('QRCodeManagement', function($scope, $route, $http, $filter, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var updatejson = {};
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	updatejson["hotel_id"] = sessionVO.hotel_id;
	 $("#divLoading").addClass('show');
	var url = RESTONZACONSTANTS.url + "/getQRCodes";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.qrcodes = data.response;
		 $("#divLoading").removeClass('show');
	}).error(function(data, status, headers, config) {
		$scope.qrcodes = "";
		$("#divLoading").removeClass('show');
	});		
	
	$scope.printAll = function() {
		 var options = {mode:"popup",popHt: 500,   popWd: 400, popX: 500,   popY: 600,popTitle:"This is Printed with Restonza",popClose: false};
	      $("div.PrintArea").printArea( options );
	};
	
	$scope.printQRCode = function(index) {
		$id = "#" + index;
		 var options = {mode:"popup",popHt: 500,   popWd: 400, popX: 500,   popY: 600,popTitle:"This is Printed with Restonza",popClose: false};
	      $($id).printArea( options );
	};
});