nw.Window.get().on('new-win-policy', function(frame, url, policy) {
    // do not open the window
    policy.ignore();
    // and open it in external browser
    nw.Shell.openExternal(url);
  })
  // This script is from http://docs.nwjs.io/en/latest/References/Window/#event-new-win-policy-frame-url-policy