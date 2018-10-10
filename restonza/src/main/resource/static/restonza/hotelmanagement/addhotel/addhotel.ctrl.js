angular.module('restonza').controller('AddHotelManagementController', function ($scope,$filter, $http, $route, RESTONZACONSTANTS) {
	$scope.imageupload = false;
	$("#divLoading").removeClass('show');
	//Others js validation
	$("#startdate").datepicker({dateFormat: 'dd/mm/yy'});  
	var imgbase64img = "";
	//Need to create db call to fetch all hotel types
	var hoteltypes = ["Ethnic", "Fast Food", "Fast Casual", "Casual dining", "Family style", "Fine dining", "Pub", "Cafeteria", "Table Top"];
	
	var currencyTypes = ["CHF", "EUR", "INR", "USD"];
	$("#currencyTypes").select2({
	  data: currencyTypes
	});
	
	$("#hoteltype").select2({
		  data: hoteltypes
		});
	
	//Hotel subscription plan
	//db to get all the values of subscription plan
	var hotelsubscription = ["3months-699", "6months-1199", "12months-1899"];
	$("#hotelsubscription").select2({
	  data: hotelsubscription
	});
	//hoteladminphone and hoteladminaadhar
	$("#hoteladminphone").mask("9999999999");
	
	//image upload functionality
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
	
	//hotel form validation
	$.validator.addMethod('filesize', function (value, element, param) {
	    return this.optional(element) || (element.files[0].size <= param)
	}, 'File size must be less than {0}');
	
	$("#addhotelform").validate({
	    // Specify validation rules
	    rules: {
	    	hotelname: "required",
	    	hoteltype: "required",
	    	currencyTypes: "required",
	    	hoteladminname: "required",
	    	hoteladminphone: "required",
	    	address: "required",
	    	username:"required",
	    	password:"required",
	    	email:"required",
	    	hoteladminphoto : {
                extension: "jpg,jpeg,png",
                filesize: 50*1024,
	    	},
	    	hotelsubscription: "required",
	    	startdate: "required"
	    },
	    errorElement: "div",
    	errorPlacement: function(error, element) {
    		element.before(error);
      },
	    // Specify validation error messages
	    messages: {
	    	hotelname: "Enter Hotel name",
	    	hoteltype: "Enter Hotel type",
	    	currencyTypes: "Please select currency type",
	    	hoteladminname: "Enter username",
	    	hoteladminphone: "Enter admin phone",
	    	address: "Enter Hotel address",
	    	username:"Enter User name",
	    	password:"Enter Password",
	    	email:"Enter Email ID",
	    	hoteladminphoto : {
                extension: "Upload photo in jpeg or jpg or png format only",
                filesize: "banner size should be less than 50 KB",
	    	},
	    	hotelsubscription: "Please select hotel subscription plan",
	    	startdate: "Please select subscription plan start date"
	    },
	    submitHandler: function(form) {
	     $("#divLoading").addClass('show');
	     var json= {};
    	 json["hotelname"] = form.hotelname.value;
    	 json["hoteltype"] = form.hoteltype.value;
    	 json["currencyType"] = form.currencyTypes.value;
    	 json["hoteladminname"] = form.hoteladminname.value;
    	 json["username"] = form.username.value;
	     json["password"] = form.password.value;
	     json["email"] = form.email.value;
    	 json["hoteladminphone"] = form.hoteladminphone.value;
    	 json["address"] = form.address.value;
    	 if ($scope.imageupload==true) {
    		 json["hoteladminphoto"] = imgbase64img.split(",")[1];
    	 }
    	 json["hotelsubscription"] = form.hotelsubscription.value;
    	 json["startdate"] = form.startdate.value;
    	 json["status"] = form.hotelstatus.value;
    	 json["barPriviledege"] = form.liquarPrivilege.value;
    	 console.log(JSON.stringify(json));
	    	var url = RESTONZACONSTANTS.url + "/addHotel";
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