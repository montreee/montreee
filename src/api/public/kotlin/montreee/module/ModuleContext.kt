package montreee.module

import montreee.data.DataRepository
import montreee.data.DataStore
import montreee.page.Page
import montreee.parameter.ParameterStore
import montreee.result.ResultElement
import montreee.result.ResultList

open class ModuleContext(
        val context: montreee.context.Context,
        val page: Page,
        val parameter: ParameterStore,
        val data: DataStore<Any> = DataStore(DataRepository()),
        private val out: MutableList<ResultElement> = mutableListOf()
) {

    val result: ResultElement = ResultList(out)
}
