var fs = require("benignTotal.json");
var obj = JSON.parse(fs.readFileSync('file', 'utf8'));
console.log(obj.totalWordsA);