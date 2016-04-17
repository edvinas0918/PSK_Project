///<reference path="../../../typings/angular.d.ts"/>

module SummerHouses.houses {

    class SummerHousesController {

        static that: SummerHousesController;

        static $inject = [
            '$rootScope',
            '$scope'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any
        ) {
            SummerHousesController.that = this;
            $scope.data = "SUMMER HOUSES CONTROLLER";
        }

    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}