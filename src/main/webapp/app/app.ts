/// <reference path="../typings/angular.d.ts" />
/// <reference path="../typings/lodash.d.ts" />

module SummerHouses {

    var app = angular.module("housesApp", ['ngResource', 'ngRoute', 'swxSessionStorage', 'ui.bootstrap']);

    export class Application_PreLoad {

        private ngInjector:ng.auto.IInjectorService = angular.injector(['ng']);
        private $http:ng.IHttpService = this.ngInjector.get<ng.IHttpService>('$http');
        private $q:ng.IQService = this.ngInjector.get<ng.IQService>('$q');

        private static that:Application_PreLoad;


        constructor() {
            console.log("Application PreLoad!");

            Application_PreLoad.that = this;

            this.preload();
        }

        private preload() {
            Application_PreLoad.that.getAllRoutes().then(function (routes) {
                var app = angular.module("housesApp");
                app.constant('allRoutes', routes);

                angular.element(document).ready(function () {
                    angular.bootstrap(document.documentElement, ["housesApp"]);
                });
            });
        }

        private getAllRoutes() {
            return Application_PreLoad.that.$http.get('app/config/routes.json').then(function (response) {
                return response.data["routes"];
            });
        }
    }


    var Application = new Application_PreLoad();

    app.factory('authInterceptor', ['$sessionStorage', function ($sessionStorage) {
        return {
            request: function (config) {
                var authToken = $sessionStorage.get("AuthorizationToken");
                if (authToken) {
                    config.headers = config.headers || {};
                    config.headers.Authorization = "Token " + authToken;
                }

                return config;
            }
        };
    }]);

    app.config(function ($httpProvider) {
        $httpProvider.interceptors.push("authInterceptor");
    });

    app.run(['$route', function ($route) {
        $route.reload();
    }]);

    app.directive('weekPicker', function () {
        function link(scope, element, attrs) {
            element.datepicker({
                firstDay: 1,
                onSelect: function (dateText, inst) {
                    if (attrs.picker && scope[attrs.picker]) {
                        scope[attrs.picker].handleDateSelect(dateText);
                    }
                },
                beforeShowDay: function (date) {

                    if (attrs.picker && scope[attrs.picker] && scope[attrs.picker].dateIsUnavailable(date)) {
                        return [false];
                    }
                    if (attrs.picker && scope[attrs.picker] && scope[attrs.picker].hasDateSelected(date)) {
                        return [true, "selected-date"];
                    }
                    return [true, "unselected-date"];
                }
            });
        }
        
        return {
            link: link,
        };
    });
}