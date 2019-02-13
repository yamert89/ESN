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
                $(".clear_flowchart").click(function () {
                    var resp = confirm("Вы уверены, что хотите удалить текущую структуру и построить её заново?");
                    if (!resp) return;
                    //TODO Удалить текущую структуру и обновить страницу с родительским нодом
                    createTree();
                });




                $(".save_flowchart").click(function () {
                    //TODO Уведомить пользователя
                    var d = document.getElementById("iframe1").contentWindow.exportData;
                    var data = d.substr(0, d.length - 1) + "]";
                    var object = JSON.parse(data);
                    object.sort(comparator);
                    var outObj = [];
                    var el;
                    for (var i = 0; i < object.length;) {
                        el = object[i];
                        if (el.parentId === 0) {
                            outObj.push(object[i]);
                            object.splice(i, 1);
                        } else i++;
                    }
                    outObj.forEach(function (value) {
                        addChildren(value);
                    });
                    $.ajax({url:"/" + "rosles" +"/savestructure", type:"post", processData: false, contentType: false, data: JSON.stringify(outObj)});

                    //alert(JSON.stringify(outObj));

                   /* outObj.forEach(function (value) {
                        value.children = [];
                        object.forEach(function (ob) {
                            if (ob.parentid === value.id) value.children.push(ob);
                        })
                    })*/

                    function addChildren(obj) {
                        obj.children = [];
                        object.forEach(function (ob) {
                            if (ob.parentId == obj.id) {
                                var idx = obj.children.push(ob) - 1;
                                addChildren(obj.children[idx]);
                            }
                        })
                    }



                });

                $(".staff_choose_btn").click(function () {
                    if (window.SELECTED_DEP == undefined || SELECTED_DEP == null) {
                        alert("Выберите отдел, который нужно отредактировать");
                        return;
                    }
                    window.EDIT_DEP_NAME = prompt("Изменить название отдела?", SELECTED_DEP.name);
                    $(".staff_container").empty();
                    fillStaff(DATA[0].employers);
                    alert("выберите людей");
                    //TODO log - выберите людей
                    window.EDIT_MODE = true;


                });

                $(".staff_add_btn").click(function () {
                    if (window.EDIT_MODE != true) {
                        alert("Нечего сохранять");//TODO replace log
                        return;
                    }
                    var employers = $(".staff_container").find($(".person_selected"));
                    var ids = [];
                    employers.each(function (i, el) {
                        ids[i] = el.getAttribute("data-id");
                    });


                    $.ajax({method:"post", url:"/" + "rosles" +"/savedep",
                        data:{newname:EDIT_DEP_NAME, oldname:SELECTED_DEP.name, ids: JSON.stringify(ids)}, success: aftersave, dataType: "text"})
                    ;





                });


        });

        function aftersave(data){
            location.reload();
            alert("Сохранено")//TODO replace log
            $("#" + data).click();
        }






        window.loadSt = function (nodeid) {
          foreachEmpl(DATA, nodeid);
        };

        function foreachEmpl(dep, nodeid){
            dep.forEach(function (el) {
                if (el.id == nodeid) {
                    /*if (el.selected === undefined) {
                        el.selected = false;
                        $('#' + nodeid).addClass("node_selected");
                    }*/
                    $(".staff_container").empty();
                    if (el.selected) {
                        foreachDeselect(DATA);   //TODO не проверять первый элемент
                        window.SELECTED_DEP = null;
                        //el.selected = false;
                        fillStaff(DATA[0].employers);
                        //return;
                    } else {
                        foreachDeselect(DATA);   //TODO не проверять первый элемент
                        window.SELECTED_DEP = el;
                        el.selected = true;


                        fillStaff(el.employers);
                    }
                }
                foreachEmpl(el.children, nodeid);
            })
        }

        function foreachDeselect(dep){
            dep.forEach(function (el) {
                el.selected = false;
                foreachDeselect(el.children);
            })
        }

        window.setStructData = function (data) {
            window.DATA = data;
            //alert(data[0].employers);
            fillStaff(data[0].employers);

        };

        function fillStaff(staff) {
            var container = $(".staff_container");
            staff.forEach(function (el) {
                container.append('<table class="person_staff" data-id=' + el.id + '>\n' +
                    '            <tr>\n' +
                    '                <td width="100px">\n' +
                    '                    <img src="' + el.photo + '" class="person_photo_staff"></td>\n' +
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
        <button class="save_flowchart">Сохранить структуру</button>
        <button class="clear_flowchart">Очистить структуру</button>
        <button class="staff_choose_btn">Редактировать отдел</button>
        <button class="staff_add_btn">Сохранить отдел</button>
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
