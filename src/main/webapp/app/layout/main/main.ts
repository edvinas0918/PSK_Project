/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {
    "use strict";

    export class MainController {

        static $inject = ['$scope', 'sh-authentication-service'];

        private authenticated:boolean;

        //$scope.authenticated = true;

        constructor(
            private $scope:any,
            private authService: any
        ) {
            $scope.authenticated = true;
           // var vm = this;
          //  this.authenticated = true;

            authService.getFullName().then((fullName:string) => {
                this.$scope.fullName = fullName;
            }, (error) => {
                this.$scope.fullName = "shittyMittyAnonymous";
            });
        }
    }

    angular.module("housesApp").controller("main", MainController);
}