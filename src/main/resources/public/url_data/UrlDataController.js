(function () {
    'use strict';
 
    var app = angular.module('client_app');
	
    app.controller('UrlDataController', function($scope, $http){
   
        $scope.addUrlData = function () {
          $http.post('/api/url_data',{ url : "https://raw.githubusercontent.com/andrewhrytsiv/files/master/data.csv"})
            .success(function (response) {
                console.log('resp_data:'+JSON.stringify(response));
             })   
            .error(function (response, status) {
                //add message on view
                console.log('Error ' + response);
            })
        };
    });

})();