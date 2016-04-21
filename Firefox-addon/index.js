/*
	Firefox add on index.js
	Running this add on creates a add on button in firefox
	Author : Anumeha Shah
*/
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
  	contentScriptFile: [self.data.url("benignTotal.js"),
  						self.data.url("maliciousTotal.js"),
  						self.data.url("countsA.js"),
  						self.data.url("countsAB.js"),
  						self.data.url("maliciousCountsA.js"),
  						self.data.url("maliciousCountsAB.js"),
  						self.data.url("benignCountsAComposite.js"),
  						self.data.url("benignCountsABComposite.js"),
  						self.data.url("benignTotalComposite.js"),
  						self.data.url("maliciousCountsAComposite.js"),
  						self.data.url("maliciousCountsABComposite.js"),
  						self.data.url("maliciousTotalComposite.js"),
  						self.data.url("CharacterKeywordTransform.js"),
  						self.data.url("CompositeWordTransform.js"),
  						self.data.url("SLM_Script.js")]
  						
    
  });
  
  job.port.on("script-response", function(response) {
	console.log(response);
  });
}


