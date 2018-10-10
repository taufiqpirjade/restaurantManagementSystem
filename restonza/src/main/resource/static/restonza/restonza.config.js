angular.module('restonza').config(['$routeProvider', function ($routeProvider,Auth) {
	$routeProvider
	//Place to add view and controller mapping
									.when(
											"/home",
											{
												templateUrl : "restonza/home/home.tmpl.html",
												controller : "HomeController"
											})
									.when(
											"/error",
											{
												templateUrl : "restonza/error/error.tmpl.html",
											})
									.when(
											"/login",
											{
												templateUrl : "restonza/login/login.tmpl.html",
												controller : "LoginController"
											})
									.when(
											"/placeorder",
											{
												templateUrl : "restonza/placeorder/neworder/neworder.tmpl.html",
												controller : "NewOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/placedorder",
											{
												templateUrl : "restonza/ordermanagement/placedorder/placedorder.tmpl.html",
												controller : "PlacedOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["admin", "waiter",,"restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/confirmedorder",
											{
												templateUrl : "restonza/ordermanagement/confirmedorder/confirmedorder.tmpl.html",
												controller : "ConfirmedOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/readyorders",
											{
												templateUrl : "restonza/ordermanagement/deliveredorder/deliveredorder.tmpl.html",
												controller : "DeliveredController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/billedorder",
											{
												templateUrl : "restonza/ordermanagement/billedorder/billedorder.tmpl.html",
												controller : "BilledController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/updateorder",
											{
												templateUrl : "restonza/placeorder/updateorder/updateorder.tmpl.html",
												controller : "UpdateOrderController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
													
											})
									.when(
											"/feedback",
											{
												templateUrl : "restonza/feedback/feedback.tmpl.html",
												controller : "FeedbackController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin", "waiter","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/categorymanagement",
											{
												templateUrl : "restonza/dishmanagement/categorymanagement/categorymanagement.tmpl.html",
												controller : "CategoryManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/dishmanagement",
											{
												templateUrl : "restonza/dishmanagement/dishesmanagement/dishmanagement.tmpl.html",
												controller : "DishManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/ingredientmanagement",
											{
												templateUrl : "restonza/dishmanagement/ingredientsmanagement/ingredientmanagement.tmpl.html",
												controller : "IngredientManagementController",
												authorize : function(Auth) {
													 var array = ["admin", "backkitchen","restrobaradmin"];
													 Auth.checkAuthorize(array);
												 }
											})
									.when(
											"/taxsetting",
											{
												templateUrl : "restonza/generalconfiguration/taxsetting.tmpl.html",
												controller : "TaxSettingController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/tablesetting",
											{
												templateUrl : "restonza/generalconfiguration/tablesetting/tablesetting.tmpl.html",
												controller : "TableSettingController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/adduser",
											{
												templateUrl : "restonza/usermanagement/adduser/adduser.tmpl.html",
												controller : "AddUserController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/edituser",
											{
												templateUrl : "restonza/usermanagement/edituser/edituser.tmpl.html",
												controller : "EditUserController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/campaignmanagement",
											{
												templateUrl : "restonza/adsmanagement/adsmanagement.tmpl.html",
												controller : "AdsManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/greetingmanagement",
											{
												templateUrl : "restonza/greetingmanagement/greetingmanagement.tmpl.html",
												controller : "GreetingsManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/updatehotel",
											{
												templateUrl : "restonza/hotelmanagement/updatehotel/updatehotel.tmpl.html",
												controller : "UpdateHotelManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/addhotel",
											{
												templateUrl : "restonza/hotelmanagement/addhotel/addhotel.tmpl.html",
												controller : "AddHotelManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/addUserManage",
											{
												templateUrl : "restonza/usermanagement/adduser/adduser.tmpl.html",
												controller : "AddUserController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
											
									.when(
											"/editProfile",
											{
												templateUrl : "restonza/usermanagement/edituser/edituser.tmpl.html",
												controller : "EditUserController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin","admin","waiter","backkitchen","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/campaignsApproval",
											{
												templateUrl : "restonza/manageapproval/adsapproval/approveCampaigns-superadmin.tmpl.html",
												controller : "CampaignsApprovalManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									
									.when(
											"/publishedCampaigns",
											{
												templateUrl : "restonza/manageapproval/adsapproval/publishedCampaigns.tmpl.html",
												controller : "AdsApprovalManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/greetingsApproval",
											{
												templateUrl : "restonza/manageapproval/greetingsapproval/greetingsapproval.tmpl.html",
												controller : "GreetingsApprovalManagementController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})	
									.when(
											"/billordermanagement",
											{
												templateUrl : "restonza/billmanagement/billmanagement.tmpl.html",
												controller : "BillOrderManagement",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/hotelanalysis",
											{
												templateUrl : "restonza/reports/hotelanalysis/hotelanalysis.tmpl.html",
												controller : "HotelAnalysisController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/pendingOrders",
											{
												templateUrl : "restonza/pendingorders/pendingorder.tmpl.html",
												controller : "PendingOrderManagement",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin", "backkitchen","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/dashboard",
											{
												templateUrl : "restonza/dashboard/dashboard.tmpl.html",
												controller : "DashboardController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/generateReport",
											{
												templateUrl : "restonza/reports/customreports/customreports.tmpl.html",
												controller : "CustomReportGenerator",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/hotelCustomAnalysis",
											{
												templateUrl : "restonza/reports/customhotelanalysis/customhotelanalysis.tmpl.html",
												controller : "CustomHotelAnalysisController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/logout",
											{
												resolve : {
													authorize : function(Auth) {
														Auth.logout();
													 }
												}
											})
									.when(
											"/waitercall",
											{
												templateUrl : "restonza/waiterwatermanagement/waitermanagement.tmpl.html",
												controller : "WaiterCallController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/watercall",
											{
												templateUrl : "restonza/waiterwatermanagement/watermanagement.tmpl.html",
												controller : "WaterCallController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/qrcodes",
											{
												templateUrl : "restonza/generalconfiguration/qrcode/qrcode.tmpl.html",
												controller : "QRCodeManagement",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/generateDetailReport",
											{
												templateUrl : "restonza/reports/admin/customreports/customreports.tmpl.html",
												controller : "AdminCustomHotelAnalysisController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/salesReport",
											{
												templateUrl : "restonza/reports/salesreport/admin/salesreports.tmpl.html",
												controller : "AdminSalesController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/hotelSalesReport",
											{
												templateUrl : "restonza/reports/salesreport/salesreports.tmpl.html",
												controller : "SalesController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/hotelMenuBulkUpload",
											{
												templateUrl : "restonza/hotelMenuBulkUpload/hotelMenuBulkUpload.tmpl.html",
												controller : "HotelMenuBulkUploadController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/manageUsers",
											{
												templateUrl : "restonza/usermanagement/manageusers/manageusers.tmpl.html",
												controller : "ManageUserController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["admin","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/barcategory",
											{
												templateUrl : "restonza/barmenumanagement/barcategory/barcategory.tmpl.html",
												controller : "BarCategoryController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.when(
											"/baritem",
											{
												templateUrl : "restonza/barmenumanagement/baritem/baritem.tmpl.html",
												controller : "BarItemController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})	
									.when(
											"/placebarorder",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/newbarorder/newbarorder.tmpl.html",
												controller : "NewBarOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
										.when(
											"/billbarordermanagement",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/billbarorder/billbarorder.tmpl.html",
												controller : "BillBarOrderManagement",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									
											.when(
											"/placedbarorder",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/placedbarorder/placedbarorder.tmpl.html",
												controller : "PlacedBarOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})	
											
											.when(
											"/confirmbarorder",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/confirmbarorder/confirmbarorder.tmpl.html",
												controller : "ConfirmedBarOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})	
											
											.when(
											"/pendingbarorder",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/pendingbarorder/pendingbarorder.tmpl.html",
												controller : "PendingBarOrderManagement",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
											.when(
											"/allCustomerData",
											{
												templateUrl : "restonza/allcustomers/allcustomers.tmpl.html",
												controller : "AllCustomersController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
											.when(
											"/paymentReport",
											{
												templateUrl : "restonza/reports/superadmin/paymentreports/paymentreports.tmpl.html",
												controller : "SuperAdminPaymentController",
												resolve : {
													authorize : function(Auth) {
														 var array = ["superadmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
											.when(
											"/updatebarorder",
											{
												templateUrl : "restonza/barmenumanagement/barordermanagement/updatebarorder/updatebarorder.tmpl.html",
												controller : "UpdateBarOrderController",
												resolve : {
													 authorize : function(Auth) {
														 var array = ["barstaff","restrobaradmin"];
														 Auth.checkAuthorize(array);
													 }
												}
											})
									.otherwise({
										redirectTo : '/login'
									})
}]);