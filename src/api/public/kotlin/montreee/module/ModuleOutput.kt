package montreee.module

import montreee.result.ResultElement

class ModuleOutput(private val resultList: MutableList<ResultElement>) {

    fun append(vararg element: ResultElement) {
        element.forEach {
            resultList.add(it)
        }
    }

}
