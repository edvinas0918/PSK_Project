module Utilities{
    export function resolveMemberStatusString(status: string): string{
        if (!status) return "";
        switch(status.toLowerCase()){
            case "admin":
                return "Administratorius";
            case "member":
                return "Klubo narys";
            case "candidate":
                return "Kandidatas";
        }
    }
}


