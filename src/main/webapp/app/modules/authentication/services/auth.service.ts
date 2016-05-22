/// <reference path="../../../../typings/angular.d.ts" />

module SummerHouses.authentication {
    export interface IAuthenticationService {
        requestUserAccessToken(redirectUrl?:string, code?:string):ng.IPromise<any>;
        isLoggedIn():boolean;
        logout(): ng.IPromise<any>;
        setUser(user: IUser): void;
        getUser():ng.IPromise<IUser>;
        getCachedUser(): IUser;
        getSessionUser(): ng.IPromise<IUser>;
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
            AuthenticationService.that.user = AuthenticationService.that.$sessionStorage.get("User");
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
                            return AuthenticationService.that.getSessionUser()
                                .then(function (user) {
                                    AuthenticationService.that.user = user;
                                    AuthenticationService.that.$sessionStorage.put("User", user);
                                    return token;
                                });
                        } else {
                            return AuthenticationService.that.$q.reject(responseData.message);
                        }
                    }, (error:any) => {
                        return AuthenticationService.that.$q.reject(error);
                    }
                );
        }

        public isLoggedIn():boolean {
            return AuthenticationService.that.getCachedUser() != undefined;
        }

        public logout():ng.IPromise<any> {
            AuthenticationService.that.$sessionStorage.put("User", "");
            AuthenticationService.that.setUser(null);

            var params = {
                url: "rest/authentication/logout",
                method: "GET"
            };

            return AuthenticationService.that.$http(params).then(function(response) {
                return true;
            }, function (error) {
                return error;
            });
        }

        public getUser():ng.IPromise<IUser> {
            if (AuthenticationService.that.getCachedUser()) {
                return AuthenticationService.that.$q.when(AuthenticationService.that.getCachedUser());
            }

            return AuthenticationService.that.getSessionUser();
        }

        public setUser(user :IUser): void {
            AuthenticationService.that.user = user;
        }
        
        public getCachedUser(): IUser {
            return AuthenticationService.that.user;
        }
        
        public getSessionUser(): ng.IPromise<IUser> {
            var params = {
                url: "rest/authentication/getUser",
                method: "GET"
            };

            return AuthenticationService.that.$http(params)
                .then (function (response: any) {
                    return response.data;
                });
        }

        onRouteChangeError(event, next, previous, rejection) {
            AuthenticationService.that.$rootScope["lastError"] = rejection;
            AuthenticationService.that.$location.path("/error");
        }
    }

    angular.module("housesApp").service('sh-authentication-service', AuthenticationService);
}