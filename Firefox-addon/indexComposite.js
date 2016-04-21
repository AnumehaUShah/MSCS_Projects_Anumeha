var self = require('sdk/self');
var tabs = require('sdk/tabs');
var buttons = require("sdk/ui/button/action");


// a dummy function, to show how tests work.
// to see how to test this function, look at test/test-index.js
function dummy(text, callback) {
  callback(text);
}

exports.dummy = dummy;


buttons.ActionButton({
	id: "attach-script",
	label: "Attach the script",
	icon:{
      "16": "./icon-16.png",
      "32": "./icon-32.png"
    },
	onClick: runScript
});

function runScript() {
  var job = tabs.activeTab.attach({
  	contentScriptFile: [self.data.url("CompositeWordTransform.js")]
  						
    
  });
  
  job.port.on("script-response", function(response) {
	console.log(response);
  });
}


