package de.contentup.montreee.modules.webui.app

import de.contentup.montreee.module.runIfDevelopmentIsDisabled

object StaticLinks {
    object JS {
        object Lib {
            val JQuery = "js/lib/jquery.js".useMinIfDevelopmentIsDisabled()
            val CoreUI = "js/lib/coreui.js".useMinIfDevelopmentIsDisabled()
            val ChartJS = "js/lib/chart.js".useMinIfDevelopmentIsDisabled()
            val CoreUIChartsJS = "js/lib/coreui-chartjs.js".useMinIfDevelopmentIsDisabled()
            val Pace = "js/lib/pace.js".useMinIfDevelopmentIsDisabled()
            val IfBreakpoint = "js/lib/if-breakpoint.js".useMinIfDevelopmentIsDisabled()
        }

        val ChartJSResizeFix = "js/chartjs-resize-fix.js".useMinIfDevelopmentIsDisabled()
        val Before = "js/before.js".useMinIfDevelopmentIsDisabled()
        val Sidebar = "js/sidebar.js".useMinIfDevelopmentIsDisabled()
        val Views = "js/views.js".useMinIfDevelopmentIsDisabled()
    }

    object CSS {
        val ChartJS = "css/chart.css".useMinIfDevelopmentIsDisabled()
        val CoreUIChartsJS = "css/coreui-chartjs.css".useMinIfDevelopmentIsDisabled()
        val Pace = "css/pace.css".useMinIfDevelopmentIsDisabled()
        val Fontawesome = "css/fontawesome.css".useMinIfDevelopmentIsDisabled()
        val Montreee = "css/montreee.css".useMinIfDevelopmentIsDisabled()
    }

    object Assets {
        val MontreeeLogo = "assets/brand/logo.svg"

        object Icons {
            val FreeSymboleDefs = "assets/icons/free-symbol-defs.svg"
        }
    }
}

fun String.useMinIfDevelopmentIsDisabled() = runIfDevelopmentIsDisabled { replace(".", ".min.") }
