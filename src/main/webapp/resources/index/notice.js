document.getElementById("win").onclick = function (ev) {
    var req = new XMLHttpRequest();
    req.open("GET", "/stat?dld=w", true);
    req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    req.send();
};