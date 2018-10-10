angular.module('restonza').controller('PendingOrderManagement',function ($scope,$timeout, $route, $http,$filter, RESTONZACONSTANTS) {
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
	  
	  $scope.reload = function () {
		  json['hotel_id'] = sessionVO.hotel_id;
		  var url = RESTONZACONSTANTS.url + "/getPendingOrders/v2";
		  $("#divLoading").addClass('show');
		  // Fetch table details call
		  $http.post(url, json).success(function(data, status, headers, config) {
			  $("#divLoading").removeClass('show');
			  if (data.status == 'success') {
				  $scope.orders = data.response;
			} else {
				$scope.orders=[];
			}
		  }).error(function(data, status, headers, config) {
			  $scope.orders=[];
		  });
		  
	  $timeout(function(){
	      $scope.reload();
	    },10000)
	  };
	  $scope.reload();
	  
	  $scope.viewOrder = function(index) {
		  $scope.resetAll();
		    var selectedOrder = $scope.orders[index];
		    $.each(selectedOrder.order_details.split(","), function (i, val) {
		    	var json = {};
		    	if (val != "") {
		    		var innerVal = val.split("-");
		    		json["id"] = (innerVal[0])
		        	json["name"] = (innerVal[1]);
		        	json["qty"] = (innerVal[2]);
		        	json["selected"] = true;
		        	json["dish_price"] = $scope.single_record[(innerVal[1])];
		        	$scope.dishDetails.push(json);
				}
		    });
		    $scope.editdata = {
		    		"orderid": selectedOrder.orderid,
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
		  json['order_id'] = orderid
		  var url = RESTONZACONSTANTS.url + "/markReady";
		  // Fetch table details call
		  $http.post(url, json).success(function(data, status, headers, config) {
			  $("#divLoading").removeClass('show');
			  console.log("success");
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
	  
	  $scope.markReadyv2 = function(foodOrderId, dishId) {
		  $scope.showModal = false;
		  $("#divLoading").addClass('show');
		  var json= {};
		  json['hotel_id'] = foodOrderId;
		  json['order_id'] = dishId
		  var url = RESTONZACONSTANTS.url + "/markReady/v2";
		  // Fetch table details call
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
	  
	  $scope.resetAll = function() {
			$scope.showModal = false;
			$scope.dishDetails = [];
			$scope.editdata = {};
		};
});


