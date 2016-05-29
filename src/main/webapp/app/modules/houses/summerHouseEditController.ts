///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
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
            SummerHouseEditController.that.scope.additionalServices= new Array<AdditionalService>();
            if ($routeParams.houseID != "0") {
                this.getHouse($routeParams.houseID);
            } else {
                SummerHouseEditController.that.scope.house = new SummerHouse(new Date(), new Date(), "", 0, 0, null, []);
                this.getAllAdditionalServices();
            }
            this.getSummerHouses();
            this.$scope.manageService = (service:AdditionalService) => {
                var houseServicePrices = SummerHouseEditController.that.scope.house.houseServicePrices;
                var matchingHouseServicePrice = _.filter(houseServicePrices, function(houseServicePrice) { return houseServicePrice.serviceID == service.id; })[0];
                if (matchingHouseServicePrice) {
                    _.remove(houseServicePrices, matchingHouseServicePrice);
                } else {
                    houseServicePrices.push(new HouseServicePrice(null, SummerHouseEditController.that.scope.house.id, service.id, service.price));
                }
            };
            this.$scope.saveHouse = (house:SummerHouse) => {

                //Tikrinam ar yra bent savaite tarp datu
                var beginDateMoment = moment(house.beginPeriod);
                var endDateMoment = moment(house.endPeriod);

                var duration = moment.duration(beginDateMoment.diff(endDateMoment));
                var days = duration.asDays();

                if (house.beginPeriod > house.endPeriod && Math.abs(days) > 7) {
                    $scope.datesDoNotMatch = true;
                    return;
                }

                //Tikrinam ar yra house su tokiu pat numeriu
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

                if ($scope.houseImage) {
                    this.saveHouseWithImage(house);
                } else {
                    SummerHouseEditController.that.postHouse(house);
                }

            }
        }

        createDateAsUTC(date:Date):Date {
            return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds()));
        }


        saveHouseWithImage(house:SummerHouse):void {
            var file = SummerHouseEditController.that.$scope.houseImage;
            var fr = new FileReader();
            fr.readAsDataURL(file);
            fr.onload = function () {
                house.image = fr.result;
                SummerHouseEditController.that.postHouse(house);
            };
        }

        postHouse(house:SummerHouse):void {
            SummerHouseEditController.that.$http.post('/rest/summerhouse/postHashMap', house).then((response:any, status) => {
                var houseID = response.data.houseID;
                let houseServicePrices = house.houseServicePrices;
                for (let houseServicePrice of houseServicePrices) {
                    houseServicePrice.houseID = houseID;
                    for (let service of SummerHouseEditController.that.$scope.additionalServices) {
                        if (service.id == houseServicePrice.serviceID) {
                            houseServicePrice.price = service.price;
                        }
                    }
                }
                if (houseServicePrices) {
                    SummerHouseEditController.that.$http.post('/rest/houseserviceprice/handleServicePrices', houseServicePrices).success(() => {
                        SummerHouseEditController.that.$location.path("/admin/houses");
                    });
                } else {
                    SummerHouseEditController.that.$location.path("/admin/houses");
                }
            }).catch((exc) => {
                console.log(exc);
            });
        }

        getAdditionalServices():void {
            SummerHouseEditController.that.$http.get('/rest/additionalservice').success((services:AdditionalService[], status) => {
                let house = SummerHouseEditController.that.scope.house;
                SummerHouseEditController.that.$http.get('/rest/houseserviceprice/findSummerhouseServicePrices/' + house.id).success((prices:HouseServicePrice[], status) => {
                    for (let service of services) {
                        for (let houseServicePrice of prices) {
                            if (houseServicePrice.additionalService.id == service.id) {
                                //public id: number, public optLockVersion: number, public houseID: number, public serviceID: number, public price: number
                                house.houseServicePrices.push(new HouseServicePrice(houseServicePrice.id, house.id, service.id, houseServicePrice.price));
                                service.price = houseServicePrice.price;
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
                SummerHouseEditController.that.scope.house.houseServicePrices = new Array<HouseServicePrice>();
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