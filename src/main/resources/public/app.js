var app = angular.module('client_app', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngStorage'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        //templateUrl: 'views/list.html',
        templateUrl: 'home/home.html',
        controller: 'HomeController'
    }).when('/login',{
        templateUrl: 'login/login.html',
        controller: 'LoginController'
    }).when('/register',{
        templateUrl: 'register/register.html',
        controller: 'RegisterController'
    }).when('/url_data', {
        templateUrl: 'url_data/url_data.html',
        controller: 'UrlDataController'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('HomeController', function($scope){
    console.log("Hello world Home!");
});

app.controller('ListCtrl', function ($scope, $http) {
    $http.get('/api/todos').success(function (data) {
        $scope.todos = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    })

    $scope.todoStatusChanged = function (todo) {
        console.log(todo);
        $http.put('/api/todos/' + todo.id, todo).success(function (data) {
            console.log('status changed');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});

app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.todo = {
        done: false
    };

    $scope.createTodo = function () {
        console.log($scope.todo);
        $http.post('/api/todos', $scope.todo).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});