angular.module('restonza').controller('UpdateBarOrderController', function ($scope,$filter,$http,$route,RESTONZACONSTANTS/* $scope, $location, $http */) {
 var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
 $("#divLoading").removeClass('show');
 var tablejson = {};
 var url = RESTONZACONSTANTS.url + "/getOccupiedTableList";
 tablejson["hotel_id"] = sessionVO.hotel_id;
 $http.post(url, tablejson).success(function(data, status, headers, config) {
	  $scope.tableno = data.response;
	  $("#tableno").select2({
		  data: $scope.tableno
		});
   }).error(function(data, status, headers, config) {
   	$scope.tableno = "";
  });
$scope.showModal = false;
$scope.dishDetails = [];
$scope.editdata = {};
var updatejson = {};
var dishrecords = [];
$scope.single_record = {};
updatejson["hotel"] = sessionVO.hotel_id;
$("#divLoading").addClass('show');
var url = RESTONZACONSTANTS.url + "/getDishes";
$http.post(url, updatejson).success(function(data, status, headers, config) {
	$("#divLoading").removeClass('show');
	  $(data.response).each(function(idx, obj){
			dishrecords.push(obj.dish_name);
			$scope.single_record[obj.dish_name] = obj.dish_price;
	    });
		$scope.dish = dishrecords;
		$("#dish").select2({
			  data: $scope.dish
		});
	}).error(function(data, status, headers, config) {
		$scope.dish = "";
		$("#divLoading").removeClass('show');
	});

$scope.resetAll = function() {
	$scope.showModal = false;
	$scope.dishDetails = [];
	$scope.editdata = {};
};
//table number change event
 $scope.displayPlacedOrder = function(){
	 $("#divLoading").addClass('show');
	var selectedTable = $("#tableno").val();
	var hotel_id = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getOrderDetails";
	var json = {};
	json["table_id"] = selectedTable;
	json["hotel_id"] = hotel_id;
	$http.post(url, json).success(function(data, status, headers, config) {
		$("#divLoading").removeClass('show');
		if (data.status != 'fail') {
			$scope.displaytable = true;
			$scope.orders = data.response;
		} else {
			$scope.displaytable = false;
			 $.bootstrapGrowl(data.response ,{
	             type: 'danger',
	             delay: 2000,
	             offset: {
	            	 from: "top",
	            	 amount: 0
	             },
	             align: "center",
	             allow_dismiss: false
	         });
		}
		
  	}).error(function(data, status, headers, config) {
  		$("#divLoading").removeClass('show');
  		console.log("error");
  		$scope.displaytable = false;
  	});
 };
	
$scope.removeOrder = function(index) {
	var json = {};
	json["order_id"] = index;
	var url = RESTONZACONSTANTS.url + "/cancelOrder";
	 $http.post(url, json).success(function(data, status, headers, config) {
		 console.log("success");
		 $scope.showModal = false;
		 $route.reload();
		 $scope.orders.splice(index, 1);
		 //form.submit();
	  	}).error(function(data, status, headers, config) {
	      console.log("error");
	  	});
  };

  $scope.editOrder = function(index) {
	$scope.resetAll();
    var selectedOrder = $scope.orders[index];
    $.each(selectedOrder.orderdetails.split(/\|/), function (i, val) {
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
    		"orderid": selectedOrder.orderid,
    		"dishDetails": $scope.dishDetails,
    }
    $scope.showModal = true;
  };

  
  //Modal Methods
  
  $scope.checkAll = function () {
      if (!$scope.selectedAll) {
          $scope.selectedAll = true;
      } else {
          $scope.selectedAll = false;
      }
      angular.forEach($scope.editdata.dishDetails, function (dishDetails) {
      	dishDetails.selected = $scope.selectedAll;
      });
  };
  
  $scope.addNew = function(dishDetails){
	  var selectedDish = $("#dish").val();
	  var hasFoo = false;
	  $.each($scope.editdata.dishDetails, function(i,obj) {
	    if (obj.name === selectedDish ) {
	    	hasFoo = true; 
	    	return false;
	    }
	  });  
	  console.log(hasFoo);
	  if (hasFoo) {
		  $id='#'+selectedDish+'tr';
		  $($id).addClass("invalid");
		$("#errormsg").text("Dish already present");
		setTimeout(function(){
			$($id).removeClass("invalid");
			$("#errormsg").text("");
		}, 1500);
	  } else {
		  $scope.editdata.dishDetails.push({
			  	'selected': true,
                'name': selectedDish, 
                'qty': "1",
                "dish_price": $scope.single_record[selectedDish],
            });
	  }
        $scope.PD = {};
    };
  
  $scope.remove = function(){
      var newDataList=[];
      $scope.selectedAll = false;
      angular.forEach($scope.editdata.dishDetails, function(selected){
          if(!selected.selected){
              newDataList.push(selected);
          }
      }); 
      $scope.editdata.dishDetails = newDataList;
  };
  
  $scope.decrement = function(event) {
  	$id = '#'+event.target.id + 'textbox';
  	var currentVal = parseInt($($id).val());
  	  if (!isNaN(currentVal) && currentVal > 1) {
            // Decrement one
            $($id).val(currentVal - 1);
        } else {
            // Otherwise put a 1 there
            $($id).val(1);
        }
  };
  
  $scope.increment = function(event) {
  	$id = '#'+event.target.id + 'textbox';
  	var currentVal = parseInt($($id).val());
      // If is not undefined
      if (!isNaN(currentVal)) {
          // Increment
          $($id).val(currentVal + 1);
      } else {
          // Otherwise put a 1 there
          $($id).val(1);
      }
  	
	};
	
	//form submit handler
	//form validation started
	 $.validator.addMethod('onecheck', function(value, ele) {
           return $("input:checked").length >= 1;
       }, 'Please Select Atleast One CheckBox');
	
	$("#ordermodelform").validate({
	    // Specify validation rules
	    rules: {
	    	checkboxes: {
	            required:
	            {
	            	onecheck: true
	            }

	          }
	    	},
	    	errorElement: "div",
	    	errorPlacement: function(error, element) {
	    		element.parent().parent().parent().parent().before(error);
           },
	    // Specify validation error messages
	    messages: {
	    	checkboxes: {
	    		required: "Select atleast one item",
	    	},
	    },
	    submitHandler: function(form) {
	    	var json= {};
	    	var totalamt = 0;
	    	var dishArray = [];
	    	json["order_id"] = $("#orderid").text();
	    	$('#ordertable tbody tr').filter(':has(:checkbox:checked)').each(function() {
   	        $tr = $(this);
	        	var dishjson = {};
	        	var dishname =$tr.find('#dishname').val();
	        	var qty = $tr.find('.qty').val();
	        	dishjson["dishname"] = dishname;
	        	dishjson["qty"] = qty;
	        	dishArray.push(dishjson);
	        	totalamt = totalamt + parseInt(qty)*parseInt($tr.find('.price').val());
	    	});
	    	 json["orderDetails"] = dishArray;
	    	 json["amt"] = totalamt;
	    	 $scope.showModal = false;
	    	 $("#divLoading").addClass('show');
	    	 var url = RESTONZACONSTANTS.url + "/updateOrderDetails";
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
	   	  		$.bootstrapGrowl(data.response ,{
		             type: 'danger',
		             delay: 2000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
	   	  	});
	    }
	  });
});