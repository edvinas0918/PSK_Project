module SummerHouses.houses {

    export class SummerHouse {
        public id:number;
        public taxID:number;
        public editMode:boolean;

        constructor(public endPeriod:Date, beginPeriod:Date, public description:string, public capacity:number, public number:number) {

        }
    }

    export class Tax {

        constructor(public id:number, public price:number) {

        }
    }
}