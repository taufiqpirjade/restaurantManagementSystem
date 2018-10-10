angular.module('restonza').controller('TaxSettingController', function ($scope,$filter, $route, $http, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var json = {};
	json["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getTaxsetting";
	$http.post(url, json).success(function(data, status, headers, config) {
		$scope.taxes = data.response;
	}).error(function(data, status, headers, config) {
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
	
	$scope.statuses = [
     {value: "true", text: 'Active'},
     {value: "false", text: 'Inactive'}
   ];
	
	$scope.isliquors = [
	                   {value: "true", text: 'Yes'},
	                   {value: "false", text: 'No'}
	                 ];
	/**
	 * Filtering status
	 */
	$scope.showStatus = function(tax) {
      var selected = [];
      if(tax.status) {
        selected = $filter('filter')($scope.statuses, {value: tax.status});
      }
      return selected.length ? selected[0].text : 'Inactive';
    };
    
    $scope.showIsLiquor = function(tax) {
        var selected = [];
        if(tax.isliquortax) {
          selected = $filter('filter')($scope.isliquors, {value: tax.isliquor});
        }
        return selected.length ? selected[0].text : 'No';
      };
    
	    /**
	     * Update tax setting in db.
	     */
	$scope.save = function(rowform,tax) {
		$("#divLoading").addClass('show');
		json["id"] = rowform.$data.id;
	  	json["hotel_id"] = sessionVO.hotel_id;
	  	json["taxname"] = rowform.$data.taxname;
	  	json["percentage"] = rowform.$data.percentage;
	  	json["status"] = rowform.$data.status;
	  	json["isliquor"] = rowform.$data.isliquortax;
		var url = RESTONZACONSTANTS.url + "/updateTaxsetting";
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
	  
	    
	/**
	 * For removing the Tax
	 */
	 $scope.removeTax = function(index) {
	    	$("#divLoading").addClass('show');
			var json = {};
			json["id"] = $scope.taxes[index].id;
			var url = RESTONZACONSTANTS.url + "/deleteTax";
			$http.post(url, json).success(function(data, status, headers, config) {
				$("#divLoading").removeClass('show');
				$scope.taxes.splice(index, 1);
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
	};
  
      $("#addtax").validate({
    	rules: {
	    	taxname: "required",
	    	percentage: "required",
	    	status: "required"
  	    },
  	    errorElement: "div",
      	errorPlacement: function(error, element) {
      		element.before(error);
        },
  	    // Specify validation error messages
  	    messages: {
  	    	taxname: "Enter valid tax name",
  	    	percentage: "Enter valid percentage",
  	    	status: "Please select status"
  	    },
  	  submitHandler: function(form) {
  		$("#divLoading").addClass('show');
  	  	json["hotel_id"] = sessionVO.hotel_id;
  	  	json["taxname"] = form.taxname.value;
  	  	json["percentage"] = form.percentage.value;
  	  	json["status"] = "true";
  	  	json["isliquor"] = form.optradio2.value;
  		var url = RESTONZACONSTANTS.url + "/saveTax";
  		 $scope.showModal = false;
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