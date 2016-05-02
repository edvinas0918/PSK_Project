module SummerHouses.mailing {

    export class Mailing {

        public currentUser: string;
        public emailAddresses: string [];

        constructor(currentUser: string, emailAddresses : string []){
            this.currentUser = currentUser;
            this.emailAddresses = emailAddresses;
        }

    }

}