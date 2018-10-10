angular.module('restonza').controller('UpdateHotelManagementController', function ($scope,$filter, $http, $route, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show');
	$("#startdate").datepicker({dateFormat: 'dd/mm/yy'});  
	$scope.showModal = false;
	$scope.showConfirmation = false;
	$scope.hotelstatus = "";
	$scope.barpriviledge="";
	
	var hoteltypes = ["Ethnic", "Fast Food", "Fast Casual", "Casual dining", "Family style", "Fine dining", "Pub", "Cafeteria", "Table Top"];
	$("#hoteltype").select2({
	  data: hoteltypes
	});
	
	$("#hoteladminphone").mask("9999999999");
	$("#hoteladminaadhar").mask("9999-9999-9999-9999");
	
	var hotelsubscription = ["3months-699", "6months-1199", "12months-1899"];
	$("#hotelsubscription").select2({
	  data: hotelsubscription
	});
	
	//image upload functionality
	$(":file").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = imageIsLoaded;
            reader.readAsDataURL(this.files[0]);
        }
    });
	
	function imageIsLoaded(e) {
	    $('#myImg').attr('src', e.target.result);
	};
	var updatejson = {};
	$("#divLoading").addClass('show');
	var url = RESTONZACONSTANTS.url + "/getHotels";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.hotels = data.response;
		$("#divLoading").removeClass('show');
	}).error(function(data, status, headers, config) {
		$scope.hotels = "";
		$("#divLoading").removeClass('show');
	});
  
  $scope.statuses = [
                     {value: 'Exprired', text: 'Exprired'},
                     {value: 'Active', text: 'Active'},
                     {value: 'Inactive', text: 'Inactive'}
                   ];
  
  
  $scope.showStatus = function(hotel) {
      var selected = [];
      if(hotel.status) {
        selected = $filter('filter')($scope.statuses, {value: hotel.status});
      }
      return selected.length ? selected[0].text : 'Not set';
    };
    
    $scope.removeHotel = function(hotelid) {
    	$("#divLoading").addClass('show');
      	var json = {};
  		json["id"] = hotelid;
  		var url = RESTONZACONSTANTS.url + "/deleteHotel";
  		$http.post(url, json).success(function(data, status, headers, config) {
  			$scope.showModal = false;
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
  			$route.reload();
  		}).error(function(data, status, headers, config) {
  			console.log("error");
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
    };
    
      $scope.editHotel = function(index) {
        var selectedHotel = $scope.hotels[index];
        $scope.editdata = {
        		"id" : selectedHotel.id,
        		"startdate": selectedHotel.startdate,
        }
        $('#hoteltype').val(selectedHotel.hoteltype).trigger('change');
        $('#hotelsubscription').val(selectedHotel.hotelsubscription).trigger('change');
        $scope.showModal = true;
        $scope.hotelstatus = (selectedHotel.status).toLowerCase();
        $scope.barpriviledge = (selectedHotel.barPriviledege).toLowerCase();
      };
      
		$("#updateSelectedHotel").validate({
		    // Specify validation rules
		    rules: {
		    	hoteltype: "required",
		    	hotelsubscription: "required",
		    	startdate: "required"
		    },
		    errorElement: "div",
	    	errorPlacement: function(error, element) {
	    		element.before(error);
          },
		    // Specify validation error messages
		    messages: {
		    	hoteltype: "Enter Hotel type",
		    	hotelsubscription: "Please select hotel subscription plan",
		    	startdate: "Please select subscription plan start date"
		    },
		    submitHandler: function(form) {
		    	$("#divLoading").addClass('show');
		    	$scope.showModal = false;
			     var json= {};
			     json["id"] = $("#hotelid").text();
		    	 json["hoteltype"] = form.hoteltype.value;
		    	 json["hotelsubscription"] = form.hotelsubscription.value;
		    	 json["startdate"] = form.startdate.value;
		    	 json["status"] = form.hotelstatus.value;
		    	 json["barPriviledege"] = form.barpriviledge.value;
		    	 var url = RESTONZACONSTANTS.url + "/updateHotel";
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
					    $route.reload();
		   	  	}).error(function(data, status, headers, config) {
		   	  		$("#divLoading").removeClass('show');
		   	  		$route.reload();
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
}).directive('ngConfirmClick', [
                                 function(){
                                     return {
                                         link: function (scope, element, attr) {
                                             var msg = attr.ngConfirmClick || "Are you sure?";
                                             var clickAction = attr.confirmedClick;
                                             element.bind('click',function (event) {
                                                 if ( window.confirm(msg) ) {
                                                     scope.$eval(clickAction)
                                                 }
                                             });
                                         }
                                     };
                             }])