angular.module('restonza').controller('CampaignsApprovalManagementController', function ($scope,$filter,$http,$route,RESTONZACONSTANTS) {

	var updatejson = {};
	var url = RESTONZACONSTANTS.url + "/getApprovalCampaigns";
	$http.post(url, updatejson).success(function(data, status, headers, config) {
		$scope.campaigns = data.response;
		$scope.statuses = [
		                   {value: 'true', text: 'Approved'},
		                   {value: 'false', text: 'Pending Approval'}
		                 ];
		  	  $scope.showStatus = function(campaign) {
		  	      var selected = [];
		  	      if(campaign.status) {
		  	        selected = $filter('filter')(
		  	        		$scope.statuses, {
		  	        			value: campaign.status
		  	        			});
		  	      }
		  	      return selected.length ? selected[0].text : 'Pending Approval';
		  	    };
    }).error(function(data, status, headers, config) {
    	$scope.campaigns = "";
	});
	
	
			
	$scope.approveCampaign = function(id,hotelid,campaignid) {
		var updatejson = {};
		updatejson["campaign_id"] = id;
		updatejson['hotel_id'] = hotelid;
		$("#divLoading").addClass('show');
		var url = RESTONZACONSTANTS.url + "/approveCampaign";
		$http.post(url, updatejson).success(function(data, status, headers, config) {
			 $route.reload();
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