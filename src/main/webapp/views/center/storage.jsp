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
    <link rel="stylesheet" href="<c:url value="/resources/static/center/storage/storage.css"/>">
    <script type="text/javascript">

        $(document).ready(function () {
           /* $(".btn_load_file").on("change", function () {
                var form = $(".form_file");
                var filename = $(this).get(0).files[0].name;
            });*/

            $(".btn_load_file").click(function () {
                var form = $(".form_file");
                var url = form.attr('action');
                var input = $(".file_input");

                var data = new FormData();
                data.append( 'file', input.get(0).files[0]);
                data.append('shared', input.attr("data-shared"));


                $.ajax({url:url, method:"POST", contentType:false, processData: false, data:data});


            });
        });
        function onSubmit(f) {

        }
    </script>
</head>
<body>

<div class="storage">
    <div class="storage_public">

        <div class="storage_header">Общие файлы</div>
        <div class="storage_wrapper">
            <div class="file f_doc" data-id="">
                <img src="resources/icons/word.png" class="file_ico">
                <div class="fileName" title="Полный текст">Имя файла</div>
                <div class="file_author"><a href="">Петя Васечкин</a></div>
                <div class="file_time">18.01.2018  16.00</div>
            </div>
        </div>
        <form action="/savefile" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="1" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
    <div class="storage_private">

        <div class="storage_header">Личные файлы</div>
        <div class="storage_wrapper">
            <div class="file f_doc" data-id="">
                <img src="resources/icons/word.png" class="file_ico">
                <input class="fileName" type="text" title="Полный текст" value="Имя файла">
                <img src="resources/cross.png" class="file_delete" title="Удалить">
                <img src="resources/share.png" class="file_share" title="Опубликовать в общие">
                <div class="file_time">18.01.2018  16.00</div>
            </div>

        </div>
        <form action="/savefile" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="1" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
</div>
</body>
</html>
