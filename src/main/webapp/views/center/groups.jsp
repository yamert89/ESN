<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<core:url value="/resources/static/center/groups/groups.css"/>">
    <script src="<core:url value="/resources/static/center/groups/groups.js"/>"></script>
</head>
<body>
<div class="groups_container">
    <div class="staff_container" id="all_staff_cont">
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

        <table class="person_staff">
            <tr>
                <td width="100px">
                    <img src="resources/avatars/wom.jpg" class="person_photo_staff"></td>
                <td valign="middle">
                    <div class="person_point">
                        <div class="person_name_staff">Буланова Татьяна георгиевна</div>
                        <div class="person_position">Главный инженер</div>
                    </div>
                </td>
            </tr>
        </table>

        <table class="person_staff">
            <tr>
                <td width="100px">
                    <img src="resources/avatars/wom.jpg" class="person_photo_staff"></td>
                <td valign="middle">
                    <div class="person_point">
                        <div class="person_name_staff">Шугаркин Федор Вячеславович</div>
                        <div class="person_position">Главный инженер</div>
                    </div>
                </td>
            </tr>
        </table>

        <table class="person_staff">
            <tr>
                <td width="100px">
                    <img src="resources/avatars/wom.jpg" class="person_photo_staff"></td>
                <td valign="middle">
                    <div class="person_point">
                        <div class="person_name_staff">Тарасова Ольга Васильевна</div>
                        <div class="person_position">Главный инженер</div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="central_wrapper">
        <img src="resources/next.png" class="arrow" id="right_arrow">
        <img src="resources/next.png" class="arrow" id="left_arrow">

        <div class="groups">
            <div class="group">Группа 1</div>
            <div class="group">Группа 2</div>
        </div>
        <button class="group_add">Добавить группу</button>

    </div>
    <div class="staff_container" id="group_staff_cont">





    </div>
</div>
</body>
</html>
