var exportData = [];
function ExportDep(name, id, parentId){
    this.name = name;
    this.id = id;
    this.parentId = parentId;
}
$(document).ready(function () {
    createTree();



    $("img").not(".node_plus").click(function () {
        nodePlusOnClick();
    });



});
function clickNode(tree, targetid, nodeid) {
    if (targetid == '') targetid = nodeid;
    //alert('tree ' + tree + ' targetid: .' + targetid + '. nodeid: ' + nodeid);
    loadSt(nodeid);
    ECOTree._canvasNodeClickHandler(tree, targetid, nodeid);
    if (!window.ADMIN) $(".node_tools").remove();
}



function createTree() {
    myTree = new ECOTree('myTree','myTreeContainer');
    createSomeNode(0, -1, orgName);
    console.log("createtree");

    $.ajax({
        url:"/getstaff",
        type:"get",
        contentType:"json",
        success: createData,
        error:exc,
        timeout:30000
    });
}

function createData(data) {

    if (data[0].name !== 'default') {
        return;
    }

    if (!data[1]) {
        $(".node_tools").remove();
        window.ADMIN = false;
    } else window.ADMIN = true;

    if (data[0].children.length === 0)  {
        $('.staff_choose_btn').attr('disabled', '');
        if (window.ADMIN) notify("Вачале добавьте подразделения. Затем сохраните структуру и распределите сотрудников", 5500);
    }

    window.ALL_EMPLOYERS = data[0].employers;

    setStructData(data[0].children);
    for (var i = 0; i < data[0].children.length; i++) {
        addChildFromData(data[0].children[i]);
    }



}

function addChildFromData(dep) {
    var id = dep.id;
    var name = dep.name;
    var parentID = dep.parentId;
    createSomeNode(id, parentID, name);
    dep.children.forEach(function (el) {
        addChildFromData(el);
    });
}

function exc(e) {
    notify('Ошибка на сервере');
}


function addChild(parent) {
    var name = prompt("Введите название дочернего подразделения", "");
    while (name.length === 0) {
        notify("Назавание не может быть пустым");
        name = prompt("Введите название подраздела", "");
    }
    var nodeId = Date.now();
    createSomeNode(nodeId, parent, name);
    //exportData = exportData.concat('{"name": \"' + name + '\", "id" : ' + nodeId +', "parentId" :' + parent + '},')
    exportData.push(new ExportDep(name, nodeId, parent));
    enableSaveStruct();
    addChildToData(nodeId, name, parent);

}

function createSomeNode(id, parent, name) {
    var l = (name.length * 14) + 9;
    if (l < 68) l = 68;
    myTree.add(id, parent, name, l);
    myTree.UpdateTree();
    nodePlusOnClick();

}

function nodePlusOnClick() {
    var node_pluse = $(".node_plus");
    node_pluse.on("click", function () {
        addChild($(this).parent().parent().attr('id'));
    });
    node_pluse.attr("src", "/resources/data/app/plus.png");
}