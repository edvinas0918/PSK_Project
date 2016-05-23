///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../utilities/weekPicker.ts"/>
///<reference path="summerHouseModel.ts"/>
///<reference path="summerhouseSearchModel.ts"/>

module SummerHouses.houses {

    class SummerhouseSearchController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http',
            '$uibModalInstance',
            'summerhouseService'
        ];

        constructor(private $rootScope:any,
                    private $scope:any,
                    private $http:any,
                    private $uibModalInstance: any,
                    private summerhouseService:SummerHouses.SummerhouseService) {
            this.$scope.weekPicker = new Utilities.WeekPicker([]);
            this.$scope.searchDto = new SummerhouseSearchDto();

            summerhouseService.getAdditionalServices()
                .then((result: AdditionalService[]) => {this.$scope.additionalServices = result;});

            this.$scope.checkedAdditionalServices = [];
            this.$scope.toggleCheck = (additionalService: AdditionalService) => {
                if (this.$scope.checkedAdditionalServices.indexOf(additionalService) === -1) {
                    this.$scope.checkedAdditionalServices.push(additionalService);
                } else {
                    this.$scope.checkedAdditionalServices.splice(this.$scope.checkedAdditionalServices.indexOf(additionalService), 1);
                }
            };


            this.$scope.submit  = () => {
                var period = this.$scope.weekPicker.getPeriod();
                this.$scope.searchDto.fromDate = period.fromDate;
                this.$scope.searchDto.untilDate = period.untilDate;
                this.$scope.searchDto.additionalServices = this.$scope.checkedAdditionalServices;

                this.$http.post('/rest/summerhouse/search', this.$scope.searchDto)
                    .success((summerhouseSearchResult: SummerHouse[]) => {
                        this.$uibModalInstance.close(summerhouseSearchResult);
                });
            };

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel')
            };
        }
    }

    angular
        .module('housesApp')
        .controller('summerhouseSearchController', SummerhouseSearchController);
}