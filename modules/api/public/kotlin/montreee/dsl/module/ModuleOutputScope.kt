package montreee.dsl.module

import montreee.module.Module
import montreee.module.phases.RenderContext
import montreee.render.ModuleRenderer
import montreee.result.HTMLResultElement
import montreee.result.HTMLResultElementTagConsumer
import montreee.result.ResultElement
import montreee.result.StringResultElement

@ModuleDsl
class ModuleOutputScope(private val renderContext: RenderContext) {
    private val context get() = renderContext.context
    private val out get() = renderContext.out
    private val page get() = renderContext.page

    @ModuleDsl
    fun append(element: ResultElement) = out.append(element)

    @ModuleDsl
    fun string(string: String) = StringResultElement(string)

    @ModuleDsl
    fun buildHTML(block: HTMLResultElementTagConsumer.() -> Unit) = HTMLResultElement(block)

    @ModuleDsl
    fun Module.render() = ModuleRenderer(context, page, this@render)()

    @ModuleDsl
    operator fun ResultElement.unaryPlus() = append(this)

    @ModuleDsl
    operator fun String.unaryPlus() = append(string(this))
}
