function dialogRenameElement(currentPath) {
    $("#dialog-rename-element-current-path").text(currentPath);
    $("#dialog-rename-element-future-name").val(currentPath.split("/").pop());
    $("#dialog-rename-element-rename-button").attr("data-parameter-path", currentPath);
    $("#dialog-rename-element-rename-button").attr("data-parameter-name", currentPath);
}

$("#dialog-rename-element-future-name").change(function () {
    $("#dialog-rename-element-rename-button").attr("data-parameter-name", $("#dialog-rename-element-future-name").val());
})
