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
                    ReservationController.that.getSummerhouseReservation(summerhouse.id).then((reservations) => {
                        ReservationController.that.$scope.summerhouse = summerhouse;
                        ReservationController.that.$scope.summerhouse.additionalServiceReservations = [];
                        //ReservationController.that.getReservedAdditionalServices(summerhouse.id);
                        ReservationController.that.getSummerhouseServices(summerhouse.id);
                        ReservationController.that.getSummerhouseReservationInfo(summerhouse.id);
                        ReservationController.that.$scope.additionalServiceReservations = [];
                        var disallowedPeriods = _.map(reservations, (reservation: any) => {
                            return {
                                fromDate: reservation.fromDate,
                                untilDate: reservation.untilDate
                            };
                        });
                        disallowedPeriods.push({
                            fromDate: moment().year() + "/01/01",
                            untilDate: moment(summerhouse.beginPeriod).year(moment().year()).format()
                        });
                        disallowedPeriods.push({
                            fromDate: moment(summerhouse.endPeriod).year(moment().year()).format(),
                            untilDate: moment().year() + "/12/31"
                        });
                        ReservationController.that.$scope.weekPicker = new Utilities.WeekPicker(
                            disallowedPeriods, "MMMM DD, YYYY", summerhouse.beginPeriod);

                        //ReservationController.that.getServicePricesForSummerhouse(summerhouse.id);
                        $("#reservationDatePicker").datepicker("refresh");
                        $("#reservationDatePicker").datepicker("setDate", moment(summerhouse.beginPeriod).toDate());

                        ReservationController.that.$scope.summerhouse.beginPeriodString =
                            moment(ReservationController.that.$scope.summerhouse.beginPeriod).locale('LT').format('MMMM Do');
                        ReservationController.that.$scope.summerhouse.endPeriodString =
                            moment(ReservationController.that.$scope.summerhouse.endPeriod).locale('LT').format('MMMM Do');

                    });
                });
            
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
                    var date = ReservationController.that.$scope.weekPicker.getReservationPeriod().untilDate;
                    date = moment(date).hour(23).minute(59).second(59).format();
                    return ReservationController.that.formatDate(date);
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
            this.$http.get('rest/reservation/additionalServices/' + summerhouseID).success((additionalServiceReservation:AdditionalServiceReservation[], status) => {
                console.log(additionalServiceReservation);
            });
        }

        private getSummerhouseServices(summerhouseID):void {
            this.$http.get('rest/houseserviceprice/findSummerhouseServicePrices/' + summerhouseID).success((prices:HouseServicePrice[], status) => {
                var services = [];
                for (let houseServicePrice of prices) {
                    let additionalService = houseServicePrice.additionalService;
                    additionalService.price = houseServicePrice.price;
                    services.push(new AdditionalServiceReservation(false, additionalService, null));
                }
                ReservationController.that.$scope.summerhouse.additionalServices = services;
            });
        }

        private getSummerhouseReservationInfo(summerhouseID):void {
            this.$http.get('rest/reservation/reservationInfoForSummerhouse/' + summerhouseID).success((reservationInfos:ReservationInfo[], status) => {
                for(let reservationInfo of reservationInfos) {
                    reservationInfo.fromDate = moment(reservationInfo.fromDate).locale('LT').format("MMMM DD, YYYY");
                    reservationInfo.untilDate = moment(reservationInfo.untilDate).locale('LT').format("MMMM DD, YYYY");
                }
                ReservationController.that.$scope.summerhouseReservations = reservationInfos;
            });
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
            var reservations = ReservationController.that.$scope.summerhouse.additionalServices;
            var checkedServices = _.filter(reservations, function (n) {
                return n.checked;
            });
            ReservationController.that.$scope.summerhouse.additionalServiceReservations = checkedServices;
            if (checkedServices && !ReservationController.that.checkIfAllServicesHaveDate(checkedServices)) {
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

        private getSummerhouseReservation(summerhouseID: string) {
            return this.$http.get('rest/reservation/summerhouse/' + summerhouseID)
                .then((response:any) => {
                console.log(response.data);
                    return response.data;
            });
        }
    }

    export class AdditionalServiceReservation {
        constructor(public checked: boolean, public service:AdditionalService, public serviceReservationStartDate:Date) {

        }
    }

    export class ReservationInfo {
        constructor(public firstName: string, public lastName:string, public fromDate: Date, public untilDate: Date) {

        }
    }

    angular
        .module('housesApp')
        .controller('reservationController', ReservationController);
}