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

            this.$scope.confirmPayments = () => {
                this.$http.put('rest/payments/confirm', this.$scope.checked).success(() => {
                    this.getPayments();
                    this.showSuccessMessage();
                });
            }

            this.$scope.checked = [];
            this.$scope.toggleCheck = (payment: Payment) => {
                if (this.$scope.checked.indexOf(payment) === -1) {
                    this.$scope.checked.push(payment);
                } else {
                    this.$scope.checked.splice(this.$scope.checked.indexOf(payment), 1);
                }
            };
        }

        getPayments(): void{
            this.$http.get('/rest/payments').success((payments: Payment[]) => {
                this.$scope.payments = payments;
            });
        }

        showSuccessMessage(): void{
            this.$scope.showAlert = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
        }
    }

    angular
        .module('housesApp')
        .controller('paymentsController', PaymentsController);
}