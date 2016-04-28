///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

module SummerHouses.mailing {

    class MailingController {

        private emailAddresses: string[];
        private isSuccesful: boolean;
        private isError: boolean;

        static $inject = [
            '$rootScope',
            '$scope',
            '$timeout',
            '$uibModalInstance',
            '$http'
        ];

        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $timeout: any,
            private $uibModalInstance: any,
            private $http: any
        ) {
            this.scope = $scope;
            this.httpService = $http;

            this.$scope.isSuccesful = false;
            this.$scope.isError = false;
            this.$scope.emailAddresses = [];
            this.$scope.emailAddresses.push("");

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };

            this.$scope.addEmailAddresses = (email: string, index: number) => {
                this.$scope.emailAddresses[index] = email;
                this.$scope.emailAddresses.push("");
            }

            this.$scope.removeEmailAddresses = (index: number) => {
               if (index > -1) {
                    this.$scope.emailAddresses.splice(index, 1);
                }
            }

            this.$scope.sendMessage = (emailAddresses: string[]) => {
                var btn =$("#load").button('loading');
                var user: string = "Aurimas Repečka";
                this.$http.post('/rest/mailing/invitation', emailAddresses, user ).success(() => {
                    btn.button('reset');
                    this.$scope.isSuccesful = true;
                    setTimeout(function() {
                        $uibModalInstance.close();
                    }, 3000);
                }).
                error(() => {
                    btn.button('reset');
                    this.$scope.isError = true;
                })
            }
        }
    }

    angular
        .module('housesApp')
        .controller('mailingController', MailingController);
}