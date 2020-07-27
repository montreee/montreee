package de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags

import kotlinx.html.TagConsumer

fun <T> TagConsumer<T>.html5Doctype() {
    onTagContentUnsafe {
        +"<!DOCTYPE html>\n"
    }
}
