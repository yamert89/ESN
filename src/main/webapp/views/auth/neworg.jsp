<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>
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
                $("#ps").append("<option value='" + input.value() + "'/>");
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
    <springform:form method="post" modelAttribute="organization">
    <div class="reg_block_org">
        <label class="reg_el">Введите название организации:</label>
        <springform:input path="name" type="text" class="reg_el"/>
        <springform:errors path="name" cssClass="error"/>
        <%--<img src="../../resources/checkbox.jpg">--%>

    </div>
    <div class="reg_block_org">
        <label class="reg_el">Добавьте описание:</label>
        <springform:textarea path="description" class="reg_el"></springform:textarea>
        <springform:errors path="description" cssClass="error"/>
        <%--<img src="../../resources/checkbox.jpg">--%>
    </div>
    <div class="reg_block_org">
        <label class="reg_el">Добавьте перечень должностей:</label>
        <springform:input path="positions" list="ps"/>
            <datalist id="ps">
                <option value="demo1"/>
            </datalist>

        <%--<springform:select path="positions" cssClass="department_adder" size="1" class="reg_el">
            <springform:option value="dep" class="reg_el">Без названия</springform:option>
        </springform:select>--%>
        <button type="button" class="reg_el">добавить</button>
    </div>
    <div class="reg_block_org">
        <input type="submit" value="Зарегистрировать">
    </div>
    </springform:form>
</div>

</body>
</html>
