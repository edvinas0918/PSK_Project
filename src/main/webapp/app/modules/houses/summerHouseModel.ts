module SummerHouses.houses {

    export class SummerHouse {
        public id:number;

        constructor(public availabilityPeriod:number, public description:string, public capacity:number, public number:number) {

        }
    }
}