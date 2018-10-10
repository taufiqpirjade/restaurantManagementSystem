angular.module('restonza').controller('PendingBarOrderManagement',function ($scope, $route, $http,$filter, RESTONZACONSTANTS) {
	  var json={};
	  $scope.instructionflag = false;
	  $scope.showModal = false;
	  $scope.dishDetails = [];
	  $scope.editdata = {};
	  var updatejson = {};
	  var dishrecords = [];
	  $scope.single_record = {};
	  $scope.showModal = false;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getBarOrder/" + sessionVO.hotel_id + "/confirm_order";
	  $("#divLoading").addClass('show');
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  $("#divLoading").removeClass('show');
			  $scope.orders = data.response;
	  }).error(function(data, status, headers, config) {
		  $scope.orders=[];
	  });
	  
	  $scope.viewOrder = function(index) {
		  $scope.resetAll();
		    var selectedOrder = $scope.orders[index];
		    $.each(selectedOrder.orderdetails.split(","), function (i, val) {
		    	var json = {};
		    	if (val != "") {
		    		var innerVal = val.split("-");
		        	json["name"] = (innerVal[0]);
		        	json["qty"] = (innerVal[1]);
		        	json["selected"] = true;
		        	json["dish_price"] = $scope.single_record[(innerVal[0])];
		        	$scope.dishDetails.push(json);
				}
		    });
		    $scope.editdata = {
		    		"orderid": selectedOrder.id,
		    		"dishDetails": $scope.dishDetails,
		    		"tableno": selectedOrder.table_id,
		    }
		    if (selectedOrder.instruction !='') {
				$scope.instructionflag= true;
				$scope.instruction = selectedOrder.instruction;
			}
		    $scope.showModal = true;
	  };
	
	  $scope.markReady = function(orderid) {
		  $scope.showModal = false;
		  $("#divLoading").addClass('show');
		  var json= {};
		  json['hotel_id'] = sessionVO.hotel_id;
		  json['parent_order_id'] = orderid
		  json['old_status'] = 'confirm_order';
		  json['new_status'] = 'ready_order';
		  var url = RESTONZACONSTANTS.url + "/updateBarOrderStatus";
		  // Fetch table details call
		  $http.post(url, json).success(function(data, status, headers, config) {
			  $("#divLoading").removeClass('show');
			  console.log("success");
	    		 $route.reload();
	    		 $.bootstrapGrowl(data.response ,{
		             type: data.status,
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
			  $.bootstrapGrowl(data.response ,{
		             type: data.status,
		             delay: 2000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
		  });
	  };
	  
	  $scope.resetAll = function() {
			$scope.showModal = false;
			$scope.dishDetails = [];
			$scope.editdata = {};
		};
});


