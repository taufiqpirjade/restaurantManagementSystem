angular.module('restonza').controller('BillBarOrderManagement',function ($scope, $http,$filter,$route, RESTONZACONSTANTS, PrintService) {
	$("#divLoading").removeClass('show');  
	var json={};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getBarOrderBilling/" + sessionVO.hotel_id;
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  $scope.orders = data.response;
		  $("#divLoading").removeClass('show');
	  }).error(function(data, status, headers, config) {
		  $scope.orders = [];
		  $("#divLoading").removeClass('show');
	  });
});