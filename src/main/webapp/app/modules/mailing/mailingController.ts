///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

module SummerHouses.mailing {

    class MailingController {

        private emailAddresses: string[];

        static $inject = [
            '$rootScope',
            '$scope',
            '$uibModalInstance',
            '$http'
        ];

        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $uibModalInstance: any,
            private $http: any
        ) {
            this.scope = $scope;
            this.httpService = $http;

            this.$scope.emailAddresses = [];
            this.$scope.emailAddresses.push("");

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };

            this.$scope.addEmailAddresses = () => {
                this.$scope.emailAddresses.push("");
            }

            this.$scope.removeEmailAddresses = (emailAddress: string) => {
                var index = this.$scope.emailAddresses.indexOf(emailAddress, 0);
                if (index > -1) {
                    this.$scope.emailAddresses.splice(index, 1);
                }
            }

            this.$scope.sendMessage = (emails: string[]) => {
                return;
                this.$http.post('/rest/invitation/emails', emails).success(() => {
                    this.$uibModalInstance.close();
                 });
            }
        }
    }

    angular
        .module('housesApp')
        .controller('mailingController', MailingController);
}