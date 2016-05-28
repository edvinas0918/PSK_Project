///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="summerHouseModel.ts"/>
module SummerHouses.houses {
    class SummerHouseServicesEdit {

        static that:SummerHouseServicesEdit;

        static $inject = [
            '$scope',
            '$http',
            '$routeParams',
            '$location'
        ];

        constructor(private $scope:any,
                    private $http:any,
                    private $routeParams:any,
                    private $location:any) {
            SummerHouseServicesEdit.that = this;

            this.getService($routeParams.serviceID);

            this.$scope.saveService = () => {
                var service = this.$scope.service;
                if (!$routeParams.serviceID){
                    service.additionalservicereservationList = [];
                    service.id = null;
                    this.$http.post('rest/additionalservice/postServiceMap/', service).success(() => {
                        //this.showSuccessMessage();
                        $location.path("/admin/services");
                        //alert("zjbs created");
                    })
                } else {
                    this.$http.put('rest/additionalservice/' + service.id, service).success(() => {
                        //this.showSuccessMessage();
                        $location.path("/admin/services");
                        //alert("zjbs edited");
                    });
                }
            }

        }

        getService(serviceID: string): void{
            if (!serviceID){
                this.$scope.service = new AdditionalService();
            } else {
                this.$http.get('/rest/additionalservice/' + serviceID).success((service: AdditionalService, status) => {
                    this.$scope.service = service;
                });
            }
        }


    }

    angular
        .module('housesApp')
        .controller('summerHouseServicesEdit', SummerHouseServicesEdit)

}