angular.module('restonza').controller('AdminSalesController', function ($scope,$filter, $http, $route, $location,$window, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show');
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	$scope.hotelno = sessionVO.hotel_id;
	 $scope.resetAll = function() {
		$("#todate").val("");
		$("#fromdate").val("");
	 };
	
	 $(".daterangepicker-field").daterangepicker({
		  forceUpdate: true,
		  callback: function(startDate, endDate, period){
		    var title = startDate.format('L') + ' – ' + endDate.format('L');
		    $(this).val(title)
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
		 json["hotelname"] = sessionVO.hotel_id;
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
	 
	// /restonza/downloadSalesPdf
	 $scope.getPdfReport = function(){
		 $("#divLoading").addClass('show');
		 var json = {};
		 var daterange = $("#daterange").val();
		 var range = daterange.split(' – ');
		 json["startdate"] = range[0].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["fromdate"] = range[0].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["todate"] = range[1].replace(/(\d\d)\/(\d\d)\/(\d{4})/, "$3-$1-$2");
		 json["hotelname"] = sessionVO.hotel_id;
		 json["reporttype"] = 	"";
		 var url = RESTONZACONSTANTS.url + "/getSalesReportWebView";
		 $http.post(url, json).success(function(data, status, headers, config) {
			 $scope.salesTableData = data.response.salesreportvo;
			 $scope.totalEarningAmount = Math.trunc(data.response.totalEarningAmount);
			 var responseData = data.response.responseParam;
			 var dailyOrderCounter = responseData.dailyOrderCounter; // for table count display.
			 var dailyEarning = responseData.dailyearning;
			 var orderCount = 0;
			 var abc = data.response.responseParam.dailyOrderCounter;
			 $.each(abc, function( index, value ) {
				 orderCount = orderCount+value;
			});
			 $("#divLoading").removeClass('show');
			 $scope.totalorderCount = orderCount;
			 var chart = AmCharts.makeChart("chartdiv", {
				    "type": "serial",
				    "theme": "light",
				    "marginRight": 40,
				    "marginLeft": 40,
				    "autoMarginOffset": 20,
				    "mouseWheelZoomEnabled":true,
				    "dataDateFormat": "YYYY-MM-DD",
				    "valueAxes": [{
				        "id": "v1",
				        "axisAlpha": 0,
				        "position": "left",
				        "ignoreAxisWidth":true
				    }],
				    "balloon": {
				        "borderThickness": 1,
				        "shadowAlpha": 0
				    },
				    "graphs": [{
				        "id": "g1",
				        "balloon":{
				          "drop":true,
				          "adjustBorderColor":false,
				          "color":"#ffffff"
				        },
				        "bullet": "round",
				        "bulletBorderAlpha": 1,
				        "bulletColor": "#FFFFFF",
				        "bulletSize": 5,
				        "hideBulletsCount": 50,
				        "lineThickness": 2,
				        "title": "red line",
				        "useLineColorForBulletBorder": true,
				        "valueField": "value",
				        "balloonText": "<span style='font-size:18px;'>[[value]]</span>"
				    }],
				    "chartScrollbar": {
				        "graph": "g1",
				        "oppositeAxis":false,
				        "offset":30,
				        "scrollbarHeight": 80,
				        "backgroundAlpha": 0,
				        "selectedBackgroundAlpha": 0.1,
				        "selectedBackgroundColor": "#888888",
				        "graphFillAlpha": 0,
				        "graphLineAlpha": 0.5,
				        "selectedGraphFillAlpha": 0,
				        "selectedGraphLineAlpha": 1,
				        "autoGridCount":true,
				        "color":"#AAAAAA"
				    },
				    "chartCursor": {
				        "pan": true,
				        "valueLineEnabled": true,
				        "valueLineBalloonEnabled": true,
				        "cursorAlpha":1,
				        "cursorColor":"#258cbb",
				        "limitToGraph":"g1",
				        "valueLineAlpha":0.2,
				        "valueZoomable":true
				    },
				    "valueScrollbar":{
				      "oppositeAxis":false,
				      "offset":50,
				      "scrollbarHeight":10
				    },
				    "categoryField": "date",
				    "categoryAxis": {
				        "parseDates": true,
				        "dashLength": 1,
				        "minorGridEnabled": true
				    },
				    "export": {
				        "enabled": true
				    },
				    "dataProvider": data.response.reportData
				});
		   }).error(function(data, status, headers, config) {
		   	
		  });
	 };
	 
	 
	
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
	    	form.submit();
			location.reload();
	    }
	  });
});