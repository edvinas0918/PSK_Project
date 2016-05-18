///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>

module SummerHouses {
    export class ReservationController {
        static that:ReservationController;

        static $inject = [
            '$http',
            '$scope',
            '$routeParams',
            'summerhouseService'
        ];

        constructor(private $http:ng.IHttpService,
                    private $scope:any,
                    private $routeParams:any,
                    private summerhouseService:SummerHouses.SummerhouseService) {
            ReservationController.that = this;

            this.$scope.reserveSummerhouse = this.reserveSummerhouse;

            summerhouseService.getSummerhouse(this.$routeParams.summerhouseID)
                .then(function (summerhouse) {
                    ReservationController.that.$scope.summerhouse = summerhouse;
                    ReservationController.that.$scope.weekPicker = new Utilities.WeekPicker(
                        [],
                        "MMMM DD, YYYY");
                    });

        }

        public reserveSummerhouse(summerhouse):void {

            var period = ReservationController.that.$scope.weekPicker.getReservationPeriod();
            var reservation = {};
            reservation.summerhouse = summerhouse;
            reservation.fromDate = period.fromDate;
            reservation.untilDate = period.untilDate;
            reservation.member = {};

            var params = {
                method: "POST",
                url: "rest/reservation",
                data: reservation,
                headers: {
                    'Content-Type': "application/json"
                }
            };

            ReservationController.that.$http(params).then(function (response) {
                console.log(response);
            }, function (error) {
                console.log(error);
                console.log(error.data.errorMessage);
            });

        }
    }

    angular
        .module('housesApp')
        .controller('reservationController', ReservationController);
}