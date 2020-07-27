let sidebar = $("#sidebar");
let sidebarToggler = $("#sidebar-toggler");

let lastResizeWasXS = xs;

function applyResizeToSidebar() {
    if (xs && !lastResizeWasXS) {
        if (sidebar.hasClass("montreee-sidebar-show")) sidebarToggler.click()
    }
    lastResizeWasXS = xs;
}

$(document).ready(function () {
    applyResizeToSidebar()
});
$(window).resize(function () {
    applyResizeToSidebar()
});
