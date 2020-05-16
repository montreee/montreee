package de.contentup.montreee.modules.webui.app.ui

import de.contentup.montreee.module.runIfDevelopmentIsDisabled

object StaticLinks {
    object JS {
        object Lib {
            val JQuery = "ui/js/lib/jquery.js".useMinIfDevelopmentIsDisabled()
            val CoreUI = "ui/js/lib/coreui.js".useMinIfDevelopmentIsDisabled()
            val ChartJS = "ui/js/lib/chart.js".useMinIfDevelopmentIsDisabled()
            val CoreUIChartsJS = "ui/js/lib/coreui-chartjs.js".useMinIfDevelopmentIsDisabled()
            val Pace = "ui/js/lib/pace.js".useMinIfDevelopmentIsDisabled()
            val IfBreakpoint = "ui/js/lib/if-breakpoint.js".useMinIfDevelopmentIsDisabled()
        }

        val ChartJSResizeFix = "ui/js/chartjs-resize-fix.js".useMinIfDevelopmentIsDisabled()
        val Before = "ui/js/before.js".useMinIfDevelopmentIsDisabled()
        val Sidebar = "ui/js/sidebar.js".useMinIfDevelopmentIsDisabled()
        val Views = "ui/js/views.js".useMinIfDevelopmentIsDisabled()
        val ApiButtons = "ui/js/api-buttons.js".useMinIfDevelopmentIsDisabled()
    }

    object CSS {
        val ChartJS = "ui/css/chart.css".useMinIfDevelopmentIsDisabled()
        val CoreUIChartsJS = "ui/css/coreui-chartjs.css".useMinIfDevelopmentIsDisabled()
        val Pace = "ui/css/pace.css".useMinIfDevelopmentIsDisabled()
        val Fontawesome = "ui/css/fontawesome.css".useMinIfDevelopmentIsDisabled()
        val Montreee = "ui/css/montreee.css".useMinIfDevelopmentIsDisabled()
    }

    object Assets {
        val MontreeeLogo = "ui/assets/brand/logo.svg"

        object Icons {
            val FreeSymboleDefs = "ui/assets/icons/free-symbol-defs.svg"
        }
    }
}

fun String.useMinIfDevelopmentIsDisabled() = runIfDevelopmentIsDisabled { replace(".", ".min.") }
