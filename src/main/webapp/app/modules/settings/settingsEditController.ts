///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="settingsModel.ts"/>

module SummerHouses.settings {

    class SettingsEditController {

        static $inject = [
            '$scope',
            '$routeParams',
            '$http',
            '$location'
        ];

        constructor(
            private $scope: any,
            private $routeParams: any,
            private $http: any,
            private $location: any
        ) {
            this.getSettings();

            this.$scope.saveSettings = (settings: Settings[]) => {
                this.$http.put('rest/settings', settings).success(() => {
                    $location.path("/settings");
                });
            }
        }

        getSettings(): void{
            this.$http.get('/rest/settings').success((settings: Settings[]) => {
                this.$scope.settings = settings;
            });
        }
    }

    angular
        .module('housesApp')
        .controller('settingsEditController', SettingsEditController);
}