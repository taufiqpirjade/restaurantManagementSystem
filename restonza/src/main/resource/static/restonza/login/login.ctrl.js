angular.module('restonza').controller('LoginController', function ($scope,Auth) {
	$scope.doAuthenticate = function(username,password) {
		Auth.doAuthenticate(username,password);
	};
	 $("#divLoading").removeClass('show');
});