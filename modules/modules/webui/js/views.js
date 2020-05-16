let montreeeAsyncLoad = new coreui.AsyncLoad(document.getElementById('ui-view'), {
    defaultPage: "main",
    errorPage: "ui/404",
    subpagesDirectory: "ui/views/"
});

function loadView(url) {
    montreeeAsyncLoad._loadPage(url)
}
