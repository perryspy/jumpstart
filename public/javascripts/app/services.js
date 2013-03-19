'use strict';

/* Services */

angular.module('services', [])
    .factory('userService', ['$resource', function($resource){
        return $resource("/api/users/:id", {id:'@id'});
    }])

    .factory('securityService', function(){
        var service = {};

        service.setConnectedUser = function(id){
            Conf.userInfo.id = id;
        }

        return service;
    });