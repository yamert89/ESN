$(document).ready(function () {
    personOnClick();

    groupOnClick();



    $(".group_add").click(function () {
        var title = prompt("Введите название группы", "Без названия");
        $(".groups").prepend("<div class='group'>" + title + "</div>");
        groupOnClick();
    });

    $("#right_arrow").click(function () {
        insertUsersInGroup();
    });

    $("#left_arrow").click(function () {
        removeUsersFromGroup();
    });

    $(".group").first().addClass("group_selected");


    // func TODO on click to profile

});

function groupOnClick() {
    var group = $(".group");
    group.click(function () {
        group.removeClass("group_selected");
        $(this).addClass("group_selected");

    });
}

function personOnClick() {
    $(".person_staff").click(function () {
        $(this).toggleClass('person_selected');
    });
}
function insertUsersInGroup() {
    var gr_st = $("#group_staff_cont");
    var all_st = $("#all_staff_cont");
    all_st.children(".person_selected").clone().appendTo(gr_st);
    $(".person_staff").removeClass("person_selected");
    gr_st.children().not(".old_staff").click(function () {
        $(this).toggleClass('person_selected');
    });
    gr_st.children().addClass("old_staff");
}

function removeUsersFromGroup() {
    $("#group_staff_cont").children(".person_selected").remove();
}