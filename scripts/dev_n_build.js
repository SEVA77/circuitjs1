const { statSync } = require('node:fs');
//const { version } = require('os');

const stepNames = [ // Name of steps
    "Devmode (mvn gwt:devmode)",
    "WebApp (mvn clean install)"
]

const pathsToCheck = [
    './war/circuitjs1/setuplist.txt',
    './target/site/circuitjs1/setuplist.txt'
]

let stateOfSteps = new Array(2); // State of steps
let diffTimes_ms = new Array(2); // diff between mtime and current time

function checkSteps() {

    let lastStep = -1;
    let lastStepTime = -1;
    let currentTime = new Date();

    function getTimeAgoInfo(time){
        time/=1000;     // milliseconds to seconds
        if (time<60) {return Math.floor(time)+" seconds ago"}
        else if (time<60**2){
            let time_m = Math.floor(time/60);
            let time_s = Math.floor(time-time_m*60);
            return time_m+" min. "+time_s+" sec. ago";
        }
        else if (time<24*(60**2)){
            let time_h = Math.floor(time/60**2);
            let time_m = Math.floor((time-time_h*(60**2))/60);
            let time_s = Math.floor(time-time_h*(60**2)-time_m*60);
            return time_h+" h. "+time_m+" min. "+time_s+" sec. ago";
        }
        else{
            let time_d = Math.floor(time/(24*(60**2)));
            let time_h = Math.floor((time-time_d*24*(60**2))/60**2)
            let time_m = Math.floor((time-time_d*24*(60**2)-time_h*(60**2))/60)
            let time_s = Math.floor(time-time_d*24*(60**2)-time_h*(60**2)-time_m*60);
            return time_d+" d. "+time_h+" h. "+time_m+" min. "+time_s+" sec. ago";
        }
    }

    for (let i = 0; i < pathsToCheck.length; i++) {
        try{
            let statInfo = statSync(pathsToCheck[i]);
            stateOfSteps[i] = true;
            diffTimes_ms[i] = currentTime.getTime()-statInfo.mtime.getTime();
            if (lastStepTime>diffTimes_ms[i] || lastStepTime<0){
                lastStep = i;
                lastStepTime = diffTimes_ms[i];
            }
        }catch{
            stateOfSteps[i] = false;
            diffTimes_ms[i] = null;
        }
    }

    console.log("Check steps:");
    for (let i = 0; i < stepNames.length; i++) {
        let getLastСhangeInfo = (i==lastStep) ? " ◀- LAST" : "";
        let getStepCompliteInfo = (stateOfSteps[i]) ? "[✔] " : "[✘] ";
        let basicInfo = getStepCompliteInfo+stepNames[i];
        if (stateOfSteps[i])
            console.info(basicInfo+" - "+getTimeAgoInfo(diffTimes_ms[i])+getLastСhangeInfo)
        else
            console.warn(basicInfo)
    }

}

checkSteps();
