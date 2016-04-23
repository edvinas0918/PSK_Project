module SummerHouses.houses {

    export class SummerHouse {
        public id:number;
        public taxID:number;

        constructor(public availabilityPeriod:number, public description:string, public capacity:number, public number:number) {

        }
    }

    export class Tax {

        constructor(public id:number, public price:number) {

        }
    }
}