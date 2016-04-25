/// <reference path="../../../../typings/angular.d.ts" />

module SummerHouses.authentication {
    export interface IAuthenticationService {
        getFullName():ng.IPromise<string>;
        // getToken():string;
        // getUser():User;
        requestUserAccessToken():ng.IPromise<any>;
        isLoggedIn():boolean;
        logout():ng.IPromise<any>;
        reissueToken():ng.IPromise<any>;

        user:IUser;
    }

    export interface IUser {
        firstName:string;
        lastName:string;
        role:string;
    }

    export class AuthenticationService implements IAuthenticationService {

        public user:IUser;
        private userToken:string;

        static that:AuthenticationService;

        static $inject = [
            '$q',
            '$http',
            '$window',
            '$location',
            '$routeParams',
            '$sessionStorage'
        ];

        constructor(private $q:ng.IQService,
                    private $http:ng.IHttpService,
                    private $window:ng.IWindowService,
                    private $location:ng.ILocationService,
                    private $routeParams:any,
                    private $sessionStorage:any) {
            AuthenticationService.that = this;
            AuthenticationService.that.userToken = AuthenticationService.that.$sessionStorage.get("AuthorizationToken");
        }

        public requestUserAccessToken():ng.IPromise<any> {

            if (AuthenticationService.that.userToken) {
                console.log("Provided cached token within current loaded session...", AuthenticationService.that);
                return AuthenticationService.that.$q.when(AuthenticationService.that.userToken);
            }

            var redirectUrl = AuthenticationService.that.$location.absUrl();
            var code;
            if (redirectUrl.indexOf('?code=') != -1) {
                code = AuthenticationService.that.getCodeFromUrl(redirectUrl);
            }

            redirectUrl = redirectUrl.replace("?code=" + code, '');

            var params = {
                url: 'rest/authentication/getUserAccessToken',
                method: 'GET'
            };

            if (code) {
                params.url += '?code=' + code;
            } else {
                params.url += '?redirectUrl=' + redirectUrl;
            }

            return AuthenticationService.that.$http(params)
                .then((response) => {
                        var responseData:any = response.data;
                        var statusCode = responseData.statusCode;

                        if (statusCode == 401) {
                            console.log("Redirecting to fb");
                            var redirectUrl = responseData.message;
                            AuthenticationService.that.$window.location.href = redirectUrl;
                        } else if (statusCode == 200) {

                            console.log("Facebook login passed");
                            var token = responseData.message.replace("access_token=", '');
                            AuthenticationService.that.userToken = token;
                            AuthenticationService.that.$sessionStorage.put("AuthorizationToken", token);
                            var currentUrl = AuthenticationService.that.$location.absUrl();
                            var code = AuthenticationService.that.getCodeFromUrl(currentUrl);
                            AuthenticationService.that.$window.location.href = currentUrl.replace("?code=" + code, '');
                            AuthenticationService.that.$q.when(token);
                        } else {
                            // shit happened
                        }

                    }, (error:any) => {
                        AuthenticationService.that.$q.reject(error);
                    }
                );
        }

        public isLoggedIn():boolean {
            return AuthenticationService.that.userToken !== undefined;
        }

        public logout():ng.IPromise <any> {
            return AuthenticationService.that.$q.when(true);
        }

        public reissueToken():ng.IPromise<any> {
            return AuthenticationService.that.$q.when(true);
        }

        public getFullName():ng.IPromise<any> {

            var token = AuthenticationService.that.userToken;
            if (token) {
                var params = {
                    url: 'rest/authentication/getUserInfo?access_token=' + token,
                    method: 'GET'
                };

                return AuthenticationService.that.$http(params).then((response:any) => {
                        console.log("User info!!!");
                        console.log(response);
                        var userData = JSON.parse(response.data.message);

                        return userData.name;
                    }
                );
            } else {
                AuthenticationService.that.$q.reject("User is not logged in");
            }
        }

        redirectToFbLogin():ng.IPromise <any> {
            return AuthenticationService.that.$q.when(true);
        }

        private getCodeFromUrl(url:string):string {
            return url.substring(url.indexOf('?code=') + 6, url.indexOf('#/') || url.length);
        }
    }

    angular.module("housesApp").service('sh-authentication-service', AuthenticationService);
}