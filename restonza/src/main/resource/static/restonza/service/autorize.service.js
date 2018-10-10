angular.module('restonza').factory('Auth',function($rootScope, $location,$route ,$q, $http, RESTONZACONSTANTS) {
	return {
		checkAuthorize : function(permissionArray) {
			var deferred = $q.defer();
			var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			if (sessionVO != null) {
				if (sessionVO.isLoggedIn) {
					if ($.inArray(sessionVO.role, permissionArray)!== -1) {
						deferred.resolve();
					} else {
						deferred.reject();
						$rootScope.message = "You are not allowed to access requested URL";
						$location.url('/home');
					}
				} else {
					deferred.reject();
					$location.url('/login');
				}
			} else {
				deferred.reject();
				$location.url('/login');
			}
			return deferred.promise;
		},
		
		getRole : function() {
			var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			if (sessionVO != null) {
				return sessionVO.role;
			}
			return "";
		},
		
		doAuthenticate : function(username, password) {
			var sessionVO = {};
			var url = RESTONZACONSTANTS.url + "/doAuthorize";
	   		var json={};
	   		json["username"] = username;
	   		json["password"] = password;
	   		$http.post(url, json).success(function(data, status, headers, config) {
	   			if (data.status === 'success') {
	   				console.log("success");
		    		$rootScope.role = data.response.user_role;
		    		sessionVO["role"] = data.response.user_role;
		    		sessionVO["hotel_id"] = data.response.hotel_id;
		    		sessionVO["empid"] = data.response.empid;
		    		sessionVO["table_count"] = data.response.table_count;
		    		sessionVO["isLoggedIn"] = true;
		 			sessionStorage.setItem("sessionVO", JSON.stringify(sessionVO));
		 			sessionStorage.setItem("ordercounter", data.response.totalOrderCount);
		    		$location.url('/home');
				} else {
					sessionVO["isLoggedIn"] = false;
					sessionStorage.setItem("sessionVO", JSON.stringify(sessionVO));
					$rootScope.errormsg = "Login Incorrect! Please try again....."
		   	  		$location.url('/error');
				}
	   	  	}).error(function(data, status, headers, config) {
	   	  		$rootScope.errormsg = "Login Incorrect! Please try again....."
	   	  		$location.url('/error');
	   	  	});
		},
		
		logout : function() {
			$location.url('/login');
			//location.reload();
		}

	};

});