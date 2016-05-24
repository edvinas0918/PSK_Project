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

        constructor(private $http:ng.IHttpService,
                    private $scope:any,
                    private $routeParams:any,
                    private $uibModal: any,
                    private summerhouseService:SummerHouses.SummerhouseService) {
            
            this.$scope.openReservationForm = this.openReservationForm;
            this.$scope.additionalServiceReservations = [];
            
            this.getReservation();

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
                console.log(this.$scope.additionalServiceReservations);
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

                this.getReservedAdditionalServices(this.$scope.reservation.id);
            });
        }

        private getReservedAdditionalServices(reservationID):AdditionalServiceReservation[] {
            this.$http.get('/rest/reservation/additionalServices/' + reservationID).success((reservations: AdditionalServiceReservation[], status) => {

                this.createAdditionalServiceReservations(reservations);
            });
        }

        private createAdditionalServiceReservations(pastReservations: AdditionalServiceReservation[]) {
            this.$scope.additionalServiceReservations = _.map(this.$scope.reservation.summerhouse.additionalServices,
                (service: AdditionalService) => {
                var pastReservation = _.find(pastReservations, (reserved: AdditionalServiceReservation) => {
                    return service.id === reserved.additionalServiceID;
                });
                if (pastReservation){
                    pastReservation.selected = true;
                    pastReservation.startDate = new Date(pastReservation.serviceStart);
                    return pastReservation;
                } else{
                    var unmadeReservation = new AdditionalServiceReservation(service, this.$scope.reservation.fromDate);
                    unmadeReservation.startDate = new Date(this.$scope.reservation.fromDate);
                    unmadeReservation.selected = false;
                    return unmadeReservation;
                }
            })
        }

        public checkIfAllServicesHaveDate(reservations: AdditionalServiceReservation[]):Boolean {
            for(let serviceReservation of reservations) {
                if (!serviceReservation.serviceReservationStartDate) { return false; }
            }
            return true;
        }

        public openReservationForm()  {
            var reservations = this.$scope.summerhouse.additionalServiceReservations;
            if(reservations && !this.checkIfAllServicesHaveDate(reservations)) {
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
    }

    export class AdditionalServiceReservation {
        constructor(public service: AdditionalService, public serviceReservationStartDate: Date) {

        }
    }

    angular
        .module('housesApp')
        .controller('reservationEditController', ReservationEditController);
}