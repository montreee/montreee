function dialogMoveElement(currentPath) {
    $("#dialog-move-element-current-path").text(currentPath);
    $("#dialog-move-element-future-path").val(currentPath);
    $("#dialog-move-element-move-button").attr("data-parameter-from", currentPath);
    $("#dialog-move-element-move-button").attr("data-parameter-to", currentPath);
}

$("#dialog-move-element-future-path").change(function () {
    $("#dialog-move-element-move-button").attr("data-parameter-to", $("#dialog-move-element-future-path").val());
})
