angular.module('restonza').controller('PlacedBarOrderController',function ($scope, $timeout,$http,$filter, growl, $route, RESTONZACONSTANTS,PrintService) {
	$("#divLoading").removeClass('show');
	var json={};
	  $scope.showModal = false;
	  $scope.single_record = {};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  $scope.reload = function () {
			  var url = RESTONZACONSTANTS.url + "/getBarOrder/" + sessionVO.hotel_id + "/new_order";
			  // Fetch table details call
			  $http.post(url, json).success(function(data, status, headers, config) {
				  var records = [];
				  $scope.orders = data.response;
				  if(data.response.length > 0) {
						growl.info('New Bar order!!',{title: ''});
						$scope.playAudio();
				  }
			  }).error(function(data, status, headers, config) {
			      $scope.orders = [];
			  });
	  
	  $timeout(function(){
	      $scope.reload();
	    },30000)
	  };
	  
	  $scope.playAudio = function() {
	        var audio = new Audio('bower_components/img/beep-05.mp3');
	        audio.play();
	        };
	        
	  $scope.reload();
	  //Added getDishes call printing 22/07/2017-------------------------------------------------------------------
	  var updatejson = {};
	  var url = RESTONZACONSTANTS.url + "/getHotelItems/" + sessionVO.hotel_id;
	  $http.post(url, updatejson).success(function(data, status, headers, config) {
	  	  $(data.response).each(function(idx, obj){
	  			$scope.single_record[obj.dish_name] = obj.price;
	  	    });
	  	});
	//end Dishes call printing 22/07/2017-------------------------------------------------------------------
	  
	    $scope.confirmOrder = function(index) {
	    	$scope.callChangeStatus(index, 'confirm_order');
	    };
	      
	      $scope.callChangeStatus = function(index, status) {
	    	  var json={};
			  json['hotel_id'] =sessionVO.hotel_id;
			  json['parent_order_id'] = $scope.orders[index].id;
			  json['old_status'] = 'new_order';
			  json['new_status'] = status;
			  var url = RESTONZACONSTANTS.url + "/updateBarOrderStatus";
			  $http.post(url, json).success(function(data, status, headers, config) {
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
	      $scope.cancelOrder = function(index) {
	    	  $scope.callChangeStatus(index, 'cancel_order');
	      };
	      
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
			
			//fetch tax details---------------------------------------------------------------------------
			var taxjson = {};
			taxjson['hotel_id'] = sessionVO.hotel_id;
			  var url = RESTONZACONSTANTS.url + "/getTaxDetails";
			  // Fetch table details call
			  $http.post(url, taxjson).success(function(data, status, headers, config) {
				  $scope.taxes = data.response.ltaxes;
			  }).error(function(data, status, headers, config) {
				  $scope.taxes = 0;
			  });
			  
			//print kot functionality modification 22/07/2017
		    $scope.printBill = function(index) {
		    		$scope.printdata = {};
		    		$scope.printdishDetails = [];
				    var selectedOrder = $scope.orders[index];
				    $.each(selectedOrder.orderdetails.split(","), function (i, val) {
				    	var json = {};
				    	if (val != "") {
				    		var innerVal = val.split("-");
				        	json["dishname"] = (innerVal[0]);
				        	json["qty"] = (innerVal[1]);
				        	json["price"] = $scope.single_record[(innerVal[0])];
				        	$scope.printdishDetails.push(json);
						}
				    });
				    var amount = parseInt(selectedOrder.sum == "" ? "0" : selectedOrder.sum);
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


