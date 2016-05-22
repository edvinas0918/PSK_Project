///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class MemberSearchController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http',
            '$uibModalInstance'
        ];

        constructor(private $rootScope:any,
                    private $scope:any,
                    private $http:any,
                    private $uibModalInstance: any) {
            this.$scope.weekPicker = new Utilities.WeekPicker([]);

            this.$scope.submit  = () => {
                var period = this.$scope.weekPicker.getReservationPeriod();
                this.$scope.members = this.getMembers(period.fromDate, period.untilDate);
                this.dismiss();
            }

            this.$scope.cancel = () => {
                this.dismiss();
            };
        }

        dismiss(){
            this.$uibModalInstance.dismiss('cancel');
        }

        getMembers(fromDate, untilDate : String): Member[]{
            this.$http.get('/rest/clubmember/reservation?from=' + fromDate + '&until=' + untilDate).success((members: Member[]) => {
                console.log(members)
                return members;
            });
            return [];
        }
    }

    angular
        .module('housesApp')
        .controller('memberSearchController', MemberSearchController);
}