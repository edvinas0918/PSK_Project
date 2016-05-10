///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../members/memberModel.ts"/>
///<reference path="../members/memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    class CandidateFormController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$http',
            '$route',
            '$window',
            'sh-authentication-service'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $routeParams: any,
            private $http: any,
            private $route: any,
            private $window: any,
            private authService: any
        ) {
            this.$scope.editing = false;
            this.$scope.editable = true;
            this.$scope.candidate = true;
            this.$scope.memberTax = 0;
            this.$scope.nextMembershipExpiration = null;
            this.$scope.errorMessage = '';
            this.$scope.successMessage = '';

            this.$scope.isAdminPage = false;

            this.authService.getUser().then((user) => {
                this.$scope.member = user;
                this.$scope.member.statusString = Utilities.resolveMemberStatusString(user.memberStatus.name);
            }, (error) => {
            });

            this.$scope.formFields = { };
            this.getFormFields();

            this.$scope.saveMember = () => {    // TODO: pagalvot apie email unikaluma
                this.$http.put('rest/clubmember/' + this.$scope.member.id, this.$scope.member).success(() => {
                    this.showSuccessMessage("Pakeitimai išsaugoti");
                });
                this.$scope.editing = false;
            };

            this.$scope.editForm = () => {
                this.$scope.editing = true;
            }

        }

        getFormFields(): void{
            this.$http.get('/rest/memberFormField').success((fields: MemberFormField[]) => {
                this.$scope.originalFieldOptions = fields;
                _.forEach(fields, (field) => {
                    this.$scope.formFields[field.fieldName] = field.visible;
                });
            });
        }

        showSuccessMessage(message: string): void{
            this.$scope.showAlert = true;
            this.$scope.successMessage = message;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
        }

        showErrorMessage(message: string): void{
            this.$scope.errorMessage = message;
            this.$scope.showAlertError = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlertError = false;
                })
            }, 4000)
        }
    }

    angular
        .module('housesApp')
        .controller('candidateFormController', CandidateFormController);
}