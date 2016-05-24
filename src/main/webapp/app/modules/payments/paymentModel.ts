module SummerHouses.payments {

    export class Payment {
        public id:number;
        public paymentDate:Date;
        public confirmed:boolean;
        public canceled:boolean;
        public taxID:Tax;
        public memberID:Member;
        public price:number;
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