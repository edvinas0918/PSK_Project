/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {

    export class LogoutController {

        static that: LogoutController;

        static $inject=[
            'sh-authentication-service',
            '$location'
        ];

        constructor(
            private authService: SummerHouses.authentication.IAuthenticationService,
            private $location: ng.ILocationService
        ) {
            LogoutController.that=this;

            authService.logout().then (function () {
                $location.path("/login");
            });
        }
    }

    angular.module ("housesApp").controller ("logoutController", LogoutController);
}