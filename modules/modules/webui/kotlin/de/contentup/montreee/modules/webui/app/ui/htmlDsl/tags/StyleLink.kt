package de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags

import kotlinx.html.*

fun <T> TagConsumer<T>.styleLink(url: String): Unit =
        LINK(attributesMapOf("href", url, "rel", LinkRel.stylesheet, "type", LinkType.textCss), this).visit {}

fun Tag.styleLink(url: String): Unit = consumer.styleLink(url)
