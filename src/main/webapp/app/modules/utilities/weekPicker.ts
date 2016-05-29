///<reference path="../../../typings/moment.d.ts"/>
///<reference path="../../../typings/lodash.d.ts"/>

module Utilities{

    class Period{
        fromDate: string;
        untilDate: string;

        constructor(fromDate: string, untilDate: string){
            this.fromDate = fromDate;
            this.untilDate = untilDate;
        }
    }

    class MomentPeriod{
        fromDate: moment.Moment;
        untilDate: moment.Moment;

        constructor(period: Period){
            this.fromDate = moment(period.fromDate).startOf("day");
            this.untilDate = moment(period.untilDate).startOf("day");
        }
    }

    export class WeekPicker{
        occupiedPeriods: MomentPeriod[];

        dates: moment.Moment[];
        format: string;
        startingDate: Date;

        constructor(occupiedPeriods: Period[], format?: string, startingDate?: string){
            this.dates = [];
            this.occupiedPeriods = _.map(occupiedPeriods, (period) => {
                return new MomentPeriod(period);
            });
            this.format = format ? format : "YYYY-MM-DD";
            this.startingDate = startingDate ? new Date(moment(startingDate).format("YYYY-MM-DD")) : new Date();
        }

        handleDateSelect(date: string) {
            var momentDate = moment(date);
            if (this.hasDateSelected(momentDate)){
                this.removeWeekByDate(momentDate)
            } else {
                if (this.weekIsIsolated(momentDate)){
                    this.removeAllDates();
                }
                this.addWeekByDate(momentDate);
            }
        }

        hasDateSelected(date){
            var momentDate = moment(date);
            return _.some(this.dates, (d) => {
                return d.format(this.format) == momentDate.format(this.format);
            });
        }

        addOrRemoveWeek(date: moment.Moment, addOrRemove){
            var weekDay = date.isoWeekday();
            var mondayDate = moment(date).add(1 - weekDay, 'days');
            for (var i = 0; i < 7; i++){
                var dayIterator = moment(mondayDate).add(i, 'days');
                addOrRemove(dayIterator);
            }
        }

        removeWeekByDate(date: moment.Moment){
            this.addOrRemoveWeek(date, (d: moment.Moment) => {
                _.remove(this.dates, (date) => {
                    return date.format(this.format) == d.format(this.format);
                });
            });
            if (this.dates.length > 7 && _.some(this.dates, (date) => {
                    return this.weekIsIsolated(date);
                })){
                this.removeAllDates();
            }
        }

        addWeekByDate(date: moment.Moment){
            this.addOrRemoveWeek(date, (d: moment.Moment) => {
                this.dates.push(d);
            });
        }

        weekIsIsolated(date: moment.Moment){
            var weekDay = date.isoWeekday();
            var mondayDate = moment(date).add(1 - weekDay, 'days');
            var sundayDate = moment(date).add(7 - weekDay, 'days');

            return !_.some(this.dates, (date) => {
                return moment(sundayDate).add(1, 'days').format(this.format) == date.format(this.format)
                    || moment(mondayDate).add(-1, 'days').format(this.format) == date.format(this.format)
            })
        }

        removeAllDates(){
            this.dates = [];
        }

        dateIsUnavailable(date: string) {
            var momentDate = moment(date);
            return _.some(this.occupiedPeriods, (period) => {
                return period.fromDate <= momentDate && period.untilDate >= momentDate;
            })
        }

        getReservationPeriod(): Period{
            var fromDate = _.min(this.dates).format(this.format);
            var untilDate = _.max(this.dates).format(this.format);
            return new Period(fromDate, untilDate);
        }
        
        getPeriod(): Period{
            if(this.dates.length == 0) return new Period(null, null);
            var fromDate = _.min(this.dates).format(this.format);
            var untilDate = _.max(this.dates).format(this.format);
            return new Period(fromDate, untilDate);
        }
    }
}