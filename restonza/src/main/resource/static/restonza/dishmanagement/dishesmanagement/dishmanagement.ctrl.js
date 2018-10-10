angular.module('restonza').controller('DishManagementController',function ($scope, $http, $filter, $route, RESTONZACONSTANTS) {
	$("#divLoading").removeClass('show'); 
	$scope.sortType = 'dish_name'; // set the default sort type
	$scope.sortReverse  = false;
	var json={};
	$scope.dishimageupload = false;
	$scope.imageupload = false;
	$scope.foodSelectedImage = null;
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  json['hotel'] = sessionVO.hotel_id;
	  // Fetch table details call
	  $scope.showModal = false;
	  $("#divLoading").addClass('show');
	  $('#dishModal').modal('hide');
	  $http.post(RESTONZACONSTANTS.url+"/getDishes", json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.dishes = data.response;
		  $("#divLoading").removeClass('show');
		  $scope.statuses = [
		                     {value: "true", text: 'Active'},
		                     {value: "false", text: 'Inactive'}
		                   ];
		  
		  $scope.vegstatuses = [
              {value: "true", text: 'YES'},
              {value: "false", text: 'NO'}
            ];
		  $scope.showStatus = function(dish) {
		      var selected = [];
		      if(dish.status) {
		        selected = $filter('filter')($scope.statuses, {value: dish.status});
		      }
		      return selected.length ? selected[0].text : 'Inactive';
		    };
		    
		    $scope.showVegNonVeg = function(dish) {
			      var selected = [];
			      if(dish.hot) {
			        selected = $filter('filter')($scope.vegstatuses, {value: dish.hot});
			      }
			      return selected.length ? selected[0].text : 'NO';
			};
		    
		    $scope.removeDish = function(index) {
		    	$("#divLoading").addClass('show');
				var json = {};
				//json["id"] = $scope.dishes[index].dish_id;
				json["id"] = index.dish_id;
				var url = RESTONZACONSTANTS.url + "/deleteDish";
				$http.post(url, json).success(function(data, status, headers, config) {
					$("#divLoading").removeClass('show');
					$scope.dishes.splice(index, 1);
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
	      $scope.save = function(rowform,dish) {
	    	  var json={};
	    	  json['hotel'] = sessionVO.hotel_id;
	    	  json['dish_id'] = dish.dish_id;
	    	  var modified_dish_name = rowform.$data.name;
	    	  var modified_dish_description = rowform.$data.description;
	    	  var modified_dish_price = rowform.$data.price;
	    	  var modified_discount = rowform.$data.discount;
	    	  var modified_status = rowform.$data.status;
	    	  
	    	  if (typeof modified_dish_name  == "undefined" || modified_dish_name == "") {
	    		  modified_dish_name = dish.dish_name;
	    	  } 
	    	  if (typeof modified_dish_description  == "undefined" || modified_dish_description  == "") {
	    		  modified_dish_description = dish.dish_description;
	    	  }
	    	  if (typeof modified_dish_price  == "undefined" || modified_dish_price  == "") {
	    		  modified_dish_price = dish.dish_price;
	    	  }
	    	  if (typeof modified_discount  == "undefined" || modified_discount  == "") {
	    		  modified_discount = 0;
	    	  } 
	    	  if (typeof modified_status  == "undefined" || modified_status  == "") {
	    		  modified_status = dish.status;
	    	  }
	    	  
	    	  $("#divLoading").addClass('show');
			  json['dish_name'] = modified_dish_name;
			  json['dish_description'] = modified_dish_description;
			  json['dish_price'] = modified_dish_price;
			  json['discount'] = modified_discount;
			  json['status'] = modified_status;
			  json['imageURI'] = $scope.foodSelectedImage = 'null' ? $scope.editImg : $scope.foodSelectedImage;
			  
			  $http.post(RESTONZACONSTANTS.url+"/updateDish", json).success(function(data, status, headers, config) {
				  $("#divLoading").removeClass('show');
				  $route.reload();
				  $.bootstrapGrowl(data.response ,{type: 'success',delay: 2000,
			             offset: {from: "top",amount: 0},
			             align: "center",
			             allow_dismiss: false
			         });
			  }).error(function(data, status, headers, config) {
			  });
			  if ($scope.dishimageupload) {
				  $scope.uploadFile(dish.dish_id,"dishimg");
			  }
	      }     
	  }).error(function(data, status, headers, config) {
	  });
	  
	  var json1 = {};
	  $http.post(RESTONZACONSTANTS.url+"/getStaticImageListing/dish", json1).success(function(data, status, headers, config) {
		  $scope.foodItemImagesArray = data.response;
	  }).error(function(data, status, headers, config) {
	      
	  });
	  
	  $scope.updateSequencing = function(dishes) {
		  $("#divLoading").addClass('show');
		  var json = $('#dishes').tableToJSON();
		  var data = [];
		  for (var i=1;i<json.length;i++) {
			  data.push(json[i].Id+" - "+i);
		  }
		  var json = {};
		  json['hotel'] = sessionVO.hotel_id;
		  json['updatedSequence'] = data;
		  $http.post(RESTONZACONSTANTS.url+"/updatedDishSequence", json).success(function(data, status, headers, config) {
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
	  
	  $scope.downloadMenu = function() {
		  var json = {};
		  json['hotelname'] = sessionVO.hotel_id;
		  $http.post(RESTONZACONSTANTS.url+"/getHotelMenuPdfDownload/dish", json,{responseType: 'arraybuffer'}).success(function(data, status, headers, config) {
			  var file = new Blob([ data ], {
	                type : 'application/pdf'
	            });
	          var isChrome = !!window.chrome && !!window.chrome.webstore;
              var isIE = /*@cc_on!@*/false || !!document.documentMode;
              var isEdge = !isIE && !!window.StyleMedia;

              if (isChrome){
                  var url = window.URL || window.webkitURL;
                  var downloadLink = angular.element('<a></a>');
                  downloadLink.attr('href',url.createObjectURL(file));
                  downloadLink.attr('target','_self');
                  downloadLink.attr('download', 'Hotel Menu Report.pdf');
                  downloadLink[0].click();
              }
              else if(isEdge || isIE){
                  window.navigator.msSaveOrOpenBlob(file,'Hotel Menu Report.pdf');
              }
              else {
                  var fileURL = URL.createObjectURL(file);
                  window.open(fileURL);
              }
              $("#divLoading").removeClass('show');
			  
			  $.bootstrapGrowl("Menu Will be downloded Soon !" ,{type: 'success',delay: 2000,
		             offset: {from: "top",amount: 0},
		             align: "center",
		             allow_dismiss: false
		         });  
		  }).error(function(data, status, headers, config) {
			  $.bootstrapGrowl("Error in downloading Menu" ,{type: 'danger',delay: 2000,
		             offset: {from: "top",amount: 0},
		             align: "center",
		             allow_dismiss: false
		         }); 
		  });
	  };
	  // Will be called when modal popup opens 
	  $scope.openModel = function() {
		  $scope.imageupload = false;
		  var json = {};
		  json['hotel'] = sessionVO.hotel_id;
		  $http.post(RESTONZACONSTANTS.url+"/getCategoriesAndIngredients", json).success(function(data, status, headers, config) {
			  $scope.modelCategory = data.response.categories;
			  $scope.modelIngredients = data.response.ingredients;
			  $("#modelIngredients").select2({
				  data: $scope.modelIngredients
				});
			  $("#modelCategory").select2({
				  data: $scope.modelCategory
				});
		  }).error(function(data, status, headers, config) {
		      $scope.table_count = "";
		  });
	  };
	  
	  /**
	   * To open the edit panel
	   */
	  
		  $scope.openEditDishModel = function(index) {
			  $scope.showEditModal = true;
			  //var defaultIngredient = {};
			  $scope.editdishid =  index.dish_id;
			  $scope.ingredients = index.dish_ingredients;
			  defaultIngredientdataarray= index.dish_ingredients.replace(/, /g, ",");
			  var abc = defaultIngredientdataarray.replace(/\[/g, '');
			  defaultcategorydataarray =  index.dish_category.replace(/, /g, ",");
			  var bcd = defaultcategorydataarray.replace(/\[/g, '');
			 // $("#editmodelIngredients").select2("refresh");
			  var json = {};
			  json['hotel'] = sessionVO.hotel_id;
			  $http.post(RESTONZACONSTANTS.url+"/getCategoriesAndIngredients", json).success(function(data, status, headers, config) {
				  $scope.modelCategory = data.response.categories;
				  $scope.modelIngredients = data.response.ingredients;
				  $("#editmodelIngredients").select2({
					  data: $scope.modelIngredients
					});
				  $("#editmodelCategory").select2({
					  data: $scope.modelCategory
					});
				 // $("#editmodelIngredients").val(defaultIngredientdataarray.split(",")).trigger("change");
				  $("#editmodelIngredients").val(abc.replace(/\]/g, '').split(",")).trigger("change");
				  $("#editmodelCategory").val(abc.replace(/\]/g, '').split(",")).trigger("change");
				  //$("#editmodelIngredients").val(bcd.replace(/\]/g, '').split(",")).trigger("change");
			  }).error(function(data, status, headers, config) {
			      $scope.table_count = "";
			  });
			  
			  $scope.editdish = index.dish_name;
			  $scope.editcategory = index.dish_category;
			  $scope.editprice = index.dish_price;
			  $scope.editctime = index.avg_cookingtime;
			  $scope.editcalories = index.calories;
			  $scope.editdiscount = index.discount;
			  $scope.editdescription = index.dish_description;
			  $scope.editstatus = index.status == true ? 'true' : 'false';
			  $scope.editImg = index.img_uri;
			  $scope.hot = index.hot == true ? 'true' : 'false';
	  }
	  
	  $scope.editSave = function() {
    	  var json={};
    	  json['hotel'] = sessionVO.hotel_id;
    	  json['dish_id'] = $scope.editdishid;
    	  var modified_dish_name = $scope.editdish;
    	  var modified_dish_description = '';
    	  var modified_dish_price =  $scope.editprice;
    	  var modified_discount = $scope.editdiscount;
    	  var modified_status = $scope.editstatus;
    	  var modified_nonVeg = $scope.hot;
    	  if (typeof $scope.editdescription  == "undefined" || $scope.editdescription  == "") {
    		  modified_dish_description = 'N/A';
    	  } else {
    		  modified_dish_description = $scope.editdescription;
    	  }
    	  
    	  $("#divLoading").addClass('show');
		  json['dish_name'] = modified_dish_name;
		  json['dish_description'] = modified_dish_description;
		  json['dish_price'] = modified_dish_price;
		  json['discount'] = modified_discount;
		  json['status'] = $scope.editstatus;
		  json['vegNonVeg'] = modified_nonVeg;
		  json['ingredients'] = $('#editmodelIngredients').val();
		  json['imageURI'] = $scope.foodSelectedImage == null ? $scope.editImg : $scope.foodSelectedImage;
		  var val = $('#editmodelCategory').val();
		  if (val.length == 0) {
			  var editcategory = [];
			  editcategory.push($scope.editcategory);
			  json['dish_category'] = editcategory;
		  } else {
			  json['dish_category'] = $('#editmodelCategory').val();
		  }
		 /* if ($scope.imageupload==true) {
				 json["image"] = imgbase64img.split(",")[1];
		  }*/
		  $http.post(RESTONZACONSTANTS.url+"/updateDish", json).success(function(data, status, headers, config) {
			  $("#divLoading").removeClass('show');
			  $route.reload();
			  $scope.showEditModal = false;
			  $.bootstrapGrowl(data.response ,{type: 'success',delay: 2000,
		             offset: {from: "top",amount: 0},
		             align: "center",
		             allow_dismiss: false
		         });
		  }).error(function(data, status, headers, config) {
		  });
		  if ($scope.dishimageupload) {
			  $scope.uploadFile($scope.editdishid,"dishimg");
		  }
      }     
	  
	  $scope.uploadFile = function(id,type){
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
			  console.log("No Images to store");
		  }
       };
	  
		//hotel form validation
		$.validator.addMethod('filesize', function (value, element, param) {
		    return this.optional(element) || (element.files[0].size <= param)
		}, 'File size must be less than {0}');
		
		 	/**
	   	   * Coding for browse button
	   	   */
	   	  $scope.functionToBeExecuted = function(imgSrc) {
	   		  $scope.foodSelectedImage = imgSrc;
	   		  $scope.showBrowseModal = false;
	   	  };
	   	  
	   	 $scope.closeDishItemAddModel = function() {
      	   $scope.showModal=false;
      	   $scope.foodSelectedImage = null;
      	   $route.reload();
         }
   	  
		$("#adddish").validate({
		    // Specify validation rules
		    rules: {
		    	dish: "required",
		    	category: "required",
		    	price: "required",
		    	dishimage : {
	                extension: "jpg,jpeg,png",
	                filesize: 50*1024,
		    	},
		    	optradio1: "required",
		    },
		    errorElement: "div",
	    	errorPlacement: function(error, element) {
	    		element.before(error);
	      },
		    // Specify validation error messages
		    messages: {
		    	dish: "Enter Dish Name",
		    	category: "Select category",
		    	price: "Enter price",
		    	dishimage : {
	                extension: "Upload photo in jpeg or jpg or png format only",
	                filesize: "dish image size should be less than 50 KB",
		    	},
		    	optradio1: "Select status",
		    },
		    submitHandler: function(form) {
			    $("#divLoading").addClass('show');
			     var json= {};
			     json['dish_name'] = form.dish.value;
				 json['dish_description'] = form.description.value;
				 json['dish_price'] = form.price.value;
				 json['calories'] = form.calories.value;
				 json['discount'] = form.disc.value;
				 json['avg_cookingtime'] = form.ctime.value;
				 json['status'] = form.optradio1.value;
				 json['vegNonVeg'] = form.optradio.value;
				 json['hotel'] = sessionVO.hotel_id;
				 /*if ($scope.imageupload==true) {
					 json["image"] = imgbase64img.split(",")[1];
				 }
		    	 */
				 json["imageURI"] = $scope.foodSelectedImage; 
				 json['ingredients'] = $('#modelIngredients').val();;
				 json['dish_category'] = $('#modelCategory').val();
		    	 console.log(JSON.stringify(json));
		    	 $scope.showModal = false;
		    	 $http.post(RESTONZACONSTANTS.url+"/addDishes", json).success(function(data, status, headers, config) {
		    		 if ($scope.dishimageupload){
		    			 $scope.uploadFile(data.response.dish_id,"dishimg"); 
		    		 }
		    		 
	    			 $("#divLoading").removeClass('show'); 
					  $route.reload();
					  $.bootstrapGrowl("Success!" ,{type: 'success',delay: 2000,
				             offset: {from: "top",amount: 0},
				             align: "center",
				             allow_dismiss: false
				       });
		    		
				  }).error(function(data, status, headers, config) {
					  $("#divLoading").removeClass('show'); 
					  $route.reload();
					  $.bootstrapGrowl(data.response ,{type: 'danger',delay: 2000,
				             offset: {from: "top",amount: 0},
				             align: "center",
				             allow_dismiss: false
				         }); 
				  });
		    }
		  });
		
}).directive('format', ['$filter', function ($filter) {
    return {
        require: '?ngModel',
        link: function (scope, elem, attrs, ctrl) {
            if (!ctrl) return;


            ctrl.$formatters.unshift(function (a) {
                return $filter(attrs.format)(ctrl.$modelValue)
            });

            ctrl.$parsers.unshift(function (viewValue) {
                var plainNumber = viewValue.replace(/[^\d|\-+|\.+]/g, '');
                elem.val($filter('number')(plainNumber));
                return plainNumber;
            });
        }
    };
}]).directive('fileModel', ['$parse', function ($parse) {
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


