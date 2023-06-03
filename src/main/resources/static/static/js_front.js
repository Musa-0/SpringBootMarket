var cell = document.getElementById("cell1");
var xmlHttp = new XMLHttpRequest();

function httpGet(theUrl)
{
    xmlHttp.responseType = 'json';
    xmlHttp.open("GET", theUrl ); // false for synchronous request
    xmlHttp.send();


}

xmlHttp.onload = function() {
    var json_account = xmlHttp.response;
    console.log(json_account)
    var tree = document.createDocumentFragment();
    for (var i in json_account) {
        var div = document.createElement("div");
        var image = document.createElement('img')
        div.setAttribute("id", "account" + i);
        div.appendChild(document.createTextNode(json_account[i]["title"]));
        image.src = json_account[i]["images"][0]["imageMedia"]["url"];

        tree.appendChild(div);
        tree.appendChild(image);
    }

	document.getElementById("main").appendChild(tree);
    };

httpGet("http://127.0.0.1:8080/products/product/")