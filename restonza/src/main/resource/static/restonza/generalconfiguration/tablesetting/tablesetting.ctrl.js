angular.module('restonza').controller('TableSettingController', function ($scope,$filter, $route, $http, RESTONZACONSTANTS/* $scope, $location, $http */) {
	$("#divLoading").removeClass('show');
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var json = {};
	json["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getTables";
	$http.post(url, json).success(function(data, status, headers, config) {
		$scope.tables = data.response;
	}).error(function(data, status, headers, config) {
		$scope.tables = "";
	});
  
  $scope.statuses = [
                     {value:'1', text: 'Inactive'},
                     {value:'2', text: 'Active'},
                   ];
  
  $scope.showStatus = function(table) {
      var selected = [];
      if(table.status) {
        selected = $filter('filter')($scope.statuses, {value: table.status});
      }
      return selected.length ? selected[0].text : 'Not set';
    };
      
      $scope.submitHandler = function(rowform,table_id) {
    	  $("#divLoading").addClass('show');
    	  var json = {};
	  	  	json["hotel_id"] = sessionVO.hotel_id;
	  	  	json["table_id"] = table_id;
	  	  	json["description"] = rowform.$data.description;
	  	  	if (rowform.$data.status == '1') {
	  	  		json["status"] = 'inactive';
			} else {
				json["status"] = 'active';
			}
	  	  	var url = RESTONZACONSTANTS.url + "/updateTable";
	  	  	$http.post(url, json).success(function(data, status, headers, config) {
	  	  		rowform.$submit();
	  	  		$("#divLoading").removeClass('show');
	  	  	}).error(function(data, status, headers, config) {
	  	  		console.log("error");
	  	  	$("#divLoading").removeClass('show');
	  	  	});
        };
  
  $scope.addTable = function() {
      $scope.inserted = {
        table_id: $scope.tables.length+1,
        descritpion: '',
        status: 'active',
      };
      
      $scope.tables.push($scope.inserted);
      	var json = {};
	  	json["hotel_id"] = sessionVO.hotel_id;
	  	json["table_id"] = $scope.inserted.table_id;
	  	json["description"] = $scope.inserted.descritpion;
	  	json["status"] = $scope.inserted.status;
	  	$("#divLoading").addClass('show');
	  	var url = RESTONZACONSTANTS.url + "/addTable";
	  	$http.post(url, json).success(function(data, status, headers, config) {
	  		$("#divLoading").removeClass('show');
	  		console.log("success");
	  	}).error(function(data, status, headers, config) {
	  		$("#divLoading").removeClass('show');
	  		console.log("error");
	  	});
    };

});