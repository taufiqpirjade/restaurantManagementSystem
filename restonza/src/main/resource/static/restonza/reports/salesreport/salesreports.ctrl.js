angular.module('restonza').controller('SalesController', function ($scope,$filter, $http, $route, $location, RESTONZACONSTANTS) {
	
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
	 
	 
	 $(".daterangepicker-field").daterangepicker({
		  forceUpdate: true,
		  callback: function(startDate, endDate, period){
		    var title = startDate.format('L') + ' – ' + endDate.format('L');
		    $(this).val(title)
		  }
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
		$("#todate").val("");
		$("#fromdate").val("");
	 };
	 
	//Others js validation
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
	    	$scope.getPdfDownload();
	    	/*form.submit();
			location.reload();*/
	    	
	    }
	  });
	
	$scope.getPdfDownload = function(){
		 $("#divLoading").addClass('show');
		 var json = {};
		 var daterange = $("#daterange").val();
		 var range = daterange.split(' – ');
		 json["startdate"] = range[0].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["fromdate"] = range[0].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["todate"] = range[1].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["hotelname"] = $("#hotelname").val();
		 json["reporttype"] = 	"";
		 var url = RESTONZACONSTANTS.url + "/getSalesReportPdfDownload";
		 $http.post(url, json, {responseType: 'arraybuffer'}).success(function(data, status, headers, config) {
			 /*window.location = "/salesReport/DownloadCSV?data="+data;*/
			 var file = new Blob([ data ], {
	                type : 'application/pdf'
	            });
	            var isChrome = !!window.chrome && !!window.chrome.webstore;
               var isIE = /*@cc_on!@*/false || !!document.documentMode;
               var isEdge = !isIE && !!window.StyleMedia;


               if (isChrome){
                   var url = window.URL || window.webkitURL;

                   var downloadLink = angular.element('<a></a>');
                   downloadLink.attr('href',url.createObjectURL(file));
                   downloadLink.attr('target','_self');
                   downloadLink.attr('download', 'salesreport.pdf');
                   downloadLink[0].click();
               }
               else if(isEdge || isIE){
                   window.navigator.msSaveOrOpenBlob(file,'salesreport.pdf');

               }
               else {
                   var fileURL = URL.createObjectURL(file);
                   window.open(fileURL);
               }
               $("#divLoading").removeClass('show');
		 });
	 }
});