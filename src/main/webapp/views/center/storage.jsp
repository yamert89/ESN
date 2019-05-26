<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
        var filesPath;
        $("#storage").addClass("selected");
        var extensions = {'.xls' : 'excel.png', '.xlsx' : 'excel.png',
            '.doc' : 'word.png', '.docx' : 'word.png',
            '.mp3' : 'music.png', '.wave' : 'music.png', '.wma' : 'music.png', '.mid' : 'music.png', '.ac3' : 'music.png', '.aac' : 'music.png', '.ogg' : 'music.png', '.flac' : 'music.png',
            '.avi' : 'movie.png', '3gp' : 'movie.png','.mkv' : 'movie.png','.mov' : 'movie.png','.mp4' : 'movie.png', '.flv' : 'movie.png', 'mpeg' : 'movie.png', 'mpg' : 'movie.png', 'swf' : 'movie.png',
            '.ico' : 'image.png', '.png' : 'image.png', '.jpg' : 'image.png', '.jpeg' : 'image.png', '.bmp' : 'image.png', '.tga' : 'image.png', '.tif' : 'image.png', '.tiff' : 'image.png', '.djvu' : 'image.png',
            '.log' : 'text.png', '.txt' : 'text.png', '.text' : 'text.png', '.err' : 'text.png',
            '.zip' : 'compressed.png', '.rar' : 'compressed.png', '.7z' : 'compressed.png', '.cab' : 'compressed.png', '.tar' : 'compressed.png', '.tgz' : 'compressed.png', '.tar-gz' : 'compressed.png', '.zipx' : 'compressed.png', '.pak' : 'compressed.png',
            '.pdf' : 'powerpoint.png'
        };

        var unknownExt = "fileicon_bg.png";

        function getFileIco(name){
            name = name.toLowerCase();
            var regexp = "\\..{2,6}$";
            var ext = name.match(regexp);
            if (!extensions[ext]) return unknownExt;
            return extensions[ext];
        }

        $(document).ready(function () {
           /* $(".btn_load_file").on("change", function () {
                var form = $(".form_file");
                var filename = $(this).get(0).files[0].name;
            });*/
            $(".fileName").keyup(function(event){
                if(event.keyCode === 13){
                    event.preventDefault();
                    $(this).blur();

                }
            });

            $(".file_ico").each(function () {
               $(this).attr("src", "../resources/icons/" + getFileIco("." + $(this).attr("data-ext")));
            });

            $(".file_delete").on("click", function () {
                var fname = $(this).prev().attr("title");
                $.ajax({url:"/savefile", method:"GET", contentType:false, data:{fname: fname, update: "delete"}});
                $('[title="' + fname + '"]').parent().remove();
            });

            $("body").on("click", ".file_share", function () {
                var fileContainer = $("#shared_files");

                var fname = $(this).prev().prev().attr("title");
                if ($(this).attr("data-shared") === "0"){
                    $.ajax({url:"/savefile", method:"GET", contentType:false, data:{fname: fname, update: "share"}});


                    var ico = getFileIco("." + $(this).prev().prev().prev().attr("data-ext"));
                    fileContainer.append('<div class="file">\n' +
                        '                    <img src="../resources/icons/' + ico + '" class="file_ico">\n' +
                        '                    <input class="fileName" readonly title="' + fname + '" value="' + fname +'">\n' +
                        '                    <div class="file_author"><a href="">'  + userName + '</a></div>\n' +
                        '                    <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                        '                </div>');
                    $(this).attr("src", "/resources/unshare.png");
                    $(this).attr("data-shared", "1");


                } else {
                    $.ajax({url:"/savefile", method:"GET", contentType:false, data:{fname: fname, update: "unshare"}});
                    fileContainer.find('[title="' + $(this).prev().prev().attr('title') + '"]').parent().remove();
                    $(this).attr("src", "/resources/share.png");
                    $(this).attr("data-shared", "0");
                }

            });

            $(".btn_load_file").click(function () {
                var url = '/savefile';
                var input = $(this).prev();

                var data = new FormData();
                var file = input.get(0).files[0];
                var idx = file.name.lastIndexOf('.');
                var newFileName = file.name.substring(0, idx);
                var extension = file.name.substring(++idx, file.name.length);
                var shared = input.attr("data-shared");
                data.append( 'file', file);
                data.append('shared', shared);
                $.ajax({url:url, method:"POST", contentType:false, processData: false, data:data, success: function () {
                        notify('Файл загружен');
                    }});

                input.get(0).value = '';
                var ico = getFileIco(file.name);
                var fileContainer;
                if (shared === "1") {
                    fileContainer = $("#shared_files");
                    fileContainer.append('<div class="file">\n' +
                        '                <img src="../resources/icons/' + ico + '" class="file_ico" data-ext="' + extension +'"  title="Скачать">\n' +
                        '                <input class="fileName" readonly title="' + newFileName + '" value="' + newFileName +'">\n' +
                        '                <div class="file_author"><a href="/"' + orgUrl + '"/users/"' + login + '>' + userName + '</a></div>\n' +
                        '                <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                        '            </div>');
                }
                fileContainer = $("#private_files");
                fileContainer.append('<div class="file">\n' +
                    '                    <img src="../resources/icons/' + ico + '" class="file_ico" data-ext="' + extension +'"  title="Скачать">\n' +
                    '                    <input class="fileName" type="text" title="' + newFileName + '" value="' + newFileName + '">\n' +
                    '                    <img src="/resources/cross.png" class="file_delete" title="Удалить">\n' +
                    '                    <img src="/resources/share.png" data-shared=\'0\' class="file_share" title="Опубликовать в общие">\n' +
                    '                    <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                    '                </div>');

            });
            filesPath = $(".storage").attr('data-path');
            $(document).on('click', '.file_ico', function () {
                var name = $(this).next().val() + '.' + $(this).attr('data-ext');
                var link = document.createElement('a');
                link.setAttribute('href',filesPath + name);
                link.setAttribute('download','download');
                link.click();
            })
        });


        function rename(el) {
            var oldName = el.getAttribute("title");
            $.ajax({url:"/savefile", method:"GET", contentType:false, data:{fname: oldName, update: "rename", newName: el.value}, success: function () {
                    notify('Файл переименован')
                }, error: function () {
                    notify('Ошибка переименования')
                }});
            el.setAttribute("title", el.value);
            el.setAttribute("value", el.value);

            var sharedEl =  $('[title="' + oldName  +'"');
           sharedEl.attr("title", el.value);
           sharedEl.attr("value", el.value);

        }
    </script>
</head>
<body>

<div class="storage" data-path="${filesPath}">
    <div class="storage_public">

        <div class="storage_header">Общие файлы</div>
        <div class="storage_wrapper" id="shared_files">
            <c:forEach var="file" items='${sharedFiles}'>
                <div class="file">
                    <img src="" class="file_ico" data-ext="${file.extension}" title="Скачать">
                    <input class="fileName" readonly title="${file.name}" value="${file.name}">
                    <div class="file_author"><a href='/${sessionScope.get("orgUrl")}/users/${file.owner.login}'>${file.owner.name}</a></div>
                    <div class="file_time">${file.time}</div>
                </div>
            </c:forEach>


        </div>
        <form action="" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="1" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
    <div class="storage_private">

        <div class="storage_header">Личные файлы</div>
        <div class="storage_wrapper" id="private_files">
            <c:set var="files" value='${user.storedFiles}'/>
            <c:forEach var="file" items='${files}'>
                <div class="file">
                    <img src="" class="file_ico" data-ext="${file.extension}"  title="Скачать">
                    <input class="fileName" type="text" title="${file.name}" value="${file.name}" onchange="rename(this)">
                    <img src='<c:url value="/resources/cross.png"/>' class="file_delete" title="Удалить">
                    <c:if test="${file.shared == false}">
                        <img src='<c:url value="/resources/share.png"/>' data-shared='0' class="file_share" title="Опубликовать в общие">
                    </c:if>
                    <c:if test="${file.shared == true}">
                        <img src='<c:url value="/resources/unshare.png"/>' data-shared='1' class="file_share" title="Опубликовать в общие">
                    </c:if>
                    <div class="file_time">${file.time}</div>
                </div>
            </c:forEach>

        </div>
        <form action="" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="0" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
</div>
</body>
</html>


