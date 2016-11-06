(function () {
    'use strict';
 
    var app = angular.module('client_app');
	
	app.controller('RegisterController', function($scope, $http,$window,$location,AuthenticationService){
	   $scope.submit = function() {
            $http.post('/api/register',{ email : $scope.email,
             username : $scope.username,password : $scope.password})
            .success(function (response) {
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