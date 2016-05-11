///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>

module SummerHouses.members {

    class MemberEditController {

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
            this.$scope.editable = false;
            this.$scope.candidate = false;
            this.$scope.memberTax = 0;
            this.$scope.nextMembershipExpiration = null;
            this.$scope.errorMessage = '';
            this.$scope.successMessage = '';

            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";
            
            this.authService.getUser().then((user) => {
                this.$scope.editable = user.id == this.$routeParams.memberID;
            }, (error) => {
            });
            
            this.$scope.formFields = { }

            this.getMember($routeParams.memberID);

            this.getFormFields();

            this.$scope.saveMember = () => {    // TODO: pagalvot apie email unikaluma
                if (this.$scope.newMember){
                    this.$scope.member.id = null;
                    this.$http.post('rest/clubmember/', this.$scope.member).success(() => {
                        this.showSuccessMessage("Pakeitimai išsaugoti");
                    })
                } else {
                    this.$http.put('rest/clubmember/' + this.$scope.member.id, this.$scope.member).success(() => {
                        this.showSuccessMessage("Pakeitimai išsaugoti");
                    });
                }
                this.$scope.editing = false;
            }

            this.$scope.editForm = () => {
                this.$scope.editing = true;
            }

            this.$scope.renewMembership = () => {
                this.$http.put('/rest/clubmember/renewMembership', this.$scope.member).then(() => {
                    this.getMember(this.$scope.member.id);
                    this.showSuccessMessage("Pakeitimai išsaugoti");
                })
                .catch((error) => {
                    switch (error.status){
                        case 406:
                            this.showErrorMessage("Nepakankamas taškų skaičius");
                            break;
                        case 500:
                            this.showErrorMessage("Sistemos klaida");
                            break;
                    }
                });
            }

            this.$scope.collectMembershipData = () => {
                this.$http.get('/rest/entities.tax/MemberTax').success((memberTax:number) => {
                    this.$scope.memberTax = memberTax;
                });

                if (this.$scope.member.membershipExpirationDate == null) {
                    this.$scope.nextMembershipExpiration = new Date();
                }else {
                    this.$scope.nextMembershipExpiration = new Date(this.$scope.member.membershipExpirationDate);
                }

                this.$scope.nextMembershipExpiration.setFullYear(this.$scope.nextMembershipExpiration.getFullYear() + 1);
            }

            this.$scope.recommendCandidate = () => {
                this.$http.put('/rest/clubmember/recommend/' + this.$scope.member.id ).success(() => {
                    this.showSuccessMessage("Rekomendacija patvirtinta");
                })
            }
        }

        getMember(memberID: string): void{
            this.$http.get('/rest/clubmember/' + memberID).success((member: Member, status) => {
                this.$scope.member = member;
                if (member.memberStatus.name.toLowerCase() === "candidate"){
                    this.$scope.candidate = true;
                }
                this.$scope.member.statusString = Utilities.resolveMemberStatusString(member.memberStatus.name);
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
        .controller('memberEditController', MemberEditController);
}