///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../members/memberModel.ts"/>
///<reference path="../members/memberFormFieldModel.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    import Settings = SummerHouses.settings.Settings;
    class CandidateFormController {

        static that: CandidateFormController;

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$uibModal',
            '$http',
            '$route',
            '$window',
            'sh-authentication-service',
            '$q',
            '$location'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $routeParams: any,
            private $uibModal: any,
            private $http: any,
            private $route: any,
            private $window: any,
            private authService: any,
            private $q: ng.IQService,
            private $location: ng.ILocationService
        ) {
            CandidateFormController.that = this;
            this.$scope.editing = false;
            this.$scope.editable = true;
            this.$scope.candidate = true;
            this.$scope.tooltip = true;
            this.$scope.memberTax = 0;
            this.$scope.nextMembershipExpiration = null;
            this.$scope.errorMessage = '';
            this.$scope.successMessage = '';

            this.$scope.recommendationsRequests =  [];
            this.$scope.recommendationsRequestsMax = 0;
            this.$scope.recommendationsReceived = [];
            this.$scope.recommendationsReceivedMin = 0;

            this.$scope.isAdminPage = false;

            this.authService.getUser().then((user) => {
                this.$scope.member = user;
                this.$scope.member.statusString = Utilities.resolveMemberStatusString(user.memberStatus.name);
            }, (error) => {
            });

            this.$scope.formFields = { };
            this.getFormFields();
            this.getRecommendationInformation();

            this.$scope.saveMember = () => {    // TODO: pagalvot apie email unikaluma
                this.$http.put('rest/clubmember/' + this.$scope.member.id, this.$scope.member).success(() => {
                    this.$q.when(authService.getSessionUser()).then(function (user) {
                        CandidateFormController.that.$scope.member = user;
                    });
                    this.showSuccessMessage("Pakeitimai išsaugoti");
                });
                this.$scope.editing = false;
            };

            this.$scope.editForm = () => {
                this.$scope.editing = true;
            };

            this.$scope.$on("refreshPage", () => {
                this.getRecommendationInformation();
                this.$scope.$apply();
            });

            this.$scope.requestRecommendations = () => {
                this.$uibModal.open({
                    templateUrl: 'app/modules/mailing/templates/mailingInvitation.html',
                    controller: 'mailingController',
                    resolve: {
                        emailSubject: () => {
                            return "Naujo nario rekomendacija";
                        },
                        emailBody: () => {
                            return "Sveiki,\nNaujas kandidatas " + this.$scope.member.firstName + " " + this.$scope.member.lastName
                                + " laukia tavo patvirtinimo! Kandidato anketą galite peržiūrėti mūsų puslapyje.\n\nPagarbiai,\n„Labanoro draugų“ klubas";
                        },
                        maxRecipients: () => {
                            return this.$scope.recommendationsRequestsMax - this.$scope.recommendationsRequests.length;
                        },
                        method: () => {
                            return "recommendation";
                        },
                        title: () => {
                            return "Prašyk rekomendacijų!";
                        }
                    }
                });
            };
            this.$scope.deactivateMember = () => {
                var result  = window.confirm("Ar tikrai norite išsiregistruoti iš sistemos?");
                if (result){
                    this.$http.delete('rest/clubmember/' + this.$scope.member.id).success(() => {
                        authService.logout().then (function () {
                            $location.path("/login");
                        });
                    });
                }
            };
        }

        getFormFields(): void{
            this.$http.get('rest/memberFormField').success((fields: MemberFormField[]) => {
                this.$scope.originalFieldOptions = fields;
                _.forEach(fields, (field) => {
                    this.$scope.formFields[field.fieldName] = field.visible;
                });
            });
        }

        getRecommendationInformation(): void{
            this.$http.get('rest/settings/reccommendationRequestMax').success((settings: Settings) => {
                this.$scope.recommendationsRequestsMax = settings.value;
            });
            this.$http.get('rest/settings/recommendationsMin').success((settings: Settings) => {
                this.$scope.recommendationsReceivedMin = settings.value;
            });
           this.$http.get('rest/invitation').success((invitations: Invitation[]) => {
                this.$scope.recommendationsRequests = invitations;
            });
            this.$http.get('rest/clubmember/recommendations').success((clubmembers: Member[]) => {
                this.$scope.recommendationsReceived = clubmembers;
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