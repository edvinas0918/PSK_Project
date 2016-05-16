module SummerHouses.houses {

    export class SummerHouse {
        public id:number;
        public taxID:number;
        public editMode:boolean;
        constructor(public endPeriod:Date, public beginPeriod:Date, public description:string, public capacity:number, public number:number, public image:any, public additionalServices:AdditionalService[]) {

        }
    }

    export class Tax {

        constructor(public id:number, public price:number) {

        }
    }

    export class HouseServicePrice {

        constructor(public houseID: number, public serviceID: number, public price: number) {

        }

    }

    export class AdditionalService {
        public id:number;
        public pricePoints:number;
        public description:string;
        public name:string;
        public selected:Boolean;
        public additionalservicereservationList:Array<SummerHouse>;

        constructor() {
            this.pricePoints = 0;
            this.description = "";
            this.name = "";
        }

    }
}