///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

module SummerHouses.houses {

    class SummerHousesController {

        static that: SummerHousesController;
        private summerhouses: SummerHouse[];

        static $inject = [
            '$rootScope',
            '$scope',
            '$http',
            '$route'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any,
            private $route: any
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
                $scope.houseExists = false;
                for (let existingHouse of this.$scope.summerhouses) {
                    if(existingHouse.number == house.number && existingHouse.id) {
                        $scope.houseExists = true;
                        break;
                    }
                }
                if(!$scope.houseExists) {
                    this.$http.post('/rest/summerhouse/postHashMap',house).success(() => {
                        $route.reload();
                    });
                }
            }

            this.$scope.deleteHouse = (house: SummerHouse) => {
                this.$http.delete('/rest/summerhouse/' + house.id).success(() => {
                    $route.reload();
                });
            }

        }

        getTaxes(): void{
            this.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                this.$scope.taxes = taxes;
            });
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