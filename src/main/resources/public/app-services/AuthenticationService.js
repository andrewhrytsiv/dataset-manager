(function () {
    'use strict';
 
    angular.module('client_app').factory('AuthenticationService', Service);
 
    function Service($http, $localStorage) {

        console.log("AuthenticationService init---------------");
        var service = {};
 
        service.Login = Login;
        service.Logout = Logout;
 
        return service;
 
        function Login(email, password, callback) {
            $http.post('/api/authenticate', { email: email, password: password })
                .success(function (response) {
                    // login successful if there's a token in the response
                    if (response.access_token) {
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = { username: username, token: response.access_token };
 
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.access_token;
 
                        // execute callback with true to indicate successful login
                        callback(true);
                    } else {
                        // execute callback with false to indicate failed login
                        callback(false);
                    }
                });
        }
 
        function Logout() {
            // remove user from local storage and clear http auth header
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
        }
    }
})();