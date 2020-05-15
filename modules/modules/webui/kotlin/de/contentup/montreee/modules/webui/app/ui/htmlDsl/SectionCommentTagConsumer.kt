package de.contentup.montreee.modules.webui.app.ui.htmlDsl

import kotlinx.html.Tag
import kotlinx.html.TagConsumer

fun <T> TagConsumer<T>.sectionCommentsConsumer(
        enabled: Boolean = true, block: SectionCommentTagConsumer<T>.() -> Unit = {}
): SectionCommentTagConsumer<T> {
    return SectionCommentTagConsumer(enabled, this).apply(block)
}

class SectionCommentTagConsumer<T>(
        val sectionCommentEnabled: Boolean = true, delegate: TagConsumer<T>
) : TagConsumer<T> by delegate

fun <T : Tag> T.sectionComment(text: String, block: T.() -> Unit) {
    try {
        if (consumer !is SectionCommentTagConsumer || !(consumer as SectionCommentTagConsumer).sectionCommentEnabled) {
            block()
            return
        }

        consumer.onTagComment(" $text ")
        block()
        consumer.onTagComment("/ $text ")

    } catch (e: ClassCastException) {
        block()
    }
}

fun <T : Tag> T.comment(text: String, block: T.() -> Unit) = sectionComment(text, block)

fun <T> TagConsumer<T>.sectionComment(text: String, block: TagConsumer<T>.() -> Unit) {
    try {
        if (this !is SectionCommentTagConsumer || !sectionCommentEnabled) {
            block()
            return
        }

        this.onTagComment(" $text ")
        block()
        this.onTagComment("/ $text ")

    } catch (e: ClassCastException) {
        block()
    }
}

fun <T> TagConsumer<T>.comment(text: String, block: TagConsumer<T>.() -> Unit) = sectionComment(text, block)
