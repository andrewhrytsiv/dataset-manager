var app = angular.module('client_app', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngStorage',
    'ngMaterial',
    'ngFileUpload'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'home/home.html',
        controller: 'HomeController'
    }).when('/login',{
        templateUrl: 'login/login.html',
        controller: 'LoginController'
    }).when('/register',{
        templateUrl: 'register/register.html',
        controller: 'RegisterController'
    }).when('/dashboard', {
        templateUrl: 'dashboard/dashboard.html',
        controller: 'DashboardController'
    }).otherwise({
        redirectTo: '/'
    })
});
/*
app.run(function($rootScope, $http, $location, $localStorage){
    // keep user logged in after page refresh
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    // redirect to login page if not logged in and trying to access a restricted page
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        var publicPages = ['/login'];
        var restrictedPage = publicPages.indexOf($location.path()) === -1;
        if (restrictedPage && !$localStorage.currentUser) {
            $location.path('/login');
        }
    });
});
    */