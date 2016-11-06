(function () {
    'use strict';
 
    var app = angular.module('client_app');
	
    app.controller('LoginController', function($scope, $http,$location, AuthenticationService){

        $scope.submit = function() {
            $http.post('/api/login',{ email : $scope.email, password : $scope.password})
                .success(function (response) {
                    console.log('resp_data:'+JSON.stringify(response));
                    if(response.access_token){
                        var currentUser = { username: response.username, access_token: response.access_token };
                        AuthenticationService.setCurrentUser(currentUser);
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.access_token;
                        $location.path('/');
                    }})
                .error(function (response, status) {
                    //add message on view
                    console.log('Error ' + response);
                })
        };
    });

})();