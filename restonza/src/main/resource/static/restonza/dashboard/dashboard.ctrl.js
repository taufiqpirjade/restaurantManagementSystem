angular.module('restonza').controller('DashboardController', function ($scope,$filter, $route, $http, RESTONZACONSTANTS/* $scope, $location, $http */) {
	var sessionVO = JSON.parse(sessionStorage.getItem("sessionVO"));
	var json = {};
	json["hotel_id"] = sessionVO.hotel_id;
	$scope.hotelInfo = sessionVO.hotel_id;
	$scope.serverURL = RESTONZACONSTANTS.url;
	var bookedSeats = [];
	var peopleSittingOnTable = [];
	var column,matchCout,tableCount,temp;
	var settings;
	var url = RESTONZACONSTANTS.url + "/getTableStructue/"+sessionVO.hotel_id
	$http.post(url, json).success(function(data, status, headers, config) {
		tableCount = data.response.tableCount;
		var occupiedTables = data.response.occupiedTableList;
		
		$.each( occupiedTables, function(i,value ){
			var tatti1 =  value.table_id;
			console.log(tatti1);
			bookedSeats[i] = parseInt(value.table_id);
			peopleSittingOnTable[value.table_id]  = parseInt(value.numberOfPeopleSitting)
		});
		$scope.occupiedtables = bookedSeats.length;
		$scope.tablesCount = tableCount;
		$scope.todaysEarning = Math.trunc(data.response.todaysEarning);
		$scope.activeTablesArray = data.response.activetables;
		$scope.totalActiveUsers = data.response.totalActiveUsers;
		var remainingCount = tableCount % 5;
		matchCout  = tableCount - remainingCount; // row 
		temp = matchCout/5;	
			
		settings = {
		               rows: 5,
		               cols: temp+1,
		               rowCssPrefix: 'row-',
		               colCssPrefix: 'col-',
		               seatWidth: 35,
		               seatHeight: 35,
		               seatCss: 'seat',
		               selectedSeatCss: 'selectedSeat',
		               selectingSeatCss: 'selectingSeat',
		               inactiveSeatCss: 'inactiveSeat'
		           };
		
		var init = function (reservedSeat,noOfPeopleSittingArray) {
	        var str = [], seatNo, className;
	        for (i = 0; i < settings.rows; i++) {
	            for (j = 0; j < settings.cols; j++) {
	                seatNo = (i + j * settings.rows + 1);
	                if (seatNo > tableCount) {
	                	break;
	                }
	                className = settings.seatCss + ' ' + settings.rowCssPrefix + i.toString() + ' ' + settings.colCssPrefix + j.toString();
	                if ($.isArray(reservedSeat) && $.inArray(seatNo, reservedSeat) != -1) {
	                    className += ' ' + settings.selectedSeatCss;
	                }
	                if ($scope.activeTablesArray[seatNo] == undefined) {
	                    className += ' ' + settings.inactiveSeatCss;
	                }
	                
	                str.push('<li class="' + className + '"' +
	                          'style="top:' + (i * settings.seatHeight).toString() + 'px;left:' + (j * settings.seatWidth).toString() + 'px">' +
	                          '<a title="Person Seating:' + noOfPeopleSittingArray[seatNo] + '">' + seatNo + '</a>' +
	                          '</li>');
	            }
	        }
	        $('#place').html(str.join(''));
	    };
	    //case I: Show from starting
	    //init();

	    //Case II: If already booked
	    init(bookedSeats,peopleSittingOnTable);

	    $('.' + settings.seatCss).click(function () {
	    if ($(this).hasClass(settings.selectedSeatCss)){
	        alert('This seat is already reserved');
	    }
	    else{
	        $(this).toggleClass(settings.selectingSeatCss);
	        }
	    });
	     
	    $('#btnShow').click(function () {
	        var str = [];
	        $.each($('#place li.' + settings.selectedSeatCss + ' a, #place li.'+ settings.selectingSeatCss + ' a'), function (index, value) {
	            str.push($(this).attr('title'));
	        });
	        alert(str.join(','));
	    })
	     
	    $('#btnShowNew').click(function () {
	        var str = [], item;
	        $.each($('#place li.' + settings.selectingSeatCss + ' a'), function (index, value) {
	            item = $(this).attr('title');                   
	            str.push(item);                   
	        });
	        alert(str.join(','));
	    })
	});
	
	/*$scope.storeOneSignalId(function (userid){
		var json = {};
		json["hotel_id"] = sessionVO.hotel_id;
		json["oneSignalId"] = userid;
		console.log(userid);
		var url = RESTONZACONSTANTS.url + "/storeOneSignalId"
		$http.post(url, json).success(function(data, status, headers, config) {
			
		});
	});*/
	
	
	/*var url = RESTONZACONSTANTS.url + "/getDashboard";
	$http.post(url, json).success(function(data, status, headers, config) {
		var chart = AmCharts.makeChart("chartdiv", {
		    "theme": "light",
		    "type": "serial",
			"startDuration": 2,
		    "dataProvider": data.response.earning,
		    "valueAxes": [{
		        "position": "left",
		        "title": "Rating"
		    }],
		    "graphs": [{
		        "balloonText": "[[category]]: <b>[[value]]</b>",
		        "fillColorsField": "colorcode",
		        "fillAlphas": 1,
		        "lineAlpha": 0.1,
		        "type": "column",
		        "valueField": "total_earnings"
		    }],
		    "depth3D": 20,
			"angle": 30,
		    "chartCursor": {
		        "categoryBalloonEnabled": false,
		        "cursorAlpha": 0,
		        "zoomable": false
		    },
		    "categoryField": "month",
		    "categoryAxis": {
		        "gridPosition": "start",
		        "labelRotation": 0
		    },
		    "export": {
		    	"enabled": true
		     }

		});
		
		$scope.tablelist = data.response.tablelist;
		
		
	}).error(function(data, status, headers, config) {
		
	});*/
});



/*$scope.reload = function () {
    $http.get('http://localhost:3000/api/todos').
        success(function (data) {
          $scope.todos = data.todos;
      });

    $timeout(function(){
      $scope.reload();
    },30000)
  };
  $scope.reload();
  
  http://stackoverflow.com/questions/33273054/angularjs-auto-reload-when-backend-change
  *
  */