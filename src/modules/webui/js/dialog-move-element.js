$(".montreee-move-element-button").click(function () {
    let fullCurrentPath = $(this).attr("data-path");
    let currentPath = fullCurrentPath.substring(0, fullCurrentPath.lastIndexOf('/'))
    let currentName = fullCurrentPath.split("/").pop()
    $("#dialog-move-element-current-path").text(currentPath);
    $("#dialog-move-element-future-path").val(currentPath);
    $("#dialog-move-element-future-path").attr("data-name", currentName);
    $("#dialog-move-element-move-button").attr("data-parameter-from", fullCurrentPath);
    $("#dialog-move-element-move-button").attr("data-parameter-to", fullCurrentPath);
})

$("#dialog-move-element-future-path").change(function () {
    let path = $("#dialog-move-element-future-path").val()
    let to
    if (!path.replace(/\s/g, '').length) {
        to = $("#dialog-move-element-future-path").attr("data-name");
    } else {
        to = path + "/" + $("#dialog-move-element-future-path").attr("data-name");
    }
    $("#dialog-move-element-move-button").attr("data-parameter-to", to);
})
