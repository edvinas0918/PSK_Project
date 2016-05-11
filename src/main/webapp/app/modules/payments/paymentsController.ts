///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="paymentModel.ts"/>

module SummerHouses.payments {

    class PaymentsController {

        private shownPayments: Payment[];
        private viewId : number;

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
            this.$scope.viewId = 0;
            this.getPayments();
            this.getShownPayments();

            this.$scope.isConfirmed  = (payment: Payment) => {
                return payment.confirmed ?  "Patvirtinas" : "Nepatvirtintas";
            }

            this.$scope.confirmPayments = () => {
                this.$http.put('rest/payments/confirm', this.$scope.checked).success(() => {
                    this.getPayments();
                    this.showSuccessMessage();
                });
            }

            this.$scope.viewChange = (viewId : number) => {
                this.getPayments();
                this.viewChanged(viewId);
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

        getShownPayments(): void{
            this.$http.get('/rest/payments').success((payments: Payment[]) => {
                this.$scope.shownPayments = payments;
            });
        }

        showSuccessMessage(): void{
            this.$scope.showAlert = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                    this.viewChanged(this.$scope.viewId);
                })
            }, 2000)
        }

        viewChanged(viewId : number) : void{
            switch (viewId){
                case 0:
                    this.$scope.viewId = viewId;
                    this.$scope.shownPayments = this.$scope.payments;
                    break;
                case 1:
                    this.$scope.viewId = viewId;
                    this.$scope.shownPayments = _.filter(this.$scope.payments, (payment) => {
                        if(payment.confirmed)
                            return payment;
                    });
                    break;
                case 2:
                    this.$scope.viewId = viewId;
                    this.$scope.shownPayments = _.filter(this.$scope.payments, (payment) => {
                        if(!payment.confirmed)
                            return payment;
                    });
                    break;
            }
        }
    }

    angular
        .module('housesApp')
        .controller('paymentsController', PaymentsController);
}