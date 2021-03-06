///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    import IAuthenticationService = SummerHouses.authentication.IAuthenticationService;

    class MembersController {

        private user: string;
        static that: MembersController;

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
            MembersController.that = this;
            this.$scope.formFields = { };
            this.$scope.search = false;

            this.authService.getUser().then((user:AuthenticationService.IUser) => {
                this.$scope.user = user;
            }, function (error) {
                this.$scope.user = null;
            });

            this.getMembers();
            this.getFormFields();
            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";

            this.$scope.deleteMember = (member: Member) => {
                this.$http.delete('rest/clubmember/' + member.id).success(() => {
                    _.pull(this.$scope.members, member);
                });
            };

            this.$scope.collectVacationData = (member: Member) => {
                this.$http.get('rest/reservation/vacationInfoForMember/' + member.id).success((vacationInfos: any) => {
                    if (!vacationInfos || vacationInfos.length == 0) {
                        this.$scope.noVacations = true;
                        return;
                    }
                    this.$scope.noVacations = false;
                    this.$scope.firstName = member.firstName;
                    this.$scope.lastName = member.lastName;
                    for (let vacationInfo of vacationInfos) {
                        vacationInfo.fromDate = moment(vacationInfo.fromDate).locale('LT').format("MMMM DD, YYYY");
                        vacationInfo.untilDate = moment(vacationInfo.untilDate).locale('LT').format("MMMM DD, YYYY");
                    }
                    this.$scope.vacationInfos = vacationInfos;
                });
            };

            this.$scope.closeVacationInfoModal = (summerhouseId: number) => {
                $('#view-vacation-info').modal('toggle');
                $(".modal-backdrop").remove();
                MembersController.that.$location.path('previewHouse/' + summerhouseId);
            };

            this.$scope.openInvitationForm = () => {
                this.$uibModal.open({
                    templateUrl: 'app/modules/mailing/templates/mailingInvitation.html',
                    controller: 'mailingController',
                    resolve: {
                        emailSubject: () => {
                            return "Kvietimas prisijungti prie „Labanoro draugų“";
                        },
                        emailBody: () => {
                            var name = this.$scope.user.firstName + " " + this.$scope.user.lastName;
                            return "Sveiki,\n" + name + " Jus kviečia prisijungti prie „Labanoro draugų“ klubo. Detalesnę informaciją ir kandidato anketą galite rasti mūsų puslapyje.\n\nPagarbiai,\n„Labanoro draugų“ klubas";
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
            };

            this.$scope.openSeachForm = () => {
                var modalInstance = this.$uibModal.open({
                    templateUrl: 'app/modules/members/templates/memberSearch.html',
                    controller: 'memberSearchController'
                });

                modalInstance.result.then((result) =>{
                    this.$scope.search = true;
                    this.$scope.members = result.filter(function (member) {
                        return member.id !== MembersController.that.$scope.user.id;
                    });
                });
            };

            this.$scope.renew = () => {
                this.getMembers();
                this.$scope.search = false;
            };

            this.$scope.redirectToMemberEdit = (member: Member) => {
                $location.path(this.$scope.isAdminPage ? "/admin/members/" + member.id : "/members/" + member.id);
            }
        }

        getMembers(): void{
            this.$http.get('rest/clubmember').success((members: Member[], status) => {
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
            this.$http.get('rest/memberFormField').success((fields: MemberFormField[]) => {
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