(function () {
    'use strict';

    var app = angular.module('client_app');

    app.controller('HeaderController', function($scope,$location, AuthenticationService){
        $scope.currentUser = AuthenticationService.getCurrentUser();

        $scope.isUserUndefined = function(){
            return !$scope.currentUser.username;
        };

        $scope.logout = function(){
            AuthenticationService.logout();
            $location.path('/');
        };
    });
})();