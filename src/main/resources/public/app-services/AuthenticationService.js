(function () {
    'use strict';
 
    angular.module('client_app').service('AuthenticationService', Service);
 
    function Service($window) {
        var service = this;
        var currentUser = JSON.parse($window.localStorage.getItem("currentUser")) || {};

        service.setCurrentUser = function(user){
            $window.localStorage.setItem("currentUser",JSON.stringify(user));
            angular.extend(currentUser, user);
        };

        service.getCurrentUser = function(){
            return currentUser;
        };

        service.logout = function(){
            $window.localStorage.clear();
            currentUser.username = null;
            currentUser.access_token = null;
        };
    }
})();