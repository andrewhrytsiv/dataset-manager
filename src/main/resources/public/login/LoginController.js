(function () {
    'use strict';
 
    var app = angular.module('client_app');
	
    app.controller('LoginController', function($location, AuthenticationService){
        var vm = this;
 
        vm.login = login;
 
        initController();
 
        function initController() {
            // reset login status
            AuthenticationService.Logout();
        };
 
        function login() {
            vm.loading = true;
            AuthenticationService.Login(vm.email, vm.password, function (result) {
                if (result === true) {
                    $location.path('/');
                } else {
                    vm.error = 'Username or password is incorrect';
                    vm.loading = false;
                }
            });
        };
    });

})();