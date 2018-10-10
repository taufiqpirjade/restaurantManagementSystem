angular.module('restonza').filter('checkIngredient', function() {
    return function(input, enteredValue, status) {
    	var keepGoing = false;
         angular.forEach(input, function(obj) {
        	 if(!keepGoing) {
        		 if((obj.ingredients_name).toLowerCase() == enteredValue.toLowerCase()) {
        			 if(obj.status == status){
        				 keepGoing = true;
        			 }
             	} else{
             		keepGoing = false;
             	}
        	 }
         });
         return keepGoing;
    };

});