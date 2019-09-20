function resizePhoto(img, width, height) {

    var options = {
        file : img,
        maxWidth : 20,
        maxHeight : 20,
        keepExif : false,
        callback : function (result) {
            console.log(result);
            window.photo = result;
        }
    };
    resize(options);

    //return photo;

}