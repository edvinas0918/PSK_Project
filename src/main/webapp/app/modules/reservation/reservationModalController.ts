///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>

namespace SummerHouses {
    export class ReservationModalController {

        static that: ReservationModalController;

        static $inject = [
            '$scope',
            '$uibModalInstance',
            'summerhouse',
            'reservationPeriod',
            '$http'
        ];
        
        constructor (
            private $scope: any,
            private $uibModalInstance: any,
            private summerhouse: any,
            private reservationPeriod: any,
            private $http: ng.IHttpService
        ) {
            ReservationModalController.that = this;
            this.$scope.summerhouse = summerhouse;
            this.$scope.reservationPeriod = reservationPeriod;
            this.$scope.reserveSummerhouse = this.reserveSummerhouse;

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };
            
            this.$scope.formatDate = (date) => {
                return moment(date).locale("LT").format("MMMM DD, YYYY");
            };

            this.$scope.calculateTotalPoints = (): number => {
                var summerhouse = ReservationModalController.that.$scope.summerhouse;
                var reservationPeriod = ReservationModalController.that.$scope.reservationPeriod;
                var beginPeriod = reservationPeriod.fromDate;
                var endPeriod = reservationPeriod.untilDate;

                return summerhouse.reservationPrice * ReservationModalController.that.getWeekDiff (beginPeriod, endPeriod);

               /* for (var i in summerhouse.additionalServices) {
                    if (summerhouse.additionalServices[i].checked) {
                        totalPoints += summerhouse.additionalServices[i].tax.price;
                    }
                }*/
                
                //return totalPoints;
            }


        }

        public getWeekDiff(beginPeriod, endPeriod): number {
            var duration = moment.duration(moment(endPeriod).add(1, 'Days').diff(moment(beginPeriod)));
            return duration.asWeeks();
            //return 0;
        }

        public reserveSummerhouse(summerhouse):void {

            var period = ReservationModalController.that.reservationPeriod;
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

            ReservationModalController.that.$http(params).then(function (response) {
                ReservationModalController.that.$scope.isSuccesful = true;
            }, function (error) {
                ReservationModalController.that.$scope.isError = true;
                ReservationModalController.that.$scope.errorMessage = error.data.errorMessage;
            });

        }
    }
    
    angular.module("housesApp")
        .controller("reservationModalController", ReservationModalController);
}