angular.module('restonza').controller('OrderManagementController',function ($scope, $http,$filter, RESTONZACONSTANTS) {
	  var json={};
	  $scope.showModal = false;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getOrderDetailsForTable";
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.orders = data.response;
		  $scope.removeOrder = function(index) {
		        $scope.orders.splice(index, 1);
		  };
	  }).error(function(data, status, headers, config) {
	      $scope.table_count = "";
	  });
	  
	  $scope.statuses = [
	                     {value: 'new_order', text: 'New Order'},
	                     {value: 'delivered_order', text: 'Delivered Order'},
	                     {value: 'ready_order', text: 'Ready Order'},
	                     {value: 'confirm_order', text: 'Confirm Order'},
	                     {value: 'cancel_order', text: 'Cancel Order'},
	                     {value: 'delivered', text: 'Delivered'},
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
	      }
});


