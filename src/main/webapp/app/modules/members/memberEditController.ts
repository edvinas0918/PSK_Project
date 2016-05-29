///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="../../../typings/moment.d.ts"/>
///<reference path="memberModel.ts"/>
///<reference path="memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>


module SummerHouses.members {

    class MemberEditController {

        static that:MemberEditController;

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$http',
            '$route',
            '$window',
            'sh-authentication-service',
            '$location'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $routeParams: any,
            private $http: any,
            private $route: any,
            private $window: any,
            private authService: any,
            private $location: ng.ILocationService
        ) {

            MemberEditController.that = this;

            var params = $route.current.params;
            this.makePaymentIfNeeded(params.paymentId, params.token, params.PayerID);


            this.$scope.eurToPay = 0;
            this.$scope.editing = false;
            this.$scope.editable = false;
            this.$scope.candidateReview = false;
            this.$scope.memberTax = 0;
            this.$scope.nextMembershipExpiration = null;
            this.$scope.errorMessage = '';
            this.$scope.successMessage = '';

            this.$scope.isAdminPage = this.$route.current.$$route.layout.toLowerCase() === "admin";
            
            this.authService.getSessionUser().then((user) => {
                this.$scope.editable = user.id == this.$routeParams.memberID;
            }, (error) => {
            });
            
            this.$scope.formFields = { };

            this.getMember($routeParams.memberID);

            this.getFormFields();

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

            this.$scope.saveMember = () => {    // TODO: pagalvot apie email unikaluma
                if (this.$scope.newMember){
                    this.$scope.member.id = null;
                    this.$http.post('rest/clubmember/', this.$scope.member).success(() => {
                        this.showSuccessMessage("Pakeitimai išsaugoti", 4000);
                    })
                } else {
                    this.$http.put('rest/clubmember/' + this.$scope.member.id, this.$scope.member).success(() => {
                        this.showSuccessMessage("Pakeitimai išsaugoti", 4000);
                    });
                }
                this.$scope.editing = false;
            };

            this.$scope.editForm = () => {
                this.$scope.editing = true;
            };

            this.$scope.renewMembership = () => {
                this.$http.put('rest/clubmember/renewMembership', this.$scope.member).then(() => {
                    this.getMember(this.$scope.member.id);
                    this.showSuccessMessage("Pakeitimai išsaugoti", 4000);
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
            };

            this.$scope.collectMembershipData = () => {
                this.$http.get('rest/settings/memberTax').success((memberTax: any) => {
                    this.$scope.memberTax = Number(memberTax.value);
                });

                if (this.$scope.member.membershipExpirationDate == null) {
                    this.$scope.nextMembershipExpiration = new Date();
                }else {
                    this.$scope.nextMembershipExpiration = new Date(this.$scope.member.membershipExpirationDate);
                }

                this.$scope.nextMembershipExpiration.setFullYear(this.$scope.nextMembershipExpiration.getFullYear() + 1);
            };

            this.$scope.recommendCandidate = () => {
                this.$http.put('/rest/clubmember/recommend/' + this.$scope.member.id ).success(() => {
                    this.showSuccessMessage("Rekomendacija patvirtinta", 5000);
                })
            }

            this.$scope.change = (pointAmount: number) => {
                MemberEditController.that.$scope.eurToPay = pointAmount;
            }

            this.$scope.buyPointsWithPayPal = () => {
                MemberEditController.that.$scope.isPaying = true;
                var btn =$("#pay").button('loading');
                var params = {
                    method: "POST",
                    url: "/rest/payments/payPalAuthorizationForPayment",
                    data: {"amount": MemberEditController.that.$scope.eurToPay, "returnURL":  this.$window.location.href, "cancelURL": this.$window.location.href},
                    headers: {
                        'Content-Type': "application/json"
                    }
                };

                this.$http(params).then(function (response) {
                    MemberEditController.that.$window.location = response.data.approval_url;
                    btn.button('reset');
                }, function (error) {
                    MemberEditController.that.$scope.isPaying = false;
                    MemberEditController.that.$scope.payPalErrorMessage = error.data.errorMessage;
                    MemberEditController.that.$scope.isModalError = true;
                });
            }
        }

        makePaymentIfNeeded(paymentID: string, token: string, payerID: string): void {
            if (payerID && paymentID && token) {
                MemberEditController.that.$scope.isPaying = true;
                MemberEditController.that.$location.search({});
                var btn =$("#update").button('loading, disabled');
                var params = {
                    method: "POST",
                    url: "/rest/payments/payPalMakePayment",
                    data: {"paymentID": paymentID, "token":  token, "payerID": payerID},
                    headers: {
                        'Content-Type': "application/json"
                    }
                };

                this.$http(params).then(function (response) {
                    btn.button('reset');
                    MemberEditController.that.$http.get('/rest/clubmember/getPoints/' + MemberEditController.that.$scope.member.id).success((points: number, status) => {
                        MemberEditController.that.$scope.member.points = points;
                        MemberEditController.that.$scope.isPaying = false;
                        MemberEditController.that.showSuccessMessage("Įsigyta klubo taškų už " + response.data.total + " EUR", 10000);
                    });
                }, function (error) {
                    btn.button('reset');
                    MemberEditController.that.$scope.isPaying = false;
                    MemberEditController.that.$scope.isError = true;
                    MemberEditController.that.$scope.payPalErrorMessage = error.data.errorMessage;
                });
                paymentID = null;
                token = null;
                payerID = null;
            }
        }

        getMember(memberID: string): void{
            this.$http.get('rest/clubmember/' + memberID).success((member: Member, status) => {
                this.$scope.member = member;
                this.$scope.member.membershipExpirationDateString =
                    moment(member.membershipExpirationDate).locale('LT').format('L');
                if (member.memberStatus.name.toLowerCase() === "candidate"){
                    this.$scope.candidateReview = true;
                }
                this.$scope.member.statusString = Utilities.resolveMemberStatusString(member.memberStatus.name);
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

        showSuccessMessage(message: string, timeout: number): void{
            this.$scope.showAlert = true;
            this.$scope.successMessage = message;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, timeout)
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