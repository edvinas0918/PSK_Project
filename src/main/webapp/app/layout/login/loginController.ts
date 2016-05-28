/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {
    
    export class LoginController {

        static that: LoginController;

        static $inject=[
            'sh-authentication-service',
            '$window',
            '$route'
        ];
        
        constructor(
            private authService: SummerHouses.authentication.IAuthenticationService,
            private $window: ng.IWindowService,
            private $route: any
        ) {
            LoginController.that=this;
        }
        
        public FBLogIn() {
            LoginController.that.authService.requestUserAccessToken(LoginController.that.authService.getBaseUrl());
        }
    }

    angular.module ("housesApp").controller ("loginController", LoginController);
}