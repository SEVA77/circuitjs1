//System commands for calling:

function setScaleUI(){
  let scale = document.getElementById("scaleUI").value;
  nw.Window.get().zoomLevel = parseFloat(scale);
}

function getScaleInfo(){
  let scaleString = document.querySelector('.scaleInfo');
  let scale = document.getElementById("scaleUI").value;
  scaleString.textContent = parseInt(scale*100+100)+"%";
}

nw.Window.get().setMinimumSize(640, 480); // for new windows


// For Run/Stop and Reset buttons:

function SetBtnsStyle() {
  let RunStopBtn = document.getElementsByClassName("run-stop-btn")[0];
  let ResetBtn = document.getElementsByClassName("reset-btn")[0];

  if (document.getElementById("trigger").checked == true){
    RunStopBtn.style.display = "none";
    ResetBtn.style.display = "none";
  } else {
    setTimeout(() => {
      RunStopBtn.style.display = "block";
      ResetBtn.style.display = "block";
    }, 1100);
  }

  if (CircuitJS1.isRunning() == false) {
    RunStopBtn.innerHTML = '&#xE801;'; // \e801
    if(RunStopBtn.classList.contains('modDefaultRunStopBtn')){
      RunStopBtn.style.color = "green";
      RunStopBtn.style.borderColor = "green";
    }
  } else {
    RunStopBtn.innerHTML = '&#xE800;'; // \e800
    if(RunStopBtn.classList.contains('modDefaultRunStopBtn')){
      RunStopBtn.style.color = "red";
      RunStopBtn.style.borderColor = "red";
    }
  }
}

