///<reference path="../../../typings/angular.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>
///<reference path="settingsModel.ts"/>
///<reference path="../members/memberFormFieldModel.ts"/>
///<reference path="../utilities/utilities.ts"/>
///<reference path="../../../typings/moment.d.ts"/>

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
            this.getFormFields();

            this.$scope.editing = false;
            this.$scope.settings = {};
            this.$scope.formFields = {};
            this.$scope.showAlert = false;
            
            this.$scope.saveSettings = () => {
                this.formatReservationDateSetting(this.$scope.settings);
                this.$http.put('rest/settings', this.$scope.settings).success(() => {
                    _.forEach(this.$scope.originalFieldOptions, (field: members.MemberFormField) => {
                        field.visible = this.$scope.formFields[field.fieldName];
                    });
                    this.$http.put('rest/memberFormField', this.$scope.originalFieldOptions).success(() => {
                        this.$scope.editing = false;
                        this.showSuccessMessage();
                    });
                    
                });
            }

            this.$scope.startEditing = () => {
                this.$scope.editing = true;
            }

            this.$scope.months = Utilities.monthsSelectOptions();
            this.$scope.days = Utilities.getIteratingNumberArray(1, 31);
            this.$scope.reservationStartMonth = this.$scope.months[0];
            this.$scope.reservationStartDay = 1;
        }

        getSettings(): void{
            this.$http.get('/rest/settings').success((settings: Settings[]) => {
                this.$scope.settings = settings;
                var startDateSetting = _.find(settings, setting => {
                    return setting.referenceCode === "reservationStartDate";
                });
                if (startDateSetting){
                    var startDate = moment(startDateSetting.value);
                    this.$scope.reservationStartMonth = this.$scope.months[startDate.month()];
                    this.$scope.reservationStartDay = startDate.date();
                }
            });
        }

        getFormFields(): void{
            this.$http.get('/rest/memberFormField').success((fields: members.MemberFormField[]) => {
                this.$scope.originalFieldOptions = fields;
                _.forEach(fields, (field) => {
                    this.$scope.formFields[field.fieldName] = field.visible;
                });
            });
        }

        showSuccessMessage(): void{
            this.$scope.showAlert = true;
            setTimeout(() => {
                this.$scope.$apply(() => {
                    this.$scope.showAlert = false;
                })
            }, 4000)
        }

        formatReservationDateSetting(settings: Settings[]): void{
            var dateSetting = _.find(settings, setting => {
                return setting.referenceCode === "reservationStartDate";
            })

            if (dateSetting){
                var date = moment().month(this.$scope.reservationStartMonth.key).date(this.$scope.reservationStartDay);
                dateSetting.value = date.format('YYYY-MM-DD');
            }


        }

    }

    angular
        .module('housesApp')
        .controller('settingsController', SettingsController);
}