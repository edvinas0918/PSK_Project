///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="mailingModel.ts"/>

module SummerHouses.mailing {

    import IAuthenticationService = SummerHouses.authentication.IAuthenticationService;
    
    class MailingController {

        private isSuccesful: boolean;
        private isError: boolean;
        private user: string;
        private emailAddresses: string [];

        static that:MailingController;

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
            MailingController.that = this;

            this.scope = $scope;
            this.httpService = $http;

            MailingController.that.authService.getUser().then((user:AuthenticationService.IUser) => {
                this.$scope.user = user.firstName + " " + user.lastName;
            }, function (error) {
                this.$scope.user = "Anonimas";
            });

            this.$scope.emailAddresses = [];
            this.$scope.emailAddresses.push("");
            this.$scope.isSuccesful = false;
            this.$scope.isError = false;

            this.$scope.$on('$viewContentLoaded', () => {
                this.$scope.$apply();
            });

            this.$scope.cancel = () => {
                this.$uibModalInstance.dismiss('cancel');
            };

            this.$scope.addEmailAddresses = () => {
                this.$scope.emailAddresses.push("");
            }

            this.$scope.removeEmailAddresses = (email: string) => {
                var index = this.$scope.emailAddresses.indexOf(email, 0);
               if (index > -1) {
                    this.$scope.emailAddresses.splice(index, 1);
                }
            }

            this.$scope.sendMessage = () => {
                var btn =$("#load").button('loading');
                this.$scope.emailAddresses.push("aurimas.repecka@gmail.com")
                var mailing = new Mailing(this.$scope.user, this.$scope.emailAddresses);
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