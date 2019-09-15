var files = [];
loadFiles();
CKEDITOR.dialog.add( 'filesDialog', function ( editor ) {
    return {
        title: "Добавить файл",
        minWidth: 400,
        minHeight: 100,
        contents:[
            {
                id: "gen",
                label: "Загрузить",
                elements: [
                    {
                        type: 'file',
                        id: 'load',
                        label: 'Загрузить'
                    },
                    {
                        type: 'button',
                        id: 'btn',
                        label: 'Выбрать из загруженных',
                        onClick : function () {
                            showFiles(this.getDialog());
                        }
                    }
                ]
            },
            {
                id: "storage",
                label: "Хранилище",
                elements:[

                ]
            }

        ],
        onShow: function(){
            this.hidePage('storage');
        },
        onOk: function() {
            var dialog = this;
            var file = editor.document.createElement('div');
            file.addClass('post_file');

            var ic = editor.document.createElement('img');
            var ico;
            var fileName;


            if (dialog.definition.dialog._.currentTabId == 'gen') {

                var f = dialog.getContentElement('gen', 'load').getInputElement().$.files[0];

                fileName = f.name;
                var data = new FormData();
                data.append('file', f);
                data.append('shared', "1");
                $.ajax({url: '/savefile', method: "POST", contentType: false, processData: false, data: data});


            }else {
                var select = dialog.getContentElement('storage', '2');
                //var fileId = select.getValue();
                console.log('VALUE   ' + select.getValue());
                fileName = select.getValue();
                var fn = select.getValue().substr(0, select.getValue().indexOf('.'));
                $.ajax({url : '/savefile', data : {fname: fn, update : 'share'}});

            }
            if (fileName == undefined || fileName == null || fileName == '') return;

            ico = getFileIco(fileName);

            ic.setAttribute('src', '/resources/icons/' + ico);
            ic.setAttribute('title', 'Скачать');
            ic.setAttribute('data-name', fileName);
            ic.setAttribute('data-owner', window.login);
            ic.addClass("post_file_ico");
            file.append(ic);

            var name = editor.document.createElement('span');
            name.setText(fileName);
            name.addClass('post_file_name');

            file.append(name);
            editor.insertElement(file);


        }

    };
});
CKEDITOR.on("dialogDefinition", function (ev) {
    var dialogDefinition = ev.data.definition;
    var tab = dialogDefinition.getContents( 'storage' );
    tab.add(
        {
            type: 'select',
            id:'2',
            label: 'Выберите файл',
            items : getFiles(),
            onChange : function () {

            }
        })
});

function loadFiles() {
    $.ajax({url : '/storage', async : false, success : function (data) {
        files = data;
        console.log('files getting done')
    }})
}

function getFiles() {
    console.log('init select');
    var items = [];
    if (files.length == 0) return [['пусто']];
    files.forEach(function (el, idx) {
        items.push([el.name, el.name + '.' + el.extension]);
    }) ;
    return items;
}

function showFiles(dialog) {
    dialog.showPage('storage');
    dialog.selectPage('storage');
    dialog.hidePage('gen');





}