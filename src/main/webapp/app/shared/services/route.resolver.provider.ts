/// <reference path="../../../typings/angular.d.ts" />/// <reference path="../../../typings/angular.d.ts" />
///<reference path="../../modules/authentication/services/auth.service.ts"/>

module SummerHouses.shared {


    import IUser = SummerHouses.authentication.IUser;
    interface IRouteResolverProvider extends ng.IServiceProvider {
        defaultRouteResolver:any;
    }

    class RouteResolverProvider implements IRouteResolverProvider {

        static that:RouteResolverProvider;

        constructor() {
            RouteResolverProvider.that = this;

            this.defaultRouteResolver = [
                this.routeResolver
            ];

            this.routeResolver = ($q:ng.IQService,
                                  $rootScope:any,
                                  $route:any,
                                  $location:ng.ILocationService,
                                  authService:SummerHouses.authentication.AuthenticationService):ng.IPromise<any> => {

                if ($location.path() == "/login" && authService.getCachedUser() != null) {
                    RouteResolverProvider.that.redirectDefault($location, authService);
                }

                var url = $location.absUrl();
                var code = RouteResolverProvider.that.getCodeFromUrl(url);

                if (code) {
                    return authService.requestUserAccessToken("http://localhost:8080", code)
                        .then(function (token) {
                            RouteResolverProvider.that.redirectDefault($location, authService);
                            return $q.when(true);
                        }, function (error) {
                            return $q.reject(error);
                        });
                }

                var routeDefinition = $route.current;

                var requireAuth = false;
                if (routeDefinition.allowedRoles.indexOf('Candidate') == -1) {
                    requireAuth = true;
                }

                var authenticationPromise:ng.IPromise<any>;

                if (requireAuth) {
                    authenticationPromise = this.authHandler($q, authService);
                }

                return $q.when(authenticationPromise).then(function (userInfo:IUser) {
                    //check for roles
                    if (userInfo) {
                        var userRole = userInfo.memberStatus.name;
                        if (routeDefinition.allowedRoles.indexOf(userRole) == -1) {
                            return $q.reject({errorMessage: "Permission denied!"});
                        } else {
                            return $q.when(true);
                        }
                    } else {
                        return $q.when(true);
                    }
                }, function (error) {
                    $location.path('/login');
                });
            };

            this.defaultRouteResolver = [
                '$q',
                '$rootScope',
                '$route',
                '$location',
                'sh-authentication-service',
                this.routeResolver
            ];
        }

        private authHandler = ($q:ng.IQService,
                               authService:SummerHouses.authentication.IAuthenticationService):ng.IPromise<any> => {

            return (authService.isLoggedIn()) ? authService.getUser() : $q.reject({authenticated: false});
        };

        private getCodeFromUrl(url:string):string {
            if (url.indexOf('?code=') != -1) {
                return url.substring(url.indexOf('?code=') + 6, url.indexOf('#/') || url.length);
            }

            return null;
        }

        private redirectDefault($location:ng.ILocationService,
                         authService:SummerHouses.authentication.IAuthenticationService) {
            authService.getUser().then(function (user) {
                var redirectPath;
                switch (user.memberStatus.name.toLowerCase()) {
                    case "admin":
                        redirectPath = "/admin/houses";
                        break;
                    case "member":
                        redirectPath = "/houses";
                        break;
                    case "candidate":
                        redirectPath = "/anketa";
                }

                authService.$window.location.href = 'http://localhost:8080/#' + redirectPath;
                $location.path(redirectPath);
            });
        }

        public defaultRouteResolver:any;

        private routeResolver:($q:ng.IQService,
                               $rootScope:any,
                               $route:any,
                               $location:ng.ILocationService,
                               authService:SummerHouses.authentication.AuthenticationService) => ng.IPromise < any >;

        public $get = ["$q", this.get];

        private get():any {
            return {};
        }
    }

    angular.module('housesApp').provider('routeResolver', RouteResolverProvider);
}