angular.module('restonza').controller('AdsApprovalManagementController', function ($scope,$filter,$http,$route,RESTONZACONSTANTS) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var updatejson = {};
	var url = RESTONZACONSTANTS.url + "/getPublishedAds";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.ads = data.response;
    }).error(function(data, status, headers, config) {
    	$scope.ads = "";
	});
	
	
	// To get the my campaigns data
	var myCampaignsJson = {};
	myCampaignsJson['hotel_id'] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/myCampaigns";
	$http.post(url, myCampaignsJson).success(function(data, status, headers, config) {
		$scope.mycampaigns = data.response;
		$scope.statuses = [
		                   {value: 'true', text: 'Approved'},
		                   {value: 'false', text: 'Pending Approval'}
		                 ];
		  	  $scope.showStatus = function(mycampaign) {
		  	      var selected = [];
		  	      if(mycampaign.status) {
		  	        selected = $filter('filter')(
		  	        		$scope.statuses, {
		  	        			value: mycampaign.status
		  	        			});
		  	      }
		  	      return selected.length ? selected[0].text : 'Pending Approval';
		  	    };
    }).error(function(data, status, headers, config) {
    	$scope.mycampaigns = "";
	});
	
	
	
	// For requesting ad---------------------
	$scope.requestAd = function(id,name,price,description) {
		var updatejson = {};
		updatejson["campaign_id"] = id;
		updatejson['hotel_id'] = sessionVO.hotel_id;
		updatejson['campaign_name'] = name;
		updatejson['price'] = price;
		updatejson['description'] = description;
		var url = RESTONZACONSTANTS.url + "/requestCampaign";
		$http.post(url, updatejson).success(function(data, status, headers, config) {
			 $route.reload();
	   		 $.bootstrapGrowl(data.response ,{
		             type: 'success',
		             delay: 2000,
		             offset: {
		            	 from: "center",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
	    }).error(function(data, status, headers, config) {
	   		 $.bootstrapGrowl(data.response ,{
		             type: 'center',
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