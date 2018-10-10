angular.module('restonza').controller('ManageUserController', function ($scope,$filter, $http, $route, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show');
	var json = {};
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var hotel_id = sessionVO.hotel_id;
	var empid = sessionVO.empid;
	json["id"] = empid;
	json["hotel_id"] = hotel_id;
	var url = RESTONZACONSTANTS.url + "/getAllUsers";
	$("#divLoading").addClass('show');
	$http.post(url, json).success(function(data, status, headers, config) {
		$("#divLoading").removeClass('show');
		$scope.users = data.response;
	}).error(function(data, status, headers, config) {
		$("#divLoading").removeClass('show');
		$scope.users = "";
	});
	
  $scope.validateField = function(data, fieldname) {
	    if (data == '' || data == undefined) {
	      return "Please Enter " + fieldname;
	    }
  };
  
  $scope.validateMobileField = function(data) {
	    if ((data == '' || data == undefined)) {
	      return "Please Enter valid mobile number";
	    } else if(data.length != 10) {
	    	return "Please Enter valid mobile number";
	    }
};
	  
  $scope.validateEmailField = function(data) {
	  var filter =  /^[\w-.+]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$/;
	    if (!filter.test(data)) {
	      return "Please Enter valid email address";
	    }
  };
  $scope.submitUserEntries = function(id, user, data) {
	  var url = RESTONZACONSTANTS.url + "/updateUserEntry";
	  $("#divLoading").addClass('show');
	  var json = {};
	  json["id"] = id;
	  json["password"] = data.password;
	  json["address"] = data.address;
	  json["salary"] = data.salary;
	  json["phonenumber"] = data.phonenumber;
	  json["email"] = data.email;
	  json["user_role"] = data.user_role;
	  json["status"] = data.status;
	  $http.post(url, json).success(function(data, status, headers, config) {
		  $("#divLoading").removeClass('show');
		  $.bootstrapGrowl('User Updated Successfully' ,{
	             type: 'success',
	             delay: 2000,
	             offset: {
	            	 from: "top",
	            	 amount: 0
	             },
	             align: "center",
	             allow_dismiss: false
	         });
		  	$state.reload();
		});
  };

  $scope.removeUser = function(index, id) {
		var json = {};
		var url = RESTONZACONSTANTS.url + "/deleteUser/" + id;
		$("#divLoading").addClass('show');
		$http.post(url, json).success(function(data, status, headers, config) {
			$("#divLoading").removeClass('show');
			$scope.users.splice(index, 1);
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
	
	/*dropdown start*/
	$scope.statuses = [
	                     {value: 'true', text: 'Active'},
	                     {value: 'false', text: 'Inactive'}
	                   ];
	  
	$scope.showStatus = function(user) {
	    var selected = [];
	    selected = $filter('filter')($scope.statuses, {value: user.status});
	    return selected.length ? selected[0].text : 'Not set';
	};
	
	$scope.roles = [
	                     {value: 'admin', text: 'Admin'},
	                     {value: 'waiter', text: 'Waiter'},
	                     {value: 'backkitchen', text: 'Kitchen Staff'}
	                   ];
	  
	$scope.showRoles = function(user) {
	    var selected = [];
	    if(user.user_role) {
	      selected = $filter('filter')($scope.roles, {value: user.user_role});
	    }
	    return selected.length ? selected[0].text : 'Not set';
	};
	/*dropdown ends*/
});