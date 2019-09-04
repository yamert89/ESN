CKEDITOR.dialog.add( 'filesDialog', function ( editor ) {
    return {
        title: "Добавить файл",
        minWidth: 400,
        height: 100,
        contents:[
            {
                id: "gen",
                label: "Настройки",
                elements: [
                    {
                        type: 'file',
                        id: 'load',
                        label: 'Загрузить',
                        validate: CKEDITOR.dialog.validate.notEmpty("Выберите файл")
                    }
                ]
            }

        ],
        onOk: function() {
            var dialog = this;

            var f = dialog.getContentElement('gen','load').getInputElement().$.files[0];



            var file = editor.document.createElement( 'div' );
            file.addClass('post_file');

            var ic = editor.document.createElement('img');
            var ico = getFileIco(f.name);
            ic.setAttribute('src', '/resources/icons/' + ico);
            ic.setAttribute('title', 'Скачать');
            ic.setAttribute('data-name', f.name);
            ic.addClass("post_file_ico");
            file.append(ic);

            var name = editor.document.createElement('span');
            name.setText(f.name);
            name.addClass('post_file_name');

            file.append(name);
            var data = new FormData();
            data.append( 'file', f);
            data.append('shared', "1");

            $.ajax({url:'/savefile', method:"POST", contentType:false, processData: false, data:data});

            editor.insertElement(file);
        }

    };
});