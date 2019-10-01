var arrow = {};

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

    //var go = this.nextSibling.nextSibling;
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
    document.body.style.overflowY = "auto";
    document.getElementsByClassName("features")[0].scrollIntoView({behavior : "smooth", block: "start", inline: "nearest"});
    arrow.style.display = "none";
}










/*
$(document).ready(function () {
    $(".wrapper").hover(function () {
        var showed = $(".lets_go_showed");
        showed.removeClass("lets_go_showed");
        showed.addClass("lets_go");
        var go = $(this).next();
        go.removeClass("lets_go");
        go.addClass("lets_go_showed");
    });

    $(".lets_go").click(function () {
        location.href = $(this).attr("id") === "cloud" ? "/notice_cloud" : "/notice_local";
    });
    /!*var ex1 = $("#ex1");
    var ex2 = $("#ex2");
    ex1.attr("src", ex1.attr("data-src"));
    ex2.attr("src", ex2.attr("data-src"));*!/
    arrow = $(".arrow");

    setTimeout(function () {
        $("body").css("overflow-y", "auto");
        if (window.pageYOffset > 0) return;
        arrow.attr("src", arrow.attr("data-src"));
        arrow.css("display", "block");
    }, 2000);
    arrow.click(scroll);
    $(window).scroll(scroll);

    $(".q").click(function () {
        var question = $(this).next();
        if (question.hasClass("hidden")) {
            showTree(question);
            $(document).scrollTop(9999);
        }
        else hideTree(question);
    });

    $(".send").click(function () {
        var email = $(".email").val();
        var text = $("textarea").val();
        $.post("/question", {message : text, email : email});
        hideTree($(".question"));
    })



});

function showTree(element) {
    element.removeClass("hidden");
    var children = element.children();
    if (children.length > 0) showTree(children);

}

function hideTree(element) {
    element.addClass("hidden");
    var children = element.children();
    if (children.length > 0) hideTree(children);
}

function scroll() {
    $(window).unbind("scroll", scroll);
    //window.scrollTo(0, $("#header").height() + 2);
    $('html, body').animate({ scrollTop: $(".office").height() + 2 }, 600);
    arrow.css("display", "none");
}*/
