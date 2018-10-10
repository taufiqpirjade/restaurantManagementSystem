angular.module('restonza').controller('HotelMenuBulkUploadController', function($scope, $route, $http, $filter, RESTONZACONSTANTS/* $scope, $location, $http */) {
$scope.showModal = false;
	$scope.bulkDatafileupload = function() {
		 var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)$/;
	        if (regex.test($("#fileUpload").val().toLowerCase())) {
	            if (typeof (FileReader) != "undefined") {
	                var reader = new FileReader();
	                reader.onload = function (e) {
	                    var table = $("<table id='dishes' class='table table-bordered table-hover table-condensed'/>");
	                    var rows = e.target.result.split("\n");
	                    var tharray = ["hotel_id","dish_name","dish_category","dish_description","dish_price","calories","discount","avg_cooking_time","hot","ingedients"]
	                    var theadtag = $("<thead />");
	                    var headerRow = $("<tr />");
	                    for (var j = 0; j < tharray.length; j++) {
	                    	  var cell = $("<th />");
	                    	  cell.html(tharray[j]);
	                    	  headerRow.append(cell);
						}
	                    theadtag.append(headerRow);
	                    table.append(theadtag);
	                    var tbodytag = $("<tbody />");
	                    for (var i = 1; i < rows.length; i++) {
	                        var row = $("<tr />");
	                        var cells = rows[i].split(",");
	                        for (var j = 0; j < cells.length; j++) {
	                            var cell = $("<td class='table-id'/>");
	                            cell.html(cells[j]);
	                            row.append(cell);
	                        }
	                        tbodytag.append(row);
	                    }
	                    table.append(tbodytag);
	                    $("#dvCSV").html('');
	                    $("#dvCSV").append(table);
	                }
	                reader.readAsText($("#fileUpload")[0].files[0]);
	                $scope.showModal = true;
	            } else {
	                alert("This browser does not support HTML5.");
	            }
	        } else {
	            alert("Please upload a valid CSV file.");
	        }
	};
	
	
	
	$("#hotelMenuBulkUploadForm").validate({
		  rules: {
			  fileUpload: "required",
		    },
		    errorElement: "div",
	    	errorPlacement: function(error, element) {
	    		element.before(error);
	      },
		    // Specify validation error messages
		    messages: {
		    	fileUpload: "Select File",
		    },
	    submitHandler: function(form) {
	     $("#divLoading").addClass('show');
	     var json = $('#dishes').tableToJSON();
	     var jsonReq = {};
	     jsonReq["bulkMenu"] = json;
	     var url = RESTONZACONSTANTS.url + "/uploadDishes";
	    	$http.post(url, jsonReq).success(function(data, status, headers, config) {
	    		$("#divLoading").removeClass('show');
	    		if (data.status == 'success') {
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
				} else {
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
				}
	   	  	}).error(function(data, status, headers, config) {
	   	  		$route.reload();
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
	
	
	//sample bulk menu template download
	$scope.downloadTemplate = function(){
		
	}
});