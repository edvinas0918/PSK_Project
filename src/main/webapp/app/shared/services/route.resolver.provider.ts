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
                                  authService:SummerHouses.authentication.AuthenticationService) => {
                var deferred = $q.defer();

                var routeDefinition = $route.current;

                var requireAuth = false;
                if (routeDefinition.allowedRoles.indexOf('Candidate') == -1) {
                    requireAuth = true;
                }

                var authenticationPromise:ng.IPromise<any>;

                if (requireAuth) {
                    authenticationPromise = this.authHandler($q, authService);
                }

                $q.when(authenticationPromise).then(function (userInfo:IUser) {
                    //check for roles
                    if (userInfo) {
                        var userRole = userInfo.memberStatus.name;
                        if (routeDefinition.allowedRoles.indexOf(userRole) == -1) {
                            deferred.reject({reason: "permission denied"});
                        } else {
                            deferred.resolve(userInfo);
                        }
                    } else {
                        deferred.resolve(true);
                    }
                }, function (error) {
                    $location.path('/login');
                    deferred.reject(error);
                });

                return deferred.promise;
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

            return (authService.isLoggedIn()) ? authService.getUser() : $q.reject({authenticated:false});
        };

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