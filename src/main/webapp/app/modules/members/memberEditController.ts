///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>

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
            this.$scope.newMember = false;
            this.$scope.memberTax = 0;
            this.$scope.nextMembershipExpiration = null;

            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";
            
            this.authService.getUser().then((user) => {
                this.$scope.editable = user.id == this.$routeParams.memberID;
            }, (error) => {
                this.$scope.newMember = true;
            });

            this.$scope.formFields = {
                firstName: false,
                lastName: false,
                email: false,
                status: false,
                points: false
            }

            this.getMember($routeParams.memberID);
            this.getFormFields();

            this.$scope.saveMember = () => {    // TODO: pagalvot apie email unikaluma
                if (this.$scope.newMember){
                    this.$scope.member.id = null;
                    this.$http.post('rest/clubmember/', this.$scope.member).success(() => {
                        this.showSuccessMessage();
                    })
                } else {
                    this.$http.put('rest/clubmember/' + this.$scope.member.id, this.$scope.member).success(() => {
                        this.showSuccessMessage();
                    });
                }
                this.$scope.editing = false;
            }

            this.$scope.editForm = () => {
                this.$scope.editing = true;
            }

            this.$scope.renewMembership = () => {
                this.$http.put('/rest/clubmember/renewMembership', this.$scope.member).success(() => {
                    this.showSuccessMessage();
                    this.$scope.$apply();
                })
                .error(() => {
                    this.showErrorMessage();
                });
            }

            this.$scope.collectMembershipData = () => {
                this.$http.get('/rest/entities.tax/MemberTax').success((memberTax: number) => {
                    this.$scope.memberTax = memberTax;
                });
                this.$scope.nextMembershipExpiration = new Date(this.$scope.member.membershipExpirationDate);
                this.$scope.nextMembershipExpiration.setFullYear(this.$scope.nextMembershipExpiration.getFullYear() + 1)
            }
        }

        getMember(memberID: string): void{
            if (this.$scope.newMember){
                this.$scope.member = new Member();
            } else {
                this.$http.get('/rest/clubmember/' + memberID).success((member: Member, status) => {
                    this.$scope.member = member;
                });
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

        showSuccessMessage(): void{
            this.$scope.showAlert = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
        }

        showErrorMessage(): void{
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