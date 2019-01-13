
var groupList = {};
groupList.names = [];
groupList.contents = [];

var container;

$(document).ready(function () {
    container = $("#group_staff_cont");
    var db_groups = $(".group_temp");
    db_groups.each(function (i) {
        groupList.names.push($(this).attr("data-name"));
        groupList.contents.push($(this).html());
    });

    var firstGroup =  $(".group").first();

    firstGroup.addClass("group_selected");
    var idx = groupList.names.indexOf(firstGroup.text());
    container.html(groupList.contents[idx]);




    groupOnClick();

    $(".person_staff").on("click", function () {
        $(this).toggleClass('person_selected');
    });

    $("#del_group").on("click", function () {
        var gr = $(".group_selected");
        var idx = groupList.names.indexOf(gr.text());
        if (idx > -1) {
            groupList.names[idx] = undefined;
            groupList.contents[idx] = undefined;
        }
        gr.remove();

    });






    $(".group_btn").click(function () {
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






    // func TODO on click to profile

});

function groupOnClick() {
    var group = $(".group");
    group.on("click", function () {
        var gr_name = $(".group_selected").text();
        saveGroup(gr_name);
        group.removeClass().addClass("group");
        $(this).addClass("group_selected");

        groupList.names.push(gr_name);
        groupList.contents.push(container.html());
        container.empty();
        var idx = groupList.names.indexOf($(this).text());
        if (idx > -1) container.html(groupList.contents[idx]);
    });
}

function personOnClick() {

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

function saveGroup(gr_name){

    var persons = $("#group_staff_cont").children();
    var p_ids = [];

    persons.each(function (i) {
        p_ids[i] = $(this).find($(".person_point")).attr("data-p-id");
    });
    $.ajax({type:"POST", url:"/savegroup", data:{groupName:gr_name, personIds: p_ids.join(",")}})






}

/*$(window).on("unload", function () {
    saveGroup();
});*/

