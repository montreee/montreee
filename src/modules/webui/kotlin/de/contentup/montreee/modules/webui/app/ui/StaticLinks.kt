package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.module.runIfDevelopmentIsDisabled

object StaticLinks {
    object JS {
        object Lib {
            val JQuery = "ui/js/lib/jquery.js".useMin()
            val CoreUI = "ui/js/lib/coreui.js".useMin()
            val ChartJS = "ui/js/lib/chart.js".useMin()
            val CoreUIChartsJS = "ui/js/lib/coreui-chartjs.js".useMin()
            val Pace = "ui/js/lib/pace.js".useMin()
            val IfBreakpoint = "ui/js/lib/if-breakpoint.js".useMin()
        }

        val ChartJSResizeFix = "ui/js/chartjs-resize-fix.js".useMin()
        val Before = "ui/js/before.js".useMin()
        val Sidebar = "ui/js/sidebar.js".useMin()
        val Views = "ui/js/views.js".useMin()
        val ApiButtons = "ui/js/api-buttons.js".useMin()
        val DialogMoveElements = "ui/js/dialog-move-element.js".useMin()
        val DialogRenameElements = "ui/js/dialog-rename-element.js".useMin()
    }

    object CSS {
        val ChartJS = "ui/css/chart.css".useMin()
        val CoreUIChartsJS = "ui/css/coreui-chartjs.css".useMin()
        val Pace = "ui/css/pace.css".useMin()
        val Fontawesome = "ui/css/fontawesome.css".useMin()
        val Montreee = "ui/css/montreee.css".useMin()
        val Icons = "ui/css/icons.css".useMin()
    }

    object Assets {
        val MontreeeLogo = "ui/assets/brand/logo.svg"
    }
}

fun String.useMin() = runIfDevelopmentIsDisabled { replace(".", ".min.") }
