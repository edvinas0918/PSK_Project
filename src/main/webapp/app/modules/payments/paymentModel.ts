module SummerHouses.payments {

    export class Payment {
        public id:number;
        public paymentDate:Date;
        public confirmed:boolean;
        public taxID:Tax;
        public memberID:Member;
    }

    export class Tax {
        public id:number;
        public name:string;
        public price:number;
    }

    export class Member {
        public id:number;
        public firstName:string;
        public lastName:string;
        public email:string;
    }
}