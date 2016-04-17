
/// <reference path="../typings/angular.d.ts" />

module SummerHouses {

    angular.module("housesApp", ['ngResource', 'ngRoute']);

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
    angular.module("housesApp").run(['$route', function($route)  {
        $route.reload();
    }]);
}