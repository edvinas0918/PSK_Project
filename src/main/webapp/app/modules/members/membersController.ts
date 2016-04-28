///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class MembersController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$uibModal',
            '$http'
        ];
        

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $uibModal: any,
            private $http: any
        ) {

            this.getMembers();

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