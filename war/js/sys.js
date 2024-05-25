//System commands for calling:

nw.Window.get().setMinimumSize(640, 480); // for new windows


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

