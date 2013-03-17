'use strict';

/* Controllers */

function TestCtrl($scope) {
    $scope.testVal = "testing the scope"
}


function LoginCtrl($scope){}


function RegisterCtrl($scope){
    $scope.register = function(user){
        console.log(user);
    }

}
