///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    class SummerHouseEditController {

        static that:SummerHouseEditController;

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$http',
            '$route',
            '$location'
        ];
        private scope:any;
        private httpService:any;

        constructor(private $rootScope:any,
                    private $scope:any,
                    private $routeParams:any,
                    private $http:any,
                    private $route:any,
                    private $location:any) {
            SummerHouseEditController.that = this;

            this.scope = $scope;
            this.httpService = $http;
            //SummerHouseEditController.that.$scope.additionalServices = new Array<AdditionalService>();
            if ($routeParams.houseID != "0") {
                this.getHouse($routeParams.houseID);
            } else {
                SummerHouseEditController.that.scope.house = new SummerHouse(new Date(), new Date(), "", 0, 0, null, []);
                this.getAllAdditionalServices();
            }
            this.getSummerHouses();
            this.getTaxes();
            this.$scope.manageService = (service:AdditionalService) => {
                if (_.filter(SummerHouseEditController.that.scope.house.additionalServices, {'id': service.id}).length > 0) {
                    _.remove(SummerHouseEditController.that.scope.house.additionalServices, {'id': service.id});
                } else {
                    if (!SummerHouseEditController.that.scope.house.additionalServices) {
                        SummerHouseEditController.that.scope.house.additionalServices = [];
                    }
                    SummerHouseEditController.that.scope.house.additionalServices.push(service);
                }
            };
            this.$scope.saveHouse = (house:SummerHouse) => {
                if (house.beginPeriod > house.endPeriod) {
                    $scope.datesDoNotMatch = true;
                    return;
                }
                if (!house.id && SummerHouseEditController.that.$scope.summerhouses) {
                    $scope.houseWithNumberExists = false;
                    for (let existingHouse of SummerHouseEditController.that.$scope.summerhouses) {
                        if (existingHouse.number == house.number && existingHouse.id) {
                            $scope.houseWithNumberExists = true;
                            return;
                        }
                    }
                }
                house.endPeriod = this.createDateAsUTC(house.endPeriod);
                house.beginPeriod = this.createDateAsUTC(house.beginPeriod);
                if($scope.houseImage){
                    this.saveHouseWithImage(house);
                } else {
                    SummerHouseEditController.that.postHouse(house);
                }
            }
        }

        createDateAsUTC(date: Date):Date {
            return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds()));
        }


        saveHouseWithImage(house: SummerHouse):void {
            var file = SummerHouseEditController.that.$scope.houseImage;
            var fr = new FileReader();
            fr.readAsDataURL(file);
            fr.onload = function () {
                house.image = fr.result;
                SummerHouseEditController.that.postHouse(house);
            };
        }

        postHouse(house: SummerHouse):void {
            SummerHouseEditController.that.$http.post('/rest/summerhouse/postHashMap', house).success((houseID:number, status) => {
                if (SummerHouseEditController.that.$scope.additionalServices) {
                    var houseServicePrices = Array<HouseServicePrice>();
                    for (let service of SummerHouseEditController.that.$scope.additionalServices) {
                        houseServicePrices.push(new HouseServicePrice(houseID,service.id,service.pricePoints));
                    }
                    SummerHouseEditController.that.$http.post('/rest/houseserviceprice/handleServicePrices', houseServicePrices).success(() => {
                        SummerHouseEditController.that.$location.path("/houses");
                    });
                } else {
                    SummerHouseEditController.that.$location.path("/houses");
                }
            });
        }

        getTaxes():void {
            SummerHouseEditController.that.$http.get('/rest/entities.tax').success((taxes:Tax[], status) => {
                SummerHouseEditController.that.$scope.taxes = taxes;
            });
        }

        getAdditionalServices():void {
            SummerHouseEditController.that.$http.get('/rest/additionalservice').success((services:AdditionalService[], status) => {
                let house = SummerHouseEditController.that.scope.house;
                SummerHouseEditController.that.$http.get('/rest/houseserviceprice/findSummerhouseServices/' + house.id).success((prices:HouseServicePrice[], status) => {
                    for (let service of services) {
                        for (let houseServicePrice of prices) {
                            if (houseServicePrice.houseServicePricePK.serviceID == service.id) {
                                service.pricePoints = houseServicePrice.price;
                            }
                        }
                        for (let houseService of house.additionalServices) {
                            if (houseService.id == service.id) {
                                service.selected = true;
                            }
                        }
                    }
                    SummerHouseEditController.that.$scope.additionalServices = services;
                });
            });
        }

        getAllAdditionalServices():void {
            SummerHouseEditController.that.$http.get('/rest/additionalservice').success((services:AdditionalService[], status) => {
                SummerHouseEditController.that.$scope.additionalServices = services;
            });
        }

        getHouse(houseID:string):void {
            SummerHouseEditController.that.httpService.get('/rest/summerhouse/' + houseID).success((house:SummerHouse, status) => {
                house.endPeriod = new Date(house.endPeriod);
                house.beginPeriod = new Date(house.beginPeriod);
                SummerHouseEditController.that.scope.house = house;
                SummerHouseEditController.that.getAdditionalServices();
            });
        }

        getSummerHouses():void {
            SummerHouseEditController.that.$http.get('/rest/summerhouse').success((summerhouses:SummerHouse[], status) => {
                SummerHouseEditController.that.$scope.summerhouses = summerhouses;
            });
        }


    }

    function fileParse($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }

    angular
        .module('housesApp')
        .controller('summerHouseEditController', SummerHouseEditController)
        .directive('fileModel', fileParse);
}