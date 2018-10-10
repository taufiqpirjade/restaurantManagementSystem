angular.module('restonza').controller('BarCategoryController', function ($scope, $http, $filter, $route, RESTONZACONSTANTS) {
	  var json={};
	  $scope.barcategory = {};   
  	  $scope.barsubcategories = [];
  	  $scope.updatebarcategory = {};
  	  $scope.editshowModal = false;
  	  
  	 //added for sorting------------------------------------------------------------
	  $scope.sortType     = 'bar_category_name'; // set the default sort type
	  $scope.sortReverse  = false;  // set the default sort order
	  //added for searching ---------------------------------------------------------
	  $scope.filteredStatus = [];
	  $scope.filteredStatus.push({value: '', text: 'All'});
	  $scope.filteredStatus.push({value: true, text: 'Active'});
	  $scope.filteredStatus.push({value: false, text: 'Inactive'});
  	  $scope.selectedImage = null;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  // Fetch table details call
	  $("#divLoading").addClass('show');
	  $http.post(RESTONZACONSTANTS.url+"/getBarCategories/" + sessionVO.hotel_id, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.barcategories = data.response;
		  
	  });
	  
	  var json1 = {};
	  $http.post(RESTONZACONSTANTS.url+"/getStaticImageListing/bar", json1).success(function(data, status, headers, config) {
		  $scope.barcategoryimagesarray = data.response;
	  }).error(function(data, status, headers, config) {
	      
	  }); 
	  
	  $("#divLoading").removeClass('show');
		  $scope.statuses = [
		                     {value: true, text: 'Active'},
		                     {value: false, text: 'Inactive'}
		                   ];
		  
		  $scope.showStatus = function(category) {
		      var selected = [];
		      if(category.status) {
		        selected = $filter('filter')($scope.statuses, {value: category.status});
		      }
		      return selected.length ? selected[0].text : 'Inactive';
		    };
		    
		    $scope.removeBarCateogory = function(index) {
		    	$("#divLoading").addClass('show');
				var json = {};
				var id = index.id;
				var url = RESTONZACONSTANTS.url + "/deleteBarCategory/" + id;
				$http.post(url, json).success(function(data, status, headers, config) {
					$("#divLoading").removeClass('show');
					 $scope.barcategories.splice(index, 1);
					 $route.reload();
					$.bootstrapGrowl(data.response ,{
			             type: data.status,
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
			             type: data.status,
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
		      
	  $scope.addNew = function(barsubcategory){
		  if(barsubcategory != null && barsubcategory !="") {
			  var selectedCategory = barsubcategory;
			  var hasFoo = false;
			  $.each($scope.barsubcategories, function(i,obj) {
			    if (obj === selectedCategory ) {
			    	hasFoo = true; 
			    	return false;
			    }
			  });  
			  if (hasFoo) {
				  $id='#'+selectedCategory+'tr';
				  $($id).addClass("invalid");
				$("#errormsg").text("Bar Sub Category already present");
				setTimeout(function(){
					$($id).removeClass("invalid");
					$("#errormsg").text("");
				}, 1500);
			  } else {
				  if(typeof selectedCategory != "undefined" && selectedCategory != "") {
				  $scope.barsubcategories.push(selectedCategory);
				  }
			  }
			  $scope.barsubcategory = null;
		  }
        }; 
        
        $scope.removeCategory = function(index) {
        	 $scope.barsubcategories.splice(index, 1);
        }
        
        $scope.uploadFile = function(id,type) {
        	if ($scope.fileToUpload) {
        		var file = $scope.fileToUpload;
                console.log('file is ' );
                console.dir(file);
                var uploadUrl = "/restonza/mobileimage/uploadCategoryPic/"+type+"/"+id;
                //$scope.fileUpload.uploadFileToUrl(file, uploadUrl);
                var fd = new FormData();
                fd.append('file', file);
                $http.post(uploadUrl, fd, {
                   transformRequest: angular.identity,
                   headers: {'Content-Type': undefined}
                })
                $scope.fileToUpload = null;
        	} else {
        		console.log("No Image to upload");
        	}
            
         };
        
    	  $scope.addBarCategories = function() {
    		  $scope.showModal = false;
    		  $("#divLoading").addClass('show');
    		     var json= {};
    	    	 json["hotel_id"] = sessionVO.hotel_id;
    	    	 json["bar_category_name"] = $scope.barcategory.barcategoryname;
    	    	 json["bar_sub_category"] = $scope.barsubcategories;
    	    	 json["image"] = $scope.selectedImage;
    	    	 json["status"] = $scope.barcategory.status== 'active' ? true : false;
    	    	 var url = RESTONZACONSTANTS.url + "/addBarCategory";
    		    	$http.post(url, json).success(function(data, status, headers, config) {
    		    		// Call to Add Image
    		    		//$scope.uploadFile(data.response.id,"bar");
    		    		$('html,body').animate({ scrollTop: 0 }, 'slow');
    					    $route.reload();
    					    $scope.clearAll();
    					    $.bootstrapGrowl("Updated Successfully" ,{
    				             type: "Success",
    				             delay: 2000,
    				             offset: {
    				            	 from: "top",
    				            	 amount: 0
    				             },
    				             align: "center",
    				             allow_dismiss: false
    				         });
    		   	  	}).error(function(data, status, headers, config) {
    		   	  		$scope.showModal = true;
    		   	  		$("#divLoading").removeClass('show');
	    		   	  	$.bootstrapGrowl(data.response ,{
				             type: data.status,
				             delay: 2000,
				             offset: {
				            	 from: "top",
				            	 amount: 0
				             },
				             align: "center",
				             allow_dismiss: false
				         });
    		   	  	});
    		    	$scope.selectedImage = null;    	
    		    	
    	  };
    	  
    	  $scope.clearAll = function() {
    		  $scope.barcategory = {};   
    	  	  $scope.barsubcategories = [];
    	  	  $scope.updatebarcategory = {};   
    	  }
    	  
    	  $scope.updateBarCategorySequence = function() {
    		  $("#divLoading").addClass('show');
    		  var json = $('#barcategory').tableToJSON();
    		  var data = [];
    		  for (var i=1;i<=json.length-1;i++) {
    			  data.push(json[i].Id+" - "+i);
    		  }
    		  var json = {};
    		  json['hotel_id'] = sessionVO.hotel_id;
    		  json['updatedSequence'] = data;
    		  $http.post(RESTONZACONSTANTS.url+"/updatedBarCategorySequence", json).success(function(data, status, headers, config) {
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
    	  
    	  $scope.openEditableModal = function(index) {
    		  $scope.clearAll();
    		  //fill the modal values
    		  $scope.updatebarcategory = index;
    		  /*if($scope.updatebarcategory.bar_sub_category != "") {
    			  $scope.barsubcategories = $scope.updatebarcategory.bar_sub_category.split(',');
    		  }*/
    		  $scope.updatebarcategory.status = $scope.updatebarcategory.status == true ? 'active' : 'inactive';
    		  $scope.editshowModal = true;
    	  }
    	  
    	  /**
    	   * Coding for browse button
    	   */
    	  $scope.functionToBeExecuted = function(imgSrc) {
    		  $scope.selectedImage = imgSrc;
    		  $scope.showBrowseModal = false;
    	  };
    	  
    	  $scope.showAddCategoryPopup = function() {
    		  $scope.showModal = true;
    		  $scope.selectedImage = null; // to set blank on every set opration
    	  }
    	  
    	  $scope.closeAddModel = function() {
    		  $scope.showModal = false;
    		  $scope.selectedImage = null;
    		  $route.reload();
    	  }
    	  
    	  
    	  $scope.updateBarCategories = function() {
    		  $scope.editshowModal = false;
    		  $("#divLoading").addClass('show');
    		     var json= {};
    	    	 json["hotel_id"] = sessionVO.hotel_id;
    	    	 json["bar_category_id"] = $scope.updatebarcategory.id;
    	    	 json["bar_category_name"] = $scope.updatebarcategory.bar_category_name;
    	    	 json["image"] = $scope.selectedImage == null ? $scope.updatebarcategory.img_uri : $scope.selectedImage;
    	    	 json["sequence"] = $scope.updatebarcategory.sequence;
    	    	 /*json["bar_sub_category"] = $scope.barsubcategories;*/
    	    	 json["status"] = $scope.updatebarcategory.status== 'active' ? true : false;
    	    	 var url = RESTONZACONSTANTS.url + "/updateBarCategory";
    		    	$http.post(url, json).success(function(data, status, headers, config) {
    		    		$('html,body').animate({ scrollTop: 0 }, 'slow');
    					    $route.reload();
    					    $scope.clearAll();
    					    $.bootstrapGrowl("Success !" ,{
    				             type: data.status,
    				             delay: 2000,
    				             offset: {
    				            	 from: "top",
    				            	 amount: 0
    				             },
    				             align: "center",
    				             allow_dismiss: false
    				         });
    		   	  	}).error(function(data, status, headers, config) {
    		   	  		$scope.editshowModal = true;
    		   	  		$("#divLoading").removeClass('show');
	    		   	  	$.bootstrapGrowl(data.response ,{
				             type: data.status,
				             delay: 2000,
				             offset: {
				            	 from: "top",
				            	 amount: 0
				             },
				             align: "center",
				             allow_dismiss: false
				         });
    		   	  	});
    		    	//$scope.uploadFile($scope.updatebarcategory.id,"bar");
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
  }])/*.service('fileUpload', ['$http', function ($http) {
      $scope.uploadFileToUrl = function(file, uploadUrl){
          var fd = new FormData();
          fd.append('file', file);
       
          $http.post(uploadUrl, fd, {
             transformRequest: angular.identity,
             headers: {'Content-Type': undefined}
          })
       
          .success(function(){
          })
       
          .error(function(){
          });
       }
    }]);*/;
