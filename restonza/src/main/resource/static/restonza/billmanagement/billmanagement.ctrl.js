angular.module('restonza').controller('BillOrderManagement',function ($scope, $http,$filter,$route, RESTONZACONSTANTS, PrintService) {
	$("#divLoading").removeClass('show');  
	var json={};
	  $scope.showModal = false;
	  $scope.showFeedback = false;
	  $scope.showPrint = false;
	  
	  $scope.printdata = {};
	  $scope.printdishDetails = [];
	  $scope.finalamountPrice = 0;
	  //Added New variables for separate bill changes
	  $scope.finalAmountTemp = 0;
	  $scope.discounMoneyt = 0;
	  $scope.foodDiscount = 0;
	  $scope.barDiscount = 0;
	  $scope.foodBillFinalAmt = 0;
	  $scope.barBillFinalAmt = 0;
	  $scope.foodDiscountMoney = 0;
	  $scope.barDiscountMoney = 0;
	  
	  $scope.finalFoodAmountTemp = 0;
	  $scope.finalBarAmountTemp = 0;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  //Added getDishes call printing 22/07/2017-------------------------------------------------------------------
	  var updatejson = {};
	  $scope.single_record = {};
	  $("#divLoading").addClass('show');
	  var url = RESTONZACONSTANTS.url + "/getHotelItems/" + sessionVO.hotel_id;
	  $http.post(url, updatejson).success(function(data, status, headers, config) {
	  	  $(data.response).each(function(idx, obj){
	  			$scope.single_record[obj.dish_name] = obj.price;
	  	    });
	  	});
	//end Dishes call printing 22/07/2017-------------------------------------------------------------------
	  json['hotel_id'] = sessionVO.hotel_id;
	  var url = RESTONZACONSTANTS.url + "/getBilledOrder";
	  // Fetch table details call
	  $http.post(url, json).success(function(data, status, headers, config) {
		  $scope.orders = data.response.allorderVO;
		  $scope.taxes = data.response.taxes;
		  $scope.ltaxes = data.response.ltaxes;
		  $("#divLoading").removeClass('show');
	  }).error(function(data, status, headers, config) {
		  $scope.orders = [];
		  $("#divLoading").removeClass('show');
	  });
	   
	  $scope.billorder = function(index) {
		  $scope.resetAll();
		  
		  var selectedOrder = $scope.orders[index];
		//tax calculation
		    var totaltax = 0;
		    angular.forEach($scope.taxes, function(value, key){
		    	totaltax = totaltax + value;
		   });
		    
		   var totalltax = 0;
		   angular.forEach($scope.ltaxes, function(value, key){
			   totalltax = totalltax + value;
		   });
		   var foodamout = selectedOrder.foodbillamt == "null" ? 0 : selectedOrder.foodbillamt;
		   var baramount = selectedOrder.barbillamt == "null" ? 0 : selectedOrder.barbillamt;
		  var foodfinalamt = (((totaltax) + 100)*(foodamout))/100; 
		  var barfinalamt = (((totalltax) + 100)*(baramount))/100;
		  
		  $scope.foodBillFinalAmt = foodfinalamt;
		  $scope.barBillFinalAmt = barfinalamt;
		  $scope.finalFoodAmountTemp = foodfinalamt;
		  $scope.finalBarAmountTemp = barfinalamt;
		  
		  var finalamount = foodfinalamt + barfinalamt;
		  $scope.finalamountPrice = finalamount;
		    $.each(selectedOrder.formatOrderSummary.split(","), function (i, val) {
		    	var json = {};
		    	if (val != "") {
		    		var innerVal = val.split("-");
		        	json["dishname"] = innerVal[0];
		        	json["qty"] = innerVal[1];
		        	json["dish_price"] = $scope.single_record[innerVal[0]];
		        	$scope.dishDetails.push(json);
				}
		    });
		    var foodDishDetails = [];
		    var barDishDetails = [];
		    var tempDishDetails = $scope.dishDetails;
		    if(selectedOrder.barordercount == 'null') {
		    	foodDishDetails = $scope.dishDetails;
		    } else {
		    	foodDishDetails = $scope.dishDetails.splice(0, tempDishDetails.length-parseInt(selectedOrder.barordercount));
		    	barDishDetails = $scope.dishDetails.splice(tempDishDetails-parseInt(selectedOrder.barordercount), tempDishDetails.length);
		    }
		    
		     
		    $scope.editdata = {
		    		"customerid" : selectedOrder.customerid,
		    		"orderid": selectedOrder.orderids,
		    		"dishDetails": $scope.dishDetails,
		    		"tableid": selectedOrder.tableid,
		    		"taxes": $scope.taxes,
		    		"ltaxes": $scope.ltaxes,
					"totalamount" : selectedOrder.sum,
					"foodamount" : selectedOrder.foodbillamt,
					"baramount" : selectedOrder.barbillamt,
					"amt" : finalamount,
					"index": index,
					"foodOrderDetails" : foodDishDetails,
					"barOrderDetails"	: barDishDetails
		    }
		    if (selectedOrder.instruction !='') {
				$scope.instructionflag= true;
				$scope.instruction = selectedOrder.instruction;
			}
		   
		  $scope.discount=0;
		  $scope.discounMoneyt=0;
		  $scope.billAmount = finalamount;
		  $scope.finalAmount = finalamount;
		  $scope.finalAmountTemp = finalamount;
		  $scope.showModal = true;
	  };
	  
	  $scope.percentDiscount = function(type) {
		  if (type > 30) {
			  $.bootstrapGrowl('Discount percent should not be more then 30%' ,{
		             type: 'warning',
		             delay: 1000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
		  } else {
			  //$scope.finalAmount = $scope.billAmount*(100 - type)/100;
			  $scope.discounMoneyt = $scope.billAmount*type/100;
			  //$scope.discounMoneyt = $scope.billAmount*type/100;
		  }
		
	  }
	  
	  
	  $scope.roundoffDiscount = function(type) {
		  if(type < $scope.finalAmount) {
			  var discount = 100*type/$scope.finalAmountTemp;
			  $scope.discount = Math.round( discount * 100) / 100;			  
		  }else {
			  $scope.finalAmount = $scope.finalAmountTemp;
		  }
	  }
	  //--------------------------------------Food Discount----------------------------------------------------
	  $scope.percentFoodDiscount = function(type) {
		  if (type > 30) {
			  $.bootstrapGrowl('Discount percent should not be more then 30%' ,{
		             type: 'warning',
		             delay: 1000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
		  } else {
			  var discount = $scope.foodBillFinalAmt*type/100;
			  $scope.foodDiscountMoney = Math.round( discount * 100) / 100;
		  }
		
	  }
	  
	  
	  $scope.roundoffFoodDiscount = function(type) {
		  if(type < $scope.foodBillFinalAmt) {
			  var discount = 100*type/$scope.finalFoodAmountTemp;
			  $scope.foodDiscount = Math.round( discount * 100) / 100;			  
		  }else {
			  $scope.foodBillFinalAmt = $scope.finalFoodAmountTemp;
		  }
	  }
	  
	//--------------------------------------Bar Discount----------------------------------------------------
	  $scope.percentBarDiscount = function(type) {
		  if (type > 30) {
			  $.bootstrapGrowl('Discount percent should not be more then 30%' ,{
		             type: 'warning',
		             delay: 1000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
		  } else {
			  var discount = $scope.barBillFinalAmt*type/100;
			  $scope.barDiscountMoney = Math.round( discount * 100) / 100; 
		  }
		
	  }
	  
	  
	  $scope.roundoffBarDiscount = function(type) {
		  if(type < $scope.barBillFinalAmt) {
			  var discount = 100*type/$scope.finalBarAmountTemp;
			  $scope.barDiscount = Math.round( discount * 100) / 100;			  
		  }else {
			  $scope.barBillFinalAmt = $scope.finalBarAmountTemp;
		  }
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  //bill generation code
	  $scope.generateBill = function(index) {
		  $scope.showModal = false;
		  $("#divLoading").addClass('show'); 
		  var selectedOrder = $scope.orders[index];
			//TODO: need to add print service
			$scope.dishDetails = [];
			var json = {};
			$.each(selectedOrder.formatOrderSummary.split(","), function (i, val) {
		    	var jsonarr = {};
		    	if (val != "") {
		    		var innerVal = val.split("-");
		    		jsonarr["dishname"] = innerVal[0];
		    		jsonarr["qty"] = innerVal[1];
		    		jsonarr["price"] = $scope.single_record[innerVal[0]];
		        	$scope.dishDetails.push(jsonarr);
				};
		    });
			var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			json["customer_id"] = selectedOrder.customerid;
			json["hotel_id"] = sessionVO.hotel_id;
			json["order_id"] = selectedOrder.orderids;
			json["food_order_ids"] = selectedOrder.food_order_ids;
			json["bar_order_ids"] = selectedOrder.bar_order_ids;
			json["orderDetails"] = $scope.dishDetails;
			json["table_id"] = selectedOrder.tableid;
			$payableamountid = '#payableamt'+index;
			var amount = $($payableamountid).text();
			json["amt"] = Number(amount.replace(/[^0-9\.-]+/g,""));
			//json["amt"] =  parseFloat($($payableamountid).text());
			json["bar_order_count"] = selectedOrder.barordercount;
			json["food_bill_amt"] = selectedOrder.foodbillamt;
			json["bar_bill_amt"] = selectedOrder.barbillamt;
			json["totalAmountWOTaxDiscount"] =  $scope.finalamountPrice;
			var url = RESTONZACONSTANTS.url + "/dobilling";
			  // Fetch table details call
			  $http.post(url, json).success(function(data, status, headers, config) {
				  $("#divLoading").removeClass('show'); 
				  $route.reload();
				  //$scope.showFeedback = true;
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
			  });
	  };
    
    //feedback register
    $("#feedbackregisterform").validate({
		// Specify validation rules
		rules : {
			orderid : "required",
			custname : "required",
			rating : "required",
		},
		errorElement : "div",
		errorPlacement : function(error, element) {
			element.before(error);
		},
		// Specify validation error messages
		messages : {
			orderid : "Something went wrong! Call Administrator",
			custname : "Please Enter Customer name",
			rating : "Please give appropriate rating",
		},
		submitHandler : function(form) {
			//TODO: need to add print service
			var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			json["orderids"] = form.orderid.value;
			json["hotel_id"] = sessionVO.hotel_id;
			json["customer_name"] = form.custname.value;
			json["comments"] = form.suggestion.value;
			json["rating"] = form.rating.value;
			json["servicerating"] = '0';
			json["ambiencerating"] = '0';
			var url = RESTONZACONSTANTS.url + "/registerFeedBack";
			  // Fetch table details call
			  $http.post(url, json).success(function(data, status, headers, config) {
				  $route.reload();
				  $scope.showFeedback = false;
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
				  $scope.orders = "";
			  });
		}
	});
    
  //print kot functionality modification 22/07/2017
    $scope.printBill = function(index) {
		  $scope.resetAll();
    		$scope.printdata = {};
    		$scope.printdishDetails = [];
		    var selectedOrder = $scope.orders[index];
		    $.each(selectedOrder.formatOrderSummary.split(","), function (i, val) {
		    	var json = {};
		    	if (val != "") {
		    		var innerVal = val.split("-");
		    		json["dishname"] = innerVal[0];
		        	json["qty"] = innerVal[1];
		        	json["price"] = $scope.single_record[innerVal[0]];
		        	$scope.printdishDetails.push(json);
				}
		    });
		    
		    var foodDishDetails = [];
		    var barDishDetails = [];
		    var tempDishDetails = $scope.printdishDetails;
		    if(selectedOrder.barordercount == 'null') {
		    	foodDishDetails = $scope.printdishDetails;
		    } else {
		    	foodDishDetails = $scope.printdishDetails.splice(0, tempDishDetails.length-parseInt(selectedOrder.barordercount));
		    	barDishDetails = $scope.printdishDetails.splice(tempDishDetails-parseInt(selectedOrder.barordercount), tempDishDetails.length);
		    }
		    
		    var amount = parseInt(selectedOrder.sum == "" ? "0" : selectedOrder.sum);
		  //tax calculation
		    var totaltax = 0;
		    angular.forEach($scope.taxes, function(value, key){
		    	totaltax = totaltax + value;
		        console.log(key + ': ' + value);
		   });
		    
		    	var totalltax = 0;
			   angular.forEach($scope.ltaxes, function(value, key){
				   totalltax = totalltax + value;
			   });
			   var foodamout = selectedOrder.foodbillamt == "null" ? 0 : selectedOrder.foodbillamt;
			   var baramount = selectedOrder.barbillamt == "null" ? 0 : selectedOrder.barbillamt;
			  var foodfinalamt = (((totaltax) + 100)*(foodamout))/100; 
			  var barfinalamt = (((totalltax) + 100)*(baramount))/100; 
			  
			  $scope.foodBillFinalAmt = foodfinalamt;
			  $scope.barBillFinalAmt = barfinalamt;
			  $scope.finalFoodAmountTemp = foodfinalamt;
			  $scope.finalBarAmountTemp = barfinalamt;
			  
			  
			  var finalamount = foodfinalamt + barfinalamt;
			  $scope.finalamountPrice = finalamount;
		    $scope.printdata = {
		    		"hotelid" : sessionVO.hotel_id,
		    		"tableid": selectedOrder.tableid, 
		    		"orderid": selectedOrder.orderids,
		    		"dishDetails": $scope.printdishDetails,
		    		"customerid" : selectedOrder.customerid,
		    		"ltaxes": $scope.ltaxes,
					"totalamount" : selectedOrder.sum,
		    		"amt" : finalamount,
		    		"taxes": $scope.taxes,
		    		"barordercount" : selectedOrder.barordercount,
					"foodbillamt" : selectedOrder.foodbillamt,
					"barbillamt" : selectedOrder.barbillamt,
					"foodOrderDetails" : foodDishDetails,
					"barOrderDetails"	: barDishDetails
		    }
		    
		      $scope.discount=0;
			  $scope.discounMoneyt=0;
			  $scope.billAmount = finalamount;
			  $scope.finalAmount = finalamount;
			  $scope.finalAmountTemp = finalamount;
			  
		    $scope.showPrint = true;
		    PrintService.setPrintVO($scope.printdata);
	  };
    
	  //modified version of printkot 22/07/2017---------------------------------------------------------------
	  $scope.printKOT = function() {
		  $scope.showPrint = false;
	  	/*$scope.showPrint = false;
		var options = {mode:"popup",popHt: 500,   popWd: 400, popX: 1000, popY: 1000, popTitle:"Restonza Hotel Management App: Bill Printing", popClose: false};
	    $('#billorderkot').printArea( options );*/
		var printDataObject = PrintService.getPrintVO();
		PrintService.doRestoBarPrintJob(printDataObject);
	};
	
	$scope.closeFeedback = function() {
		$scope.showFeedback = false;
		$route.reload();
	}
	 
	  $scope.resetAll = function() {
			$scope.dishDetails = [];
			$scope.editdata = {};
			$scope.printdata = {};
			  $scope.printdishDetails = [];
			  $scope.finalamountPrice = 0;
			  //Added New variables for separate bill changes
			  $scope.finalAmountTemp = 0;
			  $scope.discounMoneyt = 0;
			  $scope.foodDiscount = 0;
			  $scope.barDiscount = 0;
			  $scope.foodBillFinalAmt = 0;
			  $scope.barBillFinalAmt = 0;
			  $scope.foodDiscountMoney = 0;
			  $scope.barDiscountMoney = 0;
			  $scope.finalFoodAmountTemp = 0;
			  $scope.finalBarAmountTemp = 0;
		};
});