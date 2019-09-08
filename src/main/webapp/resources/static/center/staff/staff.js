$(document).ready(function(){
    $("#staff").addClass("selected");
    $(".clear_flowchart").click(function () {
        var resp = confirm("Вы уверены, что хотите удалить текущую структуру? " +
            "Все отделы и связанная с ними информация будут удалены");
        if (!resp) return;
        $.ajax({url : "/departments", method : "delete"});
        //TODO удалить один отдел
        location.reload();
    });

    $(".save_flowchart").click(function () {

        var object = exportData;
        object.sort(comparator);
        var outObj = [];

        for (var i = 0; i < object.length;) {
            addChildren(object[i]);
            outObj.push(object[i]);
            object.splice(i, 1);
        }

        $.ajax({url:"/" + orgUrl +"/savestructure", type:"post", processData: false,
            contentType: false, data: JSON.stringify(outObj), success: aftersave});

        function addChildren(obj) {
            obj.children = [];

            for (var i = 0; i < object.length;) {
                if (object[i].parentId == obj.id) {
                    var idx = obj.children.push(object[i]) - 1;
                    addChildren(obj.children[idx]);
                    object.splice(i, 1);
                } else i++;
            }

        }

    });

    $(".staff_choose_btn").click(function () {
        if (window.SELECTED_DEP == undefined || window.SELECTED_DEP == null) {
            notify("Выберите отдел, который нужно отредактировать");
            return;
        }
        window.EDIT_DEP_NAME = prompt("Изменить название отдела?", window.SELECTED_DEP.name);
        if (EDIT_DEP_NAME == null) EDIT_DEP_NAME = SELECTED_DEP.name;
        $(".staff_container").empty();
        fillStaff(window.ALL_EMPLOYERS);
        notify("Выберите людей");
        window.EDIT_MODE = true;
        $(".staff_add_btn").removeAttr("disabled");


    });

    $(".staff_add_btn").click(function () {
        if (window.EDIT_MODE != true) {
            notify("Нечего сохранять");
            return;
        }
        var employers = $(".staff_container").find($(".person_selected"));
        var ids = [];
        employers.each(function (i, el) {
            ids[i] = el.getAttribute("data-id");
        });


        $.ajax({method:"post", url:"/department",
            data:{newname:EDIT_DEP_NAME, oldname:SELECTED_DEP.name, ids: JSON.stringify(ids)}, success: aftersave, dataType: "text"})
        ;

    });

    $("span").click(function (e) {
        e.preventDefault();

    });





});

function aftersave(data){
    location.reload();
    notify('Сохранено');
    $("#" + data).click();
}

window.enableSaveStruct = function(){
    $(".save_flowchart").removeAttr("disabled");
    //[[{"id":0,"name":"default","employers":[{"id":208,"name":"FuckedUnit","position":"","userInformation":null,"photo":"/wom.jpg"},{"}],"parentId":"0","children":[]}],true]
};

window.addChildToData = function(id, name, parentId){
    var parent = findChild(window.DATA, parentId);
    parent.children.push({id : id, parentId : parentId, name : name, employers:[], children : [], new : true});

};


window.loadSt = function (nodeid) {
    foreachEmpl(DATA, nodeid);
};

function findChild(depsArray, id) {
    var res = {};
    for (var i = 0; i < depsArray.length; i++) {
        if (depsArray[i].id == id) {
            res = depsArray[i];
            break;
        }
        else {
            res = findChild(depsArray[i].children, id);
            if (res.name != undefined) break;
        }
    }
    return res;
}

function foreachEmpl(dep, nodeid){
    var el = findChild(dep, nodeid);
    $(".staff_container").empty();
    if (el.new) $(".staff_choose_btn").attr("disabled", "true");
    else $(".staff_choose_btn").removeAttr("disabled");
    if (el.selected || el.id === undefined) {
        foreachDeselect(DATA_SUB);
        window.SELECTED_DEP = null;
        //el.selected = false;
        fillStaff(window.ALL_EMPLOYERS);
        //return;
    } else {
        foreachDeselect(DATA_SUB);
        window.SELECTED_DEP = el;
        el.selected = true;


        fillStaff(el.employers);
    }
}

function foreachDeselect(dep){
    dep.forEach(function (el) {
        el.selected = false;
        foreachDeselect(el.children);
    })
}

window.setStructData = function (data) {
    if (data.length > 0){
        window.DATA = data;
        window.DATA_SUB = [];
        for (var i = 0; i < DATA.length; i++) {
            DATA_SUB[i] = DATA[i];
        }
    }

    //alert(data[0].employers);
    fillStaff(window.ALL_EMPLOYERS);

};

function fillStaff(staff) {
    if (staff.length < 1) return;
    var container = $(".staff_container");
    staff.forEach(function (el) {
        container.append('<div class="person_staff" data-id=' + el.id + '>\n' +
            '                    <img src="/resources/data' + el.photo + '" class="person_photo_staff">' +
            '                    <div class="person_point">\n' +
            '                        <div class="person_name_staff" title="' + el.shortName + '">' + el.shortName + '</div>\n' +
            '                        <div class="person_position">' + el.position + '</div>\n' +
            '                    </div>\n' +
            '        </div>')
    });

    $(".person_staff").on("click", function () {
        $(this).toggleClass('person_selected');
    });

}

function comparator(el1, el2) {
    return el1.parentid === el2.parentid ? 0 :
        el1.parentid > el2.parentid ? 1 : -1;
}