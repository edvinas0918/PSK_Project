///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../houses/summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
module SummerHouses {
    import AdditionalService = SummerHouses.houses.AdditionalService;
    import Tax = SummerHouses.houses.Tax;
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
                    private $uibModal:any,
                    private summerhouseService:SummerHouses.SummerhouseService) {
            ReservationController.that = this;

            this.$scope.reserveSummerhouse = this.reserveSummerhouse;
            this.$scope.openReservationForm = this.openReservationForm;

            summerhouseService.getSummerhouse(this.$routeParams.summerhouseID)
                .then(function (summerhouse) {
                    ReservationController.that.$scope.summerhouse = summerhouse;
                    //ReservationController.that.getReservedAdditionalServices(summerhouse.id);
                    ReservationController.that.getSummerhouseServices(summerhouse.id);
                    ReservationController.that.$scope.additionalServiceReservations = new Array<AdditionalServiceReservation>();
                    ReservationController.that.$scope.weekPicker = new Utilities.WeekPicker(
                        [
                            {
                                fromDate: moment().year() + "/01/01",
                                untilDate: moment(summerhouse.beginPeriod).year(moment().year()).format()
                            },
                            {
                                fromDate: moment(summerhouse.endPeriod).year(moment().year()).format(),
                                untilDate: moment().year() + "/12/31"
                            }
                        ],
                        "MMMM DD, YYYY");
                    //ReservationController.that.getServicePricesForSummerhouse(summerhouse.id);
                    $("#reservationDatePicker").datepicker("refresh");

                    ReservationController.that.$scope.summerhouse.beginPeriodString =
                        moment(ReservationController.that.$scope.summerhouse.beginPeriod).locale('LT').format('MMMM Do');
                    ReservationController.that.$scope.summerhouse.endPeriodString =
                        moment(ReservationController.that.$scope.summerhouse.endPeriod).locale('LT').format('MMMM Do');

                });

            this.$scope.manageService = (service:AdditionalService) => {
                var serviceReservation = new AdditionalServiceReservation(service, service.serviceBegin);
                if (service.checked) {
                    ReservationController.that.$scope.additionalServiceReservations.push(serviceReservation);
                } else {
                    _.remove(ReservationController.that.$scope.additionalServiceReservations, function (n) {
                        return n.service.id == service.id;
                    });
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

        private checkIsDateSelected():void {
            try {
                ReservationController.that.$scope.weekPicker.getReservationPeriod();
                ReservationController.that.$scope.isDateSelected = true;
            } catch (Exception) {
                ReservationController.that.$scope.isDateSelected = false;
            }
        }

        private formatDate(date):string {
            var dateString = moment(date).format();
            return dateString.substring(0, dateString.length - 6);
        }

        private getReservedAdditionalServices(summerhouseID):AdditionalServiceReservation[] {
            this.$http.get('/rest/reservation/additionalServices/' + summerhouseID).success((additionalServiceReservation:AdditionalServiceReservation[], status) => {
                console.log(additionalServiceReservation);
            });
        }

        private getSummerhouseServices(summerhouseID):void {
            this.$http.get('/rest/houseserviceprice/findSummerhouseServicePrices/' + summerhouseID).success((prices:HouseServicePrice[], status) => {
                var services = new Array<AdditionalService>();
                for (let houseServicePrice of prices) {
                    let additionalService = houseServicePrice.additionalService;
                    additionalService.price = houseServicePrice.price;
                    services.push(additionalService);
                }
                ReservationController.that.$scope.summerhouse.additionalServices = services;
            });
        }

        private getServicePricesForSummerhouse(summerhouseID):void {
            /*this.$http.get('/rest/houseserviceprice/summerhouseServicesWithTaxes/' + summerhouseID).success((servicesTax:AdditionalServiceTax[], status) => {
                var services = Array<AdditionalService>();
                for (let service of servicesTax) {
                    service.key.tax = service.value;
                    services.push(service.key);
                }
                ReservationController.that.$scope.summerhouse.additionalServices = services;
            });*/
        }

        public checkIfAllServicesHaveDate(reservations:AdditionalServiceReservation[]):Boolean {
            for (let serviceReservation of reservations) {
                if (!serviceReservation.serviceReservationStartDate) {
                    return false;
                }
            }
            return true;
        }

        public openReservationForm() {
            var reservations = ReservationController.that.$scope.summerhouse.additionalServiceReservations;
            if (reservations && !ReservationController.that.checkIfAllServicesHaveDate(reservations)) {
                ReservationController.that.$scope.selectDates = true;
                return;
            } else {
                ReservationController.that.$scope.selectDates = false;
            }
            ReservationController.that.$uibModal.open({
                templateUrl: 'app/modules/reservation/templates/reservationModal.html',
                controller: 'reservationModalController',
                resolve: {
                    summerhouse: () => {
                        return ReservationController.that.$scope.summerhouse
                    },
                    reservationPeriod: () => {
                        return ReservationController.that.$scope.weekPicker.getReservationPeriod()
                    }
                }
            });
        };
    }

    export class AdditionalServiceReservation {
        constructor(public service:AdditionalService, public serviceReservationStartDate:Date) {

        }
    }

    export class AdditionalServiceTax {
        constructor(public key:AdditionalService, public value:Tax) {

        }
    }

    angular
        .module('housesApp')
        .controller('reservationController', ReservationController);
}