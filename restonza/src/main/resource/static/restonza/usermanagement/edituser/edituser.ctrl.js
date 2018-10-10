angular.module('restonza').controller('EditUserController', function ($scope, $http, $route, RESTONZACONSTANTS) {
	$("#phonenumber").mask("9999999999");
	var updatejson = {};
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	updatejson["id"] = sessionVO.empid;
	var url = RESTONZACONSTANTS.url + "/getUser";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.response = data.response;
		$('#phonenumber').val(data.response.phonenumber).trigger('keyup');
	}).error(function(data, status, headers, config) {
		$scope.response = "";
	});
	//form validation started
	$("#edituser").validate({
	    // Specify validation rules
	    rules: {
	    	newpassword: "required",
	    	confirmpassword: {
	    		equalTo: "#newpassword"
	    	},
	    	email: "required",
	    	address: "required",
	    	phonenumber: "required",
	    },
	    errorElement: "div",
		errorPlacement: function(error, element) {
			element.before(error);
	    },
	    // Specify validation error messages
	    messages: {
	    	newpassword: "Enter new password",
	    	confirmpassword: {
	    		equalTo: "Entered password does not match with new password"
	    	},
	    	email: "Enter email address",
	    	address: "Enter Address",
	    	phonenumber: "Enter phone number",
	    },
	    submitHandler: function(form) {
	    	var json= {};
	    	var json = {};
	    	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	    	json["id"] = sessionVO.empid;
	    	json["password"] = form.newpassword.value;
	    	json["email"] = form.email.value;
	    	json["address"] = form.address.value;
	    	json["phonenumber"] = form.phonenumber.value;
	    	console.log(JSON.stringify(json));
	    	var url = RESTONZACONSTANTS.url + "/updateUser";
	    	console.log(JSON.stringify(json));
	    	$http.post(url, json).success(function(data, status, headers, config) {
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