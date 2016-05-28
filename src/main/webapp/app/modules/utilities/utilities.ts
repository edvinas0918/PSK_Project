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

    export function monthsSelectOptions(): any[]{
        return [
            {"key": "0",
                "value": "Sausis",
                "days": 31},
            {"key": "1",
                "value": "Vasaris",
                "days": 28},
            {"key": "2",
                "value": "Kovas",
                "days": 31},
            {"key": "3",
                "value": "Balandis",
                "days": 30},
            {"key": "4",
                "value": "Gegužė",
                "days": 31},
            {"key": "5",
                "value": "Birželis",
                "days": 30},
            {"key": "6",
                "value": "Liepa",
                "days": 31},
            {"key": "7",
                "value": "Rūgpjutis",
                "days": 31},
            {"key": "8",
                "value": "Rugsėjis",
                "days": 30},
            {"key": "9",
                "value": "Spalis",
                "days": 31},
            {"key": "10",
                "value": "Lapkritis",
                "days": 30},
            {"key": "11",
                "value": "Gruodis",
                "days": 31}
        ];
    }

    export function getIteratingNumberArray(minValue: number, maxValue: number){
        var arr = [];
        for (var i = minValue; i <= maxValue; i++) {
            arr.push(i);
        }
        return arr;
    }
}


