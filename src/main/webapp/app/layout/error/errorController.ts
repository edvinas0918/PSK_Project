/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses {

    export class errorController {

        static $inject = [
            '$rootScope',
            '$scope'
        ];

        constructor (
            private $rootScope:any,
            private $scope: any
        ) {
            if ($rootScope.lastError)
            {

                var response = $rootScope.lastError.httpResponse;

                if ($rootScope.lastError.data)
                {
                    response = $rootScope.lastError.data;
                }
                else if (!response) {
                    response = $rootScope.lastError;
                }

                // handle display

                $scope.errorDescription = response.errorDescription;
                $scope.errorMessage = response.errorMessage;

                if (!response.errorDescription && !response.errorMessage)
                {
                    $scope.errorMessage = response;
                }

            }
            else {
                $scope.errorMessage = "An error in the application has occurred. ";
            }
        }

    }

    angular.module("housesApp").controller("errorController", errorController);
}