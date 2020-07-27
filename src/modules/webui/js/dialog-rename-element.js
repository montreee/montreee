$(".montreee-rename-element-button").click(function () {
    let currentPath = $(this).data("path");
    let currentName = currentPath.split("/").pop()
    $("#dialog-rename-element-current-path").text(currentName);
    $("#dialog-rename-element-future-name").val(currentName);
    $("#dialog-rename-element-rename-button").attr("data-parameter-path", currentPath);
    $("#dialog-rename-element-rename-button").attr("data-parameter-name", currentPath);
})

$("#dialog-rename-element-future-name").change(function () {
    $("#dialog-rename-element-rename-button").attr("data-parameter-name", $("#dialog-rename-element-future-name").val());
})
