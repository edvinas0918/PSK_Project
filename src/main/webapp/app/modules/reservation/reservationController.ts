///<reference path="../../../typings/angular.d.ts"/>

module SummerHouses {
    export class ReservationController {
        static that: ReservationController;

        static $inject = [
            '$http',
            '$scope',
            '$routeParams',
            'summerhouseService'
        ];

        constructor(
            private $http: ng.IHttpService,
            private $scope: any,
            private $routeParams:any,
            private summerhouseService: SummerHouses.SummerhouseService
        ) {
            ReservationController.that = this;

            this.$scope.reserveSummerhouse = this.reserveSummerhouse;

            summerhouseService.getSummerhouse(this.$routeParams.summerhouseID)
                .then(function (summerhouse) {
                    ReservationController.that.$scope.summerhouse = summerhouse;
                });
        }

        public reserveSummerhouse(summerhouse): void {
            var params = {
                method: "POST",
                url: "rest/reservation",
                data: summerhouse,
                headers: {
                    'Content-Type': "application/json"
                }
            };

            ReservationController.that.$http(params).then(function (response) {
                console.log(response);
            });

        }
    }

    angular
        .module('housesApp')
        .controller('reservationController', ReservationController);
}