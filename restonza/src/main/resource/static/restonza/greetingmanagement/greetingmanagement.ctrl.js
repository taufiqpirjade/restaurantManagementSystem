angular.module('restonza').controller('GreetingsManagementController', function ($scope, $route, $http, $filter, RESTONZACONSTANTS/* $scope, $location, $http */) {
	 $("#divLoading").removeClass('show');
	$('#addgreetings')[0].reset();
	var imgbase64img = "";
	$scope.imageUpload = false;
	$("#greetingDate").datepicker({dateFormat: 'dd/mm/yy'});
	
	$(":file").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = imageIsLoaded;
            reader.readAsDataURL(this.files[0]);
        }
    });
	
	function imageIsLoaded(e) {
	    $('#myImg').attr('src', e.target.result);
	    imgbase64img = e.target.result;
	    $scope.imageUpload = true;
	};
	
	var updatejson = {};
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	updatejson["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getGreetingDetails";
	 $("#divLoading").addClass('show');
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.greetings = data.response;
		$("#divLoading").removeClass('show');
	}).error(function(data, status, headers, config) {
		$scope.greetings = "";
		$("#divLoading").removeClass('show');
	});
	
  $scope.statuses = [
                     {value: '2', text: 'Active'},
                     {value: '1', text: 'Inactive'}
                   ];
  
  
  $scope.showStatus = function(greeting) {
      var selected = [];
      if(greeting.status) {
        selected = $filter('filter')($scope.statuses, {value: greeting.status});
      }
      return selected.length ? selected[0].text : 'Not set';
    };
    
    $scope.removeGreeting = function(index) {
    	 $("#divLoading").addClass('show');
		var json = {};
		json["id"] = index;
		var url = RESTONZACONSTANTS.url + "/deleteGreeting";
		$http.post(url, json).success(function(data, status, headers, config) {
			$scope.showModal = false;
			$("#divLoading").removeClass('show');
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
  
      $scope.editGreeting = function(index) {
        var selectedGreeting = $scope.greetings[index];
        var statusmsg = "";
		if (selectedGreeting.status == '1') {
			statusmsg = "Pending Approval";
		} else {
			statusmsg = "Approved";
		}
        $scope.editdata = {
        		"greetingname": selectedGreeting.greetingname,
        		"description": selectedGreeting.description,
        		"greetingdate": selectedGreeting.greetingdate,
        		"greetingimg": selectedGreeting.greetingimg,
        		"status": statusmsg
        }
        $scope.showModal = true;
      };
      
      //ads for validation
      $.validator.addMethod('filesize', function (value, element, param) {
		    return this.optional(element) || (element.files[0].size <= param)
		}, 'File size must be less than {0}');
		
		$("#addgreetings").validate({
		    // Specify validation rules
		    rules: {
		    	greetingname: "required",
		    	banner : {
	                extension: "jpg,jpeg,png",
	                filesize: 50*1024,
		    	},
		    	greetingDate: "required"
		    },
		    errorElement: "div",
	    	errorPlacement: function(error, element) {
	    		element.before(error);
          },
		    // Specify validation error messages
		    messages: {
		    	greetingname: "Enter greetingname",
		    	banner : {
	                extension: "Upload photo in jpeg or jpg or png format only",
	                filesize: "banner size should be less than 50 KB",
		    	},
		    	greetingDate: "Enter Greeting Date"
		    },
		    submitHandler: function(form) {
		    	 $("#divLoading").addClass('show');
		    	 $scope.showModal = false;
		    	var json = {};
				json["greetingname"] = form.greetingname.value;
				json["description"] = form.description.value;
				json["greetingdate"] = form.greetingDate.value;
				if (form.imgsrc.currentSrc != '' ||  form.imgsrc.currentSrc != undefined) {
					json["greetingimg"] = form.imgsrc.currentSrc.split(",")[1];
				}
				json["status"] = form.greetingstatus.value;
				var url = RESTONZACONSTANTS.url + "/addGreetingDetails";
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
				    $('#addgreetings')[0].reset();
				    $route.reload();
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
					$route.reload();
				});
		    }
		  });
		
		$scope.resetAll = function() {
			$scope.showModal = false;
			$scope.editdata = {};
			$('#addgreetings')[0].reset();
		};
});