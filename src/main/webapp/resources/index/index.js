var arrow = {};
if (sessionStorage.getItem('scroll') == '1') {
    document.body.style.overflowY = 'auto';
}

var els = document.getElementsByClassName("wrapper");
els[0].onmouseover = lets;
els[1].onmouseover = lets;

function lets(){
    var f1 = document.getElementById("cloud_w");
    var f2 = document.getElementById("local_w");
    if (this != f1) {
        var temp = f1;
        f1 = f2;
        f2 = temp;

    }
    f1.nextSibling.nextSibling.classList.remove("lets_go");
    f1.nextSibling.nextSibling.classList.add("lets_go_showed");
    f2.nextSibling.nextSibling.classList.remove("lets_go_showed");
    f2.nextSibling.nextSibling.classList.add("lets_go");
}

function hide(){
    var showed = this.nextSibling.nextSibling;

}

var notices = document.getElementsByClassName("lets_go");
notices[0].onclick = notice;
notices[1].onclick = notice;


function notice(){
    location.href = this.getAttribute("id") === "cloud" ? "/notice_cloud" : "/notice_local";
}

arrow = document.getElementsByClassName("arrow")[0];
setTimeout(function () {
    if (window.pageYOffset > 0) return;
    arrow.style.display = "block";
}, 2000);

arrow.onclick = scroll;

document.getElementsByClassName("q")[0].onclick = function () {
    var question = this.nextSibling.nextSibling;
    if (question.classList.contains("hidden")) {
        showTree(question);
        window.scrollTo(0, 9999);
    }
    else hideTree(question);
};

document.getElementsByClassName("send")[0].onclick = function () {
    var email = document.getElementsByClassName("email")[0].value;
    var text = document.getElementsByTagName("textarea")[0].value;
    var req = new XMLHttpRequest();
    req.open("POST", "/question", true);
    req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    req.send('message=' + text + '&email='+ email);
    hideTree(document.getElementsByClassName("question")[0]);
};

function showTree(element) {
    var children = element.childNodes;
    if (children.length > 1) {
        children.forEach(function (value) {
            if (value.nodeName == "#text") return;
            showTree(value);});
    }
    element.classList.remove("hidden");

}

function hideTree(element) {
    var children = element.childNodes;
    if (children.length > 1) {
        children.forEach(function (value) {
            if (value.nodeName == "#text") return;
            hideTree(value);
        });

    }
    element.classList.add("hidden");
}

function scroll() {
    sessionStorage.setItem('scroll', '1');
    document.body.style.overflowY = "auto";
    document.getElementsByClassName("features")[0].scrollIntoView({behavior : "smooth", block: "start", inline: "nearest"});
    arrow.style.display = "none";
}