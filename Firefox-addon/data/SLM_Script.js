/*
	The project is a conversion of TARDIS into firefox add on
	Primary add on file 
	This is a content script
	This script extracts Javascript from the page in the current tab and perform machine learning analysis
	and classify the javascript either malicious or benign
*/
//var self = require('sdk/self');

//document.body.innerHTML = document.scripts[0].text;
var scripts = document.scripts;
var AlphaNumSet = createAlphaNumSet();
var AlphanumericUniformProbabilityStrategy = {"UniformProb" : 1.0/62.0};

var nGramSize = 1;
var benignModelScore = 0;
var maliciousModelScore  = 0;

var benignAverageScore = 0;
var maliciousAverageScore = 0;

self.port.emit("script-response", "Total no of Scripts : " + scripts.length);


//running for ngram 4
for(var i = 4; i<=4; i++){
	run(i);
}

self.port.emit("script-response", "BenignAverageScore : " + benignAverageScore/scripts.length);
self.port.emit("script-response", "maliciousAverageScore : " + maliciousAverageScore/scripts.length);



//This is the main function which takes the script input and calculate probability
function run(ngram){
	nGramSize = ngram;
	
	//calculate benign and malicious probability score for each script
	for(var i = 0; i < scripts.length; i++ ){
		self.port.emit("script-response", scripts[i].text);
		
		var benignModelScore = query(scripts[i].text, nGramSize, "benign", "input");
		var maliciousModelScore = query(scripts[i].text, nGramSize, "malicious", "input");
		
		//var benignCompositeArr = compositeWordTransform(scripts[i].text);
		//var benignCompositeModelScore = query(benignCompositeArr, nGramSize, "benign", "composite");
		
		//self.port.emit("script-response", "BenignComposite : " + benignCompositeModelScore);
		
		benignAverageScore = benignAverageScore + benignModelScore;
		maliciousAverageScore = maliciousAverageScore + maliciousModelScore;
		
        self.port.emit("script-response", "Benign : " + benignModelScore);
        self.port.emit("script-response", "Malicious : " +maliciousModelScore);
        
        if(maliciousModelScore > benignModelScore){
            self.port.emit("script-response", "malicious");
        }
        else{
            self.port.emit("script-response", "benign");
        }
		
	}
	
}

function query(script, nGramSize, type, model){
	var probability = 0.0;
	if(nGramSize == 1){
		var pA = 0.0;
		
		for(var i = 0; i< script.length; i++ ){
			pA = getPA(script.slice(i, i + nGramSize), type, model);
			
			probability = probability + Math.log(pA);
		}
		
	}
	else{
		for(var i = 0; i< script.length - nGramSize; i++ ){
			pA = getPA(script.slice(i, i + nGramSize -1), type, model);
			
			pAB = getPAB(script.slice(i, i + nGramSize), type, model);
			probability = probability + Math.log(pAB/pA);
			
		}
	}
	
	return probability
	
}

//This funciton get the count of ngram/totalwords for ngram  nGramSize -1
function getPA(wordSequence, type, model){
	var p = 1.0;
	/*
	var p = getP(wordSequence);
	if(p >= 0){
		alert(p);
		return p;
	}
	*/
	if(type == "benign"){
	
		if(model == "input"){
			if(benignCountsA[wordSequence] == undefined){
				incrementCountA(wordSequence, type);
			}
		
			p = benignCountsA[wordSequence]/benignTotal.totalWordsA;
			return p;
		}
		if(model == "composite"){
			alert(wordSequence);
			if(benignCountsAComposite[wordSequence] == undefined){
				incrementCountA(wordSequence, type);
			}
		
			p = benignCountsAComposite[wordSequence]/benignTotalComposite.totalWordsA;
			alert(benignCountsAComposite[wordSequence]);
			return p;
		}
	}
	
	else if(type == "malicious"){
		if(model == "input"){
			if(maliciousCountsA[wordSequence] == undefined){
				incrementCountA(wordSequence, type);
			}
		
			p = maliciousCountsA[wordSequence]/maliciousTotal.totalWordsA;
			return p;
		}
		
		if(model == "composite"){
			if(maliciousCountsAComposite[wordSequence] == undefined){
				incrementCountA(wordSequence, type);
			}
		
			p = maliciousCountsAComposite[wordSequence]/maliciousTotalComposite.totalWordsA;
			return p;
		}
	}
	
	
	
	
}

//This funciton get the count of ngram/totalwords for ngram  nGramSize
function getPAB(wordSequence, type, model){
	//alert(wordSequence);
	var p = 1.0
	/*var p = getP(wordSequence);
	if(p >= 0){
		return p;
	}*/
	
	if(type == "benign"){
		if(model == "input"){
		
			if(benignCountsAB[wordSequence] == undefined){
				incrementCountAB(wordSequence, type);
			}
		
			p = benignCountsAB[wordSequence]/benignTotal.totalWordsAB;
		
			return p;
		}
		else {
		
			if(benignCountsABComposite[wordSequence] == undefined){
				incrementCountAB(wordSequence, type);
			}
		
			p = benignCountsAComposite[wordSequence]/benignTotalComposite.totalWordsAB;
		
			return p;
		}
	}
	else if(type == "malicious"){
		if(model == "input"){
			if(maliciousCountsAB[wordSequence] == undefined){
				incrementCountAB(wordSequence, type);
			}
		
			p = maliciousCountsAB[wordSequence]/maliciousTotal.totalWordsAB;
		
			return p;
		}
		else{
			if(maliciousCountsABComposite[wordSequence] == undefined){
				incrementCountAB(wordSequence, type);
			}
		
			p = maliciousCountsABComposite[wordSequence]/maliciousTotalComposite.totalWordsAB;
		
			return p;
		}
	}
	
	
	
}

function getP(wordSequence){
	//alert(wordSequence);
	var p = 1.0
	
	if(AlphaNumSet.indexOf(wordSequence) != -1){
		return p;
	}
	else{
		return -1
	}
}

function incrementCountA(wordSequence, type){
	if(type == "benign"){
		benignCountsA[wordSequence] = 1;
		
	}
	else if(type == "malicious"){
		maliciousCountsA[wordSequence] = 1;
	}
}

function incrementCountAB(wordSequence, type){
	if(type == "benign"){
		benignCountsAB[wordSequence] = 1;
		//alert(benignCountsAB[wordSequence]);
	}
	else if(type == "malicious"){
		maliciousCountsAB[wordSequence] = 1;
	}
}

function createAlphaNumSet(){
	var AlphaNumSet = [];
	for(var i = 48; i <= 57; i++){
		AlphaNumSet.push(String.fromCharCode(i));
	}
	//A-Z
	for(var i = 65; i <= 90; i++){
		AlphaNumSet.push(String.fromCharCode(i));
	}
	//a-z
	for(var i = 97; i <= 122; i++){
		AlphaNumSet.push(String.fromCharCode(i));
	}
	return AlphaNumSet;
}