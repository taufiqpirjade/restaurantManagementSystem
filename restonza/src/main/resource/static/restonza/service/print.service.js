angular.module('restonza').factory('PrintService',function($http, RESTONZACONSTANTS) {
	var printVO = [];
	return {
		getPrintVO : function() {
			return property;
		},
		
		setPrintVO : function(value) {
			property = value;
		},
		
		doPrintJob : function(printObject) {
			var url = RESTONZACONSTANTS.url + "/doPrintJobService";
	   		var json={};
	   		json['hotel_id'] =printObject.hotelid;
	   		json["table_id"] = printObject.tableid;
			json['order_id'] = printObject.orderid;
			json["orderDetails"] = printObject.dishDetails;
			json["amt"] = printObject.amt;
			json["taxes"] = printObject.taxes;
			json["barordercount"] = printObject.barordercount;
	   		$http.post(url, json).success(function(data, status, headers, config) {
	   			if (data.status == 'success') {
	   				$.bootstrapGrowl("Bill Printed Successfully" ,{
			             type: 'success',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
				} else {
					$.bootstrapGrowl("Enable to Print! Please contact administrator" ,{
			             type: 'danger',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
				}
	   	  	});
		},
		
		doRestoBarPrintJob : function(printObject) {
			var url = RESTONZACONSTANTS.url + "/doPrintRestoBarJobService";
	   		var json={};
	   		json['hotel_id'] =printObject.hotelid;
	   		json["table_id"] = printObject.tableid;
			json['order_id'] = printObject.orderid;
			json["orderDetails"] = printObject.dishDetails;
			json["amt"] = printObject.amt;
			json["taxes"] = printObject.taxes;
			json["ltaxes"] = printObject.ltaxes;
			json["barordercount"] = printObject.barordercount;
			json["foodbillamt"] = printObject.foodbillamt;
			json["barbillamt"] = printObject.barbillamt;
	   		$http.post(url, json).success(function(data, status, headers, config) {
	   			if (data.status == 'success') {
	   				$.bootstrapGrowl("Bill Printed Successfully" ,{
			             type: 'success',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
				} else {
					$.bootstrapGrowl("Enable to Print! Please contact administrator" ,{
			             type: 'danger',
			             delay: 2000,
			             offset: {
			            	 from: "top",
			            	 amount: 0
			             },
			             align: "center",
			             allow_dismiss: false
			         });
				}
	   	  	});
		},
	};

});