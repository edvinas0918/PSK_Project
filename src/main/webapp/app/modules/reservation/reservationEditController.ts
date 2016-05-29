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
            
            this.$scope.openReservationForm = this.openReservationForm;
            this.$scope.additionalServiceReservations = [];

            this.getReservation();

            this.$scope.changeReservedServices = () => {
                var selectedServices = _.filter(this.additionalServiceReservations, function(reservation) { return reservation.selected == true });
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
                }).catch(error => {

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
                this.getAllAdditionalServices();
            });
        }

        private getReservedAdditionalServices(reservationID):AdditionalServiceReservation[] {
            this.$http.get('rest/servicesReservation/reservedServicesFor/' + reservationID).success((reservations: AdditionalServiceReservation[], status) => {

                this.createAdditionalServiceReservations(reservations);
            });
        }

        private getAllAdditionalServices():void {
            this.$http.get('rest/additionalservice').success((services:AdditionalService[], status) => {
                this.$http.get('rest/houseserviceprice/findSummerhouseServicePrices/' + this.$scope.reservation.summerhouse.id).success((prices:HouseServicePrice[], status) => {
                    var servicesWithPrices = [];
                    for (let service of services) {
                        for (let houseServicePrice of prices) {
                            if (houseServicePrice.additionalService.id == service.id) {
                                service.price = houseServicePrice.price;
                                servicesWithPrices.push(service);
                            }
                        }
                    }
                    this.$scope.additionalServices = servicesWithPrices;
                    this.getReservedAdditionalServices(this.$scope.reservation.id);
                });
            });
        }

        private createAdditionalServiceReservations(serviceReservations: AdditionalServiceReservation[]) {
            this.$scope.additionalServiceReservations = _.map(this.$scope.additionalServices, (service: AdditionalService) => {
                var serviceReservation = _.find(serviceReservations, (serviceReservation: AdditionalServiceReservation) => {
                    return service.id === serviceReservation.additionalService.id;
                });

                if (serviceReservation) {
                    serviceReservation.selected = true;
                    serviceReservation.startDate = new Date(serviceReservation.serviceStart);
                    serviceReservation.additionalService = service;
                    return serviceReservation;
                } else {
                    var possibleReservation = new AdditionalServiceReservation(service, this.$scope.reservation.fromDate);
                    possibleReservation.selected = false;
                    possibleReservation.startDate = new Date(this.$scope.reservation.fromDate);
                    return possibleReservation;
                }
            });
            this.additionalServiceReservations = this.$scope.additionalServiceReservations;
        }

        public allServicesHaveDate(reservations: AdditionalServiceReservation[]):Boolean {
            for(let serviceReservation of reservations) {
                if (!serviceReservation.startDate) { return false; }
            }
            return true;
        }

        public additionalServiceDTOs(reservations: AdditionalServiceReservation[]):AdditionalServiceReservationDTO[] {
            var serviceDTOs = [];
            for (let serviceReservation of reservations) {
                var date = moment(serviceReservation.serviceReservationStartDate).format("MMMM DD, YYYY");
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

    }

    export class AdditionalServiceReservation {
        constructor(public additionalService: AdditionalService, public serviceReservationStartDate: Date) {

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