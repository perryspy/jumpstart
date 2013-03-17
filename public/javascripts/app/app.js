'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['services']).
    config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);

        $routeProvider.when('/', {templateUrl: 'assets/partials/test.html', controller: TestCtrl});
        $routeProvider.when('/login', {templateUrl: 'assets/partials/login.html', controller: LoginCtrl});
        $routeProvider.when('/register', {templateUrl: 'assets/partials/register.html', controller: RegisterCtrl});
        $routeProvider.otherwise({redirectTo: 'assets/partials/test.html'});
    }]);