(function() {
	'use strict';
	angular.module('restonza').controller('SidebarController',
			function($scope) {

				$scope.state = false;

				$scope.toggleState = function() {
					$scope.state = !$scope.state;
				};
				
				$scope.closeNav = function() {
					$('#mySidenav').css("width", "0");
					$('#main').css("margin-left", "0");
//					$('#view').css("margin-left", "0");
				};
				
				
				$scope.openNav = function() {
					$('#mySidenav').css("width", "250px");
					$('#main').css("margin-left", "150px");
//					$('#view').css("margin-left", "150px");
				};

			});

}());