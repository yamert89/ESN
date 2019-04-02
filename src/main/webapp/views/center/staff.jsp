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

            $("span").click(function (e) {
                e.preventDefault();

            })

        });

        window.loadSt = function (nodeid) {
            foreachEmpl(DATA, nodeid);
        };

        function foreachEmpl(dep, nodeid){
            dep.forEach(function (el) {
                if (el.id == nodeid) {

                    $(".staff_container").empty();
                    if (el.selected) {
                        foreachDeselect(DATA_SUB);
                        window.SELECTED_DEP = null;
                        fillStaff(DATA[0].employers);
                    } else {
                        foreachDeselect(DATA_SUB);
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
            window.DATA_SUB = [];
            for (var i = 1; i < DATA.length; i++) {
                DATA_SUB[i] = DATA[i];
            }
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

        }

        function comparator(el1, el2) {
            return el1.parentid === el2.parentid ? 0 :
                el1.parentid > el2.parentid ? 1 : -1;
        }

        //TODO отступ слева и размер поля конфиг пользователя

    </script>
</head>
<body>
<div class="tree_wrapper">
    <div class="flowchart_wrapper">
        <iframe src='<c:url value="/resources/static/center/staff/flowchart/flowchart.html"/>' class="flowchart_frame" id="iframe1"></iframe>
    </div>
    <div class="staff_container"></div>
</div>
</body>
</html>
