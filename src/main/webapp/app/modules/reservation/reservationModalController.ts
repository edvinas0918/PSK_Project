///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

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
            this.$scope.reservationPeriod.fromDateString =
                moment(this.$scope.reservationPeriod.fromDate).locale('LT').format('MMMM Do');
            this.$scope.reservationPeriod.untilDateString =
                moment(this.$scope.reservationPeriod.untilDate).locale('LT').format('MMMM Do');
            this.$scope.reserveSummerhouse = this.reserveSummerhouse;

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };
            
            this.$scope.formatDate = (date) => {
                return moment(date).locale("LT").format("MMMM DD, YYYY");
            };

            this.$scope.calculateSummerhousePrice = () => {
                var summerhouse = ReservationModalController.that.$scope.summerhouse;
                var reservationPeriod = ReservationModalController.that.$scope.reservationPeriod;
                var beginPeriod = reservationPeriod.fromDate;
                var endPeriod = reservationPeriod.untilDate;
                return (summerhouse.reservationPrice * ReservationModalController.that.getWeekDiff (beginPeriod, endPeriod));
            };

            this.$scope.calculateTotalPoints = (): number => {
                var serviceSum = _.reduce(summerhouse.additionalServiceReservations, function(sum, n) {
                    return sum + n.service.price;
                }, 0);
                return serviceSum + this.$scope.calculateSummerhousePrice();

            };


        }

        public additionalServiceDTOs():AdditionalServiceReservationDTO[] {
            var serviceDTOs = [];
            for (let serviceReservation of this.$scope.summerhouse.additionalServiceReservations) {
                //var date = moment(serviceReservation.serviceReservationStartDate).format("MMMM DD, YYYY");
                var date = (new Date(serviceReservation.serviceReservationStartDate)).getTime();
                var serviceDTO = new AdditionalServiceReservationDTO(serviceReservation.service.price, serviceReservation.service.id, date);
                serviceDTOs.push(serviceDTO);
            }
            return serviceDTOs;
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
            // reservation.fromDate = period.fromDate;
            // reservation.untilDate = period.untilDate;
            reservation.fromDate = (new Date(period.fromDate)).getTime();
            reservation.untilDate = (new Date(period.untilDate)).getTime();
            reservation.member = {};
            var servicesDTOS = ReservationModalController.that.additionalServiceDTOs();
            var params = {
                method: "POST",
                url: "rest/reservation",
                data: {"reservation": reservation, "additionalServiceReservationDTOs":  servicesDTOS},
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

    export class AdditionalServiceReservationDTO {
        constructor(public price:number, public additionalServiceID:number, public date:Date) {

        }
    }
    
    angular.module("housesApp")
        .controller("reservationModalController", ReservationModalController);
}