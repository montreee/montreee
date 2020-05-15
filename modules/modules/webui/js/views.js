let montreeeAsyncLoad = new coreui.AsyncLoad(document.getElementById('ui-view'), {
    defaultPage: "main",
    errorPage: "ui/404",
    subpagesDirectory: "ui/views/"
});

function montreeeCallTreeEditDeleteMethod(path, onReadyViewUrl) {
    let xhr = new XMLHttpRequest();
    xhr.open('DELETE', "api/tree/edit?path=" + path);
    xhr.onload = function (result) {
        loadView(onReadyViewUrl)
    };
    xhr.send();
}

async function loadView(url) {
    montreeeAsyncLoad._loadPage(url)
}
