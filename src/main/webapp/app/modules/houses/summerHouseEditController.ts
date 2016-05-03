///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    class SummerHouseEditController {

        static that: SummerHouseEditController;

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
            SummerHouseEditController.that = this;

            this.scope = $scope;
            this.httpService = $http;
            this.getHouse($routeParams.houseID);
            this.getSummerHouses();
            this.getTaxes();
            //this.getAdditionalServices();
            this.$scope.addService = (service: AdditionalService) => {
                SummerHouseEditController.that.scope.house.additionalServices.push(service);
            }
            this.$scope.saveHouse = (house: SummerHouse) => {
                if (house.beginPeriod > house.endPeriod) {
                    $scope.datesDoNotMatch = true;
                    return;
                }
                if (!house.id) {
                    $scope.houseWithNumberExists = false;
                    for (let existingHouse of SummerHouseEditController.that.$scope.summerhouses) {
                        if(existingHouse.number == house.number && existingHouse.id) {
                            $scope.houseWithNumberExists = true;
                            return;
                        }
                    }
                    if(!$scope.houseWithNumberExists) {
                        SummerHouseEditController.that.$http.post('/rest/summerhouse/postHashMap', house).success(() => {
                            $location.path("/houses");
                        });
                    }
                } else {
                    house.editMode = true;
                    this.$http.post('/rest/summerhouse/postHashMap', house).success(() => {
                        $location.path("/houses");
                    });
                }

            }

        }

        getTaxes(): void{
            SummerHouseEditController.that.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                SummerHouseEditController.that.$scope.taxes = taxes;
            });
        }

        getAdditionalServices(): void{
            SummerHouseEditController.that.$http.get('/rest/entities.additionalservice').success((services: AdditionalService[], status) => {
                var house = SummerHouseEditController.that.scope.house;
                for (let service of services) {
                    for (let houseService of house.additionalServices) {
                        if (houseService.id == service.id) {
                            service.selected = true;
                        }
                    }
                }
                SummerHouseEditController.that.$scope.additionalServices = services;
            });
        }

        getHouse(houseID: string): void{
            if (houseID != "0") {
                SummerHouseEditController.that.httpService.get('/rest/summerhouse/' + houseID).success((house: SummerHouse, status) => {
                    SummerHouseEditController.that.scope.house = house;
                    SummerHouseEditController.that.getAdditionalServices();
                });
            }
        }

        getSummerHouses(): void{
            SummerHouseEditController.that.$http.get('/rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
                SummerHouseEditController.that.$scope.summerhouses = summerhouses;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHouseEditController', SummerHouseEditController);
}