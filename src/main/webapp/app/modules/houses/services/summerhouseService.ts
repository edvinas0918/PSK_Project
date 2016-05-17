/// <reference path="../../../../typings/angular.d.ts" />

module SummerHouses {

    export class SummerhouseService {
        static that:SummerhouseService;

        static $inject = [
            '$q',
            '$http'
        ];

        constructor(
            private $q:ng.IQService,
            private $http: ng.IHttpService
        ) {
            SummerhouseService.that = this;
        }

        public getSummerhouse(id: number): ng.IPromise<any> {
            return SummerhouseService.that.getSummerhouses()
                .then(function (summerhouses) {
                    for (var i in summerhouses) {
                        if (summerhouses[i].id == id) {
                            return summerhouses[i];
                        }
                    }

                    return null;
                });
        }

        public getSummerhouses(): ng.IPromise<any>{
            var params = {
                url: 'rest/summerhouse',
                method: 'GET'
            };

            return SummerhouseService.that.$http(params)
                .then(function (response) {
                    return response.data;
                });
        }

    }

    angular.module("housesApp").service('summerhouseService', SummerhouseService);
}