package montreee.result

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import org.w3c.dom.events.Event

class HTMLResultElement(private val builder: HTMLResultElementTagConsumer.() -> Unit) : ResultElement() {

    var elements = ResultList(mutableListOf())
        private set

    init {
        elements = HTMLResultElementTagConsumer().apply(builder).finalize()
    }

    override fun read() = elements.read()
}

data class HTMLTagResultElement(
        val name: String,
        val attributes: MutableMap<String, String>,
        val childes: MutableList<ResultElement> = mutableListOf()
) : ResultElement() {

    private fun hasAttributes() = attributes.isNotEmpty()

    private fun renderAttributes() = buildString {
        attributes.entries.forEachIndexed { i, it ->
            append(" ")
            append(it.key)
            append("=")
            append("\"${it.value}\"")
        }
    }


    private fun renderChildes() = buildString {
        childes.forEach { append(it.read()) }
    }

    override fun read(): String {
        return "<$name${if (hasAttributes()) renderAttributes() else ""}>${renderChildes()}</$name>"
    }

    override fun toString() = read()
}

class HTMLResultElementTagConsumer : TagConsumer<ResultList> {

    private val ret = ResultList(mutableListOf())

    private val path: MutableList<HTMLTagResultElement> = mutableListOf()

    private fun current() = path.last()

    private fun push(element: HTMLTagResultElement) = path.add(element)

    private fun pop() = path.remove(current())

    fun add(element: ResultElement) {
        if (path.isEmpty()) {
            ret.add(element)
            return
        }

        current().childes.add(element)
    }

    operator fun ResultElement.unaryPlus() = add(this)

    override fun onTagStart(tag: Tag) {
        val element = HTMLTagResultElement(tag.tagName, tag.attributes)
        add(element)
        push(element)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        throw UnsupportedOperationException(
                "tag attribute can't be changed as it was already written to the stream. Use with DelayedConsumer to be able to modify attributes"
        )
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        throw UnsupportedOperationException("you can't assign lambda event handler when building text")
    }

    override fun onTagEnd(tag: Tag) {
        pop()
    }

    override fun onTagContent(content: CharSequence) {
        add(StringResultElement(content.toString()))
    }

    override fun onTagContentEntity(entity: Entities) {
        throw UnsupportedOperationException()
    }

    override fun finalize() = ret

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        UnsafeImpl.block()
    }

    override fun onTagComment(content: CharSequence) {
        add(StringResultElement("<!--$content-->"))
    }

    val UnsafeImpl = object : Unsafe {
        override operator fun String.unaryPlus() {
            add(StringResultElement(this))
        }
    }
}
