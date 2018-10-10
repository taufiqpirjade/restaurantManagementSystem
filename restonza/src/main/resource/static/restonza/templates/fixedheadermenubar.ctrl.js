angular.module('restonza').controller('GetUpdatedDataController', function ($scope,$timeout, $route, $http,growl, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var json = {};
	json["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getUpdatedData";
	
	$scope.reload = function () {
		$http.post(url, json).success(function(data, status, headers, config) {
			// Angular Notifier Events
			
			if(!typeof data.response.WAITER  == "undefined" || data.response.WAITER > 0){
				growl.warning('Waiter Call !!',{title: 'Waiter call at Table No:-'+data.response.WAITER});
				$scope.playAudio();
				
			}

			if(!typeof data.response.WATER  == "undefined" || data.response.WATER > 0){
				growl.info('Water Call !!.',{title: 'Water call at Table :-'+data.response.WATER});
				$scope.playAudio();
			}
			
			if (!typeof data.response.new_order  == "undefined" || data.response.new_order > 0) {
				$scope.new_order = data.response.new_order;
				$scope.playAudio();
			} else {
				$('#neworder').removeAttr("data-badge");
			}
			
			if (!typeof data.response.confirm_order  == "undefined" || data.response.confirm_order > 0) {
				if (!typeof data.response.ready_order  == "undefined" || data.response.ready_order > 0) {
					$scope.confirm_order = data.response.confirm_order + data.response.ready_order;
				} else {
					$scope.confirm_order = data.response.confirm_order;
				}
			} else {
				if (!typeof data.response.ready_order  == "undefined" || data.response.ready_order > 0) {
					$scope.confirm_order = data.response.ready_order;
				} else {
					$('#confirmedorder').removeAttr("data-badge");
				}
			} 
			
			if (!typeof data.response.delivered_order  == "undefined" || data.response.delivered_order > 0) {
				$scope.billed = data.response.delivered_order;
			} else {
				$('#genratebill').removeAttr("data-badge");
			}  
   	  	});
		$scope.playAudio = function() {
        var audio = new Audio('bower_components/img/beep-05.mp3');
        audio.play();
        };
	    $timeout(function(){
	      $scope.reload();
	    },30000)
	  };
	  $scope.reload();
});



/*$scope.reload = function () {
    $http.get('http://localhost:3000/api/todos').
        success(function (data) {
          $scope.todos = data.todos;
      });

    $timeout(function(){
      $scope.reload();
    },30000)
  };
  $scope.reload();
  
  http://stackoverflow.com/questions/33273054/angularjs-auto-reload-when-backend-change
  *
  */