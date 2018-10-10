angular.module('restonza').controller('NewOrderController', function ($scope, $route, $http, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show');	
	$scope.dish= [];
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
		  var tablejson = {};
		  var url = RESTONZACONSTANTS.url + "/getActiveTables";
		  tablejson["hotel_id"] = sessionVO.hotel_id;
		  $http.post(url, tablejson).success(function(data, status, headers, config) {
			  $scope.tableno = data.response;
			  $("#tableno").select2({
				  data: $scope.tableno
				});
		    }).error(function(data, status, headers, config) {
		    	$scope.tableno = "";
		   });
		  //table details
		  //dishdetails
		  var updatejson = {};
		  var dishrecords = [];
		  $scope.dishList = [];
		  $scope.single_record = {};
		  $scope.dish_id_record = {};
		  updatejson["hotel"] = sessionVO.hotel_id;
		  var url = RESTONZACONSTANTS.url + "/getDishes";
		  $("#divLoading").addClass('show');
		  $http.post(url, updatejson).success(function(data, status, headers, config) {
			  $(data.response).each(function(idx, obj){
					dishrecords.push(obj.dish_name);
					$scope.single_record[obj.dish_name] = obj.dish_price;
					$scope.dish_id_record[obj.dish_name] = obj.dish_id;
			    });
				$scope.dish = dishrecords;
				$scope.dishList = dishrecords;
				$("#dish").select2({
					  data: $scope.dish,
					  width: 'resolve',
				});
				$("#divLoading").removeClass('show');
			}).error(function(data, status, headers, config) {
				$scope.dish = "";
				$("#divLoading").removeClass('show');
			});
		  //$scope.category = ["veg","non-veg","sizzlers"];
		  
		  //Table operation
		  $scope.dishDetails = [];
		  $scope.currentSelectionCounter = 1;
		  
		  $scope.incrementCurrentSelection= function() {
			  $scope.currentSelectionCounter = $scope.currentSelectionCounter+1;
		  };
		  
		  $scope.decrementCurrentSelection = function() {
			  if ($scope.currentSelectionCounter == 0) {
				  
			  } else {
				  $scope.currentSelectionCounter = $scope.currentSelectionCounter-1;
			  }
		  };
		  
		  $scope.currentSelectionQty = function() {
			  
		  };
		  
		  
		  $scope.addNew = function(dishDetails) {
			  	if ($("#dish").val() != null) {
				  var selectedDish = $("#dish").val();
				  var hasFoo = false;
				  $.each($scope.dishDetails, function(i,obj) {
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
					  $scope.dishDetails.push({
						  	'selected': true,
			                'name': selectedDish, 
			                'qty': $scope.currentSelectionCounter,
			                "dish_price": $scope.single_record[selectedDish],
			                "dish_id" : $scope.dish_id_record[selectedDish]
			            });
					  $scope.currentSelectionCounter = 1;
				  }
		        $scope.PD = {};
			  }
	        };
	        
	        
	    
	        $scope.remove = function(){
	            var newDataList=[];
	            $scope.selectedAll = false;
	            angular.forEach($scope.dishDetails, function(selected){
	                if(!selected.selected){
	                    newDataList.push(selected);
	                }
	            }); 
	            $scope.dishDetails = newDataList;
	        };
	    
	        $scope.checkAll = function () {
	            if (!$scope.selectedAll) {
	                $scope.selectedAll = true;
	            } else {
	                $scope.selectedAll = false;
	            }
	            angular.forEach($scope.dishDetails, function (dishDetails) {
	            	dishDetails.selected = $scope.selectedAll;
	            });
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
	        
	        //Table operation end
			
			//form validation started
			 $.validator.addMethod('onecheck', function(value, ele) {
		            return $("input:checked").length >= 1;
		        }, 'Please Select Atleast One CheckBox');
			
			$("#placeneworderform").validate({
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
			    	$("#divLoading").addClass('show');
			    	var json= {};
			    	var dishArray = [];
			    	var totalamt = 0;
			    	json["hotel_id"] = sessionVO.hotel_id;
			    	json["table_id"] = $("#tableno").val();
			    	json["customer_id"] = sessionVO.empid;
			    	json["estimated_time"] = $("#time").val();
			    	json["instruction"] = $("#instruction").val();
			    	$('#ordertable tbody tr').filter(':has(:checkbox:checked)').each(function() {
		    	        $tr = $(this);
	    	        	var dishjson = {};
	    	        	var dishname =$tr.find('#dishname').val();
	    	        	var qty = $tr.find('.qty').val();
	    	        	var price = $tr.find('.price').val();
	    	        	var dishId = $tr.find('.dishid').val();
	    	        	dishjson["dishname"] = dishname;
	    	        	dishjson["qty"] = qty;
	    	        	dishjson["price"] = price;
	    	        	dishjson["id"] = dishId;
	    	        	dishArray.push(dishjson);
	    	        	totalamt = totalamt + parseInt(qty)*$tr.find('.price').val();
			    	});
			    	 json["amt"] = totalamt;
			    	 json["orderDetails"] = dishArray;
			    	 var url = RESTONZACONSTANTS.url + "/addOrder";
			    	 console.log(JSON.stringify(json));
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