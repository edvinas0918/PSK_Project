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
                                  authService:SummerHouses.authentication.AuthenticationService,
                                  $window:ng.IWindowService):ng.IPromise<any> => {

                var initialRedirectPromise;

                if ($location.path() == "/login") {
                    initialRedirectPromise = $q.when(authService.getSessionUser())
                        .then(function (user:IUser) {
                            if (user) {
                                authService.setUser(user);
                                RouteResolverProvider.that.redirectDefault($window, user);
                            }
                        });
                }

                return $q.when(initialRedirectPromise).then(function () {
                    var url = $location.absUrl();
                    var code = RouteResolverProvider.that.getCodeFromUrl(url);

                    if (code && url.indexOf("/error") == -1) {
                        return authService.requestUserAccessToken("http://localhost:8080", code)
                            .then(function (token) {
                                RouteResolverProvider.that.redirectDefault($window, authService.getCachedUser());
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
                        authenticationPromise = RouteResolverProvider.that.authHandler($q, authService);
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
                });
            };

            this.defaultRouteResolver = [
                '$q',
                '$rootScope',
                '$route',
                '$location',
                'sh-authentication-service',
                '$window',
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

        private redirectDefault($window:ng.IWindowService,
                                user:IUser) {
            if (user) {
                var redirectPath:string;
                switch (user.memberStatus.name.toLowerCase()) {
                    case "admin":
                        redirectPath = "/admin/houses";
                        break;
                    case "member":
                        redirectPath = "/houses";
                        break;
                    case "candidate":
                        redirectPath = "/candidate/form";
                }

            }
            $window.location.href = 'http://localhost:8080/#' + redirectPath;
        }

        public defaultRouteResolver:any;

        private routeResolver:($q:ng.IQService,
                               $rootScope:any,
                               $route:any,
                               $location:ng.ILocationService,
                               authService:SummerHouses.authentication.AuthenticationService,
                               $window:ng.IWindowService) => ng.IPromise < any >;

        public $get = ["$q", this.get];

        private get():any {
            return {};
        }
    }

    angular.module('housesApp').provider('routeResolver', RouteResolverProvider);
}