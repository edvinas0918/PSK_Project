/// <reference path="../../../typings/angular.d.ts" />

module SummerHouses.layout {
    export class TopnavDirective {

        public link:(scope:ng.IScope, element:ng.IAugmentedJQuery, attrs:ng.IAttributes) => void;
        public templateUrl = 'app/layout/topnav/topnav.html';
        public restrict = 'EA';
        public replace = true;

        constructor() {
        }

        public static Factory() {
            var directive = () => {
                return new TopnavDirective();
            };

            directive['$inject'] = [];

            return directive;
        }
    }

    angular.module('housesApp').directive('dirTopnav', TopnavDirective.Factory());
}