///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class MembersController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http'
        ];

        private scope: any;
        private httpService: any;

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any
        ) {
            this.scope = $scope;
            this.httpService = $http;
            this.getMembers();

            this.scope.deleteMember = (member: Member) => {
                this.httpService.delete('rest/clubmember/' + member.id).success(() => {
                    _.pull(this.scope.members, member);
                });
            }
        }

        getMembers(): void{
            this.httpService.get('/rest/clubmember').success((members: Member[], status) => {
                this.scope.members = members;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('membersController', MembersController);
}