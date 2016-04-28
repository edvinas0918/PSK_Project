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

            var url = this.$location.absUrl();
            var code = this.authService.getCodeFromUrl(url);

            if (code) {
                this.FBLogIn(code);
            }
        }
        
        public FBLogIn(code?: string) {

            var redirectUrl = LoginController.that.$location.absUrl();
            
            LoginController.that.authService.requestUserAccessToken(redirectUrl, code)
                .then(function (token) {
                    if (token) {
                        LoginController.that.authService.$window.location.href = "http://localhost:8080/#/houses";
                    }
                }, function (error) {
                    console.log("authentication failed");
                });
        }
    }

    angular.module ("housesApp").controller ("loginController", LoginController);
}