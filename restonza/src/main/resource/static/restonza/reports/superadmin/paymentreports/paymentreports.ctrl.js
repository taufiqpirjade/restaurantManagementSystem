angular.module('restonza').controller('SuperAdminPaymentController', function ($scope,$filter, $http, $route, $location, RESTONZACONSTANTS) {
	$scope.dailyflag = false;
	$scope.customflag = false;
	$("#divLoading").removeClass('show');
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	$scope.hotelno = sessionVO.hotel_id;
	 $scope.resetAll = function() {
		$("#startdate").val(""); 
		$("#todate").val("");
		$("#fromdate").val("");
	 };
	 
	 /**
	  * For getting detail report view
	  */
	 $scope.getPaymentData = function() {
		 $("#divLoading").addClass('show');
		 var json = {};
		 json["startdate"] = $("#startdate").val();
		 json["fromdate"] = $("#fromdate").val();
		 json["todate"] = $("#todate").val();
		 json["hotelname"] = sessionVO.hotel_id;
		 json["reporttype"] = 	$scope.reporttype;
		 var url = RESTONZACONSTANTS.url + "/paymentDetails";
		 $http.post(url, json).success(function(data, status, headers, config) {
			 console.log(data);
			 $scope.reportData = data.response;
			 $("#divLoading").removeClass('show');
		 });
		
	 }
	 
	 $scope.changeFlag = function(selectedopetion) {
		 $scope.resetAll()
		 if (selectedopetion == 'daily') {
			$scope.dailyflag = true;
			$scope.customflag = false;
		} else {
			$scope.dailyflag = false;
			$scope.customflag = true;
		}
	 };
	
	//Others js validation
	$("#startdate").datepicker({dateFormat: 'yy-mm-dd'}); 
	$("#todate").datepicker({dateFormat: 'yy-mm-dd'});
	$("#fromdate").datepicker({dateFormat: 'yy-mm-dd'});
	
	//hotel form validation
	$.validator.addMethod('greaterThan', function (value, element, params) {
		if (!/Invalid|NaN/.test(new Date(value))) {
	        return new Date(value) > new Date($(params).val());
	    }

	    return isNaN(value) && isNaN($(params).val()) 
	        || (Number(value) > Number($(params).val())); 
	},'Must be greater than {0}.');
	
	$("#addhotelform").validate({
	    // Specify validation rules
	    rules: {
	    	hotelno: "required",
	    	reporttype: "required",
	    	startdate: "required",
	    	fromdate: "required",
	    	todate: {
	    		required: true,
	    		greaterThan: "#fromdate"
	    		}
	    },
	    errorElement: "div",
    	errorPlacement: function(error, element) {
    		element.before(error);
      },
	    // Specify validation error messages
	    messages: {
	    	hotelno: "Please select hotel id",
	    	reporttype: "Please select report type",
	    	startdate: "Please select report date",
	    	fromdate: "Please select report from date",
	    	todate: {
	    		required: "Please select report to date",
	    		greaterThan: "To date should be greather than From date"
	    		}
	    },
	    submitHandler: function(form) {
	    	$("#divLoading").addClass('show');
	    	setTimeout(function(){
	    		$("#divLoading").removeClass('show');
	    	}, 1000);
	    	form.submit();
			location.reload();
	    }
	  });
});