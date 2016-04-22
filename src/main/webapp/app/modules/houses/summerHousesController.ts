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
                var config = {
                    headers : {
                        'Content-Type': 'application/json;'
                    }
                }
                this.$http.post('/rest/summerhouse/postHashMap',house).success(() => {});
            }

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