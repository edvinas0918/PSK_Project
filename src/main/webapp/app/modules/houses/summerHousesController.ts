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
            this.$scope.search = false;
            this.$scope.errorMessage = '';

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
                    templateUrl: 'app/modules/houses/templates/housesSearch.html',
                    controller: 'summerhouseSearchController'
                });

                modalInstance.result.then((result) =>{
                    this.$scope.search = true;
                    this.$scope.summerhouses = result['summerhouses'];
                    this.$scope.searchParams = result['searchParams'];
                });
            }


            this.$scope.showView = (viewName: string) => {
                this.$scope.view = viewName;
            }

            this.$scope.renew = () => {
                this.getSummerHouses();
                this.$scope.search = false;
                this.$scope.searchParams = null;
            };

            this.$scope.cancelReservation = (reservation: any) => {
                this.$http.delete('/rest/reservation/' + reservation.id).then(() => {
                    this.getUserReservations()
                }).catch((error) => {
                    switch (error.status){
                        case 406:
                            this.showErrorMessage("Rezervacijos atšaukimas negalimas.");
                            break;
                        case 500:
                            this.showErrorMessage("Sistemos klaida");
                            break;
                    }
                });
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

        showErrorMessage(message: string): void{
            this.$scope.errorMessage = message;
            this.$scope.showAlertError = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlertError = false;
                })
            }, 4000)
        }
    }

    angular
        .module('housesApp')
        .controller('summerHousesController', SummerHousesController);
}