///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    class AvailableHousesController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http'
        ];

        constructor(private $rootScope:any,
                    private $scope:any,
                    private $http:any) {
            this.$scope.weekPicker = new Utilities.WeekPicker([]);

            this.$scope.submit  = () => {
                var period = this.$scope.weekPicker.getReservationPeriod();
                this.$scope.availableSummerhouses = this.getAvailableSummerhouses(period.fromDate, period.untilDate);
            }
        }

        getAvailableSummerhouses(fromDate, untilDate : String): SummerHouse[]{
            this.$http.get('/rest/summerhouse/available?from=' + fromDate + '&until=' + untilDate).success((availableSummerhouses: SummerHouse[]) => {
                console.log(availableSummerhouses)
                return availableSummerhouses;
            });
            return [];
        }
    }

    angular
        .module('housesApp')
        .controller('availableHousesController', AvailableHousesController);
}