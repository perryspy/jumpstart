'use strict';

/* Controllers */

var TestCtrl = ['$scope', function($scope) {
    $scope.testVal = "testing the scope"
}];


var LoginCtrl = ['$scope', 'User', function($scope, User){

}];


var RegisterCtrl = ['$scope', '$location', 'User', function($scope, $location, User){
    $scope.register = function(formData){
        console.log(formData);
        var user = new User(formData);
        user.$save();

    }

    $scope.$on('appError', function(event, errors){
        console.log('appError detected: ' + event);
        console.log(errors);
        $scope.errors = errors;
    })
}];
