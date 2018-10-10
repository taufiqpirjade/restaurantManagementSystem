angular.module('restonza').controller('AddUserController', function ($scope, $http, $route, RESTONZACONSTANTS) {
	$scope.imageupload = false;	  
	var imgbase64img = "";
		  $("#startdate").datepicker({dateFormat: 'dd/mm/yy'});
	 	  $scope.role = ["admin","waiter","backkitchen","barstaff"];
		  $("#role").select2({
			  data: $scope.role
			});
		  $("#phonenumber").mask("9999999999");
		  
		  $(":file").change(function () {
		        if (this.files && this.files[0]) {
		            var reader = new FileReader();
		            reader.onload = imageIsLoaded;
		            reader.readAsDataURL(this.files[0]);
		        }
		    });
			
			function imageIsLoaded(e) {
				$scope.imageupload = true;
			    $('#myImg').attr('src', e.target.result);
			    imgbase64img = e.target.result;
			};
			
			//form validation started
		  $.validator.addMethod('filesize', function (value, element, param) {
			    return this.optional(element) || (element.files[0].size <= param)
			}, 'File size must be less than {0}');
			
			$("#adduser").validate({
			    // Specify validation rules
			    rules: {
			    	username: "required",
			    	password: "required",
			    	fname: "required",
			    	lname: "required",
			    	address: "required",
			    	phonenumber: "required",
			    	photo : {
		                extension: "jpg,jpeg,png",
		                filesize: 50*1024,
			    	},
			    	startdate: "required",
			    	salary: "required"
			    },
			    errorElement: "div",
		    	errorPlacement: function(error, element) {
		    		element.before(error);
                },
			    // Specify validation error messages
			    messages: {
			    	username: "Enter username",
			    	password: "Enter password",
			    	fname: "Enter First name",
			    	lname: "Enter Last name",
			    	address: "Enter Address",
			    	phonenumber: "Enter phone number",
			    	photo : {
		                extension: "Upload photo in jpeg or jpg or png format only",
		                filesize: "photo size should be less than 50 KB",
			    	},
			    	startdate: "Enter start date",
			    	salary: "Enter Salary details"
			    },
			    submitHandler: function(form) {
			    	var json = {};
			    	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			    	json["hotel_id"] = sessionVO.hotel_id;
			    	json["username"] = form.username.value;
			    	json["password"] = form.password.value;
			    	json["user_role"] = form.role.value;
			    	json["fullname"] = form.fname.value + " " + form.lname.value;
			    	json["email"] = form.email.value;
			    	json["address"] = form.address.value;
			    	json["phonenumber"] = form.phonenumber.value;
			    	json["dateofjoining"] = form.startdate.value;
			    	json["salary"] = form.salary.value;
			    	json["status"] = form.empstatus.value;
			    	 if ($scope.imageupload==true) {
			    		 json["emp_photo"] = imgbase64img.split(",")[1];
			    	 }
			    	console.log(JSON.stringify(json));
			    	var url = RESTONZACONSTANTS.url + "/addUser";
			    	console.log(JSON.stringify(json));
			    	$http.post(url, json).success(function(data, status, headers, config) {
			    		$('html,body').animate({ scrollTop: 0 }, 'slow');
			    		if (data.status == 'success') {
			    			$scope.showModal = false;
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