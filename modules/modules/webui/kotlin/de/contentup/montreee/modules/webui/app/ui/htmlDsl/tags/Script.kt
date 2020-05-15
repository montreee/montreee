package de.contentup.montreee.modules.webui.app.ui.htmlDsl.tags

import kotlinx.html.*
import kotlinx.html.SCRIPT as OriginalScriptTag

open class SCRIPT(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) : OriginalScriptTag(
        initialAttributes, consumer
), CommonAttributeGroupFacade

fun <T> TagConsumer<T>.script(type: String = "text/javascript", src: String? = null, content: String = "") =
        OriginalScriptTag(attributesMapOf("type", type, "src", src), this).visit { unsafe { +content } }

fun Tag.script(type: String = "text/javascript", src: String? = null, content: String = ""): Unit =
        consumer.script(type, src, content)
