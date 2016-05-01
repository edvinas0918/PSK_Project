module SummerHouses.mailing {

    export class Mailing {

        public currentUser: string;
        public emailAddresses: string [];

        constructor(){
            this.currentUser = "";
            this.emailAddresses = [];
        }
    }

}