angular.module('restonza').controller('NewBarOrderController', function ($scope, $route, $http, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show');	
	$scope.barmenuitemnamelist= [];
	$scope.dishDetails = [];
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	//get table information
		  var tablejson = {};
		  var url = RESTONZACONSTANTS.url + "/getActiveTables";
		  tablejson["hotel_id"] = sessionVO.hotel_id;
		  $http.post(url, tablejson).success(function(data, status, headers, config) {
			  $scope.tableno = data.response;
			  $("#tableno").select2({
				  data: $scope.tableno
				});
		    }).error(function(data, status, headers, config) {
		    	$scope.tableno = [];
		   });
		  //dishdetails
		  var updatejson = {};
		  var dishrecords = [];
		  $scope.dishList = [];
		  updatejson["hotel"] = sessionVO.hotel_id;
		  var url = RESTONZACONSTANTS.url + "/getBarItemNameList/" + sessionVO.hotel_id;
		  $("#divLoading").addClass('show');
		  $http.post(url, updatejson).success(function(data, status, headers, config) {
			  var dishItems = [];
			  $.each(data.response, function (index, singleItem) {
				  dishItems.push(singleItem.bar_item_name)
				});
			  $scope.barmenuitemnamelist = data.response;
				$("#dish").select2({
					  data: dishItems,
					  width: 'resolve',
				});
				$("#divLoading").removeClass('show');
			}).error(function(data, status, headers, config) {
				$scope.dish = "";
				$("#divLoading").removeClass('show');
			});
		  
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
		  
		  
		  $scope.addNew = function(){
			  var selectedDish = $("#dish").val();
			  var hasFoo = false;
			  $.each($scope.dishDetails, function(i,obj) {
			    if (obj.name === selectedDish ) {
			    	hasFoo = true; 
			    	return false;
			    }
			  });  
			  if (hasFoo) {
				$("#errormsg").text("BarMenu Item already present");
				setTimeout(function(){
					$("#errormsg").text("");
				}, 1500);
			  } else {
				  $scope.dishDetails.push({
					  	'selected': true,
		                'name': selectedDish, 
		                'qty': 	$scope.currentSelectionCounter
		            });
				  $scope.currentSelectionCounter = 1;
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
			    	json["instruction"] = $("#instruction").val();
			    	$('#ordertable tbody tr').filter(':has(:checkbox:checked)').each(function() {
		    	        $tr = $(this);
	    	        	var dishjson = {};
	    	        	var dishname =$tr.find('#dishname').val();
	    	        	var qty = $tr.find('.qty').val();
	    	        	dishjson["dishname"] = dishname;
	    	        	dishjson["qty"] = qty;
	    	        	dishArray.push(dishjson);
			    	});
			    	 json["orderDetails"] = dishArray;
			    	 var url = RESTONZACONSTANTS.url + "/addBarOrder";
			    	 console.log(JSON.stringify(json));
			    	 $http.post(url, json).success(function(data, status, headers, config) {
			    		 $("#divLoading").removeClass('show');
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
			    }
			  });
});