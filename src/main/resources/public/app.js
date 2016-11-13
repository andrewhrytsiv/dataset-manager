var app = angular.module('client_app', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngStorage',
    'ngMaterial',
    'ngFileUpload'
]);

app.config(function ($routeProvider,$locationProvider) {
    $routeProvider.when('/', {
        title: 'Home',
        templateUrl: 'home/home.html',
        controller: 'HomeController'
    }).when('/login',{
        title: 'Login',
        templateUrl: 'login/login.html',
        controller: 'LoginController'
    }).when('/register',{
        title: 'Register',
        templateUrl: 'register/register.html',
        controller: 'RegisterController'
    }).when('/dashboard', {
        title: 'Dashboard',
        templateUrl: 'dashboard/dashboard.html',
        controller: 'DashboardController'
    }).otherwise({
        title: 'Home',
        redirectTo: '/'
    });
    // use the HTML5 History API
    $locationProvider.html5Mode(true);
});

// app.run(['$rootScope','$localStorage','$http', function($rootScope,$localStorage,$http) {
//     // keep user logged in after page refresh
//     if ($localStorage.currentUser) {
//         console.log($localStorage.currentUser)
//         $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.access_token;
//     }
//
//
//     $rootScope.$on('$routeChangeSuccess', function (event, current) {
//         $rootScope.title = current.$$route.title;
//     });
// }]);


app.run(function($rootScope, $http, $location, $localStorage,AuthenticationService){
    // keep user logged in after page refresh
    if (AuthenticationService.getCurrentUser()) {
        var user = AuthenticationService.getCurrentUser();
        console.log(user)
        $http.defaults.headers.common.Authorization = 'Bearer ' + user.access_token;
    }

    $rootScope.$on('$routeChangeSuccess', function (event, current) {
        $rootScope.title = current.$$route.title;
    });
});
