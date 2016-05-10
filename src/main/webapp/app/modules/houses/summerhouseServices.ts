///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="summerHouseModel.ts"/>
module SummerHouses.houses {
    class SummerHouseServices {

        static that:SummerHouseServices;

        static $inject = [
            '$scope',
            '$http'
        ];

        constructor(private $scope:any,
                    private $http:any) {
            SummerHouseServices.that = this;
            this.getAdditionalServices();

            this.$scope.deleteService = (service: AdditionalService) => {
                this.$http.delete('/rest/additionalservice/' + service.id).success(() => {
                    var index = this.$scope.additionalServices.indexOf(service, 0);
                    if (index > -1) {
                        this.$scope.additionalServices.splice(index, 1);
                    }
                });
            }
        }

        getAdditionalServices():void {
            SummerHouseServices.that.$http.get('/rest/additionalservice/').success((services:AdditionalService[], status) => {
                SummerHouseServices.that.$scope.additionalServices = services;
            }).error((status) => {
                console.log(status);
            });
        }

    }

    angular
        .module('housesApp')
        .controller('summerHouseServices', SummerHouseServices);

}