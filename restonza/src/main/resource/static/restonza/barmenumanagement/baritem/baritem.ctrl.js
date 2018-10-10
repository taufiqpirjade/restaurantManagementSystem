angular.module('restonza').controller('BarItemController', function ($scope, $http, $filter, $route, RESTONZACONSTANTS) {
	var json={};
	/*$scope.barcategory = {};   
  	$scope.barsubcategories = [];
  	$scope.updatebarcategory = {};*/
	$scope.serverimgpath = "http://173.255.250.117:8888/";
  	$scope.qtyDetails = [];
  	$scope.addModal = false;
  	$scope.servedas = true;
  	$scope.addbaritem = {};
  	$scope.imageupload = false;
  	$scope.barsubcategoriesmap = {};
  	$scope.subcategoryList = [];
  	$scope.subcategoryflag = false;
  	$scope.barimageuploadflag = false;
  	$scope.selectedImage = null;
  	$scope.changeServedAsStatus = function(){
  	    $scope.servedas = !$scope.servedas;
  	 	$scope.qtyDetails = [];
  	}
  	 //added for sorting------------------------------------------------------------
	  $scope.sortType     = 'bar_item_name'; // set the default sort type
	  $scope.sortReverse  = false;  // set the default sort order
	  //added for searching ---------------------------------------------------------
	  $scope.filteredStatus = [];
	  $scope.filteredStatus.push({value: '', text: 'All'});
	  $scope.filteredStatus.push({value: true, text: 'Active'});
	  $scope.filteredStatus.push({value: false, text: 'Inactive'});
  	  
	  var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	  // Fetch table details call
	  $("#divLoading").addClass('show');
	  $http.post(RESTONZACONSTANTS.url+"/getBarItems/" + sessionVO.hotel_id, json).success(function(data, status, headers, config) {
		  var records = [];
		  $scope.baritems = data.response;
	  });
	  
	  var json1 = {};
	  $http.post(RESTONZACONSTANTS.url+"/getStaticImageListing/bar", json1).success(function(data, status, headers, config) {
		  $scope.barItemImagesArray = data.response;
	  }).error(function(data, status, headers, config) {
	      
	  }); 
	  
	  function chunk(arr, size) {
		  var newArr = [];
		  for (var i=0; i<arr.length; i+=size) {
		    newArr.push(arr.slice(i, i+size));
		  }
		  return newArr;
		}
	  
	  //get barcategories
	  $http.post(RESTONZACONSTANTS.url+"/getActiveBarCategories/" + sessionVO.hotel_id, json).success(function(data, status, headers, config) {
		  var categoryrecords = [];
		  $(data.response).each(function(idx, obj){
				categoryrecords.push(obj.bar_category_name);
				$scope.barsubcategoriesmap[obj.bar_category_name] = obj.bar_sub_category;
		    });
		  $("#barcategoryname").select2({
			  data: categoryrecords,
		  });
	  });
	  
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
		    
		    $scope.removeBarItem = function(index) {
		    	$("#divLoading").addClass('show');
				var json = {};
				var id = index.id;
				var url = RESTONZACONSTANTS.url + "/deleteBarItem/" + id;
				$http.post(url, json).success(function(data, status, headers, config) {
					$("#divLoading").removeClass('show');
					 $scope.baritems.splice(index, 1);
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
		      
	$scope.editBarItem = function(baritemindex) {
		console.log("In bar item edit");
		$scope.baritemid = baritemindex.id;
		$scope.showbarItemEditModal = true;
		$scope.editbarItemName = baritemindex.bar_item_name;
		$scope.editcategory = baritemindex.bar_category_name;
		$scope.editSubCategory = baritemindex.sub_category;
		$scope.editqty = baritemindex.qty;
		$scope.editprice = baritemindex.price;
		$scope.editImg = baritemindex.barimg;
		$scope.editstatus = baritemindex.status;
		
	}
	
	$scope.editBarItemSave = function() {
	 var json= {};
   	 json["bar_item_id"] = $scope.baritemid;
   	 json["bar_item_name"] = $scope.editbarItemName;
   	 json["status"] = $scope.edititemstatus;
   	 json["barmenuprice"] = $scope.editprice;
   	 json["image"] = $scope.selectedImage == null ? $scope.editImg : $scope.selectedImage;
   	if ( $scope.editbarItemName == "" || typeof $scope.editbarItemName === "undefined") {
		 $.bootstrapGrowl('Please enter item name' ,{
            type: 'danger',
            delay: 2000,
            offset: {
           	 from: "top",
           	 amount: 0
            },
            align: "center",
            allow_dismiss: false
        });
		 return;
	}
   	 if ($scope.editprice == 0) {
		 $.bootstrapGrowl('Please add price details' ,{
             type: 'danger',
             delay: 2000,
             offset: {
            	 from: "top",
            	 amount: 0
             },
             align: "center",
             allow_dismiss: false
         });
		 return;
	}
		var url = RESTONZACONSTANTS.url + "/updateBarItem";
		$("#divLoading").addClass('show');
		$http.post(url, json).success(function(data, status, headers, config) {
				$scope.uploadFile($scope.baritemid,"barimg");
			$("#divLoading").removeClass('show');
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
		}
	
	  $scope.addNew = function(qtyvalue){
		  if(qtyvalue != null && qtyvalue !="") {
			  var selectedQty = qtyvalue;
			  var hasFoo = false;
			  $.each($scope.qtyDetails, function(i,obj) {
			    if (obj.qty === selectedQty ) {
			    	hasFoo = true; 
			    	return false;
			    }
			  });  
			  if (hasFoo) {
				  $id='#'+selectedQty+'tr';
				  $($id).addClass("invalid");
				$("#errormsg").text("Bar Sub Category already present");
				setTimeout(function(){
					$($id).removeClass("invalid");
					$("#errormsg").text("");
				}, 1500);
			  } else {
				  if(typeof selectedQty != "undefined" && selectedQty != "") {
				  $scope.qtyDetails.push({
					  "qty" : selectedQty,
					  "price" : 0,
				  });
				  }
			  }
			  $scope.qtyvalue = null;
		  }
        }; 
        
        $scope.removeQty = function(index) {
        	 $scope.qtyDetails.splice(index, 1);
        }
        
    	  $scope.addBarMenuItem = function() {
    		  //validation custom
    		  var categoryselected = $scope.addbaritem.barcategoryname.$modelValue;
    		  //var subcategoryselected = $scope.addbaritem.barsubcategoryname.$modelValue;
    		  if (typeof categoryselected  == "undefined") {
    			  $.bootstrapGrowl('Please select category' ,{
			             type: 'danger',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
    			  return;
    		  }
    	
    		  var price  = $("#addprice").val();
    		  if ($scope.servedas == true) {
    			 if (price == 0) {
    				 $.bootstrapGrowl('Please add price details' ,{
			             type: 'danger',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
    				 return;
				}
    		  }
    		  //final processing
    		  $scope.addModal = false;
    		  $("#divLoading").addClass('show');
    		     var json= {};
    	    	 json["hotel_id"] = sessionVO.hotel_id;
    	    	 json["bar_category_name"] = categoryselected;
    	    	 json["bar_item_name"] = $scope.addbaritem.bar_item_name;
    	    	 json["status"] = $scope.addbaritem.status== 'true' ? true : false;
    	    	 json["servered_in_qty"] = $scope.servedas
    	    	 if ($scope.servedas == true) {
    	    		 json["barmenuprice"] = $scope.addbaritem.price;
				} else {
					json["serving_manner_vo"] = $scope.qtyDetails;
				}
    	    	 json["image"] = $scope.selectedImage;
    	    	 console.log(JSON.stringify(json));
    	    	 var url = RESTONZACONSTANTS.url + "/addBarItem";
    		    	$http.post(url, json).success(function(data, status, headers, config) {
    		    		//	$scope.uploadFile(data.response,"barimg");
    		    		$('html,body').animate({ scrollTop: 0 }, 'slow');
    					    $route.reload();
    					    $scope.clearAll();
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
    		   	  		//$scope.showModal = true;
    		   	  		closeBarItemAddModel();
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
    	  
    	  $scope.clearAll = function() {
    		  	$scope.qtyDetails = [];
    		  	$scope.addModal = false;
    		  	$scope.servedas = true;
    		  	$scope.addbaritem = {};
    		  	$scope.imageupload = false;
    		  	$scope.barsubcategoriesmap = {};
    		  	$scope.subcategoryList = [];
    		  	$scope.subcategoryflag = false;
    	  }
    	  
    	  $scope.openEditableModal = function(index) {
    		  $scope.clearAll();
    		  //fill the modal values
    		  $scope.updatebarcategory = $scope.barcategories[index];
    		  if($scope.updatebarcategory.bar_sub_category != "") {
    			  $scope.barsubcategories = $scope.updatebarcategory.bar_sub_category.split(',');
    		  }
    		  $scope.updatebarcategory.status = $scope.updatebarcategory.status == true ? 'active' : 'inactive';
    		  $scope.editshowModal = true;
    	  }
    	  
    	  $scope.updateBarCategories = function() {
    		  $scope.editshowModal = false;
    		  $("#divLoading").addClass('show');
    		     var json= {};
    	    	 json["hotel_id"] = sessionVO.hotel_id;
    	    	 json["bar_category_id"] = $scope.updatebarcategory.id;
    	    	 json["bar_category_name"] = $scope.updatebarcategory.bar_category_name;
    	    	 json["bar_sub_category"] = $scope.barsubcategories;
    	    	 json["status"] = $scope.updatebarcategory.status== 'active' ? true : false;
    	    	 var url = RESTONZACONSTANTS.url + "/updateBarCategory";
    		    	$http.post(url, json).success(function(data, status, headers, config) {
    		    		$('html,body').animate({ scrollTop: 0 }, 'slow');
    					    $route.reload();
    					    $scope.clearAll();
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
    	  };
    	  
    	  $scope.uploadFile = function(id,type){
    		  if ($scope.fileToUploadBar) {
    			  var file = $scope.fileToUploadBar;
                  var uploadUrl = "/restonza/mobileimage/uploadCategoryPic/"+type+"/"+id;
                  //$scope.fileUpload.uploadFileToUrl(file, uploadUrl);
                  var fd = new FormData();
                  fd.append('file', file);
                  $http.post(uploadUrl, fd, {
                     transformRequest: angular.identity,
                     headers: {'Content-Type': undefined}
                  })
                  $scope.fileToUploadBar = null;
    		  }
    		  else {
    			  console.log("No Changes for Images");
    		  }
           };
           
           $scope.updateBarItemSequence = function() {
     		  $("#divLoading").addClass('show');
     		  var json = $('#baritem').tableToJSON();
     		  var data = [];
     		  for (var i=1;i<=json.length-1;i++) {
     			  data.push(json[i].Id+" - "+i);
     		  }
     		  var json = {};
     		  json['hotel_id'] = sessionVO.hotel_id;
     		  json['updatedSequence'] = data;
     		  $http.post(RESTONZACONSTANTS.url+"/updatedBarItemSequence", json).success(function(data, status, headers, config) {
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
     		  $http.post(RESTONZACONSTANTS.url+"/getHotelMenuPdfDownload/bar", json,{responseType: 'arraybuffer'}).success(function(data, status, headers, config) {
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
           
           $scope.closeBarItemAddModel = function() {
        	   $scope.addModal=false;
        	   $scope.selectedImage = null;
        	   $route.reload();
           }
           
           /**
     	   * Coding for browse button
     	   */
     	  $scope.functionToBeExecuted = function(imgSrc) {
     		  $scope.selectedImage = imgSrc;
     		  $scope.showBrowseModal = false;
     	  };
    	  
    	  $scope.displaySubCategories = function(){
    		  $scope.subcategoryflag = false;
    		  var selectedCategory = $("#barcategoryname").val();
    		  $scope.subcategoryList = [];
    		  var subcategory = $scope.barsubcategoriesmap[selectedCategory];
    		  if (subcategory != "" && subcategory != null) {
    			  $scope.subcategoryflag = true;
    			  $scope.subcategoryList = subcategory.split(',');
    			  $("#barsubcategoryname").select2({
    				  data: $scope.subcategoryList,
    			  });
    		  }
    	  }
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
