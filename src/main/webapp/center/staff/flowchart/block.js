$(document).ready(function () {
    createTree();
});

function createTree() {
    myTree = new ECOTree('myTree','myTreeContainer');
    myTree.add(0,-1,"OOO Рога и копыта");
    myTree.add(1,0,"Left Child child Child");
    myTree.add(2,0,"Right Child");
    myTree.add(3,2,"Right Child");
    myTree.add(4,3,"Right Child");
    myTree.add(2,4,"Right Child");
    myTree.UpdateTree();

}