///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    class SummerHouseEditController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$http',
            '$route',
            '$location'
        ];
        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $routeParams: any,
            private $http: any,
            private $route: any,
            private $location: any
        ) {
            this.scope = $scope;
            this.httpService = $http;
            this.getHouse($routeParams.houseID);
            this.getSummerHouses();
            this.getTaxes();
            this.$scope.saveHouse = (house: SummerHouse) => {
                if (house.beginPeriod > house.endPeriod) {
                    $scope.datesDoNotMatch = true;
                    return;
                }
                if (!house.id) {
                    $scope.houseWithNumberExists = false;
                    for (let existingHouse of this.$scope.summerhouses) {
                        if(existingHouse.number == house.number && existingHouse.id) {
                            $scope.houseWithNumberExists = true;
                            return;
                        }
                    }
                    if(!$scope.houseWithNumberExists) {
                        this.$http.post('/rest/summerhouse/postHashMap', house).success(() => {
                            $location.path("/houses");
                        });
                    }
                } else {
                    house.editMode = true
                    this.$http.post('/rest/summerhouse/postHashMap', house).success(() => {
                        $location.path("/houses");
                    });
                }

            }

        }

        getTaxes(): void{
            this.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                this.$scope.taxes = taxes;
            });
        }

        getHouse(houseID: string): void{
            if (houseID != "0") {
                this.httpService.get('/rest/summerhouse/' + houseID).success((house: SummerHouse, status) => {
                    this.scope.house = house;
                });
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
        .controller('summerHouseEditController', SummerHouseEditController);
}