///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    import IAuthenticationService = SummerHouses.authentication.IAuthenticationService;

    class MembersController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$uibModal',
            '$http',
            '$route',
            '$location',
            'sh-authentication-service'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $uibModal: any,
            private $http: any,
            private $route: any,
            private $location: any,
            private authService: IAuthenticationService
        ) {
            this.$scope.formFields = { }


            this.getMembers();
            this.getFormFields();
            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";

            this.$scope.deleteMember = (member: Member) => {
                this.$http.delete('rest/clubmember/' + member.id).success(() => {
                    _.pull(this.$scope.members, member);
                });
            }

            this.$scope.openInvitationForm = () => {
               this.$uibModal.open({
                  templateUrl: 'app/modules/mailing/templates/mailingInvitation.html',
                   controller: 'mailingController'
               });
            }

            this.$scope.redirectToMemberEdit = (member: Member) => {
                $location.path(this.$scope.isAdminPage ? "/admin/members/" + member.id : "/members/" + member.id);
            }
        }

        getMembers(): void{
            this.$http.get('/rest/clubmember').success((members: Member[], status) => {
                this.authService.getUser().then((user:AuthenticationService.IUser) => {
                _.forEach(members, (member) => {
                    member.statusString = Utilities.resolveMemberStatusString(member.memberStatus.name);
                });
                    this.$scope.members = members.filter(function (member) {
                        return member.id !== user.id;
                    });
                });
            });
        }

        getFormFields(): void{
            this.$http.get('/rest/memberFormField').success((fields: MemberFormField[]) => {
                this.$scope.originalFieldOptions = fields;
                _.forEach(fields, (field) => {
                    this.$scope.formFields[field.fieldName] = field.visible;
                });
            });
        }

    }

    angular
        .module('housesApp')
        .controller('membersController', MembersController);
}