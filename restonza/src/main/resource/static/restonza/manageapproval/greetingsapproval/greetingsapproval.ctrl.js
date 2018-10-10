angular.module('restonza').controller('GreetingsApprovalManagementController', function ($scope,$filter,$route,$http,RESTONZACONSTANTS) {

	var updatejson = {};
	var url = RESTONZACONSTANTS.url + "/getApprovalGreetings";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.greetings = data.response;
    }).error(function(data, status, headers, config) {
    	$scope.greetings = "";
	});
	
	$scope.approveGreeting = function(id) {
		var updatejson = {};
		updatejson["id"] = id;
		var url = RESTONZACONSTANTS.url + "/approveGreeting";
		$http.post(url, updatejson).success(function(data, status, headers, config) {
			 $route.reload();
	   		 $.bootstrapGrowl(data.response ,{
		             type: 'success',
		             delay: 2000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
	    }).error(function(data, status, headers, config) {
	   		 $.bootstrapGrowl(data.response ,{
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
	}
});