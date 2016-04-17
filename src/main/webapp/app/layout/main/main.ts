/// <reference path="../../typings/angular.d.ts" />

module SummerHouses {
    "use strict";

    export class MainController {

        static $inject = ['$scope'];

        private authenticated:boolean;

        //$scope.authenticated = true;

        constructor(
            private $scope:any
        ) {
            $scope.authenticated = true;
           // var vm = this;
          //  this.authenticated = true;

            this.activate();
        }

        private activate = ():void => {

        }
    }

    angular.module("housesApp").controller("main", MainController);
}