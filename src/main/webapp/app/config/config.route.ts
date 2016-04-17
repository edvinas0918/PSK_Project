/// <reference path="../../typings/angular.d.ts" />

module SummerHouses.config {
    'use strict';

    export class RouteConfigurator {

        static that: RouteConfigurator;

        static $inject = ['$routeProvider', 'allRoutes', 'routeResolverProvider'];

        static Configurator($routeProvider:any,
                            allRoutes:any,
                            routeResolverProvider:any) {

            RouteConfigurator.that = this;

            allRoutes.forEach(function (route) {
                setRoute(route.url, route.config, routeResolverProvider);
            });

            $routeProvider.otherwise({ redirectTo: '/error' });


            function setRoute(url, definition, routeResolver) {
                definition.resolve = {
                    defaultRouteResolver: routeResolver.defaultRouteResolver
                };

                $routeProvider.when(url, definition);
            }
        }
    }

    var app = angular.module('housesApp');

    app.config(['$routeProvider', 'allRoutes', 'routeResolverProvider', RouteConfigurator.Configurator]);
}