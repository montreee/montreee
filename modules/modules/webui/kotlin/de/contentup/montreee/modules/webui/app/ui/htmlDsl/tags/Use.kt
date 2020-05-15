package de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags

import kotlinx.html.*

open class USE(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) : HTMLTag(
        "use", consumer, initialAttributes, null, false, false
)

@HtmlTagMarker
fun Tag.use(classes: String? = null, block: USE.() -> Unit = {}): Unit =
        USE(attributesMapOf("class", classes), consumer).visit(block)
