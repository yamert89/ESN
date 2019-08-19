
var groupList = {};
groupList.names = [];
groupList.contents = [];

var container;

$(document).ready(function () {
    $("#groups").addClass("selected");
    var group = $(".group");
    container = $("#group_staff_cont");
    var db_groups = $(".group_temp");
    db_groups.each(function (i) {
        groupList.names.push($(this).attr("data-name"));
        groupList.contents.push($(this).html());
    });

    var firstGroup =  group.first();

    firstGroup.addClass("group_selected");
    var idx = groupList.names.indexOf(firstGroup.text());
    container.html(groupList.contents[idx]);

    $(".person_staff").on("click", function () {
        $(this).toggleClass('person_selected');
    });

    $("#del_group").on("click", deleteGroup);


    $("#add_group").click(function () {
        if (!emptyGroups()) checkEmptyGroup();
        var title = prompt("Введите название группы", "Без названия");
        if (title == undefined) return;
        $(".groups").prepend("<div class='group'>" + title + "</div>");

        $(".groups").children().first().click();
    });

    $("#right_arrow").click(function () {
        if (emptyGroups()) {
            notify("Вначале создайте новую группу");
            return;
        }
        insertUsersInGroup();

    });

    $("#left_arrow").click(function () {
        if (emptyGroups()) {
            notify("Вначале создайте новую группу");
            return;
        }
        removeUsersFromGroup();
    });

    $("#save_group").click(saveGroup);


    $(".central_wrapper").on("click", '.group', function () {
        if (!emptyGroups()) checkEmptyGroup();
        var gr_name = $(".group_selected").text();

        $(".group.group_selected").removeClass("group_selected");
        $(this).addClass("group_selected");

        if (gr_name == '') return;

        groupList.names.push(gr_name);
        groupList.contents.push(container.html());
        container.empty();
        var idx = groupList.names.indexOf($(this).text());
        if (idx > -1) container.html(groupList.contents[idx]);
    });

    $(document).on('click', '.person_photo_staff', function () {
        var login = $(this).parent().next().find($(".person_point")).attr("data-p-login");
        location.href = "/" + orgUrl + "/users/" + login;
    })




});





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

function saveGroup(){
    var gr_name = $(".group.group_selected").first().text();
   // if (groupList.names.indexOf(gr_name) !== -1) return;

    var persons = $("#group_staff_cont").children();
    if (persons.length === 0) {
        notify("Добавьте людей или удалите пустую группу");
        return;
    }
    var p_ids = [];

    persons.each(function (i) {
        p_ids[i] = $(this).find($(".person_point")).attr("data-p-id");
    });
    $.ajax({type:"post", url:"/savegroup", data:{groupName:gr_name, personIds: p_ids.join(",")},
        success: successSave, error: error})

}

function successSave() {
    notify('Группа сохранена');
}

function error(err, stat) {
    notify("Ошибка сохранения группы: " + stat);
}

function deleteGroup(){
    var gr = $(".group_selected");
    var idx = groupList.names.indexOf(gr.text());
    $.ajax({type:"get", url:"/deletegroup", data:{groupName:gr.text()}, success: refresh});
    if (idx > -1) {
        groupList.names[idx] = undefined;
        groupList.contents[idx] = undefined;
    }
    gr.remove();



}

function refresh() {
    location.reload();
}

function checkEmptyGroup(){
    if($("#group_staff_cont").children().length == 0 && $(".groups").children().length > 1) {
        if(confirm("Текущая группа пуста и будет удалена. Вы хотите удалить группу?")) deleteGroup();
    }
}

function emptyGroups() {

    return ($(".groups").children().length == 0);

}

$(window).on("beforeunload", function () {
    if (!emptyGroups()) checkEmptyGroup();
    //saveGroup();
});




