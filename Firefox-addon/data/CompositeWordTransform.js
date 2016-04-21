/* This file transforms the javascript input using composite word transformation.
 The transformed strings consists of various classes. These classes represents white space, digite hex etc*/

var compositeWordArr = [];
var currentWord ="";

function compositeWordTransform(script) {
	//alert(script);
    
    
    
    for(var i = 0; i<script.length; i++){
        var curChar = script.charCodeAt(i);
        
        if (curChar == 32 || curChar == 9 || curChar == 13) {
            addTocompositeWordArr(curChar, currentWord, "WHITESPACE");
        }
        else if (curChar < 32) {
            addTocompositeWordArr(curChar, currentWord, "PUNCTUATION");
        }
        else if (curChar == 37) {
            addTocompositeWordArr(curChar, currentWord, "PERCENT");
        }
        else if (curChar < 48) {
            addTocompositeWordArr(curChar, currentWord, "PUNCTUATION");
        }
        else if (curChar < 58) {
            currentWord = currentWord.concat(String.fromCharCode(curChar));   
        }
        else if (curChar < 65) {
            addTocompositeWordArr(curChar, currentWord, "PUNCTUATION");   
        }
        else if (curChar < 91) {
            currentWord = currentWord.concat(String.fromCharCode(curChar));   
        }
        else if (curChar < 97) {
            addTocompositeWordArr(curChar, currentWord, "PUNCTUATION");   
        }
        else if (curChar < 123) {
            currentWord = currentWord.concat(String.fromCharCode(curChar));   
        }
        else if (curChar < 127) {
            addTocompositeWordArr(curChar, currentWord, "PUNCTUATION");   
        }
        
           
    }//end of for
    //code
    return compositeWordArr;
}

function addTocompositeWordArr(curChar, word, wordType) {
    if (word.length > 0) {
    	//first check if currentWord is number
        if (!isNaN(word)) {
            //code
            compositeWordArr.push("DIGIT");
        }
        else if(word.match(/[0-9a-fA-F]{3}/i) !== null){
                
            for(var k =0; k< word.length/4; k++){
                compositeWordArr.push("HEX");
            }
        	
        }
        else{
        	compositeWordArr.push(word);
        }
        
    }
    currentWord = "";
    compositeWordArr.push(wordType);
    
    //code
}

