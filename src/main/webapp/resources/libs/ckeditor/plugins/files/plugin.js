CKEDITOR.plugins.add( 'files', {
    icons: 'files',
    init: function( editor ) {
        editor.addCommand( 'files', new CKEDITOR.dialogCommand( 'filesDialog' ) );
        editor.ui.addButton( 'Files', {
            label: 'Добавить файл',
            command: 'files',
            toolbar: 'insert'
        });
        CKEDITOR.dialog.add( 'filesDialog', this.path + 'dialogs/files.js' );
    }
});

