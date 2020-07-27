$(".montreee-api-button").click(function (event) {
    let data = {};
    let dataArray = [];
    [].forEach.call(this.attributes, function (attr) {
        if (attr.name.startsWith("data-")) {
            let name = attr.name.substr(5);
            data[name] = attr.value;
            dataArray.push({name: name, value: attr.value})
        }
    });
    let parameter = {};
    [].forEach.call(dataArray, function (item) {
        if (item.name.startsWith("parameter-")) {
            let name = item.name.substr(10);
            parameter[name] = item.value;
        }
    })
    let method = data["method"]
    let loadAfterViewUrl = data["load-after-view-url"]

    let url;
    if (Object.keys(parameter).length === 0) {
        url = data["url"]
    } else {
        url = data["url"] + "?" + Object.keys(parameter).map(function (key) {
            return key + "=" + parameter[key]
        }).join("&")
    }

    let xhr = new XMLHttpRequest();
    xhr.open(method, url);
    xhr.onload = function (result) {
        loadView(loadAfterViewUrl)
    };
    xhr.send();
})
