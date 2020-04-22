package montreee.module

import montreee.data.DataRepository
import montreee.data.DataStore

class ModuleFactoryDataStore internal constructor(impl: DataRepository<ModuleFactory>) : DataStore<ModuleFactory>(impl) {
    fun put(element: ModuleFactory) = set(element.name, element)
}
