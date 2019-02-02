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
        });

        window.loadSt = function () {
          $.ajax({url:""})
        };





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
    </div>

    <div class="staff_container">

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

    </div>
</div>
</body>
</html>
