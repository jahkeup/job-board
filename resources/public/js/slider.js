var slideCanvas = $('#slide-canvas');
var socket = new WebSocket("ws://" + window.location.host + ":" + "/slides/current" );

// Wait for slides as bare html and then insert them in the page
socket.onmessage = function(slide) {
  console.log("Receiving slide..", slide);
  slideCanvas.html(slide.data);
};
