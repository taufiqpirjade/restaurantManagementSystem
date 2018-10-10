angular.module('restonza').controller('AllCustomersController', function($scope, $route, $http, $filter, RESTONZACONSTANTS /* $scope, $location, $http */) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var url = RESTONZACONSTANTS.url + "/getAllCustomerDetails";
	 $("#divLoading").addClass('show');
	$http.post(url, "").success(function(data, status, headers, config) {
		$scope.customerList = data.response;
		$("#divLoading").removeClass('show');
	}).error(function(data, status, headers, config) {
		$("#divLoading").removeClass('show');
	});		
	
	$scope.exportCustomerDetails = function() {
		var name = "Customer Details"+(new Date()).getDate()+"/"+((new Date()).getMonth()+1);
		$('#CustomerData').tableExport({type:'excel',fileName: name});
	};
});