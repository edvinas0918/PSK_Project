///<reference path="summerHouseModel.ts"/>

module SummerHouses.houses {

    export class SummerhouseSearchDto {
        public fromDate:string;
        public untilDate:string;
        public number:number;
        public capacity:number;
        public additionalServices:AdditionalService[];
    }
}