let montreeeAsyncLoad = new coreui.AsyncLoad(document.getElementById('ui-view'), {
    defaultPage: "main",
    errorPage: "status/404",
    subpagesDirectory: "views/"
});

function montreeeCallMethod(method, url, onReadyViewUrl) {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', "views/" + url + "~" + method);
    xhr.onload = function (result) {
        montreeeAsyncLoad._loadPage(onReadyViewUrl)
    };
    xhr.send();
}
