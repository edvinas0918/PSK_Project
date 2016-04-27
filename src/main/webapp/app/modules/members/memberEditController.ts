///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class MemberEditController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$routeParams',
            '$http'
        ];
        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $routeParams: any,
            private $http: any
        ) {
            this.$scope.editing = false;
            this.$scope.editable = true;
            this.$scope.newMember = false;

            this.getMember($routeParams.memberID);

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

        showSuccessMessage(): void{
            this.$scope.showAlert = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
        }

    }

    angular
        .module('housesApp')
        .controller('memberEditController', MemberEditController);
}