angular.module('restonza').controller('IngredientManagementController', function ($scope, $http, $filter, $route, RESTONZACONSTANTS/* $scope, $location, $http */) {
	  var json={};
	  $scope.showModal = false;
	  //added for sorting------------------------------------------------------------
	  $scope.sortType     = 'ingredients_name'; // set the default sort type
	  $scope.sortReverse  = false;  // set the default sort order
	  //added for searching ---------------------------------------------------------
	  $scope.filteredStatus = [];
	  $scope.filteredStatus.push({value: '', text: 'All'});
	  $scope.filteredStatus.push({value: true, text: 'Active'});
	  $scope.filteredStatus.push({value: false, text: 'Inactive'});
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel_id'] = sessionVO.hotel_id;
	  //default status-------------------------------------------------------------
	  $scope.statuses = [
	                     {value: true, text: 'Active'},
	                     {value: false, text: 'Inactive'}
	                   ];
	  $scope.showStatus = function(ingredient) {
	      var selected = [];
	      if(ingredient.status) {
	        selected = $filter('filter')($scope.statuses, {value: ingredient.status});
	      }
	      return selected.length ? selected[0].text : 'Inactive';
	    };  
  // After pressing add button------------------------------------------------------ 
  $scope.addIngredient = function(ingredientname,status) {
	  if (typeof ingredientname  !== "undefined" && typeof status  !== "undefined") { 
		  var json={};
		  json['hotel_id'] = sessionVO.hotel_id;
		  json['ingredient_name'] = ingredientname;
		  json['status'] = status;
		  $scope.showModal = false;
		  $("#divLoading").addClass('show');
		  $http.post(RESTONZACONSTANTS.url+"/addIngredients", json).success(function(data, status, headers, config) {
			  if (data.status == 'success') {
				  $scope.showModal = false;
				  $route.reload();
				  $("#divLoading").removeClass('show');
				  $.bootstrapGrowl(data.response ,{type: 'success',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  } else {
				  $scope.showModal = false;
				  $route.reload();
				  $("#divLoading").removeClass('show');
				  $.bootstrapGrowl(data.response ,{type: 'danger',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  }
			  
		  }).error(function(data, status, headers, config) {
			  alert("Server Error");
			  $("#divLoading").removeClass('show');
		  });
	  } else {
		  alert("Please insert values");
		  $("#divLoading").removeClass('show');
	  }
	  
	  };
	  //remove ingredient------------------------------------------------------------------------------------
	  $scope.removeIngredient = function(index) {
			var json = {};
			json["id"] = $scope.ingredients[index].ingredient_id;
			var url = RESTONZACONSTANTS.url + "/deleteIngredient";
			$http.post(url, json).success(function(data, status, headers, config) {
				console.log("success");
				$scope.ingredients.splice(index, 1);
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
				console.log("error");
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
	  
	  //validating form fields--------------------------------------------------------
	  $scope.validateField = function(data, fieldname) {
		    if (data.ingredients_name == '' || data.ingredients_name == undefined) {
		    	$.bootstrapGrowl('Ingredient name can not be left blank' ,{
		             type: 'danger',
		             delay: 2000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
		    	return ''
		    }
	    	var exists = $filter("checkIngredient")($scope.ingredients, data.ingredients_name, data.status);
	    	if (exists == true) {
	    		$.bootstrapGrowl('No Data change' ,{
		             type: 'danger',
		             delay: 2000,
		             offset: {
		            	 from: "top",
		            	 amount: 0
		             },
		             align: "center",
		             allow_dismiss: false
		         });
	    		return '';
	    	}
	  };
	  //------------------------------------------------------------------------------
	  $scope.changeIngredient = function(data, ingredient) {
		  var json={};
		  json['hotel_id'] = sessionVO.hotel_id;
		  json['ingredient_name'] = data.ingredients_name;
		  json['status'] =  data.status;
		  json['old_ingredient_id'] = ingredient.ingredient_id;
		  $("#divLoading").addClass('show');
		  $http.post(RESTONZACONSTANTS.url+"/updateIngredient", json).success(function(data, status, headers, config) {
			  if (data.status == 'success') {
				  $route.reload();
				  $("#divLoading").removeClass('show');
				  $.bootstrapGrowl(data.response ,{type: 'success',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  } else {
				  $scope.showModal = false;
				  $route.reload();
				  $("#divLoading").removeClass('show');
				  $.bootstrapGrowl(data.response ,{type: 'danger',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  }
			  
		  }).error(function(data, status, headers, config) {
			  $("#divLoading").removeClass('show');
			  $.bootstrapGrowl('Unexpected Error Occurred! Please call adminstrator' ,{
				  	 type: 'danger',delay: 2000,
		             offset: {from: "top",amount: 0},
		             align: "center",
		             allow_dismiss: false
		         });
		  });
	  };
	  
	//pagination--------------------------------------------------------------------------------
 	$scope.maxSize = 5;     // Limit number for pagination display number.  
    $scope.totalCount = 0;  // Total number of items in all pages. initialize as a zero  
    $scope.pageIndex = 1;   // Current page number. First page is 1.-->  
    $scope.pageSizeSelected = 10; // Maximum number of items per page.  
    $scope.prop = {   "type": "select", 
    	    "name": "pages",
    	    "value": "20", 
    	    "values": ["20","40","60"] 
    };
    $scope.ingredients = [];
    $scope.filterIngredient = '';
    $scope.filterstatus = '';
    $scope.getIngredientList = function () {
    	//page initialization ------------------------------------------------------
		$("#divLoading").addClass('show');
    	var json = {};
    	json['hotel_id'] = sessionVO.hotel_id;
    	var url = RESTONZACONSTANTS.url + "/getIngredientsTable";
	    $http.post(url,json).then(function (response) { 
				            	   $("#divLoading").removeClass('show');
				            	   if(response.data.status == 'success') {
				            		   $scope.ingredients = response.data.response.ingredient;
				            		   $scope.totalCount = $scope.ingredients.length;
				            		   return $filter('filter')( $scope.ingredients, $scope.filterIngredient, $scope.filterstatus);
				            	   }
				            	    
				               },  
				               function (err) {  
				            	   $("#divLoading").removeClass('show');
				                   var error = err; 
				               });  
    }  
  
    $scope.$watch('filterIngredient', function(newValue,oldValue){
    	if(oldValue!=newValue){
    		$scope.pageIndex = 1;
      }
    },true);
    
    $scope.$watch('filterstatus', function(newValue,oldValue){
    	if(oldValue!=newValue){
    		$scope.pageIndex = 1;
      }
    },true);
    
    //Loading ingredient list on first time  
    $scope.getIngredientList();  
  
    //This method is calling from pagination number  
    $scope.pageChanged = function () {  
    	//currentPage = $scope.pageIndex -1;
    };  
  
    //This method is calling from dropDown  
    $scope.changePageSize = function () {  
        $scope.pageIndex = 1;  
        $scope.getIngredientList();  
    };
	
    
    $scope.sort = function (sortBy) {
        $scope.columnToOrder = sortBy;
        $scope.sortReverse = !$scope.sortReverse;
        //$Filter - Standard Service
        $scope.ingredients = $filter('orderBy')($scope.ingredients, $scope.columnToOrder, $scope.sortReverse);
    };
    
    $scope.sort('ingredients_name');
}).filter('startFrom', function() {
    return function(input, start) {
        start = +start;
        return input.slice(start);
    }
});






