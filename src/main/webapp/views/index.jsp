<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<header>
    <div class="title" align="center">Name</div>
    <div class="user">
        <span class="user_name">Иванов Иван Иванович</span>
        <img src="" class="user_photo">
    </div>
</header>
<body>
<div class="container">
    <div class="tools">
        <div class="tool">Лента</div>
        <div class="tool">Чат</div>
        <div class="tool">Сотрудники</div>
        <div class="tool">Группы</div>
        <div class="tool">Хранилище</div>
        <div class="tool">Заметки</div>
        <div class="tool">Календарь</div>
        <div class="tool">Приложения</div>
        <div class="datepicker-here"></div>

    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <iframe src="resources/static/contacts/contacts.html" frameborder="0"></iframe>
    </div>

</div>
</body>
</html>
