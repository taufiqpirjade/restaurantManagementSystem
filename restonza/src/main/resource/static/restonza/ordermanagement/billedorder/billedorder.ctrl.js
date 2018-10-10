angular.module('restonza').controller('BilledController',function ($scope, $http,$filter, RESTONZACONSTANTS) {
	  var json={};
	  $scope.showModal = false;
	  $scope.single_record = {};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  json['status'] = "billed";
	  var url = RESTONZACONSTANTS.url + "/getOrderDetailsForTable";
	  $("#divLoading").addClass('show');
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.orders = data.response;
		  $scope.removeOrder = function(index) {
		        $scope.orders.splice(index, 1);
		  };
		  $("#divLoading").removeClass('show');
	  }).error(function(data, status, headers, config) {
	      $scope.orders = "";
	      $("#divLoading").removeClass('show');
	  });
	  
	  $scope.statuses = [
	                     {value: 'billed', text: 'Billed'},
	                   ];
	  $scope.showStatus = function(order) {
	      var selected = [];
	      if(order.status) {
	        selected = $filter('filter')($scope.statuses, {value: order.status});
	      }
	      return selected.length ? selected[0].text : 'Not set';
	    };
	    
	    $scope.save = function(rowform,order) {
	    	  var modifiedorders_status = rowform.$data.status;
	    	  var json={};
			  json['hotel_id'] =sessionVO.hotel_id;
			  json['order_id'] = order.id;
			  json['status'] = modifiedorders_status;
			  var url = RESTONZACONSTANTS.url + "/updateOrderStatus";
			  $http.post(url, json).success(function(data, status, headers, config) {
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
			  });
	      };
	      
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
});


