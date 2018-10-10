angular.module('restonza').controller('AdsManagementController', function($scope, $route, $http, $filter, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var updatejson = {};
	$('#addads')[0].reset();
	var imgbase64img = "";
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	//updatejson["hotel_id"] = sessionVO.hotel_id;
	var url = RESTONZACONSTANTS.url + "/getAdsDetails";
	 $("#divLoading").addClass('show');
	$http.post(url, "").success(function(data, status, headers, config) {
		$scope.ads = data.response;
		$("#divLoading").removeClass('show');
	}).error(function(data, status, headers, config) {
		$scope.ads = "";
		$("#divLoading").removeClass('show');
	});		
	
			$scope.statuses = [ {
				value : '1',
				text : 'Pending Approval'
			}, {
				value : '2',
				text : 'Approved'
			} ];

			$scope.showStatus = function(ad) {
				var selected = [];
				if (ad.status) {
					selected = $filter('filter')($scope.statuses, {
						value : ad.status
					});
				}
				return selected.length ? selected[0].text : 'Not set';
			};

			$scope.removeAd = function(index) {
				var json = {};
				json["id"] = index;
				var url = RESTONZACONSTANTS.url + "/deleteAd";
				$http.post(url, json).success(function(data, status, headers, config) {
					console.log("success");
					$scope.showModal = false;
					$route.reload();
					$scope.ads.splice(index, 1);
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

			$scope.editAd = function(index) {
				var selectedAdd = $scope.ads[index];
				var statusmsg = "";
				if (selectedAdd.status == '1') {
					statusmsg = "Pending Approval";
				} else {
					statusmsg = "Approved";
				}
				$scope.editdata = {
					"adname" : selectedAdd.adname,
					"description" : selectedAdd.description,
					"adimg": selectedAdd.adimg,
					"status": statusmsg,
					"adprice":selectedAdd.price,
					"id":selectedAdd.id
				}
				$scope.showModal = true;
			};

			$(":file").change(function() {
				if (this.files && this.files[0]) {
					var reader = new FileReader();
					reader.onload = imageIsLoaded;
					reader.readAsDataURL(this.files[0]);
				}
			});

			function imageIsLoaded(e) {
				$('#myImg').attr('src', e.target.result);
				imgbase64img = e.target.result;
			}
			;

			//ads for validation
			$.validator.addMethod('filesize', function(value, element, param) {
				return this.optional(element)
						|| (element.files[0].size <= param)
			}, 'File size must be less than {0}');

			$("#addads").validate({
				// Specify validation rules
				rules : {
					adname : "required",
					banner : {
						/*required: true,*/
						extension : "jpg,jpeg,png",
						filesize : 50 * 1024,
					}
				},
				errorElement : "div",
				errorPlacement : function(error, element) {
					element.before(error);
				},
				// Specify validation error messages
				messages : {
					adname : "Enter adname",
					banner : {
						/*required: "Select Banner image",*/
						extension : "Upload photo in jpeg or jpg or png format only",
						filesize : "banner size should be less than 50 KB",
					}
				},
				submitHandler : function(form) {
					var json = {};
					var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
					//json["hotel_id"] = sessionVO.hotel_id;
					$scope.showModal = false;
					json["id"] = form.adid.value;
					json["adname"] = form.adname.value;
					json["adname"] = form.adname.value;
					json["price"] = form.adprice.value;
					json["description"] = form.description.value;
					json["adimg"] = imgbase64img.split(",")[1] == null ? $scope.editdata.adimg : imgbase64img.split(",")[1];
					console.log(JSON.stringify(json));
					var url = RESTONZACONSTANTS.url + "/addAdsDetails";
					$http.post(url, json).success(function(data, status, headers, config) {
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
		});