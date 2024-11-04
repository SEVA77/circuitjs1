//System commands for calling:

document.title = nw.App.manifest.window.title;
nw.Window.get().setMinimumSize(640, 480); // for new windows

let losesFocusPause = false;

//nw.Window.get().on with 'focus' and 'blur' are not working properly
//(https://github.com/nwjs/nw.js/issues/7982)
//Need another method.

/*
nw.Window.get().on('focus', function(){
  let needPause = localStorage.getItem('MOD_setPauseWhenWinUnfocused');
  if (losesFocusPause && needPause=="true"){
    CircuitJS1.setSimRunning(true);
    losesFocusPause=false;
    SetBtnsStyle();
  }
})
nw.Window.get().on('blur', function(){
  let needPause = localStorage.getItem('MOD_setPauseWhenWinUnfocused');
  if (needPause=="true"){
    if (CircuitJS1.isRunning()) losesFocusPause=true;
      CircuitJS1.setSimRunning(false);
      SetBtnsStyle();
  }
})
*/

// New method:

function onWinFocus(){
  let needPause = localStorage.getItem('MOD_setPauseWhenWinUnfocused');
  if (losesFocusPause && needPause=="true"){
    CircuitJS1.setSimRunning(true);
    losesFocusPause=false;
    SetBtnsStyle();
  }
}
function onWinBlur(){
  let needPause = localStorage.getItem('MOD_setPauseWhenWinUnfocused');
  if (needPause=="true"){
    if (CircuitJS1.isRunning()) losesFocusPause=true;
      CircuitJS1.setSimRunning(false);
      SetBtnsStyle();
  }
}
window.addEventListener("focus", onWinFocus);
window.addEventListener("blur", onWinBlur);

//but the blur event is triggered when the iframe is clicked

// For modDialog:

function setScaleUI(){
  let scale = document.getElementById("scaleUI").value;
  nw.Window.get().zoomLevel = parseFloat(scale);
}

function getScaleInfo(){
  let scaleString = document.querySelector('.scaleInfo');
  let scale = document.getElementById("scaleUI").value;
  scaleString.textContent = parseInt(scale*100+100)+"%";
}

function setTrLabelPos (top) {
  let triggerLabel = document.querySelector(".triggerLabel");
  triggerLabel.style.top = top;
}


// For Run/Stop and Reset buttons:

function SetBtnsStyle() {

  let absBtnIcon = localStorage.getItem('MOD_absBtnIcon');
  let hideAbsBtns = localStorage.getItem('MOD_hideAbsBtns');

  let RunStopBtn = document.getElementsByClassName("run-stop-btn")[0];
  let ResetBtn = document.getElementsByClassName("reset-btn")[0];

  if (document.getElementById("trigger").checked == true){
    RunStopBtn.style.display = "none";
    ResetBtn.style.display = "none";
  } else {
    if (hideAbsBtns=="false"){
      setTimeout(() => {
        RunStopBtn.style.display = "block";
        ResetBtn.style.display = "block";
      }, 1100);
    }
  }

  if (CircuitJS1.isRunning() == false) {
    RunStopBtn.innerHTML = '&#xE801;'; // \e801
    if(RunStopBtn.classList.contains('modDefaultRunStopBtn')){
      RunStopBtn.style.color = "green";
      RunStopBtn.style.borderColor = "green";
    }
  } else {
    if (absBtnIcon=="pause") RunStopBtn.innerHTML = '&#xE802;';
    else RunStopBtn.innerHTML = '&#xE800;';
    if(RunStopBtn.classList.contains('modDefaultRunStopBtn')){
      RunStopBtn.style.color = "red";
      RunStopBtn.style.borderColor = "red";
    }
  }
}

