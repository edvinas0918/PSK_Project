///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../../../typings/moment.d.ts"/>

module SummerHouses.houses {

    class SummerHousesController {

        static that: SummerHousesController;
        private summerhouses: SummerHouse[];

        static $inject = [
            '$rootScope',
            '$scope',
            '$http',
            '$route',
            '$location'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any,
            private $route: any,
            private $location: any
        ) {
            SummerHousesController.that = this;
            this.getSummerHouses();
            this.getTaxes();
            this.$scope.addEmptyHouse = () => {
                var sm: SummerHouse = {
                    description: null,
                    capacity: null,
                    number: null
                }
                $scope.summerhouses.push(sm);
                $scope.$apply();
            }
            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";
            this.$scope.deleteHouse = (house: SummerHouse) => {
                this.$http.delete('/rest/summerhouse/' + house.id).success(() => {
                    var index = this.$scope.summerhouses.indexOf(house, 0);
                    if (index > -1) {
                        this.$scope.summerhouses.splice(index, 1);
                    }
                });
            }

            this.$scope.editHouse = (house: SummerHouse) => {
                house.editMode = true;

            }

            this.$scope.previewHouse = (house: SummerHouse) => {
                this.$location.path("/previewHouse/" + house.id);
            }
            this.$scope.weekPicker = new Utilities.WeekPicker([{ fromDate: "2016-05-16", untilDate: "2016-05-22"}]);

        }

        getTaxes(): void{
            this.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                this.$scope.taxes = taxes;
            });
        }

        getSummerHouses(): void{
            this.$http.get('/rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
                for (let summerhouse of summerhouses) {
                    summerhouse.endPeriod = moment(summerhouse.endPeriod).format('MMMM Do');
                    summerhouse.beginPeriod = moment(summerhouse.beginPeriod).format('MMMM Do');
                }
                this.$scope.summerhouses = summerhouses;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}