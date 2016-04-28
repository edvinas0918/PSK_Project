///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="settingsModel.ts"/>

module SummerHouses.settings {

    class SettingsController {

        static $inject = [
            '$rootScope',
            '$scope',
            '$http'
        ];

        constructor(
            private $rootScope:any,
            private $scope: any,
            private $http: any
        ) {

            this.getSettings();
        }

        getSettings(): void{
            this.$http.get('/rest/settings').success((settings: Settings[]) => {
                this.$scope.settings = settings;
            });
        }

    }

    angular
        .module('housesApp')
        .controller('settingsController', SettingsController);
}