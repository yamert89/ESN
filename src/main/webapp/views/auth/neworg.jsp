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
    <script src="<core:url value='/resources/libs/jquery_3.1.0.js'/>"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("button").click(function () {
                $(this).addClass("clicked");
                var input = $(".department_adder");
                $("#ps").append("<option value='" + input.val() + "'/>");
                setTimeout(unselect, 200);
                input.val('');
            });
        });
        function unselect() {
            $("button").removeClass("clicked");
        }
        $(document).on('submit', function () {
            console.log('submit');

            var res = '';
            try{
                $("datalist").children().each(function (idx, el) {
                    res = res + '@@@' + el.getAttribute('value');
                });
                $("#input").val(res.substr(3));
            }catch (e) {
                console.log(e);
            }


        })
    </script>
</head>
<body>
<div class="properties_board reg_org_container">
    <springform:form method="post" modelAttribute="org" id="org">
    <div class="prop_line">
        <div class="inline">
            <label class="prop_label">Введите название организации:</label>
            <springform:input path="name" type="text" class="reg_el"/>
        </div>


        <%--<img src="../../resources/checkbox.jpg">--%>

    </div>
    <div class="prop_line">
        <div class="inline">
            <label class="prop_label">Добавьте описание:</label>
            <springform:textarea path="description" class="reg_el"></springform:textarea>
        </div>


        <%--<img src="../../resources/checkbox.jpg">--%>
    </div>
    <div class="prop_line">
        <div class="inline">
            <label class="prop_label">Введите URL на латинице:</label>
            <springform:input path="urlName"  type="text" class="reg_el" title="Относительный путь, который будет отображаться в строке браузера.
         Должен состоять только из символов латинского алфавита и цифр, исключая другие символы"></springform:input>
        </div>


            <%--<img src="../../resources/checkbox.jpg">--%>
    </div>
    <div class="prop_line">
        <div class="inline">
            <label class="prop_label">Добавьте перечень должностей:</label>
            <input list="ps" class="department_adder" name="pos" id="input" form="org"/>
            <datalist id="ps">
            </datalist>
            <button type="button" class="reg_el">добавить</button>
        </div>



        <%--<springform:select path="positions" cssClass="department_adder" size="1" class="reg_el">
            <springform:option value="dep" class="reg_el">Без названия</springform:option>
        </springform:select>--%>

    </div>
        <springform:errors path="name" cssClass="jspError"/>
        <springform:errors path="description" cssClass="jspError"/>
        <springform:errors path="urlName" cssClass="jspError"/>
       <%-- <springform:errors path="positions" cssClass="jspError"/>--%>
    <div class="prop_line">
        <input type="submit" value="Зарегистрировать">
    </div>
    </springform:form>
</div>

</body>
</html>
