/// <reference path="../../../../typings/angular.d.ts" />

module SummerHouses.authentication {
    export interface IAuthenticationService {
        requestUserAccessToken(redirectUrl?:string, code?:string):ng.IPromise<any>;
        isLoggedIn():boolean;
        logout():void;
        getCachedToken(): string;
        reissueToken():ng.IPromise<any>;
        getUser():ng.IPromise<IUser>;
        getCachedUser(): IUser;
    }

    export interface IUser {
        firstName:string;
        lastName:string;
        email:string;
        memberStatus:any;
        points:number;
        reservationGroup:number;
        accessToken:string;
        token:string;
    }

    export class AuthenticationService implements IAuthenticationService {

        private user:IUser;
        private userToken:string;

        static that:AuthenticationService;

        static $inject = [
            '$q',
            '$http',
            '$window',
            '$location',
            '$rootScope',
            '$routeParams',
            '$sessionStorage',
        ];

        constructor(private $q:ng.IQService,
                    private $http:ng.IHttpService,
                    private $window:ng.IWindowService,
                    private $location:ng.ILocationService,
                    private $rootScope:ng.IRootScopeService,
                    private $routeParams:any,
                    private $sessionStorage:any) {
            AuthenticationService.that = this;
            AuthenticationService.that.userToken = AuthenticationService.that.$sessionStorage.get("AuthorizationToken");

            this.$rootScope.$on('$routeChangeError', this.onRouteChangeError);
        }

        public requestUserAccessToken(redirectUrl?:string, code?:string):ng.IPromise<any> {

            if (AuthenticationService.that.userToken) {
                console.log("Provided cached token within current loaded session...", AuthenticationService.that);
                return AuthenticationService.that.$q.when(AuthenticationService.that.userToken);
            }

            var params = {
                url: 'rest/authentication/getUserAccessToken',
                method: 'GET'
            };

            if (code) {
                params.url += '?code=' + code;
            } else {
                params.url += '?redirectUrl=http://localhost:8080/';// + redirectUrl;
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
                            return AuthenticationService.that.getUser()
                                .then(function (user) {
                                    return AuthenticationService.that.$q.when(token);
                                });
                        } else {
                            // shit happened
                            return AuthenticationService.that.$q.reject({info: "Shit happened"});
                        }
                    }, (error:any) => {
                        return AuthenticationService.that.$q.reject(error);
                    }
                );
        }

        public isLoggedIn():boolean {
            return AuthenticationService.that.userToken !== undefined;
        }

        public logout():void {
            AuthenticationService.that.userToken = "";
            AuthenticationService.that.$sessionStorage.put("AuthorizationToken", "");
            AuthenticationService.that.user = null;
        }

        public reissueToken():ng.IPromise<any> {
            return AuthenticationService.that.$q.when(true);
        }

        public getCachedToken():string {
            return AuthenticationService.that.userToken;
        }

        public getUser():ng.IPromise<IUser> {
            if (AuthenticationService.that.user) {
                return AuthenticationService.that.$q.when(AuthenticationService.that.user);
            }

            var token = AuthenticationService.that.userToken;

            if (token) {
                var params = {
                    url: 'rest/clubmember/token/' + token,
                    method: 'GET'
                };

                return AuthenticationService.that.$http(params).then((response:any) => {
                        return AuthenticationService.that.user = response.data;
                    }
                );
            } else {
                return AuthenticationService.that.$q.reject("Unauthorized");
            }

        }
        
        public getCachedUser(): IUser {
            return AuthenticationService.that.user;
        }

        onRouteChangeError(event, next, previous, rejection) {
            AuthenticationService.that.$rootScope.lastError = rejection;
            AuthenticationService.that.$location.path("/error");
        }
    }

    angular.module("housesApp").service('sh-authentication-service', AuthenticationService);
}