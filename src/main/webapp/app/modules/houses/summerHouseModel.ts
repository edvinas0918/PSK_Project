module SummerHouses.houses {

    export class SummerHouse {
        public id:number;
        public taxID:number;
        public editMode:boolean;
        public additionalServices:AdditionalService[];
        constructor(public endPeriod:Date, beginPeriod:Date, public description:string, public capacity:number, public number:number) {

        }
    }

    export class Tax {

        constructor(public id:number, public price:number) {

        }
    }

    export class AdditionalService {

        constructor(public id:number, public priceInPoints:number, public description:string, public name:string, public selected:Boolean) {

        }

    }
}