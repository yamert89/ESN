<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Пендальф Синий
  Date: 10.11.2018
  Time: 19:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="<core:url value="/resources/static/auth/start.css"/>">
    <script src="<core:url value='/resources/libs/jquery_3.1.0.js'/>"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(":button").click(function () {
                $(this).addClass("clicked");
                var input = $(".department_adder");
                $(".l").append("<option>" + input.val() + '</option>');
                setTimeout(unselect, 200);
                input.val('');
            });
        });
        function unselect() {
            $(":button").removeClass("clicked");
        }
    </script>
</head>
<body>
<div class="reg_container reg_org_container">
    <sf:form method="post" modelAttribute="organization">
    <div class="reg_block">
        <label>
            Введите название организации:
            <sf:input path="name" type="text"/>
            <sf:errors path="name" cssClass="error"/>
            <img src="../../resources/checkbox.jpg">
        </label>
    </div>
    <div class="reg_block">
        <label>
            Добавьте описание:
            <sf:textarea path="description"></sf:textarea>
            <sf:errors path="description" cssClass="error"/>
            <img src="../../resources/checkbox.jpg">
        </label>
    </div>
    <div class="reg_block">
        <label>
            Добавьте отделы / структурные подразделения:
            <input name="list" list="list" class="department_adder"/>
            <button type="button">добавить</button>
            <datalist id="list" class="l"></datalist>
        </label>
    </div>
    <div class="reg_block">
        <input type="submit" class="auth_submit_button" value="Зарегистрировать">
    </div>

    </sf:form>

</div>

</body>
</html>
