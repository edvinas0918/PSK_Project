///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="memberModel.ts"/>

module SummerHouses.members {

    class PointsGrant {
        public memberID:number;
        public points:number;
        public description:string;

        constructor(memberID:number, points:number, description:string) {
            this.memberID = memberID;
            this.points = points;
            this.description = description;
        }
    }

    class PointsGrantController {

        static $inject = [
            '$scope',
            '$routeParams',
            '$http'
        ];
        memberID: number;

        constructor(
            private $scope: any,
            private $routeParams: any,
            private $http: any
        ) {
            this.memberID = $routeParams.memberID;
            this.getMember(this.memberID);
            this.$scope.points = 0;
            this.$scope.description = "";
            this.$scope.showAlert = false;
            this.$scope.showError = false;

            this.$scope.grantPoints = () => {
                var btn = $("#load").button('loading');
                var model = new PointsGrant(this.memberID, this.$scope.points, this.$scope.description);
                this.$http.put('rest/clubmember/grantPoints', model).then(() => {
                    btn.button('reset');
                    this.$scope.showAlert = true;
                    setTimeout(() => {
                        this.$scope.$apply(() => {
                            this.$scope.showAlert = false;
                        })
                    }, 4000)
                }).catch((error) => {
                    this.$scope.showError = true;
                    btn.button('reset');
                });
            }


        }

        getMember(memberID: number): void{
            this.$http.get('/rest/clubmember/' + memberID).success((member: Member, status) => {
                this.$scope.member = member;
            });
        }


    }

    angular
        .module('housesApp')
        .controller('pointsGrantController', PointsGrantController);
}