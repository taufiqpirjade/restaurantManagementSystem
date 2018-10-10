angular.module('restonza').controller('MainController', function ($rootScope,Auth,$location, $scope) {
	$rootScope.role = Auth.getRole();

	$scope.logoutlink = function() {
		$rootScope.role = '';
		sessionStorage.removeItem("sessionVO");
		sessionStorage.removeItem("ordercounter");
		 $("#divLoading").addClass('show');
		$location.url('/logout');
	};
});