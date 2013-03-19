'use strict';

/* Controllers */

var TestCtrl = ['$scope', function($scope) {

}];


var LoginCtrl = ['$scope', 'userService', function($scope, userService){

}];

/**
 * Controller for User Registration.  This is a 'top level' controller meaning that it should be responsible for watching
 * for and handling appError broadcasts
 */
var RegisterCtrl = ['$scope', '$location', 'userService', 'securityService', function($scope, $location, userService, securityService){
    $scope.register = function(formData){
        var user = new userService(formData);
        user.$save(function(response){
            securityService.setConnectedUser(response.data.id, function(){
                console.log('executing callback');
                $location.path('/home');
            });

        });

    }

    $scope.$on('appError', function(event, errors){
        console.log('appError detected: ' + event);
        console.log(errors);
        $scope.errors = errors;
    })
}];

/**
 * Controller for basic actions like logging out of the application and navigating to My Account page
 */
var DefaultActionsCtrl = ['$scope', '$location', 'securityService', function($scope, $location, securityService){

    /**
     * Log out of the application, clears both client and server side user credentials
     */
    $scope.logout = function(){
        securityService.clearConnectedUser(function(){
            $location.path('/');
        });


    }
}];
