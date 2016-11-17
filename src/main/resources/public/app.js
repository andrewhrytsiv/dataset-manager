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
    }).when('/apidoc', {
        title: 'API',
        templateUrl: 'apidoc/api.doc.html',
    }).otherwise({
        title: 'Home',
        redirectTo: '/'
    });
    // use the HTML5 History API
    $locationProvider.html5Mode(true);
});

app.run(function($rootScope, $http, $location, $localStorage,AuthenticationService){

    if (AuthenticationService.getCurrentUser()) {
        var user = AuthenticationService.getCurrentUser();
        $http.post('/api/validatetocken',{ access_token : user.access_token })
            .success(function (response) {
                // keep user logged in after page refresh
                $http.defaults.headers.common.Authorization = 'Bearer ' + user.access_token;
            })
            .error(function (response, status) {
                AuthenticationService.logout();
            });
    }

    $rootScope.$on('$routeChangeSuccess', function (event, current) {
        $rootScope.title = current.$$route.title;
        $rootScope.bodyClass = ($rootScope.title + '-page').toLowerCase();
    });
});
