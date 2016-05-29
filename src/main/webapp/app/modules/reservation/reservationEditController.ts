///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../houses/summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
module SummerHouses {
    import AdditionalService = SummerHouses.houses.AdditionalService;
    export class ReservationEditController {
        static that:ReservationEditController;

        static $inject = [
            '$http',
            '$scope',
            '$routeParams',
            '$uibModal',
            'summerhouseService'
        ];

        public additionalServiceReservations: AdditionalServiceReservation[];
        public reservation: any;
        constructor(private $http:ng.IHttpService,
                    private $scope:any,
                    private $routeParams:any,
                    private $uibModal: any,
                    private summerhouseService:SummerHouses.SummerhouseService) {

            ReservationEditController.that = this;

            this.$scope.openReservationForm = this.openReservationForm;
            this.$scope.additionalServiceReservations = [];

            this.getReservation();

            this.$scope.changeReservedServices = () => {
                var selectedServices = _.filter(this.$scope.additionalServiceReservations, function(reservation) {
                    return reservation.checked;
                });
                if(selectedServices && !this.allServicesHaveDate(selectedServices)) {
                    this.$scope.selectDates = true;
                    return;
                } else {
                    this.$scope.selectDates = false;
                }

                var params = {
                    method: "POST",
                    url: "rest/servicesReservation/handleServices",
                    data: {"reservationID": this.reservation.id, "additionalServiceReservationDTOs": this.additionalServiceDTOs(selectedServices)},
                    headers: {
                        'Content-Type': "application/json"
                    }
                };
                this.$http(params).then((response) => {
                    this.$scope.isSuccesful = true;
                    this.showSuccessMessage("Pakeitimai išsaugoti.");
                }).catch(response => {
                    this.showErrorMessage(response.data.errorMessage ? response.data.errorMessage : "Sistemos klaida.");
            });
            };

            this.$scope.manageService = (service:AdditionalService) => {
                var serviceReservation = new AdditionalServiceReservation(service, service.serviceBegin);
                if (service.checked) {
                    this.$scope.additionalServiceReservations.push(serviceReservation);
                } else {
                    _.remove(this.$scope.additionalServiceReservations, function(n) {
                        return n.service.id == service.id;
                    });
                }
                this.$scope.summerhouse.additionalServiceReservations = this.$scope.additionalServiceReservations;
            };

            this.$scope.getDateString = (date: any) => {
                return moment(date).format("YYYY-MM-DD");
            }

        }
        
        private formatDate(date): string {
            var dateString = moment(date).format();
            return dateString.substring(0, dateString.length - 6);
        }

        private getReservation(){
            this.$http.get('rest/reservation/' + this.$routeParams.reservationID).then((response) => {
                this.$scope.reservation = response.data;
                this.$scope.reservation.fromDateString = moment(this.$scope.reservation.fromDate).locale('LT').format('MMMM Do');
                this.$scope.reservation.untilDateString = moment(this.$scope.reservation.untilDate).locale('LT').format('MMMM Do');
                this.reservation = this.$scope.reservation;
                this.getSummerhouseServices(this.reservation.summerhouse.id);
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
                ReservationEditController.that.$scope.reservation.summerhouse.additionalServices = services;
                ReservationEditController.that.getReservedAdditionalServices(ReservationEditController.that.$scope.reservation.id);
            });
        }

        private getReservedAdditionalServices(reservationID):AdditionalServiceReservation[] {
            this.$http.get('rest/servicesReservation/reservedServicesFor/' + reservationID).success((reservations: AdditionalServiceReservation[], status) => {
                this.createAdditionalServiceReservations(reservations);
            });
        }

        private createAdditionalServiceReservations(serviceReservations: AdditionalServiceReservation[]) {
            var serviceArray = new Array();
            for (let summerhouseService of ReservationEditController.that.$scope.reservation.summerhouse.additionalServices) {
                var reservedService = _.find(serviceReservations, (serviceReservation:AdditionalServiceReservation) => {
                    return serviceReservation.additionalService.id === summerhouseService.additionalService.id;
                });
                if (reservedService) {
                    reservedService.serviceReservationStartDate = new Date(reservedService.serviceStart);
                    reservedService.additionalService.price = reservedService.payment.price;
                    reservedService.checked = true;
                    serviceArray.push(reservedService);
                } else {
                    summerhouseService.serviceReservationStartDate = new Date(this.$scope.reservation.fromDate);
                    serviceArray.push(summerhouseService);
                }
            }
            this.$scope.additionalServiceReservations = serviceArray;
        }

        public allServicesHaveDate(reservations: AdditionalServiceReservation[]):Boolean {
            for(let serviceReservation of reservations) {
                if (!serviceReservation.serviceReservationStartDate) { return false; }
            }
            return true;
        }

        public additionalServiceDTOs(reservations: AdditionalServiceReservation[]):AdditionalServiceReservationDTO[] {
            var serviceDTOs = [];
            for (let serviceReservation of reservations) {
                var date = (new Date(serviceReservation.serviceReservationStartDate)).getTime();
                var serviceDTO = new AdditionalServiceReservationDTO(serviceReservation.id, serviceReservation.additionalService.price, serviceReservation.additionalService.id, date);
                serviceDTOs.push(serviceDTO);
            }
            return serviceDTOs;
        }

        public openReservationForm()  {
            var reservations = this.$scope.summerhouse.additionalServiceReservations;
            if(reservations && !this.allServicesHaveDate(reservations)) {
                this.$scope.selectDates = true;
                return;
            } else {
                this.$scope.selectDates = false;
            }
            this.$uibModal.open({
                templateUrl: 'app/modules/reservation/templates/reservationModal.html',
                controller: 'reservationModalController',
                resolve: {
                    summerhouse: () => {return this.$scope.summerhouse},
                    reservationPeriod: () => {return this.$scope.weekPicker.getReservationPeriod()}
                }
            });
        };

        public showSuccessMessage(message) {
            this.$scope.showAlert = true;
            this.$scope.successMessage = message;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                });
            }, 4000);
        }

        public showErrorMessage(message) {
            this.$scope.showError = true;
            this.$scope.errorMessage = message;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showError = false;
                });
            }, 4000);
        }


    }

    export class AdditionalServiceReservation {
        constructor(public checked: boolean, public additionalService: AdditionalService, public serviceReservationStartDate: Date) {

        }
    }

    export class AdditionalServiceReservationDTO {
        constructor(public serviceReservationID: number, public price:number, public additionalServiceID:number, public date:Date) {

        }
    }

    angular
        .module('housesApp')
        .controller('reservationEditController', ReservationEditController);
}