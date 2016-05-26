///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="summerHouseModel.ts"/>
module SummerHouses.houses {
    class SummerHousePreviewController {

        static that:SummerHousePreviewController;

        static $inject = [
            '$routeParams',
            '$scope',
            '$http'
        ];

        constructor(private $routeParams, private $scope, private $http) {
            SummerHousePreviewController.that = this;
            this.$scope.houseID = this.$routeParams.houseID;
            this.getHouse(this.$routeParams.houseID);

        }

        getHouse(houseID:string):void {
            SummerHousePreviewController.that.$http.get('/rest/summerhouse/' + houseID).success((house:SummerHouse, status) => {
                house.endPeriod = moment(house.endPeriod).format('MMMM Do');
                house.beginPeriod = moment(house.beginPeriod).format('MMMM Do');
                SummerHousePreviewController.that.$scope.house = house;
                this.getAdditionalServices();
            });
        }

        getAdditionalServices():void {
            SummerHousePreviewController.that.$http.get('/rest/additionalservice').success((services:AdditionalService[], status) => {
                let house = SummerHousePreviewController.that.$scope.house;
                SummerHousePreviewController.that.$http.get('/rest/houseserviceprice/findSummerhouseServicePrices/' + house.id).success((prices:HouseServicePrice[], status) => {
                    let summerhouseServices = Array<AdditionalService>();
                    for (let service of services) {
                        for (let houseServicePrice of prices) {
                            if (houseServicePrice.additionalService.id == service.id) {
                                service.price = houseServicePrice.price;
                            }
                        }
                        for (let houseService of house.additionalServices) {
                            if (houseService.id == service.id) {
                                summerhouseServices.push(service);
                            }
                        }
                    }
                    SummerHousePreviewController.that.$scope.additionalServices = summerhouseServices;
                });
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHousePreviewController', SummerHousePreviewController);

}