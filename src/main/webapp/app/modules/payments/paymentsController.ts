///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="paymentModel.ts"/>

module SummerHouses.payments {

    class PaymentsController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any
        ) {

            this.getPayments();
            this.$scope.isConfirmed  = (payment: Payment) => {
                return payment.confirmed ?  "Patvirtinas" : "Nepatvirtintas";
            }
        }

        getPayments(): void{
            this.$http.get('/rest/payments').success((payments: Payment[]) => {
                this.$scope.payments = payments;
            });
        }
    }

    angular
        .module('housesApp')
        .controller('paymentsController', PaymentsController);
}