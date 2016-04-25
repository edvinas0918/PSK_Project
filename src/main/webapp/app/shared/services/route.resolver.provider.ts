/// <reference path="../../../typings/angular.d.ts" />/// <reference path="../../../typings/angular.d.ts" />
///<reference path="../../modules/authentication/services/auth.service.ts"/>

module SummerHouses.shared {
    

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
                                  authService: SummerHouses.authentication.AuthenticationService) => {
                var deferred = $q.defer();

                var reject = (val:any) => {
                    $rootScope.isResolvingRoute = false;

                    console.log("rejected", val);
                    deferred.reject(val);
                };

                var resolve = (val:any) => {
                    $rootScope.isResolvingRoute = false;

                    console.log("resolved", val);
                    deferred.resolve(val);
                };


                $rootScope.isResolvingRoute = true;

                var routeDefinition = $route.current;

                var requireAuth = true;

                if (routeDefinition.requireAuthenticatedUser === false) {
                    requireAuth = false;
                }

                var authenticationPromise:ng.IPromise<any>;

                if (requireAuth) {
                    authenticationPromise = this.authHandler($q, authService);
                }

                if (!routeDefinition.login) {
                    return authenticationPromise.then(resolve, reject);
                }
                else {
                    resolve('login');
                }

                return deferred.promise;
            };

            this.defaultRouteResolver = [
                '$q',
                '$rootScope',
                '$route',
                'sh-authentication-service',
                this.routeResolver
            ];
        }

        private authHandler = ($q:ng.IQService,
                               authService: any
        ):ng.IPromise<any> => {

            var userInfo = authService.user;
            
            var authError = () => {
                return $q.reject({authenticated: false});
            };

            var authSuccess = () => {
                return $q.when(userInfo);
            };

            return authService.requestUserAccessToken().then((response) => {
                // now check if logged in
                console.log("response from requestUserAccessToken: ");
                console.log(response);
                
                if (authService.isLoggedIn())
                    return authSuccess();
                else
                    return $q.reject({ authenticated: false });
            }, authError);
        };

        public defaultRouteResolver:any;

        private routeResolver:($q:ng.IQService,
                               $rootScope:any,
                               $route:any,
                               authService: SummerHouses.authentication.AuthenticationService
        ) => ng.IPromise < any >;

        public $get = ["$q", this.get];

        private get():any {
            return {};
        }
    }

    angular.module('housesApp').provider('routeResolver', RouteResolverProvider);
}