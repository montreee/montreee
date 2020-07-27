package montreee.render

import amber.result.Result
import montreee.context.Context
import montreee.data.DataRepository
import montreee.data.DataStore
import montreee.events.page
import montreee.page.Page
import montreee.result.ResultElement

class Renderer(private val context: Context) {

    private val pages: DataStore<Pair<Page, Map<String, Any?>>> = DataStore(DataRepository())

    fun createPage(id: String, input: Map<String, Any?>) {
        pages[id] = Pair(Page().apply { context.events.page.create.fire(this) }, input)
    }

    operator fun invoke(): Map<String, Result<ResultElement, Throwable>> {
        return (pages.map {
            context.events.page.before.fire(it.value.first)
            val pageRenderer = PageRenderer(context, it.value.first, it.value.second)
            val pageRenderResult = pageRenderer.invoke()
            val result = Pair(it.key, pageRenderResult)
            context.events.page.after.fire(it.value.first)
            result
        }).toMap()
    }
}
