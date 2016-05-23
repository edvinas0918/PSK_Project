///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../../../typings/moment.d.ts"/>

module SummerHouses.houses {

    class SummerHousesController {

        static that: SummerHousesController;
        private summerhouses: SummerHouse[];

        static $inject = [
            '$rootScope',
            '$scope',
            '$http',
            '$route',
            '$location',
            '$uibModal',
            'sh-authentication-service',
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any,
            private $route: any,
            private $location: any,
            private $uibModal: any,
            private authService: any
        ) {
            SummerHousesController.that = this;
            this.$scope.view = "all";
            this.$scope.userReservations = [];

            this.getSummerHouses();
            this.getTaxes();
            this.getUserReservations();
            this.$scope.addEmptyHouse = () => {
                var sm: SummerHouse = {
                    description: null,
                    capacity: null,
                    number: null
                };
                $scope.summerhouses.push(sm);
                $scope.$apply();
            };
            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";
            this.$scope.deleteHouse = (house: SummerHouse) => {
                this.$http.delete('/rest/summerhouse/' + house.id).success(() => {
                    var index = this.$scope.summerhouses.indexOf(house, 0);
                    if (index > -1) {
                        this.$scope.summerhouses.splice(index, 1);
                    }
                });
            };

            this.$scope.editHouse = (house: SummerHouse) => {
                house.editMode = true;

            };

            this.$scope.previewHouse = (house: SummerHouse) => {
                this.$location.path("/previewHouse/" + house.id);
            };

            this.$scope.openSeachForm = () => {
                var modalInstance = this.$uibModal.open({
                    templateUrl: 'app/modules/houses/templates/availableHouses.html',
                    controller: 'availableHousesController'
                });

                modalInstance.result.then((result) =>{
                    this.$scope.summerhouses = result;
                });
            }


            this.$scope.showView = (viewName: string) => {
                this.$scope.view = viewName;
            }
        }

        getTaxes(): void{
            this.$http.get('/rest/entities.tax').success((taxes: Tax[], status) => {
                this.$scope.taxes = taxes;
            });
        }

        getSummerHouses(): void{
            this.$http.get('/rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
                for (let summerhouse of summerhouses) {
                    summerhouse.endPeriod = moment(summerhouse.endPeriod).locale('LT').format('MMMM Do');
                    summerhouse.beginPeriod = moment(summerhouse.beginPeriod).locale('LT').format('MMMM Do');
                }
                this.$scope.summerhouses = summerhouses;
            });
        }

        getUserReservations(): void{
            this.authService.getUser().then((user) => {
                var userId = user.id;
                this.$http.get('/rest/reservation/clubmember/' + userId).then((response) => {
                    _.forEach(response.data, (reservation) => {
                        reservation.summerhouse.beginPeriod = moment(reservation.summerhouse.beginPeriod).locale('LT').format('MMMM Do');
                        reservation.summerhouse.endPeriod = moment(reservation.summerhouse.endPeriod).locale('LT').format('MMMM Do');
                    })
                    this.$scope.userReservations = response.data;
                });
            }, (error) => {
            });

        }
    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}