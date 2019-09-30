var arrow = {};

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
    var ex1 = $("#ex1");
    var ex2 = $("#ex2");
    ex1.attr("src", ex1.attr("data-src"));
    ex2.attr("src", ex2.attr("data-src"));
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
}