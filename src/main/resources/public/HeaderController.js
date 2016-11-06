(function () {
    'use strict';

    var app = angular.module('client_app');

    app.controller('HeaderController', function($scope, AuthenticationService){
        $scope.currentUser = AuthenticationService.getCurrentUser();

        $scope.isUserUndefined = function(){
            return $scope.currentUser.username == undefined;
        };

        $scope.logout = function(){
            AuthenticationService.logout();
        };
    });
})();