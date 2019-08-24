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
    storageSize();
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
                '                    <div class="file_author"><a href="">'  + shortName + '</a></div>\n' +
                '                    <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                '                </div>');
            $(this).attr("src", "/resources/data/app/unshare.png");
            $(this).attr("data-shared", "1");


        } else {
            $.ajax({url:"/savefile", method:"GET", contentType:false, data:{fname: fname, update: "unshare"}});
            fileContainer.find('[title="' + $(this).prev().prev().attr('title') + '"]').parent().remove();
            $(this).attr("src", "/resources/data/app/share.png");
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
        $.ajax({url:url, method:"POST", contentType:false, processData: false, data:data, success: function (data) {
                var not = data.success ? 'Файл загружен' : data.overflow ? 'Недостаточно свободного места' : 'Неихвестная ошибка';
                notify(not);
                if (data.overflow) return;
                input.get(0).value = '';
                var ico = getFileIco(file.name);
                var fileContainer;
                var ico_shared = "/resources/data/app/share.png";
                var sh = '0';
                if (shared === "1") {
                    fileContainer = $("#shared_files");
                    fileContainer.append('<div class="file">\n' +
                        '                <img src="../resources/icons/' + ico + '" class="file_ico" data-ext="' + extension +'"  title="Скачать">\n' +
                        '                <input class="fileName" readonly title="' + newFileName + '" value="' + newFileName +'" onchange="rename(this)">\n' +
                        '                <div class="file_author"><a href="/' + orgUrl + '/users/' + login + '">' + shortName + '</a></div>\n' +
                        '                <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                        '            </div>');
                    ico_shared = "/resources/data/app/unshare.png";
                    sh = '1';
                }
                fileContainer = $("#private_files");
                fileContainer.append('<div class="file">\n' +
                    '                    <img src="../resources/icons/' + ico + '" class="file_ico" data-ext="' + extension +'"  title="Скачать">\n' +
                    '                    <input class="fileName" type="text" title="' + newFileName + '" value="' + newFileName + '" onchange="rename(this)">\n' +
                    '                    <img src="/resources/data/app/cross.png" class="file_delete" title="Удалить">\n' +
                    '                    <img src=" ' + ico_shared + '" data-shared="' + sh + '" class="file_share" title="Опубликовать в общие">\n' +
                    '                    <div class="file_time">' + getDate(new Date()) + '</div>\n' +
                    '                </div>');

                storageSize();
            }});



    });
    filesPath = $(".storage").attr('data-path');

    $(document).on('click', '.file_ico', function () {
        var name = $(this).next().val() + '.' + $(this).attr('data-ext');
        var link = document.createElement('a');
        link.setAttribute('href',filesPath + name);
        link.setAttribute('download', name);
        link.click();
    })
});

function storageSize(){
    $.get("/storage_size", function (data) {
        var publ = $("#publicDiagram");
        publ.find(".donut-segment").attr("stroke-dasharray", data.public + ' ' + (100 - data.public));
        publ.find("text").text(data.public + '%');

        var priv = $("#privateDiagram");
        priv.find(".donut-segment").attr("stroke-dasharray", data.private + ' ' + (100 - data.private));
        priv.find("text").text(data.private + '%');
    }, "json");
}


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