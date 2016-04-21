/* This file transforms the javascript input using character key word transformation. 
The transformed strings consists of javascript key words and characters. */

//reserved is an array and it stores the JavaScript reserved key words



var characterKeywordArr = [];
var keywords =  ["download", "document", "break", "case", "catch", "continue", "debugger", "default", "delete", "do", "eval", "else",
			"finally", "for", "function", "if", "length", "in", "instanceof", "new", "random", "replace", "return", "switch", "this",
            "throw","try", "typeof", "var", "void", "window", "write", "fromcharcode",
			"while", "with", "unescape", "constructor", "toExponential", "toFixed", "toLocaleString", "toPrecision", "toString", "valueOf",
			"toSource", "toString", "valueOf", "charAt", "charCodeAt", "concat", "indexOf", "lastIndexOf", "localeCompare", "length",
			"match", "replace", "search", "slice", "split", "substr", "substring", "toLocaleLowerCase", "toLocaleUpperCase",
			"toLowerCase", "toString", "toUpperCase", "valueOf", "anchor", "big", "blink", "bold", "fixed", "fontcolor", "fontsize",
			"italics", "link", "small", "strike", "sub", "sup", "concat", "every", "filter", "forEach", "indexOf", "join", "lastIndexOf",
			"map", "pop", "push", "reduce", "reduceRight", "reverse", "shift", "slice", "some", "toSource", "sort", "splice", "toString",
			"unshift", "Date", "getDate", "getDay", "getFullYear", "getHours", "getMilliseconds", "getMinutes", "getMonth", "getSeconds",
			"getTime", "getTimezoneOffset", "getUTCDate", "getUTCDay", "getUTCFullYear", "getUTCHours", "getUTCMilliseconds",
			"getUTCMinutes", "getUTCMonth", "getUTCSeconds", "getYear", "setDate", "setFullYear", "setHours", "setMilliseconds",
			"setMinutes", "setMonth", "setSeconds", "setTime", "setUTCDate", "setUTCFullYear", "setUTCHours", "setUTCMilliseconds",
			"setUTCMinutes", "setUTCMonth", "setUTCSeconds", "setYear", "toDateString", "toGMTString", "toLocaleDateString",
			"toLocaleFormat", "toLocaleString", "toLocaleTimeString", "toSource", "toString", "toTimeString", "toUTCString", "valueOf",
			"abs", "acos", "asin", "atan", "atan2", "ceil", "cos", "exp", "floor", "log", "max", "min", "pow", "random", "round", "sin",
			"sqrt", "tan", "toSource", "exec", "test", "toSource", "toString", "element", "category", "name", "class", "http://","http","www.",".com","href","html"];


function characterKeywordtransform(script){
    script = script.toLowerCase();
    
    
    
    var comment= false;
    var ignoreUntilnl = false;
    
    for(var i = 0; i < script.length; i++){
        
        if(!comment && !ignoreUntilnl){
            for(var j = 0; j< keywords.length; j++){
                if(script.startsWith(keywords[j], i)){
                   characterKeywordArr.push(keywords[j])
                    i = i + keywords[j].length ;
                    continue;
                }
            }
        }//end of first if
        
        var currentChar = script.charAt(i);
        
        if(currentChar == "/"){
            if(script.charAt(i+1) == "/"){
                ignoreUntilnl = true;
                continue;
            }
            else if(script.charAt(i+1) == '*'){
                ignoreUntilnl= false;
                comment = true;
                continue;
            }
            else if((comment == true) && script.charAt(i-1) == '*'){
                comment = false;
                ignoreUntilnl = false;
                continue;
            }
        }//end of second if
        
        if(currentChar == ' ' || currentChar == '\t'){
            //ignore space or tab
        }
        
        else if(currentChar == '\n' || currentChar == '\r'){
            ignoreUntilnl = false;
        }
        else if(!comment && !ignoreUntilnl){
            characterKeywordArr.push(script.charAt(i));
        }
        
        
        
    }//end of outer for
    for(var k =0; k < characterKeywordArr.length; k++){
    	alert(characterKeywordArr[k]);
    }
    return characterKeywordArr;
    
}//end of method transform
