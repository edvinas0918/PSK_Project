/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {
    
    export class LoginController {

        static that: LoginController;

        static $inject=[
            'sh-authentication-service',
            '$location',
            '$route'
        ];
        
        constructor(
            private authService: SummerHouses.authentication.IAuthenticationService,
            private $location: ng.ILocationService,
            private $route: any
        ) {
            LoginController.that=this;
        }
        
        public FBLogIn() {
            LoginController.that.authService.requestUserAccessToken();
        }
    }

    angular.module ("housesApp").controller ("loginController", LoginController);
}