(function () {
    'use strict';
 
    var app = angular.module('client_app');
	
    app.controller('LoginController', function($scope, $http,$location, AuthenticationService,$mdDialog){
        $scope.loginProcess = false;

        $scope.submit = function() {
            $scope.loginProcess = true;
            $http.post('/api/login',{ email : $scope.email, password : $scope.password})
                .success(function (response) {
                    $scope.loginProcess = false;
                    if(response.access_token){
                        var currentUser = { username: response.username, access_token: response.access_token };
                        AuthenticationService.setCurrentUser(currentUser);
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.access_token;
                        $location.path('/dashboard');
                    }})
                .error(function (response, status) {
                    $scope.loginProcess = false;
                    showAlert(response)
                })
        };

        function showAlert(response) {
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title('Sorry, but it\'s not valid credentials.')
                    .textContent(response.error_message)
                    .ariaLabel('Alert Dialog Demo')
                    .ok('OK')
                    .targetEvent(response)
            );
        };

    });

})();