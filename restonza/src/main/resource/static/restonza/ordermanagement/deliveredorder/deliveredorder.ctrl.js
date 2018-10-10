angular.module('restonza').controller('DeliveredController',function ($scope, $route, $http, $filter, RESTONZACONSTANTS) {
	  var json={};
	  $scope.showModal = false;
	  $scope.single_record = {};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  json['status'] = "ready_order";
	  var url = RESTONZACONSTANTS.url + "/getOrderDetailsForTable";
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.orders = data.response;
	  }).error(function(data, status, headers, config) {
	      $scope.orders = "";
	  });
	  
	      $scope.viewOrder = function(index) {
			  $scope.resetAll();
			    var selectedOrder = $scope.orders[index];
			    $.each(selectedOrder.order_summary.split(/\|/), function (i, val) {
			    	var json = {};
			    	if (val != "") {
			    		var innerVal = val.split(",");
			        	json["name"] = (innerVal[0]).split("=")[1];
			        	json["qty"] = (innerVal[1]).split("=")[1];
			        	json["selected"] = true;
			        	json["dish_price"] = $scope.single_record[(innerVal[0]).split("=")[1]];
			        	$scope.dishDetails.push(json);
					}
			    });
			    $scope.editdata = {
			    		"orderid": selectedOrder.id,
			    		"dishDetails": $scope.dishDetails,
			    }
			    $scope.showModal = true;
		  };
		  
		  $scope.resetAll = function() {
				$scope.showModal = false;
				$scope.dishDetails = [];
				$scope.editdata = {};
			};
			
			$scope.deliverOrder = function(index) {
		    	$scope.callChangeStatus(index, 'delivered_order');
		    };
		      
		      $scope.callChangeStatus = function(index, status) {
		    	  var json={};
				  json['hotel_id'] =sessionVO.hotel_id;
				  json['order_id'] = $scope.orders[index].id;
				  json['status'] = status;
				  var url = RESTONZACONSTANTS.url + "/updateOrderStatus";
				  $http.post(url, json).success(function(data, status, headers, config) {
					  $("#divLoading").removeClass('show');
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
					  $("#divLoading").removeClass('show');
				  });
			};
	    
});


