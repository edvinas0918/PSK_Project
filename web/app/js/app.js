/**
 * Created by Edvinas.Barickis on 4/3/2016.
 */

'use strict';

var housesApp = angular.module("housesApp", ['ngResource', 'ngRoute'])
    .config (function ($routeProvider) {
        $routeProvider.when ('/info', {
            templateUrl: 'app/templates/info.html',
            controller: 'infoController'
        });

        $routeProvider.when ('/menu', {
            templateUrl: 'app/templates/menu.html',
            controller: 'menuController'
        });
    });