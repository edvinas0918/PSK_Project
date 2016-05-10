module SummerHouses.members {

    export class MemberStatus {
        public id:number;
        public name:string;

        constructor(id: number, name: string){
            this.id = id;
            this.name =  name;
        }
    }

    export class Member {
        public id:number;
        public firstName:string;
        public lastName:string;
        public email:string;
        public memberStatus: MemberStatus;
        public points:number;
        public reservationGroup:number;
        public membershipExpirationDate:Date;
        public statusString: string;

        constructor(){
            this.id = null;
            this.firstName = "";
            this.lastName = "";
            this.email = "";
            this.memberStatus = new MemberStatus(1, "Candidate");
            this.points = 0;
            this.reservationGroup = 0;
            this.membershipExpirationDate = null;
        }
    }

}