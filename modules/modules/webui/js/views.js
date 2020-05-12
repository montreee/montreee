asyncLoad = new coreui.AsyncLoad(document.getElementById('ui-view'), {
    defaultPage: "main",
    errorPage: "status/404",
    subpagesDirectory: "views/"
});

function montreeeCallMethod(method, url, onReadyViewUrl) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', "views/" + url + "§" + method);
    xhr.onload = function (result) {
        asyncLoad._loadPage(onReadyViewUrl)
    };
    xhr.send();
}
