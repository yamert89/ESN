<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 08.05.2019
  Time: 13:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Invalid Session</title>
</head>
<body>
Срок жизни сессии истек. Пожалуйста, авторизуйтесь заново.
<form action="/auth">
    <input type="submit">
</form>
</body>
</html>
