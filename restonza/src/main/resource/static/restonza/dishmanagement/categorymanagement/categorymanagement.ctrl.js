angular.module('restonza').controller('CategoryManagementController', function ($scope, $http, $filter, $route, RESTONZACONSTANTS) {
	  var json={};
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  $scope.catSelectedImage = null;
	  json['hotel_id'] = sessionVO.hotel_id;
	  // Fetch table details call
	  $("#divLoading").addClass('show');
	  
	  var json1 = {};
	  $http.post(RESTONZACONSTANTS.url+"/getStaticImageListing/dish", json1).success(function(data, status, headers, config) {
		  $scope.catImagesArray = data.response;
	  }).error(function(data, status, headers, config) {
	      
	  });
	  
	  $http.post(RESTONZACONSTANTS.url+"/getDishCategories", json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.categories = data.response;
		  $("#divLoading").removeClass('show');
		  $scope.statuses = [
		                     {value: 'true', text: 'Active'},
		                     {value: 'false', text: 'Inactive'}
		                   ];
		  
		  $scope.showStatus = function(category) {
		      var selected = [];
		      if(category.status) {
		        selected = $filter('filter')($scope.statuses, {value: category.status});
		      }
		      return selected.length ? selected[0].text : 'Inactive';
		    };
		    
		    $scope.removeCateogory = function(index) {
				var json = {};
				json["id"] = $scope.categories[index].category_id;
				var url = RESTONZACONSTANTS.url + "/deleteCategory";
				$http.post(url, json).success(function(data, status, headers, config) {
					console.log("success");
					 $scope.categories.splice(index, 1);
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
	  }).error(function(data, status, headers, config) {
	      $scope.table_count = "";
	  });
	  
	 
	  
	  $scope.updateCategorySequence = function() {
		  $("#divLoading").addClass('show');
		  var json = $('#dishCategory').tableToJSON();
		  var data = [];
		  for (var i=1;i<=json.length;i++) {
			  data.push(json[i-1].Id+" - "+i);
		  }
		  var json = {};
		  json['hotel_id'] = sessionVO.hotel_id;
		  json['updatedSequence'] = data;
		  $http.post(RESTONZACONSTANTS.url+"/updatedCategorySequence", json).success(function(data, status, headers, config) {
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
		  })
	  }
	  
	  // Add user call
	  $scope.addUser = function(categoryname,activeCat) {
		  $("#divLoading").addClass('show');
		  if (typeof categoryname  !== "undefined" && typeof activeCat  !== "undefined") {
			  var json={};
			  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
			  json['hotel_id'] = sessionVO.hotel_id;
			  json['category_name'] = categoryname;
			  json['status'] = activeCat;
			  json['imageURI'] = $scope.catSelectedImage;
			  $http.post(RESTONZACONSTANTS.url+"/addDishCategory", json).success(function(data, status, headers, config) {
			  $.bootstrapGrowl("Success" ,{type: 'success',delay: 2000,
		             offset: {from: "top",amount: 0},
		             align: "center",
		             allow_dismiss: false
		         });
			  $("#divLoading").removeClass('show'); 
			  $route.reload();
			  }).error(function(data, status, headers, config) {
			  });
		  } else {
			  alert("Please insert values");
			  $("#divLoading").removeClass('show');//TODO add genralised msg box
		  }
		 
        };
        
        $scope.closeCatAddModel = function() {
     	   $scope.showModal=false;
     	   $scope.catSelectedImage = null;
     	   $route.reload();
        }
        
        
        $scope.closeCatEditModel = function() {
      	   $scope.showCatEditModal=false;
      	   $scope.catSelectedImage = null;
         }
        
        $scope.openCategoryEditModel = function(category) {
        	$scope.showCatEditModal = true;
        	$scope.editCat = category.category_name;
        	$scope.editstatus = category.status == 'true' ? 'true' : 'false';
        	$scope.editImg = category.imageURI;
        	//$scope.catSelectedImage = category.imageURI;
        	$scope.editCategory_id = category.category_id;
        }
        
        
	      
	      $scope.editSave = function() {
	    	  var json={};
	    	  json['hotel_id'] = sessionVO.hotel_id;
			  json['category_id'] = $scope.editCategory_id;
			  json['category_name'] = $scope.editCat;
			  json['imageURI'] = $scope.catSelectedImage ==null ? $scope.editImg : $scope.catSelectedImage;
			  json['status'] =  $scope.editstatus;
			  $scope.closeCatEditModel();
			  $http.post(RESTONZACONSTANTS.url+"/updateDishCategory", json).success(function(data, status, headers, config) {
				  $route.reload();
				  $.bootstrapGrowl(data.response ,{type: 'success',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  }).error(function(data, status, headers, config) {
				  $.bootstrapGrowl(data.response ,{type: 'danger',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  });
	      }  
        /**
  	   * Coding for browse button
  	   */
  	  $scope.functionToBeExecuted = function(imgSrc) {
  		  $scope.catSelectedImage = imgSrc;
  		  $scope.showBrowseModal = false;
  	  };
  	  
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
           var model = $parse(attrs.fileModel);
           var modelSetter = model.assign;
           
           element.bind('change', function(){
              scope.$apply(function(){
                 modelSetter(scope, element[0].files[0]);
              });
           });
        }
     };
  }]);
