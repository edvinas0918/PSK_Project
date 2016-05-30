///<reference path="../../../typings/angular.d.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="../../../typings/bootbox.d.ts"/>

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
            this.$scope.successMessage = '';

            this.getSummerHouses();
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
                bootbox.confirm({
                    buttons: {
                        confirm: {
                            label: 'Taip',
                            className: 'btn btn-primary'
                        },
                        cancel: {
                            label: 'Ne',
                            className: 'btn btn-default'
                        }
                    },
                    message: 'Ar tikrai norite ištrinti vasarnamį?',
                    callback: (result) => {
                        if (!result) return;
                        this.$http.delete('rest/summerhouse/' + house.id).success(() => {
                            var index = this.$scope.summerhouses.indexOf(house, 0);
                            if (index > -1) {
                                this.$scope.summerhouses.splice(index, 1);
                            }
                        });
                    }
                });
            };

            this.$scope.editHouse = (house: SummerHouse) => {
                house.editMode = true;

            };

            this.$scope.previewHouse = (house: SummerHouse) => {
                if(this.$scope.isAdminPage){
                    this.$location.path("/admin/previewHouse/" + house.id);
                }
                else{
                    this.$location.path("/previewHouse/" + house.id);
                }
            };

            this.$scope.previewReservation = (reservation: any) => {
                if(this.$scope.isAdminPage){
                    this.$location.path("/admin/reservation/" + reservation.id + "/edit");
                }
                else{
                    this.$location.path("/reservation/" + reservation.id + "/edit");
                }
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
            };


            this.$scope.showView = (viewName: string) => {
                this.$scope.view = viewName;
            };

            this.$scope.renew = () => {
                this.getSummerHouses();
                this.$scope.search = false;
                this.$scope.searchParams = null;
            };

            this.$scope.cancelReservation = (reservation: any) => {
                bootbox.confirm({
                    buttons: {
                        confirm: {
                            label: 'Taip',
                            className: 'btn btn-primary'
                        },
                        cancel: {
                            label: 'Ne',
                            className: 'btn btn-default'
                        }
                    },
                    message: 'Ar tikrai norite atšaukti rezervaciją?',
                    callback: (result) => {
                        if (!result) return;
                        this.$http.delete('rest/reservation/' + reservation.id).then(() => {
                            this.showSuccessMessage("Rezervacija atšaukta sėkmingai");
                            this.getUserReservations();
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

                });
            }
        }

        getSummerHouses(): void{
            this.$http.get('rest/summerhouse').success((summerhouses: SummerHouse[], status) => {
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
                var params = {
                    method: "GET",
                    url: "rest/reservation/clubmember/" + userId
                };

                this.$http(params).then((response) => {
                    _.forEach(response.data, (reservation) => {
                        reservation.fromDate = moment(reservation.fromDate).locale('LT').format('MMMM Do');
                        reservation.untilDate  = moment(reservation.untilDate).locale('LT').format('MMMM Do');
                    });
                    this.$scope.userReservations = response.data;
                });
            }, (error) => {
            });
        }

        showSuccessMessage(message: string): void{
            this.$scope.showAlert = true;
            this.$scope.successMessage = message;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
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