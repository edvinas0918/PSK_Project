///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../houses/summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
module SummerHouses {
    import AdditionalService = SummerHouses.houses.AdditionalService;
    import AdditionalServiceReservation = SummerHouses.houses.AdditionalServiceReservation;
    export class ReservationController {
        static that:ReservationController;

        static $inject = [
            '$http',
            '$scope',
            '$routeParams',
            '$uibModal',
            'summerhouseService'
        ];

        constructor(private $http:ng.IHttpService,
                    private $scope:any,
                    private $routeParams:any,
                    private $uibModal: any,
                    private summerhouseService:SummerHouses.SummerhouseService) {
            ReservationController.that = this;

            this.$scope.reserveSummerhouse = this.reserveSummerhouse;
            this.$scope.openReservationForm = this.openReservationForm;

            summerhouseService.getSummerhouse(this.$routeParams.summerhouseID)
                .then(function (summerhouse) {
                    ReservationController.that.$scope.summerhouse = summerhouse;
                    //ReservationController.that.getReservedAdditionalServices(summerhouse.id);
                    ReservationController.that.$scope.additionalServiceReservations = new Array<AdditionalServiceReservation>();
                    ReservationController.that.$scope.weekPicker = new Utilities.WeekPicker(
                        [],
                        "MMMM DD, YYYY");
                    });

            this.$scope.manageService = (service:AdditionalServiceReservation) => {
                if (service.checked) {
                    ReservationController.that.$scope.additionalServiceReservations.push(service);
                } else {
                    _.remove(ReservationController.that.$scope.additionalServiceReservations, {'id': service.id});
                }
                ReservationController.that.$scope.summerhouse.additionalServiceReservations = ReservationController.that.$scope.additionalServiceReservations;
                console.log(ReservationController.that.$scope.additionalServiceReservations);
            };

            this.$scope.getFromDate = () => {
                try {
                    var date = ReservationController.that.$scope.weekPicker.getReservationPeriod();
                    return ReservationController.that.formatDate(date.fromDate);
                } catch (Exception) {
                    return null;
                }
            };

            this.$scope.getUntilDate = () => {
                try {
                    var date = ReservationController.that.$scope.weekPicker.getReservationPeriod();
                    return ReservationController.that.formatDate(date.untilDate);
                } catch (Exception) {
                    return null;
                }
            };

            this.$scope.isDateSelected = false;

            this.$scope.$on("dateChanged", () => {
                ReservationController.that.checkIsDateSelected();
                ReservationController.that.$scope.$apply();
            });
        }

        private checkIsDateSelected(): void {
            try {
                ReservationController.that.$scope.weekPicker.getReservationPeriod();
                ReservationController.that.$scope.isDateSelected = true;
            } catch (Exception) {
                ReservationController.that.$scope.isDateSelected = false;
            }
        }
        
        private formatDate(date): string {
            var dateString = moment(date).format();
            return dateString.substring(0, dateString.length - 6);
        }
        
        private getReservedAdditionalServices(summerhouseID):AdditionalServiceReservation[] {
            this.$http.get('/rest/reservation/additionalServices/' + summerhouseID).success((additionalServiceReservation: AdditionalServiceReservation[], status) => {
                console.log(additionalServiceReservation);
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

        public openReservationForm()  {
            ReservationController.that.$uibModal.open({
                templateUrl: 'app/modules/reservation/templates/reservationModal.html',
                controller: 'reservationController',
                resolve: {
                    summerhouse: () => {return ReservationController.that.$scope.summerhouse}
                }
            });
        };
    }

    angular
        .module('housesApp')
        .controller('reservationController', ReservationController);
}