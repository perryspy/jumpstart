'use strict';

/* Controllers */

var TestCtrl = ['$scope', function($scope) {

}];


var LoginCtrl = ['$scope', 'userService', function($scope, userService){

}];


var RegisterCtrl = ['$scope', '$location', 'userService', 'securityService', function($scope, $location, userService, securityService){
    $scope.register = function(formData){
        var user = new userService(formData);
        user.$save(function(response){
            console.log(response);
            securityService.setConnectedUser(response.data.id);
            $location.path('/');
        });

    }

    $scope.$on('appError', function(event, errors){
        console.log('appError detected: ' + event);
        console.log(errors);
        $scope.errors = errors;
    })
}];

var LogoutCtrl = ['$scope', function($scope){

}];
