<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" href="<core:url value="/resources/static/center/staff/flowchart/block.css"/>">
    <script type="text/javascript">
        $(document).ready(
            $(".clear_flowchart").click(function () {
                var resp = confirm("Вы уверены, что хотите удалить текущую структуру и построить её заново?");
                if (!resp) return;
                //TODO Удалить текущую структуру и обновить страницу с родительским нодом
                createTree();
            })
        );

        function loadStaff() {

            //TODO server side
        }

    </script>
</head>
<body>
<div class="tree_wrapper">

    <div class="flowchart_wrapper">
        <iframe src="flowchart.html" class="flowchart_frame"></iframe>
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
