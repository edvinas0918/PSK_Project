///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    import IAuthenticationService = SummerHouses.authentication.IAuthenticationService;

    class MembersController {

        private user: string;

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

            this.authService.getUser().then((user:AuthenticationService.IUser) => {
                this.$scope.user = user.firstName + " " + user.lastName;
            }, function (error) {
                this.$scope.user = "Anonimas";
            });

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
                    controller: 'mailingController',
                    resolve: {
                        emailSubject: () => {
                            return "Kvietimas prisijungti prie „Labanoro draugų“";
                        },
                        emailBody: () => {
                            return "Sveiki,\n" + this.$scope.user + " Jus kviečia prisijungti prie „Labanoro draugų“ klubo. Detalesnę informaciją ir kandidato anketą galite rasti mūsų puslapyje.\n\nPagarbiai,\n„Labanoro draugų“ klubas";
                        },
                        maxRecipients: () => {
                            return 5;
                        },
                        method: () => {
                            return "invitation";
                        },
                        title: () => {
                            return "Pakviesk draugą!";
                        }
                    }
                });
            }

            this.$scope.openSeachForm = () => {
                this.$uibModal.open({
                    templateUrl: 'app/modules/members/templates/memberSearch.html',
                    controller: 'memberSearchController'
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