function resizePhoto(img, width, height) {

    var options = {
        file : img,
        maxWidth : width,
        maxHeight : height,
        keepExif : false,
        callback : function (result) {
            console.log(result);
            submitFormManually(result);
        }
    };
    resize(options);

    //return photo;

}