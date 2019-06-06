<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/staff/flowchart/block.css"/>">
    <script type="text/javascript">
        $(document).ready(function(){
            $("#staff").addClass("selected");
                $(".clear_flowchart").click(function () {
                    var resp = confirm("Вы уверены, что хотите удалить текущую структуру? " +
                        "Все отделы и связанная с ними информация будут удалены");
                    if (!resp) return;
                    $.ajax({url : "/departments", method : "delete"});
                    //TODO Протестировать / удалить один отдел
                    location.reload();
                });

                $(".save_flowchart").click(function () {

                    var object = document.getElementById("iframe1").contentWindow.exportData;
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
                        alert("Выберите отдел, который нужно отредактировать");
                        return;
                    }
                    window.EDIT_DEP_NAME = prompt("Изменить название отдела?", window.SELECTED_DEP.name);
                    if (EDIT_DEP_NAME == null) EDIT_DEP_NAME = SELECTED_DEP.name;
                    $(".staff_container").empty();
                    fillStaff(DATA[0].employers);
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

                })

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
            parent.children.push({id : id, parentId : parentId, name : name, employers:[], children : []});

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
                else res = findChild(depsArray[i].children, id);
            }
            return res;
        }

        function foreachEmpl(dep, nodeid){
            var el = findChild(dep, nodeid);
            $(".staff_container").empty();
            if (el.selected) {
                foreachDeselect(DATA_SUB);
                window.SELECTED_DEP = null;
                //el.selected = false;
                fillStaff(DATA[0].employers);
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
            window.DATA = data;
            window.DATA_SUB = [];
            for (var i = 1; i < DATA.length; i++) {
                DATA_SUB[i] = DATA[i];
            }
            //alert(data[0].employers);
            fillStaff(data[0].employers);

        };

        function fillStaff(staff) {
            var container = $(".staff_container");
            staff.forEach(function (el) {
                container.append('<table class="person_staff" data-id=' + el.id + '>\n' +
                    '            <tr>\n' +
                    '                <td width="100px">\n' +
                    '                    <img src="/resources/avatars/' + el.photo + '" class="person_photo_staff"></td>\n' +
                    '                <td valign="middle">\n' +
                    '                    <div class="person_point">\n' +
                    '                        <div class="person_name_staff">' + el.name + '</div>\n' +
                    '                        <div class="person_position">' + el.position + '</div>\n' +
                    '                    </div>\n' +
                    '                </td>\n' +
                    '            </tr>\n' +
                    '        </table>')
            });

            $(".person_staff").on("click", function () {
                $(this).toggleClass('person_selected');
            });

        }

        function comparator(el1, el2) {
            return el1.parentid === el2.parentid ? 0 :
                el1.parentid > el2.parentid ? 1 : -1;
        }

    </script>
</head>
<body>
<div class="tree_wrapper">

    <div class="flowchart_wrapper">
        <iframe src='<c:url value="/resources/static/center/staff/flowchart/flowchart.html"/>' class="flowchart_frame" id="iframe1"></iframe>
        <div class="features">
        <button class="save_flowchart" disabled>Сохранить структуру</button>
        <button class="clear_flowchart">Очистить структуру</button>
        <button class="staff_choose_btn">Редактировать отдел</button>
        <button class="staff_add_btn" disabled>Сохранить отдел</button>
        </div>
    </div>

    <div class="staff_container">

<%--
        <table class="person_staff">
            <tr>
                <td width="100px">
                    <img src="resources/avatars/wom.jpg" class="person_photo_staff"></td>
                <td valign="middle">
                    <div class="person_point">
                        <div class="person_name_staff">Иванов Иван Иванович</div>
                        <div class="person_position">Главный инженер</div>
                    </div>
                </td>
            </tr>
        </table>
--%>
    </div>
</div>
</body>
</html>
