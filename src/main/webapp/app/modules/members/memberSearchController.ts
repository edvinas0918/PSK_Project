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
                var period = this.$scope.weekPicker.getPeriod();
                this.$http.get('rest/clubmember/reservation?from=' + period.fromDate + '&until=' + period.untilDate)
                    .success((members: Member[]) => {
                        this.$uibModalInstance.close(members);
                    });
            };

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel')
            };
        }
    }

    angular
        .module('housesApp')
        .controller('memberSearchController', MemberSearchController);
}