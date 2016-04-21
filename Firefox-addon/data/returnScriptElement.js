<!DOCTYPE html>
<html>
<body>

<script>
document.write("Hello World!");
</script>

<p>Click the button to display the number of script elements in the document.</p>

<button onclick="myFunction()">Try it</button>

<p id="demo"></p>

<script>
function myFunction() {
    var x = document.scripts
    document.getElementById("demo").innerHTML = "Found " + x[0].text + " script elements.";
}
</script>

</body>
</html>