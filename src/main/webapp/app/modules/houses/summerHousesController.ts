///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

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
            this.getSummerHouses();
            this.getTaxes();
            this.$scope.addEmptyHouse = () => {
                var sm: SummerHouse = {
                    availabilityPeriod: null,
                    description: null,
                    capacity: null,
                    number: null
                }
                $scope.summerhouses.push(sm);
                $scope.$apply();
            }

            this.$scope.saveHouse = (house: SummerHouse) => {
                this.$http.post('/rest/summerhouse/postHashMap',house).success(() => {
                    this.$scope.$apply();
                });
            }

        }

        getSummerHouses(): void{
            this.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                this.$scope.taxes = taxes;
            });
        }

        getTaxes(): void{
            this.$http.get('/rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
                this.$scope.summerhouses = summerhouses;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}