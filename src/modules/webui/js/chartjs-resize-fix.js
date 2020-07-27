function fixChartjsResize() {
    $('.chart').removeClass('chartjs-render-monitor');
    setTimeout(() => {
        $('.chart').addClass('chartjs-render-monitor');
    }, 100);
}

function applyChartjsResizeFix() {
    var resized = false;
    $(window).resize(function () {
        setTimeout(
            () => {
                if (!resized) {
                    fixChartjsResize();
                    resized = true;
                }
            }, 100);
    });
}

Chart.platform.disableCSSInjection = true;
applyChartjsResizeFix();
fixChartjsResize();
