angular.module('restonza').controller('CustomReportGenerator', function ($scope,$filter, $http, $route, $location, RESTONZACONSTANTS) {
	$scope.dailyflag = false;
	$scope.customflag = false;
	$("#divLoading").removeClass('show');
	var url = RESTONZACONSTANTS.url + "/getActiveHotelsDetails";
	var json = {};
	 $http.post(url, json).success(function(data, status, headers, config) {
		 $scope.hotelList = data.response;
		 var hotelnos = [];
		  for (var i = 0; i < data.response.length; i++) {
			  hotelnos.push(data.response[i].id);
		  }
		  $scope.hotelno = hotelnos;
		  $("#hotelname").select2({
			  data: $scope.hotelno
			});
	   }).error(function(data, status, headers, config) {
	   	$scope.hotelno = "";
	  });
	
	 $scope.getHotelAnalysis = function(){
		 var selectedhotel = $("#hotelname").val();
			for (var int = 0; int < $scope.hotelList.length; int++) {
				if ($scope.hotelList[int].id == selectedhotel) {
					$scope.hotel_name = $scope.hotelList[int].hotel_name;
				}
			}
	 };
	 
	 $scope.resetAll = function() {
		$("#startdate").val(""); 
		$("#todate").val("");
		$("#fromdate").val("");
	 };
	 
	 /**
	  * For getting detail report view
	  */
	 $scope.getReportData = function() {
		 $("#divLoading").addClass('show');
		 var json = {};
		 json["startdate"] = $("#startdate").val();
		 json["fromdate"] = $("#fromdate").val();
		 json["todate"] = $("#todate").val();
		 json["hotelname"] =  $("#hotelname").val();
		 if ($scope.dailyflag==true) {
			 json["reporttype"] = 	"daily"; 
		 } else {
			 json["reporttype"] = 	"custom"; 
		 }
		 var url = RESTONZACONSTANTS.url + "/downloadDetailReportWebView";
		 $http.post(url, json).success(function(data, status, headers, config) {
			 console.log(data);
			 $scope.reportData = data.response.reportdata;
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