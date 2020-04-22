package montreee.parameter

import montreee.data.DataRepository
import montreee.data.DataStore

class ParameterFactoryDataStore(impl: DataRepository<ParameterFactory<*>>) : DataStore<ParameterFactory<*>>(impl) {
    fun put(element: ParameterFactory<*>) = set(element.name, element)
    operator fun ParameterFactory<*>.unaryPlus() = put(this)
}
