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
            this.scope = $scope;
            this.httpService = $http;
            this.getMember($routeParams.memberID);

            this.scope.saveMember = () => {
                // this.httpService.delete('rest/clubmember/' + this.scope.member.id).success(() => {
                //
                // });
            }
        }

        getMember(memberID: string): void{
            this.httpService.get('/rest/clubmember/' + memberID).success((member: Member, status) => {
                this.scope.member = member;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('memberEditController', MemberEditController);
}