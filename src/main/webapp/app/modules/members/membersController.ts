///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class MembersController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$uibModal',
            '$http',
            '$route',
            '$location'
        ];
        

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $uibModal: any,
            private $http: any,
            private $route: any,
            private $location: any
        ) {

            this.getMembers();
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
                this.$scope.members = members;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('membersController', MembersController);
}