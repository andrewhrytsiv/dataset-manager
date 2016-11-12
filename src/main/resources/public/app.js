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

app.run(['$rootScope','AuthenticationService', function($rootScope, AuthenticationService) {
    $rootScope.$on('$routeChangeSuccess', function (event, current) {
        $rootScope.title = current.$$route.title;
    });
}]);

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