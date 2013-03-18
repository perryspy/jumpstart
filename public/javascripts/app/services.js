'use strict';

/* Services */

angular.module('services', [])
    .factory('userService', ['$resource', function($resource){
        return $resource("/api/users");
    }])

    .factory('securityService', function(){
        var service = {};

        service.setConnectedUser = function(id){
            window.Conf.connectedUserId = id;
        }

        return service;
    });