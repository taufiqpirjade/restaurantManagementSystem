angular.module('restonza').controller('PlacedOrderController',function ($scope, $timeout,$http,$filter, $route, RESTONZACONSTANTS,PrintService) {
	$("#divLoading").removeClass('show');
	var json={};
	  $scope.showModal = false;
	  $scope.single_record = {};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  $scope.reload = function () {
			  json['hotel_id'] = sessionVO.hotel_id;
			  json['status'] = "new_order";
			  var url = RESTONZACONSTANTS.url + "/getOrderDetailsForTable";
			  // Fetch table details call
			  $http.post(url, json).success(function(data, status, headers, config) {
				  var records = [];
				  $scope.orders = data.response;
			  }).error(function(data, status, headers, config) {
			      $scope.orders = [];
			  });
	  
	  $timeout(function(){
	      $scope.reload();
	    },5000)
	  };
	  $scope.reload();
	  //Added getDishes call printing 22/07/2017-------------------------------------------------------------------
	  var updatejson = {};
	  $scope.single_record = {};
	  updatejson["hotel"] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getDishes";
	  $http.post(url, updatejson).success(function(data, status, headers, config) {
	  	  $(data.response).each(function(idx, obj){
	  			$scope.single_record[obj.dish_name] = obj.dish_price;
	  	    });
	  	});
	//end Dishes call printing 22/07/2017-------------------------------------------------------------------
	  
	    $scope.confirmOrder = function(index) {
	    	$scope.callChangeStatus(index, 'confirm_order');
	    };
	      
	      $scope.callChangeStatus = function(index, status) {
	    	  var json={};
			  json['hotel_id'] =sessionVO.hotel_id;
			  json['order_id'] = $scope.orders[index].id;
			  json['status'] = status;
			  var url = RESTONZACONSTANTS.url + "/updateOrderStatus";
			  $http.post(url, json).success(function(data, status, headers, config) {
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
			  });
	      
			
		};
	      $scope.cancelOrder = function(index) {
	    	  $scope.callChangeStatus(index, 'cancel_order');
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
			    		'index' : index,
			    		"orderid": selectedOrder.id,
			    		"dishDetails": $scope.dishDetails,
			    		"tableno": selectedOrder.table_id,
			    }
			    
			    if (selectedOrder.instruction !='') {
					$scope.instruction = selectedOrder.instruction;
				}
			    $scope.showModal = true;
		  };
		  
		  $scope.resetAll = function() {
				$scope.showModal = false;
				$scope.dishDetails = [];
				$scope.editdata = {};
			};
			
			/*$scope.printKOT = function() {
				var options = {mode:"popup",popHt: 500,   popWd: 400, popX: 1000, popY: 1000, popTitle:"Restonza Hotel Management App: Bill Printing", popClose: false};
			    $('#billorderkot').printArea( options );
			};*/
			
			//fetch tax details---------------------------------------------------------------------------
			var taxjson = {};
			taxjson['hotel_id'] = sessionVO.hotel_id;
			  var url = RESTONZACONSTANTS.url + "/getTaxDetails";
			  // Fetch table details call
			  $http.post(url, taxjson).success(function(data, status, headers, config) {
				  $scope.taxes = data.response.taxes;
			  }).error(function(data, status, headers, config) {
				  $scope.taxes = 0;
			  });
			  
			//print kot functionality modification 22/07/2017
		    $scope.printBill = function(index) {
		    		$scope.printdata = {};
		    		$scope.printdishDetails = [];
				    var selectedOrder = $scope.orders[index];
				    $.each(selectedOrder.order_summary.split(/\|/), function (i, val) {
				    	var json = {};
				    	if (val != "") {
				    		var innerVal = val.split(",");
				        	json["dishname"] = (innerVal[0]).split("=")[1];
				        	json["qty"] = (innerVal[1]).split("=")[1];
				        	json["price"] = $scope.single_record[(innerVal[0]).split("=")[1]];
				        	$scope.printdishDetails.push(json);
						}
				    });
				    var amount = parseInt(selectedOrder.amount == "" ? "0" : selectedOrder.amount);
				    //tax calculation
				    var totaltax = 0;
				    angular.forEach($scope.taxes, function(value, key){
				    	totaltax = totaltax + value;
				        console.log(key + ': ' + value);
				   });
				    var finalamount = (((totaltax) + 100)*amount)/100;
				    $scope.printdata = {
				    		"hotelid" : sessionVO.hotel_id,
				    		"tableid": selectedOrder.table_id, 
				    		"orderid": selectedOrder.id,
				    		"dishDetails": $scope.printdishDetails,
				    		"amt" : finalamount,
				    		"taxes": $scope.taxes,
				    }
				    PrintService.doPrintJob($scope.printdata);
			  };
});


