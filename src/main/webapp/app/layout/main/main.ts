/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {
    "use strict";
    import AuthenticationService = SummerHouses.authentication.AuthenticationService;
    import IRootScopeService = angular.IRootScopeService;

    export class MainController {

        static that:MainController;

        static $inject = ['$scope', 'sh-authentication-service', '$route', '$rootScope'];

        constructor(private $scope:any,
                    private authService:any,
                    private $route:ng.route.IRouteService,
                    private $rootScope:IRootScopeService) {
            MainController.that = this;

            MainController.that.authService.getUser().then((user:AuthenticationService.IUser) => {
                this.$scope.fullName = user.firstName + " " + user.lastName;
                this.$scope.userID = user.id;
            }, (error) => {
                this.$scope.fullName = "shittyMittyAnonymous";
            });

        }

        sidebarToggleClick() {
            var classes = ['sidebar-collapse', 'sidebar-open'];
            var item = $(".sidebar-mini");
            
            for (var i in classes) {
                if (item.hasClass(classes[i])) {
                    item.removeClass(classes[i]);
                } else {
                    item.addClass(classes[i]);
                }
            }
        }
    }

    angular.module("housesApp").controller("main", MainController);
}