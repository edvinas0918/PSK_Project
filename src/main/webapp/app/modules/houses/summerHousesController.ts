///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    class SummerHousesController {

        static that: SummerHousesController;

        static $inject = [
            '$rootScope',
            '$scope',
            '$http'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any
        ) {
            SummerHousesController.that = this;
            $scope.data = this.getSummerHouses()
            this.getSummerHouses();
        }

        getSummerHouses(): void{
            this.$http.get('/rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
                this.$scope.summerhouses = summerhouses;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}