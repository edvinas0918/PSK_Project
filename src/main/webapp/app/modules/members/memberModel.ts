module SummerHouses.members {

    export class MemberStatus {
        public id:number;
        public name:string;
    }

    export class Member {
        public id:number;
        public firstName:string;
        public lastName:string;
        public email:string;
        public memberStatus:MemberStatus;
        public points:number;
        public reservationGroup:number;
    }
}