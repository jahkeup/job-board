var slideCanvas = $('#slide-canvas');
var socket, connect, recieveAndDisplay, reconnectOnEvent;
var max_retries = 0;           // * 3s OR 0 for infinite tries
var trying = false;
var retries = 1;

reconnectOnEvent = function(event) {
  // Rage quit, just refresh and try again.  This is useful for times
  // where the server is restarted so you don't have to refresh
  // browsers, they'll reconnect.
  slideCanvas.html("<h2>Disconnected from server. Will reconnect.</h2>");
  if (max_retries !== 0 && retries === max_retries) {
    console.log("Tried to reconnect too many times. Failing.");
    slideCanvas.html("<h2 class='text-danger'>Could not reconnect. Refresh the page to try again.</h2>");
  } else {
    console.log("Trying to connect again...");
    retries += 1;
    window.setTimeout(connect, 3000);
  }
};

recieveAndDisplay = function(data) {
  slideCanvas.html(data.data);
};

connect = function() {
  socket = new WebSocket("ws://" + window.location.host + ":" + "/slides/current" );

  // On errors, close existing socket and reconnect.
  socket.onclose = reconnectOnEvent;
  socket.onopen = function() { console.log("Connected! Took " + retries + " tries."); retries = 0; };

  // Wait for slides as bare html and then insert them in the page
  socket.onmessage = recieveAndDisplay;


};

// Do something.
connect();