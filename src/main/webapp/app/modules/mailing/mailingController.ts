///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="mailingModel.ts"/>

module SummerHouses.mailing {

    import IAuthenticationService = SummerHouses.authentication.IAuthenticationService;
    class MailingController {

        private isSuccesful: boolean;
        private isError: boolean;
        private mailing: Mailing;
        private user: any;

        static $inject = [
            '$rootScope',
            '$scope',
            '$timeout',
            '$uibModalInstance',
            '$http',
            'sh-authentication-service'
        ];

        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $timeout: any,
            private $uibModalInstance: any,
            private $http: any,
            private authService: IAuthenticationService
        ) {
            this.scope = $scope;
            this.httpService = $http;

            this.$scope.isSuccesful = false;
            this.$scope.isError = false;
            
            this.authService.getUser().then((result) => {
                this.$scope.user = result;
            });

            this.$scope.mailing = {
                currentUser: "AS",
                emailAddresses: []
            }

            this.$scope.mailing.emailAddresses.push("");

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };

            this.$scope.addEmailAddresses = () => {
                this.$scope.mailing.emailAddresses.push("");
            }

            this.$scope.removeEmailAddresses = (email: string) => {
                var index = this.$scope.mailing.emailAddresses.indexOf(email, 0);
               if (index > -1) {
                    this.$scope.mailing.emailAddresses.splice(index, 1);
                }
            }

            this.$scope.sendMessage = (mailing: Mailing) => {
                var btn =$("#load").button('loading');
                this.$http.post('/rest/mailing/invitation', mailing ).success(() => {
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